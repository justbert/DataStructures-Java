/**FileName: SpellCheckerTester.java
 *Assignment 
 *Course Name: CST8130 - Data Structures
 *LabSection: 301
 *Student Name: Justin Bertrand
 *Date: Feb 24, 2015
 *
 *Description: A JUnit testing class to test the Spellchecker and it's classes.
 */
package concordance;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Formatter;
import java.util.Iterator;

import org.junit.Test;

/**
 * @author Justin
 *
 */
public class ConcordanceTesterHash {

	/**
	 * Test method for {@link spellchecker.Spellchecker#createList()}.
	 * Tests the TextFile loading of a CSV file. Outputs to a file for
	 * visual confirmation, but also tests directly within the function.
	 */
	@Test
	public void testSpellcheckerCreateList() {
		Dictionary dictionary = new Dictionary(new File("Main.txt"));
		File file = new File("Oliver.txt");
		TextFile text = new TextFile();
		text.loadTextFile(file);
		Concordance concordance = new Concordance(dictionary, text);
		concordance.createConcordance();
		
		try(BufferedReader input = Files.newBufferedReader(file.toPath());
				Formatter output = new Formatter(new FileWriter(new File("testCreateList_output.txt")))) {
			
			String inputLine;
			String[] array; 
					
			while((inputLine = input.readLine()) != null) { //watch for the end of file, by looking for null
				array = inputLine.split("[^a-zA-Z']+");
				
				for(int index = 0; index < array.length; ++index) {
					
					//Eliminates empty strings causing false errors.
					if(array[index].equalsIgnoreCase("")) {
						continue;
					}
					
					if(!concordance.words.containsKey(concordance.new SpellCheckedWord(array[index], true))) {
						System.out.println("*"+array[index]);
						fail("Word is not in Concordance.");
					}
				}
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * A test case for the Dictionary loading. Tests to make sure all words have
	 * have been loaded. Does not need to test for duplicates since the TreeSet
	 * implements the Set interface which does not keep duplicates. Does not need 
	 * to be tested for order since a Tree must be in order.
	 */
	@Test
	public void testDictionaryLoad() {
		File file = new File("Main.txt");
		Dictionary dictionary = new Dictionary(file);
		
		try(BufferedReader input = Files.newBufferedReader(file.toPath())) {
			String word;
			
			while((word = input.readLine()) != null) { //watch for the end of file, by looking for null
				if(!dictionary.contains(word)) {
					fail("Word not in dictionary");
				}
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
