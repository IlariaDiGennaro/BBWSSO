package main;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.util.encoders.Hex;

public class Hasher {

	public static void main(String[] args) {
		if(args == null || args.length==0) {
			System.out.println("password and algorithm mandatory");
			return;
		}
		
		if(args.length < 1 || args[0] == null || "".equals(args[0])) {
			System.out.println("password mandatory");
			return;
		}
		
		if(args.length < 2 || args[1] == null || "".equals(args[1])) {
			System.out.println("algorithm mandatory");
			return;
		}
		
		String hashedString = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(args[1]);
			byte[] hashBytes = messageDigest.digest(args[0].getBytes(StandardCharsets.UTF_8));
			byte [] hashedBytes = Hex.encode(hashBytes);
			hashedString = new String(hashedBytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return;
		}
		
		System.out.println("password ["+args[0]+"] hashed in ["+hashedString+" , length="+hashedString.length()+"] with algorithm ["+args[1]+"]");
	}

}
