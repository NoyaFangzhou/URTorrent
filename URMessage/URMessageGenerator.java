/**
 * 
 */
package URTorrent.URMessage;

import URTorrent.Macro;

/**
 *
 * @File URMessageGenerator.java
 * @Time 2016-12-15 12:53:59 北美东部标准时间
 * @Author Fangzhou_Liu 
 * @Description: 
 */
public class URMessageGenerator {
	
	/** The Constant keepAliveID. */
	public static final byte keepAliveID = -1;
	
	/** The Constant chockID. */
	public static final byte chockID = 0;
	
	/** The Constant unchockID. */
	public static final byte unchockID = 1;
	
	/** The Constant interestedID. */
	public static final byte interestedID = 2;
	
	/** The Constant uninterestedID. */
	public static final byte uninterestedID = 3;
	
	/** The Constant haveID. */
	public static final byte haveID = 4;
	
	/** The Constant bitfieldID. */
	public static final byte bitfieldID = 5;
	
	/** The Constant requestID. */
	public static final byte requestID = 6;
	
	/** The Constant pieceID. */
	public static final byte pieceID = 7;
	
	/** The Constant cancelID. */
	public static final byte cancelID = 8;
	
	
	/**
	 * Generate handshake.
	 * 
	 * Generate a handshake on our end to send to the peer
	 *
	 * @param peer the peer
	 * @param infohash the infohash
	 * @return the byte[]
	 */
	public static byte[] generateHandshake(byte[] peer_id, byte[] infohash) {
		
		System.out.println(peer_id.length);
		
		System.out.println(infohash.length);
		int index = 0;
		byte[] handshake = new byte[68];//49+18
		
		handshake[index] = 0x13;
		index++;
		
		byte[] BTChars = { 'B', 'i', 't', 'T', 'o', 'r', 'r', 'e', 'n', 't', ' ',
				'p', 'r', 'o', 't', 'o', 'c', 'o', 'l' };
		System.arraycopy(BTChars, 0, handshake, index, BTChars.length);
		index += BTChars.length;
		
		byte[] zero = new byte[8];
		System.arraycopy(zero, 0, handshake, index, zero.length);
		index += zero.length;
		System.arraycopy(infohash, 0, handshake, index, infohash.length/2);
		index += infohash.length/2;
		
		System.arraycopy(peer_id, 0, handshake, index, peer_id.length/2);
		
		return handshake;
	}
	
	
	public static byte[] generateBitField(byte[] bitfield) {
		byte[] bytefieldmsg = new byte[bitfield.length+2];
		
		byte msg_id = bitfieldID;
		
		
		
		return bytefieldmsg;
	}
	
	public static byte[] generateHave() {
		byte[] havemsg = new byte[5];
		
		byte msg_id = haveID;
		return havemsg;
	}
	
	public static byte[] generateRequest() {
		byte[] request = new byte[13];
		
		byte msg_id = requestID;
		return request;
	}
	
	public static byte[] generatePiece() {
		byte[] piecemsg = new byte[13];
		
		/** The index. */
		int index;
		
		/** The start. */
		int start;
		
		/** The block. */
		byte[] block;
		
		byte msg_id = pieceID;
		return piecemsg;
	}
	
	public static byte[] generateChock() {
		byte[] chock = new byte[5];
		int lenght_prefix = 1;
		byte msg_id = chockID;
		return chock;
	}
	
	public static byte[] generateUnChock() {
		byte[] unchock = new byte[5];
		int lenght_prefix = 1;
		byte msg_id = unchockID;
		return unchock;
	}
	
	public static byte[] generateInterest() {
		byte[] interest = new byte[5];
		
		int lenght_prefix = 1;
		byte msg_id = interestedID;
		return interest;
	}
	
	public static byte[] generateUnInterest() {
		byte[] un_interest = new byte[5];
		byte msg_id = uninterestedID;
		int lenght_prefix = 1;
		return un_interest;
	}
}
