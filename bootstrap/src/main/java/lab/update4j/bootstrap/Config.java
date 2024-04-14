package lab.update4j.bootstrap;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class Config {

    private static  String FILE = "create.config.properties";

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

//    @Getter
//    private final Bootstap bootstap;

    @Getter
    private final Business business;


    private Config() throws IOException {
        InputStream is = Config.class.getClassLoader().getResourceAsStream(FILE);
        props = new Properties();
        props.load(is);
        log.info("props: {}", props);

//      bootstap = new Bootstap(props);
        business = new Business(props);

    }




//    protected static class Bootstap {
//
//        private final Properties props;
//
//        protected Bootstap(Properties props) {
//            this.props = props;
//        }
//    }


    protected static class Business {
        private static final String  _RemoteUrl = "config.business.remote.url";
        private static final String  _LocalFile = "config.business.local.file";

        private final Properties props;


        public Business(Properties props) {
            this.props=props;
        }

        public String remoteUrl() {
            return props.getProperty(Business._RemoteUrl);
        }

        public String localFile() {
            return props.getProperty(Business._LocalFile);
        }
    }
}
