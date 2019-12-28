import javafx.util.Pair;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.HashMap;
import java.util.HashSet;


public class Cache {
    private static void sendNotifyRequest (ZMQ.Socket socket) {
        socket.send(ParseUtils.buildNotifyRequest());
    }

    private static void sendConnectRequest (ZMQ.Socket socket, int minKey, int maxKey) {
        socket.send(ParseUtils.buildConnectRequest(minKey, maxKey));
    }

    public static void main(String[] args) {
        if (args.length != 2 ||
                ParseUtils.getCommandType(args[0]+args[1]) != ParseUtils.CommandType.RUN_CACHE) {
            System.out.println("incorrect command-line arguments");
            System.exit(-1);
        }

        HashMap<Integer, Integer> cache = new HashMap<>();

        Integer minKey = Integer.parseInt(args[0]);
        Integer maxKey = Integer.parseInt(args[1]);

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket dealer = context.socket(SocketType.DEALER);
        dealer.connect(Proxi.CACHE_ROUTER_ADDRES);

        sendConnectRequest(dealer, minKey, maxKey);
        Long nextHearbeatTime = System.currentTimeMillis() + Proxi.HEARTBEAT_TIMEOUT;

        while (!Thread.currentThread().isInterrupted()) {
            ZMsg msg = ZMsg.recvMsg(dealer, false);

            if (msg != null) {
                String command = new String(msg.getLast().getData(), ZMQ.CHARSET);
                ParseUtils.CommandType commandType = ParseUtils.getCommandType(command);

                if (commandType == ParseUtils.CommandType.GET) {
                    Integer id = ParseUtils.getKey(command);
                    Integer value = cache.get(id);
                    String response = (value == null) ? "null" : value.toString();

                    msg.getLast().reset(ParseUtils.buildReturnValueResponse(value));
                    msg.send(dealer);
                }

                if (commandType == ParseUtils.CommandType.PUT) {
                    Pair<Integer, Integer> pair = ParseUtils.getKeyValue(command);
                    cache.put(pair.getKey(), pair.getValue());
                    msg.destroy();
                }
            }

            if (System.currentTimeMillis() >= nextHearbeatTime) {
                nextHearbeatTime = System.currentTimeMillis() + Proxi.HEARTBEAT_TIMEOUT;
                sendNotifyRequest(dealer);
            }
        }

        dealer.close();
        context.close();
    }

}
