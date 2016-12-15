package URTorrent;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.security.*;
/**
 * Data operator
 * Used to Encode/Decode the data to be transmitted or received
 * Bencode or SHA1 coding algorithm provided
 * @author Fangzhou_Liu
 *
 */
public class URDataOperator {
	
	
	/** The Constant HEX_CHARS. */
	public static final char[] HEX_CHARS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	
	/**
	 * Do the Bencode Encoding
	 * @param plaintext: the plain text message to be encoded
	 * @return the Bencode Encoded message for the text
	 */
	public static String BEncode(String plaintext) {
		String code = "";
		return code;
	}
	
	/**
	 * Do the Bencode Decoding
	 * @param code: the Bencoded message to be resolved
	 * @return the human-readable message for the code
	 */
	public static String BDecode(String code) {
		String text = "";
		return text;
	}
	/**
	 * Do the SHA1 Encoding
	 * @param plaintext: the plain text message to be encoded
	 * @return the SHA1 Encoded message for the text
	 * @throws NoSuchAlgorithmException 
	 */
	public static String SEncode(String plaintext) throws NoSuchAlgorithmException {
		if (plaintext == null) {
			return null;
		}
        MessageDigest mDigest = MessageDigest.getInstance(Macro.SHA1);
        byte[] result = mDigest.digest(plaintext.getBytes());
        return toHexString(result);
	}
	
	/**
	 * Do the SHA1 Encoding
	 * @param bytes: the plain text message, in byte format, to be encoded
	 * @return the SHA1 Encoded message for the text
	 * @throws NoSuchAlgorithmException 
	 */
	public static String SEncodeBytes(byte[] bytes) throws NoSuchAlgorithmException {
		if (bytes.length == 0) {
			return null;
		}
        MessageDigest mDigest = MessageDigest.getInstance(Macro.SHA1);
        byte[] result = mDigest.digest(bytes);
//        System.out.println("result.length = " +result.length);
        
        return toHexString(result);
	}	
	
	public static byte[] SEncodeID(byte[] bytes) throws NoSuchAlgorithmException {
		 MessageDigest mDigest = MessageDigest.getInstance(Macro.SHA1);
	     byte[] result = mDigest.digest(bytes);        
	     return result;
	}
	
	/**
	 * Do the SHA1 Encoding
	 * @param plaintext: the info message to be encoded
	 * @return the SHA1 Encoded message for the text
	 * @throws NoSuchAlgorithmException 
	 */
	public static byte[] SEncodeInfohash(String info) throws NoSuchAlgorithmException {
		if (info == null) {
			return null;
		}
        MessageDigest mDigest = MessageDigest.getInstance(Macro.SHA1);
        byte[] result = mDigest.digest(info.getBytes());
        return result;
	}
	
	/**
	 * Do the URL Encoding
	 * URL Encoding means any byte not in the set 0-9, a-z, A-Z
	 * or symbols like ".", "-". ",", "_" and "~", must be encoded using the %nn format
	 * where nn means the hex value of the byte.
	 * 
	 * @param plaintext: the plain text message to be encoded
	 * @return the URLEncoded message for the text
	 */
	public static String URLEncoding(String plaintext) {
		String code = "";
		try {
			code = URLEncoder.encode(plaintext, Macro.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return code;
	}
	
	/**
	 * Do the URL Decoding
	 * @param code: the URLEncoded message to be resolved
	 * @return: the human-readable message for the code
	 */
	public static String URLDecoding(String code) {
		String text = "";
		try {
			text = URLDecoder.decode(code, Macro.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	}
	
	/**
	 * Helper methods, transfer all byte in the number/letter
	 * @param sha1
	 * @return
	 */
	public static String toHex(byte[] sha1) {
		StringBuffer code = new StringBuffer();
        for (int i = 0; i < sha1.length; i++) {
            code.append(Integer.toString((sha1[i] & 0xff) + 0x100, 16).substring(1));
        } 
        return code.toString();  
	}
	
	/**
	 * Retrieve each 20 byte SHA1 for each piece from the concatenate SHA1 
	 * with the key {pieces}
	 * @param start: the start point of the SHA1
	 * @param hash: the concatenated SHA1 for all pieces
	 * @return the 20 byte SHA1 for specific piece
	 */
	public static byte[] getPieceSHA1(int start, byte [] hash) {
		byte[] piece_sha1 = new byte[Macro.SHA1_LENGTH];
		
		for(int i = 0; i<Macro.SHA1_LENGTH; i++) {
			piece_sha1[i] = hash[i+start];
		}
		
		return piece_sha1;
	}
	
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
}
