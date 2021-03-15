public class TextCell implements Cell {
	private String fullValue; // includes ""
	private String abbreviatedValue; // doesn't include ""

	public TextCell (String cellValue) {
		this.fullValue = cellValue; 
		 
		// user inputs with ""
		// need lastIndexOf because there's two double quotes
		String tempAbbrev = cellValue.substring(1, cellValue.lastIndexOf("\""));

		this.abbreviatedValue = (tempAbbrev + "          ").substring(0, 10); 
	}

	// always call abbreviatedCellText when printing full spreadsheet because it also adds padding for spaces when formatting 
	public String abbreviatedCellText() {	
		return this.abbreviatedValue;
	} 

	// text for individual cell inspection, not truncated or padded
	public String fullCellText() {
		return this.fullValue; 
	}
}