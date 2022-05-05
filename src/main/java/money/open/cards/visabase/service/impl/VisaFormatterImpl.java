package money.open.cards.visabase.service.impl;

import lombok.extern.slf4j.Slf4j;
import money.open.cards.visabase.service.VISAISOFormatter;
import money.open.cards.visabase.Utility.Convertor;
import money.open.cards.visabase.Utility.M24Utility;
import money.open.cards.visabase.constants.TPEResponseCode;
import money.open.cards.visabase.dto.Message;
import money.open.cards.visabase.exception.TransactionException;
import org.springframework.stereotype.Service;

import java.util.Hashtable;
import java.util.Vector;

@Slf4j
@Service
public class VisaFormatterImpl implements VISAISOFormatter {

	M24Utility m24Utility = null;

	String[] MESSAGESTATUS = { "REQ", "RSP", "REQ_LOG_ON", "RSP_LOG_OFF", "CDOWN_RSP", "CDOWN_RSP_LOG",
			"CDOWN_RSP_LOG_OFF", "CDOWN_RSP_LOG_OFF_LOG", "CDOWN_RVSL_REQ", "HDOWN_REQ", "HDOWN_RSP", "HCDOWN_REQ_LOG",
			"HCDOWN_RSP", "HCDOWN_RSP_LOG", "CHDOWN_RVSL_REQ", "SUSPECT_RVSL_LOG", "TIME_OUT_RSP", "TIME_OUT_RSP_LOG",
			"COMPLETE_RVSL_LOG", "INVALID_MSG_LOG", "INVALID_STATUS_LOG" };

	// String[] SUPPORTED_MSGTYPE = {"1100", "1110", "1120", "1130","1200",
	// "1210", "1220", "1230", "1400", "1410", "1420", "1430", "1804", "1814",
	// "1644"};

	// 1- BCD
	// 2- BCD1
	// 3- EBDIC
	// 4- bytes
	// Bit String = (length/8)*2 == Like EBCDIC

	final static int[][] BITMAP87 = { { 8, 3 }, { -2, 2 }, { 6, 1 }, { 12, 1 }, { 12, 1 }, { 12, 1 }, { 10, 1 },
			{ 8, 1 }, // 8
			{ 8, 1 }, { 8, 1 }, { 6, 1 }, { 6, 1 }, { 4, 1 }, { 4, 1 }, { 4, 1 }, { 4, 1 }, // 16
			{ 4, 1 }, { 4, 1 }, { 3, 2 }, { 3, 2 }, { 0, 0 }, { 4, 1 }, { 4, 1 }, { 3, 2 }, // 24
			{ 2, 1 }, { 2, 1 }, { 1, 2 }, { 9, 3 }, { 9, 3 }, { 9, 3 }, { 9, 3 }, { -2, 2 }, // 32
			{ -2, 2 }, { -2, 2 }, { -2, 2 }, { -2, 1 }, { 12, 3 }, { 6, 3 }, { 2, 3 }, { 0, 0 }, // 40
			{ 8, 3 }, { 15, 3 }, { 40, 3 }, { -2, 3 }, { -2, 3 }, { -2, 3 }, { -2, 3 }, { -2, 3 }, // 48
			{ 3, 2 }, { 3, 2 }, { 3, 2 }, { 8, 3 }, { 16, 1 }, { -2, 3 }, { -2, 3 }, { -2, 3 }, // 56
			{ -2, 3 }, { -2, 3 }, { -2, 3 }, { -2, 3 }, { -2, 3 }, { -2, 3 }, { -2, 3 }, { 0, 0 }, // 64
			{ 0, 0 }, { 1, 2 }, { 2, 1 }, { 3, 2 }, { 3, 2 }, { 3, 2 }, { 4, 1 }, { 4, 1 }, // 72
			{ 6, 1 }, { 10, 1 }, { 10, 1 }, { 10, 1 }, { 10, 1 }, { 10, 1 }, { 10, 1 }, { 10, 1 }, // 80
			{ 10, 1 }, { 12, 1 }, { 12, 1 }, { 12, 1 }, { 12, 1 }, { 16, 1 }, { 16, 1 }, { 16, 1 }, // 88
			{ 16, 1 }, { 42, 1 }, { 1, 3 }, { 2, 3 }, { 0, 0 }, { 7, 3 }, { 42, 3 }, { 8, 3 }, // 96
			{ 17, 3 }, { 0, 0 }, { -2, 2 }, { -2, 2 }, { -2, 3 }, { -2, 3 }, { -2, 3 }, { -2, 3 }, // 104
			{ 16, 3 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, // 112
			{ 0, 0 }, { 0, 0 }, { -2, 3 }, { -2, 3 }, { -2, 3 }, { -2, 3 }, { -2, 3 }, { 0, 0 }, // 120
			{ -2, 3 }, { 0, 0 }, { -2, 3 }, { 0, 0 }, { -2, 3 }, { -2, 3 }, { -2, 4 }, { 0, 0 } // 128
	};

	// final static int[] FIN_RSP_BITMAP = { 2, 3, 4, 7, 11, 15, 19, 25, 32, 37,
	// 38, 39, 41, 42, 49, 62, 117, 118 };
	// 62.0,62.23,63.0,63.1,63.12,63.13,63.19

	final static int[] FIN_RSP_BITMAP = { 2, 3, 4, 7, 11, 15, 19, 23, 25, 32, 37, 38, 39, 41, 42, 44, 48, 49, 54, 55,
			62, 102, 117, 118 };
	// 62.0,62.23,63.0,63.1,63.12,63.13,63.19

	final static int[] FIN_OPTRSP_BITMAP = { 44, 48, 54, 62, 102 }; // 44.5,48,54,62.1,62.2,62.24,62.25,102,

	final static int[] REMOVE_LIST = { 5, 6, 8, 9, 10, 12, 13, 14, 16, 17, 18, 20, 21, 22, /* 23, */ 24, 26, 27, 28, 29,
			30, 31, 33, 34, 35, 36, 40, 43, 45, 46, 47, 50, 51, 52, 53, 56, 57, 58, 59, 60, 61, /* 63, */ 64, 65, 66,
			67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, /*90,*/ 91, 92, 93,
			94,/* 95,*/ 96, 97, 98, 99, 100, 101, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 119,
			120, 121, 122, 123, 124, 125, 126, 127, 128 };

	public VisaFormatterImpl() {

		m24Utility = new M24Utility();
	}

	public void headerFormat(String headerMessage, Hashtable<String, String> isoBuffer) {
		isoBuffer.put("H01", headerMessage.substring(0, 2)); // Field-1
		isoBuffer.put("H02", headerMessage.substring(2, 4)); // Field-2
		isoBuffer.put("H03", headerMessage.substring(4, 6)); // Field-3
		isoBuffer.put("H04", headerMessage.substring(6, 10)); // Field-4
		isoBuffer.put("H05", headerMessage.substring(10, 16)); // Field-5
		isoBuffer.put("H06", headerMessage.substring(16, 22)); // Field-6
		isoBuffer.put("H07", headerMessage.substring(22, 24)); // Field-7
		isoBuffer.put("H08", headerMessage.substring(24, 28)); // Field-8
		isoBuffer.put("H09", headerMessage.substring(28, 34)); // Field-9
		isoBuffer.put("H10", headerMessage.substring(34, 36)); // Field-10
		isoBuffer.put("H11", headerMessage.substring(36, 42)); // Field-11
		isoBuffer.put("H12", headerMessage.substring(42, 44)); // Field-12
		/*
		 * isoBuffer.put( "MSG-BITMAP", headerMessage.substring(44, 48)); isoBuffer.put(
		 * "MSG-HDR", headerMessage.substring(48, 52));
		 */
	}

	public String buildHeader(Hashtable<String, String> isoBuffer) {
		return "0000" + // Field-1
				isoBuffer.get("H01") + // Field-1
				isoBuffer.get("H02") + // Field-2
				isoBuffer.get("H03") + // Field-3
				isoBuffer.get("H04") + // Field-4
				isoBuffer.get("H05") + // Field-5//SRC
				isoBuffer.get("H06") + // Field-6
				isoBuffer.get("H07") + // Field-7
				isoBuffer.get("H08") + // Field-8
				isoBuffer.get("H09") + // Field-9
				isoBuffer.get("H10") + // Field-10
				isoBuffer.get("H11") + // Field-11
				isoBuffer.get("H12");
	}

	public Hashtable<String, String> formatMessage(Message msgBuffer, Hashtable<String, String> isoBuffer)
			throws Exception {

		int size = 0;

		String pBitmap = null;
		String sBitmap = null;
		String rawMessage=msgBuffer.getMessage();

		int headerLen = Convertor.hex2decimal(rawMessage.substring(4, 6)) * 2;
		headerLen += 4;
		headerFormat(rawMessage.substring(4, headerLen), isoBuffer);
		isoBuffer.put("MSG-HDR", rawMessage.substring(0, headerLen));
		isoBuffer.put("MTI", rawMessage.substring(headerLen, headerLen + 4));
		int offset = headerLen + 4;

		try {

			// String msgType = isoBuffer.get("MSG-TYP");

			pBitmap = parseBitmap(rawMessage.substring(offset, offset + 16));

			offset += 16;

			for (int i = 0; i < 64; i++) {
				if ('1' == pBitmap.charAt(i)) {
					int bitLen = BITMAP87[i][0];
					int bitType = BITMAP87[i][1];

					if (bitLen < 0) {
						bitLen *= -1;
						size = Convertor.hexatoDecimal(rawMessage.substring(offset, offset + bitLen));
						if (bitType == 2 && size % 2 != 0) {
							size += 1;
						} else if (bitType == 3) {
							size *= 2;
						}

						offset += bitLen;

						isoBuffer.put("DE" + String.format("%03d", (i + 1)), rawMessage.substring(offset, offset + size));
						offset += size;
					} else {
						if (bitType == 2) {
							isoBuffer.put("DE" + String.format("%03d",(i + 1)), rawMessage.substring(offset, offset + (bitLen + 1)));
							offset += (bitLen + 1);
						} else if (bitType == 3) {
							isoBuffer.put("DE" + String.format("%03d", (i + 1)), rawMessage.substring(offset, offset + (bitLen * 2)));
							offset += bitLen * 2;
						} else {
							isoBuffer.put("DE" + String.format("%03d",(i + 1)), rawMessage.substring(offset, offset + bitLen));
							offset += bitLen;
						}

					}
				} else {
					isoBuffer.put("DE" + String.format("%03d",(i + 1)), "*");
				}
			}

			if ('1' == pBitmap.charAt(0)) {

				sBitmap = parseBitmap(isoBuffer.get("DE001").toString());
				for (int i = 64; i < 128; i++) {

					if ('1' == sBitmap.charAt(i - 64)) {
						int bitLen = BITMAP87[i][0];
						int bitType = BITMAP87[i][1];

						if (bitLen < 0) {
							bitLen *= -1;
							size = Convertor.hexatoDecimal(rawMessage.substring(offset, offset + bitLen));
							if (bitType == 2 && size % 2 != 0) {
								size += 1;
							} else if (bitType == 3) {
								size *= 2;
							}

							offset += bitLen;

							isoBuffer.put("DE" + String.format("%03d",(i + 1)), rawMessage.substring(offset, offset + size));
							offset += size;
						} else {
							if (bitType == 2) {
								isoBuffer.put("DE" + String.format("%03d",(i + 1)), rawMessage.substring(offset, offset + (bitLen + 1)));
								offset += (bitLen + 1);
							} else if (bitType == 3) {
								isoBuffer.put("DE" + String.format("%03d",(i + 1)), rawMessage.substring(offset, offset + (bitLen * 2)));
								offset += bitLen * 2;
							} else {
								isoBuffer.put("DE" + String.format("%03d",(i + 1)), rawMessage.substring(offset, offset + bitLen));
								offset += bitLen;
							}

						}
					} else {

						isoBuffer.put("DE" + String.format("%03d",(i + 1)), "*");
					}
				}

			} else {

				for (int i = 64; i < 128; i++) {

					isoBuffer.put("DE" + String.format("%03d",(i + 1)), "*");
				}
			}

			isoBuffer.put("pBMP", binary2hex(pBitmap).toUpperCase());
			if(sBitmap==null)
				isoBuffer.put("sBMP", binary2hex("0000000000000000000000000000000000000000000000000000000000000000"));
			else
				isoBuffer.put("sBMP", binary2hex(sBitmap).toUpperCase());




		} catch (Exception e) {
			e.printStackTrace();
			throw new TransactionException(TPEResponseCode.FORMAT_ERROR.getResponseCode());
		}
		return isoBuffer;
	}

	public String buildMessage(Hashtable<String, String> isoBuffer) {

		return buildISOMessage(isoBuffer, false);
	}

	public String buildFinacialMessage(final Hashtable<String, String> isoBuffer) throws TransactionException {
		try {
			for (int i = 0; i < REMOVE_LIST.length; i++) {
				int bit = REMOVE_LIST[i];
				// System.out.println("Remove "+bit);
				if (i < 64) {
					isoBuffer.put("DE" + String.format("%03d",bit), "*");
				} else {
					isoBuffer.put("DE" + String.format("%03d",bit), "*");
				}
			}
		} catch (Exception e) {
			throw new TransactionException(TPEResponseCode.ERROR.getResponseCode());
		}

		return buildISOMessage(isoBuffer, false);
	}

	public String buildKeyExchangeMessage(Hashtable<String, String> isoBuffer, String messageType) {
		return buildISOMessage(isoBuffer, false);
	}

	public String buildMessage(Hashtable<String, String> isoBuffer, String msgType) {
		return buildISOMessage(isoBuffer, false);
	}

	public String getResponseMessageType(String messageType) {
		if (messageType != null && messageType.length() > 3) {
			messageType = messageType.substring(0, 2) + (Integer.parseInt(messageType.substring(2, 3)) + 1)
					+ messageType.charAt(messageType.length() - 1);
		}
		return messageType;
	}

	public String buildISOErrorMessage(final Hashtable<String, String> isoBuffer) {
		isoBuffer.put("MSG-TYP", getResponseMessageType(isoBuffer.get("MTI")));
		isoBuffer.put("MSG-HDR", buildHeader(isoBuffer));
		String losgMessage = isoBuffer.get("MSG-TYP")
				+ binary2hex("0000000000000000000000000000000000000010000000000000000000000000").toUpperCase()
				+ isoBuffer.get("DE039");
		StringBuilder msgHeader = new StringBuilder();
		msgHeader.append(isoBuffer.get("MSG-HDR"));
		String lenData = Convertor
				.paddingZeroPrefix(Convertor.convertToHex((msgHeader.length() - 4 + losgMessage.length()) / 2), 4);
		msgHeader.replace(10, 14, lenData);

		String responseMessage = msgHeader.toString() + losgMessage;

		responseMessage = Convertor.hex2Alpha(lenData + responseMessage);
		return responseMessage;
	}

	public String buildISOMessage(final Hashtable<String, String> isoBuffer, boolean request) {
		if (!request) {
			//isoBuffer.put("MTI", getResponseMessageType(isoBuffer.get("MTI")));
		}
		int lonuSize = 0;
		String losgMessage = "";
		String losgPrimaryBitMap = "";
		String losgSecondaryBitMap = "";
		int i = 0;
		isoBuffer.put("DE001", "*");
		for (i = 0; i < 128; i++) {
			if (i <= 63) {
				if (!isoBuffer.get("DE" + String.format("%03d",(i + 1))).equals("*")) {
					int bitLen = BITMAP87[i][0];
					int bitType = BITMAP87[i][1];
					// 1- BCD
					// 2- BCD1
					// 3- EBDIC
					// 4- bytes

					if (bitLen < 0) {
						lonuSize = isoBuffer.get("DE" + String.format("%03d",(i + 1))).toString().length();
						if (bitType == 2 && lonuSize % 2 != 0) {
							lonuSize -= 1;
						} else if (bitType == 3) {
							lonuSize /= 2;
						}
						losgMessage += Convertor.convertToHex(lonuSize);
						losgMessage += isoBuffer.get("DE" + String.format("%03d",(i + 1))).toString();
					} else {
						losgMessage += isoBuffer.get("DE" + String.format("%03d",(i + 1))).toString();
					}
					losgPrimaryBitMap += "1";
				} else {
					losgPrimaryBitMap += "0";
				}
			} else {
				if (!isoBuffer.get("DE" + String.format("%03d",(i + 1))).equals("*")) {
					int bitLen = BITMAP87[i][0];
					int bitType = BITMAP87[i][1];

					if (bitLen < 0) {
						lonuSize = isoBuffer.get("DE" + String.format("%03d",(i + 1))).toString().length();
						if (bitType == 2 && lonuSize % 2 != 0) {
							lonuSize -= 1;
						} else if (bitType == 3) {
							lonuSize /= 2;
						}

						losgMessage += Convertor.convertToHex(lonuSize);
						losgMessage += isoBuffer.get("DE" + String.format("%03d",(i + 1))).toString();
					} else {
						losgMessage += isoBuffer.get("DE" + String.format("%03d",(i + 1))).toString();
					}
					losgSecondaryBitMap += "1";
				} else {
					losgSecondaryBitMap += "0";
				}
			}
		}





		if (losgSecondaryBitMap.equals("0000000000000000000000000000000000000000000000000000000000000000")) {
			losgMessage = isoBuffer.get("MTI") + binary2hex(losgPrimaryBitMap).toUpperCase() + losgMessage;
		} else {
			losgMessage = isoBuffer.get("MTI") + binary2hex("1" + losgPrimaryBitMap.substring(1)).toUpperCase()
					+ binary2hex(losgSecondaryBitMap).toUpperCase() + losgMessage;
		}

		StringBuilder msgHeader = new StringBuilder();
		msgHeader.append(isoBuffer.get("MSG-HDR"));
		String lenData = Convertor
				.paddingZeroPrefix(Convertor.convertToHex((msgHeader.length() - 4 + losgMessage.length()) / 2), 4);
		msgHeader.replace(10, 14, lenData);

		String msgHeaderString = msgHeader.toString();//
		msgHeaderString = msgHeaderString.substring(0, 14) + isoBuffer.get("H06") + isoBuffer.get("H05")
				+ msgHeaderString.substring(26);
		String responseMessage = msgHeaderString + losgMessage;

		responseMessage = Convertor.hex2Alpha(lenData + responseMessage);
		return responseMessage;
	}

	private String parseBitmap(final String iobsgBitmap) {

		String losgUpperBitmap = "00000000000000000000000000000000";
		String losgLowerBitmap = "00000000000000000000000000000000";
		losgUpperBitmap += Long.toBinaryString(Long.parseLong(iobsgBitmap.substring(0, 8), 16));
		losgLowerBitmap += Long.toBinaryString(Long.parseLong(iobsgBitmap.substring(8), 16));
		losgUpperBitmap = losgUpperBitmap.substring(losgUpperBitmap.length() - 32);
		losgLowerBitmap = losgLowerBitmap.substring(losgLowerBitmap.length() - 32);

		return losgUpperBitmap + losgLowerBitmap;
	}

	public String binary2hex(final String iobsgBinaryString) {

		if (iobsgBinaryString == null) {
			return null;
		}
		String losgHexString = "";
		for (int i = 0; i < iobsgBinaryString.length(); i += 8) {
			String losgTemp = iobsgBinaryString.substring(i, i + 8);
			int lonuIntValue = 0;
			for (int k = 0, j = losgTemp.length() - 1; j >= 0; j--, k++) {
				lonuIntValue += Integer.parseInt("" + losgTemp.charAt(j)) * Math.pow(2, k);
			}
			losgTemp = "0" + Integer.toHexString(lonuIntValue);
			losgHexString += losgTemp.substring(losgTemp.length() - 2);
		}
		return losgHexString;
	}

	public Vector<Object> buildRow(Message response, String stationName, String messageStatus, String dateTime,
			String dsnToken, Hashtable<String, String> isoBuffer, String referenceNumber, String apiName) {
		// TODO Auto-generated method stub

		Vector<Object> row = new Vector<Object>();
		row.addElement(response);
		row.addElement(stationName);
		row.addElement(messageStatus);
		row.addElement(dateTime);
		row.addElement(dsnToken);
		row.addElement(isoBuffer);
		row.addElement(referenceNumber);
		row.addElement(apiName);

		return row;
	}

	public static void main(String[] args) throws Exception {
		//String tmp = "000016010200FD00000095362600000000000000000000000100723C648128E880161040345678901234560000000000000021000404151950000505204950040403095999084090200006476134204034567890123456D030912312345000F9F0F9F4F1F5F0F0F0F5F0F5E3C5D9D4C9C4F0F1C3C1D9C440C1C3C3C5D7E3D6D94040C1C3D8E4C9D9C5D940D5C1D4C5404040404040404040404040C3C9E3E840D5C1D4C540404040E4E2416CC2F4F0F4F3F2F2F1F1F0F8F7F5F0F0F2F05FE3C5E2E340C3C1D9C440D6D5C5404040404040404040404040615FF2F4F0F6F1F2F6F0F0F5F4F5F0F0F0F0F0F06F08400142080000000000000000058000000002";
		VisaFormatterImpl visaisoFormatter = new VisaFormatterImpl();
		System.out.println(visaisoFormatter.binary2hex("0111111011111101011001101001000100101000111000001111101000010110"));

		String tmp="";

		//tmp = "000016007100C100000095362600000000000000000000000110722020810AC08004104043221109000425000000000000040000040416162000051007020006476135F9F0F9F4F1F6F0F0F0F5F1F0F1F4E3C5D9D4C9C4F0F1C3C1D9C440C1C3C3C5D7E3D6D940400356080000000000000000";
		//tmp = "000016010200D400000095362600000000000000000000000220E22020810ED084060000000004000000104071780000000011301000091312034201689407020006476135F9F2F5F6F1F2F0F1F6F8F9F4F0F0F0F0C5C1F0F0C1E3D4F0F1404040C3C1D9C440C1C3C3C5D7E3D6D9404017404040404040404040404040404040404040404040404007022AF1F0F0F195A49393C3F0F0F0F0F0F0F0F0F0F0F0F0F1F0F0F295A49393C3F0F0F0F0F0F0F0F0F0F0F0F0098000000000000000E8068020000002F00FF2F5F6F4F1F0F1F0F0F0F8F4F7F9F9";
		//tmp = "0000160102004400000000000000000000000000000000000800822000000800000204000000000000000421140506000093F2F1F1F1F1F4F0F0F0F0F9F30580000000020081";
		//tmp = "00001601020160000000000000000000000000000000000002107EFD669128E0FA1610403456789012345601100000000000400000000002500000000002500005021312266100000061000000000388184226050203090000601108400510000100C4F0F0F0F0F0F0F0F00C012345678901204034567890123456D030912312345000F2F1F2F2F1F3F0F0F0F3F8F8C1E3D4F0F1404040C3C1D9C440C1C3C3C5D7E3D6D94040C1C3D8E4C9D9C5D940D5C1D4C5404040404040404040404040C3C9E3E840D5C1D4C540404040E4E208400840084095A4939395A49393F4E84B4CC4C3F36ED45D2001010100000000670100649F3303204000950500000400009F37049BADBCAB9F100C0B010A03A0B00000000000009F26080123456789ABCDEF9F360200FF820200009C01019F1A0208409A030101019F02060000000123005F2A0208409F03060000000000008407A00000000310100425000010098000000000000000E8068020000002F0";
		     tmp = "0000160102015A000000000000000000000000000000000002007EFD669128E0FA1610403456789012345601100000000000400000000002500000000002500005041243116100000061000000000441181244050403090000601108400510000100C4F0F0F0F0F0F0F0F00C012345678901204034567890123456D030912312345000F2F1F2F4F1F2F0F0F0F4F4F1C1E3D4F0F1404040C3C1D9C440C1C3C3C5D7E3D6D94040C1C3D8E4C9D9C5D940D5C1D4C5404040404040404040404040C3C9E3E840D5C1D4C540404040E4E208400840084062FAF4E84B13D45D2001010100000000670100649F3303204000950500000400009F37049BADBCAB9F100C0B010A03A0B00000000000009F26080123456789ABCDEF9F360200FF820200009C01019F1A0208409A030101019F02060000000123005F2A0208409F03060000000000008407A00000000310100425000010098000000000000000E806*";
		//  tmp  = "00001601020160000000000000000000000000000000000002007EFD669128E0FA1610403456789012345601100000000000400000000002500000000002500005040559256100000061000000000403112925050403090000601108400510000100C4F0F0F0F0F0F0F0F00B012345678901204034567890123456D030912312345000F2F1F2F4F0F5F0F0F0F4F0F3C1E3D4F0F1404040C3C1D9C440C1C3C3C5D7E3D6D94040C1C3D8E4C9D9C5D940D5C1D4C5404040404040404040404040C3C9E3E840D5C1D4C540404040E4E208400840084062FAF4E84B13D45D2001010100000000670100649F3303204000950500000400009F37049BADBCAB9F100C0B010A03A0B00000000000009F26080123456789ABCDEF9F360200FF820200009C01019F1A0208409A030101019F02060000000123005F2A0208409F03060000000000008407A00000000310100425000010098000000000000000E8068020000002F0";
		Hashtable<String, String> isoBuffer = new Hashtable<String, String>();

		Message msgBuffer = new Message();
		msgBuffer.setMessage(tmp);
		visaisoFormatter.formatMessage(msgBuffer, isoBuffer);

		isoBuffer.put("H01", "16"); // Field-1
		isoBuffer.put("H02", "01"); // Field-2
		isoBuffer.put("H03", "02"); // Field-3
		isoBuffer.put("H04", "0160"); // Field-4
		isoBuffer.put("H05", "000000"); // Field-5
		isoBuffer.put("H06", "000000"); // Field-6
		isoBuffer.put("H07", "00"); // Field-7
		isoBuffer.put("H08", "0000"); // Field-8
		isoBuffer.put("H09", "000000"); // Field-9
		isoBuffer.put("H10", "00"); // Field-10
		isoBuffer.put("H11", "000000"); // Field-11
		isoBuffer.put("H12", "00"); // Field-12

		isoBuffer.put("MTI" ,"0210");
		isoBuffer.put("DE002" ,"4034567890123456");
		isoBuffer.put("DE003" ,"011000");
		isoBuffer.put("DE004" ,"000000004000");
		isoBuffer.put("DE005" ,"000000025000");
		isoBuffer.put("DE006" ,"000000025000");
		isoBuffer.put("DE007" ,"0502142324");
		isoBuffer.put("DE009" ,"61000000");
		isoBuffer.put("DE010" ,"61000000");
		isoBuffer.put("DE011" ,"000390");
		isoBuffer.put("DE012" ,"190935");
		isoBuffer.put("DE013" ,"0502");
		isoBuffer.put("DE014" ,"0309");
		isoBuffer.put("DE016" ,"0000");
		isoBuffer.put("DE018" ,"6011");
		isoBuffer.put("DE019" ,"0840");
		isoBuffer.put("DE022" ,"0510");
		isoBuffer.put("DE023" ,"0001");
		isoBuffer.put("DE025" ,"00");
		isoBuffer.put("DE028" ,"C4F0F0F0F0F0F0F0F0");
		isoBuffer.put("DE032" ,"012345678901");
		isoBuffer.put("DE035" ,"4034567890123456D030912312345000");
		isoBuffer.put("DE037" ,"F2F1F2F2F1F3F0F0F0F3F9F0");
		isoBuffer.put("DE041" ,"C1E3D4F0F1404040");
		isoBuffer.put("DE042" ,"C3C1D9C440C1C3C3C5D7E3D6D94040");
		isoBuffer.put("DE043" ,"C1C3D8E4C9D9C5D940D5C1D4C5404040404040404040404040C3C9E3E840D5C1D4C540404040E4E2");
		isoBuffer.put("DE049" ,"0840");
		isoBuffer.put("DE050" ,"0840");
		isoBuffer.put("DE051" ,"0840");
		isoBuffer.put("DE052" ,"62FAF4E84B13D45D");
		isoBuffer.put("DE053" ,"2001010100000000");
		isoBuffer.put("DE055" ,"0100649F3303204000950500000400009F37049BADBCAB9F100C0B010A03A0B00000000000009F26080123456789ABCDEF9F360200FF820200009C01019F1A0208409A030101019F02060000000123005F2A0208409F03060000000000008407A0000000031010");
		isoBuffer.put("DE060" ,"25000010");
		isoBuffer.put("DE062" ,"8000000000000000E8");
		isoBuffer.put("DE063" ,"8020000002F0");

		for (int i = 0; i < 128; i++) {
			isoBuffer.putIfAbsent("DE" + String.format("%03d",(i + 1)), "*");
		}

		msgBuffer.setMessage(new VisaFormatterImpl().buildSignOnMessage(isoBuffer));
	}

	@Override
	public String buildSignOnMessage(Hashtable<String, String> isoBuffer) {
		isoBuffer.put("MSG-HDR", buildHeader(isoBuffer));
		return buildISOMessage(isoBuffer, false);
	}

	@Override
	public String buildTransactionMessage(Hashtable<String, String> isoBuffer) {
		isoBuffer.put("MSG-HDR", buildHeader(isoBuffer));
		return buildISOMessage(isoBuffer, false);
	}






}
