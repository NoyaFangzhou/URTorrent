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
	
	public static int BLOCK_SIZE = 16384; // Default block size
	
	public static int PIECE_SIZE = 262144;// Default piece size (256K bytes)
	
	public static String UTF_8 = "UTF-8";
	
	public static String SHA1 = "SHA1";
	
	public static int SHA1_LENGTH = 20; // Default length of SHA1 for each piece
	
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
	 * Message Macro
	 */
	//Handshake part
	public static int HSK_PSTRLEN = 18;
	
	public static String HSK_PSTR = "URTorrent protocol";
	
	public static String HSK_RESERVE = "00000000";
	
	//Request part
	public static int REQ_LEN = 13;
	
	//Have part
	public static int HAVE_LEN = 5;
	
	//Cancel part
	public static int CANCEL_LEN = 13;
	
	/**
	 * Message ID Macro
	 */
	public static int CHOKE_ID = 0;
	
	public static int UNCHOKE_ID = 1;
	
	public static int INTEREST_ID = 2;
	
	public static int NOTINTEREST_ID = 3;
	
	public static int HAVE_ID = 4;
	
	public static int BITFIELD_ID = 5;
	
	public static int REQUEST_ID = 6;
	
	public static int PIECE_ID = 7;
	
	public static int CANCEL_ID = 8;
	
	/**
	 * Peer Status
	 */
	//the client is choking the peer
	//its message will be ignored by the peer
	public static int AM_CHOKING = 1;
		
	//the client is interested in the peer
	//its message will be receive and handled b the peer
	public static int AM_INTERESTED = 1;
		
	//the peer is choking this client
	public static int PEER_CHOKING = 1;
		
	//the peer is interested in this client
	public static int PEER_INTERESTED = 1;
	

}
