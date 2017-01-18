/**
 * 
 */
package fi.euclides.proof;


import fi.euclides.util.Messages;
import fi.euclides.event.DescriptionBuilder;
import fi.euclides.model.Triangle;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Model;
import fi.euclides.model.math.Exact;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

/**
 * @author Wim
 *
 */
public abstract class LabelTester extends LabelDelegate {

	/**
	 * @param string
	 */
	public LabelTester(String string) {
		super(string);
	}

	public abstract Destroyable[] createDepend();
	protected abstract boolean test(Label l);
	public abstract boolean define(Label l);

	public void update(Observable observable, Object arg) {
			Label l = (Label) observable;

			if(arg == Destroyable.RENAME)
			{
				define(l);	// setString als neveneffect.
				l.notifyObservers();
			} else
			if(arg == Model.DELAY)
			{	
				if(l.isDefined() && l.getState() <= Label.EXACT && !test(l)) ;//destroy(l); // FIXME GEODEFINER/EUCLIDES 
			} else if(arg != Destroyable.DESTROY && arg != Destroyable.VISIBLE && !"STATE".equals(arg))
			{
				if(l.isDefined())
				{	
					getModel().addDelay(observable, this);
				} 
			}
	}

	protected void destroy(Label l) {
// no destruction of internal labels in expressions.
// TODO convert external label to internal label here?
		if(l.getIndex() != 0)
			l.destroy();
	}

	private void setState(Label l, int state)
	{
		if(l != null)
			l.setState(state);
	}
	
	protected boolean setState(Label l, Numbers d, double eps) {
		if(l!=null)
			l.value=d;
		if(l!=null && l.getState() > Label.EXACT)  // keep state if: NAGEKEKEN, BEWEZEN, OPGAVE, VOLDOENDE
			return true;
		if(d.equals(Numbers.ZERO))
		{
			setState(l,Label.EXACT);
			return true;
		}
		if(d instanceof Exact)
		{
			setState(l,Label.FALSE);
			return false;
		}
		boolean test = ((Numbers.abs(d).doubleValue())<eps);
		if(!test)
			setState(l,Label.FALSE);
		else
			setState(l,Label.INEXACT);
		return test;
	}

	/**
	 * @param ll
	 */
	protected void addLabel(Destroyable[] ll) {
		Label l = new Label();
		l.register(this);
		l.setDepend(ll);
		if(define(l))
		{
			setStatus(Messages.getString("LabelTester.0") + l.getString()); //$NON-NLS-1$
			getModel().add(l);
		} else {
			setStatus(Messages.getString("LabelTester.1")); //$NON-NLS-1$
			destroy(l);
		}
	}

	protected String s(Destroyable d) {
		if(d.getIndex()==0 && d instanceof Label) {
			Label d2 = (Label) d;
			if(d2.getRegistered() instanceof LabelValue)
				return ((LabelValue)d2.getRegistered()).getSymbolicValue(d2);
			return d2.getString();
		}
		if(d.getIndex() == 0 && d instanceof Triangle) {
			DescriptionBuilder builder = new DescriptionBuilder(getTracker().getMapper());
			builder.visitTriangle((Triangle) d);
			return builder.toString().substring(4);  // skip " IS "
		}
		return super.s(d);
	}

	
}
