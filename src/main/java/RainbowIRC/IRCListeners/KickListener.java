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
import org.pircbotx.hooks.events.KickEvent;

/**
 *
 * @author cnaude
 */
public class KickListener extends ListenerAdapter {

    MyPlugin plugin;
    RainbowBot ircBot;

    /**
     *
     * @param plugin
     * @param ircBot
     */
    public KickListener(MyPlugin plugin, RainbowBot ircBot) {
        this.plugin = plugin;
        this.ircBot = ircBot;
    }

    /**
     *
     * @param event
     */
    @Override
    public void onKick(KickEvent event) {
        Channel channel = event.getChannel();
        User recipient = event.getRecipient();
        User user = event.getUser();

        if (ircBot.isValidChannel(channel.getName())) {
            ircBot.broadcastIRCKick(recipient, user, event.getReason(), channel);
        }
    }
}