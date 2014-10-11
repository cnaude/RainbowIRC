package RainbowIRC;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This thread checks each bot for connectivity and reconnects when appropriate.
 *
 * @author Chris Naude
 *
 */
public class BotWatcher {

    private final MyPlugin plugin;
    private final Timer timer;

    /**
     *
     * @param plugin
     */
    public BotWatcher(final MyPlugin plugin) {
        this.plugin = plugin;
        this.timer = new Timer();
        startWatcher();
    }

    private void startWatcher() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (RainbowBot ircBot : plugin.ircBots.values()) {
                    if (ircBot.isConnectedBlocking()) {
                        //plugin.logDebug("[" + ircBot.botNick + "] CONNECTED");
                        ircBot.setConnected(true);
                    } else {
                        ircBot.setConnected(false);
                        if (ircBot.autoConnect) {
                            plugin.logInfo("[" + ircBot.botNick + "] NOT CONNECTED");
                            ircBot.reload();
                        }
                    }
                }
            }

        }, plugin.ircConnCheckInterval * 1000, plugin.ircConnCheckInterval * 1000);
        
    }

    /**
     *
     */
    public void cancel() {
        timer.cancel();
    }

}
