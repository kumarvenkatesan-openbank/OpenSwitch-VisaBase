package money.open.cards.visabase;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ServletComponentScan(basePackages = "money.open.cards.visabase.listener")
public class VisaBaseApplication {

	public static void main(String[] args) {

		SpringApplication.run(VisaBaseApplication.class, args);



	}

	@Bean
	public ObjectMapper mapper() {
		return new ObjectMapper();
	}
}
