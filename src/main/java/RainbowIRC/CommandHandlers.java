package RainbowIRC;

import RainbowIRC.Commands.Debug;
import RainbowIRC.Commands.RemoveOp;
import RainbowIRC.Commands.ListOps;
import RainbowIRC.Commands.ListBots;
import RainbowIRC.Commands.Whois;
import RainbowIRC.Commands.ReloadBotConfigs;
import RainbowIRC.Commands.Save;
import RainbowIRC.Commands.MuteList;
import RainbowIRC.Commands.Send;
import RainbowIRC.Commands.ListVoices;
import RainbowIRC.Commands.Notice;
import RainbowIRC.Commands.Leave;
import RainbowIRC.Commands.RemoveVoice;
import RainbowIRC.Commands.DeOp;
import RainbowIRC.Commands.Connect;
import RainbowIRC.Commands.Msg;
import RainbowIRC.Commands.CTCP;
import RainbowIRC.Commands.DeVoice;
import RainbowIRC.Commands.SendRaw;
import RainbowIRC.Commands.ReloadBots;
import RainbowIRC.Commands.Server;
import RainbowIRC.Commands.Voice;
import RainbowIRC.Commands.ReloadConfig;
import RainbowIRC.Commands.Reload;
import RainbowIRC.Commands.ReloadBot;
import RainbowIRC.Commands.List;
import RainbowIRC.Commands.Op;
import RainbowIRC.Commands.AddOp;
import RainbowIRC.Commands.Nick;
import RainbowIRC.Commands.Mute;
import RainbowIRC.Commands.Kick;
import RainbowIRC.Commands.Motd;
import RainbowIRC.Commands.Join;
import RainbowIRC.Commands.AddVoice;
import RainbowIRC.Commands.MessageDelay;
import RainbowIRC.Commands.Disconnect;
import RainbowIRC.Commands.ReloadBotConfig;
import RainbowIRC.Commands.Help;
import RainbowIRC.Commands.Topic;
import RainbowIRC.Commands.IRCCommandInterface;
import RainbowIRC.Commands.Login;
import RainbowIRC.Commands.UnMute;
import RainbowIRC.Commands.Say;
import PluginReference.MC_Command;
import PluginReference.MC_Player;
import RainbowIRC.Commands.Load;
import RainbowIRC.Commands.Unload;
import com.google.common.base.Joiner;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author cnaude
 */
public class CommandHandlers implements MC_Command {

    public HashMap<String, IRCCommandInterface> commands = new HashMap<>();
    public ArrayList<String> sortedCommands = new ArrayList<>();
    private final MyPlugin plugin;

    public CommandHandlers(MyPlugin plugin) {
        this.plugin = plugin;

        commands.put("addop", new AddOp(plugin));
        commands.put("addvoice", new AddVoice(plugin));
        commands.put("connect", new Connect(plugin));
        commands.put("ctcp", new CTCP(plugin));
        commands.put("deop", new DeOp(plugin));
        commands.put("devoice", new DeVoice(plugin));
        commands.put("debug", new Debug(plugin));
        commands.put("disconnect", new Disconnect(plugin));
        commands.put("join", new Join(plugin));
        commands.put("kick", new Kick(plugin));
        commands.put("leave", new Leave(plugin));
        commands.put("list", new List(plugin));
        commands.put("listbots", new ListBots(plugin));
        commands.put("listops", new ListOps(plugin));
        commands.put("listvoices", new ListVoices(plugin));
        commands.put("load", new Load(plugin));
        commands.put("login", new Login(plugin));
        commands.put("messagedelay", new MessageDelay(plugin));
        commands.put("msg", new Msg(plugin));
        commands.put("motd", new Motd(plugin));
        commands.put("mute", new Mute(plugin));
        commands.put("mutelist", new MuteList(plugin));
        commands.put("nick", new Nick(plugin));
        commands.put("notice", new Notice(plugin));
        commands.put("op", new Op(plugin));
        commands.put("reload", new Reload(plugin));
        commands.put("reloadbot", new ReloadBot(plugin));
        commands.put("reloadbotconfig", new ReloadBotConfig(plugin));
        commands.put("reloadbotconfigs", new ReloadBotConfigs(plugin));
        commands.put("reloadbots", new ReloadBots(plugin));
        commands.put("reloadconfig", new ReloadConfig(plugin));
        commands.put("removeop", new RemoveOp(plugin));
        commands.put("removevoice", new RemoveVoice(plugin));
        commands.put("save", new Save(plugin));
        commands.put("say", new Say(plugin));
        commands.put("send", new Send(plugin));
        commands.put("sendraw", new SendRaw(plugin));
        commands.put("server", new Server(plugin));
        commands.put("topic", new Topic(plugin));
        commands.put("unload", new Unload(plugin));
        commands.put("unmute", new UnMute(plugin));
        commands.put("voice", new Voice(plugin));
        commands.put("whois", new Whois(plugin));
        commands.put("help", new Help(plugin));

        for (String s : commands.keySet()) {
            sortedCommands.add(s);
        }
        Collections.sort(sortedCommands, Collator.getInstance());
        plugin.logDebug("Commands enabled: " + Joiner.on(", ").join(sortedCommands));
    }

    @Override
    public String getCommandName() {
        return "irc";
    }

    @Override
    public ArrayList<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public String getHelpLine(MC_Player plr) {
        return "/irc help";
    }

    @Override
    public ArrayList<String> getTabCompletionList(MC_Player player, String[] args) {
        return null;
    }

    @Override
    public void handleCommand(MC_Player plr, String[] args) {        
        plugin.logDebug("ARGS: " + Joiner.on(" ").join(args));
        if (args.length >= 1) {
            String subCmd = args[0].toLowerCase();
            if (commands.containsKey(subCmd)) {
                if (!plr.hasPermission("irc." + subCmd)) {
                    plr.sendMessage(plugin.noPermission);
                }
                commands.get(subCmd).dispatch(plr, args);
                return;
            }
        }
        commands.get("help").dispatch(plr, args);
    }

    @Override
    public boolean hasPermissionToUse(MC_Player plr) {
        if (plr == null) {
            return true;
        }
        return plr.hasPermission("irc");
    }

}
