package jagongadpro.keranjang.service;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class DiscountPricingStrategy implements BillingStrategy {
    private double discountRate;

    public DiscountPricingStrategy() {
        this.discountRate = 10.0;  
    }

    @Override
    public double calculateTotal(Map<String, Integer> itemQuantities, Map<String, Double> itemPrices) {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            double price = itemPrices.getOrDefault(entry.getKey(), 0.0);
            total += (price * entry.getValue()) * (1 - discountRate / 100);
        }
        return total;
    }
}

