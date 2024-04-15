package lab.update4j.bootstrap;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.update4j.Configuration;
import org.update4j.service.Delegate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Bootstrap implements Delegate {


    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
    
        try {
          bootstrap.main(List.of());
        } catch (Throwable throwable) {
          throwable.printStackTrace();
        }
      }

    @Override
    public void main(List<String> arg0) throws Throwable {

        URL configUrl = new URL(Config.getInstance().getBusiness().remoteUrl());
        Configuration config;
        try (Reader in = new InputStreamReader(configUrl.openStream(), StandardCharsets.UTF_8)) {
            log.info("verifying remote config {} for updates ...", Config.getInstance().getBusiness().remoteUrl() );
            config = Configuration.read(in);
        } catch (IOException e) {
            Path path = Paths.get(Config.getInstance().getBusiness().localFile());
            log.warn("Could not load remote config, falling back to local {}", path);
            try (Reader in = Files.newBufferedReader(path)) {
                config = Configuration.read(in);
            }
        }
        BootstrapUpdateHandler bootstrapUpdateHandler = new BootstrapUpdateHandler(config);
        bootstrapUpdateHandler.update();
        bootstrapUpdateHandler.launch();
    }
    
}
