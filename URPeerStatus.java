package URTorrent;

/**
 *
 * @File URPeerStatus.java
 * @Time 2016-12-07 10:03:52
 * @Author Fangzhou_Liu 
 * @Description: 
 */
public class URPeerStatus {
	//the client is choking the peer
	//its message will be ignored by the peer
	public static int AM_CHOKING = 1;
		
	//the client is interested in the peer
	//its message will be receive and handled b the peer
	public static int AM_INTERESTED = 1;
		
	//the peer is choking this client
	public static int PEER_CHOKING = 1;
		
	//the peer is interested in this client
	public static int PEER_INTERESTED = 1;

}
