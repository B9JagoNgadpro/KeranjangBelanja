package jagongadpro.keranjang.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartSubscriber {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.REQUEST_QUEUE)
    public void receiveMessage(String email) {
        System.out.println("Received request to view cart for email: " + email);
        
        // Simulate fetching cart details
        String cartDetails = "Cart details for " ;

        // Send response back
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.RESPONSE_ROUTING_KEY, cartDetails);
    }
}

