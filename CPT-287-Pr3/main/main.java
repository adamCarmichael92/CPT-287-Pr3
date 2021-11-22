import java.util.*;
import java.util.Stack;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class main {
	
	public static class TreeNode {
		String val;
		TreeNode left, right;
		TreeNode(String item) {
			val = item;
			left = right = null;
		}
	}
	
	//converts a string with no whitespace to a string with whitespace
	//returns a string with whitespace
	//example: changes 2+2 to 2 + 2
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
	
	
	public static TreeNode infixToPostfix(String infixExp) {
		// Initialize an empty stack
		Stack<String> stk = new Stack<>();
		// Initialize postfix string
		StringBuilder postfix = new StringBuilder();
				
		infixExp = format(infixExp);
				
		Scanner scanner = new Scanner(infixExp);
		while (scanner.hasNext()) {
			String token = scanner.next();
			if (Character.isDigit(token.charAt(0))) { postfix.append(token).append(' '); }
			// opening parenthesis
			else if (token.equals("(")) { stk.push(token); }
			// closing parenthesis
			else if (token.equals(")")) {
				while (!stk.peek().equals("(")) { postfix.append(stk.pop()).append(' '); }
				stk.pop();
			}
			// operator
			else {
				while (!stk.isEmpty() && !stk.peek().equals("(") && precedence(token) <= precedence(stk.peek())) {
		                  postfix.append(stk.pop()).append(' ');
				}
					// Push the current operator onto the stack.
		            stk.push(token);
			}
		}
			// Pop the remaining operators from the stack and append them to the postfix expression string.
		    while (!stk.isEmpty()) { postfix.append(stk.pop()).append(' '); }
	        scanner.close();
	        String postfixExp = postfix.toString();
	        
	        //Create expression tree with the postfix expression
	        Stack<TreeNode> stack = new Stack<TreeNode>();
	        Scanner scan = new Scanner(postfixExp);
	        TreeNode p = null, c1 = null, c2 = null;
	        while (scan.hasNext()) {
	        	
	        	String tkn = scan.next();
	        	//if operand push to stack
	        	if (Character.isDigit(tkn.charAt(0))) { 
	        		p = new TreeNode(tkn);
	        		stack.push(p);
	        	} // if operator: create nodes of tree by
	        	  // popping two values from stack and assign
	        	  // them as the children of the operator
	        	else {
	        		p = new TreeNode(tkn);
	        		c1 = stack.pop();
	        		c2 = stack.pop();
	        		p.right = c1;
	        		p.left = c2;
	        		//push parent to stack
	        		stack.push(p);
	        	}
	        }
	    scan.close();
	    stack.pop();
	    TreeNode root = p;
		return root;
	}
	
	public static int evaluatePostfix(TreeNode root, Stack<Integer> stack) {
		
		//Postorder traverse the tree and calculate the expression 
		if (root != null) {
			if (root.left != null) {
				evaluatePostfix(root.left, stack);
			}
			if (root.right != null) {
				evaluatePostfix(root.right, stack);
			}
			//If node is an operand push to the stack
			if (Character.isDigit(root.val.charAt(0))) {
				stack.push(Integer.valueOf(root.val));
			} else {
				//Node is an operator: pop top two values in stack and evaluate the
				//expression with the appropriate operator.
				int right = stack.pop();
				int left = stack.pop();
				if (root.val.equals("^")) { stack.push((int) Math.pow(left, right)); }
				else if (root.val.equals("*")) { stack.push(left * right); }
				else if (root.val.equals("/")) {  
					if (right == 0) {
						throw new IllegalArgumentException("Divide by zero.");
					} else {
						stack.push(left / right);
					}
				}
				else if (root.val.equals("%")) { stack.push(left % right); }
				else if (root.val.equals("+")) { stack.push(left + right); }
				else if (root.val.equals("-")) { stack.push(left - right); }
				else if (root.val.equals(">")) {  
					if (left > right) { stack.push(1); }
					else { stack.push(0); }
				}
				else if (root.val.equals(">=")) {  
					if (left >= right) { stack.push(1); }
					else { stack.push(0); }
				}
				else if (root.val.equals("<")) {  
					if (left < right) { stack.push(1); }
					else { stack.push(0); }
				}
				else if (root.val.equals("<=")) {  
					if (left <= right) { stack.push(1); }
					else { stack.push(0); }
				}
				else if (root.val.equals("==")) {  
					if (left == right) { stack.push(1); }
					else { stack.push(0); }
				}
				else if (root.val.equals("!=")) {  
					if (left != right) { stack.push(1); }
					else { stack.push(0); }
				}
				else if (root.val.equals("&&")) {
					if (left == 1 && right == 1) { stack.push(1); }
					else { stack.push(0); }
				}
				else if (root.val.equals("||")) {
					if (left == 1 || right == 1) { stack.push(1); }
					else { stack.push(0); }
				}
			}
		}
		return stack.peek();
	}
	
	
	public static void main(String[] args) throws IOException {
		//Open input file, scanner, output file, and writer
		FileInputStream inputFile = new FileInputStream("input.txt");
		Scanner scanner = new Scanner(inputFile);
		
		//Scan input file and call the functions to calculate expressions from input file
		while (scanner.hasNextLine()) {
			String infixExp = scanner.nextLine();
			//insert function for converting infix to postfix
			TreeNode postfix = infixToPostfix(infixExp);
			//insert function to evaluate postfix expression and print to screen
			Stack<Integer> stack = new Stack<Integer>();
			System.out.println(evaluatePostfix(postfix, stack));
		}
		scanner.close();
		inputFile.close();
		
		
	}


}
