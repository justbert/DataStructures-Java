package postalCode;

/*FileName: PostalCodeIndex
 *Assignment 1
 *Course Name: CST8130 - Data Structures
 *LabSection: 301
 *Student Name: Justin Bertrand
 *Date: Jan 20th, 2015
 *
 *Description: A class used to index PostalCode objects by postal code, city, latitude, and longitude. 
 *				It also provides rudimentary search options.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PostalCodeIndex {
	
	/*
	 * Package level variables to allow for JUnit testing.
	 */
	ObservableList<PostalCode> codeOrderList = FXCollections.observableArrayList(new ArrayList<>());
	ObservableList<PostalCode> cityOrderList;
	ObservableList<PostalCode> latitudeOrderList;
	ObservableList<PostalCode> longitudeOrderList;
	private long startTime = System.currentTimeMillis();
	
	/*
	 * A constructor which takes in a file of Canada Post formatted postal code data, and indexes it 
	 * through PostalCode object creation.
	 */
	public PostalCodeIndex() {
		File file = new File("postal_codes.csv");
		try(BufferedReader input = Files.newBufferedReader(file.toPath());) {
			String inputLine = input.readLine(); //first line is title information, abandon
			
			while((inputLine = input.readLine()) != null) { //watch for the end of file, by looking for null
				codeOrderList.add(new PostalCode(inputLine));
			}
			
			Collections.sort(codeOrderList, PostalCode.ComparePostalCode.instance);
			
			//Creates a copy of the list of all the references to the PostalCodes but a different reference to the list.
			cityOrderList = FXCollections.observableList(new ArrayList<PostalCode>(codeOrderList));
			latitudeOrderList = FXCollections.observableList(new ArrayList<PostalCode>(codeOrderList));
			longitudeOrderList = FXCollections.observableList(new ArrayList<PostalCode>(codeOrderList));
			
			//Sort all of the lists according to their respective
			Collections.sort(latitudeOrderList, PostalCode.CompareLatitude.instance);
			Collections.sort(cityOrderList, PostalCode.CompareCity.instance);
			Collections.sort(longitudeOrderList, PostalCode.CompareLongitude.instance);
			
			double elapsedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
			System.gc();
			System.out.println(elapsedTime);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Get statements for the lists which return unmodifiable lists.
	 */
	public ObservableList<PostalCode> getCodeOrderList() {
		return FXCollections.unmodifiableObservableList(codeOrderList);
	}
	
	public ObservableList<PostalCode> getCityOrderList() {
		return FXCollections.unmodifiableObservableList(cityOrderList);
	}
	
	public ObservableList<PostalCode> getLatitudeOrderList() {
		return FXCollections.unmodifiableObservableList(latitudeOrderList);
	}
	
	public ObservableList<PostalCode> getLongitudeOrderList() {
		return FXCollections.unmodifiableObservableList(longitudeOrderList);
	}
	
	/*
	 * Returns the index position of a PostalCode object which contains the String passed as an argument.
	 */
	public int getCodeListIndex(String arg) {
		PostalCode searchObject = new PostalCode(arg,"","",0.0,0.0);
		return Math.abs(Collections.binarySearch(this.codeOrderList, searchObject , PostalCode.ComparePostalCode.instance));
	}
	
	/*
	 * Returns the index position of a PostalCode object which contains the String passed as an argument.
	 */
	public int getCityListIndex(String arg) {
		PostalCode searchObject = new PostalCode("",arg,"",0.0,0.0);
		return Math.abs(Collections.binarySearch(this.cityOrderList, searchObject , PostalCode.CompareCity.instance));
	}
	
	/*
	 * Returns the index position of a PostalCode object which contains the String passed as an argument.
	 */
	public int getLatitudeListIndex(double arg) {
		PostalCode searchObject = new PostalCode("", "", "", arg, 0.0);
		return Math.abs(Collections.binarySearch(this.latitudeOrderList, searchObject , PostalCode.CompareLatitude.instance));
	}
	
	public int getLatitudeListIndex(String arg) {
		return getLatitudeListIndex(Double.parseDouble(arg));
	}
	
	/*
	 * Returns the index position of a PostalCode object which contains the String passed as an argument.
	 */
	public int getLongitudeListIndex(double arg) {
		PostalCode searchObject = new PostalCode("", "", "", 0.0, arg);
		return Math.abs(Collections.binarySearch(this.longitudeOrderList, searchObject , PostalCode.CompareLongitude.instance));
	}
	
	public int getLongitudeListIndex(String arg) {
		return getLongitudeListIndex(Double.parseDouble(arg));
	}
}
	