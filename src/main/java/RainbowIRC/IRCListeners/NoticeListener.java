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
import org.pircbotx.hooks.events.NoticeEvent;

/**
 *
 * @author cnaude
 */
public class NoticeListener extends ListenerAdapter {

    MyPlugin plugin;
    RainbowBot ircBot;

    /**
     *
     * @param plugin
     * @param ircBot
     */
    public NoticeListener(MyPlugin plugin, RainbowBot ircBot) {
        this.plugin = plugin;
        this.ircBot = ircBot;
    }

    /**
     *
     * @param event
     */
    @Override
    public void onNotice(NoticeEvent event) {
        Channel channel = event.getChannel();
        String message = event.getMessage();
        String notice = event.getNotice();
        User user = event.getUser();

        plugin.logInfo("-" + user.getNick() + "-" + message);
        if (channel != null) {
            if (ircBot.isValidChannel(channel.getName())) {
                ircBot.broadcastIRCNotice(user, message, notice, channel);
            }
        }
    }
}
