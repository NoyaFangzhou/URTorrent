package URTorrent;

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
		try {
			String id = URDataOperator.SEncode(("CSC457-HFZ-"+port));
			System.out.println("Peer ID for port:"+port+" is "+ id);
			System.out.println("Peer ID lenght: "+ id.length());
			return id;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
