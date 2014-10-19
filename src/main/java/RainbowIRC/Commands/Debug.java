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
public class Debug implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "([t|f])";
    private final String desc = "Enable or disable debug mode.";
    private final String name = "debug";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 
    
    /**
     *
     * @param plugin
     */
    public Debug(MyPlugin plugin) {
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
            plugin.sendMessage(sender, ChatColor.DARK_PURPLE + "Debug mode is currently "
                    + ChatColor.WHITE + plugin.debugMode());
        } else if (args.length == 2) {
            if (args[1].startsWith("t")) {
                plugin.debugMode(true);
            } else if (args[1].startsWith("f")) {
                plugin.debugMode(false);
            } else {
                plugin.sendMessage(sender, usage);
            }
            plugin.sendMessage(sender, ChatColor.DARK_PURPLE + "Debug mode is now "
                    + ChatColor.WHITE + plugin.debugMode());
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
