/**
 * 
 */
package URTorrent.URMessage;

import URTorrent.Macro;

/**
 *
 * @File HandShakeMsg.java
 * @Time 2016-12-12 10:12:15 北美东部标准时间
 * @Author Fangzhou_Liu 
 * @Description: 
 */
public class HandShakeMsg implements URMessage {
	
	private int pstrlen;
	private String pstr;
	private byte[] reserved;
	private String hash;
	private String id;
	
	public HandShakeMsg(String hash, String id) {
		this.setPstrlen(Macro.HSK_PSTRLEN);
		this.setPstr(Macro.HSK_PSTR);
		this.setReserved(Macro.HSK_RESERVE.getBytes());
		this.hash = hash;
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see URMessage.URMessage#receive()
	 */
	@Override
	public void parse() {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the pstrlen
	 */
	public int getPstrlen() {
		return pstrlen;
	}

	/**
	 * @param pstrlen the pstrlen to set
	 */
	public void setPstrlen(int pstrlen) {
		this.pstrlen = pstrlen;
	}

	/**
	 * @return the pstr
	 */
	public String getPstr() {
		return pstr;
	}

	/**
	 * @param pstr the pstr to set
	 */
	public void setPstr(String pstr) {
		this.pstr = pstr;
	}

	/**
	 * @return the reserved
	 */
	public byte[] getReserved() {
		return reserved;
	}

	/**
	 * @param reserved the reserved to set
	 */
	public void setReserved(byte[] reserved) {
		this.reserved = reserved;
	}
	
	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param hash the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	@Override 
	public String toString() {
		return pstrlen + ";" + pstr + ";" + reserved + ";" + hash + ";" + id;
	}

}
