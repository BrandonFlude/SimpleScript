package com.brandonflude.ldi.simplescript.interpreter;

// Imported for file work
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import com.brandonflude.ldi.simplescript.parser.ast.*;
import com.brandonflude.ldi.simplescript.values.*;

public class Parser implements SimpleScriptVisitor {
	
	// Global file variables
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	String fileName = "";
	String line = null;
	
	// Scope display handler
	private Display scope = new Display();
	private Display immutables = new Display();
	
	// Get the ith child of a given node.
	private static SimpleNode getChild(SimpleNode node, int childIndex) {
		return (SimpleNode)node.jjtGetChild(childIndex);
	}
	
	// Get the token value of the ith child of a given node.
	private static String getTokenOfChild(SimpleNode node, int childIndex) {
		return getChild(node, childIndex).tokenValue;
	}
	
	// Execute a given child of the given node
	private Object doChild(SimpleNode node, int childIndex, Object data) {
		return node.jjtGetChild(childIndex).jjtAccept(this, data);
	}
	
	// Execute a given child of a given node, and return its value as a Value.
	// This is used by the expression evaluation nodes.
	Value doChild(SimpleNode node, int childIndex) {
		return (Value)doChild(node, childIndex, null);
	}
	
	// Execute all children of the given node
	Object doChildren(SimpleNode node, Object data) {
		return node.childrenAccept(this, data);
	}
	
	// Called if one of the following methods is missing...
	public Object visit(SimpleNode node, Object data) {
		System.out.println(node + ": acceptor not implemented in subclass?");
		return data;
	}
	
	// Execute a SimpleScript program
	public Object visit(ASTCode node, Object data) {
		return doChildren(node, data);	
	}
	
	// Execute a statement
	public Object visit(ASTStatement node, Object data) {
		return doChildren(node, data);	
	}

	// Execute a block
	public Object visit(ASTBlock node, Object data) {
		return doChildren(node, data);	
	}

	// Function definition
	public Object visit(ASTFnDef node, Object data) {
		// Already defined?
		if (node.optimised != null)
			return data;
		// Child 0 - identifier (fn name)
		String fnname = getTokenOfChild(node, 0);
		if (scope.findFunctionInCurrentLevel(fnname) != null)
			throw new ExceptionSemantic("Function " + fnname + " already exists.");
		FunctionDefinition currentFunctionDefinition = new FunctionDefinition(fnname, scope.getLevel() + 1);
		
		// Child 1 - function definition parameter list
		doChild(node, 1, currentFunctionDefinition);
		// Add to available functions
		scope.addFunction(currentFunctionDefinition);
		
		// Here will be code for switching between no OLD definition or has a NEW Definition
		if (node.fnIsDeprecated) // run new function from definition
		{				
			// This will need to shift up 1 to the new function
			// Child 2 - function use
			currentFunctionDefinition.setFunctionUse(getChild(node, 2));
			
			// Show a system warning about this deprecation issue - but still run because I am nice :)
			String newFunctionName = getTokenOfChild(node, 2);		
			
			// Optional println for warning
			// System.out.println("WARNING: " + fnname + " is deprecated, consider using the new function: " + newFunctionName);		
			if (scope.findFunction(newFunctionName) != null)
			{
				// Child 3 - function body
				
				// Get the function body from the /other/ function, use the scope to find it (should be previously declared)
				FunctionDefinition replacementFunction = scope.findFunction(newFunctionName);
				currentFunctionDefinition.setFunctionBody(replacementFunction.getFunctionBody());
				
				// Child 4 - optional return expression
				if (node.fnHasReturn)
				{
					// If the new function has a return, set it to the contents of the @USE function
					if(replacementFunction.getFunctionReturnExpression() != null)
					{
						currentFunctionDefinition.setFunctionReturnExpression(replacementFunction.getFunctionReturnExpression());
					}
				}
			}
			else
			{
				throw new ExceptionSemantic("Couldn't find the @USE defined function. Is it declared ABOVE the old function?");
			}
		}
		else
		{
			// Child 2 - function body
			currentFunctionDefinition.setFunctionBody(getChild(node, 2));
						
			// Child 3 - optional return expression
			if (node.fnHasReturn)
			{
				currentFunctionDefinition.setFunctionReturnExpression(getChild(node, 3));
			}	
		}
	
		// Preserve this definition for future reference, and so we don't define
		// it every time this node is processed.
		node.optimised = currentFunctionDefinition;
		return data;
	}
	
	// Function definition parameter list
	public Object visit(ASTParmlist node, Object data) {
		FunctionDefinition currentDefinition = (FunctionDefinition)data;
		for (int i=0; i<node.jjtGetNumChildren(); i++)
			currentDefinition.defineParameter(getTokenOfChild(node, i));
		return data;
	}
	
	// Function body
	public Object visit(ASTFnBody node, Object data) {
		return doChildren(node, data);
	}
	
	// Function return expression
	public Object visit(ASTReturnExpression node, Object data) {
		return doChildren(node, data);
	}
	
	// Function call
	public Object visit(ASTCall node, Object data) {
		FunctionDefinition fndef;
		if (node.optimised == null) { 
			// Child 0 - identifier (fn name)
			String fnname = getTokenOfChild(node, 0);
			fndef = scope.findFunction(fnname);
			if (fndef == null)
			{
				throw new ExceptionSemantic("Function " + fnname + " is undefined.");
			}
			// Save it for next time
			node.optimised = fndef;
		} else
			fndef = (FunctionDefinition)node.optimised;
		FunctionInvocation newInvocation = new FunctionInvocation(fndef);
		// Child 1 - arglist
		doChild(node, 1, newInvocation);
		// Execute
		scope.execute(newInvocation, this);
		return data;
	}
	
	// Function invocation in an expression
	public Object visit(ASTFnInvoke node, Object data) {
		FunctionDefinition fndef;
		if (node.optimised == null) { 
			// Child 0 - identifier (fn name)
			String fnname = getTokenOfChild(node, 0);
			fndef = scope.findFunction(fnname);
			if (fndef == null)
				throw new ExceptionSemantic("Function " + fnname + " is undefined.");
			if (!fndef.hasReturn())
				throw new ExceptionSemantic("Function " + fnname + " is being invoked in an expression but does not have a return value.");
			// Save it for next time
			node.optimised = fndef;
		} else
			fndef = (FunctionDefinition)node.optimised;	
		FunctionInvocation newInvocation = new FunctionInvocation(fndef);
		// Child 1 - arglist
		doChild(node, 1, newInvocation);
		// Execute
		return scope.execute(newInvocation, this);
	}
	
	// Function invocation argument list.
	public Object visit(ASTArgList node, Object data) {
		FunctionInvocation newInvocation = (FunctionInvocation)data;
		for (int i=0; i<node.jjtGetNumChildren(); i++)
			newInvocation.setArgument(doChild(node, i));
		newInvocation.checkArgumentCount();
		return data;
	}
	
	// Execute an IF 
	public Object visit(ASTIfStatement node, Object data) {
		// evaluate boolean expression
		Value hopefullyValueBoolean = doChild(node, 0);
		if (!(hopefullyValueBoolean instanceof ValueBoolean))
			throw new ExceptionSemantic("The test expression of an if statement must be boolean.");
		if (((ValueBoolean)hopefullyValueBoolean).booleanValue())
			doChild(node, 1);							// if(true), therefore do 'if' statement
		else if (node.ifHasElse)						// does it have an else statement?
			doChild(node, 2);							// if(false), therefore do 'else' statement
		return data;
	}
	
	// Execute a FOR loop
	public Object visit(ASTForLoop node, Object data) {
		// loop initialisation
		doChild(node, 0);
		while (true) {
			// evaluate loop test
			Value hopefullyValueBoolean = doChild(node, 1);
			if (!(hopefullyValueBoolean instanceof ValueBoolean))
				throw new ExceptionSemantic("The test expression of a for loop must be boolean.");
			if (!((ValueBoolean)hopefullyValueBoolean).booleanValue())
				break;
			// do loop statement
			doChild(node, 3);
			// assign loop increment
			doChild(node, 2);
		}
		return data;
	}
	
	// Execute a WHILE loop
	public Object visit(ASTWhileLoop node, Object data) {
		while (true) {
			// evaluate loop test
			Value hopefullyValueBoolean = doChild(node, 0);
			if (!(hopefullyValueBoolean instanceof ValueBoolean))
				throw new ExceptionSemantic("The test expression of a while loop must be boolean.");
			if (!((ValueBoolean)hopefullyValueBoolean).booleanValue())
				break;
			// do loop statement
			doChild(node, 1);
		}
		return data;
	}
	
	// Process an identifier
	// This doesn't do anything, but needs to be here because we need an ASTIdentifier node.
	public Object visit(ASTIdentifier node, Object data) {
		return data;
	}
	
	// Execute the WRITE statement
	public Object visit(ASTWrite node, Object data) {
		Display.ArrayReference arrayReference;
		Display.Reference reference;
		
		// Find number of nodes
		int numOfChildren = node.jjtGetNumChildren();
		String stringBuilder = "";
				 
		// Loop through all the children, appending to a string as we go
		for(int c = 0; c < numOfChildren; c++)
		{
			// Check whether the value is in [] or not, the node should be set as writeIsFromArray
			if(node.writeIsFromArray == true)
			{
				int indexToFind;
				String name = getTokenOfChild(node, 0);

				if(immutables.findArray(name) != null)
				{
					arrayReference = immutables.findArray(name);
				}
				else
				{
					throw new ExceptionSemantic("Variable or parameter " + name + " is undefined.");
				}
				
				// If it's a number, convert - else find the variable user is using.
				if(getTokenOfChild(node, 1).matches("^[0-9]"))
				{
					indexToFind = Integer.parseInt(getTokenOfChild(node, 1));
				}
				else
				{
					String paramName = getTokenOfChild(node, 1);
					reference = scope.findReference(paramName);	
					if (reference == null) {
						// Look in constants, and then throw an error
						if(immutables.findReference(paramName) != null)
						{	
							throw new ExceptionSemantic("Variable or parameter " + paramName + " is undefined.");
						}
					}
					indexToFind = reference.getValue().intValue();
				}
				
				stringBuilder = arrayReference.getValue(indexToFind).toString();
			}
			else
			{
				stringBuilder = stringBuilder + doChild(node, c);  
			}	
		}

		System.out.println(stringBuilder);
		return data;
	}
	
	// Dereference a variable or parameter, and return its value.
	public Object visit(ASTDereference node, Object data) {
		Display.Reference reference;
		if (node.optimised == null) {
			String name = node.tokenValue;
			reference = scope.findReference(name);
			if (reference == null)
			{
				// Look in constants, and fetch the value from there instead
				if(immutables.findReference(name) != null)
				{
					reference = immutables.findReference(name);
				}
				else
					throw new ExceptionSemantic("Variable or parameter " + name + " is undefined.");
			}
			node.optimised = reference;
		} else
			reference = (Display.Reference)node.optimised;
		return reference.getValue();
	}
	
	// Execute an assignment statement.
	public Object visit(ASTAssignment node, Object data) {
		Display.Reference reference;
		Display.ArrayReference arrayReference = null;
		boolean allow_set = false;
	
		if (node.optimised == null) {
			String name = getTokenOfChild(node, 0);
			
			// Arrays are immutable in SimpleScript
			if(node.assignmentIsArray) 
			{				
				arrayReference = immutables.findArray(name);
				if(arrayReference == null)
				{
					// Create it
					arrayReference = immutables.defineArray(name);
					// Set flag to allow this to be set first time around.
					allow_set = true;
				}
				node.optimised = arrayReference;
			}

			if(node.assignmentIsConst) 
			{
				// Add this to a separate scope instead, perhaps one named constants
				reference = immutables.findReference(name);
				if(reference == null)
				{
					reference = immutables.defineVariable(name);
					// Set flag to allow this to be set first time around.
					allow_set = true;
				}
				node.optimised = reference;
			}
			else
			{	
				reference = scope.findReference(name);
				if (reference == null)
				{
					reference = scope.defineVariable(name);
				}
				node.optimised = reference;
			}	
		} else {
			reference = (Display.Reference)node.optimised;
		}
		
		// Check here somehow if reference is to an array, const or normal var
		if (immutables.findReference(getTokenOfChild(node, 0)) == null || allow_set == true)
		{
			if(node.assignmentIsArray)
			{
				// Loop through children and do child
				for (int i = 1; i < node.jjtGetNumChildren(); i++)
				{
					arrayReference.setValue(doChild(node, i), i - 1);
				}
			}
			else
			{
				// Case for setting constants on creation
				reference.setValue(doChild(node, 1));
			}	
		}
		else
		{
			throw new ExceptionSemantic("You cannot modify a constant.");
		}
		return data;
	}

	// OR
	public Object visit(ASTOr node, Object data) {
		return doChild(node, 0).or(doChild(node, 1));
	}

	// AND
	public Object visit(ASTAnd node, Object data) {
		return doChild(node, 0).and(doChild(node, 1));
	}

	// EQUAL TO
	public Object visit(ASTCompEqual node, Object data) {
		return doChild(node, 0).eq(doChild(node, 1));
	}

	// NOT EQUAL TO
	public Object visit(ASTCompNequal node, Object data) {
		return doChild(node, 0).neq(doChild(node, 1));
	}

	// >=
	public Object visit(ASTCompGTE node, Object data) {
		return doChild(node, 0).gte(doChild(node, 1));
	}

	// <=
	public Object visit(ASTCompLTE node, Object data) {
		return doChild(node, 0).lte(doChild(node, 1));
	}

	// >
	public Object visit(ASTCompGT node, Object data) {
		return doChild(node, 0).gt(doChild(node, 1));
	}

	// <
	public Object visit(ASTCompLT node, Object data) {
		return doChild(node, 0).lt(doChild(node, 1));
	}

	// +
	public Object visit(ASTAdd node, Object data) {
		return doChild(node, 0).add(doChild(node, 1));
	}

	// -
	public Object visit(ASTSubtract node, Object data) {
		return doChild(node, 0).subtract(doChild(node, 1));
	}
	
	// ++
	public Object visit(ASTAdd1 node, Object data) {
		Display.Reference reference;
		if (node.optimised == null) {
			String name = getTokenOfChild(node, 0);
			reference = scope.findReference(name);	
			if (reference == null) {
				// Look in constants, and then throw an error
				if(immutables.findReference(name) != null)
				{
					throw new ExceptionSemantic("Variable: " + name + " cannot be changed.");
				}
				else
					throw new ExceptionSemantic("Variable: " + name + " was not found.");
			}
		} else {
			reference = (Display.Reference)node.optimised;
		}
		reference.setValue(reference.getValue().add1());
		return data;
	}
	
	// --
	public Object visit(ASTSubtract1 node, Object data) {
		Display.Reference reference;
		if (node.optimised == null) {
			String name = getTokenOfChild(node, 0);
			reference = scope.findReference(name);	
			if (reference == null) {
				// Look in constants, and then throw an error
				if(immutables.findReference(name) != null)
				{
					throw new ExceptionSemantic("Variable: " + name + " cannot be changed.");
				}
				else
					throw new ExceptionSemantic("Variable: " + name + " was not found.");
			}
		} else {
			reference = (Display.Reference)node.optimised;
		}
		reference.setValue(reference.getValue().subtract1());
		return data;
	}
	
	public Object visit(ASTPE node, Object data) {	
		Display.Reference reference;
		if (node.optimised == null) {
			String name = getTokenOfChild(node, 0);
			reference = scope.findReference(name);	
			if (reference == null) {
				// Look in constants, and then throw an error
				if(immutables.findReference(name) != null)
				{
					throw new ExceptionSemantic("Variable: " + name + " cannot be changed.");
				}
				else
					throw new ExceptionSemantic("Variable: " + name + " was not found.");
			}
		} else {
			reference = (Display.Reference)node.optimised;
		}	
	
		reference.setValue(reference.getValue().add(doChild(node, 1)));
		return data;
	}
	
	public Object visit(ASTME node, Object data) {
		Display.Reference reference;
		if (node.optimised == null) {
			String name = getTokenOfChild(node, 0);
			reference = scope.findReference(name);	
			if (reference == null) {
				// Look in constants, and then throw an error
				if(immutables.findReference(name) != null)
				{
					throw new ExceptionSemantic("Variable: " + name + " cannot be changed.");
				}
				else
					throw new ExceptionSemantic("Variable: " + name + " was not found.");
			}
		} else {
			reference = (Display.Reference)node.optimised;
		}
		
		reference.setValue(reference.getValue().subtract(doChild(node, 1)));
		return data;
	}
	
	// *
	public Object visit(ASTTimes node, Object data) {
		return doChild(node, 0).mult(doChild(node, 1));
	}

	// /
	public Object visit(ASTDivide node, Object data) {
		return doChild(node, 0).div(doChild(node, 1));
	}

	// NOT
	public Object visit(ASTUnaryNot node, Object data) {
		return doChild(node, 0).not();
	}

	// + (unary)
	public Object visit(ASTUnaryPlus node, Object data) {
		return doChild(node, 0).unary_plus();
	}

	// - (unary)
	public Object visit(ASTUnaryMinus node, Object data) {
		return doChild(node, 0).unary_minus();
	}

	// Return string literal
	public Object visit(ASTCharacter node, Object data) {
		if (node.optimised == null)
			node.optimised = ValueString.stripDelimited(node.tokenValue);
		return node.optimised;
	}

	// Return integer literal
	public Object visit(ASTInteger node, Object data) {
		if (node.optimised == null)
			node.optimised = new ValueInteger(Integer.parseInt(node.tokenValue));
		return node.optimised;
	}

	// Return floating point literal
	public Object visit(ASTRational node, Object data) {
		if (node.optimised == null)
			node.optimised = new ValueRational(Double.parseDouble(node.tokenValue));
		return node.optimised;
	}

	// Return true literal
	public Object visit(ASTTrue node, Object data) {
		if (node.optimised == null)
			node.optimised = new ValueBoolean(true);
		return node.optimised;
	}

	// Return false literal
	public Object visit(ASTFalse node, Object data) {
		if (node.optimised == null)
			node.optimised = new ValueBoolean(false);
		return node.optimised;
	}
	
	public Object visit(ASTExit node, Object data) {
		System.exit(0);
		return node.optimised;
	}
	
	public Object visit(ASTOpenFile node, Object data) {
		Display.Reference reference;
		if (node.optimised == null)
		{
			// Get variable name
			String name = getTokenOfChild(node, 0);
			reference = scope.findReference(name);
			if(reference != null)
			{
				fileName = reference.getValue().toString();
			}
			else if(immutables.findReference(name) != null)
			{
				// Perhaps it's a constant
				reference = immutables.findReference(name);
				fileName = reference.getValue().toString();
			}
			else
			{
				// Treat this as a raw file name
				fileName = doChild(node, 0).toString();
			}
		}
	
		// Open file using standard Java methods
		try {
            fileReader = new FileReader(fileName);
		} catch(FileNotFoundException ex) {
			throw new ExceptionSemantic("Unable to open file " + fileName);
        }
		
		return node.optimised;
	}
	
	public Object visit(ASTReadFile node, Object data) {
		// Read contents of the file opened through OPEN keyword
        bufferedReader = new BufferedReader(fileReader);
        int i = 1;
        
        try {
        	while((line = bufferedReader.readLine()) != null) {
        		if(node.readLineFromFile == true)
        		{
        			// Defined here to stop 'null' when reading the whole file
        			int lineToRead = Integer.parseInt(getTokenOfChild(node, 0));
        			if(i == lineToRead)
        			{
        				System.out.println(line);
        			}
        			i++;
        		}
        		else
        		{
        			System.out.println(line);
        		}   
            }   
		} catch(IOException ex) {
            throw new ExceptionSemantic("Unable to read a file. Did you OPEN it yet?");
        }
        
		return node.optimised;
	}
	
	public Object visit(ASTEditFile node, Object data) {		
		// Concatenate all the text into one string
		int numOfChildren = node.jjtGetNumChildren();
		String text = "";
		
		// Loop through all the children, appending to a string as we go
		for(int c = 0; c < numOfChildren; c++)
		{
			if(c == 0)
			{
				text = text + doChild(node, c).toString();
			}
			else
			{
				text = text + " " + doChild(node, c).toString();
			}
			
		}
		
		try {
            FileWriter fileWriter = new FileWriter(fileName, true);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            
            // Append line
            bufferedWriter.write(text + "\n");
            
            // Close the writer
            bufferedWriter.close();
        }
        catch(IOException ex) {
        	throw new ExceptionSemantic("Unable to edit this file.");
        }
		return node.optimised;
	}
	
	public Object visit(ASTCloseFile node, Object data) {
		// Files should always be closed, but we'll let the user do this.
		try {
			fileReader.close();
        	bufferedReader.close();
        	fileName = "";
		} catch(IOException ex) {
			throw new ExceptionSemantic("Unable to close a file.");
        }
		return node.optimised;
	}
	
	public Object visit(ASTClearFile node, Object data) {
		try {
            FileWriter fileWriter = new FileWriter(fileName, false);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        
            // Close the writer
            bufferedWriter.close();
        }
        catch(IOException ex) {
        	throw new ExceptionSemantic("Unable to clear the opened file.");
        }
		
		return node.optimised;
	}
	
	public Object visit(ASTSort node, Object data) {
		String name = getTokenOfChild(node, 0);
		if(immutables.findArray(name) != null)
		{
			// arrayReference contains the array
			Display.ArrayReference arrayReference = immutables.findArray(name);
			if(arrayReference != null)
			{
				// Sort this array
				arrayReference.sort(name);
			}
			node.optimised = arrayReference;
		}
		else
		{
			throw new ExceptionSemantic("Variable or parameter " + name + " is undefined or is not an array.");
		}
		return node.optimised;
	}
	
	public Object visit(ASTSizeOf node, Object data) {
		String name = getTokenOfChild(node, 0);
		if(immutables.findArray(name) != null)
		{
			Display.ArrayReference arrayReference = immutables.findArray(name);
			int arraySize = arrayReference.getSize(name);
			
			// Set node to the value of this, allows for j: sizeof array
			node.optimised = new ValueInteger(arraySize);
		}
		else
		{
			throw new ExceptionSemantic("Variable or parameter " + name + " is undefined or is not an array.");
		}
		return node.optimised;
	}
	
	public Object visit(ASTRand node, Object data) {
		Random random = new Random();
		int randNum;
		
		if(node.randHasParams == true)
		{
			int min = doChild(node, 0).intValue();
			int max = doChild(node, 1).intValue();
			
			randNum = random.nextInt(max - min) + min;
		}
		else
		{
			// Generate number between 0 and 100
			randNum = random.nextInt(100);
		}
		
		node.optimised = new ValueInteger(randNum);
		return node.optimised;
	}
	
	public Object visit(ASTInput node, Object data) {		
		// First child should be the question
		System.out.println(doChild(node, 0).toString());
		
		Scanner reader = new Scanner(System.in);
		
		// Read in response
		String response = reader.next(); 
		
		// Close reader
		reader.close();
		
		// Return response
		return response;
	}
	
	public Object visit(ASTDictCreate node, Object data) {
		Display.DictReference dictReference = null;
		String name = getTokenOfChild(node, 0);
		dictReference = scope.findDictionary(name);
		if(dictReference == null)
		{
			// Create Dictionary
			dictReference = scope.defineDictionary(name);
		}
		node.optimised = dictReference;
		return node.optimised;
	}
		
	public Object visit(ASTDictAdd node, Object data) {
		Display.DictReference dictReference;
		String name = getTokenOfChild(node, 0);
		String key = doChild(node, 1).toString();
		Value value = doChild(node, 2);
		
		dictReference = scope.findDictionary(name);
		
		if(dictReference != null)
		{
			dictReference.setValue(key, value);
		}
		else
		{
			throw new ExceptionSemantic("Dictionary " + name + " is undefined.");
		}	
		
		node.optimised = dictReference;
		return node.optimised;
	}
	
	public Object visit(ASTDictWrite node, Object data) {
		Display.Reference reference;
		Display.DictReference dictReference;
		String name = getTokenOfChild(node, 0);
		Value keyToFind = doChild(node, 1);		 // doChild fails if it's a variable
		Value value;
			
		dictReference = scope.findDictionary(name);
		if(dictReference != null)
		{
			value = dictReference.getValue(keyToFind.toString());
			if(value != null)
			{
				System.out.println(value);
			}
			else
			{
				throw new ExceptionSemantic("Dictionary " + name + " doesn't contain a value for key: " + keyToFind + ".");
			}
		}
		else
		{
			throw new ExceptionSemantic("Dictionary " + name + " is undefined.");
		}		
		return data;
	}
}
