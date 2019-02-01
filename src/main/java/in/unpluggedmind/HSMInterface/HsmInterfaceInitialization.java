package in.unpluggedmind.HSMInterface;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import in.unpluggedmind.CryptValues;
import in.unpluggedmind.commands.CmdGenValue;
import in.unpluggedmind.model.Keys;

@Component
public class HsmInterfaceInitialization {
	
	@Autowired
	Environment env;
	
	@Autowired
	Keys keys;
		
	private final String ZMK_CLR = "ZMK_CLR";
	private final String ZMK_LMK = "ZMK_LMK";
	private final String TMK_ZMK = "TMK_ZMK";
	private final String TMK_LMK = "TMK_LMK";
	private final String ZPK_CLR = "ZPK_CLR";
	private final String ZPK_ZMK = "ZPK_ZMK";
	private final String ZPK_LMK = "ZPK_LMK";
	private final String TPK_CLR = "TPK_CLR";
	private final String TPK_TMK = "TPK_TMK";
	private final String TPK_LMK = "TPK_LMK";
	private final String PVK_CLR = "PVK_CLR";
	private final String PVK_LMK = "PVK_LMK";
	private final String CVK_CLR = "CVK_CLR";
	private final String CVK_LMK = "CVK_LMK";
	
	public void init() throws InvalidKeyException, UnknownHostException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IOException, SMException {
		System.out.println("HSM Interface "+ getClass().getName() +" Initialization");
		getKeys(keys);
		process();	
	}
	
	public void getKeys(Keys k) {
		
		k.setZMK_CLR(env.getProperty(ZMK_CLR));
		k.setZMK_LMK(env.getProperty(ZMK_LMK));
		k.setTMK_ZMK(env.getProperty(TMK_ZMK));
		k.setTMK_LMK(env.getProperty(TMK_LMK));
		k.setZPK_CLR(env.getProperty(ZPK_CLR));
		k.setZPK_ZMK(env.getProperty(ZPK_ZMK));
		k.setZPK_LMK(env.getProperty(ZPK_LMK));
		k.setTPK_CLR(env.getProperty(TPK_CLR));
		k.setTPK_TMK(env.getProperty(TPK_TMK));
		k.setTPK_LMK(env.getProperty(TPK_LMK));
		k.setPVK_CLR(env.getProperty(PVK_CLR));
		k.setPVK_LMK(env.getProperty(PVK_LMK));
		k.setCVK_CLR(env.getProperty(CVK_CLR));
		k.setCVK_LMK(env.getProperty(CVK_LMK));
	}
	
	public void process() throws UnknownHostException, IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, SMException {
		System.out.println("HSM Interface Initiated");
		
		String command = null;
		String iCommand = null;
		String header = null;
		
		Socket socket = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		//byte[] b = new byte[100];

		socket = new Socket("127.0.0.1", 9998);
		System.out.println("Connecting to " + socket);

		if (socket != null) {
			System.out.println("HSM Connection Status :" + socket.isConnected());
		
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			
			/* ************************************************************************* */
			 
			//iCommand = CmdGenKey.generate(KeyTypes.ZMK, keys);
			//iCommand = CmdGenKey.generate(KeyTypes.ZPK);
			//iCommand = CmdGenKey.generate(KeyTypes.ZPKzmk);
			//iCommand = CmdGenKey.generate(KeyTypes.PVK);
			//iCommand = CmdGenKey.generate(KeyTypes.PVKzmk);
			//iCommand = CmdGenKey.generate(KeyTypes.CVK);
			
			//iCommand = CmdGenValue.calculate(CryptValues.CVV, keys);
			//iCommand = CmdGenValue.calculate(CryptValues.PVV, keys);
			iCommand = CmdGenValue.calculate(CryptValues.PINBlock, keys);
			//iCommand = CmdGenValue.calculate(CryptValues.TranPINLMK, keys);
			//iCommand = CmdGenValue.calculate(CryptValues.EncPINLMK, keys);
			//iCommand = CmdGenValue.calculate(CryptValues.PVVLMK, keys);
			//iCommand = CmdGenValue.calculate(CryptValues.PVVCust, keys);
			
			//iCommand = CmdVerification.verify("CVV");
			//iCommand = CmdVerification.verify("PVV");
			//iCommand = CmdVerification.verify("CHAIN");
			/* ************************************************************************* */

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
