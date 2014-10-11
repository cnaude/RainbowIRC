/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RainbowIRC.Commands;

import RainbowIRC.RainbowBot;
import RainbowIRC.MyPlugin;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public class ReloadBotConfigs implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "";
    private final String desc = "Reload all bot configurations.";
    private final String name = "reloadbotconfigs";

    /**
     *
     * @param plugin
     */
    public ReloadBotConfigs(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        for (RainbowBot ircBot : plugin.ircBots.values()) {
            ircBot.reloadConfig(sender);
        }
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
