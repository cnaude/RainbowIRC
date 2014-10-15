package RainbowIRC.Utilities;

import PluginReference.MC_Player;
import RainbowIRC.MyPlugin;
import RainbowIRC.RainbowBot;
import org.pircbotx.User;

/**
 * Main class containing all message template token expanding methods
 *
 * @author cnaude
 */
public class ChatTokenizer {

    MyPlugin plugin;

    /**
     * Class initializer
     *
     * @param plugin
     */
    public ChatTokenizer(MyPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * IRC to game chat tokenizer without a message
     *
     * @param ircBot
     * @param user
     * @param channel
     * @param template
     * @return
     */
    public String chatIRCTokenizer(RainbowBot ircBot, User user, org.pircbotx.Channel channel, String template) {
        return plugin.colorConverter.ircColorsToGame(ircUserTokenizer(template, user)
                .replace("%NICKPREFIX%", ircBot.getNickPrefix(user, channel))
                .replace("%CHANNEL%", channel.getName()));
    }

    /**
     * IRC user tokenizer
     *
     * @param user
     * @param template
     * @return
     */
    public String ircUserTokenizer(String template, User user) {
        String host = user.getHostmask();
        String server = user.getServer();
        String away = user.getAwayMessage();
        String ircNick = user.getNick();
        if (host == null) {
            host = "";
        }
        if (server == null) {
            server = "";
        }
        if (away == null) {
            away = "";
        }
        return template.replace("%HOST%", host)
                .replace("%NAME%", ircNick)
                .replace("%SERVER%", server)
                .replace("%AWAY%", away);
    }

     /**
     * IRC kicking user tokenizer
     *
     * @param template
     * @param recipient
     * @param kicker
     * @return
     */
    public String ircUserTokenizer(String template, User recipient, User kicker) {
        String host = kicker.getHostmask();
        String server = kicker.getServer();
        String away = kicker.getAwayMessage();
        String ircNick = kicker.getNick();
        if (host == null) {
            host = "";
        }
        if (server == null) {
            server = "";
        }
        if (away == null) {
            away = "";
        }
        return ircUserTokenizer(template, recipient)
                .replace("%KICKERHOST%", host)
                .replace("%KICKER%", ircNick)
                .replace("%KICKERSERVER%", server)
                .replace("%KICKERAWAY%", away);
    }

    /**
     * Normal IRC to game chat tokenizer
     *
     * @param ircBot
     * @param user
     * @param channel
     * @param template
     * @param message
     * @return
     */
    public String ircChatToGameTokenizer(RainbowBot ircBot, User user, org.pircbotx.Channel channel, String template, String message) {
        String ircNick = user.getNick();
        String tmpl;
        MC_Player player = this.getPlayer(ircNick);
        if (player != null) {
            tmpl = playerTokenizer(player, template);
        } else {
            plugin.logDebug("ircChatToGameTokenizer: null player: " + ircNick);
            tmpl = playerTokenizer(ircNick, template);
        }
        return plugin.colorConverter.ircColorsToGame(ircUserTokenizer(tmpl, user)
                .replace("%NICKPREFIX%", ircBot.getNickPrefix(user, channel))
                .replace("%MESSAGE%", message)
                .replace("%CHANNEL%", channel.getName()));
    }

    /**
     * IRC to Hero chat channel tokenizer
     *
     * @param ircBot
     * @param user
     * @param channel
     * @param template
     * @param message
     * @param tChannel
     * @return
     */
    public String ircChatToTownyChatTokenizer(RainbowBot ircBot, User user, org.pircbotx.Channel channel, String template, String message, String tChannel) {
        String ircNick = user.getNick();
        String tmpl;
        MC_Player player = this.getPlayer(ircNick);
        if (player != null) {
            tmpl = playerTokenizer(player, template);
        } else {
            tmpl = playerTokenizer(ircNick, template);
        }
        return plugin.colorConverter.ircColorsToGame(ircUserTokenizer(tmpl, user)
                .replace("%TOWNYCHANNEL%", tChannel)
                .replace("%NICKPREFIX%", ircBot.getNickPrefix(user, channel))
                .replace("%MESSAGE%", message)
                .replace("%CHANNEL%", channel.getName()));
    }

    /**
     * IRC kick message to game
     *
     * @param ircBot
     * @param recipient
     * @param kicker
     * @param reason
     * @param channel
     * @param template
     * @return
     */
    public String ircKickTokenizer(RainbowBot ircBot, User recipient, User kicker, String reason, org.pircbotx.Channel channel, String template) {
        return plugin.colorConverter.ircColorsToGame(ircUserTokenizer(template, recipient, kicker)
                .replace("%NICKPREFIX%", ircBot.getNickPrefix(kicker, channel))
                .replace("%REASON%", reason)
                .replace("%CHANNEL%", channel.getName()));
    }

    /**
     * IRC mode change messages
     *
     * @param ircBot
     * @param user
     * @param mode
     * @param channel
     * @param template
     * @return
     */
    public String ircModeTokenizer(RainbowBot ircBot, User user, String mode, org.pircbotx.Channel channel, String template) {
        return plugin.colorConverter.ircColorsToGame(ircUserTokenizer(template, user)
                .replace("%MODE%", mode)
                .replace("%NICKPREFIX%", ircBot.getNickPrefix(user, channel))
                .replace("%CHANNEL%", channel.getName()));
    }

    /**
     * IRC notice change messages
     *
     * @param ircBot
     * @param user
     * @param message
     * @param notice
     * @param channel
     * @param template
     * @return
     */
    public String ircNoticeTokenizer(RainbowBot ircBot, User user, String message, String notice, org.pircbotx.Channel channel, String template) {
        return plugin.colorConverter.ircColorsToGame(ircUserTokenizer(template, user)
                .replace("%NICKPREFIX%", ircBot.getNickPrefix(user, channel))
                .replace("%MESSAGE%", message)
                .replace("%NOTICE%", notice)
                .replace("%CHANNEL%", channel.getName()));
    }

    /**
     * Game chat to IRC
     *
     * @param pName
     * @param template
     * @param message
     * @return
     */
    public String gameChatToIRCTokenizer(String pName, String template, String message) {
        return plugin.colorConverter.gameColorsToIrc(template
                .replace("%WORLD%", plugin.defaultPlayerWorld)
                .replace("%NAME%", pName)
                .replace("%MESSAGE%", plugin.colorConverter.gameColorsToIrc(message)));
    }

    /**
     * Game chat to IRC
     *
     * @param player
     * @param template
     * @param message
     * @return
     */
    public String gameChatToIRCTokenizer(MC_Player player, String template, String message) {
        if (message == null) {
            message = "";
        }
        return plugin.colorConverter.gameColorsToIrc(playerTokenizer(player, template).replace("%MESSAGE%", message));
    }

    /**
     * Game chat to IRC
     *
     * @param source
     * @param name
     * @param template
     * @param message
     * @return
     */
    public String dynmapWebChatToIRCTokenizer(String source, String name,
            String template, String message) {
        if (message == null) {
            message = "";
        }

        return plugin.colorConverter.gameColorsToIrc(
                playerTokenizer(name, template)
                .replace("%SOURCE%", source)
                .replace("%MESSAGE%", message));
    }

    /**
     * Game player AFK to IRC
     *
     * @param player
     * @param template
     *
     * @return
     */
    public String gamePlayerAFKTokenizer(MC_Player player, String template) {
        return plugin.colorConverter.gameColorsToIrc(playerTokenizer(player, template));
    }

    /**
     * Game chat to IRC
     *
     * @param template
     * @param message
     * @return
     */
    public String gameChatToIRCTokenizer(String template, String message) {
        return plugin.colorConverter.gameColorsToIrc(template
                .replace("%MESSAGE%", message));
    }

    /**
     * Game kick message to IRC
     *
     * @param player
     * @param template
     * @param reason
     * @param message
     * @return
     */
    public String gameKickTokenizer(MC_Player player, String template, String message, String reason) {
        return plugin.colorConverter.gameColorsToIrc(
                gameChatToIRCTokenizer(player, template, message)
                .replace("%MESSAGE%", message)
                .replace("%REASON%", reason));
    }
    
    public String playerTokenizer(MC_Player player, String message) {
        String pName = player.getName();        
        String displayName = player.getName();
        String playerIP = player.getIPAddress();
        String host = plugin.getHostFromIp(playerIP);
        String worldName = "";
        String worldAlias = "";
        String worldColor = "";
        String jobShort = "";
        String job = "";
        
        if (playerIP == null) {
            playerIP = "";
        }
        if (displayName == null) {
            displayName = "";
        }
        if (player.getWorld() != null) {
            worldName = player.getWorld().getName();            
        }        
        plugin.logDebug("[P]Raw message: " + message);
        return message.replace("%DISPLAYNAME%", displayName)
                .replace("%JOBS%", job)
                .replace("%JOBSSHORT%", jobShort)
                .replace("%NAME%", pName)
                .replace("%PLAYERIP%", playerIP)
                .replace("%HOST%", host)                
                .replace("%WORLDALIAS%", worldAlias)
                .replace("%WORLDCOLOR%", worldColor)
                .replace("%WORLD%", worldName);
    }

    private String playerTokenizer(String player, String message) {
        plugin.logDebug("Tokenizing " + player);
        String worldName = plugin.defaultPlayerWorld;        
        String displayName = plugin.getDisplayName(player);
        String worldAlias = "";
        String worldColor = "";
        String jobShort = "";
        String job = "";
        if (!worldName.isEmpty()) {            
        }        
        plugin.logDebug("[S]Raw message: " + message);
        return message.replace("%DISPLAYNAME%", displayName)
                .replace("%JOBS%", job)
                .replace("%JOBSSHORT%", jobShort)
                .replace("%NAME%", player)                
                .replace("%WORLDALIAS%", worldAlias)
                .replace("%WORLDCOLOR%", worldColor)
                .replace("%WORLD%", worldName);
    }

    private MC_Player getPlayer(String name) {
        MC_Player player = null;
        if (plugin.exactNickMatch) {
            plugin.logDebug("Checking for exact player matching " + name);
            player = plugin.getServer().getOnlinePlayerByName(name);
        } else {
            plugin.logDebug("Checking for player matching " + name);
            for (MC_Player p : plugin.getServer().getPlayers()) {
                if (p.getName().equalsIgnoreCase(name)) {
                    player = p;
                }
            }
        }
        return player;
    }

    /**
     *
     * @param player
     * @param template
     * @param cmd
     * @param params
     * @return
     */
    public String gameCommandToIRCTokenizer(MC_Player player, String template, String cmd, String params) {
        return plugin.colorConverter.gameColorsToIrc(playerTokenizer(player, template)
                .replace("%COMMAND%", cmd)
                .replace("%PARAMS%", params));
    }
    
    /**
     *
     * @param player
     * @param template
     * @param cmd
     * @param params
     * @return
     */
    public String gameCommandToIRCTokenizer(String player, String template, String cmd, String params) {
        return plugin.colorConverter.gameColorsToIrc(playerTokenizer(player, template)
                .replace("%COMMAND%", cmd)
                .replace("%PARAMS%", params));
    }

    public String targetChatResponseTokenizer(String target, String message, String template) {
        return plugin.colorConverter.gameColorsToIrc(template
                .replace("%TARGET%", target)
                .replace("%MESSAGE%", message)
        );
    }

    public String msgChatResponseTokenizer(String target, String message, String template) {
        return plugin.colorConverter.ircColorsToGame(template
                .replace("%TARGET%", target)
                .replace("%MESSAGE%", message)
        );
    }
}
