/**
 * 
 */
package URTorrent.URMessage;

import URTorrent.Macro;
import java.nio.*;

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
		// pstrlen
		handshake[index] = 0x13;
		index++;
		// pstr
		byte[] BTChars = { 'B', 'i', 't', 'T', 'o', 'r', 'r', 'e', 'n', 't', ' ',
				'p', 'r', 'o', 't', 'o', 'c', 'o', 'l' };
		System.arraycopy(BTChars, 0, handshake, index, BTChars.length);
		index += BTChars.length;
		// reserved
		byte[] zero = new byte[8];
		System.arraycopy(zero, 0, handshake, index, zero.length);
		index += zero.length;
		// info_hash
		System.arraycopy(infohash, 0, handshake, index, infohash.length/2);
		index += infohash.length/2;
		// peer_id
		System.arraycopy(peer_id, 0, handshake, index, peer_id.length/2);
		
		return handshake;
	}
	
	public static byte[] generateBitField(byte[] bitfield) {
		int index = 0;
		// length_prefix
		byte[] bitfieldmsg = new byte[bitfield.length+1];
		byte[] length_prefix = ByteBuffer.allocate(4).putInt(bitfield.length+1).array();
		System.arraycopy(bitfield, 0, length_prefix, 0, length_prefix.length);
		index += length_prefix.length;
		// msg id
		byte msg_id = bitfieldID;
		// piece index
		bitfieldmsg[index] = msg_id;
		System.arraycopy(bitfield, index+1, bitfieldmsg, 0, bitfield.length);
		
		return bitfieldmsg;
	}
	
	public static byte[] generateHave(int pindex) {
		int index = 0;
		// length_prefix
		byte[] havemsg = new byte[9];
		byte[] length_prefix = ByteBuffer.allocate(4).putInt(5).array();
		System.arraycopy(length_prefix, 0, havemsg, 0, length_prefix.length);
		index += length_prefix.length;
		// msg id
		byte msg_id = haveID;
		// piece index
		byte[] piece_index = ByteBuffer.allocate(4).putInt(pindex).array();
		System.arraycopy(piece_index, index+1, havemsg, 0, piece_index.length);
		return havemsg;
	}
	
	public static byte[] generateRequest() {
		byte[] request = new byte[13];
		int index = 0;
		// length_prefix
		byte[] length_prefix = 	ByteBuffer.allocate(4).putInt(13).array();
		System.arraycopy(length_prefix, 0, request, 0, length_prefix.length);
		index += length_prefix.length;
		byte msg_id = requestID;
		// msg id
		request[index] = msg_id;
		// index
		// begin
		// length
		return request;
	}
	
	public static byte[] generatePiece() {
		int index = 0;
		byte[] piecemsg = new byte[13];
		// length_prefix
		byte[] length_prefix = 	ByteBuffer.allocate(4).putInt(13).array();
		System.arraycopy(length_prefix, 0, piecemsg, 0, length_prefix.length);
		index += length_prefix.length;
		byte msg_id = pieceID;
		piecemsg[index] = msg_id;
		
		/** The index. */
//		int index;
		
		/** The begin. */
		int begin;
		
		/** The block. */
		byte[] block;
		
		return piecemsg;
	}
	
	public static byte[] generateChock() {
		byte[] chock = new byte[5];
		int index = 0;
		byte[] length_prefix = 	ByteBuffer.allocate(4).putInt(1).array();
		System.arraycopy(length_prefix, 0, chock, 0, length_prefix.length);
		index += length_prefix.length;
		byte msg_id = chockID;
		chock[index] = msg_id;
		
		return chock;
	}
	
	public static byte[] generateUnChock() {
		byte[] unchock = new byte[5];
		int index = 0;
		byte[] length_prefix = 	ByteBuffer.allocate(4).putInt(1).array();
		System.arraycopy(length_prefix, index, unchock, 0, length_prefix.length);
		index += length_prefix.length;
		byte msg_id = chockID;
		unchock[index] = msg_id;
		return unchock;
	}
	
	public static byte[] generateInterest() {
		byte[] interest = new byte[5];
		int index = 0;
		byte[] length_prefix = 	ByteBuffer.allocate(4).putInt(1).array();
		System.arraycopy(length_prefix, index, interest, 0, length_prefix.length);
		byte msg_id = interestedID;
		interest[index] = msg_id;
		return interest;
	}
	
	public static byte[] generateUnInterest() {
		byte[] uninterest = new byte[5];
		int index = 0;
		byte[] length_prefix = 	ByteBuffer.allocate(4).putInt(1).array();
		System.arraycopy(length_prefix, index, uninterest, 0, length_prefix.length);
		byte msg_id = uninterestedID;
		uninterest[index] = msg_id;
		return uninterest;
	}
}
