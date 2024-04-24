package jagongadpro.keranjang.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BillingStrategyTest {

    @Mock
    private PricingService mockPricingService;

    private BillingStrategy discountStrategy;
    private BillingStrategy normalStrategy;
    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this); 
        discountStrategy = new DiscountPricingStrategy(20); // 20% discount
        normalStrategy = new NormalPricingStrategy();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close(); 
    }

    @Test
    public void testDiscountPricingStrategy() {
        Map<String, Integer> items = new HashMap<>();
        items.put("item1", 3); // 3 units of item1
        when(mockPricingService.getPrice("item1")).thenReturn(100.0); 

        double total = discountStrategy.calculateTotal(items, mockPricingService);

        assertEquals(240.0, total, 0.01); // Expected: 3 * 100 * (1 - 0.20)
    }

    @Test
    public void testNormalPricingStrategy() {
        Map<String, Integer> items = new HashMap<>();
        items.put("item1", 2); // 2 units of item1
        when(mockPricingService.getPrice("item1")).thenReturn(50.0); 

        double total = normalStrategy.calculateTotal(items, mockPricingService);

        assertEquals(100.0, total, 0.01); // Expect: 2 * 50
    }
}
