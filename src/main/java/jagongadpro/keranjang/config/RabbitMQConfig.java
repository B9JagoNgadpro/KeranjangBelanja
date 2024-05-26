package jagongadpro.keranjang.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class RabbitMQConfig {

    @Value("${request.queue}")
    private String requestQueue;

    @Value("${response.queue}")
    private String responseQueue;

    @Value("${exchange}")
    private String exchange;

    @Bean
    public Queue requestQueue() {
        return new Queue(requestQueue, false);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(responseQueue, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding requestBinding(Queue requestQueue, DirectExchange exchange) {
        return BindingBuilder.bind(requestQueue).to(exchange).with(requestQueue.getName());
    }

    @Bean
    public Binding responseBinding(Queue responseQueue, DirectExchange exchange) {
        return BindingBuilder.bind(responseQueue).to(exchange).with(responseQueue.getName());
    }
}
