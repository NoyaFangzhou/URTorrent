package URTorrent;

import java.io.*;
import java.net.*;

/**
 * TCPConnection Class
 * Used to do the TCP connections for Both Cliend/Server
 * @author Fangzhou_Liu
 *
 */
public class URTCPConnector {
	
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
	
	/**
	 * Build the TCP connection with the tracker
	 * @param url: ip_addr of the server
	 * @param port: port of the server
	 * @throws IOException
	 */
	public void TCPClientConnect(String url, int port) throws IOException {
		
		Socket client_socket = new Socket(url, port);  
	
        //获取Socket的输出流，用来发送数据到服务端    
		//Get the output stream for Socket
		//Send data to tracker
        PrintStream out = new PrintStream(client_socket.getOutputStream());  
        out.println("Hello World");
        
        //获取Socket的输入流，用来接收从服务端发送过来的数据
        //Get the input stream for Socket
        //Receive data from tracker
        BufferedReader buf =  new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
        
        System.out.println("Received from Server: " + buf.readLine());  
        
        buf.close();
        out.close();
        client_socket.close();
		
	}
	
	/**
	 * Build the TCP connection with the client
	 * @param port
	 * @throws IOException
	 */
	public void TCPServerConnect(int port) throws IOException {
		System.out.println("Server Open！....\n");
		ServerSocket serv_sock = new ServerSocket(port);
		Socket client_sock = null;
		boolean run_state = true;
		while(run_state) {
			client_sock = serv_sock.accept();  
			
            System.out.println("Client connected！....\n" + client_sock);
			//获取Socket的输出流，用来向客户端发送数据  
            PrintStream out = new PrintStream(client_sock.getOutputStream());  
            //获取Socket的输入流，用来接收从客户端发送过来的数据  
            BufferedReader buf = new BufferedReader(new InputStreamReader(client_sock.getInputStream()));
            
            System.out.println("Received from Client: "+buf.readLine());
            out.println("Hello World from Server!");
            client_sock.close();

            
		}
		
	}

}
