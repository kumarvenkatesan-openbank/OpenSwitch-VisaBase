package money.open.cards.visabase.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdditionalResponseData {


    //static int[] Field44Offset = { 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 4, 4, 1 };
    /***
     * 44.1 Response Source/Reason Code
     * 44.2 Not applicable
     * 44.3 Not applicable
     * 44.4 Reserved
     * 44.5 CVV/iCVV Results Code (responses and requests; both Visa and Plus)
     * 44.6 Not applicable
     * 44.7 Not applicable
     * 44.8 Card Authentication Results Code
     * 44.9 Not applicable
     * 44.10 Not applicable
     * 44.11 Original Response Code
     * 44.12 Not applicable
     * 44.13 Not applicable
     */
    private String reasonCode;
    private String reserved;
    private String cvvIcvvResultCode;
    private String cardAuthResultCode;
    private String orgResponseCode;

}
