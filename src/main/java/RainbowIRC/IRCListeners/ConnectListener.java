package RainbowIRC.IRCListeners;

import RainbowIRC.RainbowBot;
import RainbowIRC.MyPlugin;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;

/**
 *
 * @author cnaude
 */
public class ConnectListener extends ListenerAdapter {

    MyPlugin plugin;
    RainbowBot ircBot;

    /**
     *
     * @param plugin
     * @param ircBot
     */
    public ConnectListener(MyPlugin plugin, RainbowBot ircBot) {
        this.plugin = plugin;
        this.ircBot = ircBot;
    }

    /**
     *
     * @param event
     */
    @Override
    public void onConnect(ConnectEvent event) {
        PircBotX bot = event.getBot();
        if (bot.getUserBot().getNick().isEmpty()) {
            plugin.logError("Connected but bot nick is blank!");
        } else {
            ircBot.broadcastIRCConnect(ircBot.botNick);
            if (ircBot.sendRawMessageOnConnect) {
                plugin.logInfo("Sending raw message to server");
                ircBot.asyncRawlineNow(ircBot.rawMessage);
            }
        }
        ircBot.setConnected(true);
        ircBot.autoJoinChannels();
    }
}
