package spellchecker;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/*FileName: Dictionary.java
 *Assignment 
 *Course Name: CST8130 - Data Structures
 *LabSection: 301
 *Student Name: Justin Bertrand
 *Date: Feb 11, 2015
 *
 *Description: A dictionary which imports in a CSV file of words.
 */

public class Dictionary {
	
	private TreeSet<String> words;
	
	/**
	 * Constructor which allows the loading in of a file.
	 * @param file A CSV file of words to be loaded in.
	 */
	public Dictionary(File file) {
		this();
		this.loadFile(file);
	}
	
	/**
	 * Default constructor which initializes the TreeSet to use 
	 * the String.CASE_INSENSITIVE_ORDER Comparator.
	 */
	public Dictionary() {
		this.words = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
	}
	
	/**
	 * Adds a new word to the dictionary.
	 * @param word A String which is added to the dictionary.
	 */
	public void addWord(String word) {
		words.add(word);
	}
	
	/**
	 * Returns True if the dictionary contains the inputed word, false if not.
	 * @param word A String to be passed to the dictionary.
	 * @return A boolean representing if the word is in the dictionary.
	 */
	public boolean contains(String word) {
		return words.contains(word);
	}
	
	/**
	 * Allows the loading in of a CSV file(separated by carriage returns).
	 * @param file A CSV file of words to be loaded in.
	 */
	public void loadFile(File file) {
		
		double startTime = System.currentTimeMillis();
		try(BufferedReader input = Files.newBufferedReader(file.toPath());) {
			String word;
			
			while((word = input.readLine()) != null) { //watch for the end of file, by looking for null
				words.add(word);
			}
					
			double elapsedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
			
			System.gc();
			System.out.println("Dictionary loaded in : " + elapsedTime + " seconds.");
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns 10 words that are closest matches to the inputed word. If inputed word is
	 * in the dictionary, also adds that words to the list, returning a list of 11 words.
	 * @param word A word used to seed the list.
	 * @return A List of close matches.
	 */
	public List<String> closeMatches(String word) {
		
		double startTime = System.currentTimeMillis();
		List<String> list = new ArrayList<>(11);	
		String higher = word;
		String lower = word;
		int index = 0;
		
		if(words.contains(word))
		list.add(word);
		
		higher = words.higher(higher);
		lower = words.lower(lower);
		while(index < 5) {
			if(higher != null) {
				list.add(higher);
				higher = words.higher(higher);
			} else if(lower != null) {
				list.add(lower);
				lower = words.lower(lower);
			} else {
				list.sort(String.CASE_INSENSITIVE_ORDER);
				return list;
			}
			++index;
		}
		
		while(index < 10) {
			if(lower != null) {
				list.add(lower);
				lower = words.lower(lower);
			} else if(higher != null) {
				list.add(higher);
				higher = words.higher(higher);
			} else {
				list.sort(String.CASE_INSENSITIVE_ORDER);
				return list;
			}
			++index;
		}
		
		list.sort(String.CASE_INSENSITIVE_ORDER);
		double elapsedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
		System.out.println("Close matches loaded in : " + elapsedTime + " seconds.");
		return list;
	}
}
