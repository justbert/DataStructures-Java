package concordance;
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

	List<Paragraph> text;
	
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
			
			String paragraph;
			int index = 0;

			while((paragraph = input.readLine()) != null) { //watch for the end of file, by looking for null
				if(paragraph != "\r\n" && paragraph != "\n" && paragraph != "\r")
				text.add(new Paragraph(paragraph, ++index));
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
	public Iterator<Paragraph> getIterator() {
		return text.iterator();
	}
	
	public class Paragraph {
		
		private String text;
		private int index;
		
		public Paragraph(String paragraph, int index) {
			this.text = paragraph;
			this.index = index;
		}
		
		public String getText() {
			return this.text;
		}
		
		public String toString() {
			return this.index + ": " + text;
		}
	}
}
