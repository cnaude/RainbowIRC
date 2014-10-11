package RainbowIRC;

/**
 *
 * @author cnaude
 */
public class Description {
    
    private final String description, name, version, url;
    
    public Description(String description, String name, String version, String url) {
        this.description = description;
        this.name = name;
        this.version = version;
        this.url = url;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getVersion() {
        return version;
    }
    
    public String getURL() {
        return url;
    }
    
}