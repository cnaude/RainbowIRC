/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowIRC.Commands;

import RainbowIRC.MyPlugin;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public class Join implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "[bot] [channel] ([password])";
    private final String desc = "Join IRC channel.";
    private final String name = "join";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public Join(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        if (args.length >= 3) {
            String bot = args[1];
            String channelName = args[2];
            String password = "";
            if (args.length >= 4) {
                for (int i = 3; i < args.length; i++) {
                    password = password + " " + args[i];
                }
            }
            if (plugin.ircBots.containsKey(bot)) {
                plugin.ircBots.get(bot).asyncJoinChannel(channelName, password);
                plugin.sendMessage(sender, ChatColor.WHITE + "Joining " + channelName + "...");
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
