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
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author cnaude
 */
public class MessageListener extends ListenerAdapter {

    MyPlugin plugin;
    RainbowBot ircBot;

    /**
     *
     * @param plugin
     * @param ircBot
     */
    public MessageListener(MyPlugin plugin, RainbowBot ircBot) {
        this.plugin = plugin;
        this.ircBot = ircBot;
    }

    /**
     *
     * @param event
     */
    @Override
    public void onMessage(MessageEvent event) {

        String message = event.getMessage();
        Channel channel = event.getChannel();
        User user = event.getUser();

        plugin.logDebug("Message caught <" + user.getNick() + ">: " + message);
                
        if (ircBot.isValidChannel(channel.getName())) {
            plugin.ircMessageHandler.processMessage(ircBot, user, channel, message, false);
        }
    }
}
