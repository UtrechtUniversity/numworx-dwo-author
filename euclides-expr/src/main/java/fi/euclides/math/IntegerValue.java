package fi.euclides.math;

import java.io.IOException;

import fi.euclides.model.math.Complex;
import fi.euclides.model.math.Exact;
import fi.euclides.model.math.NumberCodec;
import fi.euclides.model.math.Numbers;

public class IntegerValue extends Exact {
	final int value;

	@Override
	public long longValue() { return value; }
	@Override
	public boolean isNaN() { return false; }
	
	@Override
	protected Numbers sqrt() {
		if(value == 1 || value == 0) return this;
		if(value < 0) return createComplex(ZERO, sqrt(neg()));
		double sqrt = Math.sqrt(value);
		if (sqrt == Math.ceil(sqrt))
			return new IntegerValue( (int) sqrt);
		return Numbers.createDouble(sqrt);
	}

	@Override
	public double doubleValue() {
		return value;
	}


	@Override
	public int intValue() {
		return value;
	}

	IntegerValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	@Override
	protected int signum() {
		return value<0 ? -1 : value>0 ? +1 : 0;
	}

	@Override
	public void writeNumber(NumberCodec memento) throws IOException {
		memento.writeInteger(value);
	}

	@Override
	protected Numbers round() {
		return this;
	}

	@Override
	protected Numbers floor() {
		return this;
	}

	@Override
	protected Numbers ceiling() {
		return this;
	}

	@Override
	protected Numbers neg() {
		if(value == Integer.MIN_VALUE)
			return Numbers.createRational( - (long) Integer.MIN_VALUE, 1L);
		return Numbers.createInteger(-value);
	}
	
	public boolean equals (Object other) {
		if (other == this) return true;
		if (other instanceof IntegerValue) {
			return value == ((IntegerValue) other).value;
		}
		return false;
	}
	
	public int hashCode() {
		return value;
	}
}
