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
public class Disconnect implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "([bot])";
    private final String desc = "Disconnect from IRC server.";
    private final String name = "disconnect";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public Disconnect(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        if (args.length == 1) {
            for (RainbowBot ircBot : plugin.ircBots.values()) {
                ircBot.asyncQuit(sender);
            }
        } else if (args.length == 2) {
            String bot = args[1];
            if (plugin.ircBots.containsKey(bot)) {
                plugin.ircBots.get(bot).asyncQuit(sender);
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
