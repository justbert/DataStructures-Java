package spellchecker;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;

/*FileName: Spellchecker.java
 *Assignment 
 *Course Name: CST8130 - Data Structures
 *LabSection: 301
 *Student Name: Justin Bertrand
 *Date: Feb 14, 2015
 *
 *Description: A spellchecker which uses a dictionary to make sure
 *				that words in TextFile are spelled correctly.
 */

/**
 * @author Justin
 *
 */
public class Spellchecker {

	Dictionary dictionary;
	TextFile text;
	List<SpellCheckedWord> words;
	
	/**
	 * Constructor which takes in a Dictionary, and a TextFile.
	 * @param dict
	 * @param text
	 */
	public Spellchecker(Dictionary dict, TextFile text) {
		this.dictionary = dict;
		this.text = text;
		this.words = new LinkedList<SpellCheckedWord>();
	}
	
	/**
	 * Default constructor which initializes values.
	 */
	public Spellchecker() {
		this(new Dictionary(), new TextFile());
	}
	
	/**
	 * A parser to remove punctuation and special characters.
	 * @param word A word which needs to be parsed.
	 * @return The passed in word without punctuation or special characters.
	 */
	private String parse(String word) {

		//Pattern pattern = Pattern.compile("\"?([[A-Z][a-z]']+)[\\(\\).,:;?!\"]?[\\(\\).,:;?!\"]?\"?");
		//Pattern pattern = Pattern.compile("\"?([[A-Z][a-z]]*)\\p{Punct}+?\"?");
		Pattern pattern = Pattern.compile("\"*([[A-Z][a-z]']*)\\p{Punct}*");
		Matcher matcher = pattern.matcher(word);

		if (matcher.find()) {
		    return matcher.group(1);
		} else {
			return word;
		}
	}
	
	/**
	 * Returns the list of spellchecked words.
	 * @return List of spellchecked words.
	 */
	public List<SpellCheckedWord> getList() {
		return this.words;
	}
	
	/**
	 * Creates the list of spellchecked words.
	 */
	public void createList() {
		double startTime = System.currentTimeMillis();
		
		Iterator<String> iterator = text.getIterator();
		String[] sArray;
		
		while(iterator.hasNext()) {	
			sArray = iterator.next().split("[^a-zA-Z']+");
			//sArray = iterator.next().split("\\s+|--");
			
			for(int index = 0; index < sArray.length; ++index) {
				
				String word = sArray[index].trim();
				
//				if(word.contains("\"") || word.contains(".") || word.contains(",") || word.contains("?") 
//						|| word.contains("!") || word.contains(";")|| word.contains(":") || word.contains("(")
//						|| word.contains(")"))
//				word = parse(sArray[index]);
				
				/*if(word != null && !dictionary.contains(word)) {
					misspeltWords.add(word);
				} else {
					if(word == null)
					correctWords.add("null");
					else
					correctWords.add(word);
				}*/
				
//				if(!dictionary.contains(word)) {
//					words.add(new SpellCheckedWord(word, false));
//				} else {
//					words.add(new SpellCheckedWord(word, true));
//				}
				
				words.add(new SpellCheckedWord(word, dictionary.contains(word)));
				
			}
		}
		
		double elapsedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
		System.gc();
		System.out.println("Misspelled word list created in : " + elapsedTime + " seconds.");
	}
	
	/**
	 * @author Justin
	 * A class representing a word that has been spellchecked.
	 */
	public class SpellCheckedWord {
		
		private boolean correct;
		private String word;
		
		/**
		 * A contructor which takes in a word and a boolean representing if it's correctly spelled or not.
		 * @param word A word.
		 * @param correct A boolean representing if the word is correctly spelled or not.
		 */
		public SpellCheckedWord(String word, boolean correct) {
			this.word = word;
			this.correct = correct;
		}
		
		/**
		 * Returns a boolean representing if the word is correctly spelled. 
		 * @return Boolean representing if the Word is correctly spelled or not.
		 */
		public boolean isCorrect() {
			return this.correct;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return this.word;
		}
	}
	
	/**
	 * @author Justin
	 * A class extending ListCell so that a SpellCheckedWord could be properly displayed in a ListView.
	 * A word is black if correctly spelled, red if not.
	 */
	public class SpellCheckedWordCell extends ListCell<SpellCheckedWord> {
		
		@Override
		public void updateItem(SpellCheckedWord item, boolean empty) {
			super.updateItem(item, empty);
			
			setText(item == null ? "" : item.toString());
			
			if(item != null)
			setTextFill(item.isCorrect() ? Color.BLACK : Color.RED);
		}
	}
}