import javafx.util.Pair;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.HashMap;
import java.util.HashSet;


public class Cache {
    private static void sendNotifyRequest (ZMQ.Socket socket, String port) {
        socket.send(ParseUtils.buildNotifyRequest(port, System.currentTimeMillis()));
    }

    private static void sendConnectRequest (ZMQ.Socket socket, String port, int minKey, int maxKey) {
        socket.send(ParseUtils.buildConnectRequest(port, minKey, maxKey));
    }

    public static void main(String[] args) {
        if (args.length != 3 ||
                ParseUtils.getCommandType(args[0]+args[1]+args[2]) != ParseUtils.CommandType.RUN_CACHE) {
            System.out.println("incorrect command-line arguments");
            System.exit(-1);
        }

        HashMap<Integer, Integer> cache = new HashMap<>();

        String port = args[0];
        int minKey = Integer.parseInt(args[1]);
        int maxKey = Integer.parseInt(args[2]);

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket dealer = context.socket(SocketType.DEALER);
        dealer.connect(Proxi.CLIENT_ROUTER_ADDRES);

        while (!Thread.currentThread().isInterrupted()) {
            ZMsg msg = ZMsg.recvMsg(dealer, false);

            if (msg != null) {
                String command = new String(msg.getLast().getData(), ZMQ.CHARSET);
                ParseUtils.CommandType commandType = ParseUtils.getCommandType(command);

                if (commandType == ParseUtils.CommandType.GET) {
                    int id = ParseUtils.getKey(command);
                }

                if (commandType == ParseUtils.CommandType.PUT) {
                    Pair<Integer, Integer> pair = ParseUtils.getKeyValue(command);
                    cache.put(pair.getKey(), pair.getValue());
                    msg.destroy();
                }
            }
        }

    }

}
