package jagongadpro.keranjang.model;

import jakarta.persistence.*;
import java.util.Map;
import java.util.HashMap;

@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @ElementCollection
    @CollectionTable(name = "cart_items", joinColumns = @JoinColumn(name = "email"))
    @MapKeyColumn(name = "item_id")
    @Column(name = "quantity")
    private Map<String, Integer> items = new HashMap<>();

    @Column(name = "total_price")
    private double totalPrice;

    // Default constructor
    public ShoppingCart() {
        this.items = new HashMap<>();
        this.totalPrice = 0.0;
    }

    public ShoppingCart(String email) {
        this.email = email;
        this.items = new HashMap<>();
        this.totalPrice = 0.0;
    }

    // Getter and Setter methods
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}

