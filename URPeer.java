package URTorrent;
import java.net.*;
import java.security.*;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

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
	
	private String ip;
	
	private String port;
	
	private int role;
	
	private URMetaInfo metainfo;
	
	private int serv_peer;//the position of the serv_peer in the peerlist
	
	private ArrayList<URPeerInfo> peers;
	
	private boolean[] bitfield;
	
	private int[] piecePrevalence;
	
	private PeerStatus status;
	
	private URAnnounce announce;
	
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
		this.announce = new URAnnounce();
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
			status.left = metainfo.getFile_size();
			System.out.println("!!!!!!"+ metainfo.getFile_size());
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
			byte[] bitfieldmsg = URMessageGenerator.generateBitField(Utils.boolToBitfieldArray(this.bitfield));
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
	
	/**
	 * Build the TCP connection with the tracker
	 * @param url: ip_addr of the server
	 * @param port: port of the server
	 * @throws IOException
	 * @throws BencodingException 
	 */
	public void trackerRequest() throws IOException {
		String Info = "";
		File torrentFile = new File(torrent_file);
		TorrentInfo torrentInfo = null;
		if(!torrentFile.exists()){
			System.out.println("No such torrent file exists");
			return;
		}
		
		TorrentReader torrentReader = new TorrentReader();
		
		if((torrentInfo = torrentReader.parseTorrentFile(torrentFile)) == null){
			System.out.println("Unable to parse torrent file; Exiting.");
			return;
		} else{
			System.out.println("Torrent file parsed successfully");
		}
		
//		System.out.println(torrentInfo.announce_url);
		//+++++++++++++++++++  get URL  +++++++++++++++++++++++++
		String urlinfo = createURL(torrentInfo, id.getBytes(), port+"", status.upload, status.download, status.left, "started");
		//get tracker response, begin parser
		String trackerResponse = sendGETRecieveResponse(urlinfo);
		String preInfo = trackerResponse.substring(0, trackerResponse.indexOf("peers")-2)+"e";
		String peers = trackerResponse.substring(trackerResponse.lastIndexOf(":")+1,trackerResponse.length());
//		System.out.println(preInfo+"\n"+peers);
//		System.out.println(peers);
		byte[] pe = peers.getBytes();
		System.out.println("size:"+(pe.length));
		for(int i =0; i<pe.length-2; i = i + 6){
//			System.out.print(pe[i + 0] + ".");
//			System.out.print(pe[i + 1] + ".");
//			System.out.print(pe[i + 2] + ".");
//			System.out.print(pe[i + 3] + "\n");
//			System.out.println((int)(pe[i + 4] << 8 | pe[i +5]));
			//＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋
			String remote_peer_ip = pe[i + 0] + "." + pe[i + 1] + "." + pe[i + 2] + "." + pe[i + 3];
			int remote_peer_port = (int)(pe[i + 4] << 8 | pe[i +5]);
			boolean isExist = false;
			for(URPeerInfo exist_peer : this.peers) {
				if(exist_peer.ip.equals(remote_peer_ip) && exist_peer.port == (remote_peer_port)) {
					isExist = true;
					break;
				}
			}
			if(!isExist) {
				URPeerInfo remote_peerInfo = new URPeerInfo(remote_peer_ip ,remote_peer_port);
				this.peers.add(remote_peerInfo);
			}
			//＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋
		}
		
		TrackerResponseParser(preInfo, this.announce);
		
	}
	
	private static String createURL(TorrentInfo torrentInfo, byte[] peerId, String port, int uploaded, int downloaded, int left, String event) throws MalformedURLException {
		String newURL ="GET /announce";
		newURL += "?info_hash=" + Utils.toHexString(torrentInfo.info_hash.array())
				+ "&peer_id=" + Utils.toHexString(peerId) + "&port="
				+ port + "&uploaded=" + uploaded + "&downloaded="
				+ downloaded + "&left=" + left + "&compact=1&event="+event+" HTTP/1.1";
		return newURL;
	}
	
	private static String sendGETRecieveResponse(String url) {
		try {
			Socket socket = new Socket(Macro.LOCALHOST, Macro.TRACKER_PORT);
			String header_line = "";
			String payload = "";
	        try {
				OutputStream out = socket.getOutputStream();
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
				url = url+"\r\n\r\n";
//				System.out.println(url);
				out.write(url.getBytes()); 
				DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
				
				String response_header = "";
				int line = 0;
	            while(true){    
		           	response_header=in.readLine();
		           	if(response_header==null) {
		   	           	 break;
		   	         }
		           	if(line == 0) {
		           		header_line = response_header;
		           	}
		           	if(line == 4) {
		           		payload = response_header;
		           	}
		           	line ++;
	            }                                    
				out.close();
				in.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	        // Check whether the response header is 2XX
//	        System.out.println(header_line);
		    if(!URTCPConnector.TCPResponseCodeChecker(header_line)) {
		    	System.out.println("ERROR!\nCannot contact to tracker successfully\nReason: "+payload);
		    	System.exit(0);
		    	return null;
		    }
		    else {
//		    	System.out.println(payload);
		    	return payload;
		    }
		} catch (IOException e) {
			System.out.println("Error communicating with tracker");
			return null;
		}
	}
	
	
	private static void TrackerResponseParser(String resp, URAnnounce announce) {
		InputStream input = new ByteArrayInputStream(resp.getBytes(StandardCharsets.UTF_8));
		try {
			BencodeMap map = (BencodeMap) Bencode.parseBencode(input);
			//go over all key-val pairs for metainfo dict
			for(BencodeString key : map.keySet()) {
				if(key.equals(new BencodeString(URAnnounce.INTERVAL))) {
					BencodeInt interval = (BencodeInt)map.get(key);
					announce.setInterval(interval.getInt());
				}
				else if(key.equals(new BencodeString(URAnnounce.MIN_INTERVAL))) {
					BencodeInt min_interval = (BencodeInt)map.get(key);
					announce.setMin_interval(min_interval.getInt());
				}
				else if(key.equals(new BencodeString(URAnnounce.COMPLETE))) {
					BencodeInt complete = (BencodeInt)map.get(key);
					announce.setComplete(complete.getInt());
				}
				else if(key.equals(new BencodeString(URAnnounce.INCOMPLETE))) {
					BencodeInt incomplete = (BencodeInt)map.get(key);
//					System.out.println("incomplete: "+ incomplete.getInt());
					announce.setIncomplete(incomplete.getInt());
				}
				else if(key.equals(new BencodeString(URAnnounce.DOWNLOADED))) {
					BencodeInt download = (BencodeInt)map.get(key);
					announce.setDownload(download.getInt());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public void Print_Announce() throws IOException{
		trackerRequest();
		System.out.println("Tracker responded: HTTP/1.1 200 OK");
		System.out.println("complete | downloaded | incomplete | interval | min interval");
		System.out.println("------------------------------------------------------------");
		System.out.print(announce.getComplete()+"          ");
		System.out.print(announce.getDownload()+"            ");
		System.out.print(announce.getIncomplete()+"           ");
		System.out.print(announce.getInterval()+"           ");
		System.out.print(announce.getMin_interval()+"\n");
		System.out.println("------------------------------------------------------------");
		System.out.println("Peer List :");
		System.out.println("IP          | Port");
		System.out.println("---------------------------");
		for(int i=0; i<peers.size(); i++){
			System.out.print(peers.get(i).ip+"    ");
			System.out.println(peers.get(i).port);
		}
		System.out.println("...Tracker closed connection");
	}
	
	public void Print_Trackerinfo(){
		System.out.println("complete | downloaded | incomplete | interval | min interval");
		System.out.println("------------------------------------------------------------");
		System.out.print(announce.getComplete()+"          ");
		System.out.print(announce.getDownload()+"            ");
		System.out.print(announce.getIncomplete()+"           ");
		System.out.print(announce.getInterval()+"           ");
		System.out.print(announce.getMin_interval()+"\n");
		System.out.println("------------------------------------------------------------");
		System.out.println("Peer List (self included):");
		System.out.println("IP          | Port");
		System.out.println("---------------------------");
//		System.out.println("127.0.0.1"+"    "+"9999");
		for(int i=0; i<peers.size(); i++){
			System.out.print(peers.get(i).ip+"    ");
			System.out.println(peers.get(i).port);
		}
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
