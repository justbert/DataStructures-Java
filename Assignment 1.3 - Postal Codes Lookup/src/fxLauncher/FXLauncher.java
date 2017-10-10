package fxLauncher;

/*FileName: FXLauncher
 *Assignment 1
 *Course Name: CST8130 - Data Structures
 *LabSection: 301
 *Student Name: Justin Bertrand
 *Date: Jan 20th, 2015
 *
 *Description: A class used to index PostalCode objects by postal code, city, latitude, and longitude. 
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import postalCode.PostalCode;
import postalCode.PostalCodeIndex;


public class FXLauncher extends Application {
	
	public static void main(String[] args) { launch(args);}
	private PostalCodeIndex postalCodeIndex = new PostalCodeIndex();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Postal Code Analyzer");
		
		Text codeTitle = new Text(" Sorted by: Postal Code");
		Text cityTitle = new Text(" Sorted by: City");
		Text latitudeTitle = new Text(" Sorted by: Latitude");
		Text longitudeTitle = new Text(" Sorted by: Longitude");
		
		ListView<PostalCode> listCodeOrder = new ListView<>(postalCodeIndex.getCodeOrderList());
		listCodeOrder.setPrefWidth(800);
		ListView<PostalCode> listCityOrder = new ListView<>(postalCodeIndex.getCityOrderList());
		listCityOrder.setPrefWidth(800);
		ListView<PostalCode> listLatitudeOrder = new ListView<>(postalCodeIndex.getLatitudeOrderList());
		listLatitudeOrder.setPrefWidth(800);
		ListView<PostalCode> listLongitudeOrder = new ListView<>(postalCodeIndex.getLongitudeOrderList());
		listLongitudeOrder.setPrefWidth(800);
		
		TextField codeField = new TextField("Search");
		codeField.setOnAction(event-> {	
			int index = postalCodeIndex.getCodeListIndex(codeField.getText());
			if(index >= 0 && index < postalCodeIndex.getCodeOrderList().size()) {
				listCodeOrder.getSelectionModel().clearAndSelect(index);
				listCodeOrder.scrollTo(index);
			} else {
				codeField.setText("No results for " + codeField.getText());
			}
		});
		
		TextField cityField = new TextField("Search");
		cityField.setOnAction(event-> {
			int index = postalCodeIndex.getCityListIndex(cityField.getText());
			if(index >= 0 && index < postalCodeIndex.getCityOrderList().size()) {
				listCityOrder.getSelectionModel().clearAndSelect(index);
				listCityOrder.scrollTo(index);
			} else {
				cityField.setText("No results for " + cityField.getText());
			}
		});
		
		TextField latField = new TextField("Search");
		latField.setOnAction(event-> {
			int index = postalCodeIndex.getLatitudeListIndex(latField.getText());
			if(index >= 0 && index < postalCodeIndex.getLatitudeOrderList().size()) {
				listLatitudeOrder.getSelectionModel().clearAndSelect(index);
				listLatitudeOrder.scrollTo(index);
			} else {
				latField.setText("No results for " + latField.getText());
			}
		});
		
		TextField lonField = new TextField("Search");
		lonField.setOnAction(event-> {
			int index = postalCodeIndex.getLongitudeListIndex(lonField.getText());
			if(index >= 0 && index < postalCodeIndex.getLongitudeOrderList().size()) {
				listLongitudeOrder.getSelectionModel().clearAndSelect(index);
				listLongitudeOrder.scrollTo(index);
			} else {
				lonField.setText("No results for " + lonField.getText());
			}
		});
		
		String legend = String.format(" %-7s | %-30s | %-21s | %-12s | %-12s", "Postal", "City", "Province", "Latitude", "Longitude");
		
		Insets padding = new Insets(5.0);
		VBox vBoxCode = new VBox(codeTitle, codeField, new Text(legend), listCodeOrder);
		vBoxCode.setPadding(padding);
		VBox vBoxCity = new VBox(cityTitle, cityField, new Text(legend), listCityOrder);
		vBoxCity.setPadding(padding);
		VBox vBoxLatitude = new VBox(latitudeTitle, latField, new Text(legend), listLatitudeOrder);
		vBoxLatitude.setPadding(padding);
		VBox vBoxLongitude = new VBox(longitudeTitle, lonField, new Text(legend), listLongitudeOrder);
		vBoxLongitude.setPadding(padding);
		
		HBox hBoxTop = new HBox(vBoxCode, vBoxCity);
		HBox hBoxBot = new HBox(vBoxLatitude, vBoxLongitude);
		
		VBox vBox = new VBox(hBoxTop, hBoxBot);
		
		/*Scanner inputKB = new Scanner(System.in);
		System.out.print("Would you like to search for an entry (Type yes to search): ");
		if(inputKB.nextLine().trim().equalsIgnoreCase("yes")) {
			System.out.print("Search for: ");
			String search = inputKB.nextLine();
			inputKB.close();
			int codeIndex = postalCodeIndex.getCodeListIndex(search);
			if(codeIndex != -1) {
				int cityIndex = postalCodeIndex.getCityListIndex(search);
				int latIndex = postalCodeIndex.getLatitudeListIndex(search);
				int lonIndex = postalCodeIndex.getLongitudeListIndex(search);
				
				System.out.println(String.format("Postal: %d, City: %d, Lat: %d, Lon: %d", codeIndex, cityIndex, latIndex, lonIndex));
				
				listCodeOrder.getSelectionModel().clearAndSelect(codeIndex);
				listCodeOrder.scrollTo(codeIndex);
				listCityOrder.getSelectionModel().clearAndSelect(cityIndex);
				listCityOrder.scrollTo(cityIndex);
				listLatitudeOrder.getSelectionModel().clearAndSelect(latIndex);
				listLatitudeOrder.scrollTo(latIndex);
				listLongitudeOrder.getSelectionModel().clearAndSelect(lonIndex);
				listLongitudeOrder.scrollTo(lonIndex);
			} else {
				System.out.println("Query not found.");
			}
		}*/
		//Sets the font to a monospace font so as to keep columns aligned.
		vBox.setStyle("-fx-font: 13px monospace;");
		
		Scene scene = new Scene(vBox);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
