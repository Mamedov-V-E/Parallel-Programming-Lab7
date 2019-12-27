import org.zeromq.SocketType;
import org.zeromq.ZMQ;


public class Cache {
    private static void sendNotifyRequest (ZMQ.Socket socket, ) {

    }

    private static void sendConnectRequest (ZMQ.Socket socket, String port, int minKey, int maxKey) {
        socket.send()
    }

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
        ZMQ.Socket dealer = context.socket(SocketType.DEALER);
        dealer.connect(Proxi.CLIENT_ROUTER_ADDRES);


    }

}
