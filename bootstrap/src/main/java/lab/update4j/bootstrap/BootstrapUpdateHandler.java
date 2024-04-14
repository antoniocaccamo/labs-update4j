package lab.update4j.bootstrap;

import org.update4j.*;
import org.update4j.inject.Injectable;
import org.update4j.service.UpdateHandler;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Builder
@RequiredArgsConstructor
public class BootstrapUpdateHandler implements UpdateHandler, Injectable {

    private final Configuration configuration;


    public void update() {
        Path zip = Paths.get("business-update.zip");
        UpdateResult updateResult = this.configuration.update(
                UpdateOptions.archive(zip).updateHandler(BootstrapUpdateHandler.this));
        boolean isUpdateSuccessful = updateResult.getException() == null;

        if (isUpdateSuccessful) {
            try {
                Archive.read(zip).install();
            } catch (IOException e) {
                log.error("error during installation", e);
            }
        }
    }

    /**
     * launching application
     */
    public void launch() {
        log.info("launching business app");
        configuration.launch(this);
    }

    @Override
    public void updateDownloadFileProgress(FileMetadata file, float frac) {
        log.info("Downloading {} : {} %", file.getPath().getFileName(), ((int) (100 * frac)));
    }


    @Override
    public void updateCheckUpdatesProgress(float frac) throws Throwable {
        log.info("update progress : {} %", ((int) (100 * frac)));
    }
}
