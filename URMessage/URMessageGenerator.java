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
	public static byte[] generateHandshake(byte[] peer_id, ByteBuffer infohash) {
		
		int index = 0;
		byte[] handshake = new byte[68];//20+20+1+8+18
		// pstrlen
		handshake[index] = 18;
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
		System.arraycopy(infohash.array(), 0, handshake, index, 20);
		index += 20;
		// peer_id
		System.arraycopy(peer_id, 0, handshake, index, 20);
		
		return handshake;
	}
	
	public static byte[] generateBitField(byte[] bitfield) {
		int index = 0;
		// length_prefix
		byte[] bitfieldmsg = new byte[bitfield.length+5];
		byte[] length_prefix = ByteBuffer.allocate(4).putInt(bitfield.length+1).array();
		System.arraycopy(length_prefix, 0, bitfieldmsg, 0, length_prefix.length);
		index += length_prefix.length;
		// msg id
		byte msg_id = bitfieldID;
		// piece index
		bitfieldmsg[index] = msg_id;
		System.out.println("from "+(index+1)+" to "+(index+1+bitfield.length) + " left "+ (bitfieldmsg.length - index-1));
		System.arraycopy(bitfield, 0, bitfieldmsg, index+1, bitfield.length);
		
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
		index ++; 
		// piece index
		byte[] piece_index = ByteBuffer.allocate(4).putInt(pindex).array();
		System.arraycopy(piece_index, 0, havemsg, index, piece_index.length);
		return havemsg;
	}
	
	public static byte[] generateRequest(int request_piece_index, int request_piece_begin, int request_piece_length) {
		byte[] request = new byte[13];
		int index = 0;
		// length_prefix
		byte[] length_prefix = 	ByteBuffer.allocate(4).putInt(13).array();
		System.arraycopy(length_prefix, 0, request, 0, length_prefix.length);
		index += length_prefix.length;
		byte msg_id = requestID;
		// msg id
		request[index] = msg_id;
		index ++;
		// index
		byte[] piece_index = 	ByteBuffer.allocate(4).putInt(request_piece_index).array();
		System.arraycopy(piece_index, 0, request, index, 4);
		index += 4;
		// begin
		byte[] piece_begin = 	ByteBuffer.allocate(4).putInt(request_piece_begin).array();
		System.arraycopy(piece_begin, 0, request, index, 4);
		index += 4;
		// length
		byte[] piece_length = 	ByteBuffer.allocate(4).putInt(request_piece_length).array();
		System.arraycopy(piece_length, 0, request, index, 4);
		return request;
	}
	
	public static byte[] generatePiece(int requested_piece_index, int requested_piece_begin, byte[] requested_piece_data) {
		int index = 0;
		byte[] piecemsg = new byte[13];
		// length_prefix
		byte[] length_prefix = 	ByteBuffer.allocate(4).putInt(13).array();
		System.arraycopy(length_prefix, 0, piecemsg, 0, length_prefix.length);
		index += length_prefix.length;
		byte msg_id = pieceID;
		piecemsg[index] = msg_id;
		index ++;
		
		/** The index. */
		byte[] piece_index = ByteBuffer.allocate(4).putInt(requested_piece_index).array();
		System.arraycopy(piece_index, 0, piecemsg, index, 4);
		index += 4;
		/** The begin. */
		byte[] piece_begin = ByteBuffer.allocate(4).putInt(requested_piece_begin).array();
		System.arraycopy(piece_begin, 0, piecemsg, index, 4);
		index += 4;
		/** The block. */
		System.arraycopy(requested_piece_data, 0, piecemsg, 0, requested_piece_data.length);
		
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
