package fi.euclides.proof;

import java.util.Vector;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Complex;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class CrossRatio extends LabelValue {

	public CrossRatio(String string) {
		super(string);
	}

	public String getSymbolicValue(Label l) {
		Destroyable d[] = l.getDepend();
		return "(" + s(d[0]) + "," + s(d[1]) + "," + s(d[2]) + "," + s(d[3]) + ")";
	}

	public Destroyable[] createDepend() {
		return new Punt[4];
	}

	public void update(Observable observable, Object arg) {
		if(arg == null)
		{   Label l = (Label) observable;
			if(!l.isDefined())
				return;
			Punt[] d =  (Punt[]) l.getDepend();
			recalc(l, d);
		} else
			super.update(observable, arg);
	}

	private void recalc(Label l, Punt[] d) {
		Numbers cA = Numbers.createComplex(d[0].getX(), d[0].getY());
		Numbers cB = Numbers.createComplex(d[1].getX(), d[1].getY());
		Numbers cC = Numbers.createComplex(d[2].getX(), d[2].getY());
		Numbers cD = Numbers.createComplex(d[3].getX(), d[3].getY());
		Numbers tAC = Numbers.sub(cC, cA);
		Numbers tAD = Numbers.sub(cD, cA);
		Numbers tBC = Numbers.sub(cC, cB);
		Numbers tBD = Numbers.sub(cD, cB);
		
		Numbers value = Numbers.div (
				Numbers.div(tAC, tBC),
				Numbers.div(tAD, tBD));
		boolean test = Math.abs(Numbers.imag(value).doubleValue()) < 0.0000001;
		l.setDefined(test);		
		setStringValue(l, Numbers.real(value));
	}

	public String getSubKey() {
		return "CR";
	}

	public void command() {
		Vector select = getModel().getSelect();
		if(select.size() == 4)
		{
			Label l = new Label();
			Punt[] d = (Punt[]) createDepend();
			for (int i = 0; i < d.length; i++) {
				d[i] = (Punt) select.get(i);
			}
			l.setDepend(d);
			l.setState(Label.VALUE);
			l.register(this);
			l.setX((d[0].getXd()+d[1].getXd()+d[2].getXd()+d[3].getXd())/4.0);
			l.setY((d[0].getYd()+d[1].getYd()+d[2].getYd()+d[3].getYd())/4.0);
			recalc(l, d);
			getModel().add(l);
			return;
		}
		setStatus(string);
	}

	public boolean allowSelection(Vector selection) {
		// TODO Auto-generated method stub
		return super.allowSelection(selection);
	}

}
