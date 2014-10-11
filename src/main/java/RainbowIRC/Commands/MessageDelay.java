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
public class MessageDelay implements IRCCommandInterface  {

    private final MyPlugin plugin;
    private final String usage = "[bot] [milliseconds]";
    private final String desc = "Change IRC message delay.";
    private final String name = "messagedelay";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public MessageDelay(MyPlugin plugin) {
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
            if (args[2].matches("\\d+")) {
                String bot = args[1];
                if (plugin.ircBots.containsKey(bot)) {
                    long delay = Long.parseLong(args[2]);
                    plugin.ircBots.get(bot).setIRCDelay(sender, delay);
                } else {
                    sender.sendMessage(plugin.invalidBotName.replace("%BOT%", bot));
                }
            } else {
                sender.sendMessage(fullUsage);
            }
        } else if (args.length == 2) {
            String bot = args[1];
            if (plugin.ircBots.containsKey(bot)) {
                sender.sendMessage(ChatColor.WHITE + "IRC message delay is currently "
                        + plugin.ircBots.get(bot).getMessageDelay() + " ms.");
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
