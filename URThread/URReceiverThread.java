/**
 * 
 */
package URTorrent.URThread;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import URTorrent.*;

/**
 *
 * @File URReceiverThread.java
 * @Time 2016-12-14 01:28:17 北美东部标准时间
 * @Author Fangzhou_Liu 
 * @Description: 
 */
public class URReceiverThread extends Thread {
	
	private int port; //peers own port
	
	private URPeer peer;
	
	public URReceiverThread(int port, URPeer peer) {
		this.port = port;
		this.peer = peer;
	}
	
	/**
	 * Main operations for this receiver thread
	 */
	public void run() {
		System.out.println("Server Open！....\n");
		ServerSocket serv_sock;
		try {
			serv_sock = new ServerSocket(port);
			Socket client_sock = null;
			boolean run_state = true;
			while(run_state) {
				client_sock = serv_sock.accept(); 
				
				String remote_ip = client_sock.getInetAddress()+"";
				int remote_port = client_sock.getPort();
				System.out.println("Receive message from other peers!");
				System.out.println("Peer IP: "+ remote_ip);
				System.out.println("Peer port:" + remote_port);
				DataInputStream in_stream = new DataInputStream(client_sock.getInputStream());
				byte[] messagecontent = new byte[Macro.PIECE_SIZE];
				int message_length = in_stream.read(messagecontent, 0, Macro.PIECE_SIZE); 
				
				//check message id
				if(messagecontent[4] == 0) {
					System.out.println("--Chock message--\n");
					peer.getStatus().PeerChokingFlag = true;
					for(URPeerInfo remote_peer: peer.getPeers()) {
						if(remote_peer.ip.equals(remote_ip) && remote_peer.port == remote_port) {
							if(!remote_peer.ChokeFlag) {
								remote_peer.InterestFlag = true;
								peer.request(remote_ip, remote_port);
							}
						}
					}
					continue;
				}
				if(messagecontent[4] == 1) {
					System.out.println("--UnChock message--\n");
					peer.getStatus().PeerChokingFlag = false;
					for(URPeerInfo remote_peer: peer.getPeers()) {
						if(remote_peer.ip.equals(remote_ip) && remote_peer.port == remote_port) {
							if(!remote_peer.ChokeFlag) {
								remote_peer.InterestFlag = true;
								peer.request(remote_ip, remote_port);
								break;
							}
						}
					}
					continue;
				}
				if(messagecontent[4] == 2) {
					System.out.println("--Interest message--\n");
					peer.getStatus().PeerInterested = true;
					continue;
				}
				if(messagecontent[4] == 3) {
					System.out.println("--UnInterest message--\n");
					peer.getStatus().PeerInterested = false;
					continue;
				}
				if(messagecontent[4] == 4) {
					System.out.println("--Have message--\n");
					TimeUnit.SECONDS.sleep(1);
					continue;
				}
				if(messagecontent[4] == 5) {
					System.out.println("--BitField message--\n");
					byte[] remote_bitfield = new byte[messagecontent.length - 5];//get the remote bit torrent
					System.arraycopy(messagecontent, 4, remote_bitfield, 0, messagecontent.length-5);
					for(URPeerInfo remote_peer : peer.getPeers()) {
						if(remote_peer.ip.equals(remote_ip) && remote_peer.port == remote_port) {
							if(remote_peer.MsgbitfieldFlag) {
								//set this peers bitfield
								remote_peer.bitfield = UtilTools.bitfieldToBoolArray(remote_bitfield, remote_bitfield.length);
								if(peer.getRole() == Macro.SEEDER) {
									peer.getStatus().AmChokingFlag = false;
									peer.unchoked(remote_ip, remote_port);
									remote_peer.ChokeFlag = false;
								}
								else {
									peer.getStatus().AmInterestedFlag = true;
									peer.interest(remote_ip, remote_port);
									remote_peer.UninterestFlag = false;
								}
								break;
							}
							else {
								//announce its own bitfield
								peer.bitfield(remote_ip, remote_port);
								remote_peer.MsgbitfieldFlag = true;
							}
							break;
						}
					}
					TimeUnit.SECONDS.sleep(1);
					
					int select_peer_index = RandomSelect(peer.getPeers().size());
					URPeerInfo selected_peer = peer.getPeers().get(select_peer_index);
					continue;
				}
				if(messagecontent[4] == 6) {
					System.out.println("--Request message--\n");
					TimeUnit.SECONDS.sleep(1);
					continue;
				}
				if(messagecontent[4] == 7) {
					System.out.println("--Piece message--\n");
					TimeUnit.SECONDS.sleep(1);
					continue;
				}
				else {
					System.out.println("--Handshake message--\n");
					for(URPeerInfo remote_peer: peer.getPeers()) {
						if(remote_peer.ip.equals(remote_ip) && remote_peer.port == remote_port) {
							if(!remote_peer.MsghandshakeFlag) {
								peer.handshake2(remote_ip, remote_port);
								remote_peer.MsghandshakeFlag = true;
							}
							else {
								System.arraycopy(messagecontent, 48, remote_peer.peerId, 0, 20);
								peer.bitfield(remote_ip, remote_port);
								remote_peer.MsgbitfieldFlag = true;	
							}
						}
						else {
							System.out.println("--Unserved Peers--");
//							peer.getPeers().add(new URPeerInfo(remote_ip, remote_port));//update the remote peer list
						}
						TimeUnit.SECONDS.sleep(1);
					}
				}
				
				in_stream.close();
	            client_sock.close();    
			}
		} catch (NumberFormatException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Randomly select a number from 0 to bound-1
	 * @param bound: the bound of this selection
	 * @return: the random number that indicate a specific peer
	 */
	private static int RandomSelect(int bound) {
		Random rand = new Random();
		return rand.nextInt(bound);
	}

}
