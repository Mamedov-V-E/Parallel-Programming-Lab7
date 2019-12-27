import org.zeromq.SocketType;
import org.zeromq.ZMQ;


public class Cache {
    private static final String CACHE_ADDRESS = "tcp://localhost";

    private static void sendNotifyRequest (ZMQ.Socket socket, ) {

    }

    public static void main(String[] args) {
        if (args.length != 2 ||
                ParseUtils.getCommandType(args[0]+args[1]) != ParseUtils.CommandType.RUN_CACHE) {
            System.out.println("incorrect command-line arguments");
            System.exit(-1);
        }

        int minKey = Integer.parseInt(args[0]);
        int maxKey = Integer.parseInt(args[1]);

        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket dealer = context.socket(SocketType.DEALER);
        dealer.connect(CACHE_ADDRESS + ":" + port);
    }

}
