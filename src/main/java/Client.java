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
            ExecuteCommand(sc.nextLine());
        }
    }

    private static void ExecuteCommand(String commandLine) {
        if (ParseUtils.getCommandType(commandLine) == ParseUtils.CommandType.GET) {
            Integer id = ParseUtils.getKey(commandLine);
            sendGetRequest(id);
        }
        else if (ParseUtils.getCommandType(commandLine) == ParseUtils.CommandType.PUT) {
            Pair<Integer, Integer> pair = ParseUtils.getKeyValue(commandLine);
            Integer id = pair.getKey();
            Integer value = pair.getValue();
            sendPutRequest(id, value);
        } else {
            System.out.println("invalid command");
        }
    }

    private static void sendGetRequest(Integer id) {

    }

    private static void sendPutRequest(Integer id, Integer value) {

    }
}
