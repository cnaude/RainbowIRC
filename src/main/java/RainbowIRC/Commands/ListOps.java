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
public class ListOps implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "[bot] [channel]";
    private final String desc = "List IRC user mask in auto-op list.";
    private final String name = "listops";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public ListOps(MyPlugin plugin) {
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
                if (plugin.ircBots.get(bot).opsList.containsKey(channelName)) {
                    sender.sendMessage(ChatColor.DARK_PURPLE + "-----[  " + ChatColor.WHITE + channelName
                            + ChatColor.DARK_PURPLE + " - " + ChatColor.WHITE + "Auto Op Masks" + ChatColor.DARK_PURPLE + " ]-----");
                    for (String userMask : plugin.ircBots.get(bot).opsList.get(channelName)) {
                        sender.sendMessage(" - " + userMask);
                    }
                } else {
                    sender.sendMessage(plugin.invalidChannel.replace("%CHANNEL%", channelName));
                }
            } else {
                sender.sendMessage(plugin.invalidBotName.replace("%BOT%", bot));
            }
        } else {
            sender.sendMessage(fullUsage);
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
