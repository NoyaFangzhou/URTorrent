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
	 * @param pos: the position of the content being add
	 */
	public static void FileWriter(String fileName, String addContent, int pos) {
		
	}
	
	public static String getInfoHash(String fileName) {
		
		String info_hash = null;
		return info_hash;
	}

}
