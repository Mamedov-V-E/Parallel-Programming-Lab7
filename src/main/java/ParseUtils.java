import java.util.regex.Pattern;

public class ParseUtils {
    public static final Pattern GET_COMMAND_PATTERN = Pattern.compile("^GET \\d+$", Pattern.CASE_INSENSITIVE);
    public static final Pattern PUT_COMMAND_PATTERN = Pattern.compile("^PUT \\d+ \\d+$", Pattern.CASE_INSENSITIVE);
    public static final Pattern RETURN_VALUE_COMMAND_PATTERN = Pattern.compile("^RETURN_VALUE \\d+$", Pattern.CASE_INSENSITIVE);
    public static final Pattern CONNECT_COMMAND_PATTERN = Pattern.compile("^CONNECT \\d+ \\d+$", Pattern.CASE_INSENSITIVE);
    public static final Pattern NOTIFY_COMMAND_PATTERN = Pattern.compile("^NOTIFY$", Pattern.CASE_INSENSITIVE);
    public static final Pattern OK_COMMAND_PATTERN = Pattern.compile("^OK$", Pattern.CASE_INSENSITIVE);
    public static final Pattern RUN_CACHE_PATTERN = Pattern.compile("^\\d+ \\d+$");
    public static final String DELIMITER = " ";

    public enum CommandType {
        GET,
        PUT,
        RUN_CACHE,
        RETURN_VALUE,
        CONNECT,
        NOTIFY,
        INVALID,
        OK
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
        if (RETURN_VALUE_COMMAND_PATTERN.matcher(command).find()) {
            return CommandType.RETURN_VALUE;
        }
        if (CONNECT_COMMAND_PATTERN.matcher(command).find()) {
            return CommandType.CONNECT;
        }
        if (NOTIFY_COMMAND_PATTERN.matcher(command).find()) {
            return CommandType.NOTIFY;
        }
        if (OK_COMMAND_PATTERN.matcher(command).find()) {
            return CommandType.OK;
        }
        return CommandType.INVALID;
    }

    public static String buildConnectRequest (Integer minKey, Integer maxKey) {
        return "CONNECT " + minKey + " " + maxKey;
    }

    public static String buildNotifyRequest () {
        return "NOTIFY";
    }

    public static String buildOkResponse () {
        return "OK";
    }

    public static Integer[] getKeyValue(String putCommand) {
        String[] words = putCommand.split(DELIMITER);
        return new Integer[] {Integer.parseInt(words[1]), Integer.parseInt(words[2])};
    }

    public static Integer getKey (String getCommand) {
        String[] words = getCommand.split(DELIMITER);
        return Integer.parseInt(words[1]);
    }

    public static String buildReturnValueResponse (Integer value) {
        return "RETURN_VALUE " + value;
    }
}
