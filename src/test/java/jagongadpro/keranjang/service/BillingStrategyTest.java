package jagongadpro.keranjang.service;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BillingStrategyTest {

    @Test
    public void testDiscountPricingStrategy() {
        BillingStrategy discountStrategy = new DiscountPricingStrategy(20); // 20% discount
        PricingService pricingService = id -> {
            return 100.0; // Hardcoded price for any item
        };
        
        Map<String, Integer> items = new HashMap<>();
        items.put("item1", 3); // 3 units of item1
        
        double total = discountStrategy.calculateTotal(items, pricingService);

        assertEquals(240.0, total, 0.01); // Expected: 3 * 100 * (1 - 0.20)
    }

    @Test
    public void testNormalPricingStrategy() {
        BillingStrategy normalStrategy = new NormalPricingStrategy();
        PricingService pricingService = id -> {
            return 50.0; // Hardcoded price for any item
        };

        Map<String, Integer> items = new HashMap<>();
        items.put("item1", 2); // 2 units of item1
        
        double total = normalStrategy.calculateTotal(items, pricingService);

        assertEquals(100.0, total, 0.01); // Expected: 2 * 50
    }
}
