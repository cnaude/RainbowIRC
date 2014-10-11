/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowIRC.IRCListeners;

import RainbowIRC.TemplateName;
import RainbowIRC.RainbowBot;
import RainbowIRC.MyPlugin;
import org.pircbotx.Channel;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.NickChangeEvent;

/**
 *
 * @author cnaude
 */
public class NickChangeListener extends ListenerAdapter {

    MyPlugin plugin;
    RainbowBot ircBot;

    /**
     *
     * @param plugin
     * @param ircBot
     */
    public NickChangeListener(MyPlugin plugin, RainbowBot ircBot) {
        this.plugin = plugin;
        this.ircBot = ircBot;
    }

    /**
     *
     * @param event
     */
    @Override
    public void onNickChange(NickChangeEvent event) {
        String newNick = event.getNewNick();
        String oldNick = event.getOldNick();
        plugin.logDebug("OLD: " + oldNick);
        plugin.logDebug("NEW: " + newNick);

        for (String channelName : ircBot.channelNicks.keySet()) {
            Channel channel = ircBot.getChannel(channelName);
            if (channel != null) {
                if (ircBot.enabledMessages.get(channelName).contains(TemplateName.IRC_NICK_CHANGE)) {
                    plugin.broadcast(plugin.colorConverter.ircColorsToGame(
                            plugin.getMsgTemplate(ircBot.botNick, TemplateName.IRC_NICK_CHANGE)                            
                            .replace("%NEWNICK%", newNick)
                            .replace("%OLDNICK%", oldNick)
                            .replace("%CHANNEL%", channelName)), "irc.message.nickchange");
                }
                if (ircBot.channelNicks.get(channelName).contains(oldNick)) {
                    ircBot.channelNicks.get(channelName).remove(oldNick);
                    plugin.logDebug("Removing " + oldNick);
                }
                ircBot.channelNicks.get(channelName).add(newNick);
                plugin.logDebug("Adding " + newNick);
            }
        }

    }
}
