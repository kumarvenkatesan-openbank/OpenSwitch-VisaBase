package money.open.cards.visabase.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdditionalAmounts {

    private String accountType;
    private String amountType;
    private String currencyCode;
    private String amountSign;
    private String amount;
}
