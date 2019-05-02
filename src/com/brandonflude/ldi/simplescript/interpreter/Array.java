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
		// Sorted Integers
		ArrayList<Integer> sortedIntegers = new ArrayList<Integer>();	
		
		// Sorted Strings
		ArrayList<String> sortedStrings = new ArrayList<String>();
		
		for(int i = 0; i < values.size(); i++)
		{		
			
			if(values.get(i).getName() == "integer")
			{
				int temp = values.get(i).intValue();
				sortedIntegers.add(temp);
			}
			
			if(values.get(i).getName() == "string")
			{
				String temp = values.get(i).stringValue();
				sortedStrings.add(temp);
			}
		}

		// Sort the Java Array using Collections
		Collections.sort(sortedIntegers);
		Collections.sort(sortedStrings);
		
		int i = 0;
		
		// Overwrite the passed in Array (SS Array) with for loop
		if(!sortedIntegers.isEmpty())
		{
			for(; i < sortedIntegers.size(); i++)
			{	
				Value k = new ValueInteger(sortedIntegers.get(i));
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
