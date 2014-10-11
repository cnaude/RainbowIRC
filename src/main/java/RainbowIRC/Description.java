package RainbowIRC;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 *
 * @author cnaude
 */
public class Description {

    private final String description, name, url, version;

    public Description(String description, String name, String url) {
        this.description = description;
        this.name = name;
        this.url = url;
        this.version = getVersionFromTxt("version.txt");
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

    private String getVersionFromTxt(String fileName) {
        InputStream inputStream = MyPlugin.class.getResourceAsStream("/" + fileName);

        String result;
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            result = scanner.next();
        }

        return result;
    }

}
