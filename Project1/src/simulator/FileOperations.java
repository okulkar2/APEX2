package simulator;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 *Class FileProcessor is used to open the file 
 *and read and return a line from the given 
 *File Name 
 *
*/
public class FileOperations {
	FileReader fileInput;
	BufferedReader bufferedInput;

	/**
	 *
	 *FileProcessor() Constructor is used to open
	 *the File. It returns FileNotFond Exception
	 *if the file is not found.
	 *
	 *@param: fileName- The name of the File to
	 *be opened
	 *@throws FileNotFoundException
	*/
	public FileOperations(String fileName){

		try{
			fileInput = new FileReader(fileName);
            bufferedInput = new BufferedReader(fileInput);
		}
		catch(FileNotFoundException ex) {
			System.err.println("Exception:\"" + fileName + "\" was not found. Please enter Correct filename.");
			System.err.println("Suggestion: Check for file extension or any Spelling mistake in File Name\n");
			System.exit(1);                
        	}
	}

	/**
	 *
	 *readLineFromFile() is used to read and return
	 *a line from the opened File.
	 *
	 *@return String. A single line from the file
	 *@throws IOException
	*/
	public String readLineFromFile(){
		String line = null;
		
		try{

			if((line = bufferedInput.readLine()) != null){
				return line;
			}
		}
		catch(NullPointerException ex) {
    		System.err.println("File is Blank");		 
    		System.exit(1);                    
		}
		catch(IOException ex) {
            System.err.println("Unable to read a line from file");		 
			System.exit(1);                    
        }

		return line;
	}

	/**
	 *
	 *closeFile() is used to close the file opened.
	 *
	 *@throws IOException
	*/
	public void closeFile(){
		
		try{

			bufferedInput.close();
		}
		catch(IOException ex) {
            		System.err.println("Error: Some problem while closing file");; 
			System.exit(1);                 
        	}
	}
}
