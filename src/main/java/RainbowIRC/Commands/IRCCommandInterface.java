package RainbowIRC.Commands;

import PluginReference.MC_Player;

/**
 *
 * @author cnaude
 */
public interface IRCCommandInterface {
    void dispatch(MC_Player sender, String[] args);
    String name();
    String desc();
    String usage();    
}
