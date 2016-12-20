/**
 * 
 */
package URTorrent;

/**
 *
 * @File URAnnounce.java
 * @Time 2016-12-15 08:30:17 北美东部标准时间
 * @Author Fangzhou_Liu 
 * @Description: 
 */
public class URAnnounce {
	
	public static String INTERVAL = "interval";
	
	public static String MIN_INTERVAL = "min interval";
	
	public static String COMPLETE = "complete";
	
	public static String INCOMPLETE = "incomplete";
	
	public static String DOWNLOADED = "downloaded";
	
	private int min_interval;
	
	private int interval;
	
	private int download;
	
	private int complete;
	
	private int incomplete;
	
	public URAnnounce() {
		this.min_interval = 0;
		this.interval = 0;
		this.download = 0;
		this.complete = 0;
		this.incomplete = 0;
	}

	/**
	 * @return the min_interval
	 */
	public int getMin_interval() {
		return min_interval;
	}

	/**
	 * @param min_interval the min_interval to set
	 */
	public void setMin_interval(int min_interval) {
		this.min_interval = min_interval;
	}

	/**
	 * @return the interval
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * @param interval the interval to set
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * @return the download
	 */
	public int getDownload() {
		return download;
	}

	/**
	 * @param download the download to set
	 */
	public void setDownload(int download) {
		this.download = download;
	}

	/**
	 * @return the complete
	 */
	public int getComplete() {
		return complete;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "URAnnounce [min_interval=" + min_interval + ", interval=" + interval + ", download=" + download
				+ ", complete=" + complete + ", incomplete=" + incomplete + "]";
	}

	/**
	 * @param complete the complete to set
	 */
	public void setComplete(int complete) {
		this.complete = complete;
	}

	/**
	 * @return the incomplete
	 */
	public int getIncomplete() {
		return incomplete;
	}

	/**
	 * @param incomplete the incomplete to set
	 */
	public void setIncomplete(int incomplete) {
		this.incomplete = incomplete;
	}
	
	
	
}
