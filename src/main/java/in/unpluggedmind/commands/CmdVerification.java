package in.unpluggedmind.commands;

public class CmdVerification {
	
	public static String verify(String v) {
		System.out.println(v);
		
		switch(v)
		{
		case "CVV" :
			return verifyCVV();
		case "PVV" :
			return verifyPVV();
		case "CHAIN" : 
			return commandChain();
		default : 
			System.out.println("!!! Invalid Request !!!");
		}
			
		return null;
	}

	/**
	 * Verify CVV
	 * CY
	 * CVK
	 * CVV
	 * PAN/Account
	 * Delimiter: [;]
	 * Expiry (YYMM)
	 * Service Code								
	*/
	private static String verifyCVV() {
		return "CY"
				+ "U8F3CE2A39000981D4F080FC68469DB1D"
				+ "532"
				+ "4000000000000002"
				+ ";"
				+ "2012"
				+ "101";
	}

	/**
	 * Verify a PIN(interchange) - VISA PVV method
	 * EC
	 * ZPK
	 * PVK
	 * PIN Block
	 * PIN Block Format Code
	 * Account (12)
	 * PVKI
	 * PVV
	 */
	private static String verifyPVV() {
		return "EC"
				+ "U5D836C6EB367308DF59729139A04E017"
				+ "U1661EAF0B01F534C3A23292A67818C91"
				+ "CBA110B75A5D592E" 
				+ "01"
				+ "000000000000"
				+ "1"
				+ "9670";
	}
	
	/**
	 * Command Chaining
	 * NK
	 * 0
	 * 02
	 * 0062
	 * CYU8F3CE2A39000981D4F080FC68469DB1D5324000000000000002;2012101
	 * 0103
	 * ECU951E300CD42EF71EF1C70EA36392EC5CU81E0C5387FD156F911DF25D95024806B3BD7785B1B15A15C0100000000000010498
	 *	
	 * NL
	 * 00
	 * 02
	 * 0004
	 * CZ00
	 * 0004
	 * ED00
	 */
	private static String commandChain() {
		return "NK"
				+ "0"
				+ "02"
				+ "0062"
					+ "CY"
					+ "U8F3CE2A39000981D4F080FC68469DB1D"
					+ "532"
					+ "4000000000000002"
					+ ";"
					+ "2012"
					+ "101"
				+ "0103"
					+ "EC"
					+ "U951E300CD42EF71EF1C70EA36392EC5C"
					+ "U81E0C5387FD156F911DF25D95024806B"
					+ "3BD7785B1B15A15C01"
					+ "000000000000"
					+ "1"
					+ "0498";
	}			
	
}
