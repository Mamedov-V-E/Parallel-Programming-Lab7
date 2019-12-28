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
            ExecuteCommand(sc.nextLine());
        }
    }

    private static void ExecuteCommand(String commandLine) {
        String[] words = commandLine.split(ParseUtils.DELIMITER);
        String commandName = words[0];
        if (ParseUtils.getCommandType(commandLine) == ParseUtils.CommandType.GET) {
            int id = Integer.parseInt(words[1]);
            sendGetRequest(id);
        }
        else if (ParseUtils.getCommandType(commandLine) == ParseUtils.CommandType.PUT) {

            int id = Integer.parseInt(words[1]);
            int value = Integer.parseInt(words[2]);
            sendPutRequest(id, value);
        } else {
            System.out.println("invalid command");
        }
    }

    private static void sendGetRequest(int id) {

    }

    private static void sendPutRequest(int id, int value) {

    }
}
