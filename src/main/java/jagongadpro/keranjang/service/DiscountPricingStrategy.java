package jagongadpro.keranjang.service;

import java.util.Map;

public class DiscountPricingStrategy implements BillingStrategy {
    private double discountRate;

    public DiscountPricingStrategy(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public double calculateTotal(Map<String, Integer> items, PricingService pricingService) {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            double price = pricingService.getPrice(entry.getKey());
            total += (price * entry.getValue()) * (1 - discountRate / 100);
        }
        return total;
    }
}
