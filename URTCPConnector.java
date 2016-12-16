package URTorrent;

import java.io.*;
import java.net.*;

import URTorrent.URMessage.*;

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
	 * Send Handshake message to other peers.
	 * @param url: url of the peer
	 * @param port: port # of the peer
	 * @param info_hash: info hash of its own metainfo file
	 * @param id: 20byte id #
	 * @throws IOException
	 */
	public static void SendHandshake(String url, int port, byte[] handshake) throws IOException {
		
		Socket hsksocket = new Socket(url, port);  	
		System.out.println(handshake+"\n");
        //获取Socket的输出流，用来发送数据到服务端    
		//Get the output stream for Socket
		//Send handshake to peer
		DataOutputStream out_stream = new DataOutputStream(hsksocket.getOutputStream()); 
		out_stream.write(handshake);
        
        //获取Socket的输入流，用来接收从服务端发送过来的数据
        //Get the input stream for Socket
        //Receive response from peer
//        BufferedReader buf =  new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
//        
//        System.out.println(buf.readLine());  
        
//        buf.close();
        out_stream.close();
        hsksocket.close();
	}
	
	
	/**
	 * Send BitField message to other peers.
	 * @param url: url of the peer
	 * @param port: port # of the peer
	 * @param bitfield: bitfield message
	 * @throws IOException
	 */
	public static void SendBitField(String url, int port, byte[] bitfield) throws IOException {
		
		Socket btfsocket = new Socket(url, port);  	
		System.out.println(bitfield+"\n");
        //获取Socket的输出流，用来发送数据到服务端    
		//Get the output stream for Socket
		//Send bitfield to peer
		
		DataOutputStream out_stream = new DataOutputStream(btfsocket.getOutputStream()); 
		out_stream.write(bitfield);
        
        //获取Socket的输入流，用来接收从服务端发送过来的数据
        //Get the input stream for Socket
        //Receive response from peer
//        BufferedReader buf =  new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
//        
//        System.out.println(buf.readLine());  
//        
//        buf.close();
        out_stream.close();
        btfsocket.close();
		
	}
	
	/**
	 * Send UnChoke message to selected peers.
	 * @param url: url of the peer
	 * @param port: port # of the peer
	 * @param unchoked: unchoked message
	 * @throws IOException
	 */
	public static void SendUnChoked(String url, int port, byte[] unchoked) throws IOException {
		
		Socket btfsocket = new Socket(url, port);  	
		System.out.println(unchoked+"\n");
        //获取Socket的输出流，用来发送数据到服务端    
		//Get the output stream for Socket
		//Send bitfield to peer
		
		DataOutputStream out_stream = new DataOutputStream(btfsocket.getOutputStream()); 
		out_stream.write(unchoked);
        
        //获取Socket的输入流，用来接收从服务端发送过来的数据
        //Get the input stream for Socket
        //Receive response from peer
//        BufferedReader buf =  new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
//        
//        System.out.println(buf.readLine());  
//        
//        buf.close();
        out_stream.close();
        btfsocket.close();   
	}
	
	
	public static void SendInterest(String url, int port, byte[] interest) throws IOException {
		Socket btfsocket = new Socket(url, port);  	
		System.out.println(interest+"\n");
        //获取Socket的输出流，用来发送数据到服务端    
		//Get the output stream for Socket
		//Send bitfield to peer
		
		DataOutputStream out_stream = new DataOutputStream(btfsocket.getOutputStream()); 
		out_stream.write(interest);
        
        //获取Socket的输入流，用来接收从服务端发送过来的数据
        //Get the input stream for Socket
        //Receive response from peer
//        BufferedReader buf =  new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
//        
//        System.out.println(buf.readLine());  
//        
//        buf.close();
        out_stream.close();
        btfsocket.close(); 
	}
	
	
	public static void SendRequest(String url, int port, byte[] request) throws IOException {
		Socket btfsocket = new Socket(url, port);  	
		System.out.println(request+"\n");
        //获取Socket的输出流，用来发送数据到服务端    
		//Get the output stream for Socket
		//Send bitfield to peer
		
		DataOutputStream out_stream = new DataOutputStream(btfsocket.getOutputStream()); 
		out_stream.write(request);
        
        //获取Socket的输入流，用来接收从服务端发送过来的数据
        //Get the input stream for Socket
        //Receive response from peer
//        BufferedReader buf =  new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
//        
//        System.out.println(buf.readLine());  
//        
//        buf.close();
        out_stream.close();
        btfsocket.close(); 
	}
	
	public static void SendPieces(String url, int port, byte[] pieces) throws IOException {
		Socket btfsocket = new Socket(url, port);  	
		System.out.println(pieces+"\n");
        //获取Socket的输出流，用来发送数据到服务端    
		//Get the output stream for Socket
		//Send bitfield to peer
		
		DataOutputStream out_stream = new DataOutputStream(btfsocket.getOutputStream()); 
		out_stream.write(pieces);
        
        //获取Socket的输入流，用来接收从服务端发送过来的数据
        //Get the input stream for Socket
        //Receive response from peer
//        BufferedReader buf =  new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
//        
//        System.out.println(buf.readLine());  
//        
//        buf.close();
        out_stream.close();
        btfsocket.close(); 
	}
	
	public static void SendPieceData(String url, int port, byte[] data) throws IOException {
		Socket btfsocket = new Socket(url, port);  	
		System.out.println(data+"\n");
        //获取Socket的输出流，用来发送数据到服务端    
		//Get the output stream for Socket
		//Send bitfield to peer
		
		DataOutputStream out_stream = new DataOutputStream(btfsocket.getOutputStream()); 
		out_stream.write(data);
        
        //获取Socket的输入流，用来接收从服务端发送过来的数据
        //Get the input stream for Socket
        //Receive response from peer
//        BufferedReader buf =  new BufferedReader(new InputStreamReader(client_socket.getInputStream()));
//        
//        System.out.println(buf.readLine());  
//        
//        buf.close();
        out_stream.close();
        btfsocket.close(); 
	}

	
	
	
	
	
	
	
	
	
	
	
	public TorrentInfo parseTorrentFile(File torrentFileName) {
		try {
			DataInputStream dataInputStream = new DataInputStream(new FileInputStream(torrentFileName));
			long fSize = torrentFileName.length();

			if (fSize > Integer.MAX_VALUE || fSize < Integer.MIN_VALUE) {
				dataInputStream.close();
				throw new IllegalArgumentException(fSize + " is too large a torrent filesize for this program to handle");
			}

			byte[] torrentData = new byte[(int)fSize];
			dataInputStream.readFully(torrentData);
			TorrentInfo torrentInfo = new TorrentInfo(torrentData);

			dataInputStream.close();			
			return torrentInfo;
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found");
			return null;
		} catch (IOException e) {
			System.out.println("Error: Unable to interface with file");
			return null;
		} catch (IllegalArgumentException e) {
			System.out.println("Error: Illegal argument");
			return null;
		} catch (BencodingException e) {
			System.out.println("Error: Invalid torrent file specified");
			return null;
		}
	}
}
