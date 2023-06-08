package ucr.rp.if7100.EmailProcessingService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class EmailProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailProcessingServiceApplication.class, args);
	}

}
