package URTorrent;
import java.util.*;
import java.io.*;
import java.security.*;
import URTorrent.URBencode.*;

/**
 * Do the File Manipulation, include read and write files
 * @author Fangzhou_Liu
 *
 */
public class URFileOperator {
	
	/**
	 * Read and display all contents from file
	 * @param fileName: the name of a file to be opened.
	 */
	public static void FileReader(String fileName) {
		
		// This will reference one line at a time
		String line = null;
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line+"\n");
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
		
	}
	
	/**
	 * Read the file and return the content in a specific line
	 * @param fileName
	 * @param lineNumber
	 * @return
	 */
	public static String URFileLineReader(String fileName, int lineNumber) {
		// This will reference the line number
		int line_count = 0;
		// This will reference one line at a time
		String line = null;
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
            	if(line_count == lineNumber) {
            		return line;
            	}
                System.out.println(line+"\n");
                line_count ++;
            }
            // Always close files.
            bufferedReader.close();  
            return "ERROR! No such line!";
        }
        catch(FileNotFoundException ex) {
        	String error = "Unable to open file '" + fileName + "'";
            System.out.println(error);
            return error;
        }
        catch(IOException ex) {
        	String error ="Error reading file '" + fileName + "'";
            System.out.println(error);
            return error;                 
            // Or we could just do this: 
            // ex.printStackTrace();
        }
	}
	
	/**
	 * Write something into the file
	 * @param fileName: the name of a file to be written
	 * @param addContent: the newly added content
	 */
	public static void FileWriter(String fileName, String addContent) {
		
	}
	
	/**
	 * Parse the information hide behind the metainfo file
	 * show all key-value pairs
	 * list all piece hash info 
	 * @param fileName: the file name of the .torrent file
	 * @param port: port number that the peer listen to
	 * @param peerID: id of the current peer
	 */
	public static void MetaInfoFileParser(String fileName, String port, String peerID) {
		
		HashMap<String, String> result = new HashMap<String, String>();//key -> value
		ArrayList<String> piece_list = new ArrayList<String>();
		try {
			InputStream stream = new FileInputStream(fileName);
			//map{announce, info}
			BencodeMap metainfo = (BencodeMap)Bencode.parseBencode(stream);
			//info{length, name, piece_length, pieces}
			BencodeMap info = (BencodeMap)metainfo.get(new BencodeString("info"));
			//go over all key-val pairs for metainfo dict
			for(BencodeString key : metainfo.keySet()) {
				if(key.equals(new BencodeString(URMetaInfo.ANNOUNCE))) {
					result.put(URMetaInfo.ANNOUNCE, metainfo.get(key).toString());
				}
				else {
					result.put(URMetaInfo.INFO, URDataOperator.SEncode(info.toString()));
				}
			}
			//go over all key-val pairs for info sub-dict
			for(BencodeString key : info.keySet()) {
				if(!key.toString().equals(URMetaInfo.PIECES)) {
					result.put(key.toString(), info.get(key).toString());
				}
			}
			
			//get all piece 20byte hash
			piece_list = parsePieces((BencodeString)info.get(new BencodeString(URMetaInfo.PIECES)));
			
			//display the content
			System.out.println("-------------------------------------");
			System.out.println("- IP/port: "+Macro.LOCALHOST+"/"+port);
			System.out.println("- ID: "+peerID);
			System.out.println("- metainfo file: "+fileName);
			for(String key : result.keySet()) {
				System.out.println("- "+key + ": " + result.get(key));
			}
			System.out.println("- piece's hashes: ");
			for(int i = 0; i<piece_list.size(); i++) {
				System.out.println(i + " " + piece_list.get(i));
			}
		} catch (UnsupportedEncodingException e) {
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
	public static ArrayList<String> parsePieces(BencodeString pieces_hash) throws NoSuchAlgorithmException{
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
