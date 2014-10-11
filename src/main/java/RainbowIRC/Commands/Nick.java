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
public class Nick implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "[bot] [nick]";
    private final String desc = "Change bot's IRC nick.";
    private final String name = "nick";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public Nick(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        if (args.length == 3) {
            String bot = args[1];
            String nick = args[2];
            if (plugin.ircBots.containsKey(bot)) {
                if (plugin.ircBots.containsKey(nick)) {
                    sender.sendMessage(ChatColor.RED 
                            + "There is already a bot with that nick!");
                } else {
                    plugin.ircBots.get(bot).asyncChangeNick(sender, nick);
                    RainbowBot ircBot = plugin.ircBots.remove(bot);
                    plugin.ircBots.put(nick, ircBot);
                    ircBot.botNick = nick;
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
