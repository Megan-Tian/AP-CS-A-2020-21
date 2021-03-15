public class FormulaCell extends RealCell {
	// all fields protected and inherited from RealCell
	public FormulaCell (String cellValue) {
		// all dummy implementations - just need to compile for finalCheckpointA and checkpoint 3
		this.fullValue = cellValue; // prints out formula user enters 
		this.abbreviatedValue = (cellValue + "          ").substring(0, 10); 
		this.doubleValue = -1; 
	}	

	// all methods inherited and not overriden

	// TODO update to parse and execute formulas, esp getDoubleValue()
}