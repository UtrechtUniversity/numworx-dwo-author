package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.math.Numbers;
import fi.wiskopdr.expressies.Bin;

public class BinomCDF extends Som {

	public double geefWaarde(Numbers kind1, Numbers kind2, Numbers kind3)
	{	
		double n = kind1.doubleValue();
		double p = kind2.doubleValue();
		double k = kind3.doubleValue();
		double waarde = Double.NaN;
		if(Double.isNaN(n) || Double.isNaN(p) || Double.isNaN(k)) return Double.NaN;
		waarde = 0;
		int round = (int) Math.round(n);
		for( int i=0 ; i<k+1 ; i++)
		{	
			double nBovenK = Bin.binom(round, i);
			double kans = Math.pow(p, i) * Math.pow(1-p, n-i);
			waarde = waarde + nBovenK*kans;
		}
		return waarde;
	}

}
