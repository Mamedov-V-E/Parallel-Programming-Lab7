import java.util.regex.Pattern;

public class ParseUtils {
    public static final Pattern GET_COMMAND_PATTERN = Pattern.compile("^GET \\d+&", Pattern.CASE_INSENSITIVE);
    public static final Pattern PUT_COMMAND_PATTERN = Pattern.compile("^PUT \\d+ \\d+&", Pattern.CASE_INSENSITIVE);
    public static final Pattern RUN_CACHE_PATTERN = Pattern.compile("^\\d+ \\d+ \\d+&");

    public enum CommandType {
        GET,
        PUT,
        RUN_CACHE,
        INVALID
    }

    public static CommandType getCommandType(String command) {
        if (GET_COMMAND_PATTERN.matcher(command).find()) {
            return CommandType.GET;
        }
        if (PUT_COMMAND_PATTERN.matcher(command).find()) {
            return CommandType.PUT;
        }
        if (RUN_CACHE_PATTERN.matcher(command).find()) {
            return CommandType.RUN_CACHE;
        }
        return CommandType.INVALID;
    }

    public static String buildConnectRequest (String port, int minKey, int maxKey) {
        return "CONNECT" + port + minKey + maxKey;
    }
}
