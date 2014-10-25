/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowIRC.Commands;

import RainbowIRC.MyPlugin;
import RainbowIRC.Utilities.BotsAndChannels;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public class Topic implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "([bot]) ([channel]) ([topic])";
    private final String desc = "Set, or get, IRC channel top";
    private final String name = "topic";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage;

    /**
     *
     * @param plugin
     */
    public Topic(MyPlugin plugin) {
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
        int idx;

        if (args.length >= 4) {
            bac = new BotsAndChannels(plugin, sender, args[1], args[2]);
            idx = 3;
        } else if (args.length == 2) {
            bac = new BotsAndChannels(plugin, sender);
            idx = 1;
        } else {
            bac = new BotsAndChannels(plugin, sender);
            for (String botName : bac.bot) {
                for (String channelName : bac.channel) {
                    plugin.sendMessageToSender(sender, ChatColor.WHITE + "[" + ChatColor.DARK_PURPLE 
                            + botName + ChatColor.WHITE 
                            + "/" + ChatColor.DARK_PURPLE + channelName 
                            + ChatColor.WHITE + "]"
                            + " Topic: " + plugin.ircBots.get(botName)
                                    .channelTopic.get(channelName));
                }
            }
            plugin.sendMessageToSender(sender, fullUsage);
            return;
        }
        if (bac.bot.size() > 0 && bac.channel.size() > 0) {
            for (String botName : bac.bot) {
                for (String channelName : bac.channel) {
                    String topic = "";
                    for (int i = idx; i < args.length; i++) {
                        topic = topic + " " + args[i];                        
                    }
                    plugin.ircBots.get(botName).changeTopic(channelName,
                            topic.substring(1), sender);
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
