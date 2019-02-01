package in.unpluggedmind.commands;

import in.unpluggedmind.KeyTypes;
import in.unpluggedmind.model.Keys;

public class CmdGenKey {

	static String command = null;
	
	public static String generate(KeyTypes keyType, Keys keys) {
		String k = keyType.toString();
		System.out.println(k);
		
		switch(k)
		{
		case "ZMK" :
		case "ZPK" :
		case "ZPKzmk" :
		case "PVK" :
		case "PVKzmk" :
			GenA0(keyType, keys);
			break;
		case "CVK" :
			GenAS(keyType);
			break;
		default : 
			break;
		}
		return command;
	}
	

	/**
	 * Generate a Key
	 * A0
	 * Mode [0-Gen Key, 1-Gen key under ZMK/TMK]
	 * Key Type [000-ZMK, 001-ZPK, 002-PVK]
	 * Key Scheme(LMK): [U-Double Length]
	 * ZMK (encrypted under LMK pair 04-05)
	 * Key Scheme(ZMK): [U-Double Length, T-Triple Length]
	 */	
	private static String GenA0(KeyTypes keyType, Keys keys) {
		switch(keyType.toString())
		{
		case "ZMK" :
			command = "A0"
					+ "0"
					+ "000"
					+ "U";			
			break;
		case "ZPK" :
			command = "A0"
					+ "0"
					+ "001"
					+ "U";
			break;
		case "ZPKzmk" :
			// ZPK under ZMK
			command = "A0"
					+ "1"
					+ "001"
					+ "U"
					+ keys.getZMK_LMK() // "UFE2D3CA8621D70D050DB81CED4AD3A0B"
					+ "U";
			break;
		case "PVK" :	
			break;
		case "PVKzmk" :	
			// PVK(TPK?) under ZMK 
			command = "A0"
					+ "1"
					+ "002"
					+ "U"
					+ keys.getZMK_LMK() //"UFE2D3CA8621D70D050DB81CED4AD3A0B"
					+ "U";
			break;
		default :
			break;
		}
		
		return command;
	}
	
	/**
	 * Generate a CVK Pair
	 * AS
	 * Delimiter: [;]
	 * Reserved (Optional): [0]
	 * Key Scheme(LMK): [U-Double Length]
	 * Key Check Value Type (Optional): [0- 16 digit,backward compatible, 1- 16 digit]
	 */
	private static String GenAS(KeyTypes keyType) {
		
		switch(keyType.toString())
		{
		case "CVK" :
			command = "AS"
					+ ";"
					+ "0"
					+ "U"
					+ "1";
			break;
		default :
			break;
		}
		
		return command;
	}
	
}
