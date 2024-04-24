package jagongadpro.keranjang.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ShoppingCart {
    private int itemId; // ID dari item sebagai integer
    private int quantity; // Jumlah dari item tersebut dalam keranjang

    public ShoppingCart(int itemId, int quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }
}
