package money.open.cards.visabase.Utility;

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;

@Slf4j
public class LogUtils {
    
    
    public static void writeRequestLog(String msgStatus,Hashtable<String, String> isoBuffer)
    {
        //Log Request Here
        for(int HE=1;HE<=12;HE++)
        {
            log.info("<"+msgStatus+">"+"H"+ String.format("%02d",HE) +"="+isoBuffer.get("H"+String.format("%02d",HE)));
        }
        log.info("<"+msgStatus+">"+"MTI"+"="+isoBuffer.get("MTI"));


        log.info("<"+msgStatus+">"+"pBMP"+"="+isoBuffer.get("pBMP"));
        log.info("<"+msgStatus+">"+"sBMP"+"="+isoBuffer.get("sBMP"));

        for(int DE=1;DE<=128;DE++)
        {	if(!isoBuffer.get("DE"+String.format("%03d",DE)).equals("*"))
            log.info("<"+msgStatus+">"+"DE"+ String.format("%03d",DE) +"="+isoBuffer.get("DE"+String.format("%03d",DE)));
        }


    }

    public static void writeHexDump(String messageStatus, byte[] bytes) {
        log.info(messageStatus+"->\n"+
                ISOUtil.hexdump(bytes));
        log.info("->"+messageStatus);
    }

    public static void main(String[] args) {
        String tmp="016000001601020160000000000000000000000000000000000002007EFD669128E0FA1610403456789012345601100000000000400000000002500000000002500005041225156100000061000000000438175455050403090000601108400510000100C4F0F0F0F0F0F0F0F00C012345678901204034567890123456D030912312345000F2F1F2F4F1F2F0F0F0F4F3F8C1E3D4F0F1404040C3C1D9C440C1C3C3C5D7E3D6D94040C1C3D8E4C9D9C5D940D5C1D4C5404040404040404040404040C3C9E3E840D5C1D4C540404040E4E208400840084062FAF4E84B13D45D2001010100000000670100649F3303204000950500000400009F37049BADBCAB9F100C0B010A03A0B00000000000009F26080123456789ABCDEF9F360200FF820200009C01019F1A0208409A030101019F02060000000123005F2A0208409F03060000000000008407A00000000310100425000010098000000000000000E8068020000002F0";
        LogUtils.writeHexDump("TEST",tmp.getBytes());
    }
}
