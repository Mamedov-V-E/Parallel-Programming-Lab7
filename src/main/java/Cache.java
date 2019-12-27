public class Cache {

    public static void main(String[] args) {
        if (args.length != 3 ||
                ParseUtils.getCommandType(args[0]+args[1]+args[2]) != ParseUtils.CommandType.RUN_CACHE) {
            System.out.println("incorrect command-line arguments");
            System.exit(-1);
        }

        int port = Integer.parseInt(args[0]);
        int minKey = Integer.parseInt(args[1]);
        int maxKey = Integer.parseInt(args[2]);
        
    }
}
