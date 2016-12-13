package URTorrent;
import java.net.*;
import java.security.*;
import java.util.*;
import java.io.*;
import URTorrent.URBencode.*;
import URTorrent.URMessage.*;

/**
 * Main Functional Class
 * Contains all Peer actions here
 * 
 * @author Fangzhou_Liu
 *
 */
public class URPeer {
	
	private String id;
	
	private String port;
	
	URMetaInfo metainfo;
	
	public URPeer(String port, String fileName) {
		this.id = URPeerIDGenerator.getPeerID(port);
		this.port = port;
		this.metainfo = new URMetaInfo(fileName);
	}
	
	/**
	 * Run the URPeer
	 */
	private void run(String command) {
		
		
	}
	
	/**
	 * operate the urtorrent <metainfo> command
	 * show the information about the metainfo file
	 * 
	 * Parse the information hide behind the metainfo file
	 * show all key-value pairs
	 * list all piece hash info 
	 * @param fileName: the file name of the .torrent file
	 * @param port: port number that the peer listen to
	 * @param peerID: id of the current peer
	 */
	public void MetaInfoFileParser(String fileName) {
		//display the content
		System.out.println("-------------------------------------");
		System.out.println("- IP/port: "+Macro.LOCALHOST+"/"+port);
		System.out.println("- ID: "+id);
		System.out.println("- metainfo file: "+fileName);
		System.out.println("- announce: "+metainfo.getAnnounce());
		for(String key : metainfo.getInfo().keySet()) {
			System.out.println("- "+key + ": " + metainfo.getInfo().get(key));
		}
		System.out.println("- piece's hashes: ");
		for(int i = 0; i<metainfo.getPieces().size(); i++) {
			System.out.println(i + " " + metainfo.getPieces().get(i));
		}	
	}
	
	public void handshake(String info_hash) {
		HandShakeMsg handshake = new HandShakeMsg(info_hash, id);
		
		System.out.println(handshake.toString());
	}
	
	
	

}
