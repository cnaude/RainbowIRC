/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowIRC.Commands;

import RainbowIRC.Utilities.BotsAndChannels;
import RainbowIRC.MyPlugin;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public class MuteList implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "([bot]) ([channel])";
    private final String desc = "List muted IRC user(s) for a channel.";
    private final String name = "mutelist";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage;

    /**
     *
     * @param plugin
     */
    public MuteList(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        BotsAndChannels bac;

        if (args.length >= 3) {
            bac = new BotsAndChannels(plugin, sender, args[1], args[2]);
        } else if (args.length == 1) {
            bac = new BotsAndChannels(plugin, sender);
        } else {
            sender.sendMessage(fullUsage);
            return;
        }
        if (bac.bot.size() > 0 && bac.channel.size() > 0) {
            for (String botName : bac.bot) {
                for (String channelName : bac.channel) {
                    plugin.ircBots.get(botName).muteList(channelName, sender);
                }
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
