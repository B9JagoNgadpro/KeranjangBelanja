package jagongadpro.keranjang.controller;

import jagongadpro.keranjang.dto.KeranjangResponse;
import jagongadpro.keranjang.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/add")
    public ResponseEntity<KeranjangResponse> addItem(@RequestParam String email, @RequestParam int itemId, @RequestParam int quantity) {
        KeranjangResponse response = shoppingCartService.addItem(email, itemId, quantity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeItem(@RequestParam String email, @RequestParam int itemId) {
        shoppingCartService.deleteItem(email, itemId);
        return ResponseEntity.ok().build();  // No content to return
    }

    @PutMapping("/update")
    public ResponseEntity<KeranjangResponse> updateItem(@RequestParam String email, @RequestParam int itemId, @RequestParam int quantity) {
        KeranjangResponse response = shoppingCartService.updateItem(email, itemId, quantity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view/{email}")
    public ResponseEntity<KeranjangResponse> viewCart(@PathVariable String email) {
        KeranjangResponse response = shoppingCartService.findCartByEmail(email);  
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
