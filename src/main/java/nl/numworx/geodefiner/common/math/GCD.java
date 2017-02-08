package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;


class GCD extends Som {
	
	GCD() {
		super("gcd");
	}

	private double geefWaarde(Numbers kind1, Numbers kind2)
	{	
		long m  = Math.round(kind1.doubleValue());
		long n  = Math.round(kind2.doubleValue());
		double waarde;
		if(Double.isNaN(kind1.doubleValue()) || Double.isNaN(kind2.doubleValue())) waarde = Double.NaN;
		else waarde = getGCD(m,n);			
		return waarde;
	}

	private static double getGCD(long m, long n) {
		return new fi.wiskopdr.expressies.GCD(null, null).getGCD(m, n);
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		double waarde = geefWaarde(labels[0].value, labels[1].value);
		setStringValue(l, Numbers.createDouble(waarde));
	}

}
