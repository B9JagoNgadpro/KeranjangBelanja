package jagongadpro.keranjang.service;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BillingStrategyTest {

    @Test
    public void testDiscountPricingStrategy() {
        BillingStrategy discountStrategy = new DiscountPricingStrategy(20); 
        PricingService pricingService = id -> {
            return 100.0; 
        };
        
        Map<String, Integer> items = new HashMap<>();
        items.put("item1", 3); 
        
        double total = discountStrategy.calculateTotal(items, pricingService);

        assertEquals(240.0, total, 0.01); 
    }

    @Test
    public void testNormalPricingStrategy() {
        BillingStrategy normalStrategy = new NormalPricingStrategy();
        PricingService pricingService = id -> {
            return 50.0; 
        };

        Map<String, Integer> items = new HashMap<>();
        items.put("item1", 2); 
        
        double total = normalStrategy.calculateTotal(items, pricingService);

        assertEquals(100.0, total, 0.01); 
    }
}
