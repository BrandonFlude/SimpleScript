package com.brandonflude.ldi.simplescript.interpreter;

import java.util.HashMap;

import com.brandonflude.ldi.simplescript.values.*;

/** Dictionary context. */
class Dictionary {

	private String dictionaryName;
	private HashMap<String, Value> values = new HashMap<String, Value>(100);
		
	/** Get name. */
	String getName()
	{
		return dictionaryName;
	}
	
	/** Ctor for user-defined dictionary. */
	Dictionary(String name) {
		dictionaryName = name;
		values = new HashMap<String, Value>();
	}
		
	/** Given a slot number, set its value. */
	void setValue(String key, Value value) {
		values.put(key, value);
	}

	/** Get a variable or parameter value given a slot number. */
	Value getValue(String key) {
		return values.get(key);
	}	
	
	int getSize(String name) {
		return values.size();
	}
}
