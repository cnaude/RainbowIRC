package RainbowIRC.Utilities;

import PluginReference.MC_Player;
import RainbowIRC.MyPlugin;
import java.util.ArrayList;

/**
 *
 * @author cnaude
 */
public class BotsAndChannels {

    public ArrayList<String> bot = new ArrayList<>();
    public ArrayList<String> channel = new ArrayList<>();

    public BotsAndChannels(MyPlugin plugin, MC_Player sender,
            String botName, String channelName) {
        if (plugin.ircBots.containsKey(botName)) {
            bot.add(botName);
            if (plugin.ircBots.get(botName).isValidChannel(channelName)) {           
                channel.add(channelName);
            } else {
                plugin.sendMessage(sender, plugin.invalidChannelName.replace("%CHANNEL%", channelName));
            }
        } else {
            plugin.sendMessage(sender, plugin.invalidBotName.replace("%BOT%", botName));
        }
    }

    public BotsAndChannels(MyPlugin plugin, MC_Player sender) {
        for (String botName : plugin.ircBots.keySet()) {
            bot.add(botName);
            for (String channelName : plugin.ircBots.get(botName).botChannels) {
                channel.add(channelName);
            }
        }
    }

}
