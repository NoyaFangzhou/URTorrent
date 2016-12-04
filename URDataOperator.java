package URTorrent;
import java.io.UnsupportedEncodingException;
import java.net.*;
/**
 * Data operator
 * Used to Encode/Decode the data to be transmitted or received
 * Bencode or SHA1 coding algorithm provided
 * @author Fangzhou_Liu
 *
 */
public class URDataOperator {
	
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
	 */
	public static String SEncode(String plaintext) {
		String code = "";
		return code;
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
}
