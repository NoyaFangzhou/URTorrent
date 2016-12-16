package URTorrent;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Generate the ID for Peers to do Peer communication
 * Runs once when peer begin to work
 * @author Fangzhou_Liu
 *
 */
public class URPeerIDGenerator {
	/**
	 * Generate the ID for each peers
	 * This ID is identical for each peer.
	 * @return The String format peer id for the Peer that call this method
	 */
	public static String getPeerID(String port) {
//		try {
//			String id = URDataOperator.SEncode(("CSC457-HFZ-Peer-"+port));
			String id = "CSC457-HFZ-Peer-"+port;
			System.out.println("Peer ID for port:"+port+" is "+ id);
			System.out.println("Peer ID lenght: "+ id.length());
			return id;
//		} catch (NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
	}
	
//	public static byte[] getPeerID(String port) {
//		byte[] id = ("CSC453-HFZ-"+port).getBytes();
//		// Try to generate the info hash value
//		try {
//			MessageDigest digest = MessageDigest.getInstance("SHA-1");
//			digest.update(id);
//			byte[] id_hash = digest.digest();
//			System.out.println("id length" + id_hash.length);
//			return id_hash;
//		}
//		catch(NoSuchAlgorithmException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}

}
