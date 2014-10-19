/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowIRC.Commands;

import RainbowIRC.RainbowBot;
import RainbowIRC.MyPlugin;
import java.util.ArrayList;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public class CTCP implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "([bot]) [target] [command]";
    private final String desc = "Send CTCP command to the user or channel.";
    private final String name = "ctcp";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage;    

    /**
     *
     * @param plugin
     */
    public CTCP(MyPlugin plugin) {
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
            plugin.logDebug("Dispatching ctcp command...");
            int msgIdx = 2;
            String target;
            java.util.List<RainbowBot> myBots = new ArrayList<RainbowBot>();
            if (plugin.ircBots.containsKey(args[1])) {
                myBots.add(plugin.ircBots.get(args[1]));
                msgIdx = 3;
                target = args[2];
            } else {
                myBots.addAll(plugin.ircBots.values());
                target = args[1];
            }

            if (msgIdx == 3 && args.length <= 3) {
                plugin.sendMessage(sender, fullUsage);
                return;
            }

            for (RainbowBot ircBot : myBots) {
                String msg = "";
                for (int i = msgIdx; i < args.length; i++) {
                    msg = msg + " " + args[i];
                }
                ircBot.asyncCTCPCommand(target, msg.substring(1));
                plugin.sendMessage(sender, "Sent CTCP command \"" + msg.substring(1) + "\" to \"" + target + "\"");
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