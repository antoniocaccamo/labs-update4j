package lab.update4j.business;

import org.update4j.Configuration;
import org.update4j.LaunchContext;
import org.update4j.service.Launcher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Business implements Launcher {


    public static void main(String[] args) {

        log.warn("should not start form here...");
        new Business().run( null);
    }


    /**
     * Called when the business application should be launched, after injection was
     * performed (see {@link Configuration#launch(Injectable)}) .
     *
     * <p>
     * To do any initialization before injection, do it in the constructor, but be
     * aware that unless you specify the launcher in the {@code launcher} section of
     * the configuration, the constructor might be called even if this provider will
     * not be used in the end. This happens as the service loading mechanism first
     * loads all providers and then compares versions to use the one with the
     * highest version.
     *
     * @param context Context information about the launch.
     */
    @Override
    public void run(LaunchContext context) {
        log.info("running business application !!!!");
    }
}
