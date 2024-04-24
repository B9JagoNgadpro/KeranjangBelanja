// package jagongadpro.keranjang.service;

// import jagongadpro.keranjang.model.ShoppingCartItem;
// import jagongadpro.keranjang.repository.ShoppingCartRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.ArrayList;
// import java.util.Iterator;
// import java.util.List;

// @Service
// public class ShoppingCartService {

//     @Autowired
//     private ShoppingCartRepository shoppingCartRepository;

//     public ShoppingCartItem addItem(ShoppingCartItem item) {
//         return shoppingCartRepository.addItem(item);
//     }

//     public List<ShoppingCartItem> findAllItems() {
//         Iterator<ShoppingCartItem> itemIterator = shoppingCartRepository.findAllItems();
//         List<ShoppingCartItem> allItems = new ArrayList<>();
//         itemIterator.forEachRemaining(allItems::add);
//         return allItems;
//     }

//     public ShoppingCartItem updateItem(String itemId, ShoppingCartItem item) {
//         return shoppingCartRepository.updateItem(itemId, item);
//     }

//     public void deleteItem(String itemId) {
//         shoppingCartRepository.removeItem(itemId);
//     }

//     public ShoppingCartItem findItemById(String itemId) {
//         return shoppingCartRepository.findItemById(itemId);
//     }
// }
