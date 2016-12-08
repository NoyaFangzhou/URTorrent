package URTorrent;

import java.io.*;
import java.security.*;
import java.util.*;

import URTorrent.URBencode.*;

public class Test {
	public static void main(String args[]) {
//		URFileOperator.FileReader("UR.mp3.torrent");
//		System.out.println(URFileOperator.URFileLineReader("UR.mp3.torrent", 0));
		
//		System.out.println(URTCPConnector.TCPResponseCodeChecker("HTTP/1.0 200 OK"));
		try {
//			InputStream stream = new ByteArrayInputStream("d8:completei0e10:downloadedi0e10:incompletei1e8:intervali1638e12:min intervali819e5:peersd8:completei0e10:downloadedi0e10:incompletei1e8:intervali1638e12:min intervali819e5:peers0:ee".getBytes("UTF-8"));
			InputStream stream = new FileInputStream("UR.mp3.torrent");
			BencodeMap map = (BencodeMap)Bencode.parseBencode(stream);
//			System.out.println(map);
//			System.out.println(map.get(new BencodeString("info")));
//			BencodeMap info = (BencodeMap)map.get(new BencodeString("info"));
//			System.out.println(map.keySet()+"\n");
			
//			for(BencodeString key : map.keySet()) {
//				System.out.println("\n"+key);
//				System.out.print(map.get(key));
//			}
//			
//			for(BencodeString key : info.keySet()) {
//				System.out.println("\n"+key);
//				if(!key.equals(new BencodeString(URMetaInfo.PIECES))) {
//					System.out.print(info.get(key));
//				}
//			}
//			System.out.println(info.get(new BencodeString(URMetaInfo.PIECES)).getClass());
//			ArrayList<String> piece_list = URFileOperator.parsePieces((BencodeString)info.get(new BencodeString(URMetaInfo.PIECES)));
//			
//			for(int i = 0; i<piece_list.size(); i++) {
//				System.out.println("piece "+i+" "+piece_list.get(i)+"\n");
//			}
			URFileOperator.MetaInfoFileParser("UR.mp3.torrent", "6969", URPeerIDGenerator.getPeerID());
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
