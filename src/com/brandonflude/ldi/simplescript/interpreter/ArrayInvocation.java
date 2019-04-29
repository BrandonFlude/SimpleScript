package com.brandonflude.ldi.simplescript.interpreter;

import java.util.Vector;

import com.brandonflude.ldi.simplescript.values.Value;

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
}
