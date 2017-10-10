import static org.junit.Assert.*;
import geodetic.Coordinate;
import geodetic.GeodeticCalculator;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;

import junit.framework.Assert;

import org.junit.Test;

/**FileName: QuadTreeTesting.java
 *Assignment 
 *Course Name: CST8130 - Data Structures
 *LabSection: 301
 *Student Name: Justin Bertrand
 *Date: Mar 29, 2015
 *
 *Description: 
 */

/**
 * @author Justin
 *
 */
public class QuadTreeTesting {

	@Test
	public void test() {
		PostalCodeIndex index = new PostalCodeIndex();
		long startTime = System.currentTimeMillis();
		
//		LinkedList<PostalCode> collection1 = index.findClosest(new Coordinate(49, -123));
		LinkedList<PostalCode> collection1 = index.findClosest(new Coordinate(50, -120));
		LinkedList<PostalCode> collection2 = index.findClosest(new Coordinate(44, -120));
		
		double elapsedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
		System.gc();
		System.out.println("Coordinate found in " + elapsedTime + " seconds");
		
		System.out.println("Collection 1: " + collection1);
		System.out.println("Collection 2: " + collection2);
	}
	
	@Test
	public void BruteforceLookup() {
		QuadTree postalCodeQuadTree = new QuadTree(new Coordinate(80.0, -150.0), new Coordinate(40.0, -50.0));
		PostalCode tempCode;
		LinkedList<PostalCode> pCodeList= new LinkedList<>();
		long startTime = System.currentTimeMillis();
		double elapsedTime;
		
		File file = new File("postal_codes.csv");
		try(BufferedReader input = Files.newBufferedReader(file.toPath());) {
			String inputLine = input.readLine(); //first line is title information, abandon
			
			while((inputLine = input.readLine()) != null) { //watch for the end of file, by looking for null
				tempCode = new PostalCode(inputLine);
				postalCodeQuadTree.add(tempCode);
				pCodeList.add(tempCode);
			}
			
			elapsedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
			System.gc();
			System.out.println("QuadTree created in: " + elapsedTime + " seconds");
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//
		Coordinate toFind;
		for(int index = 0; index < 10; ++index) {
			startTime = System.currentTimeMillis();
			
			toFind = new Coordinate(Math.random()*40+40, Math.random()*(-100)-50);
			System.out.println("Coordinate to find: " + toFind);
			
			LinkedList<PostalCode> quadTreeFound = postalCodeQuadTree.findClosest(toFind);
			elapsedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
			
			System.out.println("QuadTree: Found in " + elapsedTime +"seconds. Distance is "
					+ GeodeticCalculator.distVincenty(toFind, quadTreeFound.getFirst().getCoordinate()) +
					".\nCode is " + quadTreeFound.getFirst());
			
			startTime = System.currentTimeMillis();
			PostalCode listClosest = pCodeList.getFirst();
			for(PostalCode code : pCodeList) {
				if(GeodeticCalculator.distVincenty(code.getCoordinate(), toFind) < 
						GeodeticCalculator.distVincenty(listClosest.getCoordinate(), toFind)) {
					listClosest = code;
				}
			}
			
			elapsedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
			System.out.println("List: Found in " + elapsedTime +"seconds. Distance is "
					+ GeodeticCalculator.distVincenty(toFind, listClosest.getCoordinate())
					+ ".\nCode is " + listClosest);
			
			if(GeodeticCalculator.distVincenty(toFind, listClosest.getCoordinate()) != 
					GeodeticCalculator.distVincenty(toFind, quadTreeFound.getFirst().getCoordinate())) {
				System.out.println("FAIL");
			} else {
				System.out.println("PASS");
			}
			
			startTime = System.currentTimeMillis();
			quadTreeFound = postalCodeQuadTree.findClosest(listClosest.getCoordinate());
			elapsedTime = (double)(System.currentTimeMillis() - startTime)/1000.0;
			
			System.out.println("QuadTree: Found in " + elapsedTime +"seconds. Distance is "
					+ GeodeticCalculator.distVincenty(listClosest.getCoordinate(), quadTreeFound.getFirst().getCoordinate()) +
					".\nCode is " + quadTreeFound.getFirst());
			
			startTime = System.currentTimeMillis();
			System.out.println();
			
			
		}
		
		
	}

}
