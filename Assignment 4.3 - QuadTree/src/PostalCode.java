

import java.util.Comparator;

import geodetic.*;

public class PostalCode {
	
	private String code;
	private String city;
	private String province;
	private Coordinate coordinate;
	
	public PostalCode(String inputLine) {
		String[] fields = inputLine.split("\\||\\r\\n");
		
		this.code = fields[1];
		this.city = fields[2].intern();
		this.province = fields[3].intern();
		this.coordinate = new Coordinate(Double.parseDouble(fields[6]), Double.parseDouble(fields[7])).intern();
		
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s %s", code, city, province, coordinate.toString());
	}
	
	public Coordinate getCoordinate() {
		return this.coordinate;
	}
	
	public static class ComparePostalCode implements Comparator<PostalCode> {
		
		public static final ComparePostalCode instance = new ComparePostalCode();
		
		private ComparePostalCode() {};
		
		public int compare(PostalCode arg0, PostalCode arg1) {
			return arg0.code.compareTo(arg1.code);
		}
	}//End of ComparePostalCode class
	
	public static class CompareCity implements Comparator<PostalCode> {
		
		public static final CompareCity instance = new CompareCity();
		
		private CompareCity() {}
		
		public int compare(PostalCode arg0, PostalCode arg1) {
			return arg0.city.compareTo(arg1.city);
		}
	}//End of CompareCity class
	
	public static class CompareLatitude implements Comparator<PostalCode> {
		
		public static final CompareLatitude instance = new CompareLatitude();
		
		private CompareLatitude() {};
		
		public int compare(PostalCode arg0, PostalCode arg1) {
			if(arg0.coordinate.getLatitude() == arg1.coordinate.getLatitude()) {
				return 0;
			} else if(arg0.coordinate.getLatitude() < arg1.coordinate.getLatitude()) {
				return -1;
			} else {
				return 1;
			}
		}
	}//End of CompareLatitude class
	
	public static class CompareLongitude implements Comparator<PostalCode> {
		
		public static final CompareLongitude instance = new CompareLongitude();
		
		private CompareLongitude() {}
		
		public int compare(PostalCode arg0, PostalCode arg1) {
			
			if(arg0.coordinate.getLongitude() == arg1.coordinate.getLongitude()) {
				return 0;
			} else if(arg0.coordinate.getLongitude() < arg1.coordinate.getLongitude()) {
				return -1;
			} else {
				return 1;
			}
		}
	}//End of CompareLongitude class
}
