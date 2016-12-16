/**
 * 
 */
package URTorrent;
import java.io.IOException;
import java.nio.*;
/**
 *
 * @File Test.java
 * @Time 2016-12-15 07:26:58 北美东部标准时间
 * @Author Fangzhou_Liu 
 * @Description: 
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		byte[] len = new byte[4];
//		int a = 159;
//		len = ByteBuffer.allocate(4).putInt(a).array();
//		System.out.println(len.length);
		URPeer peer = new URPeer("6881", "UR.mp3.torrent");
		try {
			peer.trackerRequest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
