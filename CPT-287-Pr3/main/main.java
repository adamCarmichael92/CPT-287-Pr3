import java.util.*;
import java.util.Stack;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class main {

	public static void main(String[] args) throws IOException {
		//Open input file, scanner, output file, and writer
		FileInputStream inputFile = new FileInputStream("E:/downloads/input.txt");
		Scanner scanner = new Scanner(inputFile);
		
		//Scan input file and call the functions to calculate expressions from input file
		while (scanner.hasNextLine()) {
			String infixExp = scanner.nextLine();
			//insert function for converting infix to postfix
			String postfix = infixToPostfix(infixExp);
			//insert function to evaluate postfix expression and print to screen
			System.out.println(evaluatePostfix(postfix));
		}
		scanner.close();
		inputFile.close();
		
		
	}
	
	
	
	
	public static int evaluatePostfix (String str) {
		
	}
	
	
	public static String infixToPostfix(String infixExp) {
		
	}
	
	
	
	/** Returns the precedence of an operator.
	* @param operator: the operator to find its precedence
	* @return: the precedence of the operator
	*/
public static int precedence(String operator) {
	//Set each operators precedence
	//The higher the precedence the higher int returned
	if (operator.equals("^")) { return 7; }
	if (operator.equals("*") || operator.equals("/") || operator.equals("%")) { return 6; }
	if (operator.equals("+") || operator.equals("-")) { return 5; }
	if (operator.equals("<") || operator.equals("<=") || operator.equals(">") || operator.equals(">=")) { return 4; }
	if (operator.equals("==") || operator.equals("!=")) { return 3; }
	if (operator.equals("&&")) { return 2; }
	if (operator.equals("||")) { return 1; }
	// Throw exception for non supported operators
	throw new IllegalArgumentException(String.format("Operator %s is not a valid operator.", operator));
}
	
	public static String format(String original) {
		// convert to character array
		char[] arr = original.toCharArray();
		// Initialize string with spaces
		StringBuilder spaced = new StringBuilder();
		// traverse the array
		for (int i = 0; i < arr.length; i++) {
			if ( arr[i] == '|' && arr[i + 1] == '|') {
				spaced.append(arr[i]);
				spaced.append(arr[i + 1] + " ");
				i++;
				i++;
			}
			else if ( arr[i] == '&' && arr[i + 1] == '&') {
				spaced.append(arr[i]);
				spaced.append(arr[i + 1] + " ");
				i++;
				i++;
			}
			else if (arr[i] == '=' && arr[i + 1] == '=') {
				spaced.append(arr[i]);
				spaced.append(arr[i + 1] + " ");
				i++;
				i++;
			}
			else if (arr[i] == '!' && arr[i + 1] == '=') {
				spaced.append(arr[i]);
				spaced.append(arr[i + 1] + " ");
				i++;
				i++;
			}
			else if (arr[i] == '>' && arr[i + 1] == '=') {
				spaced.append(arr[i]);
				spaced.append(arr[i + 1] + " ");
				i++;
				i++;
			}
			else if (arr[i] == '<' && arr[i + 1] == '=') {
				spaced.append(arr[i]);
				spaced.append(arr[i + 1] + " ");
				i++;
				i++;
			}
			// format with spaces
			spaced.append(arr[i] + " ");
		}
		return spaced.toString();
	}
	
	
	
	
	
	
	
	
	
	
}