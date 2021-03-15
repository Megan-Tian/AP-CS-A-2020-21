public class PercentCell extends RealCell {
	// all fields protected and inherited from RealCell
	public PercentCell(String cellValue) {
		// convert string percent to decimal 
		String withoutPercent = cellValue.substring(0, cellValue.indexOf("%"));
		double d = Double.parseDouble(withoutPercent); 
		this.doubleValue = d / 100;

		// convert decimal percent rep back to string percent - elimininats begining/trailing zeroes
		// casting to int automatically truncates
		this.abbreviatedValue = ((int) d + "%         ").substring(0, 10); 

		this.fullValue = doubleValue + ""; 				
	}
	// all other methods inherited and not overriden
}