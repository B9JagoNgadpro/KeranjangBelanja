package jagongadpro.keranjang.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @RabbitListener(queues = "${request.queue}")
    public String handleCartRequest(String userId) {
        // Proses permintaan di sini dan kembalikan responsenya
        return "Detail keranjang untuk user " + userId;
    }
}
