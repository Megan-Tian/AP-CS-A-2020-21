import java.util.*;

public class Main {
	public static void main(String[] args) {
		// FracCalcTests.FinalCheckpoint();

		intro(); //all print stmts prompting user must be commented out for final checkpoint to work
		Scanner console = new Scanner(System.in);

		System.out.println("Please enter your expression: "); // this line must be commented out for checkpoint to work
		String expression = console.nextLine();
		while (!expression.equalsIgnoreCase("quit")) {
			System.out.println(produceAnswer(expression));
			System.out.println("Please enter your expression: "); // this line must be commented out for checkpoint to work
			expression = console.nextLine(); 
		}

		console.close(); 
	}

	public static String produceAnswer(String input) {
		String frac1 = input.substring(0, input.indexOf(" "));
		String operator = input.substring(input.indexOf(" ") + 1, input.indexOf(" ") + 2);
		String frac2 = input.substring(input.indexOf(" ") + 3);

		// variables storing whole, numerator, and denominator parts initialized to
		// their "default values" as provided in the assignment description

		String whole1 = frac1;
		String numerator1 = "0";
		String denominator1 = "1";

		String whole2 = frac2;
		String numerator2 = "0";
		String denominator2 = "1";

		if (frac1.indexOf("/") != -1) { // checks if user has a fractional part
			if (frac1.indexOf("_") != -1) { // checks whether it's a mixed number
				whole1 = frac1.substring(0, frac1.indexOf("_")); // negative integer parts have the negative sign included
			} else {
				whole1 = "0";
			}

			numerator1 = frac1.substring(frac1.indexOf("_") + 1, frac1.indexOf("/"));
			// negative numerator AND improper fractions accounted for because indexOf("_") returns -1 when there's no int part (no "_") numerator1 starts at index 0 where the negative sign is
			denominator1 = frac1.substring(frac1.indexOf("/") + 1, frac1.length());
		}

		// same test/process as with frac1, repeated for frac2
		if (frac2.indexOf("/") != -1) {
			if (frac2.indexOf("_") != -1) {
				whole2 = frac2.substring(0, frac2.indexOf("_"));
			} else {
				whole2 = "0";
			}
			numerator2 = frac2.substring(frac2.indexOf("_") + 1, frac2.indexOf("/"));
			denominator2 = frac2.substring(frac2.indexOf("/") + 1, frac2.length());
		}

		return calculate(operator, whole1, numerator1, denominator1, whole2, numerator2, denominator2);
	}

	public static String calculate(String operator, String w1, String n1, String d1, String w2, String n2, String d2) {
		// first converts frac1 and frac2 from produceAnswer() to improper fractions using their parsed whole/numerator/denominator parts

		int whole1 = Integer.parseInt(w1); 
		int numerator1 = Integer.parseInt(n1);
		int whole2 = Integer.parseInt(w2); 
		int numerator2 = Integer.parseInt(n2); 

		// need if statements (next block of code) to make sure negative improper fractions are converted correctly, ie. -38_3/72 (final checkpoint). Can't add numerator directly to whole part times denominator if the mixed number is negative - need to subtract
		// this part below will only convert the whole/int part of the mixed number into a fraction
		int improperNumerator1 = whole1 * Integer.parseInt(d1); 
		int improperDenominator1 = Integer.parseInt(d1);

		int improperNumerator2 = whole2 * Integer.parseInt(d2); 
		int improperDenominator2 = Integer.parseInt(d2);

		// now accounts for fractional part of mixed number. 
		if (whole1 != 0) {
			if (whole1 < 0 && numerator1 > 0) {
				// ex. -38_3/72, this does -38-(3/72). "Distributes" the negative in front of the int part of the mixed number
				improperNumerator1 -= numerator1; 
			} else if (whole1 > 0 && numerator1 > 0) {
				// both whole part and numerator part are positive, ex. 1_1/2
				improperNumerator1 += numerator1; 
			} 
		}
		// does the same thing for frac2
		if (whole2 != 0) {
			if (whole2 < 0 && numerator2 > 0) {
				// ex. -38_3/72, this does -38-(3/72). "Distributes" the negative in front of the int part of the mixed number
				improperNumerator2 -= numerator2; 
			} else if (whole2 > 0 && numerator2 > 0) {
				// both whole part and numerator part are positive, ex. 1_1/2
				improperNumerator2 += numerator2; 
			} 
		}	

		// for fractions whose whole number part is 0
		if (whole1 == 0) {
			improperNumerator1 = numerator1;
		}
		if (whole2 == 0) {
			improperNumerator2 = numerator2;
		}
		
		int numeratorFinal = 0;
		int denominatorFinal = 1;

		// does operations on the converted improper fractions from above
		if (operator.equals("+")) {
			numeratorFinal = improperNumerator1 * improperDenominator2 + improperNumerator2 * improperDenominator1;
			denominatorFinal = improperDenominator1 * improperDenominator2;

		} else if (operator.equals("-")) {
			numeratorFinal = improperNumerator1 * improperDenominator2 - improperNumerator2 * improperDenominator1;
			denominatorFinal = improperDenominator1 * improperDenominator2;

		} else if (operator.equals("*")) {
			numeratorFinal = improperNumerator1 * improperNumerator2;
			denominatorFinal = improperDenominator1 * improperDenominator2;

		} else if (operator.equals("/")) {
			numeratorFinal = improperNumerator1 * improperDenominator2;
			denominatorFinal = improperDenominator1 * improperNumerator2;
		}

		return simplify(numeratorFinal, denominatorFinal);
	}

	public static String simplify(int n, int d) {
		String fracFinal = "";
		// make everything positive for now, take care of negatives at the very end
		int numerator = Math.abs(n);
		int denominator = Math.abs(d);

		int gcd = gcd(numerator, denominator); 
		if (denominator / gcd == 1) { // if improper fraction can be simplified to an integer
			fracFinal = Integer.toString(numerator / gcd);

		} else if (numerator == 0) {
			fracFinal = "0"; // special corner case

		} else if (numerator < denominator) { // if fraction < 1, ex. 4/8
			fracFinal = (numerator/gcd) + "/" + (denominator/gcd);

		} else { // if fraction > 1, ex. 20/4 -> converts to mixed number 			
			fracFinal = (numerator/denominator) + "_" + ((numerator / gcd) % (denominator / gcd)) + "/" + (denominator / gcd); 
		}

		// takes care of signs using original parameters n and d that are signed. Puts negative sign in front of integer part of mixed number when appropriate
		if (Math.signum(n) == 1.0 && Math.signum(d) == 1.0) {
			return fracFinal;
		} else if ((Math.signum(n) == 1.0 && Math.signum(d) == -1.0) || (Math.signum(n) == -1.0 && Math.signum(d) == 1.0)) {
			return "-" + fracFinal;
		} else { // if (Math.signum(n) == -1.0 && Math.signum(d) == -1.0) 
			return fracFinal; 
		}
	}

	public static int gcd(int a, int b) {
		int gcd = 1;
		for (int i = 1; i <= a && i <= b; i++) {
			if (a % i == 0 && b % i == 0) {
				gcd = i; 
			}
		}
		return gcd; 
	}

	public static void intro() {
		System.out.println(
				"Welcome to the fraction calculator! Please enter your expression when prompted with the following format specifications: \n");
		System.out.println(
				"1) Fractions should be written as '3_1/2' or '12/5' with an underscore separating the whole number part. '31/2' will be interpreted as thirty-one over two as an improper fraction.\n");
		System.out.println("2) Separate each argument in the expression with a space, like this: '3_4/5 + 1/5'\n");
		System.out.println("3) Whole numbers, fractions, mixed numbers, negative numbers, are all allowed\n");
		System.out.println("4) Type 'quit' after the program prompts you to enter an expression to exit the program\n");
	}
}