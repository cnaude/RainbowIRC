package RainbowIRC.Utilities;

import PluginReference.ChatColor;
import RainbowIRC.MyPlugin;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.pircbotx.Colors;

/**
 *
 * @author cnaude
 */
public class ColorConverter {

    MyPlugin plugin;
    private final boolean stripGameColors;
    private final boolean stripIRCColors;
    private final boolean stripIRCBackgroundColors;
    private final Map<String, String> ircColorMap = new HashMap<>();
    private final Map<String, String> gameColorMap = new HashMap<>();
    private final Pattern bgColorPattern;
    private final Pattern singleDigitColorPattern;
    private final Pattern colorHack;

    /**
     *
     * @param plugin
     * @param stripGameColors
     * @param stripIRCColors
     * @param stripIRCBackgroundColors
     */
    public ColorConverter(MyPlugin plugin, boolean stripGameColors, boolean stripIRCColors, boolean stripIRCBackgroundColors) {
        this.stripGameColors = stripGameColors;
        this.stripIRCColors = stripIRCColors;
        this.stripIRCBackgroundColors = stripIRCBackgroundColors;
        this.plugin = plugin;
        buildDefaultColorMaps();
        this.bgColorPattern = Pattern.compile("((\\u0003\\d+),\\d+)");
        this.singleDigitColorPattern = Pattern.compile("((\\u0003)(\\d))\\D+");
        this.colorHack = Pattern.compile("((\\u0003\\d+)(,\\d+))\\D");
    }

    /**
     *
     * @param message
     * @return
     */
    public String gameColorsToIrc(String message) {
        if (stripGameColors) {
            return stripColor(message);
        } else {
            String newMessage = message;
            for (String gameColor : ircColorMap.keySet()) {
                newMessage = newMessage.replace(gameColor, ircColorMap.get(gameColor));
            }
            // We return the message with the remaining MC color codes stripped out
            return stripColor(newMessage);
        }
    }

    public String stripColor(String message) {
        return message.replaceAll("§\\w", "");
    }

    public String translateAlternateColorCodes(char code, String message) {        
        Pattern p = Pattern.compile(code + "(\\w)");
        Matcher m = p.matcher(message);
        while (m.find()) {
            message = message.replace(code + m.group(1) , "§" + m.group(1));
        }
        return message;
    }

    /**
     *
     * @param message
     * @return
     */
    public String ircColorsToGame(String message) {
        Matcher matcher;
        if (stripIRCBackgroundColors) {
            matcher = bgColorPattern.matcher(message);
            while (matcher.find()) {
                plugin.logDebug("Strip bg color: " + matcher.group(1) + " => " + matcher.group(2));
                message = message.replace(matcher.group(1), matcher.group(2));
            }
        }
        matcher = singleDigitColorPattern.matcher(message);
        while (matcher.find()) {
            plugin.logDebug("Single to double: " + matcher.group(3) + " => "
                    + matcher.group(2) + "0" + matcher.group(3));
            // replace \u0003N with \u00030N
            message = message.replace(matcher.group(1), matcher.group(2) + "0" + matcher.group(3));
        }
        matcher = colorHack.matcher(message);
        while (matcher.find()) {
            plugin.logDebug("Silly IRC colors: " + matcher.group(1) + " => "
                    + matcher.group(2));
            // replace \u0003N,N with \u00030N
            message = message.replace(matcher.group(1), matcher.group(2));
        }

        if (stripIRCColors) {
            return Colors.removeFormattingAndColors(message);
        } else {
            String newMessage = message;
            for (String ircColor : gameColorMap.keySet()) {
                newMessage = newMessage.replace(ircColor, gameColorMap.get(ircColor));
            }
            // We return the message with the remaining IRC color codes stripped out
            return Colors.removeFormattingAndColors(newMessage);
        }
    }

    public void addIrcColorMap(String gameColor, String ircColor) {
        String chatColor;
        try {
            chatColor = gameColor.toUpperCase();
        } catch (Exception ex) {
            plugin.logError("Invalid game color: " + gameColor);
            return;
        }
        if (chatColor != null) {
            plugin.logDebug("addIrcColorMap: " + gameColor + " => " + ircColor);
            ircColorMap.put(chatColor, getIrcColor(ircColor));
        }
    }

    public void addGameColorMap(String ircColor, String gameColor) {
        String chatColor;
        try {
            chatColor = gameColor.toUpperCase();
        } catch (Exception ex) {
            plugin.logError("Invalid game color: " + gameColor);
            return;
        }
        plugin.logDebug("addGameColorMap: " + ircColor + " => " + gameColor);
        gameColorMap.put(getIrcColor(ircColor), chatColor);
    }

    private String getIrcColor(String ircColor) {
        String s = "";
        try {
            s = (String) Colors.class.getField(ircColor.toUpperCase()).get(null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            plugin.logError(ex.getMessage());
        }
        if (s.isEmpty()) {
            plugin.logError("Invalid IRC color: " + ircColor);
        }
        return s;
    }

    private void buildDefaultColorMaps() {
        ircColorMap.put(ChatColor.AQUA, Colors.CYAN);
        ircColorMap.put(ChatColor.BLACK, Colors.BLACK);
        ircColorMap.put(ChatColor.BLUE, Colors.BLUE);
        ircColorMap.put(ChatColor.BOLD, Colors.BOLD);
        ircColorMap.put(ChatColor.DARK_AQUA, Colors.TEAL);
        ircColorMap.put(ChatColor.DARK_BLUE, Colors.DARK_BLUE);
        ircColorMap.put(ChatColor.DARK_GRAY, Colors.DARK_GRAY);
        ircColorMap.put(ChatColor.DARK_GREEN, Colors.DARK_GREEN);
        ircColorMap.put(ChatColor.DARK_PURPLE, Colors.PURPLE);
        ircColorMap.put(ChatColor.DARK_RED, Colors.RED);
        ircColorMap.put(ChatColor.GOLD, Colors.OLIVE);
        ircColorMap.put(ChatColor.GRAY, Colors.LIGHT_GRAY);
        ircColorMap.put(ChatColor.GREEN, Colors.GREEN);
        ircColorMap.put(ChatColor.LIGHT_PURPLE, Colors.MAGENTA);
        ircColorMap.put(ChatColor.RED, Colors.RED);
        ircColorMap.put(ChatColor.UNDERLINE, Colors.UNDERLINE);
        ircColorMap.put(ChatColor.YELLOW, Colors.YELLOW);
        ircColorMap.put(ChatColor.WHITE, Colors.WHITE);
        ircColorMap.put(ChatColor.RESET, Colors.NORMAL);

        gameColorMap.put(Colors.BLACK, ChatColor.BLACK);
        gameColorMap.put(Colors.BLUE, ChatColor.BLUE);
        gameColorMap.put(Colors.BOLD, ChatColor.BOLD);
        gameColorMap.put(Colors.BROWN, ChatColor.GRAY);
        gameColorMap.put(Colors.CYAN, ChatColor.AQUA);
        gameColorMap.put(Colors.DARK_BLUE, ChatColor.DARK_BLUE);
        gameColorMap.put(Colors.DARK_GRAY, ChatColor.DARK_GRAY);
        gameColorMap.put(Colors.DARK_GREEN, ChatColor.DARK_GREEN);
        gameColorMap.put(Colors.GREEN, ChatColor.GREEN);
        gameColorMap.put(Colors.LIGHT_GRAY, ChatColor.GRAY);
        gameColorMap.put(Colors.MAGENTA, ChatColor.LIGHT_PURPLE);
        gameColorMap.put(Colors.NORMAL, ChatColor.RESET);
        gameColorMap.put(Colors.OLIVE, ChatColor.GOLD);
        gameColorMap.put(Colors.PURPLE, ChatColor.DARK_PURPLE);
        gameColorMap.put(Colors.RED, ChatColor.RED);
        gameColorMap.put(Colors.TEAL, ChatColor.DARK_AQUA);
        gameColorMap.put(Colors.UNDERLINE, ChatColor.UNDERLINE);
        gameColorMap.put(Colors.WHITE, ChatColor.WHITE);
        gameColorMap.put(Colors.YELLOW, ChatColor.YELLOW);
    }
}
