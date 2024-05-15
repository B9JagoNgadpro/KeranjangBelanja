package jagongadpro.keranjang.controller;

import jagongadpro.keranjang.dto.KeranjangResponse;
import jagongadpro.keranjang.service.ShoppingCartService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to the Shopping Cart API");
    }
    
    @PostMapping("/add")
    public ResponseEntity<?> addItem(@RequestParam String email, @RequestParam String itemId, @RequestParam int quantity) {
        try {
            KeranjangResponse response = shoppingCartService.addItem(email, itemId, quantity);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeItem(@RequestParam String email, @RequestParam String itemId) {
        shoppingCartService.deleteItem(email, itemId);
        return ResponseEntity.ok().build();  
    }

    @PutMapping("/update")
    public ResponseEntity<KeranjangResponse> updateItem(@RequestParam String email, @RequestParam String itemId, @RequestParam int quantity) {
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

    @DeleteMapping("/clear/{email}")
    public ResponseEntity<Void> clearCart(@PathVariable String email) {
        shoppingCartService.clearCart(email);
        return ResponseEntity.ok().build();  
    }
}
