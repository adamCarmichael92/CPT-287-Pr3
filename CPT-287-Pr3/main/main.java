import java.util.*;
import java.util.Stack;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {
		//Open input file, scanner, output file, and writer
		FileInputStream inputFile = new FileInputStream("input.txt");
		Scanner scanner = new Scanner(inputFile);
		
		//Scan input file and call the functions to calculate expressions from input file
		while (scanner.hasNextLine()) {
			String infixExp = scanner.nextLine();
			//insert function for converting infix to postfix
			BTNode expTree = infixToPostfix(infixExp);
			postorderTraverse(expTree);
			
			//insert function to evaluate postfix expression and print to screen

		}
		scanner.close();
		inputFile.close();
	}
	
	
	/**
	 * Turns an infix expression into a postfix expression
	 * @param infixExp: original infix expression
	 * @return: postfix expression
	 */
	public static BTNode infixToPostfix(String infixExp) {
		// Stack to hold digits
		Stack<BTNode> nodes = new Stack<>();
		// Stack to hold operators
		Stack<String> operators = new Stack<>();
		BTNode top, top1, top2;
		
		Scanner scanner = new Scanner(infixExp);
		
		while (scanner.hasNext()) {
			String token = scanner.next();
			if (token.equals("(")) {
				// push '(' to operators stack
				operators.add(token);
			}
			else if (Character.isDigit(token.charAt(0))) {
				// push numbers to nodes stack as node
				top = newNode(token);
				nodes.add(top);
			}
			else if (token.equals(")")) {
				while (!operators.isEmpty() && operators.peek() != "(") {
					top = newNode(operators.peek());
					operators.pop();
					top1 = nodes.peek();
					nodes.pop();
					top2 = nodes.peek();
					nodes.pop();
					top.left = top2;
					top.right = top1;
					nodes.add(top);
				}
				operators.pop();
			}
			else {
				while (!operators.isEmpty() && !operators.peek().equals("(") && precedence(token) <= precedence(operators.peek())) {
					// Get and remove top element from operators stack
					top = newNode(operators.peek());
					operators.pop();
					// Get and remove the top element from nodes stack
					top1 = nodes.peek();
					nodes.pop();
					// Get and remove the next element from nodes stack
					top2 = nodes.peek();
					nodes.pop();
					// Update the tree
					top.left = top2;
					top.right = top1;
					// Push the operator to the nodes stack
					nodes.add(top);
				}
				// Push token to operator stack
				operators.push(token);
			}
		}
		top = nodes.peek();
		return top;
	}
	
	
	// Binary Tree Node
	public class BTNode<T> {
		// Data Field
		public T data;
		public BTNode<T> left, right;
		
		// Constructor
		public BTNode(T value) { data = value; }
		
		public BTNode(T value, BTNode<T> leftChild, BTNode<T> rightChild) {
			data = value;
			left = leftChild;
			right = rightChild;
		}
		
	}
	
	
	// Function to create new node
	static BTNode newNode(String s) {
		BTNode<String> node = null;
		node.data = s;
		node.left = node.right = null;
		return node;
		
	}
	
	/**
	 * Postorder traverse a binary tree
	 * @param root: root node of binary tree
	 */
	public static <T> void postorderTraverse(BTNode<T> root) {
		if (root != null) {
			postorderTraverse(root.left);
			postorderTraverse(root.right);
			System.out.print(root.data.toString() + ' ');
		}
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
	
	/**
	 * Formats spaces into input 
	 * @param original: original string
	 * @return string with spaces between operands and operators
	 */
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
