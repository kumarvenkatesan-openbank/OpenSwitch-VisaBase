package money.open.cards.visabase.service;

import java.util.Hashtable;

public interface LoadTransactionService {

    void loadTransactionDetails(Hashtable<String, String> isoBuffer);
    String getDeliveryChannel(String tpCode,String mcc);

}
