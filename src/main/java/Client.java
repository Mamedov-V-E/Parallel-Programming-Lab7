import javafx.util.Pair;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket requester = context.socket(SocketType.REQ);
        requester.connect(Proxi.CLIENT_ROUTER_ADDRES);

        System.out.println("launch and connect client");

        Scanner sc = new java.util.Scanner(System.in);
        while (!Thread.currentThread().isInterrupted()) {
            ExecuteCommand(requester, sc.nextLine());
            String response = requester
        }
    }

    private static void ExecuteCommand(ZMQ.Socket requester, String commandLine) {
        if (ParseUtils.getCommandType(commandLine) == ParseUtils.CommandType.GET ||
                ParseUtils.getCommandType(commandLine) == ParseUtils.CommandType.PUT) {
            requester.send(commandLine);
        } else {
            System.out.println("invalid command");
        }
    }
}
