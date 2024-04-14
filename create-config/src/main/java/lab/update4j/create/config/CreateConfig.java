package lab.update4j.create.config;

import lombok.extern.slf4j.Slf4j;

import org.update4j.Configuration;
import org.update4j.FileMetadata;
import org.update4j.FileMetadata.Reference;

import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import lombok.extern.java.Log;

@Slf4j
public class CreateConfig {

    private static final String BASE_URI = "https://raw.githubusercontent.com/ruangustavo/update4j-without-ui/master/build/";
    private static final String CONFIG_DIR = System.getProperty("user.dir") + "/dist";
    private static final String BUSINESS_DIR = CONFIG_DIR + "/business";
    private static final String BOOTSTRAP_DIR = CONFIG_DIR + "/bootstrap";
    private static final String MAVEN_BASE = "https://repo1.maven.org/maven2";

    public static void main(String[] args) {


//        Configuration bootstrapConfig = getBootstrapConfig();
//        writeTo(CONFIG_DIR + "/setup.xml", bootstrapConfig);
//
//
//        Configuration businessConfig = getUpdateConfig();
//        writeTo(BUSINESS_DIR + "/config.xml", businessConfig);

        log.info("Config.getInstance().remoteUrl()                  : {}", Config.getInstance().remoteUrl() );
        log.info("Config.getInstance().configDir()                  : {}", Config.getInstance().configDir() );
        log.info("Config.getInstance().getBootstrap().configDir()   : {}", Config.getInstance().getBootstrap().configDir() );
        log.info("Config.getInstance().getBootstrap().classpath()   : {}", Config.getInstance().getBootstrap().classpath() );
        log.info("Config.getInstance().getBusiness().configDir()    : {}", Config.getInstance().getBusiness().configDir() );

        Config config = Config.getInstance();



        writeTo(BUSINESS_DIR + "/config.xml", buildBusinessConfiguration(config));
        writeTo(config.setupFile(), buildBootstrapConfiguration(config));
    }


    private static Configuration buildBusinessConfiguration(Config config) {
        log.info("buildBusinessConfiguration .. ");
        Configuration.Builder builder = Configuration.builder()
            .property("default.launcher.main.class", org.update4j.Bootstrap.class.getName())
            .property("maven.central", MAVEN_BASE)
            .basePath("${user.dir}/builder")

        ;
        config.getBusiness().classpath()

                .forEach(s -> {
                    Path path = Path.of(config.configDir(), s).normalize();
                    log.info("{}  exists ? {}", path.toFile().getAbsolutePath(), path.toFile().exists());
                    builder.file(
                            FileMetadata.readFrom( path.toFile().getAbsolutePath())
                                    .classpath()
                                    .uri(  URI.create(config.remoteUrl()).resolve(s))
                    );
                })
        ;
        return builder.build();
    }



    private static Configuration buildBootstrapConfiguration(Config config) {

        log.info("buildBootstrapConfiguration .. ");
        Configuration.Builder builder = Configuration.builder()
            .property("default.launcher.main.class", org.update4j.Bootstrap.class.getName())
            .property("maven.central", MAVEN_BASE)
            .basePath("${user.dir}/bootstrap")
            .file(FileMetadata.readFrom(BUSINESS_DIR + "/config.xml")
                          .uri(URI.create(config.remoteUrl()).resolve("business/config.xml"))
                          .path("../business/config.xml"))
        ;

    
        config.getBootstrap().classpath()

                .forEach(s -> {
                    Path path = Path.of(config.configDir(), s).normalize();
                    log.info("{}  exists ? {}", path.toFile().getAbsolutePath(), path.toFile().exists());
                    builder.file(
                        FileMetadata.readFrom( path.toFile().getAbsolutePath())
                        .classpath()
                        .uri(  URI.create(config.remoteUrl()).resolve(s))
                    );
                })
       ;

        return builder.build();

    }


//    /**
//     * Configuração do Update4j para o bootstrap (setup.xml)
//     *
//     * @return configuração do Update4j para o bootstrap (setup.xml)
//     */
//    public static Configuration getBootstrapConfig() {
//        URI businessConfigUri = URI.create(BASE_URI).resolve("business/config.xml");
//        URI bootstrapJarUri = URI.create(BASE_URI).resolve("bootstrap/bootstrap-1.0.0.jar");
//
//        return Configuration.builder()
//                       .basePath("${user.dir}/bootstrap")
//                       .file(FileMetadata.readFrom(BUSINESS_DIR + "/config.xml").uri(businessConfigUri).path("../business/config.xml"))
//                       .file(FileMetadata.readFrom(BOOTSTRAP_DIR + "/bootstrap-1.0.0.jar").classpath().uri(bootstrapJarUri))
//                       .property("default.launcher.main.class", "org.update4j.Bootstrap")
//                       .property("maven.central", MAVEN_BASE)
//                       .build();
//    }

//    /**
//     * Configuração do Update4j para o a atualização da aplicação de negócio (config.xml)
//     *
//     * @return configuração do Update4j para o a aplicação de negócio
//     */
//    public static Configuration getUpdateConfig() {
//        return Configuration.builder()
//                       .basePath("${user.dir}/business")
//                       .baseUri(BASE_URI + "business")
//                       .property("maven.central", MAVEN_BASE)
//                       .file(FileMetadata.readFrom(BUSINESS_DIR + "/business-1.0.0.jar").path("business-1.0.0.jar").classpath())
//                       .files(FileMetadata.streamDirectory(BUSINESS_DIR).filter(CreateConfig::isImage))
//                       .build();
//    }

    /**
     * @param output caminho o qual o arquivo de configuração será escrito
     * @param config configuração do Update4j
     */
    public static void writeTo(String output, Configuration config) {
        try (Writer out = Files.newBufferedWriter(Paths.get(output))) {
            config.write(out);
        } catch (IOException e) {
            System.out.printf("Error writing config file: %s%n", e.getMessage());
        }
    }

    private static boolean isImage(Reference file) {
        return Stream.of("png", "jpeg", "jpg")
                       .anyMatch(extension -> file.getSource().toString().endsWith(extension));
    }
}