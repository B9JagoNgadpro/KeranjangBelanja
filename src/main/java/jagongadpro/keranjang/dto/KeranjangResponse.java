package jagongadpro.keranjang.dto;

import java.util.Map;

public class KeranjangResponse {
    private String email;
    private Map<Integer, Integer> items;
    private double totalPrice;

    public KeranjangResponse(String email, Map<Integer, Integer> items, double totalPrice) {
        this.email = email;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    // Getter dan Setter
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<Integer, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Integer> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
