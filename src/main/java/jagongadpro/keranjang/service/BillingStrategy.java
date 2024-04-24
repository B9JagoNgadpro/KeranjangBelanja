package jagongadpro.keranjang.service;

import java.util.Map;

public interface BillingStrategy {
    double calculateTotal(Map<String, Integer> items, PricingService pricingService);
}
