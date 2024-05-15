package jagongadpro.keranjang.service;

import java.util.Map;

public interface BillingStrategy {
    double calculateTotal(Map<String, Integer> itemQuantities, Map<String, Double> itemPrices);
}
