package URTorrent;

import java.io.*;
import java.security.*;
import java.util.*;
import java.text.*;

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
	public static final String HELP = "help";

	
	
	public static void main (String [] args) {
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
		
		while(true) {
			Scanner scan= new Scanner(System.in);
			System.out.print("<urtorrent>");
			//Get the input command
			String text= scan.nextLine();
			switch(text) {
			case METAINFO:
				new URPeer().showMetaInfo(metafile, port);
				break;
			default:
				System.out.println("No such command for Peer");
				break;
			}
		}
		
	}
}
