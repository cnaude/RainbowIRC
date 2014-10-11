package RainbowIRC.IRCListeners;

import RainbowIRC.RainbowBot;
import RainbowIRC.MyPlugin;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.QuitEvent;

/**
 *
 * @author cnaude
 */
public class QuitListener extends ListenerAdapter {

    MyPlugin plugin;
    RainbowBot ircBot;

    /**
     *
     * @param plugin
     * @param ircBot
     */
    public QuitListener(MyPlugin plugin, RainbowBot ircBot) {
        this.plugin = plugin;
        this.ircBot = ircBot;
    }

    /**
     *
     * @param event
     */
    @Override
    public void onQuit(QuitEvent event) {        
        String nick = event.getUser().getNick();
        for (String channelName : ircBot.channelNicks.keySet()) {
            if (ircBot.channelNicks.get(channelName).contains(nick)) {
                ircBot.broadcastIRCQuit(event.getUser(), ircBot.getChannel(channelName), event.getReason());
            }
            ircBot.channelNicks.get(channelName).remove(nick);
        }
    }
}

