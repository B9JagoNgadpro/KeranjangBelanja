package jagongadpro.keranjang.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameApiPropertiesTest {

    @Test
    void testGameApiProperties() {
        GameApiProperties gameApiProperties = new GameApiProperties();
        
        gameApiProperties.setUrl("http://35.240.130.147/api/games/get-all");
        
        assertEquals("http://35.240.130.147/api/games/get-all", gameApiProperties.getUrl());
    }
}
