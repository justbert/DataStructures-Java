package concordance;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import concordance.Concordance.SpellCheckedWord;
import concordance.TextFile.Paragraph;

/*FileName: FXLauncherTree.java
 *Assignment 
 *Course Name: CST8130 - Data Structures
 *LabSection: 301
 *Student Name: Justin Bertrand
 *Date: Feb 11, 2015
 *
 *Description: A JavaFX launcher for the SpellChecker.
 */

/**
 * @author Justin
 *
 */
public class FXLauncherConcordanceHash extends Application {

	Concordance concordance;
	ListView<SpellCheckedWord> wordList;
	final WebView browser = new WebView();
	final WebEngine webEngine = browser.getEngine();
	TextField searchBox;
	
	public static void main(String[] args) { launch(args);}
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("ConcordanceV3 - Tree");
		
/*
 * Initializing variables
 */
		//Menus
		MenuItem openItem = new MenuItem("Open...");
		Menu fileMenu = new Menu("File");
		fileMenu.getItems().add(openItem);
		MenuBar bar = new MenuBar();
		bar.getMenus().add(fileMenu);
		
		Dictionary dictionary = new Dictionary();
		dictionary.loadFile(new File("Main.txt"));
		
		TextFile text = new TextFile();

		searchBox = new TextField("Search...");
		
		//Allows the selection and loading in of a CSV text file into the spellchecker
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Please choose text file: ");
		fileChooser.setInitialDirectory(
				 new File("/Users/Justin/Dropbox/College/Level 3/Data Structures/CST8130-Workbench/Assignment 2 - SpellChecker"));
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Text Files", "*.txt"));
		File selectedFile = fileChooser.showOpenDialog(primaryStage);
		 
		//Prevents errors if no file is loaded
		if(selectedFile != null) {
			text.loadTextFile(selectedFile);
		}
		
		concordance = new Concordance(dictionary, text);
		concordance.createConcordance();
		
		Set<SpellCheckedWord> keyset = concordance.getMap().keySet();
		ArrayList<SpellCheckedWord> list = new ArrayList<SpellCheckedWord>();
		list.addAll(keyset);
		list.sort(Concordance.CompareSpellCheckedWord.instance);
		
		wordList = new ListView<>(FXCollections.observableArrayList(list));
		wordList.autosize();
		wordList.setCellFactory(new Callback<ListView<SpellCheckedWord>, ListCell<SpellCheckedWord>>() {
			
			@Override
			public ListCell<SpellCheckedWord> call(ListView<SpellCheckedWord> param) {
				// TODO Auto-generated method stub
				return concordance.new SpellCheckedWordCell();
			}
		});
		
/*
 *	Events 
 */
		wordList.setOnMouseClicked(click -> {
			updateBrowser(wordList.getSelectionModel().getSelectedItem());
		});
		
		searchBox.setOnAction(search -> {
			String selected = searchBox.getText();
			int index = Math.abs(Collections.binarySearch(list, concordance.new SpellCheckedWord(selected, true), Concordance.CompareSpellCheckedWord.instance));
			System.out.print(index);
			wordList.getSelectionModel().clearAndSelect(index);
			wordList.scrollTo(index);
			updateBrowser(wordList.getSelectionModel().getSelectedItem());
			
		});
/*
 * Setting up scene		
 */
		VBox vBoxWords = new VBox(searchBox, wordList);
		vBoxWords.autosize();
		HBox hBoxContent = new HBox(vBoxWords, browser);
		VBox totalVBox = new VBox(bar, hBoxContent);
		
		Scene scene = new Scene(totalVBox);
		primaryStage.setScene(scene);
		primaryStage.show();
			
	}
	
	
	/**
	 * Updates the browser using a word that can be part of the concordance, and updates
	 * the browser window to display all paragraphs containing the word. 
	 * @param selected A word that is part of the concordance.
	 */
	private void updateBrowser(SpellCheckedWord selected) {
		double startTime = System.currentTimeMillis();
		Pattern findPattern;
		
		if(selected != null && selected.toString().contains("'")) {
			findPattern = Pattern.compile("(?i)("+selected+")");
		} else {
			findPattern = Pattern.compile("(?i)\\b("+selected+"\\b)");
		}
		
		StringBuilder sb = new StringBuilder();
		LinkedList<TextFile.Paragraph> parList = concordance.getMap().get(selected);
		HashSet<String> matchSet = new HashSet<>();
		//sb.append("<p style=\"font-size: 10;\">");
		
		for(Paragraph par : parList) {
			sb.append(par.toString());
			sb.append("<br/><br/>");
		}
		
		String allParagraphs = sb.toString();
		
		Matcher matcher = findPattern.matcher(allParagraphs);
		
		while(matcher.find()) {
			matchSet.add(matcher.group(1));
		}
		
		Iterator<String> matchIterator = matchSet.iterator();
		
		while(matchIterator.hasNext()) {
			String current = matchIterator.next();
			Matcher currentMatcher;
			
			if(current.contains("'")) {
				currentMatcher = Pattern.compile(current).matcher(allParagraphs);
			} else {
				currentMatcher = Pattern.compile("\\b"+current+"\\b").matcher(allParagraphs);
			}
			
			if(selected.isCorrect()) {
				allParagraphs = currentMatcher.replaceAll("<div style=\"border:1px solid blue;display:inline;padding: 1px;\">"+current+"</div>");
			} else {
				allParagraphs = currentMatcher.replaceAll("<div style=\"border:1px solid blue;display:inline;padding: 1px;color:red;\">"+current+"</div>");
			}
		}
		
		System.out.println(selected.hashCode());
		
		//sb.append("</p>");
		webEngine.loadContent(allParagraphs);
		double elapsedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
		System.out.println("Browser Content created in : " + elapsedTime + " seconds.");
	}
}
