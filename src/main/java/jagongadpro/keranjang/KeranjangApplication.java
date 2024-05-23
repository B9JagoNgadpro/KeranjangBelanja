package jagongadpro.keranjang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import jagongadpro.keranjang.config.GameApiProperties;

@EnableScheduling
@EnableAsync
@SpringBootApplication
@EnableConfigurationProperties(GameApiProperties.class)
public class KeranjangApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeranjangApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
