package com.brandonflude.ldi.simplescript.interpreter;

import java.util.Vector;
import java.util.ArrayList;
import java.util.Collections;

import com.brandonflude.ldi.simplescript.values.*;

/** Array context. */
class Array {

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
	Array(String name) {
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
		// Sorted Numbers
		ArrayList<Integer> sortedNumbers = new ArrayList<Integer>();	
		
		// Sorted Strings
		ArrayList<String> sortedStrings = new ArrayList<String>();
		
		for(int i = 0; i < values.size(); i++)
		{		
			
			if(values.get(i).getName() == "integer")
			{
				int temp = values.get(i).intValue();
				sortedNumbers.add(temp);
			}

			if(values.get(i).getName() == "rational")
			{
				int temp = values.get(i).intValue();
				sortedNumbers.add(temp);
			}
			
			if(values.get(i).getName() == "string")
			{
				String temp = values.get(i).stringValue();
				sortedStrings.add(temp);
			}
		}

		// Sort the Java Array using Collections
		Collections.sort(sortedNumbers);
		Collections.sort(sortedStrings);
		
		int i = 0;
		
		// Overwrite the passed in Array (SS Array) with for loop
		if(!sortedNumbers.isEmpty())
		{
			for(; i < sortedNumbers.size(); i++)
			{	
				Value k = new ValueInteger(sortedNumbers.get(i));
				setValue(i, k);
			}
		}
		
		if(!sortedStrings.isEmpty())
		{	
			for(int a = 0; a < sortedStrings.size(); i++, a++)
			{
				// Strings after Integers
				Value j = new ValueString(sortedStrings.get(a));
				setValue(i, j);
			}
		}
	}
}
