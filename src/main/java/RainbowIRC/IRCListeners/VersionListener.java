package RainbowIRC.IRCListeners;

import RainbowIRC.MyPlugin;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.VersionEvent;

/**
 *
 * @author cnaude
 */
public class VersionListener extends ListenerAdapter {

    MyPlugin plugin;

    /**
     *
     * @param plugin
     */
    public VersionListener(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param event
     */
    @Override
    public void onVersion(VersionEvent event) {
        event.respond("[Name: " + plugin.getDescription().getName() + "]"
                + "[Desc: " + plugin.getDescription().getDescription() + "]"
                + "[Version: " + plugin.getDescription().getVersion() + "]"
                + "[URL: " + plugin.getDescription().getURL() + "]");
    }
}
