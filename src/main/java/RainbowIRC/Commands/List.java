/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowIRC.Commands;

import RainbowIRC.RainbowBot;
import RainbowIRC.MyPlugin;
import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public class List implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "([bot]) ([channel])";
    private final String desc = "List users in IRC channel.";
    private final String name = "list";
    //private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public List(MyPlugin plugin) {
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
                ircBot.sendUserList(sender);
            }
        } else if (args.length > 1) {
            String bot = args[1];
            if (plugin.ircBots.containsKey(bot)) {
                RainbowBot ircBot = plugin.ircBots.get(bot);
                if (args.length > 2) {
                    ircBot.sendUserList(sender, args[2]);
                } else {
                    ircBot.sendUserList(sender);
                }
            } else {
                plugin.sendMessageToSender(sender, plugin.invalidBotName.replace("%BOT%", bot));
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
