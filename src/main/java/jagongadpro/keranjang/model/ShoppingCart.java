package jagongadpro.keranjang.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.HashMap;

@Entity
@Table(name = "shopping_carts")
@Getter
@Setter
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
}
