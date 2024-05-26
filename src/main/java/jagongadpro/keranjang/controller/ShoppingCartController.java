package jagongadpro.keranjang.controller;

import jagongadpro.keranjang.dto.KeranjangResponse;
import jagongadpro.keranjang.service.ShoppingCartService;
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
    public ResponseEntity<KeranjangResponse> addItem(@RequestParam String email, @RequestParam String itemId, @RequestParam int quantity) {
        try {
            KeranjangResponse response = shoppingCartService.addItem(email, itemId, quantity);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/increment")
    public ResponseEntity<KeranjangResponse> incrementItem(@RequestParam String email, @RequestParam String itemId) {
        KeranjangResponse response = shoppingCartService.incrementItem(email, itemId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/decrement")
    public ResponseEntity<KeranjangResponse> decrementItem(@RequestParam String email, @RequestParam String itemId) {
        KeranjangResponse response = shoppingCartService.decrementItem(email, itemId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<KeranjangResponse> removeItem(@RequestParam String email, @RequestParam String itemId) {
        KeranjangResponse response = shoppingCartService.deleteItem(email, itemId);
        return ResponseEntity.ok(response);
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
    public ResponseEntity<KeranjangResponse> clearCart(@PathVariable String email) {
        KeranjangResponse response = shoppingCartService.clearCart(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create/{email}")
    public ResponseEntity<KeranjangResponse> createCart(@PathVariable String email) {
        try {
            KeranjangResponse response = shoppingCartService.createCart(email);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
