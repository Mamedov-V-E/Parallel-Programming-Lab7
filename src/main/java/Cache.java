import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.net.Socket;

public class Cache {
    private static final String CACHE_ADDRESS = "tcp://localhost";

    public static void main(String[] args) {
        if (args.length != 3 ||
                ParseUtils.getCommandType(args[0]+args[1]+args[2]) != ParseUtils.CommandType.RUN_CACHE) {
            System.out.println("incorrect command-line arguments");
            System.exit(-1);
        }

        String port = args[0];
        int minKey = Integer.parseInt(args[1]);
        int maxKey = Integer.parseInt(args[2]);

        ZMQ.Context context = ZMQ.context(1);
        Socket dealer = context.socket(SocketType.DEALER);
        dealer.bind(CACHE_ADDRESS + ":" + port);
    }

}
