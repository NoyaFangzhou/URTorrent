package URTorrent;
/**
 * TCPConnection Class
 * Used to do the TCP connections for Both Cliend/Server
 * @author Fangzhou_Liu
 *
 */
public class URTCPConnector {
	
	public static void TCPClientConnect(String message) {
		
	}
	
	public static void TCPServerConnect(String message) {
		
	}
	
	/**
	 * Check whether the response code is 2XX or not
	 * If is 2XX, it indicate a success request, 
	 * otherwise,a fail request or some error occurs
	 * @param resp :the first line of the response.
	 * @return: the check result
	 */
	public static boolean TCPResponseCodeChecker(String resp) {
		boolean isSucc = true;
		int resp_code = Integer.parseInt(resp.split(" ")[1]);
		if((int)resp_code/100 != 2) {
			isSucc = false;
		}
		return isSucc;
	}

}
