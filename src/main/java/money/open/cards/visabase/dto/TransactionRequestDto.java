package money.open.cards.visabase.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Component
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionRequestDto {

    private String   requestId;
    private String mti;
    private String proxyCardNumber;// proxyCardNo
    private String tpCode;// transaction processing code
    private String transactionAmount;// transaction amount
    private String settlementAmount;
    private String billingAmount;// billing amount
    private String billingFeeAmount;
    private String transactionFeeAmount;// transaction fee
    private String settlementFeeAmount;
    private String transactionProcessingFeeAmount;
    private String settlementProcessingFeeAmount;
    private String settlementConversionRate;
    private String billingConversionRate;
    @JsonFormat(shape = Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime transactionDateTime;// transaction datetime
    private String stan;// stan
    private String localTime;
    @JsonFormat(shape = Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate localDate;
    @JsonFormat(shape = Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;
    @JsonFormat(shape = Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate settlementDate;
    @JsonFormat(shape = Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate conversionDate;
    @JsonFormat(shape = Shape.STRING,pattern = "yyyy-MM-dd")
    private LocalDate captureDate;
    private String mcc;// mcc
    private String acqInstCountryCode;
    private String fwdInstCountryCode;
    private String posEntryMode;// pos entry mode
    private String cardSeqNum;
    private String posConditionCode;// pos condition code
    private String posPinCaptureCode;
    private String authIdResp;
    private String acquirerBin;// acquirerBin
    private String forwardingInstIdCode;
    private String track2Data;
    private String rrn;// rrn
    private String cardAcceptorTerminalid;// terminalId
    private String cardAcceptorId;// merchantId
    private String cardAcceptorLocation;// terminalLocation
    private String transactionCurrencyCode;
    private String settlementCurrencyCode;
    private String billingCurrencyCode;
    private String pinData;
    private String securityRelatedControlInformation;
    private String iccRelatedData;
    private String infData;
    private String financialNetworkCode;
    private String visaposInformation;
    private String settlementInstIdCode;
    private String receivingInstIdCode;
    private String settlementCode;
    private String receivingInstCountryCode;
    private String settlementInstCountryCode;
    private String originalDataElements;
    private String accountIdentification1;
    private String accountIdentification2;
    private String acqNetworkId; // Network Type
    private String acqInstitutionId;// Acquirer Institution Id
    private String channelType;// Channel Type // acquirerChannel
    private String transactionType;// Transaction Type
    private String issuerBin;// IssuerId/IssuerBin
    private String issuerInstId;// Issuer Institution Id
    private String transactionKey;
    private String securityInfo;//<PIN/CVV1/CCV2/EMV/iCVV/CAVV>
    private String securityResult;//<S/F>
    private String accountNumber;
    private String accumFlag;
    private String acquirerId;
    private String issuerId;
    private String networkId;
    private String apiRefNo;
    private String authNumber;
    private String availableBalance;
    private Date businessDate;
    private String chargeAccumFlag;
    private String cin;
    private String drCrAmount;
    private String drCrFlag;
    private String issConvRate;
    private String issCurrencyCode;
    private String kycFlag;
    private String productId;
    private String reasonCode;
    private String responseCode;
    private String revAmount;
    private String revFlag;
    private String transactionStatus;
    private String incTxnCount;
    private String reserverFld1;
    private String reserverFld2;
    private String reserverFld3;
    private String reserverFld4;
    private String reserverFld5;
    private String reserverFld6;
    private String reserverFld7;
    private String reserverFld8;
    private String reserverFld9;
    private String reserverFld10;
    private String cardProduct;
    private String acquirerInstitutionId;
    private String networkType;
    private String replyTopic;
    private String issuerInstitutionId;
    private String maskedPan;
    private String hashedPan;
    private int incrementalTransactionCount;
    private int contactlessTransaction; // 0 -- false , 1-- true
}