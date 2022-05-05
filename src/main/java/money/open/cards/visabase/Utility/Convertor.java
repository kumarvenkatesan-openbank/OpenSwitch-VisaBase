package money.open.cards.visabase.Utility;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import money.open.cards.visabase.dto.TransactionRequestDto;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Slf4j
public class Convertor implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 * @param data
	 * @return
	 */
	private static String digits = "0123456789ABCDEF";

	/**
	 * Alpha to Hex conversion
	 * 
	 * @param data
	 * @return
	 */

	static String MASK_FORMAT="xxxx-xxxx-xxxx-####";
	public static String maskCardNumber(String cardNumber) {
		int index = 0;
		StringBuilder maskedNumber = new StringBuilder();
		for (int i = 0; i < MASK_FORMAT.length(); i++) {
			char c = MASK_FORMAT.charAt(i);
			if (c == '#') {
				maskedNumber.append(cardNumber.charAt(index));
				index++;
			} else if (c == 'x') {
				maskedNumber.append(c);
				index++;
			} else {
				maskedNumber.append(c);
			}
		}
		return maskedNumber.toString();
	}

	public static String alpha2Hex(String data) {
		log.debug("Inside alpha2Hex method");
		// log.info("Method Called for converting alphabets to hexbytes");
		char[] alpha = data.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < alpha.length; i++) {
			int count = Integer.toHexString(alpha[i]).toUpperCase().length();
			if (count <= 1) {
				sb.append("0").append(Integer.toHexString(alpha[i]).toUpperCase());
			} else {
				sb.append(Integer.toHexString(alpha[i]).toUpperCase());
			}
		}
		log.debug("Successfully executed alpha2Hex method");
		return sb.toString();
	}

	/** **** Added by Julius for Web service API call on Jul 20,2012 ****** */
	/**
	 * Alpha to Hex conversion
	 * 
	 * @param data
	 * @return
	 */
	public static String alpha2HexLowerCase(String data) {
		// log.info("Inside alpha2Hex()");
		// log.info("Method Called for converting alphabets to hexbytes");
		char[] alpha = data.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < alpha.length; i++) {
			int count = Integer.toHexString(alpha[i]).length();
			if (count <= 1) {
				sb.append("0").append(Integer.toHexString(alpha[i]));
			} else {
				sb.append(Integer.toHexString(alpha[i]));
			}
		}
		log.debug("Hexabyte Value Returned");
		return sb.toString();
	}

	/**
	 * this method return convert alpha to hex. it check for return length is even
	 * or it will pad with 20
	 * 
	 * @param data
	 * @return
	 */
	public static String alpha2HexEven(String data) {
		// log.info("Inside alpha2HexEven()");
		log.debug("Method called to convert alpha to hex and to check the length");
		data = Convertor.removeSpace(Convertor.alpha2Hex(data));
		int datalength = data.length() / 2;
		log.debug("Length of the hexbyte ::" + datalength);
		if ((datalength % 2) == 0)
			return data;
		else
			return data + "20";
	}

	public static String hexLength(String message) {
		// log.info("Inside hexLength()");
		log.debug("Method called to check the hex byte length");
		char[] char_data = message.toCharArray();
		int length = 0;
		for (int i = 1; i < char_data.length; i = i + 2) {
			length = length + 1;
		}
		log.debug("Length is passed to convertToHex method");
		return convertToHex(length);
	}

	/**
	 * FOR COVERTING STRING HEX VALUE TO DECIMAL
	 * 
	 * @param s
	 * @return
	 */
	public static int hex2decimal(String s) {
		s = s.toUpperCase();
		int val = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = digits.indexOf(c);
			val = 16 * val + d;
		}
		return val;
	}

	public static String decimal2hex(int s) {
		String tmp = Integer.toHexString(s);
		tmp = paddingZeroPrefix(tmp, 2).toUpperCase();
		return tmp;
	}

	public static void file2Hex(FileInputStream fis) {

	}

	public static String getPadding(String padFormat, int length) {
		// log.info("Inside getPadding()");
		log.debug("Method called for padding");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append(padFormat);
		}
		log.debug("Padding Value Generated");
		return sb.toString();
	}

	/***************************************************************************
	 * 
	 * @param parityCorrectValue
	 * @return
	 */
	public static String convertBinaryToHexaDecimal(String parityCorrectValue) {
		// log.info("Inside convertBinaryToHexaDecimal()");
		log.debug("Method called for converting Binary to Hexa decimal");
		String expectedHexadecimalValue = "";
		for (int i = 0; i < parityCorrectValue.length(); i++) {
			if (i % 4 == 0) {
				expectedHexadecimalValue = expectedHexadecimalValue
						+ Integer.toHexString(Integer.parseInt(parityCorrectValue.substring(i, i + 4), 2));

			}
		}
		log.debug("Binary value converted to HexaDecimal Value ");
		return expectedHexadecimalValue;
	}

	public static String getXorFromValues(String data1, String data2) {
		log.info("Method called for getting Xor values");
		log.debug("Method called for getting Xor values");
		char one[] = data1.toCharArray();
		char two[] = data2.toCharArray();
		String output = "";
		for (int i = 0; i < data1.length(); i++) {
			String first = "" + one[i];
			String second = "" + two[i];
			if (first.equals(second)) {
				output = output + 0;
			} else {
				output = output + 1;
			}
		}
		log.debug("Xor values generated:: " + output);
		return output;
	}

	/***************************************************************************
	 * 
	 * @param input
	 * @return
	 */
	public static String convertToBinary(String input) {
		log.info("Method called for converting input values to Binary ");
		log.debug("Method called for converting input values to Binary ");
		char[] dataArray;
		String[] res = new String[input.length()];
		dataArray = input.toCharArray();
		int arrayLength = dataArray.length;
		for (int counter = arrayLength - 1; counter >= 0; counter--) {

			if (dataArray[counter] == 'A' || dataArray[counter] == 'B' || dataArray[counter] == 'C'
					|| dataArray[counter] == 'D' || dataArray[counter] == 'E' || dataArray[counter] == 'F') {

				int sd = hexatoDecimal(String.valueOf(dataArray[counter]));
				res[counter] = Integer.toBinaryString(sd);

			} else {
				int s1 = Integer.parseInt(String.valueOf(dataArray[counter]));
				res[counter] = Integer.toBinaryString(s1);

			}

		}
		log.debug("Binary Value Generated");
		String result = "";
		String finalResult = "";
		int resLength = res.length;
		for (int counter = 0; counter <= resLength - 1; counter++) {
			result = res[counter];
			if (result.length() == 3) {
				result = "0" + result;
			}
			if (result.length() == 2) {
				result = "00" + result;
			}
			if (result.length() == 1) {
				result = "000" + result;
			}
			finalResult = finalResult + result;

		}
		log.debug("Final Binary Value Generated " + finalResult);
		return finalResult;

	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	public static String convertToHex(int data) {
		String hexlen = Integer.toHexString(data).toUpperCase();
		if (hexlen.length() <= 1) {
			hexlen = "0" + Integer.toHexString(data).toUpperCase();
		}
		return hexlen;

	}

	public static byte[] byteConvertor(String str) {
		// log.info("Method called for converting alpha to byte");
		// log.debug("Method called for converting alpha to byte");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.reset();
		int j = 0;
		int strLen = str.length();

		for (int i = 0; i < strLen; i = i + 2) {
			j = i + 2;
			baos.write((byte) Integer.parseInt(str.substring(i, j), 16));
		}
		return baos.toByteArray();
	}

	/**
	 * This method will return a decimal value for the given hexadecimal input
	 * 
	 * @param data
	 * @return
	 */
	public static int hexatoDecimal(String data) {
		if (data.length() <= 1) {
			data = "0" + data;
		}
		String strout = "";
		char[] ch = data.toCharArray();
		StringBuffer stvalue = new StringBuffer();
		for (int i = 0; i < ch.length; i = i + 2) {
			char s = ch[i];
			char s1 = ch[i + 1];
			stvalue.append(s).append(s1);
			int intvalue = Integer.parseInt(stvalue.toString(), 16);
			stvalue.delete(0, 1);
			strout = strout + Integer.toString(intvalue);
		}
		log.debug("Hexa value converted to decimal value");
		return Integer.parseInt(strout);
	}

	/**
	 * It will return Decimal Length of given data
	 * 
	 * @param data
	 * @return
	 */
	public static int getDecimalLength(int data) {
		log.info("Method called for getting decimal length ");
		log.debug("Method called for getting decimal length");
		return data * 2;
	}

	/**
	 * Mansoor 10.10.2006
	 * 
	 * @param data Alpha to Hex conversion with space
	 * @return
	 */
	public static String alpha2HexC(String data) {
		// log.info("Method called for converting alpha to hex with space");
		// log.debug("Method called for converting alpha to hex with space");
		char[] alpha = data.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < alpha.length; i++) {
			int count = Integer.toHexString(alpha[i]).toUpperCase().length();
			if (count <= 1) {
				sb.append("").append("0").append(Integer.toHexString(alpha[i]).toUpperCase());
			} else {
				sb.append("").append(Integer.toHexString(alpha[i]).toUpperCase());
			}
		}
		log.info("Alpha to hexa conversion value :: " + sb.toString());
		return sb.toString();
	}

	/**
	 * Hex to Alpha conversion Ediited by mansoor on 10.01.07 to handle odd data
	 * also
	 * 
	 * @param data
	 * @return
	 */
	/*
	 * public static String hex2Alpha(String data) { int len = data.length();
	 * StringBuffer sb = new StringBuffer(); for (int i = 0; i < len; i = i + 2) {
	 * int j = i + 2; sb.append((char) Integer.valueOf(data.substring(i, j), 16)
	 * .intValue()); } data = sb.toString(); return data; }
	 */

	public static String hex2Alpha(String data) {
		// log.info("Method called for converting hex value to alpha value");
		// log.debug("Method called for converting hex value to alpha
		// value");
		try {
			int len = data.length();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < len; i = i + 2) {
				int j = i + 2;

				sb.append((char) Integer.valueOf(data.substring(i, j), 16).intValue());
			}
			data = sb.toString();
			// log.info("Hexa value generated from alpha ");
		} catch (NumberFormatException e) {

		}
		return data;
	}


	private static String hexToAscii(String hexStr) {
		StringBuilder output = new StringBuilder("");

		for (int i = 0; i < hexStr.length(); i += 2) {
			String str = hexStr.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}

		return output.toString();
	}

	/**
	 * Mansoor 22.09.2006 //method removeSpace() is added
	 * 
	 * @param data
	 * @return
	 */
	public static String removeSpace(String data) {
		log.info("Method called to remove space");
		// log.debug("Method called to remove space");
		char[] ipArray = data.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ipArray.length; i++) {
			if ((int) ipArray[i] != 32) {
				sb.append(ipArray[i]);
			}
		}
		log.info("Space removed ");
		return sb.toString();
	}

	public static String addSpace(String data) {
		log.info("Method called for adding space");
		log.debug("Method called for adding space");
		char[] char_data = data.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 1; i < char_data.length; i = i + 2) {
			sb.append(char_data[i - 1]).append(char_data[i]).append(" ");
		}
		log.debug("Space added ");
		return sb.toString();
	}

	/**
	 * code convert byte array to string suvedha 28.09.2006
	 * 
	 * @param ba
	 * @return
	 */
	/*
	 * public static String ba2s(byte[] ba) { StringBuffer sb = new StringBuffer();
	 * int off = 0; int len = ba.length;
	 * 
	 * for (int i = off; i < off + len; i++) { int b = (int) ba[i]; char c =
	 * digits.charAt((b >> 4) & 0xf); sb.append(c); c = digits.charAt(b & 0xf);
	 * sb.append((char) c); } return hex2Alpha(sb.toString()); }
	 */

	/**
	 * code convert byte array to string suvedha 28.09.2006
	 * 
	 * @param ba
	 * @return
	 */
	public static String ba2s(byte[] ba) {
		// log.info("Method called for converting byte array to string");
		log.debug("Method called for converting byte array to string");
		StringBuffer sb = new StringBuffer();
		int off = 0;
		int len = ba.length;

		for (int i = off; i < off + len; i++) {
			int b = (int) ba[i];
			char c = digits.charAt((b >> 4) & 0xf);
			sb.append(c);
			c = digits.charAt(b & 0xf);
			sb.append((char) c);
		}
		log.debug("Byte array convered to string and passed to hex2Alpha method");
		return hex2Alpha(sb.toString());
	}

	public static String unicodeParser(String data) {
		log.info("Method called for parsing unicode");
		log.debug("Method called for parsing unicode");
		StringTokenizer str = new StringTokenizer(data);
		String temp = "";
		while (str.hasMoreTokens()) {
			temp = temp + "\\u" + str.nextToken();
		}
		log.debug("Unicode parsed :: " + temp);
		return temp;

	}

	/***************************************************************************
	 * added on 26.12.06 by mansoor this method is produce formatted output of the
	 * given message
	 *
	 * @param message
	 */
	public static void print(String message) {
		log.info("print" + message);
	}

	public static String getXORValue(String data1, String data2) {
		log.info("Method called for getting XOR value");
		log.debug("Method called for getting XOR value");
		int x = hexatoDecimal(data1) ^ hexatoDecimal(data2);
		log.debug("XOR value generated :: " + x);
		log.debug("XOR value generated and value passed to convertToHex method");
		return convertToHex(x);
	}

	public static String rightPad(String data) {
		log.info("Method called for padding right");
		log.debug("Method called for padding right");
		String temp = data;
		for (int i = temp.length(); i < 16; i++) {
			temp = temp + "F";
		}
		log.debug("Right padding value generated :: " + temp);
		return temp;
	}

	public static String getAccountNumber(String accno) {
		log.info("Method called for getting account number ");
		log.debug("Method called for getting account number");
		if (accno.length() == 16) {
			accno = accno.substring(3, 15);
			log.debug("Account Number got " + accno);
			return accno;
		} else {
			return "";
		}

	}

	/**
	 *
	 * @param data
	 * @param expectedLength
	 * @return
	 */
	public static String padding(String data, int expectedLength) {
		log.info("Method called for padding data");
		log.debug("Method called for padding data");
		StringBuffer sb = new StringBuffer(data);
		if (data.length() == expectedLength) {
			return data;
		} else
			for (int i = data.length(); i < (expectedLength); i++) {
				sb.append("F");

			}
		log.debug("Padded value :: " + sb.toString());
		return sb.toString();
	}

	public static String paddingZeroPrefix(String data, int expectedLength) {
		StringBuffer sb = new StringBuffer(data);
		if (data.length() == expectedLength) {
			return data;
		} else
			for (int i = data.length(); i < (expectedLength); i++) {
				sb.insert(0, '0');

			}
		log.debug("Padded value :: " + sb.toString());
		return sb.toString();
	}

	public static String paddingSpacePrefix(String data, int expectedLength) {
		StringBuffer sb = new StringBuffer(data);
		if (data.length() == expectedLength) {
			return data;
		} else
			for (int i = data.length(); i < (expectedLength); i++) {
				sb.insert(0, ' ');

			}
		log.debug("Padded value :: " + sb.toString());
		return sb.toString();
	}

	public static String paddingZeroSuffix(String data, int expectedLength) {
		String sb = "";
		if (data.length() == expectedLength) {
			return data;
		} else
			for (int i = data.length(); i < (expectedLength); i++) {
				sb += "0";

			}
		sb = data + sb;
		log.debug("Padded value :: " + sb.toString());
		return sb;
	}

	public static String appendPadding(String data, int expectedLength) {
		// log.info("Method called for appending space");
		// log.debug("Method called for appending space");
		StringBuilder sb = new StringBuilder(data);
		if (data.length() == expectedLength) {
			return data;
		} else
			for (int i = data.length(); i < (expectedLength); i++) {
				sb.append(" ");

			}
		log.info("Value after data padded :: " + sb.toString());
		return sb.toString();
	}

	public static String appendPaddingRefund(String data, int expectedLength) {
		// log.info("Method called for appending space");
		// log.debug("Method called for appending space");
		StringBuilder sb = new StringBuilder(data);

		for (int i = 0; i < (expectedLength); i++) {
			sb.append(" ");

		}
		log.info("Value after data padded :: " + sb.toString());
		return sb.toString();
	}

	public static String getBinaryValue(String intValue) {
		log.info("Method called for getting Binary Value");
		log.debug("Method called for getting Binary Value");
		String binaryValue = "";
		String padValue = "";
		String binValue = "";
		for (int i = 0; i < intValue.length(); i++) {
			int decimalValue = Integer.parseInt(intValue.substring(i, i + 1));

			binValue = Integer.toBinaryString(decimalValue);
			if (binValue.length() != 4) {
				int length = binValue.length();
				if (length == 1)
					padValue = "000" + binValue;
				if (length == 2)
					padValue = "00" + binValue;
				if (length == 3)
					padValue = "0" + binValue;

				binaryValue += padValue;
			} else {
				binaryValue += binValue;
			}

		}
		log.debug("Binary value generated :: " + binaryValue);
		return binaryValue;
	}

	public static String convertPanMask(String panNum) {
		log.info("Method called for converting PAN mask");
		log.debug("Method called for converting PAN mask");
		if (panNum.length() != 22) {
			String middleDigits = "  XXXX  XXXX  ";
			String first4Digit = panNum.substring(0, 4);
			String last4Digit = panNum.substring(12, 16);
			panNum = first4Digit + middleDigits + last4Digit;
		}
		log.debug("Pan No generated :: " + panNum);
		return panNum;
	}

	// KMS integration ACM3.0 Starts
	public static int gethexValue(String s) {
		log.info("Method called for getting hexa values");
		log.debug("Method called for getting hexa values");
		s = s.toUpperCase();
		int val = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = digits.indexOf(c);
			val = 16 * val + d;
		}
		log.debug("Hexa value generated :: " + val);
		return val;
	}

	/**
	 * Mansoor 23.09.2006 //method addSpace() is added
	 *
	 * @param count
	 * @return
	 */
	public static String addSpace(int count) {
		//log.debug("Method called for adding space");
		//log.info("Method called for adding space:" + count);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count; i++) {
			sb.append(" ");
		}
		//log.info("After adding space [" + sb.toString() + "]");
		return sb.toString();
	}

	/**
	 *
	 * @param hexDigit
	 * @return
	 */
	public static boolean isHexDigit(String hexDigit) {
		log.info("Method called for checking whether input value is hex digit or not");
		log.debug("Method called for checking whether input value is hex digit or not");
		char[] hexDigitArray = hexDigit.toCharArray();
		int hexDigitLength = hexDigitArray.length;

		boolean isNotHex;
		for (int i = 0; i < hexDigitLength; i++) {
			isNotHex = Character.digit(hexDigitArray[i], 16) == -1;
			if (isNotHex) {
				log.debug("Input value is not a hex digit");
				return false;
			}
		}
		log.debug("Input value is a hex digit");
		return true;
	}

	public static byte[] strToBytes(String s) {
		log.debug("Method called for converting string to byte");
		if (s.length() % 2 != 0)
			throw new IllegalArgumentException("String length % 2 != 0");
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		for (int i = 0; i < s.length(); i += 2) {
			byte byte0 = (byte) (Character.digit(s.charAt(i), 16) & 0xff);
			byte0 <<= 4;
			byte0 |= Character.digit(s.charAt(i + 1), 16);
			bytearrayoutputstream.write(byte0);
		}
		log.debug("String converted to byte ");
		return bytearrayoutputstream.toByteArray();
	}

	/***************************************************************************
	 *
	 * @param data
	 * @return
	 */
	public static String convertToNativeHex(long data) {
		log.info("Method called to convert data to native hex");
		log.debug("Method called to convert data to native hex");
		String hexlen = Long.toHexString(data).toUpperCase();
		if (hexlen.length() <= 1) {
			hexlen = "00000" + Long.toHexString(data).toUpperCase();
		} else if (hexlen.length() <= 2) {
			hexlen = "0000" + Long.toHexString(data).toUpperCase();
		} else if (hexlen.length() <= 3) {
			hexlen = "000" + Long.toHexString(data).toUpperCase();
		}
		log.debug("Hex length value generated :: " + hexlen);
		return hexlen;
	}

	/**
	 * This method will return Long value for given hexadecimal input
	 *
	 * @param data
	 * @return
	 */
	public static long hexatoLong(String data) {
		return Long.parseLong(data, 16);
	}

	/**
	 * This method will return int value for given hexadecimal input
	 *
	 * @param data
	 * @return
	 */
	public static long hexatoInteger(String data) {
		return Integer.parseInt(data, 16);
	}

	public static String getEMVProperties(String data) {
		log.info("Method called for getting EMV Properties");
		log.debug("Method called for getting EMV Properties");
		char[] alpha = data.toCharArray();
		StringBuffer sb = new StringBuffer();
		String emvData;
		for (int i = 0; i < alpha.length; i++) {
			int count = Integer.toHexString(alpha[i]).toUpperCase().length();
			if (count <= 1) {
				sb.append("0").append(Integer.toHexString(alpha[i]).toUpperCase());
			} else {
				sb.append(Integer.toHexString(alpha[i]).toUpperCase());
			}
		}
		emvData = sb.toString();
		int emvDataLength = emvData.length();
		int length;
		String hexSpace = " ";
		if (emvDataLength < 40) {
			length = 40 - emvDataLength;
			length = length / 2;
			for (int i = 0; i < length; i++) {
				hexSpace = "20" + hexSpace;
			}
			emvData = emvData + hexSpace;
		}
		log.debug("EMV Data generated :: " + emvData);
		return emvData;

	}

	/***************************************************************************
	 * This method gets the user name and pads with spaces
	 *
	 * @param data
	 * @return hex string
	 */
	public static String getNativeName(String data) {
		log.info("Method called for getting native name");
		log.debug("Method called for getting native name");
		String NativeName = Convertor.alpha2Hex(data);

		int NativeNameLength = NativeName.length();
		int length;
		String hexSpace = " ";
		if (NativeNameLength < 40) {
			length = 40 - NativeNameLength;
			length = length / 2;
			for (int i = 0; i < length; i++) {
				hexSpace = "20" + hexSpace;
			}
			NativeName = NativeName + hexSpace;
		}
		log.debug("Native Name :: " + NativeName);
		return NativeName;

	}

	/**
	 * This method gets the user age and pads with spaces
	 *
	 * @param data
	 * @return hex string
	 */
	public static String getNativeAge(int data) {
		log.info("Method called for getting native age");
		log.debug("Method called for getting native age");
		String NativeAge = Integer.toHexString(data).toUpperCase();

		if (NativeAge.length() < 2) {
			NativeAge = "0" + NativeAge;
		}
		log.debug("Native Age :: " + NativeAge);
		return NativeAge;

	}

	/**
	 *
	 * @param data
	 * @param dataLength
	 * @return
	 */
	public static String stringPadding(String data, int dataLength) {
		log.info("Method called for string padding");
		log.debug("Method called for string padding");
		int len = data.length();
		data = alpha2Hex(data);
		if (len < dataLength) {
			len = dataLength - len;
			for (int i = 0; i < len; i++)
				data = data + "FF";
		}
		log.debug("String padded value :: " + data);
		return data;
	}

	/***************************************************************************
	 * This method gets the user profession and pads with spaces
	 *
	 * @param data
	 * @return hex string
	 */
	public static String getNativeProfession(String data) {
		log.info("Method called for getting native profession");
		log.debug("Method called for getting native profession");
		String NativeProfession = Convertor.alpha2Hex(data);
		int NativeProfessionLength = NativeProfession.length();
		int length;
		String hexSpace = " ";
		if (NativeProfessionLength < 60) {
			length = 60 - NativeProfessionLength;
			length = length / 2;
			for (int i = 0; i < length; i++) {
				hexSpace = "20" + hexSpace;
			}
			NativeProfession = NativeProfession + hexSpace;
		}
		log.debug("Native Profession :: " + NativeProfession);
		return NativeProfession;
	}

	// Added By Bharathi G for null and empty check

	public static boolean isNullorEmpty(String cmd) {
		log.info("Method called for null and empty check");
		log.debug("Method called for null and empty check");
		boolean cmdStatus = false;

		if (cmd.contains("null") || cmd.contains("NULL") || cmd.contains(" ")) {
			cmdStatus = false;
		} else {
			cmdStatus = true;
		}
		log.debug("Null or empty value status :: " + cmdStatus);
		return cmdStatus;
	}

	public static String truncate(String transferAmount) {
		log.info("Method called to truncate the value");
		log.debug("Method called to truncate the value");
		String[] dotValue = transferAmount.split("\\.");

		if (transferAmount.indexOf('.') == -1) {

			return transferAmount + ".0";
		}
		int dot = transferAmount.length() - transferAmount.indexOf(".");
		if (dot > 3) {
			return transferAmount.substring(0, transferAmount.length() - dot + 3);
			// return dotValue[0] + ".0";
		} else if (dot == 2) {
			log.debug("Transfer Amount :: " + transferAmount);
			return transferAmount;
		}
		if (dotValue[1].equals("00")) {
			return dotValue[0] + ".0";
		} else {
			log.debug("Transfer Amount :: " + transferAmount);
			return transferAmount;
		}
	}

	// Added by Madan Mohan for converting binary EBCDIC to ASCII java string
	public static String convertEBCDIC(String ebcdic) {
		try {
			ebcdic = new String(ebcdic.getBytes(), "CP1047");
		} catch (UnsupportedEncodingException use) {
			log.error("Cp037 not supported :(");
		}
		return ebcdic;
	}

	public static String convert(String[] hexDigits) {
		byte[] bytes = new byte[hexDigits.length];

		for (int i = 0; i < bytes.length; i++)
			bytes[i] = Integer.decode(hexDigits[i]).byteValue();

		String result;
		try {
			result = new String(bytes, "ASCII");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/***************************************************************************
	 * This Method convert String to Byte Array
	 *
	 * @param str
	 * @return
	 */
	public static byte[] byteConvertorForPGP(String str) {
		// log.info("Method called for converting alpha to byte");
		// log.debug("Method called for converting alpha to byte");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.reset();
		int j = 0;
		int strLen = str.length();

		for (int i = 0; i < strLen; i = i + 2) {
			j = i + 2;
			log.info("i :::::: " + i + " / j ::::: " + j);
			baos.write((byte) Integer.parseInt(str.substring(i, j), 16));
		}
		return baos.toByteArray();
	}

	/* To get equivalent Hex length for a decimal length */
	public static String getHexLength(int decimalLength) {
		log.info("Method called for getting decimal length ");
		log.debug("Method called for getting decimal length");
		int byteValue = decimalLength / 8;
		return convertToHex(byteValue);
	}

	public static String ebcdicOfHex(char hexbyte) {
		String ebcdic = null;

		switch (hexbyte) {
		case 0x00:
			ebcdic = "<NUL>";
			break;
		case 0x01:
			ebcdic = "<SOH>";
			break;
		case 0x02:
			ebcdic = "<STX>";
			break;
		case 0x03:
			ebcdic = "<ETX>";
			break;
		case 0x04:
			ebcdic = "<SEL>";
			break;
		case 0x05:
			ebcdic = "<HT>";
			break;
		case 0x06:
			ebcdic = "<RNL>";
			break;
		case 0x07:
			ebcdic = "<DEL>";
			break;
		case 0x08:
			ebcdic = "<GE>";
			break;
		case 0x09:
			ebcdic = "<SPS>";
			break;
		case 0x0A:
			ebcdic = "<RPT>";
			break;
		case 0x0B:
			ebcdic = "<VT>";
			break;
		case 0x0C:
			ebcdic = "<FF>";
			break;
		case 0x0D:
			ebcdic = "\r";
			break;
		case 0x0E:
			ebcdic = "<SO>";
			break;
		case 0x0F:
			ebcdic = "<SI>";
			break;
		case 0x10:
			ebcdic = "<DLE>";
			break;
		case 0x11:
			ebcdic = "<DC1>";
			break;
		case 0x12:
			ebcdic = "<DC2>";
			break;
		case 0x13:
			ebcdic = "<DC3>";
			break;
		case 0x14:
			ebcdic = "<RES/ENP>";
			break;
		case 0x15:
			ebcdic = "\n";
			break;
		case 0x16:
			ebcdic = "<BS>";
			break;
		case 0x17:
			ebcdic = "<POC>";
			break;
		case 0x18:
			ebcdic = "<CAN>";
			break;
		case 0x19:
			ebcdic = "<EM>";
			break;
		case 0x1A:
			ebcdic = "<UBS>";
			break;
		case 0x1B:
			ebcdic = "<CU1>";
			break;
		case 0x1C:
			ebcdic = "<IFS>";
			break;
		case 0x1D:
			ebcdic = "<IGS>";
			break;
		case 0x1E:
			ebcdic = "<IRS>";
			break;
		case 0x1F:
			ebcdic = "<ITB/IUS>";
			break;
		case 0x20:
			ebcdic = "<DS>";
			break;
		case 0x21:
			ebcdic = "<SOS>";
			break;
		case 0x22:
			ebcdic = "<FS>";
			break;
		case 0x23:
			ebcdic = "<WUS>";
			break;
		case 0x24:
			ebcdic = "<BYP/INP>";
			break;
		case 0x25:
			ebcdic = "<LF>";
			break;
		case 0x26:
			ebcdic = "<ETB>";
			break;
		case 0x27:
			ebcdic = "<ESC>";
			break;
		case 0x28:
			ebcdic = "<SA>";
			break;
		case 0x29:
			ebcdic = "<SFE>";
			break;
		case 0x2A:
			ebcdic = "<SM/SW>";
			break;
		case 0x2B:
			ebcdic = "<CSP>";
			break;
		case 0x2C:
			ebcdic = "<MFA>";
			break;
		case 0x2D:
			ebcdic = "<ENQ>";
			break;
		case 0x2E:
			ebcdic = "<ACK>";
			break;
		case 0x2F:
			ebcdic = "<BEL>";
			break;
		case 0x30:
			break;
		case 0x31:
			break;
		case 0x32:
			ebcdic = "<SYN>";
			break;
		case 0x33:
			ebcdic = "<IR>";
			break;
		case 0x34:
			ebcdic = "<PP>";
			break;
		case 0x35:
			ebcdic = "<TRN>";
			break;
		case 0x36:
			ebcdic = "<NBS>";
			break;
		case 0x37:
			ebcdic = "<EOT>";
			break;
		case 0x38:
			ebcdic = "<SBS>";
			break;
		case 0x39:
			ebcdic = "<IT>";
			break;
		case 0x3A:
			ebcdic = "<RFF>";
			break;
		case 0x3B:
			ebcdic = "<CU3>";
			break;
		case 0x3C:
			ebcdic = "<DC4>";
			break;
		case 0x3D:
			ebcdic = "<NAK>";
			break;
		case 0x3E:
			break;
		case 0x3F:
			ebcdic = "<SUB>";
			break;
		case 0x40:
			ebcdic = " ";
			break;
		case 0x41:
			ebcdic = "<RSP>";
			break;
		case 0x42:
			break;
		case 0x43:
			break;
		case 0x44:
			break;
		case 0x45:
			break;
		case 0x46:
			break;
		case 0x47:
			break;
		case 0x48:
			break;
		case 0x49:
			break;
		case 0x4A:
			ebcdic = "[";
			break;
		case 0x4B:
			ebcdic = ".";
			break;
		case 0x4C:
			ebcdic = "<";
			break;
		case 0x4D:
			ebcdic = "(";
			break;
		case 0x4E:
			ebcdic = "+";
			break;
		case 0x4F:
			ebcdic = "|";
			break;
		case 0x50:
			ebcdic = "&";
			break;
		case 0x51:
			break;
		case 0x52:
			break;
		case 0x53:
			break;
		case 0x54:
			break;
		case 0x55:
			break;
		case 0x56:
			break;
		case 0x57:
			break;
		case 0x58:
			break;
		case 0x59:
			break;
		case 0x5A:
			ebcdic = "!";
			break;
		case 0x5B:
			ebcdic = "$";
			break;
		case 0x5C:
			ebcdic = "*";
			break;
		case 0x5D:
			ebcdic = ")";
			break;
		case 0x5E:
			ebcdic = ";";
			break;
		case 0x5F:
			ebcdic = "^";
			break;
		case 0x60:
			ebcdic = "-";
			break;
		case 0x61:
			ebcdic = "/";
			break;
		case 0x62:
			break;
		case 0x63:
			break;
		case 0x64:
			break;
		case 0x65:
			break;
		case 0x66:
			break;
		case 0x67:
			break;
		case 0x68:
			break;
		case 0x69:
			break;
		case 0x6A:
			ebcdic = "|";
			break;
		case 0x6B:
			ebcdic = ",";
			break;
		case 0x6C:
			ebcdic = "%";
			break;
		case 0x6D:
			ebcdic = "_";
			break;
		case 0x6E:
			ebcdic = ">";
			break;
		case 0x6F:
			ebcdic = "?";
			break;
		case 0x70:
			break;
		case 0x71:
			break;
		case 0x72:
			break;
		case 0x73:
			break;
		case 0x74:
			break;
		case 0x75:
			break;
		case 0x76:
			break;
		case 0x77:
			break;
		case 0x78:
			break;
		case 0x79:
			ebcdic = "`";
			break;
		case 0x7A:
			ebcdic = ":";
			break;
		case 0x7B:
			ebcdic = "#";
			break;
		case 0x7C:
			ebcdic = "@";
			break;
		case 0x7D:
			ebcdic = "'";
			break;
		case 0x7E:
			ebcdic = "=";
			break;
		case 0x7F:
			ebcdic = "\"";
			break;
		case 0x80:
			break;
		case 0x81:
			ebcdic = "a";
			break;
		case 0x82:
			ebcdic = "b";
			break;
		case 0x83:
			ebcdic = "c";
			break;
		case 0x84:
			ebcdic = "d";
			break;
		case 0x85:
			ebcdic = "e";
			break;
		case 0x86:
			ebcdic = "f";
			break;
		case 0x87:
			ebcdic = "g";
			break;
		case 0x88:
			ebcdic = "h";
			break;
		case 0x89:
			ebcdic = "i";
			break;
		case 0x8A:
			break;
		case 0x8B:
			ebcdic = "{";
			break;
		case 0x8C:
			break;
		case 0x8D:
			break;
		case 0x8E:
			break;
		case 0x8F:
			ebcdic = "+";
			break;
		case 0x90:
			break;
		case 0x91:
			ebcdic = "j";
			break;
		case 0x92:
			ebcdic = "k";
			break;
		case 0x93:
			ebcdic = "l";
			break;
		case 0x94:
			ebcdic = "m";
			break;
		case 0x95:
			ebcdic = "n";
			break;
		case 0x96:
			ebcdic = "o";
			break;
		case 0x97:
			ebcdic = "p";
			break;
		case 0x98:
			ebcdic = "q";
			break;
		case 0x99:
			ebcdic = "r";
			break;
		case 0x9A:
			break;
		case 0x9B:
			ebcdic = "}";
			break;
		case 0x9C:
			break;
		case 0x9D:
			break;
		case 0x9E:
			break;
		case 0x9F:
			break;
		case 0xA0:
			break;
		case 0xA1:
			ebcdic = "~";
			break;
		case 0xA2:
			ebcdic = "s";
			break;
		case 0xA3:
			ebcdic = "t";
			break;
		case 0xA4:
			ebcdic = "u";
			break;
		case 0xA5:
			ebcdic = "v";
			break;
		case 0xA6:
			ebcdic = "w";
			break;
		case 0xA7:
			ebcdic = "x";
			break;
		case 0xA8:
			ebcdic = "y";
			break;
		case 0xA9:
			ebcdic = "z";
			break;
		case 0xAA:
			break;
		case 0xAB:
			break;
		case 0xAC:
			break;
		case 0xAD:
			ebcdic = "[";
			break;
		case 0xAE:
			break;
		case 0xAF:
			break;
		case 0xB0:
			break;
		case 0xB1:
			break;
		case 0xB2:
			break;
		case 0xB3:
			break;
		case 0xB4:
			break;
		case 0xB5:
			break;
		case 0xB6:
			break;
		case 0xB7:
			break;
		case 0xB8:
			break;
		case 0xB9:
			ebcdic = "`";
			break;
		case 0xBA:
			break;
		case 0xBB:
			break;
		case 0xBC:
			break;
		case 0xBD:
			break;
		case 0xBE:
			break;
		case 0xBF:
			break;
		case 0xC0:
			ebcdic = "{";
			break;
		case 0xC1:
			ebcdic = "A";
			break;
		case 0xC2:
			ebcdic = "B";
			break;
		case 0xC3:
			ebcdic = "C";
			break;
		case 0xC4:
			ebcdic = "D";
			break;
		case 0xC5:
			ebcdic = "E";
			break;
		case 0xC6:
			ebcdic = "F";
			break;
		case 0xC7:
			ebcdic = "G";
			break;
		case 0xC8:
			ebcdic = "H";
			break;
		case 0xC9:
			ebcdic = "I";
			break;
		case 0xCA:
			break;
		case 0xCB:
			break;
		case 0xCC:
			break;
		case 0xCD:
			break;
		case 0xCE:
			break;
		case 0xCF:
			break;
		case 0xD0:
			ebcdic = "}";
			break;
		case 0xD1:
			ebcdic = "J";
			break;
		case 0xD2:
			ebcdic = "K";
			break;
		case 0xD3:
			ebcdic = "L";
			break;
		case 0xD4:
			ebcdic = "M";
			break;
		case 0xD5:
			ebcdic = "N";
			break;
		case 0xD6:
			ebcdic = "O";
			break;
		case 0xD7:
			ebcdic = "P";
			break;
		case 0xD8:
			ebcdic = "Q";
			break;
		case 0xD9:
			ebcdic = "R";
			break;
		case 0xDA:
			break;
		case 0xDB:
			break;
		case 0xDC:
			break;
		case 0xDD:
			break;
		case 0xDE:
			break;
		case 0xDF:
			break;
		case 0xE0:
			ebcdic = "\\";
			break;
		case 0xE1:
			break;
		case 0xE2:
			ebcdic = "S";
			break;
		case 0xE3:
			ebcdic = "T";
			break;
		case 0xE4:
			ebcdic = "U";
			break;
		case 0xE5:
			ebcdic = "V";
			break;
		case 0xE6:
			ebcdic = "W";
			break;
		case 0xE7:
			ebcdic = "X";
			break;
		case 0xE8:
			ebcdic = "Y";
			break;
		case 0xE9:
			ebcdic = "Z";
			break;
		case 0xEA:
			break;
		case 0xEB:
			break;
		case 0xEC:
			break;
		case 0xED:
			break;
		case 0xEE:
			break;
		case 0xEF:
			break;
		case 0xF0:
			ebcdic = "0";
			break;
		case 0xF1:
			ebcdic = "1";
			break;
		case 0xF2:
			ebcdic = "2";
			break;
		case 0xF3:
			ebcdic = "3";
			break;
		case 0xF4:
			ebcdic = "4";
			break;
		case 0xF5:
			ebcdic = "5";
			break;
		case 0xF6:
			ebcdic = "6";
			break;
		case 0xF7:
			ebcdic = "7";
			break;
		case 0xF8:
			ebcdic = "8";
			break;
		case 0xF9:
			ebcdic = "9";
			break;
		case 0xFA:
			break;
		case 0xFB:
			break;
		case 0xFC:
			break;
		case 0xFD:
			break;
		case 0xFE:
			break;
		case 0xFF:
			break;
		default:
			ebcdic = "<<UNHANDLED CODE>>";
			break;
		}
		return ebcdic;
	}

	public static String ebcdicToHex(char hexbyte) {
		String ebcdic = null;

		switch (hexbyte) {
		case ' ':
			ebcdic = "40";
			break;
		case '.':
			ebcdic = "4B";
			break;
		case '<':
			ebcdic = "4C";
			break;
		case '(':
			ebcdic = "4D";
			break;
		case '+':
			ebcdic = "4E";
			break;
		case '|':
			ebcdic = "4F";
			break;
		case '&':
			ebcdic = "50";
			break;
		case '!':
			ebcdic = "5A";
			break;
		case '$':
			ebcdic = "5B";
			break;
		case '*':
			ebcdic = "5C";
			break;
		case ')':
			ebcdic = "5D";
			break;
		case ';':
			ebcdic = "5E";
			break;
		case '^':
			ebcdic = "5F";
			break;
		case '-':
			ebcdic = "60";
			break;
		case '/':
			ebcdic = "61";
			break;

		case ',':
			ebcdic = "6B";
			break;
		case '%':
			ebcdic = "6C";
			break;
		case '_':
			ebcdic = "6D";
			break;
		case '>':
			ebcdic = "6E";
			break;
		case '?':
			ebcdic = "6F";
			break;
		case ':':
			ebcdic = "7A";
			break;
		case '#':
			ebcdic = "7B";
			break;
		case '@':
			ebcdic = "7C";
			break;
		case '\'':
			ebcdic = "7D";
			break;
		case '=':
			ebcdic = "7E";
			break;
		case '"':
			ebcdic = "7F";
			break;

		case 'a':
			ebcdic = "81";
			break;
		case 'b':
			ebcdic = "82";
			break;
		case 'c':
			ebcdic = "83";
			break;
		case 'd':
			ebcdic = "84";
			break;
		case 'e':
			ebcdic = "85";
			break;
		case 'f':
			ebcdic = "86";
			break;
		case 'g':
			ebcdic = "87";
			break;
		case 'h':
			ebcdic = "88";
			break;
		case 'i':
			ebcdic = "89";
			break;

		case 'j':
			ebcdic = "91";
			break;
		case 'k':
			ebcdic = "92";
			break;
		case 'l':
			ebcdic = "93";
			break;
		case 'm':
			ebcdic = "94";
			break;
		case 'n':
			ebcdic = "95";
			break;
		case 'o':
			ebcdic = "96";
			break;
		case 'p':
			ebcdic = "97";
			break;
		case 'q':
			ebcdic = "98";
			break;
		case 'r':
			ebcdic = "99";
			break;

		case 's':
			ebcdic = "A2";
			break;
		case 't':
			ebcdic = "A3";
			break;
		case 'u':
			ebcdic = "A4";
			break;
		case 'v':
			ebcdic = "A5";
			break;
		case 'w':
			ebcdic = "A6";
			break;
		case 'x':
			ebcdic = "A7";
			break;
		case 'y':
			ebcdic = "A8";
			break;
		case 'z':
			ebcdic = "A9";
			break;
		case '[':
			ebcdic = "AD";
			break;
		case '`':
			ebcdic = "B9";
			break;
		case ']':
			ebcdic = "BD";
			break;
		case '{':
			ebcdic = "C0";
			break;
		case 'A':
			ebcdic = "C1";
			break;
		case 'B':
			ebcdic = "C2";
			break;
		case 'C':
			ebcdic = "C3";
			break;
		case 'D':
			ebcdic = "C4";
			break;
		case 'E':
			ebcdic = "C5";
			break;
		case 'F':
			ebcdic = "C6";
			break;
		case 'G':
			ebcdic = "C7";
			break;
		case 'H':
			ebcdic = "C8";
			break;
		case 'I':
			ebcdic = "C9";
			break;

		case '}':
			ebcdic = "D0";
			break;
		case 'J':
			ebcdic = "D1";
			break;
		case 'K':
			ebcdic = "D2";
			break;
		case 'L':
			ebcdic = "D3";
			break;
		case 'M':
			ebcdic = "D4";
			break;
		case 'N':
			ebcdic = "D5";
			break;
		case 'O':
			ebcdic = "D6";
			break;
		case 'P':
			ebcdic = "D7";
			break;
		case 'Q':
			ebcdic = "D8";
			break;
		case 'R':
			ebcdic = "D9";
			break;
		case '\\':
			ebcdic = "E0";
			break;
		case 'S':
			ebcdic = "E2";
			break;
		case 'T':
			ebcdic = "E3";
			break;
		case 'U':
			ebcdic = "E4";
			break;
		case 'V':
			ebcdic = "E5";
			break;
		case 'W':
			ebcdic = "E6";
			break;
		case 'X':
			ebcdic = "E7";
			break;
		case 'Y':
			ebcdic = "E8";
			break;
		case 'Z':
			ebcdic = "E9";
			break;

		case '0':
			ebcdic = "F0";
			break;
		case '1':
			ebcdic = "F1";
			break;
		case '2':
			ebcdic = "F2";
			break;
		case '3':
			ebcdic = "F3";
			break;
		case '4':
			ebcdic = "F4";
			break;
		case '5':
			ebcdic = "F5";
			break;
		case '6':
			ebcdic = "F6";
			break;
		case '7':
			ebcdic = "F7";
			break;
		case '8':
			ebcdic = "F8";
			break;
		case '9':
			ebcdic = "F9";
			break;

		default:
			ebcdic = "40";
			break;
		}
		return ebcdic;
	}

	public static String convertEBCDICHexValue(String ebcdicHexValue) {
		int j = 0;
		StringBuilder data = new StringBuilder();
		for (int i = 0; i < ebcdicHexValue.length() / 2; i++) {
			String hex = ebcdicHexValue.substring(j, j + 2);
			j = j + 2;
			char c = (char) Integer.parseInt(hex, 16);
			data.append(ebcdicOfHex(c));

		}
		return data.toString();
	}

	public static String convertToEBSDICHEXValue(String alphaNumricString) {
		StringBuilder data = new StringBuilder();
		for (char c : alphaNumricString.toCharArray()) {
			data.append(ebcdicToHex(c));
		}
		return data.toString();
	}

	public static String paddingASCIIZero(String data, int expectedLength) {
		if (data.length() == expectedLength) {
			return data;
		} else
			for (int i = data.length(); i < (expectedLength); i += 2) {
				data += "30";

			}
		log.debug("Padded value :: " + data);
		return data.toString();
	}

	private static String hexToASCII(String hexValue)
	{
		StringBuilder output = new StringBuilder("");
		for (int i = 0; i < hexValue.length(); i += 2)
		{
			String str = hexValue.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}
		return output.toString();
	}

	public static void main(String[] args) throws IOException {

		String t = "016000001601020160000000000000000000000000000000000002007EFD669128E0FA1610403456789012345601100000000000400000000002500000000002500005041322406100000061000000000444184802050403090000601108400510000100C4F0F0F0F0F0F0F0F00C012345678901204034567890123456D030912312345000F2F1F2F4F1F3F0F0F0F4F4F4C1E3D4F0F1404040C3C1D9C440C1C3C3C5D7E3D6D94040C1C3D8E4C9D9C5D940D5C1D4C5404040404040404040404040C3C9E3E840D5C1D4C540404040E4E208400840084062FAF4E84B13D45D2001010100000000670100649F3303204000950500000400009F37049BADBCAB9F100C0B010A03A0B00000000000009F26080123456789ABCDEF9F360200FF820200009C01019F1A0208409A030101019F02060000000123005F2A0208409F03060000000000008407A00000000310100425000010098000000000000000E8068020000002F0";
		Charset charset = StandardCharsets.ISO_8859_1;
		System.out.println(ISOUtil.hexdump(
				Convertor.hexToASCII(t).getBytes(charset)));


		String msg = "{\"requestId\":\"abc1c598-c643-41fe-8eea-13eda587b2ce\",\"mti\":\"0200\",\"proxyCardNumber\":\"4034567890123456\",\"tpCode\":\"011000\",\"transactionAmount\":\"000000004000\",\"settlementAmount\":\"000000025000\",\"billingAmount\":\"000000025000\",\"billingFeeAmount\":\"0\",\"transactionFeeAmount\":\"00000000\",\"settlementFeeAmount\":\"0\",\"transactionProcessingFeeAmount\":\"0\",\"settlementProcessingFeeAmount\":\"0\",\"settlementConversionRate\":\"61000000\",\"billingConversionRate\":\"61000000\",\"transactionDateTime\":\"2022-04-30 17:22:29\",\"stan\":\"000313\",\"localTime\":\"17:22:30\",\"localDate\":\"2030-04-01\",\"expiryDate\":\"2009-03-01\",\"settlementDate\":\"2022-04-30\",\"conversionDate\":\"2022-04-30\",\"captureDate\":\"2022-04-30\",\"mcc\":\"6011\",\"acqInstCountryCode\":\"0840\",\"fwdInstCountryCode\":\"0840\",\"posEntryMode\":\"0510\",\"cardSeqNum\":\"0001\",\"posConditionCode\":\"00\",\"posPinCaptureCode\":\"-\",\"authIdResp\":\"-\",\"acquirerBin\":\"12345678901\",\"forwardingInstIdCode\":\"-\",\"track2Data\":\"4034567890123456D030912312345000\",\"rrn\":\"212011000313\",\"cardAcceptorTerminalid\":\"ATM01   \",\"cardAcceptorId\":\"CARD ACCEPTOR  \",\"cardAcceptorLocation\":\"ACQUIRER NAME            CITY NAME    US\",\"transactionCurrencyCode\":\"0840\",\"settlementCurrencyCode\":\"0840\",\"billingCurrencyCode\":\"0840\",\"pinData\":\"nullnull4Y.<DC3>M)\",\"securityRelatedControlInformation\":\"2001010100000000\",\"acqNetworkId\":\"VISA\",\"acqInstitutionId\":\"12345678901\",\"channelType\":\"ATM\",\"transactionType\":\"01\",\"issuerBin\":\"403456\",\"securityInfo\":\"YYYYYY\",\"acquirerId\":\"VISA\",\"apiRefNo\":\"5fedd9d4-9b29-46ab-8f0a-f2b6da6d205f\",\"reserverFld1\":\"000016010201600000000000000000000000000000000000\",\"replyTopic\":\"tcpiprecieve\",\"maskedPan\":\"xxxx-xxxx-xxxx-3456\",\"hashedPan\":\"F4F0F3F4F5F6F7F8F9F0F1F2F3F4F5F6\",\"incrementalTransactionCount\":0,\"contactlessTransaction\":0}";

		TransactionRequestDto transactionRequestDto = null;
			transactionRequestDto = new ObjectMapper()
					.findAndRegisterModules()
					.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
					.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
					.readValue(msg, TransactionRequestDto.class);

			System.out.println("Check->"+transactionRequestDto.getAcquirerBin());

			System.out.println("1:"+Convertor.convertEBCDICHexValue("95A4939395A49393F4E84B4CC4C3F36ED45D"));
			System.out.println("2:"+Convertor.convertEBCDICHexValue("62FAF4E84B13D45D"));
			System.out.println("3:"+Convertor.convertToEBSDICHEXValue("nullnull4Y.<DC3>M)"));


		StringBuilder strVal = new StringBuilder("012345678901");
		int i = 0;
		for (; i < strVal.length(); i++) {
			if (strVal.charAt(i) != '0') {
				break;
			}
		}

		System.out.println(strVal.substring(i)+"]");

	}

	public static boolean isHexValue(String data) {
		Pattern pattern = Pattern.compile("[0-9a-fA-F]+");
		Matcher match = pattern.matcher(data);
		if (match.matches())
			return true;
		else
			return false;
	}

	public static LocalTime getLocalTimeForString(final String string) {
		String PATTERN_TIME = "HHmmss";
		return LocalTime.parse(string,
				DateTimeFormatter.ofPattern(PATTERN_TIME));
	}
	public static LocalDateTime getLocalDateTimeForString(final String string) {
		String PATTERN_TIME = "HHmmss";
		return LocalDateTime.parse(string,
				DateTimeFormatter.ofPattern(PATTERN_TIME));
	}
}
