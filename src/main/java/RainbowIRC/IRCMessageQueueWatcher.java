package RainbowIRC;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Chris Naude Poll the command queue and dispatch to Bukkit
 */
public class IRCMessageQueueWatcher {

    private final MyPlugin plugin;
    private final RainbowBot ircBot;
    private final Timer timer;
    private final BlockingQueue<IRCMessage> queue = new LinkedBlockingQueue<>();

    /**
     *
     * @param plugin
     * @param ircBot
     */
    public IRCMessageQueueWatcher(final RainbowBot ircBot, final MyPlugin plugin) {
        this.plugin = plugin;
        this.ircBot = ircBot;
        this.timer = new Timer();
        
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                queueAndSend();
            }

        }, 0, 1000);
    }

    private void queueAndSend() {
        IRCMessage ircMessage = queue.poll();
        if (ircMessage != null) {
            plugin.logDebug("[" + queue.size() + "]: queueAndSend message detected");
            if (ircMessage.ctcpResponse) {
                ircBot.blockingCTCPMessage(ircMessage.target, ircMessage.message);
            } else {
                ircBot.blockingIRCMessage(ircMessage.target, ircMessage.message);
            }
        }
    }

    public void cancel() {
        timer.cancel();
    }

    public String clearQueue() {
        int size = queue.size();
        if (!queue.isEmpty()) {
            queue.clear();
        }
        return "Elements removed from message queue: " + size;
    }

    /**
     *
     * @param ircMessage
     */
    public void add(IRCMessage ircMessage) {
        queue.offer(ircMessage);
    }
}
