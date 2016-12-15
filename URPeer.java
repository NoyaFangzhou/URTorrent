package URTorrent;
import java.net.*;
import java.security.*;
import java.util.*;
import java.io.*;
import URTorrent.URBencode.*;
import URTorrent.URMessage.*;
import URTorrent.URThread.*;

/**
 * Main Functional Class
 * Contains all Peer actions here
 * 
 * @author Fangzhou_Liu
 *
 */
public class URPeer {
	
	private String id;
	
	private String torrent_file;
	
	private String port;
	
	private int role;
	
	private URMetaInfo metainfo;
	
	private int serv_peer;//the position of the serv_peer in the peerlist
	
	private ArrayList<URPeerInfo> peers;
	
	private boolean[] bitfield;
	
	private int[] piecePrevalence;
	
	private PeerStatus status;
	
//	private ArrayList<PeerStatus> statusList;//stores the status for each remote peer
	
	//constructor
	public URPeer(String port, String fileName) {
		this.id = URPeerIDGenerator.getPeerID(port);
		this.port = port;
		this.metainfo = new URMetaInfo(fileName);
		this.torrent_file = fileName;
		this.peers = new ArrayList<URPeerInfo>(Macro.MAX_PEERS);
		this.status = new PeerStatus(metainfo.getPieces().size());
		//生成 bitfield
		this.bitfield = new boolean[this.metainfo.getPieces().size()];
		this.piecePrevalence = new int[this.metainfo.getPieces().size()];
		//全0序列	
		Arrays.fill(this.piecePrevalence, 0);
	}
	
	

	/**
	 * @return the peers
	 */
	public ArrayList<URPeerInfo> getPeers() {
		return peers;
	}

	/**
	 * @param peers the peers to set
	 */
	public void setPeers(ArrayList<URPeerInfo> peers) {
		this.peers = peers;
	}

	/**
	 * @return the bitfield
	 */
	public boolean[] getBitfield() {
		return bitfield;
	}

	/**
	 * @param bitfield the bitfield to set
	 */
	public void setBitfield(boolean[] bitfield) {
		this.bitfield = bitfield;
	}

	/**
	 * @return the status
	 */
	public PeerStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(PeerStatus status) {
		this.status = status;
	}

	/**
	 * @return the torrent_file
	 */
	public String getTorrent_file() {
		return torrent_file;
	}

	/**
	 * @param torrent_file the torrent_file to set
	 */
	public void setTorrent_file(String torrent_file) {
		this.torrent_file = torrent_file;
	}

	/**
	 * @return the role
	 */
	public int getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(int role) {
		this.role = role;
		if(this.role == Macro.LEECHER) {
			Arrays.fill(this.bitfield, false);	//all 0
			status.bitfield = this.bitfield;
		}
		else {
			Arrays.fill(this.bitfield, true);	//all 1
			status.bitfield = this.bitfield;
		}
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
	
	/**
	 * Doing the handshake process
	 * Send TCP socket contains the String format of handshake
	 * 
	 * @param info_hash: info_hash needed for handshake message
	 */
	public void handshake() {
		try {
			byte[] handshakemsg = URMessageGenerator.generateHandshake(id.getBytes(), metainfo.getInfo_hash().getBytes());
			
			for(int i = 0; i<peers.size(); i++) {
				URTCPConnector.SendHandshake(peers.get(i).ip, peers.get(i).port, handshakemsg);
			}
			
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Doing the handshake process
	 * Send TCP socket contains the String format of handshake
	 * 
	 * @param info_hash: info_hash needed for handshake message
	 */
	public void handshake2(String target_ip, int target_port) {
		try {
			byte[] handshakemsg = URMessageGenerator.generateHandshake(id.getBytes(), metainfo.getInfo_hash().getBytes());
			
			URTCPConnector.SendHandshake(target_ip, target_port, handshakemsg);
			
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Sending the bitfield message to other peers
	 * Send TCP socket contains the String format of bitfield
	 * Bitfield message indicate which piece it has and which piece it needs
	 * 
	 * @param info_hash: info_hash needed for bitfield message
	 */
	public void bitfield(String target_ip, int target_port) {
		try {
			byte[] bitfieldmsg = URMessageGenerator.generateBitField(UtilTools.boolToBitfieldArray(this.bitfield));
			URTCPConnector.SendBitField(target_ip, target_port, bitfieldmsg);
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public void unchoked(String target_ip, int target_port) {
		try {
			byte[] unchokemsg = URMessageGenerator.generateUnChock();
			URTCPConnector.SendUnChoked(target_ip, target_port, unchokemsg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void request(String target_ip, int target_port) {
		try {
			byte[] requestmsg = URMessageGenerator.generateRequest();
			URTCPConnector.SendUnChoked(target_ip, target_port, requestmsg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void piece(String target_ip, int target_port) {
		try {
			byte[] piecemsg = URMessageGenerator.generatePiece();
			URTCPConnector.SendUnChoked(target_ip, target_port, piecemsg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void interest(String target_ip, int target_port) {
		try {
			byte[] interestmsg = URMessageGenerator.generateInterest();
			URTCPConnector.SendInterest(target_ip, target_port, interestmsg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void trackerRequest() {
		try {
			String header = "GET /announce?info_hash="+metainfo.getInfo_hash()+"&";
			String body = "";
			if(role == Macro.SEEDER) {
				body = "peer_id="+id+"&port="+port+"&uploaded=0&downloaded=0&left=0&compact=1&event=started";
			}
			else if(role == Macro.LEECHER) {
				body = "uploaded=0&downloaded=0&left="+metainfo.getInfo().get(metainfo.LENGTH)+"&compact=1&event=started";
			}
			String tail = " HTTP/1.1\r\n\r\n";
			
			Socket request = new Socket(Macro.LOCALHOST, Macro.TRACKER_PORT);
			System.out.println("Request Header:\n"+header+body+tail);
			
			PrintWriter out = new PrintWriter(request.getOutputStream());
			BufferedReader buf = new BufferedReader(new InputStreamReader(request.getInputStream()));
			out.print((header+body+tail));
			//get the response
			String response;
			int line_num = 0;
			while((response = buf.readLine())!=null) {
				System.out.println(response+"\n");
				if(line_num == 0) {
					if(!URTCPConnector.TCPResponseCodeChecker(response)) {
						System.out.println("Tracker Error, Peer closing ....!");
						System.exit(0);
					}
				}
				if(line_num == 2) {
					//parse the return bencode
				}
			}
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Check whether the info_hash in the received handshake is current serving
	 * @param serv_info_hash: info_hash that is currently serving
	 * @param response: the response handshake
	 * @return
	 */
	public boolean checkHandshake(byte[] serv_info_hash, byte[] response) {
		byte[] peerHash = new byte[20];//info hash in the received handshake
		System.arraycopy(response, 28, peerHash, 0, 20);

		if(!Arrays.equals(peerHash, serv_info_hash))
		{
			System.out.println("Handshake verification failed with Peer: " + id);
			return false;
		}
		System.out.println("Verified Handshake.");
		return true;
	}
	
	
	
	
	public void PrintStatus() {
		System.out.println("----------------------------------");
		System.out.println("Peer Current Status\n----------------------------------");
		System.out.println("Downloaded: "+status.download+"\nUploaded: "+status.upload+"\nLeft: "+status.left+"\nMy bit field: \n"+status.bitfield());
	}
	
	
	public void PrintConnection() {
		System.out.println("----------------------------------");
		System.out.println("Peers Connection Status\n----------------------------------");
		System.out.println("Downloaded: "+status.download+"\nUploaded: "+status.upload+"\nLeft"+status.left+"\nMy bit field: "+status.bitfield());
	}
	
	
	

}
