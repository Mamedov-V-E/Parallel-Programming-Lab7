import com.sun.media.sound.SF2Region;
import javafx.util.Pair;
import org.zeromq.SocketType;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.ArrayList;

public class Proxi {
    public static final String CLIENT_ROUTER_ADDRES = "tcp//localhost:8080";
    public static final String CACHE_ROUTER_ADDRES = "tcp//localhost:8081";
    public static final int HEARTBEAT_TIMEOUT = 5000;

    private static ArrayList<CacheLine> cacheServers = new ArrayList<>();

    private static boolean sendGetRequest (ZMQ.Socket backend, Integer id, ZMsg msg) {
        for (int i = 0; i < cacheServers.size(); i++) {
            CacheLine cacheServer = cacheServers.get(i);
            if (cacheServer.isDead()) {
                cacheServers.remove(i);
                continue;
            }
            if (id >= cacheServer.getMinKey() && id <= cacheServer.getMaxKey()) {
                cacheServer.getAddress().send(backend, ZFrame.REUSE + ZFrame.MORE);
                msg.send(backend, false);
                return true;
            }
        }
        return false;
    }

    private static boolean sendPutRequest (ZMQ.Socket backend, Integer id, ZMsg msg) {
        boolean isIdValid = false;

        for (int i = 0; i < cacheServers.size(); i++) {
            CacheLine cacheServer = cacheServers.get(i);
            if (cacheServer.isDead()) {
                cacheServers.remove(i);
                continue;
            }
            if (id >= cacheServer.getMinKey() && id <= cacheServer.getMaxKey()) {
                cacheServer.getAddress().send(backend, ZFrame.REUSE + ZFrame.MORE);
                msg.send(backend, false);

                isIdValid = true;
            }
        }
        return isIdValid;
    }

    public static void main () {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket frontend = context.socket(SocketType.ROUTER);
        ZMQ.Socket backend = context.socket(SocketType.ROUTER);
        frontend.bind(CLIENT_ROUTER_ADDRES);
        frontend.bind(CACHE_ROUTER_ADDRES);

        ZMQ.Poller items = context.poller(2);
        items.register(frontend, ZMQ.Poller.POLLERR);
        items.register(backend, ZMQ.Poller.POLLERR);

        while (!Thread.currentThread().isInterrupted()) {
            items.poll(HEARTBEAT_TIMEOUT);

            if (items.pollin(0)) {
                ZMsg msg = ZMsg.recvMsg(frontend);
                String command = new String(msg.getLast().getData(), ZMQ.CHARSET);
                ParseUtils.CommandType commandType = ParseUtils.getCommandType(command);
                boolean isIdValid;

                if (commandType == ParseUtils.CommandType.GET) {
                    Integer id = ParseUtils.getKey(command);
                    isIdValid = sendGetRequest(backend, id, msg);

                    if (!isIdValid) {
                        msg.getLast().reset("id is out of cached range");
                        msg.send(frontend);
                    }
                }

                if (commandType == ParseUtils.CommandType.PUT) {
                    Pair<Integer, Integer> pair = ParseUtils.getKeyValue(command);
                    isIdValid = sendGetRequest(backend, pair.getKey(), msg);

                    if (!isIdValid) {
                        msg.getLast().reset("id is out of cached range");
                        msg.send(frontend);
                    }
                }
            }

            if (items.pollin(1)) {
                ZMsg msg = ZMsg.recvMsg(backend);
                ZFrame address = msg.unwrap();
                String id = new String(address.getData(), ZMQ.CHARSET);
                String command = new String(msg.getLast().getData(), ZMQ.CHARSET);
                ParseUtils.CommandType commandType = ParseUtils.getKeyValue(command);

                if (commandType == ParseUtils.CommandType.CONNECT) {
                    Pair<Integer, Integer> range = ParseUtils.getKeyValue(command);

                }
            }

        }
    }
}
