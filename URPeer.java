package URTorrent;
import java.net.*;
import java.security.*;
import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.*;

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
	
	private TorrentInfo metainfo;
	
	private int serv_peer;//the position of the serv_peer in the peerlist
	
	private boolean[] bitfield;
	
	private int[] piecePrevalence;
	
	private URPeerInfo self_status;
	
	private URAnnounce announce;

	
	//constructor
	public URPeer(String ip, String port, String fileName) throws BencodingException {
		this.ip = ip;
		this.id = URPeerIDGenerator.getPeerID(port);
		this.port = port;
		this.metainfo = parseTorrentFile(new File(fileName));
		this.torrent_file = fileName;
		this.self_status = new URPeerInfo(ip, Integer.parseInt(port),this.metainfo.piece_hashes.length);
		//生成 bitfield
		this.bitfield = new boolean[this.metainfo.piece_hashes.length];
		this.piecePrevalence = new int[this.metainfo.piece_hashes.length];
		//全0序列	
		Arrays.fill(this.piecePrevalence, 0);
		this.announce = new URAnnounce();
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
	public URPeerInfo getStatus() {
		return self_status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(URPeerInfo status) {
		this.self_status = status;
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
	 * @return the metainfo
	 */
	public TorrentInfo getMetainfo() {
		return metainfo;
	}

	/**
	 * @param metainfo the metainfo to set
	 */
	public void setMetainfo(TorrentInfo metainfo) {
		this.metainfo = metainfo;
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
			self_status.bitfield = this.bitfield;
			self_status.left = metainfo.file_length;
		}
		else {
			Arrays.fill(this.bitfield, true);	//all 1
			self_status.bitfield = this.bitfield;
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
			byte[] handshakemsg = URMessageGenerator.generateHandshake(id.getBytes(), metainfo.info_hash);
			for(int i = 0; i<self_status.remote_peers.size(); i++) {
				URTCPConnector.SendHandshake(self_status.remote_peers.get(i).ip, self_status.remote_peers .get(i).port, handshakemsg);
			}
			
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
			byte[] handshakemsg = URMessageGenerator.generateHandshake(id.getBytes(), metainfo.info_hash);
			
			URTCPConnector.SendHandshake(target_ip, target_port, handshakemsg);
			
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
		int selected_piece_index = RarestSelecte();
		int selected_piece_startpoint = this.calPieceLocation(selected_piece_index);
		int selected_piece_length = this.calPieceSize(selected_piece_index); 
		try {
			byte[] requestmsg = URMessageGenerator.generateRequest(selected_piece_index, selected_piece_startpoint, selected_piece_length);
			URTCPConnector.SendUnChoked(target_ip, target_port, requestmsg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void piece(String target_ip, int target_port, int requested_piece_index) {
		int requested_piece_startpoint = this.calPieceLocation(requested_piece_index);
		int requested_piece_length = this.calPieceSize(requested_piece_index);
		byte[] requested_piece_data;
		try {
			this.getStatus().upload += requested_piece_length;
			requested_piece_data = Utils.readFile(requested_piece_index, 0, requested_piece_length, metainfo, new File(this.metainfo.file_name));
			byte[] piecemsg = URMessageGenerator.generatePiece(requested_piece_index, requested_piece_startpoint, requested_piece_data);
			URTCPConnector.SendUnChoked(target_ip, target_port, piecemsg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void have(String target_ip, int target_port, int piece_index) {
		try {
			byte[] havemsg = URMessageGenerator.generateHave(piece_index);
			URTCPConnector.SendHave(target_ip, target_port, havemsg);
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
	 * Send the GET request to the tracker
	 * @param url: the GET request
	 * @param metainfo: used to get the ip and port of the tracker
	 * @return the response of the tracker after request
	 */
	private static byte[] sendGETRecieveResponse(String url, TorrentInfo metainfo) {
		try {
			System.out.println("tracker IP/port = " + metainfo.tracker_ip+"/"+metainfo.tracker_port);
			Socket socket = new Socket(metainfo.tracker_ip, metainfo.tracker_port);
			String header_line = "";
			String payload = "";
			OutputStream out = socket.getOutputStream();
			url = url+"\r\n\r\n";
			out.write(url.getBytes()); 
			InputStream inputStream = socket.getInputStream();
			byte[] response = new byte[256];
			int actual_length = inputStream.read(response);
//			System.out.println("tracker response length = " + actual_length);
//			System.out.println("return response length = " + response.length);
			byte[] return_response = new byte[actual_length];
			System.arraycopy(response, 0, return_response, 0, actual_length);
//			for(int i = 0; i<return_response.length; i++) {
//				System.out.println("byt["+(i+1)+"] : " + return_response[i]);
//			}
			out.close();
			inputStream.close();
			return return_response;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error communicating with tracker");
			return null;
		}
	}
	
	/**
	 * Parse the tracker's response bencode payload
	 * 
	 * @param resp: byte array of the entire response
	 * @param announce: the object that store the tracker response
	 * @throws BencodingException
	 */
	private static void TrackerResponseParser(String resp, URAnnounce announce) throws BencodingException {
		InputStream input = new ByteArrayInputStream(resp.getBytes(StandardCharsets.UTF_8));
		try {
//			System.out.println("resp = " +resp);
			BencodeMap map = (BencodeMap) Bencode.parseBencode(input);
//			for(BencodeString key : map.keySet()) {
//				System.out.println("key: " + key.toString() + " val: " + map.get(key));
//			}

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
//		HashMap<ByteBuffer, Object> info = (HashMap<ByteBuffer, Object>) Bencoder2.decode(resp);
	}
	
	
	
	/**
	 * Parses the torrent file.
	 *
	 * @param torrentFile the torrent file
	 * @return the torrent info
	 */
	public TorrentInfo parseTorrentFile(File torrentFile) {
		
		try {
			DataInputStream dataInputStream = new DataInputStream(new FileInputStream(torrentFile));
			long fSize = torrentFile.length();

			if (fSize > Integer.MAX_VALUE || fSize < Integer.MIN_VALUE) {
				dataInputStream.close();
				throw new IllegalArgumentException(fSize + " is too large a torrent filesize for this program to handle");
			}
			byte[] torrentData = new byte[(int)fSize];
			dataInputStream.readFully(torrentData);
			TorrentInfo torrentInfo = new TorrentInfo(torrentData);

			dataInputStream.close();
			
			System.out.println("Successfully parsed torrent file.");
			
			return torrentInfo;
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found");
			return null;
		} catch (IOException e) {
			System.out.println("Error: Unable to interface with file");
			return null;
		} catch (IllegalArgumentException e) {
			System.out.println("Error: Illegal argument");
			return null;
		} catch (BencodingException e) {
			System.out.println("Error: Invalid torrent file specified");
			return null;
		}
	}
	
	/**
	 * Build the TCP connection with the tracker
	 * @param event: event key word that will be attaced on the GET request URL	
	 */
	public void update(String event) {
		try {
//			String url = "GET /announce? info_hash=%05z%f3%0b%f0%ab%13%c32%edX%7d%c8s%85%7b%adOPU&peer_id=%1a%89%1f%13%16%06s%1b%caZ%92%84%e9n%c7%60%ef%3fy%b6&port=6666&uploaded= 0&downloaded=0&left=2706181& compact=1&event=started HTTP/1.1";
			String url = createURL(this.metainfo, self_status.upload, self_status.download, self_status.left, event);
		
			HashMap<ByteBuffer, Object> response = null;

			byte[] trackerResponse = sendGETRecieveResponse(url, this.metainfo);

			if (trackerResponse == null) {
				System.out.println("Error communicating with tracker");
				System.exit(0);
			}
			String str_resp = new String(trackerResponse);
//			System.out.println("response = \n" + str_resp.split("\\n")[4]);
			// response code  = str_resp.split("\\n")[0]
			
			// response payload = str_resp.split("\\n")[4]
			String str_payload = str_resp.split("\\n")[4];
			
			
			if(!URTCPConnector.TCPResponseCodeChecker(str_resp.split("\\n")[0])) {
				System.out.println("Detail: " + str_payload);
				System.exit(0);
			}
			System.out.println("\n<--Communicate to Tracker Successfully-->\n");
			String bencode = str_payload.split("5:peers")[0]+"e";
			int peer_pair_num = Integer.parseInt(str_payload.split("5:peers")[1].split(":")[0]);
//			System.out.println("pair num: "+peer_pair_num);
			String peerinfo = str_payload.split("5:peers")[1];
			byte[] peer_pair = new byte[peer_pair_num];
//			System.out.println("peer info length: "+ str_payload.split("5:peers")[1].split(":")[1].length());
			System.arraycopy(str_payload.split("5:peers")[1].split(":")[1].getBytes(), 0, peer_pair, 0, peer_pair_num);
			TrackerResponseParser(bencode, this.announce);
			
			for(int i =0; (i<peer_pair.length-1); i = i + 6){
				//＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋＋
				String remote_peer_ip = peer_pair[i + 0] + "." + peer_pair[i + 1] + "." + peer_pair[i + 2] + "." + peer_pair[i + 3];
//				System.out.println("pe[i+4] = " +peer_pair[i+4] +"\npe[i+4] << 8= " + (peer_pair[i+4] << 8) );
//				System.out.println("pe[i+4] = " +peer_pair[i+5]);
//				System.out.println("pe[i+4] | pe[i+5] = " +(peer_pair[i + 4] << 8 | peer_pair[i +5]));
				int remote_peer_port = (peer_pair[i + 4] << 8 | peer_pair[i +5]);
				System.out.println((i/6)+" ip = "+remote_peer_ip + " port = "+remote_peer_port);
				boolean isExist = false;
				boolean isSelf = false;
//				System.out.println("self ip = " + this.ip + " self port = " + this.port);
				if(remote_peer_ip.equals(this.ip) && remote_peer_port == Integer.parseInt(this.port)) {
//					System.out.println("is self");
					isSelf = true;
				}
				for(URPeerInfo exist_peer : self_status.remote_peers) {
					if(exist_peer.ip.equals(remote_peer_ip) && exist_peer.port == (remote_peer_port)) {
						isExist = true;
						break;
					}
				}
				if(!isExist && !isSelf) {
					URPeerInfo remote_peerInfo = new URPeerInfo(remote_peer_ip ,remote_peer_port, this.metainfo.piece_hashes.length);
					this.self_status.remote_peers.add(remote_peerInfo);
				}
			}
			System.out.println("Announce:\n" + announce.toString());
			System.out.println("Remote Peers:\n" + this.self_status.showRemotePeers());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.out.println("Error decoding tracker response");
			System.exit(0);
		} catch (BencodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the url.
	 *
	 * @param announceURL the announce url
	 * @return the url
	 */
	private String createURL(TorrentInfo torrentInfo, int uploaded, int downloaded, int left, String event) throws MalformedURLException {
		String newURL ="GET /announce";
		newURL += "?info_hash=" + Utils.toHexString(torrentInfo.info_hash.array())
				+ "&peer_id=" + Utils.toHexString(this.id.getBytes()) + "&port="
				+ this.port + "&uploaded=" + uploaded + "&downloaded="
				+ downloaded + "&left=" + left + "&compact=1&event="+event+" HTTP/1.1";
		return newURL;
	}
	
	
	//================
	//Printer
	//================
	
	/**
	 * metainfo command call
	 */
	public void Print_Metainfo() {
		try {
			metainfo.TorrentInfoFileParser(torrent_file, id);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * announce command call
	 * @throws IOException
	 */
	public void Print_Announce() throws IOException{
		update("");
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
		System.out.println(this.ip + "    " + this.port);
		for(int i=0; i<self_status.remote_peers.size(); i++){
			System.out.print(self_status.remote_peers.get(i).ip+"    ");
			System.out.println(self_status.remote_peers.get(i).port);
		}
		System.out.println("...Tracker closed connection");
	}
	
	/**
	 * Trackerinfo command call
	 */
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
		System.out.println(this.ip + "    " + this.port);
//		System.out.println("127.0.0.1"+"    "+"9999");
		for(int i=0; i<self_status.remote_peers.size(); i++){
			System.out.print(self_status.remote_peers.get(i).ip+"    ");
			System.out.println(self_status.remote_peers.get(i).port);
		}
	}
	
	/**
	 * Status command call
	 */
	public void Print_Status() {
		System.out.println("----------------------------------");
		System.out.println("Peer Current Status\n----------------------------------");
		System.out.println("Downloaded: "+self_status.download
				+"\nUploaded: "+self_status.upload 
				+"\nLeft: "+self_status.left
				+"\nMy bit field: \n" 
				+ Utils.booleanAryToString(self_status.bitfield));
	}
	
	/**
	 * show command call
	 */
	public void Print_Connection() {
		System.out.println("----------------------------------");
		System.out.println("Peers Connection Status\n----------------------------------");
		System.out.println("Downloaded: "+self_status.download
				+"\nUploaded: "+self_status.upload
				+"\nLeft"+self_status.left
				+"\nMy bit field: "+Utils.booleanAryToString(self_status.bitfield));
	}
	
	/**
	 * Rarest first algorithm
	 * calculate the total number of each piece in the piecePrevalence array
	 * if there has more than one rarest pieces, select the first piece that is rarest
	 * 
	 * @return the piece index for this rarest piece
	 */
	private int RarestSelecte() {
		int selected_piece_number = 0;
		int min = 0;
		int min_index = 0;
		for(int i = 0; i< this.piecePrevalence.length; i++) {
			if(i == 0) {
				min = this.piecePrevalence[i];
			}
			else if(this.piecePrevalence[i] < min) {
				min = this.piecePrevalence[i];
				min_index = i;
			}
		}
		selected_piece_number = min_index;
		return selected_piece_number;
	}
	
	/**
	 * Randomly select a number from 0 to bound-1
	 * @return: the URPeerInfo object for a specific known peer
	 */
	private URPeerInfo RandomChoke() {
		int peer_num = this.self_status.remote_peers.size();
		Random rand = new Random();
		int peer_index = rand.nextInt(peer_num);
		return this.self_status.remote_peers.get(peer_index);
	}
	
	
	/**
	 * calculate the prevalence of the bitfield
	 * can be used to calculate which piece is the rarest piece
	 * 
	 * @param bitfield
	 */
	public void calPiecePrevalence(boolean[] bitfield) {
		for(int i = 0; i < bitfield.length; i++) {
			if(bitfield[i]) {
				this.piecePrevalence[i] ++;
			}
		}
	}
	
	
	/**
	 * calculate the start point of the specific piece
	 * 
	 * @param piece_index: index points to the specific location in the bitfield
	 * @return
	 */
	public int calPieceLocation(int piece_index) {
		int location = 0;
		// last piece
		if(piece_index == this.metainfo.piece_hashes.length) {
			location  = (this.metainfo.file_length - calPieceSize(piece_index));
		}
		else {
			location = this.metainfo.piece_length*(piece_index);
		}
		return location;
		
	}
	
	
	
	/**
	 * calculate the piece size according to the given piece_index 
	 * 
	 * @param piece_index: index points to the specific location in the bitfield
	 * @return
	 */
	public int calPieceSize(int piece_index) {	
		int size = 0;
		// last piece
		if(piece_index == this.metainfo.piece_hashes.length) {
			size = (this.metainfo.file_length - (piece_index * this.metainfo.piece_length)); //last block
		}
		else {
			size = this.metainfo.piece_length;
		}
		return size;

		
	}
	
	
	/**
	 * Check whether all the bitfield set to true
	 * if all true, it will change its role to seeder
	 * @return
	 */
	public boolean checkBitfield() {
		boolean result = true;
		for(int i = 0; i<bitfield.length; i++) {
			if(!bitfield[i]) {
				result = false;
				break;
			}
		}
		return result;
	}

}
