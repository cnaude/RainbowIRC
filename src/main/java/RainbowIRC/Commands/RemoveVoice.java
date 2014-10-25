package RainbowIRC.Commands;

import RainbowIRC.MyPlugin;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public class RemoveVoice implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "[bot] [channel] [user mask]";
    private final String desc = "Remove a user mask from the auto-voice list.";
    private final String name = "removevoice";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public RemoveVoice(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        if (args.length == 4) {
            String bot = args[1];
            String channel = args[2];
            if (plugin.ircBots.containsKey(bot)) {
                // #channel, user
                plugin.ircBots.get(bot).removeVoice(channel, args[3], sender);
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
