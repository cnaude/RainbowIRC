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
public class Send implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "([bot]) ([channel]) [message]";
    private final String desc = "Send a message to an IRC channel.";
    private final String name = "send";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage; 

    /**
     *
     * @param plugin
     */
    public Send(MyPlugin plugin) {
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
            String channelName = null;
            List<RainbowBot> myBots = new ArrayList<>();
            if (plugin.ircBots.containsKey(args[1])) {
                myBots.add(plugin.ircBots.get(args[1]));
                msgIdx = 2;
                if (args.length >= 3) {
                    if (plugin.ircBots.get(args[1]).isValidChannel(args[2])) {
                        channelName = args[2];
                    }
                }
            } else {
                myBots.addAll(plugin.ircBots.values());
            }
            for (RainbowBot ircBot : myBots) {
                String msg = "";
                for (int i = msgIdx; i < args.length; i++) {
                    msg = msg + " " + args[i];
                }
                if (channelName == null) {
                    for (String c : ircBot.botChannels) {
                        if (sender instanceof MC_Player) {
                            ircBot.gameChat((MC_Player) sender, c, msg.substring(1));
                        } else {
                            ircBot.consoleChat(c, msg.substring(1));
                        }
                    }
                } else {
                    if (sender instanceof MC_Player) {
                        ircBot.gameChat((MC_Player) sender, channelName, msg.substring(1));
                    } else {
                        ircBot.consoleChat(channelName, msg.substring(1));
                    }
                }

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
