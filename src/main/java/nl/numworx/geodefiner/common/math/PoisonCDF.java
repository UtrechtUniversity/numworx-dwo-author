package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.math.Numbers;

public class PoisonCDF extends Som {

	public static double poissonCum(double lambda, int x)
	{	double cum = 0;
		for(int i=1 ; i<x+1 ; i++)
		{	cum = cum + PoisonPDF.poisson(lambda, i);
		}
		return cum;
	}
	
	public double geefWaarde(Numbers kind1, Numbers kind2)
	{	double lambda = kind1.doubleValue();
		double x = kind2.doubleValue();
		double waarde = Double.NaN;
		if(Double.isNaN(lambda) || Double.isNaN(x)) return Double.NaN;
		waarde = poissonCum(lambda,(int)Math.rint(x));
		return waarde;
	}

}
