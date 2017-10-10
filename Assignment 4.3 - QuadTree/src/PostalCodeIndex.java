

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

import geodetic.Coordinate;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;

public class PostalCodeIndex {
	
	/*
	 * Package level variables to allow for JUnit testing.
	 */
	QuadTree postalCodeQuadTree = new QuadTree(new Coordinate(80.0, -150.0), new Coordinate(40.0, -50.0));
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
				postalCodeQuadTree.add(new PostalCode(inputLine));
			}
			
			double elapsedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
			System.gc();
			System.out.println("QuadTree created in: " + elapsedTime + " seconds");
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public LinkedList<PostalCode> findClosest(Coordinate coordinate) {
		return this.postalCodeQuadTree.findClosest(coordinate);
	}
}
	