package jagongadpro.keranjang.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Map;
import java.util.HashMap;

@Getter @Setter
@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    @Column(name = "email", nullable = false)
    private String email; // Email pengguna sebagai primary key

    @ElementCollection
    @CollectionTable(name = "cart_items") // Tabel untuk menyimpan item dalam keranjang
    @MapKeyColumn(name = "item_id") // Kolom untuk item ID
    @Column(name = "quantity") // Kolom untuk quantity
    private Map<Integer, Integer> items = new HashMap<>();

    @Column(name = "total_price")
    private double totalPrice; // Total harga dari semua item dalam cart

    public ShoppingCart(String email) {
        this.email = email;
        this.items = new HashMap<>();
        this.totalPrice = 0.0;
    }
}
