import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket requester = context.socket(SocketType.REQ);
        requester.connect(Proxi.CLIENT_ROUTER_ADDRES);

        System.out.println("client started");

        Scanner sc = new java.util.Scanner(System.in);
        while (!Thread.currentThread().isInterrupted()) {
            ExecuteCommand(requester, sc.nextLine());
        }
    }

    private static void ExecuteCommand(ZMQ.Socket requester, String commandLine) {
        if (ParseUtils.getCommandType(commandLine) == ParseUtils.CommandType.GET ||
                ParseUtils.getCommandType(commandLine) == ParseUtils.CommandType.PUT) {

            requester.send(commandLine);
            String response = requester.recvStr();

            System.out.println(response);
        } else {
            System.out.println("invalid command");
        }
    }
}
