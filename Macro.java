package URTorrent;

/**
 * Store all constant parameter and values used in this project
 * @author Fangzhou_Liu
 *
 */
public class Macro {
	/**
	 * General Macro
	 */
	public static String LOCALHOST = "127.0.0.1"; //IP Address of the localhost
	
	public static int TRACKER_PORT = 6969; //Port # of the Tracker
	
	public static int BLOCK_SIZE = 16384; // Default block size
	
	public static int PIECE_SIZE = 262144;// Default piece size (256K bytes)
	
	public static String UTF_8 = "UTF-8";
	
	public static String SHA1 = "SHA1";
	
	public static int SHA1_LENGTH = 20; // Default length of SHA1 for each piece
	
	public static int MAX_PEERS = 10; // maximum number of peers that can support;
	
	//Handshake 
	//{pstrlen(18)} {pstr(URTorrent protocol)} {reserved(8 byte 0s)} {info_hash} {peer_id}
	
	//Bitfield
	//{1+B(the length of the Bitfield)} {5} {Bitfield}
	
	//Request
	//{13} {6} {(index)(begin)(length)}
	
	//Piece
	//{9+L(Length of the block)} {7} {(index)(begin)(block)}
	
	//Have
	//{5} {4} {(piece index)}
	
	//Cancel
	//{13} {8} {(index)(begin)(length)}
	
	/**
	 * Role of each peers
	 */
	public static int LEECHER = 1;
	
	public static int SEEDER = 0;
	
	
	/**
	 * Test Data
	 */
	public static String TEST_PORT = "6000";
	
	public static String TEST_URL = LOCALHOST;
	

}
