/**
 * 
 */
package URTorrent;

import java.io.IOException;

/**
 *
 * @File ServerTest.java
 * @Time 2016-12-09 11:33:41 北美东部标准时间
 * @Author Fangzhou_Liu 
 * @Description: 
 */
public class ServerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		URTCPConnector connector = new URTCPConnector();
		try {
			connector.TCPServerConnect(6969);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
