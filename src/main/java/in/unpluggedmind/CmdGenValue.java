package in.unpluggedmind;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.jpos.security.SMException;

public class CmdGenValue {

	public static String calculate(CryptValues val) 
			throws InvalidKeyException, SMException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		String v = val.toString();
		System.out.println(v);
		
		switch(v)
		{
		case "CVV" :
			return calculateCVV();
		case "PVV" :
			return calculatePVV();
		case "PVVcust" :
			return calculatePVVcust();
		case "PINLMK" :
			return translatePINLMK();
		case "PINBlock" :
			return calculatePINBlock();
		case "EncPIN" :
			return calculateEncPIN();
		default : 
			System.out.println("!!! Invalid Request !!!");
		}
			
		return null;
	}
	
	/**
	 * Calculate CVV
	 * CW
	 * CVK
	 * PAN/Account
	 * Delimiter: [;]
	 * Expiry (YYMM)
	 * Service Code
	 */
	private static String calculateCVV() {
		return "CW"
				+ "U8F3CE2A39000981D4F080FC68469DB1D"
				+ "4000000000000002"
				+ ";"
				+ "2012"
				+ "101";
	}
	
	/**
	 * Calculate Visa PVV (LMK encrypted PIN)
	 * DG
	 * PVK (under LMK)
	 * PIN (under LMK)
	 * Account (12)
	 * PVKI				
	 */
	private static String calculatePVV() {
		return "DG"
				+ "U1661EAF0B01F534C3A23292A67818C91"
				+ "01234"
				+ "000000000000"
				+ "1;";	
	}


	/* !!! NOT SUPPORTED by SIMULATOR !!! */
	/**
	 * Calculate Visa PVV (Customer selected PIN)
	 * FW
	 * PIN Block Key Type: [001-ZPK, 002-TPK]
	 * PIN Block Key: ZPK or TPK
	 * PVK
	 * PIN Block
	 * PIN Block Format Code
	 * Account (12)
	 * PVKI
	 */
	private static String calculatePVVcust() {
		return "FW"
			+ "001"
			+ "UD25B25E64AFD3626E9D9DCCDB49CFE9E" //"U951E300CD42EF71EF1C70EA36392EC5C"
			+ "U81E0C5387FD156F911DF25D95024806B"
			+ "CBA110B75A5D592E" 
			+ "01"
			+ "000000000000"
			+ "1";
	}
	
	/**
	 * Encrypt clear PIN under LMK
	 * BA
	 * Clear Pin (Length+Pin)
	 * PAN/Account (12) 
	 */ 
	private static String calculateEncPIN() {
		return "BA"
				+ "41234"
				+ "000000000000";
	}
	
	/**
	 * Translate PIN from ZPK to LMK
	 * JE
	 * Source ZPK (under LMK)
	 * PIN Block
	 * PIN Block Format code
	 * Account (12)
	 */
	private static String translatePINLMK() {
		return "JE"					
				+ "U5D836C6EB367308DF59729139A04E017"
				+ "CBA110B75A5D592E"
				+ "01"
				+ "000000000000";

	}
		
	/**
	 * Calculate PIN Block (under ZPK) (Format ISO-0)
	 * PIN 
	 * PAN/Account (12)
	 * ZPK
	 * PIN Block Filler Character: [0 or F]		
	 *
	 * PIN = 1234
	 * Block 1 [0 + Pin Length + Filler to make it 16]: 0 + 4 + 1234 + FFFFFFFFFF = 041234FFFFFFFFFF
	 * Block 2 [0000 + PAN(12)]: 0000 + 000000000000 = 0000000000000000
	 * Clear PIN Block: XOR(Block 1 , Block 2)
	 * Encrypted PIN Block: Apply 3DES with ZPK
	 */
	private static String calculatePINBlock() 
			throws InvalidKeyException, SMException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		System.out.println("****** CALCULATE PIN BLOCK ******");
		String pin = "1234";
		byte format = 0x1;
		String accountNum = "000000000000";
		String ZpkClear = "AEE60419E6B6011C80A149D3B94FABBF";
					
		GenPINBlock.calculateEncPINBlock(pin, format, accountNum, ZpkClear);
		return "Check logs for PIN Block";
	}
					
	
				
	}
