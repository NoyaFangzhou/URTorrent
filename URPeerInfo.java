/**
 * 
 */
package URTorrent;

import java.util.ArrayList;

/**
 *
 * @File URPeerInfo.java
 * @Time 2016-12-15 11:23:30 北美东部标准时间
 * @Author Fangzhou_Liu 
 * @Description: 
 */
public class URPeerInfo {

	public String ip;
	public int port;
	
	
	public boolean AmChokingFlag;
	public boolean AmInterestedFlag;
	public boolean PeerChokingFlag;
	public boolean PeerInterestedFlag;
	
	public boolean MsgChokeFlag;
	public boolean MsgInterestFlag;
	
	public boolean MsghandshakeFlag;
	public boolean MsgbitfieldFlag;
	
	public boolean[] bitfield;
	
	public int download;
	public int upload;
	public int left;
	
	public byte[] peerId;
	
	public ArrayList<URPeerInfo> remote_peers; //stores the status for each remote peer
	
	
	
	/**
	 * Constructor
	 */
	public URPeerInfo(String ip, int port, int piece_num) {
		// TODO Auto-generated constructor stub
		this.ip = ip;
		this.port = port;
		
		this.AmChokingFlag = true;
		this.PeerChokingFlag = false;
		this.AmInterestedFlag = false;
		this.PeerInterestedFlag = false;
		this.MsgChokeFlag = false;
		this.MsgInterestFlag = false;
		this.MsghandshakeFlag = false;
		this.MsgbitfieldFlag = false;
		
		this.remote_peers = new ArrayList<URPeerInfo>(Macro.MAX_PEERS);
		
		this.download = 0;
		this.upload = 0;
	}
	
	public String toString() {
		String header = "PeerInfo:\n";
		String body = "Peer ID: "+this.peerId +"\nbitfield: "+this.bitfield
				+"\nAmChokingFlag: "+ this.AmChokingFlag + "\nUnPeerChokingFlag: "+ this.PeerChokingFlag
				+"\nAmInterestedFlags: "+ this.AmInterestedFlag +"\nPeerInterestedFlag: "+this.PeerInterestedFlag
				+"\nMsgChokeFlag: "+this.MsgChokeFlag+"\nMsgInterestFlag: "+this.MsgInterestFlag
				+"\nMsghandshakeFlag: "+this.MsghandshakeFlag+"\nMsgbitfieldFlag: "+this.MsgbitfieldFlag
				+"\ndownload: "+this.download +"\nupload: "+this.upload + "\nleft: " + this.left;
		return header+body;
	}
	
	/**
	 * return all the remote peers info(ip address + port)
	 * @return the String format representation of all remote peers
	 */
	public String showRemotePeers() {
		String peers = "";
		for(int i= 0; i<this.remote_peers.size(); i++) {
			URPeerInfo remote = remote_peers.get(i);
			peers +="Peer "+i+": ["+ remote.ip +" "+remote.port+"];\n";
		}
		return peers;
	}

}
