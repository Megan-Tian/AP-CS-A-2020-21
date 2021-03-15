// Update this file with your own code.
import java.util.*;
import java.io.*; // for doing stuff with files

public class Spreadsheet implements Grid {
	private Cell[][] spreadsheet;
	// make class constants
	private static final int ROWS = 20; 
	private static final int COLUMNS = 12; 

	EmptyCell emptyCell = new EmptyCell(); 

	public Spreadsheet() {
		this.spreadsheet = new Cell[ROWS][COLUMNS];
		setAllToEmpty(); 		
	}

	private void setAllToEmpty() {
		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLUMNS; c++) {
				this.spreadsheet[r][c] = emptyCell; 
			}
		}
	}

	@Override
	public String processCommand(String command)
	{
		String result = "";

		// the try/catch needed for file manipulation also handles improper commands and reprompts user

		try {		 
		// EMPTY STRING
    if (command == null || command.equals("")) {
			result = "";
		}

    // QUIT
		else if (command.equalsIgnoreCase("quit")) {
      result = "quit";
    }    
    
    // CELL INSPECTION
    // should return with "" if string, () if equation, etc.
		// formatting should be done in each cell type's fullCellText() method
    else if (command.length() <= 3) {
      SpreadsheetLocation loc = new SpreadsheetLocation(command);
      result = inspectCell(loc);
    }

    // ASSIGNMENT TO STRING VALUES
    else if (command.contains("\"")) {
      // location of cell
      SpreadsheetLocation loc = new SpreadsheetLocation(command.substring(0, command.indexOf("=") - 1));

      // string being entered, includes double quotes
      String text = command.substring(command.indexOf("\""));

      result = assignStringToCell(loc, text);
    }

    // CLEARING ENTIRE SPREADSHEET
    else if (command.equalsIgnoreCase("clear")) {
      result = clearAll(); 
    }

    //CLEARING A PARTICULAR CELL
    else if (command.substring(0, 5).equalsIgnoreCase("clear") && command.length() > 5) {
      SpreadsheetLocation loc = new SpreadsheetLocation(command.substring(6));
      result = clearCell(loc); 
    }
		
		// ASSIGN TO DOUBLE VALUE
		else if (command.contains("=") && !command.contains("%") && !command.contains("(")) {
			SpreadsheetLocation loc = new SpreadsheetLocation(command.substring(0, command.indexOf("=") - 1));

			// value being entered
      String text = command.substring(command.indexOf("=") + 2);

			result = assignDoubleToCell(loc, text); 
		}

		// ASSIGN TO PERCENT VALUE
		else if (command.contains("=") && command.contains("%")) {
			SpreadsheetLocation loc = new SpreadsheetLocation(command.substring(0, command.indexOf("=") - 1));

			String text = command.substring(command.indexOf("=") + 2);

			result = assignPercentToCell(loc, text); 
		}

		// ASSIGN TO FORMULA
		else if (command.contains("=") && command.contains("(")) {
			SpreadsheetLocation loc = new SpreadsheetLocation(command.substring(0, command.indexOf("=") - 1));

			String text = command.substring(command.indexOf("=") + 2);

			result = assignFormulaToCell(loc, text); 
		}

		// SAVE AS CSV - ch.6 in textbook
		else if (command.contains("save")) {
			/* 
			fomatted as [CellIdentifier,CellType,FullCellText]. Empty cells are not recorded
				A20,ValueCell,2.2
				B20,PercentCell,0.024
				C20,FormulaCell,( A20 + B20 )
				D20,TextCell,"howdy"
			*/
			String fileName = command.substring(command.indexOf(" ") + 1); 
			result = saveFile(fileName); 
		}

		// OPEN AS CSV
		else if (command.contains("open")) {
			String fileName = command.substring(command.indexOf(" ") + 1); 
			result = openFile(fileName); 
		}

	} catch (Exception e) {
		result = "ERROR: Something Went Wrong :( Please re-type. Check for common errors that a) the command is within the spreadsheet grid range b) the command is formatted properly c) the file exists, etc."; 
	}
		return result;     
	}

	private String inspectCell(Location loc) {
		return getCell(loc).fullCellText();
	}

	private String assignStringToCell(Location loc, String text) {
		// set cell in given location to value
    this.spreadsheet[loc.getRow()][loc.getCol()] = new TextCell(text);

    // print spreadsheet
     return getGridText();
	}

	private String clearAll() {
		setAllToEmpty();
    return getGridText();
	}

	private String clearCell(Location loc) {
		this.spreadsheet[loc.getRow()][loc.getCol()] = emptyCell;
    return getGridText();
	}

	private String assignDoubleToCell(Location loc, String text) {
		this.spreadsheet[loc.getRow()][loc.getCol()] = new ValueCell(text);
		return getGridText();
	}

	private String assignPercentToCell(Location loc, String text) {
		this.spreadsheet[loc.getRow()][loc.getCol()] = new PercentCell(text);
		return getGridText();
	}

	private String assignFormulaToCell(Location loc, String text) {
		this.spreadsheet[loc.getRow()][loc.getCol()] = new FormulaCell(text);
		return getGridText();
	}

	private String saveFile(String fileName) throws IOException { 
		// fomatted as [CellIdentifier,CellType,FullCellText]. Empty cells are not recorded
		PrintWriter writer = new PrintWriter(new FileWriter(fileName)); 
		// https://www.baeldung.com/java-write-to-file#write-with-printwriter

		for (int r = 0; r < ROWS; r++) {
			for (int c = 0; c < COLUMNS; c++) {
				Cell cell = this.spreadsheet[r][c];
				SpreadsheetLocation loc = new SpreadsheetLocation("" + (char)(c + 'A') + (r + 1));
				// need r + 1 because the computer recognizes columns as 0-based indexing while it prints out as starting at 1

				if (cell instanceof EmptyCell == false) {
					writer.println(loc.toString() + "," + cell.getClass().getSimpleName() + "," + cell.fullCellText());
				} 
			}
		}

		writer.close();
		return getGridText();
	}

	private String openFile(String fileName) throws IOException {
		// fomatted as [CellIdentifier,CellType,FullCellText]. Empty cells are not recorded
		Scanner reader = new Scanner(new File(fileName));
		// set all default to emptyCells
		setAllToEmpty(); 

		while (reader.hasNextLine()) {
   		String line = reader.nextLine(); // gets a "set" of info as CellIdentifier,CellType,FullCellText
			// ex. A1,ValueCell,0.1
			String stringLocation = line.substring(0, line.indexOf(","));
			SpreadsheetLocation loc = new SpreadsheetLocation(stringLocation); 
			String cellType = line.substring(line.indexOf(",") + 1, line.lastIndexOf(","));
			String cellText = line.substring(line.lastIndexOf(",") + 1);

			if (cellType.equals("TextCell")) {
				this.spreadsheet[loc.getRow()][loc.getCol()] = new TextCell(cellText);

			} else if (cellType.equals("ValueCell")) {
				this.spreadsheet[loc.getRow()][loc.getCol()] = new ValueCell(cellText);

			} else if (cellType.equals("PercentCell")) {
				double p = Double.parseDouble(cellText); 
				this.spreadsheet[loc.getRow()][loc.getCol()] = new PercentCell((p * 100) + "%");

			} else if (cellType.equals("FormulaCell")) {
				this.spreadsheet[loc.getRow()][loc.getCol()] = new FormulaCell(cellText);

			} 
		}

		reader.close();
		return getGridText();
	}

	@Override
	public int getRows() {
		return ROWS;
	}

	@Override
	public int getCols() {
		return COLUMNS;
	}

	@Override
	public Cell getCell(Location loc) { 
		return this.spreadsheet[loc.getRow()][loc.getCol()];
	}

	@Override
	public String getGridText() { 
    char[] topRow = {'A','B','C','D','E','F','G','H','I','J','K','L'};
    String grid = "";

    //TOP ROW
    grid += "   |";
    for (int i = 0; i < COLUMNS; i++) {
      grid += (topRow[i] + "         |");
    }
    grid += "\n";

    //MAIN GRID
    for (int r = 0; r < ROWS; r++) {
      //prints the row number
      grid += (((r + 1) + " ").substring(0, 2)) + " |";
      //prints value/object of each cell in a row
      for (int c = 0; c < COLUMNS; c++) {
        //returns the abbreviated version of the cell contents
        //10 characters or less
        grid += (spreadsheet[r][c].abbreviatedCellText()) + "|";
      }
      //enters down to the next line
      grid += "\n";
    }

    return grid;
	}

}
