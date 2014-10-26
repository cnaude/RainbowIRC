package RainbowIRC;

import joebkt.CommandResultTypeMaybe;
import joebkt.CommandSender;
import joebkt.EntityGeneric;
import joebkt.IntegerCoordinates;
import joebkt.TextObject;
import joebkt.Vec3D;
import joebkt.World;
import joebkt.di_BaseException;

/**
 *
 * @author Chris Naude We have to implement our own CommandSender so that we can
 * receive output from the command dispatcher.
 */
public class IRCCommandSender implements CommandSender {

    private final RainbowBot ircBot;
    private final String target;
    private final MyPlugin plugin;
    private final boolean ctcpResponse;

    /**
     *
     * @param message
     */
    public void sendMessage(String message) {
        plugin.logDebug("sendMessage[single]: " + message);
        ircBot.messageQueue.add(new IRCMessage(target,
                plugin.colorConverter.gameColorsToIrc(message), ctcpResponse));
    }

    /**
     *
     * @param messages
     */
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            plugin.logDebug("sendMessage[multi]: " + message);
            ircBot.messageQueue.add(new IRCMessage(target,
                    plugin.colorConverter.gameColorsToIrc(message), ctcpResponse));
        }
    }

    /**
     *
     * @param ircBot
     * @param target
     * @param plugin
     * @param ctcpResponse
     */
    public IRCCommandSender(RainbowBot ircBot, String target, MyPlugin plugin, boolean ctcpResponse) {
        this.target = target;
        this.ircBot = ircBot;
        this.plugin = plugin;
        this.ctcpResponse = ctcpResponse;
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public TextObject e_() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /*
    * Result of command...
    */
    @Override
    public void sendMessageObj(TextObject to) {
        sendMessage(to.getText1());        
    }

    /*
    * hasPermission
    */
    @Override
    public boolean a(int i, String string) {
        return true;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public IntegerCoordinates c() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public World getWorld() {
        //return World(plugin.getServer().getWorld(0));
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EntityGeneric f() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean doesCommandProduceFeedback() {
        return true;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void a(CommandResultTypeMaybe crtm, int i) throws di_BaseException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vec3D d() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
