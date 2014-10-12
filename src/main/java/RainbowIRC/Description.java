package RainbowIRC;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cnaude
 */
public class Description {

    Properties prop;
    private final String VERSION_KEY = "version";
    private final String NAME_KEY = "name";
    private final String URL_KEY = "url";
    private final String DESCRIPTION_KEY = "description";

    public Description() {
        prop = new Properties();  
        loadPropFile("plugin.properties");
    }

    public String getDescription() {
        return prop.getProperty(DESCRIPTION_KEY);
    }

    public String getName() {
        return prop.getProperty(NAME_KEY);
    }

    public String getVersion() {
        return prop.getProperty(VERSION_KEY);
    }

    public String getURL() {
        return prop.getProperty(URL_KEY);
    }

    private void loadPropFile(String fileName) {
        InputStream inputStream = MyPlugin.class.getResourceAsStream("/" + fileName);
        
        try {
            prop.load(inputStream);
        } catch (IOException ex) {
            Logger.getLogger(Description.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
