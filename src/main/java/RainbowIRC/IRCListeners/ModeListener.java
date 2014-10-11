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
import org.pircbotx.hooks.events.ModeEvent;

/**
 *
 * @author cnaude
 */
public class ModeListener extends ListenerAdapter {

    MyPlugin plugin;
    RainbowBot ircBot;

    /**
     *
     * @param plugin
     * @param ircBot
     */
    public ModeListener(MyPlugin plugin, RainbowBot ircBot) {
        this.plugin = plugin;
        this.ircBot = ircBot;
    }

    /**
     *
     * @param event
     */
    @Override
    public void onMode(ModeEvent event) {
        Channel channel = event.getChannel();
        String mode = event.getMode();
        User user = event.getUser();

        if (!ircBot.isValidChannel(channel.getName())) {
            ircBot.broadcastIRCMode(user, mode, channel);
        }
    }
}
