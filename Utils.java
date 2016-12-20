package URTorrent;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Random;
import java.security.*;
import java.util.*;

/**
 * The Class Utils handles miscellaneous functions that help us
 * perform tasks throughout the torrent client.
 * 
 * @author Deepak, Mike, Josh
 * modified by Fangzhou Liu
 */
public class Utils extends ToolKit {
	
	/** The Constant HEX_CHARS. */
	public static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * To hex string.
	 * 
	 * Converts a byte array to a hex string
	 *
	 * @param bytes the bytes
	 * @return the string
	 */
	public static String toHexString(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		
		if (bytes.length == 0) {
			return "";
		}

		StringBuilder sb = new StringBuilder(bytes.length * 3);

		for (byte b : bytes) {
			byte hi = (byte) ((b >> 4) & 0x0f);
			byte lo = (byte) (b & 0x0f);

			sb.append('%').append(HEX_CHARS[hi]).append(HEX_CHARS[lo]);
		}
		return sb.toString();
	}
	
	/**
	 * Gen peer id.
	 * 
	 * Generates a random peer id to identify ourself
	 *
	 * @return the byte[]
	 */
	protected static byte[] genPeerId() {
		Random rand = new Random(System.currentTimeMillis());
		byte[] peerId = new byte[20];

		peerId[0] = 'G';
		peerId[1] = 'P';
		peerId[2] = '0';
		peerId[3] = '2';

		for (int i = 4; i < 20; ++i) {
			peerId[i] = (byte) ('A' + rand.nextInt(26));
		}
		return peerId;
	}

	/**
	 * Bitfield to bool array.
	 * 
	 * Converts a given bitfield to a boolean array
	 *
	 * @param bitfield the bitfield
	 * @param numPieces the num pieces
	 * @return the boolean[]
	 */
//	public static boolean[] bitfieldToBoolArray(byte[] bitfield, int numPieces) {
//		if (bitfield == null)
//			return null;
//		else {
//			boolean[] retArray = new boolean[numPieces];
//			for (int i = 0; i < retArray.length; i++) {
//				int byteIndex = i / 8;
//				int bitIndex = i % 8;
//
//				if (((bitfield[byteIndex] << bitIndex) & 0x80) == 0x80)
//					retArray[i] = true;
//				else
//					retArray[i] = false;
//
//			}
//			return retArray;
//		}
//	}
	
	public static boolean[] bitfieldToBoolArray(byte[] bitfield) {
		boolean[] retArray = new boolean[bitfield.length];
		for (int i = 0; i < retArray.length; i++) {
			if(bitfield[i] == 1) {
				retArray[i] = true;
			}
			else {
				retArray[i] = false;
			}
		}
		return retArray;
	}

	/**
	 * Bool to bitfield array.
	 * 
	 * Converts a given boolean array to a bitfield
	 *
	 * @param verifiedPieces the verified pieces
	 * @return the byte[]
	 */
//	public static byte[] boolToBitfieldArray(boolean[] verifiedPieces) {
//		int length = verifiedPieces.length / 8;
//
//		if (verifiedPieces.length % 8 != 0) {
//			++length;
//		}
//
//		int index = 0;
//		byte[] bitfield = new byte[length];
//
//		for (int i = 0; i < bitfield.length; ++i) {
//			for (int j = 7; j >= 0; --j) {
//
//				if (index >= verifiedPieces.length) {
//					return bitfield;
//				}
//
//				if (verifiedPieces[index++]) {
//					bitfield[i] |= (byte) (1 << j);
//				}
//			}
//		}
//		return bitfield;
//	}
	/**
	 * Transfer a boolean array type bitfield into a byte array
	 * 
	 * @param bitfield_bool: the boolean type array to be transfered
	 * @return: the byte type array that represents the bitfield
	 */
	public static byte[] boolToBitfieldArray(boolean[] bitfield_bool) {
		byte[] bitfield = new byte[bitfield_bool.length];
		for(int i = 0; i<bitfield_bool.length; i++) {
			if(bitfield_bool[i]) {
				bitfield[i] = 1;
			}
			else {
				bitfield[i] = 0;
			}
		}
		return bitfield;
	}
	
	/**
	 * Check pieces.
	 * 
	 * Check the pieces of the file we have on disk so that we can increment
	 * downloaded stats and send appropriate bitfield
	 *
	 * @return the boolean[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean[] checkPieces(TorrentInfo torrentInfo, File outputFile) throws IOException {

		int numPieces = torrentInfo.piece_hashes.length;
		int pieceLength = torrentInfo.piece_length;
		int fileLength = torrentInfo.file_length;
		ByteBuffer[] pieceHashes = torrentInfo.piece_hashes;
		int lastPieceLength = fileLength % pieceLength == 0 ? pieceLength : fileLength % pieceLength;

		byte[] piece = null;
		boolean[] verifiedPieces = new boolean[numPieces];

		for (int i = 0; i < numPieces; i++) {
			if (i != numPieces - 1) {
				piece = new byte[pieceLength];
				piece = readFile(i, 0, pieceLength, torrentInfo, outputFile);
			} else {
				piece = new byte[lastPieceLength];
				piece = readFile(i, 0, lastPieceLength, torrentInfo, outputFile);
			}
			
			
			
			if (verifySHA1(piece, pieceHashes[i], i)) {
				verifiedPieces[i] = true;
			}
		}
		
		return verifiedPieces;
	}
	
	/**
	 * Read file.
	 * 
	 * Reads a piece from file on the disk in instances
	 * where we are uploading a piece or if we are 
	 * checking the pieces of the file we already have
	 *
	 * @param index the index, start with 0
	 * @param offset the offset
	 * @param length the length
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] readFile(int index, int offset, int length, TorrentInfo torrentInfo, File outputFile) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(outputFile, "r");
		byte[] data = new byte[length];
		
		raf.seek(torrentInfo.piece_length * index + offset);
		raf.read(data);
		raf.close();
		
		return data;
	}
	
	/**
	 * Write the byte array into the file
	 * 
	 * @param index: the piece's location
	 * @param offset: the offset of the piece, always set to 0
	 * @param length: the length of the piece
	 * @param torrentInfo: the metainfo data structure
	 * @param data: the byte array represent the piece data
	 * @param outputFileName: the output file name
	 * @throws IOException
	 */
	public static void writeFile(int index, int offset, int length, TorrentInfo torrentInfo, byte[] data, String outputFileName) throws IOException {
		File output_file = new File(outputFileName);
		System.out.println("!!!!!!");
		if(!output_file.exists()) {
			output_file.createNewFile();
		}
		RandomAccessFile raf = new RandomAccessFile(output_file, "rw");
		
		raf.seek(torrentInfo.piece_length * index + offset);
		raf.write(data, offset, length);
		
		raf.close();
		
	}
	
	
	/**
	 * Verify SHA1.
	 * 
	 * Verifies the SHA1 of a piece of a file against the info hash 
	 * values
	 *
	 * @param piece the piece
	 * @param SHA1Hash the sH a1 hash
	 * @return true, if successful
	 * @throws NoSuchAlgorithmException 
	 */
	public static boolean verifySHA1(byte[] piece, ByteBuffer SHA1Hash, int index) {
		MessageDigest mDigest;
		try {
			mDigest = MessageDigest.getInstance(Macro.SHA1);
			return new String(mDigest.digest(piece)).equals(new String(SHA1Hash.array()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}	
	}
	
	/**
	 * Data format transfer
	 * 
	 * Transfer the boolean array, indicating the bitfield that has(1 -> have, 0 -> miss)
	 * return a String of 0/1 to indicate the piece that had alread have
	 * 
	 * @param piece_indicator
	 * @return
	 */
	public static String booleanAryToString(boolean[] piece_indicator) {
		String return_str = "";
		for(int i = 0; i<piece_indicator.length; i++) {
			if(piece_indicator[i]) {
				return_str += "1";
			}
			else {
				return_str += "0";
			}
		}
		return "["+return_str+"]";
	}
	
	/**
	 * Helper methods
	 * 
	 * used to show the human readable format of the byte array
	 * 
	 * @param byteAry: the byte array to be displayed
	 */
	public static void showByteArray(byte[] byteAry) {
		System.out.println("length: "+byteAry.length+"\ncontent: " + new String(byteAry));
		for(int i = 0; i<byteAry.length; i++) {
			System.out.print(byteAry[i]+" ");
		}
	}
}
