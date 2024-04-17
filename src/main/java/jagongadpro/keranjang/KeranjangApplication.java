package jagongadpro.keranjang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class KeranjangApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeranjangApplication.class, args);
	}

}
