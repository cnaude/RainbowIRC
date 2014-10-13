package RainbowIRC;

import PluginReference.ChatColor;
import PluginReference.MC_DamageType;
import PluginReference.MC_EventInfo;
import RainbowIRC.Commands.IRCCommandInterface;
import RainbowIRC.Utilities.CaseInsensitiveMap;
import RainbowIRC.Utilities.ChatTokenizer;
import RainbowIRC.Utilities.ColorConverter;
import RainbowIRC.Utilities.Query;
import RainbowIRC.Utilities.RegexGlobber;
import com.google.common.base.Joiner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.pircbotx.IdentServer;
import PluginReference.MC_Player;
import PluginReference.MC_Server;
import PluginReference.PluginBase;
import PluginReference.PluginInfo;
import RainbowIRC.Configuration.Configuration;
import RainbowIRC.Configuration.ConfigurationProvider;
import RainbowIRC.Configuration.YamlConfiguration;
import RainbowIRC.Utilities.IRCMessageHandler;
import static java.lang.StrictMath.log;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Chris Naudé
 */
public class MyPlugin extends PluginBase {

    public static MC_Server server = null;

    public String LOG_HEADER_INFO;
    public String LOG_HEADER_ERROR;
    public String LOG_HEADER_DEBUG;
    public String LOG_HEADER_F;
    //static final Logger log = Logger.getLogger("Minecraft");
    private final String sampleFileName;
    private final String MAINCONFIG;
    private File botsFolder;
    private File configFile;
    public static long startTime;
    public boolean identServerEnabled;
    private final CaseInsensitiveMap<HashMap<String, String>> messageTmpl;
    public String defaultPlayerSuffix,
            defaultPlayerPrefix,
            defaultPlayerGroup,
            defaultGroupPrefix,
            defaultPlayerWorld,
            defaultGroupSuffix,
            customTabPrefix,
            heroChatEmoteFormat,
            listFormat,
            listSeparator,
            listPlayer,
            ircNickPrefixIrcOp,
            ircNickPrefixSuperOp,
            ircNickPrefixOp,
            ircNickPrefixHalfOp,
            ircNickPrefixVoice;
    private final CaseInsensitiveMap<String> displayNameCache;

    public ArrayList<String> kickedPlayers = new ArrayList<>();

    public final String invalidBotName = ChatColor.RED + "Invalid bot name: "
            + ChatColor.WHITE + "%BOT%"
            + ChatColor.RED + ". Type '" + ChatColor.WHITE + "/irc listbots"
            + ChatColor.RED + "' to see valid bots.";

    public final String invalidChannelName = ChatColor.RED + "Invalid channel name: "
            + ChatColor.WHITE + "%CHANNEL%";

    public final String invalidChannel = ChatColor.RED + "Invalid channel: "
            + ChatColor.WHITE + "%CHANNEL%";
    public final String noPermission = ChatColor.RED + "You do not have permission to use this command.";

    private boolean debugEnabled;
    private boolean stripGameColors;
    private boolean stripIRCColors;
    private boolean stripIRCBackgroundColors;
    private boolean listSortByName;
    public boolean exactNickMatch;
    public boolean ignoreChatCancel;
    public Long ircConnCheckInterval;
    public Long ircChannelCheckInterval;
    public ChannelWatcher channelWatcher;
    public ColorConverter colorConverter;
    public RegexGlobber regexGlobber;
    public CaseInsensitiveMap<RainbowBot> ircBots;
    private BotWatcher botWatcher;
    public IRCMessageHandler ircMessageHandler;
    public CommandHandlers commandHandlers;
    public CommandQueueWatcher commandQueue;
    Configuration mainConfig;

    public ChatTokenizer tokenizer;
    private final File cacheFile;
    public HashMap<String, IRCCommandInterface> commands;
    public ArrayList<String> sortedCommands;
    private final Map<String, String> hostCache;
    private final Description pluginDescription;
    private final File pluginFolder;

    public MyPlugin() {
        this.pluginDescription = new Description();
        this.pluginFolder = new File("plugins_mod" + File.separator + pluginDescription.getName() + File.separator);
        this.cacheFile = new File(this.pluginFolder, "displayName.cache");
        this.sortedCommands = new ArrayList<>();
        this.commands = new HashMap<>();
        this.MAINCONFIG = "MAIN-CONFIG";
        this.sampleFileName = "SampleBot.yml";
        this.ircBots = new CaseInsensitiveMap<>();
        this.messageTmpl = new CaseInsensitiveMap<>();
        this.displayNameCache = new CaseInsensitiveMap<>();
        this.hostCache = new HashMap<>();

    }

    /**
     *
     * @param argServer
     */
    @Override
    public void onStartup(MC_Server argServer) {
        server = argServer;        
        LOG_HEADER_INFO = "[" + pluginDescription.getName() + "/INFO]";
        LOG_HEADER_ERROR = "[" + pluginDescription.getName() + "/ERROR]";
        LOG_HEADER_DEBUG = "[" + pluginDescription.getName() + "/DEBUG]";
        LOG_HEADER_F = ChatColor.DARK_PURPLE + "[" + pluginDescription.getName() + "]" + ChatColor.WHITE;
        logInfo("Starting " + getDescription().getName() + " version " + getDescription().getVersion());

        botsFolder = new File(pluginFolder + "/bots");
        configFile = new File(pluginFolder, "config.yml");
        createConfig();
        loadConfig();
        loadDisplayNameCache();
        if (identServerEnabled) {
            logInfo("Starting Ident Server ...");
            try {
                IdentServer.startServer();
            } catch (Exception ex) {
                logError(ex.getMessage());
            }
        }
        regexGlobber = new RegexGlobber();
        tokenizer = new ChatTokenizer(this);
        commandHandlers = new CommandHandlers(this);
        getServer().registerCommand(commandHandlers);
        loadBots();
        createSampleBot();
        channelWatcher = new ChannelWatcher(this);
        botWatcher = new BotWatcher(this);
        ircMessageHandler = new IRCMessageHandler(this);
        commandQueue = new CommandQueueWatcher(this);
        logInfo(getDescription().getName() + " by cnaude is now enabled.");
    }

    /**
     *
     */
    @Override
    public void onShutdown() {
        if (channelWatcher != null) {
            logDebug("Disabling channelWatcher ...");
            channelWatcher.cancel();
        }
        if (botWatcher != null) {
            logDebug("Disabling botWatcher ...");
            botWatcher.cancel();
        }
        if (ircBots.isEmpty()) {
            logInfo("No IRC bots to disconnect.");
        } else {
            logInfo("Disconnecting IRC bots.");
            commandQueue.cancel();
            for (RainbowBot ircBot : ircBots.values()) {
                ircBot.quit();
            }
            ircBots.clear();
        }
        if (identServerEnabled) {
            logInfo("Stopping Ident Server");
            try {
                IdentServer.stopServer();
            } catch (IOException ex) {
                logError(ex.getMessage());
            }
        }
        saveDisplayNameCache();
    }

    /**
     *
     * @return
     */
    public boolean debugMode() {
        return debugEnabled;
    }

    public String getMsgTemplate(String botName, String tmpl) {
        if (messageTmpl.containsKey(botName)) {
            if (messageTmpl.get(botName).containsKey(tmpl)) {
                return messageTmpl.get(botName).get(tmpl);
            }
        }
        if (messageTmpl.get(MAINCONFIG).containsKey(tmpl)) {
            return messageTmpl.get(MAINCONFIG).get(tmpl);
        }
        return "INVALID TEMPLATE";
    }

    public String getMsgTemplate(String tmpl) {
        return getMsgTemplate(MAINCONFIG, tmpl);
    }

    public void loadCustomColors(Configuration config) {

        for (String t : config.getSection("irc-color-map").getKeys()) {
            colorConverter.addIrcColorMap(t, config.getString("irc-color-map." + t));
        }
        for (String t : config.getSection("game-color-map").getKeys()) {
            colorConverter.addGameColorMap(t, config.getString("game-color-map." + t));
        }
    }

    public void loadTemplates(Configuration config, String configName) {
        messageTmpl.put(configName, new HashMap<String, String>());

        if (config.getString("message-format") != null) {
            for (String t : config.getSection("message-format").getKeys()) {
                if (!t.startsWith("MemorySection")) {
                    messageTmpl.get(configName).put(t, colorConverter.translateAlternateColorCodes('&',
                            config.getString("message-format." + t, "")));
                    logDebug("message-format: " + t + " => " + messageTmpl.get(configName).get(t));
                }
            }
        } else {
            logDebug("No message-format section found for " + configName);
        }
    }

    private void loadConfig() {
        try {
            mainConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException ex) {
            Logger.getLogger(MyPlugin.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        debugEnabled = mainConfig.getBoolean("Debug");
        identServerEnabled = mainConfig.getBoolean("enable-ident-server");
        logDebug("Debug enabled");
        stripGameColors = mainConfig.getBoolean("strip-game-colors", false);
        stripIRCColors = mainConfig.getBoolean("strip-irc-colors", false);
        stripIRCBackgroundColors = mainConfig.getBoolean("strip-irc-bg-colors", true);
        exactNickMatch = mainConfig.getBoolean("nick-exact-match", true);
        ignoreChatCancel = mainConfig.getBoolean("ignore-chat-cancel", false);
        colorConverter = new ColorConverter(this, stripGameColors, stripIRCColors, stripIRCBackgroundColors);
        logDebug("strip-game-colors: " + stripGameColors);
        logDebug("strip-irc-colors: " + stripIRCColors);

        loadTemplates(mainConfig, MAINCONFIG);
        loadCustomColors(mainConfig);

        defaultPlayerSuffix = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("message-format.default-player-suffix", ""));
        defaultPlayerPrefix = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("message-format.default-player-prefix", ""));
        defaultPlayerGroup = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("message-format.default-player-group", ""));
        defaultGroupSuffix = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("message-format.default-group-suffix", ""));
        defaultGroupPrefix = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("message-format.default-group-prefix", ""));
        defaultPlayerWorld = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("message-format.default-player-world", ""));

        ircNickPrefixIrcOp = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("nick-prefixes.ircop", "~"));
        ircNickPrefixSuperOp = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("nick-prefixes.ircsuperop", "&&"));
        ircNickPrefixOp = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("nick-prefixes.op", "@"));
        ircNickPrefixHalfOp = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("nick-prefixes.halfop", "%"));
        ircNickPrefixVoice = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("nick-prefixes.voice", "+"));

        listFormat = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("list-format", ""));
        listSeparator = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("list-separator", ""));
        listPlayer = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("list-player", ""));
        listSortByName = mainConfig.getBoolean("list-sort-by-name", true);

        ircConnCheckInterval = mainConfig.getLong("conn-check-interval");
        ircChannelCheckInterval = mainConfig.getLong("channel-check-interval");

        customTabPrefix = colorConverter.translateAlternateColorCodes('&', mainConfig.getString("custom-tab-prefix", "[IRC] "));
        logDebug("custom-tab-prefix: " + customTabPrefix);
    }

    private void loadBots() {
        if (botsFolder.exists()) {
            logInfo("Checking for bot files in " + botsFolder);
            for (final File file : botsFolder.listFiles()) {
                if (file.getName().endsWith(".yml")) {
                    logInfo("Loading bot file: " + file.getName());
                    RainbowBot pircBot = new RainbowBot(file, this);
                    ircBots.put(file.getName(), pircBot);
                    logInfo("Loaded bot: " + pircBot.botNick);
                }
            }
        }
    }

    private void createSampleBot() {
        File file = new File(pluginFolder + "/" + sampleFileName);
        try {
            try (InputStream in = MyPlugin.class.getResourceAsStream("/" + sampleFileName)) {
                byte[] buf = new byte[1024];
                int len;
                try (OutputStream out = new FileOutputStream(file)) {
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
            }
        } catch (IOException ex) {
            logError("Problem creating sample bot: " + ex.getMessage());
        }
    }

    private void createSampleConfig() {
        if (!configFile.exists()) {
            try {
                try (InputStream in = MyPlugin.class.getResourceAsStream("/config.yml")) {
                    byte[] buf = new byte[1024];
                    int len;
                    try (OutputStream out = new FileOutputStream(configFile)) {
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                    }
                }
            } catch (IOException ex) {
                logError("Problem creating sample config: " + ex.getMessage());
            }
        }
    }

    /**
     *
     * @param sender
     */
    public void reloadMainConfig(MC_Player sender) {
        sender.sendMessage(LOG_HEADER_F + " Reloading config.yml ...");
        loadConfig();
        sender.sendMessage(LOG_HEADER_F + ChatColor.WHITE + " Done.");
    }

    private void createConfig() {
        if (!pluginFolder.exists()) {
            try {
                pluginFolder.mkdir();
            } catch (Exception e) {
                logError(e.getMessage());
            }
        }

        if (!configFile.exists()) {
            createSampleConfig();
        }

        if (!botsFolder.exists()) {
            try {
                botsFolder.mkdir();
            } catch (Exception e) {
                logError(e.getMessage());
            }
        }
    }

    /**
     *
     * @param message
     */
    public void logInfo(String message) {
        //log.log(Level.INFO, String.format("%s %s", LOG_HEADER, message));
        System.out.println(String.format("%s %s %s ", getLogPrefix(), LOG_HEADER_INFO, message));
    }

    /**
     *
     * @param message
     */
    public void logError(String message) {
        //log.log(Level.SEVERE, String.format("%s %s", LOG_HEADER, message));
        System.out.println(String.format("%s %s %s ", getLogPrefix(), LOG_HEADER_ERROR, message));
    }

    /**
     *
     * @param message
     */
    public void logDebug(String message) {
        if (debugEnabled) {
            //log.log(Level.INFO, String.format("%s [DEBUG] %s", LOG_HEADER, message));
            System.out.println(String.format("%s %s %s ", getLogPrefix(), LOG_HEADER_DEBUG, message));
        }
    }

    /**
     *
     * @return
     */
    public String getMCUptime() {
        long jvmUptime = ManagementFactory.getRuntimeMXBean().getUptime();
        String msg = "Server uptime: " + (int) (jvmUptime / 86400000L) + " days"
                + " " + (int) (jvmUptime / 3600000L % 24L) + " hours"
                + " " + (int) (jvmUptime / 60000L % 60L) + " minutes"
                + " " + (int) (jvmUptime / 1000L % 60L) + " seconds.";
        return msg;
    }

    /**
     *
     * @param ircBot
     * @param channelName
     * @return
     */
    public String getMCPlayers(RainbowBot ircBot, String channelName) {
        Map<String, String> playerList = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (MC_Player player : getServer().getPlayers()) {
            //if (ircBot.hideListWhenVanished.get(channelName)) {
            //    logDebug("List: Checking if player " + player.getName() + " is vanished.");
            //    if (vanishHook.isVanished(player)) {
            //        logDebug("Not adding player to list command" + player.getName() + " due to being vanished.");
            //        continue;
            //    }
            //}
            String pName = tokenizer.playerTokenizer(player, listPlayer);
            playerList.put(player.getName(), pName);
        }

        String pList;
        if (!listSortByName) {
            // sort as before
            ArrayList<String> tmp = new ArrayList<>(playerList.values());
            Collections.sort(tmp, Collator.getInstance());
            pList = Joiner.on(listSeparator).join(tmp);
        } else {
            // sort without nick prefixes 
            pList = Joiner.on(listSeparator).join(playerList.values());
        }

        String msg = listFormat
                .replace("%COUNT%", Integer.toString(playerList.size()))
                .replace("%MAX%", "00")
                .replace("%PLAYERS%", pList);
        logDebug("L: " + msg);
        return colorConverter.gameColorsToIrc(msg);
    }

    public String getRemotePlayers(String commandArgs) {
        if (commandArgs != null) {
            String host;
            int port = 25565;
            if (commandArgs.contains(":")) {
                host = commandArgs.split(":")[0];
                port = Integer.parseInt(commandArgs.split(":")[1]);
            } else {
                host = commandArgs;
            }
            Query query = new Query(host, port);
            try {
                query.sendQuery();
            } catch (IOException ex) {
                return ex.getMessage();
            }
            String players[] = query.getOnlineUsernames();
            for (String s : query.getValues().keySet()) {
                logDebug(s + " => " + query.getValues().get(s));
            }
            String m;
            if (players.length == 0) {
                m = "There are no players on " + host + ":" + port;
            } else {
                m = "Players on " + host + "(" + players.length + "): " + Joiner.on(", ").join(players);
            }
            return m;
        } else {
            return "Invalid host.";
        }
    }

    /**
     *
     * @param pName
     * @return
     */
    public String getDisplayName(String pName) {
        String displayName = null;
        MC_Player player = getPlayer(pName);
        logDebug("player: " + player);
        if (player != null) {
            //displayName = player.getDisplayName();
            displayName = pName;
        }
        if (displayName != null) {
            logDebug("Caching displayName for " + pName + " = " + displayName);
            displayNameCache.put(pName, displayName);
        } else if (displayNameCache.containsKey(pName)) {
            displayName = displayNameCache.get(pName);
        } else {
            displayName = pName;
        }
        return displayName;
    }

    /**
     *
     * @param player
     */
    public void updateDisplayNameCache(MC_Player player) {
        //logDebug("Caching displayName for " + player.getName() + " = " + player.getDisplayName());
        //displayNameCache.put(player.getName(), player.getDisplayName());
    }

    /**
     *
     * @param player
     * @param displayName
     */
    public void updateDisplayNameCache(String player, String displayName) {
        logDebug("Caching displayName for " + player + " = " + displayName);
        displayNameCache.put(player, displayName);
    }

    public void saveDisplayNameCache() {
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(cacheFile));
        } catch (IOException ex) {
            logError(ex.getMessage());
            return;
        }

        try {
            for (String s : displayNameCache.keySet()) {
                logDebug("Saving to displayName.cache: " + s + "\t" + displayNameCache.get(s));
                writer.write(s + "\t" + displayNameCache.get(s) + "\n");
            }
            writer.close();
        } catch (IOException ex) {
            logError(ex.getMessage());
        }
    }

    public void loadDisplayNameCache() {
        try {
            try (BufferedReader in = new BufferedReader(new FileReader(cacheFile))) {
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.equals("\n")) {
                        continue;
                    }
                    String[] parts = line.split("\t", 2);
                    updateDisplayNameCache(parts[0], parts[1]);
                }
            }
        } catch (IOException | NumberFormatException e) {
            logError(e.getMessage());
        }
    }

    /**
     *
     * @param message
     * @param permission
     */
    public void broadcast(String message, String permission) {
        for (MC_Player player : getServer().getPlayers()) {
            if (player.hasPermission(permission)) {
                player.sendMessage(message);
            }
        }
    }

    public MC_Player getPlayer(String name) {
        for (MC_Player player : getServer().getPlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    /**
     *
     * @param debug
     */
    public void debugMode(boolean debug) {
        debugEnabled = debug;
        mainConfig.set("Debug", debug);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(mainConfig, configFile);
        } catch (IOException ex) {
            logError("Problem saving to " + configFile.getName() + ": " + ex.getMessage());
        }
    }

    public MC_Server getServer() {
        return server;
    }

    public Description getDescription() {
        return pluginDescription;
    }

    public String getHostFromIp(String playerIP) {
        if (hostCache.containsKey(playerIP)) {
            return hostCache.get(playerIP);
        }
        InetAddress addr = null;
        try {
            addr = InetAddress.getByName(playerIP);
        } catch (UnknownHostException ex) {
            logError(ex.getMessage());
        }
        String host = "UnknownHost";
        if (addr != null) {
            host = addr.getHostName();
            hostCache.put(playerIP, host);
        }
        return host;
    }

    public void clearHostCache(String playerIP) {
        if (hostCache.containsKey(playerIP)) {
            hostCache.remove(playerIP);
        }
    }

    @Override
    public void onPlayerLogin(final String playerName, UUID uuid, final String ip) {
        String displayName = playerName;

        logDebug("PlayerJoinEvent: " + displayName);
        if (kickedPlayers.contains(playerName)) {
            kickedPlayers.remove(playerName);
            logDebug("Removing player " + playerName + " from the recently kicked list.");
        }

        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                clearHostCache(ip);
                for (RainbowBot ircBot : ircBots.values()) {
                    ircBot.gameJoin(playerName, "Player " + playerName + " has joined the game.");
                }
                //updateDisplayNameCache(playerName);
            }
        }, 2, TimeUnit.SECONDS);
    }

    /**
     *
     * @param playerName
     * @param uuid
     */
    @Override
    public void onPlayerLogout(String playerName, UUID uuid) {
        MC_Player player = getServer().getOnlinePlayerByName(playerName);
        logDebug("QUIT: " + playerName);
        if (kickedPlayers.contains(playerName)) {
            kickedPlayers.remove(playerName);
            logDebug("Player " + playerName + " was in the recently kicked list. Not sending quit message.");
            return;
        }
        for (RainbowBot ircBot : ircBots.values()) {
            ircBot.gameQuit(player, "Player " + playerName + " has logged out of the game.");
        }
    }

    /**
     *
     * @param plrVictim
     * @param plrKiller
     * @param dmgType
     * @param deathMsg
     */
    @Override
    public void onPlayerDeath(MC_Player plrVictim, MC_Player plrKiller, MC_DamageType dmgType, String deathMsg) {
        for (RainbowBot ircBot : ircBots.values()) {
            ircBot.gameDeath(plrVictim, deathMsg, TemplateName.GAME_DEATH);
        }
    }

    /**
     *
     * @param plr
     * @param msg
     * @param ei
     */
    @Override
    public void onPlayerInput(MC_Player plr, String msg, MC_EventInfo ei) {
        if (msg.startsWith("/")) {
            if (plr.hasPermission("irc.message.gamechat")) {
                if (msg.startsWith("/me ")) {
                    for (RainbowBot ircBot : ircBots.values()) {
                        ircBot.gameAction(plr, msg.replace("/me", ""));
                    }
                } else if (msg.startsWith("/broadcast ")) {
                    for (RainbowBot ircBot : ircBots.values()) {
                        ircBot.gameBroadcast(plr, msg.replace("/broadcast", ""));
                    }
                }
            }

            for (RainbowBot ircBot : ircBots.values()) {
                if (msg.startsWith("/")) {
                    String cmd;
                    String params = "";
                    if (msg.contains(" ")) {
                        cmd = msg.split(" ", 2)[0];
                        params = msg.split(" ", 2)[1];
                    } else {
                        cmd = msg;
                    }
                    cmd = cmd.substring(0);
                    if (ircBot.channelCmdNotifyEnabled && !ircBot.channelCmdNotifyIgnore.contains(cmd)) {
                        ircBot.commandNotify(plr, cmd, params);
                    }
                }
            }
        } else {
            if (plr.hasPermission("irc.message.gamechat")) {
                logDebug("Player " + plr.getName() + " has permission irc.message.gamechat");
                for (RainbowBot ircBot : ircBots.values()) {
                    ircBot.gameChat(plr, msg);
                }
            } else {
                logDebug("Player " + plr.getName() + " does not have irc.message.gamechat permission.");
            }
        }
    }

    @Override
    public PluginInfo getPluginInfo() {
        PluginInfo info = new PluginInfo();
        info.description = (getDescription().getName() + " " + getDescription().getVersion() + " (cnaude.org)");
        info.eventSortOrder = -1000.0D;
        return info;
    }

    public void broadcastMessage(String message, String perm) {
        for (MC_Player player : getServer().getPlayers()) {
            if (player.hasPermission(perm)) {
                player.sendMessage(message);
            }
        }
    }

    public static String getLogPrefix() {
       Calendar cal = Calendar.getInstance();
       return String.format("[%02d:%02d:%02d]", cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
    }

}
