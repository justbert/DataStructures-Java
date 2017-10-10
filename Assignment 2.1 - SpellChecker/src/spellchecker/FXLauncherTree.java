package spellchecker;
import java.io.File;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import spellchecker.Spellchecker.SpellCheckedWord;

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
public class FXLauncherTree extends Application {

	public static void main(String[] args) { launch(args);}
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("SpellChecker - Tree");
		
		Dictionary dictionary = new Dictionary();
		dictionary.loadFile(new File("Main.txt"));
		
		TextFile text = new TextFile();
		
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
		
		Spellchecker checker = new Spellchecker(dictionary, text);
		checker.createList();
		
		ListView<SpellCheckedWord> words = new ListView<>(FXCollections.observableList(checker.getList()));
		words.setEditable(true);
		//Allows the creation of custom ListCells
		words.setCellFactory(new Callback<ListView<SpellCheckedWord>, ListCell<SpellCheckedWord>>() {
			
			@Override
			public ListCell<SpellCheckedWord> call(ListView<SpellCheckedWord> param) {
				// TODO Auto-generated method stub
				return checker.new SpellCheckedWordCell();
			}
		});
		
		//Creation of the button and the correction window
		Button button = new Button("Correct word");
		button.setOnAction(event -> {
			SpellCheckedWord word = words.getSelectionModel().getSelectedItem();
			
			if(word != null) {
				Stage popupStage = new Stage();
				popupStage.setTitle("Close Matches");
				ListView<String> closeMatches = new ListView<>(FXCollections.observableList(dictionary.closeMatches(word.toString())));
				closeMatches.autosize();
				
				//Button to cancel the word replacement 
				Button cancelButton = new Button("Cancel");
				cancelButton.setOnAction(cancelPopup -> {
					popupStage.close();
				});
				
				//Button to confirm the exchange of a spellchecked word
				Button confirmButton = new Button("Confirm");
				confirmButton.setOnAction(confirm -> {
					String correctWord = closeMatches.getSelectionModel().getSelectedItem();
					
					if(correctWord != null) {
						int index = words.getSelectionModel().getSelectedIndex();
						words.getItems().remove(index);
						words.getItems().add(index, checker.new SpellCheckedWord(correctWord, true));
						popupStage.close();
					}
				});
				
				HBox buttonBox = new HBox(cancelButton, confirmButton);
				VBox popupVBox = new VBox(buttonBox, closeMatches);
				
				Scene popupScene = new Scene(popupVBox);
				popupStage.setScene(popupScene);
				popupStage.show();
			}
			});
		
		//Button newFile = new Button()
		
		VBox vboxPrimary = new VBox(button, words);
		
		Scene primaryScene = new Scene(vboxPrimary);
		primaryStage.setScene(primaryScene);
		primaryStage.show();
			
	}
}
