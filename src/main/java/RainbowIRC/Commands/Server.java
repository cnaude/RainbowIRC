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
public class Server implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "[bot] [server] ([true|false])";
    private final String desc = "Set IRC server hostname. Optionally set autoconnect.";
    private final String name = "server";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public Server(MyPlugin plugin) {
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
            String bot = args[1];
            String server = args[2];
            if (plugin.ircBots.containsKey(bot)) {
                if (args.length == 3) {
                    plugin.ircBots.get(bot).setServer(sender, server);
                } else if (args.length == 4) {
                    plugin.ircBots.get(bot).setServer(sender, server, Boolean.parseBoolean(args[3]));
                }
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
