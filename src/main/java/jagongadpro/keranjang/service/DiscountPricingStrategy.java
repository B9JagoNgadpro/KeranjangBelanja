package jagongadpro.keranjang.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Qualifier("discountPricingStrategy")
public class DiscountPricingStrategy implements BillingStrategy {
    private double discountRate = 10.0;

    @Override
    public int calculateTotal(Map<String, Integer> itemQuantities, Map<String, Double> itemPrices) {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            double price = itemPrices.getOrDefault(entry.getKey(), 0.0);
            total += (price * entry.getValue()) * (1 - discountRate / 100);
        }
        return (int) Math.round(total);
    }
}
