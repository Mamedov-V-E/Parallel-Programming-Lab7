import javafx.util.Pair;

import java.util.regex.Pattern;

public class ParseUtils {
    public static final Pattern GET_COMMAND_PATTERN = Pattern.compile("^GET \\d+&", Pattern.CASE_INSENSITIVE);
    public static final Pattern PUT_COMMAND_PATTERN = Pattern.compile("^PUT \\d+ \\d+&", Pattern.CASE_INSENSITIVE);
    public static final Pattern RETURN_VALUE_COMMAND_PATTERN = Pattern.compile("^RETURN_VALUE \\d+&", Pattern.CASE_INSENSITIVE);
    public static final Pattern CONNECT_COMMAND_PATTERN = Pattern.compile("^CONNECT&", Pattern.CASE_INSENSITIVE);
    public static final Pattern NOTIFY_COMMAND_PATTERN = Pattern.compile("^NOTIFY+&", Pattern.CASE_INSENSITIVE);
    public static final Pattern RUN_CACHE_PATTERN = Pattern.compile("^\\d+ \\d+&");
    public static final String DELIMITER = " ";

    public enum CommandType {
        GET,
        PUT,
        RUN_CACHE,
        RETURN_VALUE,
        CONNECT,
        NOTIFY,
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
        if (RETURN_VALUE_COMMAND_PATTERN.matcher(command).find()) {
            return CommandType.RETURN_VALUE;
        }
        if (CONNECT_COMMAND_PATTERN.matcher(command).find()) {
            return CommandType.CONNECT;
        }
        if (NOTIFY_COMMAND_PATTERN.matcher(command).find()) {
            return CommandType.NOTIFY;
        }
        return CommandType.INVALID;
    }

    public static String buildConnectRequest (String port, Integer minKey, Integer maxKey) {
        return "CONNECT " + port + " " + minKey + " " + maxKey;
    }

    public static String buildNotifyRequest () {
        return "NOTIFY";
    }

    public static Pair<Integer, Integer> getKeyValue(String putCommand) {
        String[] words = putCommand.split(DELIMITER);
        return new Pair<>(Integer.parseInt(words[1]), Integer.parseInt(words[2]));
    }

    public static Integer getKey (String getCommand) {
        String[] words = getCommand.split(DELIMITER);
        return Integer.parseInt(words[1]);
    }

    public static String buildReturnValueResponse (Integer value) {
        return "RETURN_VALUE " + value;
    }
}
