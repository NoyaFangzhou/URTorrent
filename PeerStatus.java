/**
 * 
 */
package URTorrent;
import java.net.*;
import java.util.Arrays;
/**
 *
 * @File PeerObject.java
 * @Time 2016-12-15 01:58:35 北美东部标准时间
 * @Author Fangzhou_Liu 
 * @Description: 
 * Used to store the status of each peers
 */
public class PeerStatus {
	
	public boolean AmChokingFlag;
	public boolean AmInterestedFlag;
	public boolean PeerChokingFlag;
	public boolean PeerInterested;
	
	public boolean MsgChokeFlag;
	public boolean MsgInterestFlag;
	
	public boolean MsghandshakeFlag;
	public boolean MsgbitfieldFlag;
	
	public boolean[] bitfield;
	
	public int download;
	public int upload;
	
	public int left;
	
	public byte[] peerId;
	
	public PeerStatus(int bitfield_length) {
		this.AmChokingFlag = true;
		this.AmInterestedFlag = false;
		this.PeerChokingFlag = true;
		this.PeerInterested = false;
		
		this.MsgChokeFlag = false;
		this.MsgInterestFlag = false;
		
		this.MsghandshakeFlag = false;
		this.MsgbitfieldFlag = false;
		
		this.bitfield = new boolean[bitfield_length];
		Arrays.fill(this.bitfield, false);	
		this.download = 0;
		this.upload = 0;
		this.left = 0;
	}
	
	public String bitfield() {
		String btf = "";
		for(int i = 0; i<this.bitfield.length; i++) {
			if(this.bitfield[i]) {
				btf += 1+"";
			}
			else {
				btf += 0+"";
			}
		}
		return btf;
	}
	
	public String status() {
		return ""+this.AmChokingFlag+this.AmInterestedFlag+this.PeerChokingFlag+this.PeerInterested;
	}

}
