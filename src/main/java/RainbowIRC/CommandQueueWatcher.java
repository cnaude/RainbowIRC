package RainbowIRC;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import joebkt.di;
import net.minecraft.server.MinecraftServer;

/**
 *
 * @author Chris Naude Poll the command queue and dispatch to Bukkit
 */
public class CommandQueueWatcher {

    private final MyPlugin plugin;
    private final Timer timer;
    private final Queue<IRCCommand> queue = new ConcurrentLinkedQueue<>();

    /**
     *
     * @param plugin
     */
    public CommandQueueWatcher(final MyPlugin plugin) {
        this.plugin = plugin;
        this.timer = new Timer();
        startWatcher();
    }

    private void startWatcher() {
        plugin.logDebug("Starting command queue");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                queueAndSend();
            }

        }, 0, 5000);

    }

    private void queueAndSend() {
        IRCCommand ircCommand = queue.poll();
        if (ircCommand != null) {
            //plugin.getServer().dispatchCommand(ircCommand.getIRCCommandSender(), ircCommand.getGameCommand());
            //plugin.getServer().executeCommand(ircCommand.getGameCommand());
            try {
                MinecraftServer.getServer().getCommandSender().executeCommand(ircCommand.getIRCCommandSender(), ircCommand.getGameCommand());
            } catch (di ex) {
                Logger.getLogger(CommandQueueWatcher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     */
    public void cancel() {
        timer.cancel();
    }

    public String clearQueue() {
        int size = queue.size();
        if (!queue.isEmpty()) {
            queue.clear();
        }
        return "Elements removed from command queue: " + size;
    }

    /**
     *
     * @param command
     */
    public void add(IRCCommand command) {
        plugin.logDebug("Adding command to queue: " + command.getGameCommand());
        queue.offer(command);
    }
}
