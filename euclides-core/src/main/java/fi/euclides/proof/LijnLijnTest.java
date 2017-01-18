package fi.euclides.proof;

import java.util.Vector;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.LoodLijn;
import fi.euclides.model.ParallelLijn;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DComparator;

public class LijnLijnTest extends LabelTester {
	private static final double EPS = 0.000001;
	public static final String PARALLEL = "P";
	public static final String PERPENDICULAR = "L";
	boolean pl;
	
	public LijnLijnTest(String string, boolean pl) {
		super(string);
		this.pl = pl;
	}

	/**
	 * @return
	 */
	public String getSubKey() {
		return this.pl?PARALLEL:PERPENDICULAR;
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#command()
	 */
	public void command() {
		Vector select = getModel().getSelect();
		if(select.size() == 2 &&
				select.firstElement() instanceof Lijn &&
				select.lastElement() instanceof Lijn)
		{
			Lijn l1 = (Lijn) select.firstElement();
			Lijn l2 = (Lijn) select.lastElement();
			Lijn[] ll = new Lijn[] { l1, l2 };
			addLabel(ll);
		} else {
			setStatus("selecteer twee lijnen");
		}	
	}

	/**
	 * @param l1
	 * @param l2
	 * @return
	 */
	private Numbers bracket(Lijn l1, Lijn l2) {
		Numbers x1 = l1.getDXn();
		Numbers x2 = l2.getDXn();
		Numbers y1 = l1.getDYn();
		Numbers y2 = l2.getDYn();
		
		Numbers r;
		if(!pl)
			r = Numbers.add(Numbers.mul(x1,x2),Numbers.mul(y1,y2));
		else 
			r = Numbers.sub(Numbers.mul(x1,y2),Numbers.mul(y1,x2));
		return r;
	}

	/**
	 * @param l
	 * @return
	 */
	protected boolean test(Label l) {
		Numbers d;
		Lijn[] lijnen = (Lijn[]) l.getDepend();
		d = bracket(lijnen[0], lijnen[1]);
		return setState(l, d,EPS);
	}

	/**
	 * @return
	 */
	public Destroyable[] createDepend() {
		return new Lijn[2];
	}

	public boolean define(Label l) {
		boolean result = test(l);
		if(result)
		{
			Lijn[] ll = (Lijn[]) l.getDepend();
			final Lijn l1 = ll[0];
			final Lijn l2 = ll[1];
			DComparator.sort(ll);
			if(pl)
			{
				if(ParallelLijn.isParallel(l1, l2))
					l.setState(Label.CONFIGURATION);
			} else if (LoodLijn.isLoodRecht(l1,l2))
				l.setState(Label.CONFIGURATION);
			l.register(this);
			l.setString(s(ll[0]) + string + s(ll[1]));
			l.setX((l1.getX1()+l2.getX2())/2);
			l.setY((l1.getY1()+l2.getY2())/2);
		}
		return result;
	}

}

