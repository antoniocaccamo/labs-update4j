package lab.update4j.create.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputFilter;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
public class Config {

    private static  String FILE = "create.config.properties";

    private static final String  _RemoteUrl = "config.remote.url";
    private static final String  _ConfigDir = "config.dir";
    private static final String  _SetupFile = "config.setup.file";

    private static final Config config;

    static {
        try {
            config = new Config();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Config getInstance() {
        return config;
    }



    private final Properties props;

    @Getter
    private final Bootstrap bootstrap;

    @Getter
    private final Business business;


    private Config() throws IOException {
        props = new Properties();
        props.load(new FileReader( FILE ));
        log.info("props: {}", props);

        bootstrap = new Bootstrap(props);
        business = new Business(props);

    }


    public String configDir() {
        return props.getProperty(Config._ConfigDir);
    }

    public String remoteUrl() {
        return props.getProperty(Config._RemoteUrl);
    }

    public String setupFile() {
        return props.getProperty(Config._SetupFile);
    }


    protected static class Bootstrap {

        private static final String  _ConfigDir = "config.bootstrap.dir";
        private static final String _Classpath  = "config.bootstrap.classpath.";

        private final Properties props;

        protected Bootstrap(Properties props) {
            this.props = props;
        }

        public String configDir() {
            return props.getProperty(Bootstrap._ConfigDir);
        }

        public List<String> classpath() {

            return props.entrySet().stream()
                    .filter( entry -> ((String)entry.getKey()).startsWith(_Classpath))
                    .map( entry-> (String) entry.getValue())
                    .collect(Collectors.toUnmodifiableList());
        }
    }


    protected static class Business {
        private static final String  _ConfigDir = "config.business.dir";
        private static final String _Classpath  = "config.business.classpath.";

        private final Properties props;


        public Business(Properties props) {
            this.props=props;
        }

        public String configDir() {
            return props.getProperty(Business._ConfigDir);
        }

        public List<String> classpath() {

            return props.entrySet().stream()
                           .filter( entry -> ((String)entry.getKey()).startsWith(_Classpath))
                           .map( entry-> (String) entry.getValue())
                           .collect(Collectors.toUnmodifiableList());
        }

    }
}
