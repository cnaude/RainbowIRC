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
public class Save implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "([bot])";
    private final String desc = "Save bot configuration.";
    private final String name = "save";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public Save(MyPlugin plugin) {
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
                ircBot.saveConfig(sender);
            }
        } else if (args.length >= 2) {
            String bot = args[1];
            if (plugin.ircBots.containsKey(bot)) {
                plugin.ircBots.get(bot).saveConfig(sender);
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
