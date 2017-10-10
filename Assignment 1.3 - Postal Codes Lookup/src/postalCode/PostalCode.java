package postalCode;


import java.util.Comparator;

public class PostalCode {
	
	private String code;
	private String city;
	private String province;
	double latitude;
    double longitude;
	
	public PostalCode(String inputLine) {
		String[] fields = inputLine.split("\\||\\r\\n");
		
		this.code = fields[1];
		this.city = fields[2].intern();
		this.province = fields[3].intern();
		this.latitude = Double.parseDouble(fields[6]);
		this.longitude = Double.parseDouble(fields[7]);
		
	}
	
	public PostalCode(String code, String city, String province, double latitude, double longitude) {
		this.code = code;
		this.city = city;
		this.province = province;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return String.format("%-7s | %-30s | %-21s | %-12f | %-12f", code, city, province, latitude, longitude);
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
			//return arg0.latitude.compareTo(arg1.latitude);
			if(arg0.latitude > arg1.latitude) {
				return 1;
			} else if(arg0.latitude < arg1.latitude) {
				return -1;
			} else {
				return 0;
			}
		}
	}//End of CompareLatitude class
	
	/*
	 * A singleton comparator class which compares two PostalCode objects' longitudes, and returns a number smaller than 0 if the first argument comes before the second lexicographically,
	 * which returns 0 is both arguments are the same, and returns a number larger than 0 if the first argument comes after the second
	 * lexicographically.
	 */
	public static class CompareLongitude implements Comparator<PostalCode> {
		
		public static final CompareLongitude instance = new CompareLongitude();
		
		private CompareLongitude() {}
		
		public int compare(PostalCode arg0, PostalCode arg1) {
			if(arg0.longitude > arg1.longitude) {
				return 1;
			} else if(arg0.longitude < arg1.longitude) {
				return -1;
			} else {
				return 0;
			}
		}
	}//End of CompareLongitude class
}
