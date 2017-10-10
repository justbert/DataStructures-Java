package spellchecker;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/*FileName: TextFile.java
 *Assignment 
 *Course Name: CST8130 - Data Structures
 *LabSection: 301
 *Student Name: Justin Bertrand
 *Date: Feb 11, 2015
 *
 *Description: Loads up a CSV text file into memory.
 */

public class TextFile {

	List<String> text;
	
	/**
	 * Default constructor which initializes the list.
	 */
	public TextFile() {
		text = new LinkedList<>();
	}
	
	/**
	 * Loads the specified text file.
	 * @param file The file to be loaded.
	 */
	public void loadTextFile(File file) {
		
		double startTime = System.currentTimeMillis();
		
		try(BufferedReader input = Files.newBufferedReader(file.toPath());) {
			
			String word;

			while((word = input.readLine()) != null) { //watch for the end of file, by looking for null
				if(word != "\r\n" && word != "\n" && word != "\r")
				text.add(word);
			}
			
			double elapsedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
			System.gc();
			System.out.println("Text file loaded in : " + elapsedTime + " seconds.");
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns an Iterator to be used to go over the text file.
	 * @return An Iterator.
	 */
	public Iterator<String> getIterator() {
		return text.iterator();
	}
}
