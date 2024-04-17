package jagongadpro.keranjang.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;


@Controller
@RequestMapping("/api/cart")
public class ShoppingCartController {

    @GetMapping("/")
    public String createHomePage(){
        return "ShoppingCart";
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addItemToCart() {
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping(value = "/remove")
    public ResponseEntity<String> removeItemFromCart() {
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping(value = "/update")
    public ResponseEntity<String> updateItemInCart() {
        return ResponseEntity.ok("Hello World");
    }
}
