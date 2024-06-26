package jagongadpro.keranjang.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

 class DiscountPricingStrategyTest {

    private DiscountPricingStrategy discountPricingStrategy;

    @BeforeEach
     void setUp() {
        discountPricingStrategy = new DiscountPricingStrategy();
    }

    @Test
     void testCalculateTotal_withDiscount() {
        Map<String, Integer> itemQuantities = new HashMap<>();
        itemQuantities.put("item1", 2);
        itemQuantities.put("item2", 3);

        Map<String, Double> itemPrices = new HashMap<>();
        itemPrices.put("item1", 100.0);
        itemPrices.put("item2", 200.0);

        double total = discountPricingStrategy.calculateTotal(itemQuantities, itemPrices);

        // Menghitung total dengan diskon 10%
        double expectedTotal = (100.0 * 2 + 200.0 * 3) * 0.9;
        assertThat(total).isEqualTo(expectedTotal);
    }

    @Test
     void testCalculateTotal_noPrice() {
        Map<String, Integer> itemQuantities = new HashMap<>();
        itemQuantities.put("item1", 2);

        Map<String, Double> itemPrices = new HashMap<>();

        double total = discountPricingStrategy.calculateTotal(itemQuantities, itemPrices);

        assertThat(total).isEqualTo(0.0);
    }
}
