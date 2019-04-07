package com.brandonflude.ldi.simplescript.interpreter;

/** This is the base class for every AST node.  
 * 
 * @author dave
 *
 */
public class BaseASTNode {
	// The actual source code from which the token was constructed.  Only set on literals, etc.
	public String tokenValue = null;
	
	// Set at parse-time in an IF ... ELSE construct to indicate to the compiler
	// or interpreter whether or not an IF clause has an ELSE.	
	public boolean ifHasElse = false;
	
	// Set at parse-time in a function definition to indicate whether or not the function
	// has a return value.
	public boolean fnHasReturn = false;
	
	// Set at parse-time in a function definition to indicate whether or not the function
	// has been marked as old and has an alternative.
	public boolean fnIsDeprecated = false;
	
	// References an object that optimises execution of the node.  For example, it might 
	// reference a compiled function definition, so that the function needn't be redefined
	// on every execution.	
	public Object optimised = null;
	
	// Set an assignment to indicate whether it is an array or not
	public boolean assignmentIsArray = false;
	
	// Set a write statement to show it is being concatenated with another value when printed out
	public boolean writeIsConcatenation = false;
}
