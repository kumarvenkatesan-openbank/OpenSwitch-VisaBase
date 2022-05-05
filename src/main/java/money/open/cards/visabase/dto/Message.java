/**
 * 
 */
package money.open.cards.visabase.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class Message {

	private String referenceNumber;
	private String iSOversion;
	private String message;
	private String apiName;
	private String stationName;
	private String messageStatus;
	private String dateTime;
	private String dsnToken;
	private int type;
	private int responseRequired = 1;
	private int intiateKeyRequestBankservflag;
	private String stationCode;


}
