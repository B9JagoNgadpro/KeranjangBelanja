package jagongadpro.keranjang;

import jagongadpro.keranjang.config.GameApiProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootTest
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties(GameApiProperties.class)
public class KeranjangApplicationTest {

    @Test
    public void contextLoads() {

    }
}
