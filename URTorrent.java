package URTorrent;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Scanner;

/**
 * Main running program
 * Launch the URTorrent Peer Client and type in the command
 * @author Fangzhou_Liu
 *
 */
public class URTorrent {
//	public static void main (String [] args) {
//		System.out.println("-------------------------------------");
//		System.out.println("Metainfo File Name: " + args[0]);
//		System.out.println("Port Number: " + args[1]);
//		System.out.println("-------------------------------------");
//		System.out.println("\nURTorrent Launching ..... \n");
//		launch();
//	}
	
	/**
	 * Start the URTorrent Client
	 * Take the user input as command, do operations according to the command
	 */
	public static void launch() {
		
		while(true) {
			Scanner scan= new Scanner(System.in);
			System.out.print("<urtorrent>");
			//Get the input command
			String text= scan.nextLine();
			System.out.println(text);
		}
		
	}
	
	private static String encryptPassword(String password)
	{
	    String sha1 = "";
	    try
	    {
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(password.getBytes("UTF-8"));
	        System.out.println(password.getBytes("UTF-8"));
	        System.out.println(crypt.digest());
	        sha1 = byteToHex(crypt.digest());
	    }
	    catch(NoSuchAlgorithmException e)
	    {
	        e.printStackTrace();
	    }
	    catch(UnsupportedEncodingException e)
	    {
	        e.printStackTrace();
	    }
	    return sha1;
	}

	private static String byteToHex(final byte[] hash) {
	    Formatter formatter = new Formatter();
	    for (byte b : hash) {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
}
