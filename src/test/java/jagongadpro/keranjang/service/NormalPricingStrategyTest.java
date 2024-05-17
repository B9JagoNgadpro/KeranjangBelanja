package jagongadpro.keranjang.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class NormalPricingStrategyTest {

    private NormalPricingStrategy normalPricingStrategy;

    @BeforeEach
    public void setUp() {
        normalPricingStrategy = new NormalPricingStrategy();
    }

    @Test
    public void testCalculateTotal() {
        Map<String, Integer> itemQuantities = new HashMap<>();
        itemQuantities.put("item1", 2);
        itemQuantities.put("item2", 3);

        Map<String, Double> itemPrices = new HashMap<>();
        itemPrices.put("item1", 100.0);
        itemPrices.put("item2", 200.0);

        double total = normalPricingStrategy.calculateTotal(itemQuantities, itemPrices);

        double expectedTotal = (100.0 * 2 + 200.0 * 3);
        assertThat(total).isEqualTo(expectedTotal);
    }

    @Test
    public void testCalculateTotal_noPrice() {
        Map<String, Integer> itemQuantities = new HashMap<>();
        itemQuantities.put("item1", 2);

        Map<String, Double> itemPrices = new HashMap<>();

        double total = normalPricingStrategy.calculateTotal(itemQuantities, itemPrices);

        assertThat(total).isEqualTo(0.0);
    }
}
