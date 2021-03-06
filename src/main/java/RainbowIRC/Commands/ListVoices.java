package RainbowIRC.Commands;

import RainbowIRC.MyPlugin;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public class ListVoices implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "[bot] [channel]";
    private final String desc = "List IRC user mask in auto-voice list.";
    private final String name = "listvoices";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public ListVoices(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        if (args.length == 3) {
            String bot = args[1];
            String channelName = args[2];
            if (plugin.ircBots.containsKey(bot)) {
                if (plugin.ircBots.get(bot).voicesList.containsKey(channelName)) {
                    plugin.sendMessageToSender(sender, ChatColor.DARK_PURPLE + "-----[  " + ChatColor.WHITE + channelName
                            + ChatColor.DARK_PURPLE + " - " + ChatColor.WHITE + "Auto Voice Masks" + ChatColor.DARK_PURPLE + " ]-----");
                    for (String userMask : plugin.ircBots.get(bot).voicesList.get(channelName)) {
                        plugin.sendMessageToSender(sender, " - " + userMask);
                    }
                } else {
                    plugin.sendMessageToSender(sender, plugin.invalidChannel.replace("%CHANNEL%", channelName));
                }
            } else {
                plugin.sendMessageToSender(sender, plugin.invalidBotName.replace("%BOT%", bot));
            }
        } else {
            plugin.sendMessageToSender(sender, fullUsage);
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String desc() {
        return desc;
    }

    @Override
    public String usage() {
        return usage;
    }
}
