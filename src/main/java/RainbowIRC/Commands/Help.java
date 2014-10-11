package RainbowIRC.Commands;

import RainbowIRC.MyPlugin;
import PluginReference.ChatColor;
import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public class Help implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "([command])";
    private final String desc = "Display help on a specific command or list all commands.";
    private final String name = "help";

    /**
     *
     * @param plugin
     */
    public Help(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        if (args.length >= 2) {
            String s = args[1];
            if (plugin.commandHandlers.commands.containsKey(s)) {
                sender.sendMessage(helpStringBuilder(
                        plugin.commandHandlers.commands.get(s).name(),
                        plugin.commandHandlers.commands.get(s).desc(),
                        plugin.commandHandlers.commands.get(s).usage()));
                return;
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid sub command: " 
                        + ChatColor.WHITE + s);
                return;
            }
        }
        sender.sendMessage(plugin.colorConverter.translateAlternateColorCodes('&',
                "&5-----[  &"+plugin.getDescription().getName()+"&5 - &f" + plugin.getDescription().getVersion() + "&5 ]-----"));
        for (String s : plugin.commandHandlers.sortedCommands) {
            if (plugin.commandHandlers.commands.containsKey(s)) {
                sender.sendMessage(helpStringBuilder(
                        plugin.commandHandlers.commands.get(s).name(),
                        plugin.commandHandlers.commands.get(s).desc(),
                        plugin.commandHandlers.commands.get(s).usage()));
            }
        }

    }

    private String helpStringBuilder(String n, String d, String u) {
        return plugin.colorConverter.translateAlternateColorCodes('&', "&5/irc "
                + n + " &6" + u + " &f- " + d);
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
