package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.wiskopdr.expressies.Bin;

public class BinomPDF extends Som {
	
	BinomPDF() {
		super("binomPDF");
	}

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

	@Override
	public Destroyable[] createDepend() {
		return new Label[3];
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		double waarde = geefWaarde(labels[0].value, labels[1].value, labels[1].value);
		setStringValue(l, Numbers.createDouble(waarde));
	}

}
