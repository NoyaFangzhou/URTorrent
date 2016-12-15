/**
 * 
 */
package URTorrent.URThread;

import java.util.Scanner;

import URTorrent.URPeer;

/**
 *
 * @File URCommandThread.java
 * @Time 2016-12-15 02:30:58 北美东部标准时间
 * @Author Fangzhou_Liu 
 * @Description: 
 */
public class URCommandThread extends Thread {
	
	/**
	 * All URTorrent Peer Command Line command
	 */
	public static final String METAINFO = "metainfo";
	public static final String ANNOUNCE = "announce";
	public static final String TRACKERINFO = "trackerinfo";
	public static final String SHOW = "show";
	public static final String STATUS = "status";
	public static final String HELP = "help";
	
	public URPeer peer;
	
	
	public URCommandThread(URPeer peer) {
		this.peer = peer;
	}
	
	public void run() {
		while(true) {	
			
			Scanner scan= new Scanner(System.in);
			System.out.print("<urtorrent>");
			//Get the input command
			String text= scan.nextLine();
			switch(text) {
			//show the information about the metainfo file
			case METAINFO:
				peer.MetaInfoFileParser(peer.getTorrent_file());
				break;
			//send GET request to the tracker
			//update the information to be used later
			//print the status line of the response 
			case ANNOUNCE:
				break;
			//prints the information contained in the latest tracker response
			//the output is just part of the announce output, without status line
			case TRACKERINFO:
				break;
			//prints the status of all current peer connections
			//including the choked/interested states and up/download state
			case SHOW:
				break;
			//prints the status of the current download
			//which piece are missing, the number of up/downloaded and remain bytes
			case STATUS:
				break;
			//error command handler
			default:
				System.out.println("No such command for Peer");
				break;
			}
		}
	}

}
