package jagongadpro.keranjang.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpPricingServiceTest {

    @Test
    public void testGetPrice() {
        HttpPricingService pricingService = new HttpPricingService("http://fakeurl.com/api/prices") {
            @Override
            public double getPrice(String id) {
                return 120.0; 
            }
        };

        double price = pricingService.getPrice("item1");
        assertEquals(120.0, price, 0.01);
    }
}
