import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.ArrayList;

public class Proxi {
    public static final String CLIENT_ROUTER_ADDRES = "tcp//localhost:8080";
    public static final String CACHE_ROUTER_ADDRES = "tcp//localhost:8081";
    public static final int HEARTBEAT_TIMEOUT = 5000;

    public static void main () {
        ArrayList<CacheLine> cacheServers = new ArrayList<>();

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
            }
        }
    }
}
