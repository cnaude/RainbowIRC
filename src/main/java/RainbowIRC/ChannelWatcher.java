package RainbowIRC;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Chris Naude This thread checks each for users and updates the
 * internal lists.
 */
public class ChannelWatcher {

    private final MyPlugin plugin;
    private final Timer timer;

    /**
     *
     * @param plugin
     */
    public ChannelWatcher(final MyPlugin plugin) {
        this.plugin = plugin;
        this.timer = new Timer();
        startWatcher();
    }

    private void startWatcher() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (RainbowBot ircBot : plugin.ircBots.values()) {
                    ircBot.updateNickList();
                }

            }

        }, plugin.ircChannelCheckInterval * 1000, plugin.ircChannelCheckInterval * 1000);

    }

    /**
     *
     */
    public void cancel() {
        timer.cancel();
    }

}
