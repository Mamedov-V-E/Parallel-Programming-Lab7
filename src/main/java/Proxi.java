import org.zeromq.SocketType;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.ArrayList;

public class Proxi {
    public static final String CLIENT_ROUTER_ADDRESS = "tcp://localhost:8080";
    public static final String CACHE_ROUTER_ADDRESS = "tcp://localhost:8081";
    public static final int HEARTBEAT_TIMEOUT = 150;

    private static final ArrayList<CacheLine> cacheServers = new ArrayList<>();

    private static void updateHeartbeat(String id) {
        for (int i = 0; i < cacheServers.size(); i++) {
            CacheLine cacheServer = cacheServers.get(i);
            if (cacheServer.getId().equals(id)) {
                cacheServer.setHertbeatTime(System.currentTimeMillis());
            }
        }
    }

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
                System.out.println("found id among cache servers");
                cacheServer.getAddress().send(backend, ZFrame.REUSE + ZFrame.MORE);
                msg.send(backend, false);

                isIdValid = true;
            }
        }
        return isIdValid;
    }

    public static void main (String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket frontend = context.socket(SocketType.ROUTER);
        ZMQ.Socket backend = context.socket(SocketType.ROUTER);
        frontend.bind(CLIENT_ROUTER_ADDRESS);
        backend.bind(CACHE_ROUTER_ADDRESS);

        System.out.println("proxi started");

        ZMQ.Poller items = context.poller(2);
        items.register(frontend, ZMQ.Poller.POLLIN);
        items.register(backend, ZMQ.Poller.POLLIN);

        while (!Thread.currentThread().isInterrupted()) {
            items.poll(HEARTBEAT_TIMEOUT);

            //System.out.println("outside if");
            if (items.pollin(0)) {
                System.out.println("inside if1");
                ZMsg msg = ZMsg.recvMsg(frontend);
                String command = new String(msg.getLast().getData(), ZMQ.CHARSET);
                ParseUtils.CommandType commandType = ParseUtils.getCommandType(command);
                boolean isIdValid;

                if (commandType == ParseUtils.CommandType.GET) {
                    System.out.println("got GET command!");
                    Integer id = ParseUtils.getKey(command);
                    isIdValid = sendGetRequest(backend, id, msg);

                    if (!isIdValid) {
                        msg.getLast().reset("id is out of cached range");
                        msg.send(frontend);
                    }
                }

                if (commandType == ParseUtils.CommandType.PUT) {
                    Integer[] pair = ParseUtils.getKeyValue(command);
                    isIdValid = sendPutRequest(backend, pair[0], msg);

                    if (!isIdValid) {
                        msg.getLast().reset("id is out of cached range");
                        msg.send(frontend);
                    }
                }
            }

            if (items.pollin(1)) {
                //System.out.println("got command from cache server");
                ZMsg msg = ZMsg.recvMsg(backend);
                ZFrame address = msg.unwrap();
                String id = new String(address.getData(), ZMQ.CHARSET);
                String command = new String(msg.getLast().getData(), ZMQ.CHARSET);
                ParseUtils.CommandType commandType = ParseUtils.getCommandType(command);

                if (commandType == ParseUtils.CommandType.CONNECT) {
                    Integer[] range = ParseUtils.getKeyValue(command);

                    System.out.println("new server added to cache list");
                    cacheServers.add(new CacheLine(
                            id, address, range[0], range[1], System.currentTimeMillis()
                    ));
                }

                if (commandType == ParseUtils.CommandType.OK) {
                    msg.send(frontend);
                }
                if (commandType == ParseUtils.CommandType.NOTIFY) {
                    updateHeartbeat(id);
                }
                if (commandType == ParseUtils.CommandType.RETURN_VALUE) {
                    msg.send(frontend);
                }
            }

        }

        frontend.close();
        backend.close();
        context.close();
    }
}
