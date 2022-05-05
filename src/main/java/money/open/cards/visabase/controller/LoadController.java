package money.open.cards.visabase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import money.open.cards.visabase.config.VisaBaseConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadController {

    @Autowired
    VisaBaseConfig visaBaseConfig;

    @GetMapping("/home")
    public String home() throws JsonProcessingException {
        //System.out.println(objectMapper.writeValueAsString(loadTransactionService.loadTransactionDetails(new LoadTransaction())));
        System.out.println("Visa base Config--"+visaBaseConfig.getSocketCount());
        return "home page....";
    }

}
