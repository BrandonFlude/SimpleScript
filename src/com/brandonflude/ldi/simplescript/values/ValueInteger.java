package com.brandonflude.ldi.simplescript.values;

public class ValueInteger extends ValueAbstract {

	private int internalValue;
	
	public ValueInteger(int l) {
		internalValue = l;
	}
	
	public String getName() {
		return "integer";
	}
	
	/** Convert this to a primitive long. */
	public long longValue() {
		return (long)internalValue;
	}
	
	/** Convert this to a primitive double. */
	public double doubleValue() {
		return (double)internalValue;
	}
	
	/** Convert this to a primitive String. */
	public String stringValue() {
		return "" + internalValue;
	}
	
	public int intValue() {
		return (int)internalValue;
	}

	public int compare(Value v) {
		if (internalValue == v.longValue())
			return 0;
		else if (internalValue > v.longValue())
			return 1;
		else
			return -1;
	}
	
	public Value add(Value v) {		
		// Check if number is a whole Number		
		if(v.doubleValue() == (int)v.doubleValue())
		{
			return new ValueInteger(internalValue + v.intValue());
		}
		else
		{
			return new ValueDouble(internalValue + v.doubleValue());
		}
	}

	public Value subtract(Value v) {
		return new ValueInteger(internalValue - v.intValue());
	}
	
	public Value add1() {
		return new ValueInteger(internalValue + 1);
	}

	public Value subtract1() {
		return new ValueInteger(internalValue - 1);
	}

	public Value mult(Value v) {
		return new ValueInteger(internalValue * v.intValue());
	}

	public Value div(Value v) {
		return new ValueInteger(internalValue / v.intValue());
	}

	public Value unary_plus() {
		return new ValueInteger(internalValue);
	}

	public Value unary_minus() {
		return new ValueInteger(-internalValue);
	}
	
	public String toString() {
		return "" + internalValue;
	}
}
