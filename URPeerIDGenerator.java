package URTorrent;
/**
 * Generate the ID for Peers to do Peer communication
 * Runs once when peer begin to work
 * @author Fangzhou_Liu
 *
 */
public class URPeerIDGenerator {
	private volatile static int id = 0;
	/**
	 * Generate the ID for each peers
	 * This ID is identical for each peer.
	 * @return The String format peer id for the Peer that call this method
	 */
	public static String getPeerID() {
		return "CSC457-HFZ-"+(id++);
	}

}
