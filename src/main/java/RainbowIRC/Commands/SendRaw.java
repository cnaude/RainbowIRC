/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowIRC.Commands;

import RainbowIRC.RainbowBot;
import RainbowIRC.MyPlugin;
import java.util.ArrayList;
import java.util.List;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public class SendRaw implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "([bot]) [message]";
    private final String desc = "Send raw message to the IRC server.";
    private final String name = "sendraw";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public SendRaw(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        if (args.length >= 2) {
            int msgIdx = 1;
            List<RainbowBot> myBots = new ArrayList<RainbowBot>();
            if (plugin.ircBots.containsKey(args[1])) {
                myBots.add(plugin.ircBots.get(args[1]));
                msgIdx = 2;
            } else {
                myBots.addAll(plugin.ircBots.values());
            }
            for (RainbowBot ircBot : myBots) {
                String msg = "";
                for (int i = msgIdx; i < args.length; i++) {
                    msg = msg + " " + args[i];
                }
                plugin.logDebug("Sending raw message to the server: " + msg.substring(1));
                ircBot.asyncRawlineNow(msg.substring(1));                  
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
