import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Client {
    public static final String PROXI_ADDRES = "proxi";

    public static void main(String[] args) {
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket requester = context.socket(SocketType.REQ);
        requester.connect()
        Scanner sc = new java.util.Scanner(System.in);
        while (true) {
            ExecuteCommand(sc.nextLine());
        }
    }

    private static void ExecuteCommand(String commandLine) {
        String[] words = commandLine.split(" ");
        String commandName = words[0];
        if (commandName.toLowerCase().equals("get")) {
            if (words.length != 2) {
                System.out.println("Incorrect command format: GET id");
            } else {
                int id = Integer.parseInt(words[1]);
                sendGetRequest(id);
            }
        }
        if (commandName.toLowerCase().equals("put")) {
            if (words.length != 3) {
                System.out.println("Incorrect command format: PUT id value");
            } else {
                int id = Integer.parseInt(words[1]);
                int value = Integer.parseInt(words[2]);
                sendPutRequest(id, value);
            }
        }
    }

    private static void sendGetRequest(int id) {

    }

    private static void sendPutRequest(int id, int value) {

    }
}
