import java.util.Scanner;

public class Client {
    public static final String PROXI_NAME = "proxi";

    public static void main(String[] args) {

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
                System.out.println("");
            }
        }
    }
}
