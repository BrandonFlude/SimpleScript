package com.brandonflude.ldi.simplescript.interpreter;

import com.brandonflude.ldi.simplescript.values.Value;

/** A display manages run-time access to variable and parameter scope where
 *  functions may be nested.
 */ 
class Display {

	private final int maximumFunctionNesting = 64;
	private FunctionInvocation[] display = new FunctionInvocation[maximumFunctionNesting];
	private ArrayInvocation[] arrays = new ArrayInvocation[maximumFunctionNesting];
	private int currentLevel;
	private int arrayCounter = 0;

	/** Reference to a slot. */
	class Reference {
		private int displayDepth;
		private int slotNumber;
		
		/** Ctor */
		Reference(int depth, int slot) {
			displayDepth = depth;
			slotNumber = slot;
		}
		
		/** Set value pointed to by this reference. */
		void setValue(Value v) {
			display[displayDepth].setValue(slotNumber, v);
		}
		
		/** Get value pointed to by this reference. */
		Value getValue() {
			return display[displayDepth].getValue(slotNumber);
		}
	}
	
	/** Reference to a slot for arrays. */
	class ArrayReference {
		private int displayDepth;
		private int slotNumber;
		
		/** Ctor */
		public ArrayReference(int currentLevel, int slot) {
			displayDepth = currentLevel;
			slotNumber = slot;
		}
		
		/** Set value pointed to by this reference. */
		void setValue(Value v, int position) {
			arrays[displayDepth].setValue(position, v);
		}
		
		/** Get value pointed to by this reference. */
		Value getValue(int index) {
			return arrays[displayDepth].getValue(index);
		}
	}
	
	/** Ctor */
	Display() {
		// root or 0th scope
		currentLevel = 0;
		display[currentLevel] = new FunctionInvocation(new FunctionDefinition("%main", currentLevel));
	}
	
	/** Execute a function in its scope, using a specified parser. */
	Value execute(FunctionInvocation fn, Parser p) {
		int changeLevel = fn.getLevel();
		FunctionInvocation oldContext = display[changeLevel];
		int oldLevel = currentLevel;
		display[changeLevel] = fn;
		currentLevel = changeLevel;
		Value v = display[currentLevel].execute(p);
		display[changeLevel] = oldContext;
		currentLevel = oldLevel;
		return v;
	}
	
	/** Get the current scope nesting level. */
	int getLevel() {
		return currentLevel;
	}
	
	/** Return a Reference to a variable or parameter.  Return null if it doesn't exist. */
	Reference findReference(String name) {
		int level = currentLevel;
		while (level >= 0) {
			int offset = display[level].findSlotNumber(name);
			if (offset >= 0)
				return new Reference(level, offset);
			level--;
		}
		return null;		
	}
	
	/** Return a Reference to an array.  Return null if it doesn't exist. */
	ArrayReference findArray(String name) {
		int level = currentLevel;
		while (level >=0) {
			if (arrays[level] == null) {
				// Do nothing
			} else {
				String arrayName = arrays[level].getName();
				if (arrayName.equals(name)) {
					return new ArrayReference(level, 0);
				}
			}
			level--;
		}
		return null;
	}

	/** Create a variable in the current level and return its Reference. */
	Reference defineVariable(String name) {
		return new Reference(currentLevel, display[currentLevel].defineVariable(name));
	}
	
	/** Create a array in the current level and return its Reference. */
	ArrayReference defineArray(String name) {
		arrays[arrayCounter] = new ArrayInvocation(name);
		ArrayReference arrayRef = new ArrayReference(arrayCounter, 0);
		// Ready it for the next one
		arrayCounter++;
		return arrayRef;
	}

	/** Find a function.  Return null if it doesn't exist. */
	FunctionDefinition findFunction(String name) {
		int level = currentLevel;
		while (level >= 0) {
			FunctionDefinition definition = display[level].findFunction(name);
			if (definition != null)
				return definition;
			level--;
		}
		return null;
	}
	
	/** Find a function in the current level.  Return null if it doesn't exist. */
	FunctionDefinition findFunctionInCurrentLevel(String name) {
		return display[currentLevel].findFunction(name);
	}
	
	/** Add a function to the current level. */
	void addFunction(FunctionDefinition definition) {
		display[currentLevel].addFunction(definition);
	}
	
}
