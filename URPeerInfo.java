/**
 * 
 */
package URTorrent;

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
	
	
	public boolean ChokeFlag;
	public boolean UnChokeFlag;
	public boolean InterestFlag;
	public boolean UninterestFlag;
	
	public boolean MsgChokeFlag;
	public boolean MsgInterestFlag;
	
	public boolean MsghandshakeFlag;
	public boolean MsgbitfieldFlag;
	
	public boolean[] bitfield;
	
	public int download;
	public int upload;
	
	public byte[] peerId;
	/**
	 * 
	 */
	public URPeerInfo(String ip, int port) {
		// TODO Auto-generated constructor stub
		this.ChokeFlag = true;
		this.UnChokeFlag = false;
		this.InterestFlag = false;
		this.UninterestFlag = false;
		this.MsgChokeFlag = false;
		this.MsgInterestFlag = false;
		this.MsghandshakeFlag = false;
		this.MsgbitfieldFlag = false;
		
		this.download = 0;
		this.upload = 0;
	}
	
	public String toString() {
		String header = "PeerInfo:\n";
		String body = "Peer ID: "+this.peerId +"\nbitfield: "+this.bitfield
				+"\nChokeFlag: "+ this.ChokeFlag + "\nUnChokeFlag: "+ this.UnChokeFlag
				+"\nInterestFlag: "+ this.InterestFlag +"\nUnInterestFlag: "+this.UninterestFlag
				+"\nMsgChokeFlag: "+this.MsgChokeFlag+"\nMsgInterestFlag: "+this.MsgInterestFlag
				+"\nMsghandshakeFlag: "+this.MsghandshakeFlag+"\nMsgbitfieldFlag: "+this.MsgbitfieldFlag
				+"\ndownload: "+this.download +"\nupload: "+this.upload;
		return header+body;
	}
	
	public String bitfield() {
		String btf = "";
		for(int i = 0; i<this.bitfield.length; i++) {
			btf += this.bitfield[i]+"";
		}
		return btf;
	}
	

}
