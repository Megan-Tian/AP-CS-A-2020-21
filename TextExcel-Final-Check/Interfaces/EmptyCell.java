public class EmptyCell implements Cell {
	// both methods return 10 spaces for a blank cell
	private String abbreviatedValue;
	private String fullValue; 

	public EmptyCell() {
		this.abbreviatedValue = "          ";
		this.fullValue = "";
	}

	public String abbreviatedCellText() {
		return this.abbreviatedValue;
	} 

	public String fullCellText() {
		return this.fullValue;
	}
}