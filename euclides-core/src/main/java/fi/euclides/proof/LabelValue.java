package fi.euclides.proof;



import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public abstract class LabelValue extends  LabelDelegate  {

	public LabelValue(String string) {
		super(string);
	}
	
	public abstract String getSymbolicValue(Label l);

	/* (non-Javadoc)
	 * @see fi.euclides.util.Observer#update(fi.euclides.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object arg) {
	}

	protected void setStringValue(Label l, Numbers n) {
		if(l.isDefined())
			l.setString(Numbers.toString(n));
		else
			l.setString("");
		l.setValue(n);
	}

}
