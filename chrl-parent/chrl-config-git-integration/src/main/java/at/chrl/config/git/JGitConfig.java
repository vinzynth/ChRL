package at.chrl.config.git;

import at.chrl.nutils.configuration.Property;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ChRL on 11.02.16.
 * Project: ChRL
 * Package: at.chrl.config.git
 * <p>
 * <br>
 */
public class JGitConfig {

    @Property(key = "at.chrl.config.git.repo", defaultValue = "", description = "Git Remote URL. Must start with http (no ssh supported yet). Example: http://repo.bravestone.at/bravestone/config.git")
    public static String REPO;

    @Property(key = "at.chrl.config.git.repo.username", defaultValue = "")
    public static String USERNAME;

    @Property(key = "at.chrl.config.git.repo.password", defaultValue = "")
    public static String PASSWORD;

    @Property(key = "at.chrl.config.git.hostname", defaultValue = "", description = "Hostname - Used for git Branch")
    public static String HOSTNAME;

    static {
        try {
            HOSTNAME = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            System.out.println("Can not resolve hostname");
            e.printStackTrace();
        }
    }
}
