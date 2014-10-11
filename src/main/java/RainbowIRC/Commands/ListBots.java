/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowIRC.Commands;

import RainbowIRC.RainbowBot;
import RainbowIRC.MyPlugin;
import PluginReference.ChatColor;
import PluginReference.MC_Player;
import org.pircbotx.Channel;

/**
 *
 * @author cnaude
 */
public class ListBots implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "";
    private final String desc = "List IRC bots.";
    private final String name = "listbots";

    /**
     *
     * @param plugin
     */
    public ListBots(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        sender.sendMessage(ChatColor.DARK_PURPLE + "-----[  " + ChatColor.WHITE + "IRC Bots"
                + ChatColor.DARK_PURPLE + "   ]-----");
        for (RainbowBot ircBot : plugin.ircBots.values()) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "* " + ChatColor.WHITE + ircBot.botNick
            + ChatColor.DARK_PURPLE + " [" + ChatColor.GRAY + ircBot.getFileName() + ChatColor.DARK_PURPLE + "]");
            if (ircBot.isConnected()) {
                for (Channel channel : ircBot.getChannels()) {
                    sender.sendMessage(ChatColor.DARK_PURPLE + "  - " + ChatColor.WHITE + channel.getName());
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Not connected.");
            }
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
