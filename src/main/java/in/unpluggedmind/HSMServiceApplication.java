package in.unpluggedmind;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.jpos.security.SMException;

/**
 * Main Class 
 */
public class HSMServiceApplication {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws InvalidKeySpecException 
	 * @throws SMException 
	 * @throws InvalidAlgorithmParameterException 
	 */
	public static void main(String args[]) 
			throws UnknownHostException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, SMException, InvalidKeySpecException, InvalidAlgorithmParameterException {
		
		System.out.println("HSM Interface Initializing...");
		
		String command = null;
		String iCommand = null;
		String header = null;
		
		Socket socket = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		byte[] b = new byte[100];

		socket = new Socket("127.0.0.1", 9998);
		System.out.println("Connecting to " + socket);

		if (socket != null) {
			System.out.println("HSM Connection Status :" + socket.isConnected());
		
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
			/* ********************************************************************************************** */
			iCommand = CmdGenKey.generate(KeyTypes.ZMK);
			//iCommand = CmdGenKey.generate(KeyTypes.ZPK);
			//iCommand = CmdGenKey.generate(KeyTypes.ZPKzmk);
			//iCommand = CmdGenKey.generate(KeyTypes.PVK);
			//iCommand = CmdGenKey.generate(KeyTypes.PVKzmk);
			//iCommand = CmdGenKey.generate(KeyTypes.CVK);
			
			//iCommand = CmdGenValue.calculate(CryptValues.CVV);
			//iCommand = CmdGenValue.calculate(CryptValues.PVV);
			//iCommand = CmdGenValue.calculate(CryptValues.PINBlock);
			//iCommand = CmdGenValue.calculate(CryptValues.TranPINLMK);
			//iCommand = CmdGenValue.calculate(CryptValues.EncPINLMK);
			//iCommand = CmdGenValue.calculate(CryptValues.PVVLMK);
			//iCommand = CmdGenValue.calculate(CryptValues.PVVCust);
			
			//iCommand = CmdVerification.verify("CVV");
			//iCommand = CmdVerification.verify("PVV");
			//iCommand = CmdVerification.verify("CHAIN");
			/* ********************************************************************************************** */

			header = "0000"; //Thales Simulator
			if (iCommand != null) {
				command = header + iCommand;	
			}else {
				System.out.println("Invalid command. Aborted !!! ");
			}
				
			out.writeUTF(command);
			System.out.println("Input to HSM : " + command);
			out.flush();

			String response = in.readUTF();
			System.out.println("Output from HSM : " + response);
			System.out.println("");
			
		}
		

	}

}
