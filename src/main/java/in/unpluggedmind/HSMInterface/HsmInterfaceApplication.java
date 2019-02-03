package in.unpluggedmind.HSMInterface;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.jpos.security.SMException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("in.unpluggedmind")
public class HsmInterfaceApplication {
	
	private static ApplicationContext context;

	public static void main(String[] args) throws InvalidKeyException, UnknownHostException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IOException, SMException {
		context = SpringApplication.run(HsmInterfaceApplication.class, args);
		System.out.println("HSM Interface Initialization");
		HsmInterfaceInitialization hsmInit = context.getBean(HsmInterfaceInitialization.class);
		hsmInit.init();
	}
	
	public static ApplicationContext getContext() {
		return context;
	}

}

