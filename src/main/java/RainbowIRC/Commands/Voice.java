package RainbowIRC.Commands;

import RainbowIRC.MyPlugin;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public class Voice implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "[bot] [channel] [user(s)]";
    private final String desc = "Voice an IRC user in a channel.";
    private final String name = "voice";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public Voice(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        if (args.length >= 4) {
            String bot = args[1];
            String channelName = args[2];
            if (plugin.ircBots.containsKey(bot)) {
                for (int i = 3; i < args.length; i++) {
                    // #channel, user
                    plugin.ircBots.get(bot).voice(channelName, args[i]);
                    plugin.sendMessage(sender, "Giving voice status to " 
                            + ChatColor.WHITE + args[i] 
                            + ChatColor.RESET + " on " 
                            + ChatColor.WHITE + channelName);
                }
            } else {
                plugin.sendMessage(sender, plugin.invalidBotName.replace("%BOT%", bot));
            }
        } else {
            plugin.sendMessage(sender, fullUsage);
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
