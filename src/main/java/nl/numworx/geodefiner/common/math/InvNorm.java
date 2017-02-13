package nl.numworx.geodefiner.common.math;

import fi.euclides.expr.Som;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.wiskopdr.expressies.StatUtil;

class InvNorm extends Som {

	InvNorm() {
		super("invNorm");
	}

	static double phiInv(double p)
	{	return StatUtil.getInvCDF(p, true);
	}
	
	private double geefWaarde(double kans, double mu, double sigma)
	{	
		double waarde;
		if(Double.isNaN(kans) || Double.isNaN(mu) || Double.isNaN(sigma))waarde = Double.NaN;
		else waarde = mu + sigma * phiInv(kans);			
		return waarde;
	}

	@Override
	public String getSymbolicValue(Label l) {
		Destroyable d[] = l.getDepend();
		return "invNorm(" + s(d[0]) + "," + s(d[1]) + "," + s(d[2]) + ")";
	}

	@Override
	public Destroyable[] createDepend() {
		return new Label[3];
	}

	@Override
	protected void recalc(Label l, Label[] labels) {
		Numbers n = Numbers.createDouble(geefWaarde(d(labels[0]), d(labels[1]), d(labels[2])));
		setStringValue(l, n);
	}

	private double d(Label label) {
		return label.value.doubleValue();
	}

}
