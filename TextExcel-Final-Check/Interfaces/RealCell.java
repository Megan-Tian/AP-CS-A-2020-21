public class RealCell implements Cell {
	// make protected so child classes can use and change in constructor without having to write set() methods
	// https://www.tutorialspoint.com/java/java_access_modifiers.htm
	protected String fullValue; 
	protected String abbreviatedValue; 
	protected double doubleValue; 

	// always call abbreviatedCellText when printing full spreadsheet because it also adds padding for spaces when formatting 
	public String abbreviatedCellText() {	
		return this.abbreviatedValue;
	} 

	// text for individual cell inspection, not truncated or padded
	public String fullCellText() {
		return this.fullValue; 
	}

	// needs to be overridden in ValueCell, PercentCell, and FormulaCell
	public double getDoubleValue() {
		return this.doubleValue; 
	}

}
