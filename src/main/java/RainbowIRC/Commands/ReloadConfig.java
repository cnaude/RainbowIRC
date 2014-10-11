/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowIRC.Commands;

import RainbowIRC.MyPlugin;
import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public class ReloadConfig implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "";
    private final String desc = "Reload PurpleIRC/config.yml.";
    private final String name = "reloadconfig";    

    /**
     *
     * @param plugin
     */
    public ReloadConfig(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        plugin.reloadMainConfig(sender);
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
