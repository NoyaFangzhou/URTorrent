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
				
				String remote_ip = client_sock.getInetAddress().getHostAddress();
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
					for(URPeerInfo remote_peer: peer.getStatus().remote_peers) {
						if(remote_peer.ip.equals(remote_ip) && remote_peer.port == remote_port) {
							if(!remote_peer.AmChokingFlag) {
								remote_peer.AmChokingFlag = true;
								peer.request(remote_ip, remote_port);
								break;
							}
						}
					}
				}
				if(messagecontent[4] == 1) {
					System.out.println("--UnChock message--\n");
					peer.getStatus().PeerChokingFlag = false;
					for(URPeerInfo remote_peer: peer.getStatus().remote_peers) {
						if(remote_peer.ip.equals(remote_ip) && remote_peer.port == remote_port) {
							if(remote_peer.AmChokingFlag) {
								remote_peer.AmInterestedFlag = false;
								peer.request(remote_ip, remote_port);
								break;
							}
						}
					}
				}
				if(messagecontent[4] == 2) {
					System.out.println("--Interest message--\n");
					peer.getStatus().PeerInterestedFlag = true;
					for(URPeerInfo remote_peer: peer.getStatus().remote_peers) {
						if(remote_peer.ip.equals(remote_ip) && remote_peer.port == remote_port) {
							if(remote_peer.AmChokingFlag) {
								remote_peer.AmChokingFlag = false;
								remote_peer.AmInterestedFlag = true;
								peer.unchoked(remote_ip, remote_port);
								break;
							}
						}
					}
				}
				if(messagecontent[4] == 3) {
					System.out.println("--UnInterest message--\n");
					peer.getStatus().PeerInterestedFlag = false;
					for(URPeerInfo remote_peer: peer.getStatus().remote_peers) {
						if(remote_peer.ip.equals(remote_ip) && remote_peer.port == remote_port) {
							remote_peer.AmInterestedFlag = false;
							peer.getStatus().remote_peers.remove(remote_peer);// delete it from the remote_peer
							break;
						}
					}
				}
				if(messagecontent[4] == 4) {
					System.out.println("--Have message--\n");
					for(URPeerInfo remote_peer: peer.getStatus().remote_peers) {
						if(remote_peer.ip.equals(remote_ip) && remote_peer.port == remote_port) {
							byte[] piece_index_byte = new byte[4];
							System.arraycopy(messagecontent, 5, piece_index_byte, 0, 4);
							int piece_index = URDataOperator.byteToInteger(piece_index_byte);
							remote_peer.bitfield[piece_index] = true;
							break;
						}
					}
					TimeUnit.SECONDS.sleep(1);
				}
				if(messagecontent[4] == 5) {
					System.out.println("--BitField message--\n");
					byte[] remote_bitfield = new byte[messagecontent.length - 5];//get the remote bit field
					System.arraycopy(messagecontent, 5, remote_bitfield, 0, messagecontent.length-5);
					for(URPeerInfo remote_peer : peer.getStatus().remote_peers) {
						if(remote_peer.ip.equals(remote_ip) && remote_peer.port == remote_port) {
							if(remote_peer.MsgbitfieldFlag) {
								//set this peers bitfield
								remote_peer.bitfield = Utils.bitfieldToBoolArray(remote_bitfield);
								peer.calPiecePrevalence(remote_peer.bitfield);
								if(peer.getRole() == Macro.SEEDER) {
									//random choke7
									peer.getStatus().AmChokingFlag = false;
									peer.unchoked(remote_ip, remote_port);
									remote_peer.PeerChokingFlag = false;
								}
								else {
									peer.getStatus().AmInterestedFlag = true;
									peer.interest(remote_ip, remote_port);
									remote_peer.PeerInterestedFlag = true;
								}
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
					
//					int select_peer_index = RandomSelect(peer.getPeers().size());
//					URPeerInfo selected_peer = peer.getPeers().get(select_peer_index);
					continue;
				}
				if(messagecontent[4] == 6) {
					System.out.println("--Request message--\n");
					byte[] piece_index_byte = new byte[4];
					int piece_index = URDataOperator.byteToInteger(piece_index_byte);
					peer.piece(remote_ip, remote_port, piece_index);
					TimeUnit.SECONDS.sleep(1);
				}
				if(messagecontent[4] == 7) {
					System.out.println("--Piece message--\n");
					byte[] length_prefix_byte = new byte[4];
					System.arraycopy(messagecontent, 0, length_prefix_byte, 0, 4);
					byte[] index_byte = new byte[4];
					System.arraycopy(messagecontent, 5, index_byte, 0, 4);
					//length_prefix = 9 + L(block.length)
					int payload_length = URDataOperator.byteToInteger(length_prefix_byte);
					int piece_index = URDataOperator.byteToInteger(index_byte);
					byte[] recv_data = new byte[payload_length - 9];
					System.arraycopy(messagecontent, 13, recv_data, 0, recv_data.length);
					// Check Data Integrity
					if (Utils.verifySHA1(recv_data, peer.getMetainfo().piece_hashes[piece_index], piece_index)) {
						Utils.writeFile(piece_index, 0, recv_data.length, peer.getMetainfo(), recv_data, peer.getTorrent_file());
						// Update its download & left value
						peer.getStatus().download += recv_data.length;
						peer.getStatus().left -= recv_data.length;
						// Update its bitfield
						peer.setBitfield(Utils.checkPieces(peer.getMetainfo(), new File(peer.getTorrent_file())));
					}
					if (!peer.checkBitfield()) {
						peer.have(remote_ip, remote_port, piece_index);
					}
					else {
						//send uninterest message
						System.out.println("Finish Receiving!!!!!!");
						break;
					}
					TimeUnit.SECONDS.sleep(1);
				}
				else {
					System.out.println("--Handshake message--\n");
					for(URPeerInfo remote_peer: peer.getStatus().remote_peers) {
						System.out.println("remote peer: "+remote_peer.ip +" "+remote_peer.port);
						if(remote_peer.ip.equals(remote_ip) && remote_peer.port == remote_port) {
							System.out.println("--Served Peers--");
							if(!remote_peer.MsghandshakeFlag) {
								System.out.println("--Send handshake to Peer--");
								peer.handshake2(remote_ip, remote_port);
								remote_peer.MsghandshakeFlag = true; //已经发送过handshake
							}
							else {
								System.out.println("--Send bitfield to Peer--");
								System.arraycopy(messagecontent, 48, remote_peer.peerId, 0, 20);
								peer.bitfield(remote_ip, remote_port);
								remote_peer.MsgbitfieldFlag = true;	//已经发送过bitfield
							}
						}
						else {
							System.out.println("--Unserved Peers--");
//							peer.getPeers().add(new URPeerInfo(remote_ip, remote_port));//update the remote peer list
						}
						TimeUnit.SECONDS.sleep(1);
						break;
					}
				}
				
				in_stream.close();
	            client_sock.close();  
			}// while
		} catch (NumberFormatException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		peer.update("stop");
	}

}
