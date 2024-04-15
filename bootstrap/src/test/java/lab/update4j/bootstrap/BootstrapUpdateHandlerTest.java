package lab.update4j.bootstrap;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @auhtor antonio.caccamo on 2024-04-15 @ 10:48
 */
class BootstrapUpdateHandlerTest {

    @Test
    public void when_date_then_ok() {

        System.out.println("business-update.%s.zip".formatted(DateTimeFormatter.ofPattern("yyyy_MM_DD_HH_mm_ss_SSS").format(LocalDateTime.now())));
    }

}