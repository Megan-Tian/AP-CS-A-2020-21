//Update this file with your own code.
import java.util.*; 

public class SpreadsheetLocation implements Location {
	private int column;
	private int row;

	public SpreadsheetLocation(String cellLocation) {
		this.row = Integer.parseInt(cellLocation.substring(1, cellLocation.length())) - 1; // need to subtract 1 because while it appears to start at 1, internally it's zero-based 
		this.column = Character.toUpperCase(cellLocation.charAt(0)) - 'A'; // This handles lower-case column letters as well
		// 'A' is also considered "0" for zero-based indexing 
	}
	
  @Override
  public int getRow() {
		return this.row;  
  }

  @Override
  public int getCol() {
		return this.column; 
  }

	// not required but good for debugging, and used to print cell location in opening/saving files
	public String toString() {
		return "" + (char)('A' + getCol()) + (getRow() + 1); 
	}

}
