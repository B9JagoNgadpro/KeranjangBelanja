package jagongadpro.keranjang.repository;

import jagongadpro.keranjang.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, String> {
    ShoppingCart findByEmail(String email);
}
