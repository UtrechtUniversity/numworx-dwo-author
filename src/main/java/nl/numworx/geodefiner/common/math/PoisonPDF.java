package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.math.Numbers;

public class PoisonPDF extends Som {

	public static double poisson(double lambda, int x)
	{
		return new fi.wiskopdr.expressies.PoissonPDF(null, null).poisson(lambda, x);
	}
	
	public double geefWaarde(Numbers kind1, Numbers kind2)
	{	
		double lambda = kind1.doubleValue();
		double x = kind2.doubleValue();
		double waarde = Double.NaN;
		if(Double.isNaN(lambda) || Double.isNaN(x)) return Double.NaN;
		waarde = poisson(lambda,(int)Math.rint(x));
		return waarde;
	}

}
