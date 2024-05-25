package jagongadpro.keranjang;

import jagongadpro.keranjang.config.GameApiProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@EnableScheduling
@EnableAsync
@EnableConfigurationProperties(GameApiProperties.class)
public class KeranjangApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    public void restTemplateBeanExists() {
        RestTemplate restTemplate = applicationContext.getBean(RestTemplate.class);
        assertNotNull(restTemplate);
    }

    @Test
    public void testMain() {
        try (var mockedSpringApplication = mockStatic(SpringApplication.class)) {
            ConfigurableApplicationContext mockContext = mock(ConfigurableApplicationContext.class);
            mockedSpringApplication.when(() -> SpringApplication.run(KeranjangApplication.class, new String[]{}))
                                   .thenReturn(mockContext);

            KeranjangApplication.main(new String[]{});

            mockedSpringApplication.verify(() -> SpringApplication.run(KeranjangApplication.class, new String[]{}));
        }
    }
}
