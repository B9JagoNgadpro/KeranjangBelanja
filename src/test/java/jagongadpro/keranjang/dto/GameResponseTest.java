package jagongadpro.keranjang.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameResponseTest {

    @Test
    public void testGameResponseGettersAndSetters() {
        GameResponse gameResponse = new GameResponse();
        gameResponse.setId("1");
        gameResponse.setNama("Game 1");
        gameResponse.setDeskripsi("Deskripsi Game 1");
        gameResponse.setHarga(100);
        gameResponse.setKategori("Action");
        gameResponse.setStok(10);

        assertEquals("1", gameResponse.getId());
        assertEquals("Game 1", gameResponse.getNama());
        assertEquals("Deskripsi Game 1", gameResponse.getDeskripsi());
        assertEquals(100, gameResponse.getHarga());
        assertEquals("Action", gameResponse.getKategori());
        assertEquals(10, gameResponse.getStok());
    }
}
