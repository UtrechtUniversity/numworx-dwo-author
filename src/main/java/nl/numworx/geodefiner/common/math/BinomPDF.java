package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.math.Numbers;
import fi.wiskopdr.expressies.Bin;

public class BinomPDF extends Som {

	public double geefWaarde(Numbers kind1, Numbers kind2, Numbers kind3)
	{	
		double n = kind1.doubleValue();
		double p = kind2.doubleValue();
		double k = kind3.doubleValue();
		double waarde = Double.NaN;
		if(Double.isNaN(n) || Double.isNaN(p) || Double.isNaN(k)) return Double.NaN;
		double nBovenK = Bin.binom((int)Math.round(n),(int)Math.round(k));
		double kans = Math.pow(p, k) * Math.pow(1-p, n-k);
		waarde = nBovenK*kans;
		return waarde;
	}

}
