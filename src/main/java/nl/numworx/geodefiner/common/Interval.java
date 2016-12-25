package nl.numworx.geodefiner.common;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.PuntOp;
import fi.euclides.model.Segment;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class Interval extends LabelValue {

	private static final Destroyer DESTROYER = new Destroyer();
	private static final StepValue NULL_STEP = new StepValue(Numbers.ZERO);

	private static final class Destroyer implements Observer {
		@Override
		public void update(Observable observable, Object arg) {
			Label l = (Label) observable;
			if(Label.DESTROY.equals(arg) && l.getP() instanceof PuntOp) {
				PuntOp<Segment> pl = (PuntOp<Segment>) l.getP();
				l.setP(new VrijPunt());
				//l.setP(null);
				Segment lijn = pl.getOp();
				lijn.getP1().destroy();
				lijn.getP2().destroy();
				return;
			}
		}
	}

	public Interval() {
		super("..");
	}

	@Override
	public String getSymbolicValue(Label l) {
		Destroyable[] labels = l.getDepend();
		String a = labels[0].toString();
		String b = labels[1].toString();
		return a + " .. " + b;
	}

	@Override
	public Destroyable[] createDepend() {
		return new Label[2];
	}

	@Override
	public Label define(Destroyable[] depend) {
		Label label = super.define(depend);
		label.addObserver(DESTROYER);
		return label;
	}

	@Override
	public void update(Observable observable, Object arg) {
		Label l = (Label) observable;
		StepValue sv = l.adapt(StepValue.class);
		if (sv == null) sv = NULL_STEP;
		
		Destroyable[] minmax = l.getDepend();
		Numbers min = ((Label)minmax[0]).value; min = sv.stepUp(min);
		Numbers max = ((Label)minmax[1]).value; max = sv.stepDown(max);
		if (l.getP() instanceof PuntOp) {
			PuntOp<Lijn> pl = (PuntOp<Lijn>) l.getP();
			Lijn lijn = pl.getOp();
			Numbers x1 = lijn.getX1n(); Numbers dx = lijn.getDXn();
			Numbers v = Numbers.sub( pl.getX(), x1 );
			v = Numbers.div(v, dx);
			v = Numbers.mul(v, Numbers.sub(max, min));
			v = Numbers.add(v, min);
			v = sv.step(v);
			setStringValue(l, v);
			
		} else if (l.value == null) {
			setStringValue(l, sv.step(Numbers.div(Numbers.add(max, min), Numbers.TWO)));
		} else if(min.doubleValue() > l.value.doubleValue()) {
			setStringValue(l, min);
		} else if(max.doubleValue() < l.value.doubleValue()) {
			setStringValue(l, max);
		}
	}

}
