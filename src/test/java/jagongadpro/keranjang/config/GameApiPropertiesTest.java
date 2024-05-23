package jagongadpro.keranjang.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class GameApiPropertiesTest {

    @Autowired
    private GameApiProperties gameApiProperties;

    @Test
    public void testGameApiProperties() {
        assertEquals("http://35.240.130.147/api/games/get-all", gameApiProperties.getUrl());
    }
}
