package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.wiskopdr.expressies.Bin;

public class BinomCDF extends Som {

	BinomCDF() {
		super("binomcdf");
	}

	private double geefWaarde(Numbers kind1, Numbers kind2, Numbers kind3)
	{	
		if ( kind1.isNaN() || kind2.isNaN() || kind3.isNaN())
			return Double.NaN;
		
		
		long n = Numbers.round(kind1).longValue();
		double p = kind2.doubleValue();
		double k = kind3.doubleValue();
		double waarde;
		waarde = 0;
		int round = (int) (n);
		for( int i=0 ; i<k+1 ; i++)
		{	
			double nBovenK = Bin.binom(round, i);
			double kans = Math.pow(p, i) * Math.pow(1-p, n-i);
			waarde = waarde + nBovenK*kans;
		}
		return waarde;
	}

	@Override
	public Destroyable[] createDepend() {
		return new Label[3];
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		double waarde = geefWaarde(labels[0].value, labels[1].value, labels[2].value);
		setStringValue(l, Numbers.createDouble(waarde));
	}

}
