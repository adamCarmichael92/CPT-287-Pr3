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
		//initialize two stacks: one for tree nodes and one for characters {operators, and "(", ")"}
		Stack<TreeNode> stackNodes = new Stack<>();
		Stack<String> stackChars = new Stack<>();
		//Create nodes to build tree
		TreeNode p = null, c1 = null, c2 = null;
		
		//Format string to include spaces
		infixExp = format(infixExp);
		
		//Scan through string
		Scanner scanner = new Scanner(infixExp);
		while (scanner.hasNext()) {
			String token = scanner.next();
			//If character is an open parentheses push to char stack
			if (token.equals("(")) { 
				stackChars.push(token); 
			}
			//if character is an operand create tree node and push to node stack
			else if (Character.isDigit(token.charAt(0))) {
				p = new TreeNode(token);
				stackNodes.push(p);
			}
			//If character is a closing parentheses
			else if (token.equals(")")) {
				while (!stackChars.isEmpty() && !stackChars.peek().equals("(")) {
					//create a tree node with top operator in stack chars and remove it from the stack
					p = new TreeNode(stackChars.pop());
					//create tree nodes for the children and remove them from the nodes stack
					c1 = stackNodes.peek();
					stackNodes.pop();
					c2 = stackNodes.peek();
					stackNodes.pop();
					//assign the children to the parent
					p.left = c2;
					p.right = c1;
					//push parent back to node stack
					stackNodes.push(p);
				}
				stackChars.pop();
			}
			//If character is an operator
			else {
				//Check to see if chars stack is empty, has an open parentheses on top, and check the precedence of token
				while (!stackChars.isEmpty() && !stackChars.peek().equals("(") && precedence(token) <= precedence(stackChars.peek())) {
					//create a tree node with top operator in stack chars and remove it from the stack
					p = new TreeNode(stackChars.peek());
					stackChars.pop();
					//create tree nodes for the children and remove them from the nodes stack
					c1 = stackNodes.peek();
					stackNodes.pop();
					c2 = stackNodes.peek();
					stackNodes.pop();
					//assign the children to the parent
					p.left = c2;
					p.right = c1;
					//push parent back to node stack
					stackNodes.push(p);
				}
				stackChars.push(token);
			}
		}
		while (!stackChars.isEmpty() && !stackNodes.isEmpty()) {
			p = new TreeNode(stackChars.pop());
			c1 = stackNodes.pop();
			c2 = stackNodes.pop();
			p.right = c1;
			p.left = c2;
			stackNodes.push(p);
		}
		scanner.close();
		p = stackNodes.peek();
		return p;
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
