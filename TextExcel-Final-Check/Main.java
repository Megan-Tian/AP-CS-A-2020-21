import java.io.FileNotFoundException;
import java.util.*;

// Main.java in repl.it = TestExcel.java in instructions
class Main {
  public static void main(String[] args) {
    // The line below runs tests on your classes before your code starts. Change 
    // the string to "Extra Credit" once your program passes part A if you wish to
    // pursue extra points. 
    
	  TestRunner.Run("Part A");

		Spreadsheet spreadsheet = new Spreadsheet(); 
		System.out.println(spreadsheet.getGridText());
		
		Scanner user = new Scanner(System.in); 
		String userInput = user.nextLine(); 
		String output = ""; 

		while (!userInput.equalsIgnoreCase("quit")) {
			output = spreadsheet.processCommand(userInput); 
			System.out.println(output);
			userInput = user.nextLine(); 
		}

		user.close(); 

  }
}