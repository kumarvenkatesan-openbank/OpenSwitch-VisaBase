package money.open.cards.visabase.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SecurityRelatedControlInformation {

	private String securityFormatCode;
	private String algorithmId;
	private String pinBlockFormat;
	private String zoneKeyIndex;
	private String reservedForFutureUse1;
	private String reservedForFutureUse2;



}
