/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowIRC.IRCListeners;

import RainbowIRC.RainbowBot;
import RainbowIRC.MyPlugin;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PrivateMessageEvent;

/**
 *
 * @author cnaude
 */
public class PrivateMessageListener extends ListenerAdapter {

    MyPlugin plugin;
    RainbowBot ircBot;

    /**
     *
     * @param plugin
     * @param ircBot
     */
    public PrivateMessageListener(MyPlugin plugin, RainbowBot ircBot) {
        this.plugin = plugin;
        this.ircBot = ircBot;
    }

    /**
     *
     * @param event
     */
    @Override
    public void onPrivateMessage(PrivateMessageEvent event) {

        String message = event.getMessage();
        User user = event.getUser();
        Channel channel;

        plugin.logDebug("Private message caught <" + user.getNick() + ">: " + message);

        for (String myChannel : ircBot.botChannels) {
            channel = ircBot.getChannel(myChannel);
            if (channel != null) {
                if (user.getChannels().contains(channel)) {
                    plugin.ircMessageHandler.processMessage(ircBot, user, channel, message, true);
                    return;
                }
                plugin.logDebug("Private message from " + user.getNick() + " ignored because not in valid channel.");
            }
        }
    }
}
