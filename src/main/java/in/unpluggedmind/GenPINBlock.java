package in.unpluggedmind;

import org.jpos.iso.ISOUtil;
import org.jpos.security.SMAdapter;
import org.jpos.security.SMException;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Generate PIN Block
 * Provide 
 * - PAN/Account Number (12 digit, exclude first 3 and last check digit)
 * - PIN Block Format
 * - Clear PIN
 * - Clear PEK (PIN Encryption Key)
 */
public class GenPINBlock {
	
	// a 64-bit block of ones used when calculating pin blocks
    private static final byte[] fPaddingBlock = ISOUtil.hex2byte("FFFFFFFFFFFFFFFF");
    
    public static void calculateEncPINBlock(String pin, byte format, String accountNum, String Zpk) 
    		throws SMException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
    		    	
    	// Calculate Clear PIN Block
        byte[] clearPinblockFormat = calculateClearPINBlock(pin, format, accountNum);
        
        // Apply 3DES encryption on Clear PIN Block (PEK = Clear ZPK)
        desPIN(clearPinblockFormat, Zpk);
        
    }
    
    public static byte[] calculateClearPINBlock (String pin, byte pinBlockFormat, String accountNumber) {
    	System.out.println("Calculate CLEAR PIN Block");
    	byte[] pinBlockClear = null;

    	// Block 1
    	byte[] block1 = ISOUtil.hex2byte(new String(formatPINBlock(pin, 0x0)));
    	
    	// Block 2
    	byte[] block2 = ISOUtil.hex2byte("0000" + accountNumber);
        
    	// Clear PIN Block
        pinBlockClear = ISOUtil.xor(block1, block2);
        
        System.out.println("pinBlockClear : " + pinBlockClear);
        return pinBlockClear;
    }
    
    private static char[] formatPINBlock(String pin, int checkDigit){
    	System.out.println("Format PIN Block");
    	
        char[] block = ISOUtil.hexString(fPaddingBlock).toCharArray();
        
        char[] pinLenHex = String.format("%02X", pin.length()).toCharArray();
        pinLenHex[0] = (char)('0' + checkDigit);

        // pin length then pad with 'F'
        System.arraycopy(pinLenHex, 0, block, 0, pinLenHex.length);
        System.arraycopy(pin.toCharArray(), 0, block, pinLenHex.length, pin.length());
        
        System.out.println("Formatted PIN Block :" + block);
        return block;
      }
    
    public static void desPIN(byte[] clearPinblockFormat, String Zpk) 
    		throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
    	
    	/* Split provided Zpk into 2 parts.
         * Trick JCE by making a double length key into a triple length, 
         * this way the key doesn't change due to the way the key parts 
         * are encrypted and decrypted fr 3DES
         */
        String	ZPK_PART1 = Zpk.substring(0, Zpk.length()/2);
        String	ZPK_PART2 = Zpk.substring(Zpk.length()/2, Zpk.length());
        final String ZPK  = ZPK_PART1 + ZPK_PART2 + ZPK_PART1;
        System.out.println("ZPK: " + ZPK);
        
        // Encrypt the pinblock with the clear ZPK
        Cipher desCipher = Cipher.getInstance("DESede/ECB/NoPadding");
        SecretKeySpec zpk = new SecretKeySpec(ISOUtil.hex2byte(ZPK), "DESede");
        desCipher.init(Cipher.ENCRYPT_MODE, zpk);
        String encryptedPinBlock = ISOUtil.hexString(desCipher.doFinal(clearPinblockFormat));
        System.out.println("Encrypted Pin Block :" + encryptedPinBlock);        
    }
    

}