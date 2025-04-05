package tn.cloudnine.queute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class QueuteApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueuteApplication.class, args);
	}

}
