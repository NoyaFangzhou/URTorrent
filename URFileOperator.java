package URTorrent;

import java.io.*;

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
	 * Write something into the file
	 * @param fileName: the name of a file to be written
	 * @param addContent: the newly added content
	 */
	public static void FileWriter(String fileName, String addContent) {
		
	}

}
