package URTorrent;
import java.net.*;
import java.io.*;

/**
 * Main Functional Class
 * Contains all Peer actions here
 * 
 * @author Fangzhou_Liu
 *
 */
public class URPeer {
	
	/**
	 * Run the URPeer
	 */
	private void run(String command) {
		
	}
	
	/**
	 * Build the TCP connection with the tracker
	 */
	private void TCPConnect(String url, int port) throws IOException {
		
		Socket client_socket = new Socket(url, port);  
	
        //获取Socket的输出流，用来发送数据到服务端    
		//Get the output stream for Socket
		//Send data to tracker
        PrintStream out = new PrintStream(client_socket.getOutputStream());  
        //获取Socket的输入流，用来接收从服务端发送过来的数据
        //Get the input stream for Socket
        //Receive data from tracker
        BufferedReader buf =  new BufferedReader(new InputStreamReader(client_socket.getInputStream())); 
        
        
        
        
        
        
		
	}
	
	
	

}
