import java.text.DecimalFormat;

public class ValueCell extends RealCell {
	// all fields protected and inherited from RealCell
	public ValueCell(String cellValue) {
		this.doubleValue = Double.parseDouble(cellValue);
		this.abbreviatedValue = (this.doubleValue + "          ").substring(0, 10); 

		// fullValue is a string representation of a double
		// if user enters "5", abbreviatedValue should show "5.0", but the fullValue should show just "5"
				
		if (!cellValue.contains(".")) {
			this.fullValue = String.format("%.0f", this.doubleValue); 	
		} else {
			this.fullValue = this.doubleValue + ""; 
		}
	}

	// abbreviatedCellText(), fullCellText(), getDoubleValue() inherited and not overriden
}