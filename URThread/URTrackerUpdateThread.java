/**
 * 
 */
package URTorrent.URThread;

import java.util.*;
import java.util.concurrent.TimeUnit;
import URTorrent.URPeer;

/**
 *
 * @File URTrackerUpdateThread.java
 * @Time 2016-12-19 12:29:44 北美东部标准时间
 * @Author Fangzhou_Liu 
 * @Description: 
 */
public class URTrackerUpdateThread extends Thread {

	private URPeer peer;
	/**
	 * 
	 */
	public URTrackerUpdateThread(URPeer peer) {
		// TODO Auto-generated constructor stub
		this.peer = peer;
	}
	
	public void run() {
		// start tracker updating thread after 10s
		// and run this thread every 10s
		while(true) {
			try {
				System.out.println("\n<-- Periodically send Request to Tracker -->\n");
				peer.update("");
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
