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
public class Msg implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "([bot]) [user] [message]";
    private final String desc = "Send a private message to an IRC user.";
    private final String name = "msg";
    private final String fullUsage = ChatColor.WHITE + "Usage: " + ChatColor.GOLD + "/irc " + name + " " + usage;

    /**
     *
     * @param plugin
     */
    public Msg(MyPlugin plugin) {
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
            plugin.logDebug("Dispatching msg command...");
            int msgIdx = 2;
            String nick;
            java.util.List<RainbowBot> myBots = new ArrayList<RainbowBot>();
            if (plugin.ircBots.containsKey(args[1])) {
                myBots.add(plugin.ircBots.get(args[1]));
                msgIdx = 3;
                nick = args[2];
            } else {
                myBots.addAll(plugin.ircBots.values());
                nick = args[1];
            }

            if (msgIdx == 3 && args.length <= 3) {
                plugin.sendMessageToSender(sender, fullUsage);
                return;
            }

            for (RainbowBot ircBot : myBots) {
                String msg = "";
                final String template = plugin.getMsgTemplate(ircBot.botNick, "game-pchat-response");
                for (int i = msgIdx; i < args.length; i++) {
                    msg = msg + " " + args[i];
                }
                if (sender instanceof MC_Player) {
                    ircBot.msgPlayer((MC_Player) sender, nick, msg.substring(1));
                } else {
                    ircBot.consoleMsgPlayer(nick, msg.substring(1));
                }                
                if (!template.isEmpty()) {
                    plugin.sendMessageToSender(sender, plugin.tokenizer.msgChatResponseTokenizer(nick, msg.substring(1), template));
                }
            }
        }
        else {
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
