package concordance;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import concordance.TextFile.Paragraph;

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
public class Concordance {

	Dictionary dictionary;
	TextFile text;
	HashMap<SpellCheckedWord, LinkedList<Paragraph>> words;
	
	/**
	 * Constructor which takes in a Dictionary, and a TextFile.
	 * @param dict
	 * @param text
	 */
	public Concordance(Dictionary dict, TextFile text) {
		this.dictionary = dict;
		this.text = text;
		this.words = new HashMap<>();
	}
	
	/**
	 * Default constructor which initializes values.
	 */
	public Concordance() {
		this(new Dictionary(), new TextFile());
	}
	
	/**
	 * A parser to remove punctuation and special characters.
	 * @param word A word which needs to be parsed.
	 * @return The passed in word without punctuation or special characters.
	 */
	private String parse(String word) {

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
	public HashMap<SpellCheckedWord, LinkedList<Paragraph>> getMap() {
		return this.words;
	}
	
	/**
	 * Creates the list of spellchecked words.
	 */
	public void createConcordance() {
		double startTime = System.currentTimeMillis();
		
		SpellCheckedWord one = new SpellCheckedWord("one", true);
		System.out.println(one.equals("one"));
		
		Iterator<Paragraph> iterator = text.getIterator();
		String[] sArray;
		LinkedList<Paragraph> list;
		
		while(iterator.hasNext()) {	
			
			Paragraph paragraph = iterator.next();
			sArray = paragraph.getText().split("[^a-zA-Z']+");
			//sArray = iterator.next().split("\\s+|--");
			//sArray = paragraph.getText().split("\"*([[A-Z][a-z]']*)\\p{Punct}*");
			
			SpellCheckedWord checkedWord;
			
			for(String word : sArray) {
				if(!word.equalsIgnoreCase("")) {
					checkedWord = new SpellCheckedWord(word.toLowerCase(), dictionary.contains(word));
					
					if(words.containsKey(checkedWord)) {
						list = words.get(checkedWord);
						if(!list.getLast().equals(paragraph)) {
							list.add(paragraph);
						}
					} else {
						list = new LinkedList<Paragraph>();
						list.add(paragraph);
						words.put(checkedWord, list);
					}
				}
				
			}
		}
		
		double elapsedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
		System.gc();
		System.out.println("Concordance created in : " + elapsedTime + " seconds.");
	}
	
	/**
	 * @author Justin
	 * A class representing a word that has been spellchecked.
	 */
	public class SpellCheckedWord {
		
		private boolean correct;
		private String word;
		private int occurance;
		/**
		 * A contructor which takes in a word and a boolean representing if it's correctly spelled or not.
		 * @param word A word.
		 * @param correct A boolean representing if the word is correctly spelled or not.
		 */
		public SpellCheckedWord(String word, boolean correct) {
			this.word = word;
			this.correct = correct;
			this.occurance = 1;
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
		
		@Override
		public int hashCode() {
			return this.word.hashCode();
		}
		
		@Override
		public boolean equals(Object arg) {
			if(arg == null) 
				return false;
			else
			return this.hashCode() == arg.hashCode();
		}
		
		public void addOccurance() {
			++occurance;
		}
		
		public int getOccurance() {
			return occurance;
		}
		
	}
	
	public static class CompareSpellCheckedWord implements Comparator<SpellCheckedWord> {
		
		public static final CompareSpellCheckedWord instance = new CompareSpellCheckedWord();
		
		private CompareSpellCheckedWord() {};
		
		public int compare(SpellCheckedWord arg0, SpellCheckedWord arg1) {
			return arg0.word.compareToIgnoreCase(arg1.word);
		}
	}
	
	/**
	 * @author Justin
	 * A class extending ListCell so that a SpellCheckedWord could be properly displayed in a ListView.
	 * A word is black if correctly spelled, red if not.
	 */	
	public class SpellCheckedWordCell extends ListCell<SpellCheckedWord> {
		private Label label = new Label();
		
		@Override
		public void updateItem(SpellCheckedWord item, boolean empty) {
			super.updateItem(item, empty);
			
			setText(null);
			
			if(empty) {
				setGraphic(null);
			} else {
				label.setText(item.toString());
				label.setTextFill(item.isCorrect() ? Color.BLACK : Color.RED);
				label.setFont(new Font(Math.sqrt((double)words.get(item).size())*2+10.0));
				setGraphic(label);
			}
		}
	}
}

