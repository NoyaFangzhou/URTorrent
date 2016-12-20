/**
 * 
 */
package URTorrent;
import java.nio.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.net.*;
import java.io.*;
import URTorrent.URMessage.*;
/**
 *
 * @File Test.java
 * @Time 2016-12-15 07:26:58 北美东部标准时间
 * @Author Fangzhou_Liu 
 * @Description: 
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port = 9998;
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
				}
				if(messagecontent[4] == 1) {
					System.out.println("--UnChock message--\n");
				}
				if(messagecontent[4] == 2) {
					System.out.println("--Interest message--\n");
				}
				if(messagecontent[4] == 3) {
					System.out.println("--UnInterest message--\n");
				}
				if(messagecontent[4] == 4) {
					System.out.println("--Have message--\n");
					TimeUnit.SECONDS.sleep(1);
				}
				if(messagecontent[4] == 5) {
					System.out.println("--BitField message--\n");
					TimeUnit.SECONDS.sleep(1);
				}
				if(messagecontent[4] == 6) {
					System.out.println("--Request message--\n");
					TimeUnit.SECONDS.sleep(1);
				}
				if(messagecontent[4] == 7) {
					System.out.println("--Piece message--\n");
					TimeUnit.SECONDS.sleep(1);
				}
				else {
					System.out.println("--Handshake message--\n");
					TimeUnit.SECONDS.sleep(1);
					break;
				}
				in_stream.close();
	            client_sock.close();
			}
		} catch (NumberFormatException | IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}

}
