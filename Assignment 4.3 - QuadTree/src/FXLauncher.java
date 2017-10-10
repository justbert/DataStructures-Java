import geodetic.Coordinate;

import java.util.LinkedList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import jdk.nashorn.internal.runtime.FindProperty;


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
public class FXLauncher extends Application {
	
	TextField latitudeBox;
	TextField longitudeBox;
	Button searchButton;
	PostalCodeIndex postalIndex;
	
	public static void main(String[] args) { launch(args);}
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("QuadTree");
		
		postalIndex = new PostalCodeIndex();
		
		StackPane mapLayout = new StackPane();
		
		mapLayout.autosize();
		Image mapCanada = new Image("file:Canada.png", 1200, 0, true, false);
		
		ImageView mapBackground = new ImageView(mapCanada);
		mapBackground.preserveRatioProperty();
		mapBackground.autosize();
		mapBackground.fitWidthProperty().bind(mapLayout.widthProperty());

		mapLayout.getChildren().addAll(mapBackground);
		latitudeBox = new TextField("Latitude");
		longitudeBox = new TextField("Longitude");
		searchButton = new Button("Search");
		ListView<PostalCode> listView = new ListView<>();
		listView.setPrefWidth(450);
		
		HBox topBox = new HBox(latitudeBox, longitudeBox, searchButton);
		HBox botBox = new HBox(listView, mapLayout);
		VBox everything = new VBox(topBox, botBox);
		
		searchButton.setOnAction(searchAndUpdate -> {
			double latitudeSearch = Double.parseDouble(latitudeBox.getText());
			double longitudeSearch = Double.parseDouble(longitudeBox.getText());
			Coordinate searchCoordinate = new Coordinate(latitudeSearch, longitudeSearch);
			
			LinkedList<PostalCode> foundList = postalIndex.findClosest(searchCoordinate);
			if(foundList != null) {
				listView.setItems(FXCollections.observableList(postalIndex.findClosest(searchCoordinate)));

				Coordinate foundCoord = listView.getItems().get(0).getCoordinate();
				
				double longUnit = mapCanada.getWidth()/100.0;
				double latUnit = mapCanada.getHeight()/40.0;
				
				Circle searchDot = new Circle(2.5);
				searchDot.setFill(Color.RED);
				searchDot.setTranslateX((searchCoordinate.getLongitude() + 100.0)*longUnit);
				searchDot.setTranslateY(-(searchCoordinate.getLatitude()-60.0)*latUnit);
				
				Circle foundDot = new Circle(2.5);
				foundDot.setFill(Color.BLUE);
				foundDot.setTranslateX((foundCoord.getLongitude() + 100.0)*longUnit);
				foundDot.setTranslateY(-(foundCoord.getLatitude()-60.0)*latUnit);
				
				mapLayout.getChildren().clear();
				mapLayout.getChildren().addAll(mapBackground, searchDot, foundDot);
			} else {
				listView.getItems().clear();
			}			
		});
		
		Scene scene = new Scene(everything);
		primaryStage.setScene(scene);
		primaryStage.show();
			
	}
}
