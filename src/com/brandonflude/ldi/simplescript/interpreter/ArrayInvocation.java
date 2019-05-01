package com.brandonflude.ldi.simplescript.interpreter;

import java.util.Vector;
import java.util.ArrayList;
import java.util.Collections;

import com.brandonflude.ldi.simplescript.values.Value;
import com.brandonflude.ldi.simplescript.values.ValueInteger;

/** Array invocation context. */
class ArrayInvocation {

	private String arrayName;
	private Vector<Value> values;
	
	private final void setSlot(int n, Value v) {
		if (n >= values.size())
			values.setSize(n + 1);
		values.set(n, v);
	}
	
	/** Get name. */
	String getName()
	{
		return arrayName;
	}
	
	/** Ctor for user-defined function. */
	ArrayInvocation(String name) {
		arrayName = name;
		values = new Vector<Value>();
	}
		
	/** Given a slot number, set its value. */
	void setValue(int slotNumber, Value value) {
		setSlot(slotNumber, value);
	}

	/** Get a variable or parameter value given a slot number. */
	Value getValue(int slotNumber) {
		return values.get(slotNumber);
	}	
	
	int getSize(String name) {
		return values.size();
	}
	
	void sort(String name) {
		// Sorted Integers
		ArrayList<Integer> sortedIntegers = new ArrayList<Integer>();	
		
		// Sorted Strings
		//ArrayList<String> sortedStrings = new ArrayList<String>();
		
		for(int i = 0; i < values.size(); i++)
		{		
			try
			{
				(values.get(i)).intValue();
			  
			  	int temp = values.get(i).intValue();
				
				// Add value to ArrayList
				sortedIntegers.add(temp);
		        
		    } catch (NumberFormatException ex) {
		       // String!
		    	//String temp = values.get(i).stringValue();
		    	// Add value to ArrayList
				//sortedStrings.add(temp);
		    }
		}

		// Sort the Java Array using Collections
		Collections.sort(sortedIntegers);
		//Collections.sort(sortedStrings);
		
		// Overwrite the passed in Array (SS Array) with for loop
		for(int i = 0; i < values.size(); i++)
		{
			Value j = new ValueInteger(sortedIntegers.get(i));
			setValue(i, j);
		}
	}
}
