package URTorrent;

import java.io.*;
import java.security.*;
import java.util.*;
import java.text.*;
import URTorrent.URThread.*;


/**
 * Main running program
 * Launch the URTorrent Peer Client and type in the command
 * @author Fangzhou_Liu
 *
 */
public class URTorrent {
	
	/**
	 * All URTorrent Peer Command Line command
	 */
	public static final String METAINFO = "metainfo";
	public static final String ANNOUNCE = "announce";
	public static final String TRACKERINFO = "trackerinfo";
	public static final String SHOW = "show";
	public static final String STATUS = "status";
	public static final String QUIT = "quit";
	public static final String HELP = "help";
	
	private ArrayList<PeerStatus> statusList;//stores the status for each remote peer
	
	public static ArrayList<URPeerInfo> info = new ArrayList<URPeerInfo>();
	
	public static void main (String [] args) {
//		info.add(new URPeerInfo(Macro.LOCALHOST, 6000));
		System.out.println("-------------------------------------");
		System.out.println("Metainfo File Name: " + args[0]);
		System.out.println("Port Number: " + args[1]);
		System.out.println("Time: " + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss z").format(new Date()));
		System.out.println("\nDeveloper: Fangzhou Liu");
		System.out.println("           Hao Zhou");
		System.out.println("-------------------------------------");
		System.out.println("\nURTorrent Launching ..... \n");
		launch(args[0], args[1]);
	}
	
	/**
	 * Start the URTorrent Client
	 * Take the user input as command, do operations according to the command
	 */
	public static void launch(String metafile, String port) {
		URPeer peer = new URPeer(port, metafile);
//		peer.setPeers(info);
		if(findSrc(metafile)) {
			peer.setRole(Macro.SEEDER);
		}
		else {
			peer.setRole(Macro.LEECHER);
		}
//		System.out.println("role: "+peer.getRole());
		try {
			peer.trackerRequest();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		//Open the server thread to receive messages
		URReceiverThread server = new URReceiverThread(Integer.parseInt(port), peer);
		server.start();
		
		if((peer.getRole() == Macro.LEECHER) && peer.getPeers().size() > 1) {
			//send the handshake for all known peers
			peer.handshake();
		}
		
		while(true) {	
			
			Scanner scan= new Scanner(System.in);
			System.out.print("<urtorrent>");
			//Get the input command
			String text= scan.nextLine();
			switch(text.toLowerCase()) {
			//show the information about the metainfo file
			case METAINFO:
				peer.MetaInfoFileParser(peer.getTorrent_file());
				break;
			//send GET request to the tracker
			//update the information to be used later
			//print the status line of the response 
			case ANNOUNCE:
				try {
					peer.Print_Announce();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			//prints the information contained in the latest tracker response
			//the output is just part of the announce output, without status line
			case TRACKERINFO:
				peer.Print_Trackerinfo();
				break;
			//prints the status of all current peer connections
			//including the choked/interested states and up/download state
			case SHOW:
				break;
			//prints the status of the current download
			//which piece are missing, the number of up/downloaded and remain bytes
			case STATUS:
				peer.PrintStatus();
				break;
			case HELP:
				PrintHelp();
				break;
			case QUIT:
				System.out.println("PEER EXISTING ......");
				System.exit(0);
			//error command handler
			default:
				System.out.println("No such command for Peer");
				break;
			}
		}
	}
	
	/**
	 * Find the role of the peer
	 * If the source file exist, it is seeder, don't send handshake
	 * If the source file doesn't exist, it is leecher, send handshake
	 * @param fileName
	 * @return
	 */
	public static boolean findSrc(String fileName) {
		String srcfile = fileName.split(".torrent")[0];
		System.out.println(srcfile);
		boolean isSeeder = true;
		File source = new File(srcfile);    
		if(source.exists()) {
			isSeeder = false;
		}    
		return isSeeder;
	}
	
	
	private static void PrintHelp() {
		System.out.println("----------------------------------------------");
		System.out.println("	  HELP PANEL                    ");
		System.out.println("----------------------------------------------\n");
		System.out.println(" - metainfo:  show the information about the metainfo file");
		System.out.println(" - announce:  show the latest information in tracker and the Tracker\'s response");
		System.out.println(" - trackerinfo:  show the information contained in the latest tracker response");
		System.out.println(" - status:   show the current piece status");
		System.out.println(" - show:  show the status of all currrent peer connections, include choked/interested states");
		System.out.println(" - quit: shut down the peer");
		System.out.println("");
		System.out.println("----------------------------------------------");
	}
}
