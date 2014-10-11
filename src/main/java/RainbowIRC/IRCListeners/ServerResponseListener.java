package RainbowIRC.IRCListeners;

import RainbowIRC.RainbowBot;
import RainbowIRC.MyPlugin;
import org.pircbotx.ReplyConstants;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ServerResponseEvent;

/**
 *
 * @author cnaude
 */
public class ServerResponseListener extends ListenerAdapter {

    MyPlugin plugin;
    RainbowBot ircBot;

    /**
     *
     * @param plugin
     * @param ircBot
     */
    public ServerResponseListener(MyPlugin plugin, RainbowBot ircBot) {
        this.plugin = plugin;
        this.ircBot = ircBot;
    }

    /**
     *
     * @param event
     */
    @Override
    public void onServerResponse(ServerResponseEvent event) {
        int serverReply = event.getCode();
        
        if (serverReply == ReplyConstants.ERR_BADCHANNELKEY) {
            plugin.logInfo("Bad channel password.");
        }
        
        if (serverReply == ReplyConstants.ERR_BANNEDFROMCHAN) {
            plugin.logInfo("Banned from the channel.");
        }
        
        //plugin.logDebug("Server response: " + event.getRawLine());
        
    }
}
