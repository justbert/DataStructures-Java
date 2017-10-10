package postalCode;

/*FileName: PostalCodeIndexJUnit
 *Assignment 1
 *Course Name: CST8130 - Data Structures
 *LabSection: 301
 *Student Name: Justin Bertrand
 *Date: Jan 20th, 2015
 *
 *Description: A class for testing the PostalCode and PostalCodeIndex. Checks the checksum and sort order of PostalCode lists.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.junit.Test;

public class PostalCodeIndexJUnit {
	private PostalCodeIndex index = new PostalCodeIndex();
	
	@Test
	/*
	 * Checks the correctness of the sort through a checksum and a sort verification.
	 */
	public void CodeChecksumAndSortTest() {
		ObservableList<PostalCode> codeListOriginal = FXCollections.observableList(new ArrayList<>(index.codeOrderList));
		long checkSumOriginal = 0;
		
		for(PostalCode item : codeListOriginal) {
			checkSumOriginal += item.hashCode();
		}
		
		ObservableList<PostalCode> codeListSorted = FXCollections.observableList(new ArrayList<>(codeListOriginal));
		codeListSorted.sort(PostalCode.ComparePostalCode.instance);
		long checkSumSorted = 0;
		
		checkSumSorted += codeListSorted.get(0).hashCode();
		for(int i = 1; i < codeListSorted.size(); ++i) {
			checkSumSorted += codeListSorted.get(i).hashCode();
			if(codeListSorted.get(i-1).code.compareTo(codeListSorted.get(i).code) > 0) {
				fail("Sorting failed.");
			}
		}
		System.out.println("CSOriginal: "+checkSumOriginal+ " CSSorted: "+checkSumSorted);
		assertEquals(checkSumOriginal, checkSumSorted);
	}
	
	/*
	 * Checks the correctness of the sort through a checksum and a sort verification.
	 */
	@Test
	public void CityCheckSumAndSortTest() {
		ArrayList<PostalCode> codeListOriginal = new ArrayList<>(index.codeOrderList);
		long checkSumOriginal = 0;
		
		for(int i = 0; i < codeListOriginal.size(); ++i) {
			checkSumOriginal += codeListOriginal.get(i).hashCode();
		}
		
		ArrayList<PostalCode> cityListSorted = new ArrayList<>(codeListOriginal);
		cityListSorted.sort(PostalCode.CompareCity.instance);
		
		long checkSumSorted = 0;
		checkSumSorted += cityListSorted.get(0).hashCode();
		for(int i = 1; i < cityListSorted.size(); ++i) {
			checkSumSorted += cityListSorted.get(i).hashCode();
			if(cityListSorted.get(i-1).city.compareTo(cityListSorted.get(i).city) > 0) {
				fail("Sorting failed.");
			}
		}
		System.out.println("CSOriginal: "+checkSumOriginal+ " CSSorted: "+checkSumSorted);
		assertEquals(checkSumOriginal, checkSumSorted);
	}
	
	/*
	 * Checks the correctness of the sort through a checksum and a sort verification.
	 */
	@Test
	public void LatitudeCheckSumAndSortTest() {
		ArrayList<PostalCode> codeListOriginal = new ArrayList<>(index.codeOrderList);
		long checkSumOriginal = 0;
		
		for(int i = 0; i < codeListOriginal.size(); ++i) {
			checkSumOriginal += codeListOriginal.get(i).hashCode();
		}
		
		ArrayList<PostalCode> latitudeListSorted = new ArrayList<>(codeListOriginal);
		latitudeListSorted.sort(PostalCode.CompareLatitude.instance);
		
		long checkSumSorted = 0;
		checkSumSorted += latitudeListSorted.get(0).hashCode();
		for(int i = 1; i < latitudeListSorted.size(); ++i) {
			checkSumSorted += latitudeListSorted.get(i).hashCode();
			if(latitudeListSorted.get(i-1).latitude > latitudeListSorted.get(i).latitude) {
				fail("Sorting failed.");
			}
		}
		System.out.println("CSOriginal: "+checkSumOriginal+ " CSSorted: "+checkSumSorted);
		assertEquals(checkSumOriginal, checkSumSorted);
	}
	
	/*
	 * Checks the correctness of the sort through a checksum and a sort verification.
	 */
	@Test
	public void LongitudeCheckSumAndSortTest() {
		ArrayList<PostalCode> codeListOriginal = new ArrayList<>(index.codeOrderList);
		long checkSumOriginal = 0;
		
		for(int i = 0; i < codeListOriginal.size(); ++i) {
			checkSumOriginal += codeListOriginal.get(i).hashCode();
		}
		
		ArrayList<PostalCode> longitudeListSorted = new ArrayList<>(codeListOriginal);
		longitudeListSorted.sort(PostalCode.CompareLongitude.instance);
		
		long checkSumSorted = 0;
		checkSumSorted += longitudeListSorted.get(0).hashCode();
		for(int i = 1; i < longitudeListSorted.size(); ++i) {
			checkSumSorted += longitudeListSorted.get(i).hashCode();
			if(longitudeListSorted.get(i-1).longitude > longitudeListSorted.get(i).longitude) {
				fail("Sorting failed.");
			}
		}
		System.out.println("CSOriginal: "+checkSumOriginal+ " CSSorted: "+checkSumSorted);
		assertEquals(checkSumOriginal, checkSumSorted);
	}

}
