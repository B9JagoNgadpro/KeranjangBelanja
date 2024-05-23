package jagongadpro.keranjang.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Qualifier("normalPricingStrategy")
public class NormalPricingStrategy implements BillingStrategy {
    @Override
    public double calculateTotal(Map<String, Integer> itemQuantities, Map<String, Double> itemPrices) {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : itemQuantities.entrySet()) {
            double price = itemPrices.getOrDefault(entry.getKey(), 0.0);
            total += price * entry.getValue();
        }
        return total;
    }
}
