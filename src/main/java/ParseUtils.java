import java.util.regex.Pattern;

public class ParseUtils {
    public static final Pattern GET_COMMAND_PATTERN = Pattern.compile("^GET \\d+&", Pattern.CASE_INSENSITIVE);
    public static final Pattern PUT_COMMAND_PATTERN = Pattern.compile("^PUT \\d+ \\d+&", Pattern.CASE_INSENSITIVE);
    public static final Pattern RUN_CACHE_PATTERN = Pattern.compile("^\\d+ \\d+ \\d+&");

    public enum CommandType {
        GET,
        PUT,
        INVALID
    }

    public static CommandType getCommandType(String command) {
        if (GET_COMMAND_PATTERN.matcher(command).find()) {
            return CommandType.GET;
        }
        if (PUT_COMMAND_PATTERN.matcher(command).find()) {
            return CommandType.PUT;
        }
        return CommandType.INVALID;
    }
}
