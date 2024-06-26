package jagongadpro.keranjang.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

 class KeranjangResponseTest {

    @Test
     void testKeranjangResponseConstructorAndGetters() {
        Map<String, Integer> items = new HashMap<>();
        items.put("item1", 2);

        KeranjangResponse keranjangResponse = new KeranjangResponse("test@example.com", items, 100);

        assertEquals("test@example.com", keranjangResponse.getEmail());
        assertEquals(items, keranjangResponse.getItems());
        assertEquals(100, keranjangResponse.getTotalPrice());
    }

    @Test
     void testKeranjangResponseSetters() {
        KeranjangResponse keranjangResponse = new KeranjangResponse();
        keranjangResponse.setEmail("test@example.com");

        Map<String, Integer> items = new HashMap<>();
        items.put("item1", 2);
        keranjangResponse.setItems(items);

        keranjangResponse.setTotalPrice(100);

        assertEquals("test@example.com", keranjangResponse.getEmail());
        assertEquals(items, keranjangResponse.getItems());
        assertEquals(100, keranjangResponse.getTotalPrice());
    }
}
