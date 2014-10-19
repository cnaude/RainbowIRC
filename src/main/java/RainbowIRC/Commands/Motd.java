package RainbowIRC.Commands;

import PluginReference.MC_Player;
import RainbowIRC.RainbowBot;
import RainbowIRC.MyPlugin;
import java.util.ArrayList;
/**
 *
 * @author cnaude
 */
public class Motd implements IRCCommandInterface {

    private final MyPlugin plugin;
    private final String usage = "([bot])";
    private final String desc = "Get server motd.";
    private final String name = "motd";

    /**
     *
     * @param plugin
     */
    public Motd(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     *
     * @param sender
     * @param args
     */
    @Override
    public void dispatch(MC_Player sender, String[] args) {
        java.util.List<RainbowBot> myBots = new ArrayList<>();
        if (args.length >= 2) {
            if (plugin.ircBots.containsKey(args[1])) {
                myBots.add(plugin.ircBots.get(args[1]));
                
            } else {
                plugin.sendMessage(sender, plugin.invalidBotName.replace("%BOT%", args[1]));
            }
        } else {
            myBots.addAll(plugin.ircBots.values());
        }

        for (RainbowBot ircBot : myBots) {
            String motd = ircBot.getMotd();
            if (motd != null) {
                plugin.sendMessage(sender, motd);
            } else {
                plugin.sendMessage(sender, "No MOTD found.");
            }
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
