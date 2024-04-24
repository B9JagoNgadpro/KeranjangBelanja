package jagongadpro.keranjang.controller;

import org.springframework.http.ResponseEntity;
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

    @GetMapping("/add")
    public ResponseEntity<String> getAddItemForm() {
        return ResponseEntity.ok("This is a example POST to add an item.");
    }
    
    @GetMapping("/remove")
    public ResponseEntity<String> getRemoveItemForm() {
        return ResponseEntity.ok("This is a example POST to remove an item.");
    }
    
    @GetMapping("/update")
    public ResponseEntity<String> getUpdateItemForm() {
        return ResponseEntity.ok("This is a example POST to update an item.");
    }
    
}
