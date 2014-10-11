package RainbowIRC.Commands;

import RainbowIRC.MyPlugin;
import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public class Reload implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "";
    private final String desc = "Reload the plugin.";
    private final String name = "reload";

    /**
     *
     * @param plugin
     */
    public Reload(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        sender.sendMessage("Disabling PurpleIRC...");
        plugin.onShutdown();
        sender.sendMessage("Enabling PurpleIRC...");
        plugin.onStartup(plugin.getServer());
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String desc() {
        return desc;
    }

    @Override
    public String usage() {
        return usage;
    }
}
