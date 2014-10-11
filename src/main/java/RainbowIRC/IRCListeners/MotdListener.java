/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowIRC.IRCListeners;

import RainbowIRC.RainbowBot;
import RainbowIRC.MyPlugin;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MotdEvent;

/**
 *
 * @author cnaude
 */
public class MotdListener extends ListenerAdapter {
    
    MyPlugin plugin;
    RainbowBot ircBot;

    /**
     *
     * @param plugin
     * @param ircBot
     */
    public MotdListener(MyPlugin plugin, RainbowBot ircBot) {
        this.plugin = plugin;
        this.ircBot = ircBot;
    }
    
    /**
     *
     * @param event
     */
    @Override
    public void onMotd(MotdEvent event) {
        if (ircBot.showMOTD) {
            plugin.logInfo(event.getMotd());
        }
    }
}
