package BDD;

/**
 * Reduced Ordered BDD implementation for ISSTAC
 */

import java.util.*;
import java.io.*;

/**
 * Node: any node in a BDD has a variable name and two branches for 0 & 1 this variable is assigned
 */
class Node {
	private String        mVariableName;
	private Node          mFalseChild;
	private Node          mTrueChild;
	
	// ONLY used for printing whether a Node is reused
	private static int    mCount;
	private int           mNumber;
	
	public Node(String varName) {
		mVariableName = varName;
		mFalseChild = null;
		mTrueChild = null;
		mNumber = mCount++;
	}
	
	public boolean hasSameChildren(Node fChild, Node tChild) {
		return (fChild == mFalseChild && tChild == mTrueChild);
	}
	
	public String getVariableName() {
		return mVariableName;
	}
	
	public Node getFalseChild() {
		return mFalseChild;
	}
	
	public Node getTrueChild() {
		return mTrueChild;
	}
	
	public void setFalseChild(Node fChild) {
		mFalseChild = fChild;
	}
	
	public void setTrueChild(Node tChild) {
		mTrueChild = tChild;
	}
	
	public int getNodeNumber() {
		return mNumber;
	}
};

/**
 * BDD class 
 */
public class BDD {
	// two leaves (0 and 1) with the corresponding names to indicate they're terminals
	private Node                              mZeroNode;
	private Node                              mOneNode;
	private Node                              mRoot;
	
	// a hash table is used to look up for a variable's order
	private Hashtable<String, Integer>        mLookupTable;
	
	private LinkedList<Object>                mPostfix;
	
	private Hashtable<String, HashSet<Node> > mNodeTable;
	
	/**
	 * note that: a boolean function can be written in any form
	 * the precedence of boolean operators are given as follows:
	 * (of course parentheses always precedes the others)
	 * 1. ~ (negation)
	 * 2. & (conjunction) 
	 * 3. ^ (exclusion)
	 * 4. | (disjunction)
	 * 5. > (implication)
	 * also, for each binary operator, the association is from LEFT to RIGHT
	 */
	public static void main(String[] args) {
		//String boolFunc = "~x0 & (x0 | x2) & (x1 ^ x3) & (x2 > x3)";
		String boolFunc = "(x0 | x1) & (x2 | x3)";
		String[] varOrder = {"x0", "x2", "x1", "x3"};
		BDD bdd = new BDD(boolFunc, varOrder);
	}
	
	/**
	 * BDD constructor
	 */
	public BDD(String boolFunc, String[] varOrder) {
		mZeroNode = new Node("ZERO");
		mOneNode = new Node("ONE");
		mLookupTable = new Hashtable<String, Integer>();
		mPostfix = new LinkedList<Object>();
		mNodeTable = new Hashtable<String, HashSet<Node> >();
		
		for (int i = 0; i < varOrder.length; i++)
			mLookupTable.put(varOrder[i], i);
		
		mLookupTable.put("ZERO", Integer.MAX_VALUE);
		mLookupTable.put("ONE", Integer.MAX_VALUE);
		
		parseBooleanFunction(boolFunc);
		
		mRoot = constructBDD();
		printBDD(mRoot, 0);
	}
	
	/**
	 * get operator precedence level
	 */
	private int getPrecedence(String op) {
		switch (op) {
			case "~": 	return 0;
			case "&": 	return 1;
			case "^": 	return 2;
			case "|": 	return 3;
			case ">": 	return 4;
			default: 	return 5;  // in the case of "("
		}
	}
	
	/**
	 * parse a boolean function: transform the infix format into postfix format
	 */
	private void parseBooleanFunction(String boolFunc) {
		// use a stack to achieve infix -> postfix
		Stack<String> opStack = new Stack<String>();
		
		String regex = "((?<=%1$s)|(?=%1$s))";
		String delimiters = "[~&^|>()]";
		for (String element : boolFunc.split(String.format(regex, delimiters))) {
			String str = element.trim();
			if (str.isEmpty())
				continue;
			else if (str.equals("("))
				opStack.push(str);
			else if (str.equals(")")) {
				String op = opStack.empty() ? "(" : opStack.pop();
				while (!op.equals("(")) {
					mPostfix.add(op);
					op = opStack.pop();
				}
			}
			else if (str.equals("~") || str.equals("&") || str.equals("^") || str.equals("|") || str.equals(">")) {
				while (!opStack.empty() && getPrecedence(opStack.peek()) <= getPrecedence(str)) {
					String op = opStack.pop();
					mPostfix.add(op);
				}
				opStack.push(str);
			}
			else {
				Node varNode = buildNode(str, mZeroNode, mOneNode);
				mPostfix.add(varNode);
			}
		}
		
		while (!opStack.empty()) {
			String op = opStack.pop();
			mPostfix.add(op);
		}
	}
	
	/**
	 * construct BDD according to the parsed formula stored in a linked list
	 */
	private Node constructBDD() {
		ListIterator<Object> iter = mPostfix.listIterator();
		while (iter.hasNext()) {
			Object obj = iter.next();
			if (obj instanceof Node)
				continue;
			
			// this object is a string that gives the boolean operation
			String op = (String)obj;
			int operandNum = op.equals("~") ? 1 : 2;
			iter.remove();
			
			Node result = null;
			Node[] operand = new Node[operandNum];
			
			for (int i = 0; i < operandNum; i++) {
				Object prev = iter.hasPrevious() ? iter.previous() : null;
				if (prev == null || !(prev instanceof Node))
					return null;
				operand[i] = (Node)prev;
				iter.remove();
			}
			
			// unary operator v.s. binary operator
			if (operandNum == 1)
				result = operateBDD(op, operand[0], mZeroNode);
			else
				result = operateBDD(op, operand[1], operand[0]);
			
			iter.add(result);
		}
		
		Object root = mPostfix.removeFirst();
		if (!mPostfix.isEmpty() || !(root instanceof Node))
			return null;
		
		return (Node)root;
	}
	
	private Node evaluateToTerminalNode(String op, boolean x, boolean y) {
		boolean val = false;
		// take advantage of Java can switch on strings
		switch (op) {
			case "~":
				val = !x;
				break;
			case "&": 
				val = x & y;
				break;
			case "^":
				val = x ^ y;
				break;
			case "|": 
				val = x | y;
				break;
			case ">":
				val = !x | y;
				break;
		}
		
		if (val)
			return mOneNode;
		else
			return mZeroNode;
	}
	
	/**
	 * if a node with the same name & two children already exists, return it directly;
	 * otherwise, build this node
	 */
	private Node buildNode(String varName, Node fChild, Node tChild) {
		if (fChild == tChild)
			return fChild;
		
		if (!mNodeTable.containsKey(varName)) {
			HashSet<Node> nodeSet = new HashSet<Node>();
			mNodeTable.put(varName, nodeSet);
		}
		
		HashSet<Node> nodeSet = mNodeTable.get(varName);
		for (Node node : nodeSet) {
			if (node.hasSameChildren(fChild, tChild))
				return node;
		}
		
		Node node = new Node(varName);
		node.setFalseChild(fChild);
		node.setTrueChild(tChild);
		
		nodeSet.add(node);
		
		return node;
	}
		
	/**
	 * apply an operation on two BDDs
	 */
	private Node operateBDD(String op, Node node1, Node node2) {
		if (node1 == null || node2 == null)
			return null;
		
		String varName1 = node1.getVariableName();
		Node fChild1 = node1.getFalseChild();
		Node tChild1 = node1.getTrueChild();
		
		String varName2 = node2.getVariableName();
		Node fChild2 = node2.getFalseChild();
		Node tChild2 = node2.getTrueChild();
		
		Integer id1 = mLookupTable.get(varName1);
		Integer id2 = mLookupTable.get(varName2);
		if (id1 == null || id2 == null)
			return null;
		
		int idInt1 = id1.intValue();
		int idInt2 = id2.intValue();
		
		if (idInt1 == Integer.MAX_VALUE && idInt2 == Integer.MAX_VALUE) {
			boolean b1 = node1 == mZeroNode ? false : true;
			boolean b2 = node2 == mZeroNode ? false : true;
			return evaluateToTerminalNode(op, b1, b2);
		}
		else if (idInt1 == idInt2) {
			Node fChild = operateBDD(op, fChild1, fChild2);
			Node tChild = operateBDD(op, tChild1, tChild2);
			return buildNode(varName1, fChild, tChild);
		}
		else if (id1.intValue() < id2.intValue()) {
			Node fChild = operateBDD(op, fChild1, node2);
			Node tChild = operateBDD(op, tChild1, node2);
			return buildNode(varName1, fChild, tChild);
		}
		else {
			Node fChild = operateBDD(op, node1, fChild2);
			Node tChild = operateBDD(op, node1, tChild2);
			return buildNode(varName2, fChild, tChild);
		}
	}
	
	/**
	 * replicate the given BDD till reaching a node having an ID not smaller than the given one
	private Node replicateBDD(Node node, int till) {
		Integer id = mLookupTable.get(node);
		if (id == null || id.intValue() >= till)
			return node;
		
		Node newNode = new Node(node.getVariableName());
		Node fChild = replicateBDD(node.getFalseChild(), till);
		Node tChild = replicateBDD(node.getTrueChild(), till);
		
		newNode.setFalseChild(fChild);
		newNode.setTrueChild(tChild);
		
		return newNode;
	}
	*/
	
	private void printBDD(Node node, int indent) {
		if (node == null)
			return;
		
		for (int i = 0; i < indent; i++)
			System.out.print('\t');
		System.out.print("Variable Name: " + node.getVariableName());
		System.out.println("\tNode Number: " + Integer.toString(node.getNodeNumber()));
		
		for (int i = 0; i < indent + 1; i++)
			System.out.print('\t');
		System.out.println("Zero Branch: ");
		printBDD(node.getFalseChild(), indent + 1);
		
		for (int i = 0; i < indent + 1; i++)
			System.out.print('\t');
		System.out.println("One Branch: ");
		printBDD(node.getTrueChild(), indent + 1);
	}
};




