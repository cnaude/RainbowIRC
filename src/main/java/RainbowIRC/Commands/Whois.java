/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowIRC.Commands;

import RainbowIRC.RainbowBot;
import RainbowIRC.MyPlugin;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public class Whois implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "([bot]) [nick]";
    private final String desc = "Get whois info for IRC user.";
    private final String name = "whois";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public Whois(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        if (args.length == 2) {
            String nick = args[1];
            for (RainbowBot ircBot : plugin.ircBots.values()) {
                ircBot.sendUserWhois(sender, nick);
            }
        } else if (args.length == 3) {
            String bot = args[1];
            String nick = args[2];
            if (plugin.ircBots.containsKey(bot)) {
                RainbowBot ircBot = plugin.ircBots.get(bot);
                ircBot.sendUserWhois(sender, nick);
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
