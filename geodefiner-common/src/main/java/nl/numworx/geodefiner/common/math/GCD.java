package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.math.IntegerFactory;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;


class GCD extends Som {
	
	GCD() {
		super("gcd");
	}

	private Numbers geefWaarde(Numbers kind1, Numbers kind2)
	{	
		if( kind1.isNaN() || kind2.isNaN()) return Numbers.NaN;
		
		long m  = Numbers.round(kind1).longValue();
		long n  = Numbers.round(kind2).longValue();
		
		Numbers waarde = Numbers.createRational(getGCD(m,n),1);			
		return waarde;
	}

	private static long getGCD(long m, long n) {
		return IntegerFactory.getGCD(m, n);
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		Numbers waarde = geefWaarde(labels[0].value, labels[1].value);
		setStringValue(l, waarde);
	}

}
