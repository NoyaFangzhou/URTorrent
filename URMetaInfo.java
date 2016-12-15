package URTorrent;

import java.util.*;
import java.io.*;
import java.security.*;
import URTorrent.URBencode.*;

/**
 * Contains all keys in the metafile dictionary
 * also contains all keys in the info sub dictionary
 * @author Fangzhou_Liu
 *
 */
public class URMetaInfo {
	
	//in metainfo dict
	public static String ANNOUNCE = "announce";
	
	public static String INFO = "info";
	
	//in info sub-dict
	public static String LENGTH = "length";
	
	public static String NAME = "name";
	
	public static String PIECE_LENGTH = "piece length";
	
	public static String PIECES = "pieces";// announce 
	
	private String announce;
	
	private byte[] info_hash;
	// info dict
	private HashMap<String, String> info = new HashMap<String, String>();//key -> value
	// array list of pieces
	private ArrayList<String> piece_list = new ArrayList<String>();
	
	public URMetaInfo(String filename) {
		parseHash(filename);
	}
	
	public String getAnnounce() {
		return this.announce;
	}
	
	public HashMap<String, String>getInfo() {
		return this.info;
	}
	
	public String getInfo_hash() throws NoSuchAlgorithmException {
		return URDataOperator.SEncode(info.toString());
	}
	
	public ArrayList<String> getPieces() {
		return this.piece_list;
	}
	
	private void parseHash(String fileName) {
		try {
			InputStream stream = new FileInputStream(fileName);
			//map{announce, info}
			BencodeMap metainfo = (BencodeMap)Bencode.parseBencode(stream);
			//info{length, name, piece_length, pieces}
			BencodeMap info = (BencodeMap)metainfo.get(new BencodeString("info"));
			//go over all key-val pairs for metainfo dict
			for(BencodeString key : metainfo.keySet()) {
				if(key.equals(new BencodeString(URMetaInfo.ANNOUNCE))) {
					this.announce = metainfo.get(key).toString();
				}
				else {
					this.info.put(URMetaInfo.INFO, URDataOperator.SEncode(info.toString()));
				}
			}
			//go over all key-val pairs for info sub-dict
			for(BencodeString key : info.keySet()) {
				if(!key.toString().equals(URMetaInfo.PIECES)) {
					this.info.put(key.toString(), info.get(key).toString());
				}
			}
			
			//get all piece 20byte hash
			this.piece_list = parsePieces((BencodeString)info.get(new BencodeString(URMetaInfo.PIECES)));
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * separate the concatenate piece hashes 20 bytes each
	 * transfer their format to hex value
	 * add these piece hash data into an ArrayList
	 * 
	 * @param pieces_hash: concatenated info hashes for all pieces
	 * @return the ArrayList of the hashes for each pieces, each element represent a info hash for one piece
	 * @throws NoSuchAlgorithmException
	 */
	private ArrayList<String> parsePieces(BencodeString pieces_hash) throws NoSuchAlgorithmException{
//		int piece_num = (total_length%piece_length == 0 ? (int)total_length/piece_length : (int)total_length/piece_length +1 );
		ArrayList<String> pieces = new ArrayList<String>();//pieces(in hash) list
		int piece_length = pieces_hash.getBytes().length;
//		TestUtil.print(piece_length+"");
		int total_piece = (int)piece_length/Macro.SHA1_LENGTH;
		for(int i = 0; i<total_piece; i++) {
			pieces.add(URDataOperator.toHex(URDataOperator.getPieceSHA1(i*Macro.SHA1_LENGTH, pieces_hash.getBytes())));
		}
		
		return pieces;
	}
	
	


}
