import java.util.Scanner;

public class Client {
    public static final String PROXI_NAME = "proxi";

    public static void main(String[] args) {

        Scanner sc = new java.util.Scanner(System.in);
        while (true) {
            String commandLine = sc.nextLine();
            String[] words = commandLine.split(" ");
            String commandWord = words[0];
        }
    }
}
