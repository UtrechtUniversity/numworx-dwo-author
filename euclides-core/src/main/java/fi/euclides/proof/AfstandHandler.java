package fi.euclides.proof;

import java.io.IOException;
import java.util.Vector;

import fi.euclides.model.Cirkel;
import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;

public class AfstandHandler extends LabelValue {

	public static final String TYPE ="A";

	public String getSubKey() { return TYPE; }
	
	public AfstandHandler(String string) {
		super(string);
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#command()
	 */
	public void command() {
		Vector<?> select = getModel().getSelect();
		Punt o = getModel().getO();
		Punt u = getModel().getU();
		if(select.size()==2 && select.firstElement() instanceof Punt && select.lastElement() instanceof Punt)
		{
			Label l = new Label();
			l.register(this);
			Punt p1 = (Punt)select.firstElement();
			Punt p2 = (Punt)select.lastElement();
			if(p1.getIndex()>p2.getIndex())
			{
				Punt tmp = p1; p1=p2;p2=tmp; // swap 
			}
			l.setDepend( new Punt[] { p1, p2, o, u });
			l.setX(p1.getXd()/2+p2.getXd()/2);
			l.setY(p1.getYd()/2+p2.getYd()/2);
			setString(l,getString(p1, p2));
			getModel().add(l);
		} else if(select.size()==1 && select.firstElement() instanceof Segment)
		{
			Label l = new Label();
			Segment s = (Segment) select.firstElement();
			l.register(this);
			Punt p1 = s.getP1();
			Punt p2 = s.getP2();
			l.setDepend( new Destroyable[] { s, s, o, u });
			l.setX(p1.getXd()/2+p2.getXd()/2);
			l.setY(p1.getYd()/2+p2.getYd()/2);
			setString(l,getString(p1, p2));
			getModel().add(l);
			
		} else if(select.size()==1 && select.firstElement() instanceof Cirkel)
		{
			Label l = new Label();
			Cirkel c = (Cirkel) select.firstElement();
			l.register(this);
			Punt p1 = c.getRadius();
			Punt p2 = c.getRadius2();
			l.setDepend( new Destroyable[] { c, c, o, u });
			l.setX(p1.getXd()/2+p2.getXd()/2);
			l.setY(p1.getYd()/2+p2.getYd()/2);
			setString(l,getString(c, c));
			getModel().add(l);	
		}
	}

	private void setString(Label l, String string) {
		l.value = value;
		DefaultAdapter.getDefault(l).put(Numbers.class, org);
		if(l.isDefined()) 
			l.setString(string);
		else
			l.setString("");
	}

	public void update(Observable observable, Object arg) {
		Label l = (Label) observable;
		if(arg != Destroyable.DESTROY && arg != Destroyable.VISIBLE)
		{   Destroyable[] punten = (Destroyable[]) l.getDepend();
			setString(l,getString(punten[0], punten[1]));
			l.notifyObservers();
		}
	}

	/**
	 * @return
	 */
	public Destroyable[] createDepend() {
		Destroyable[] depend = new Destroyable[4];
		depend[2]=getModel().getO();
		depend[3]=getModel().getU();
		return depend;
	}
	
	public Destroyable[] createDepend(Codec codec, Label label) throws IOException {
		return new Destroyable[4];
	}

	Numbers value, org;	
	
	public String getSymbolicValue(Label l) {
		Destroyable[] p = l.getDepend();
		String d = "d(" + s(p[0]);
		if(p[0]!=p[1])
			d += "," + s(p[1]);
		return d + ")";
	}

	private String getString(Destroyable d1, Destroyable d2) {
		Punt p1=getModel().getO(), p2=getModel().getU();
		if(d1 instanceof Punt)
		{
			p1 = (Punt) d1; p2 = (Punt) d2;
		} else if(d1 instanceof Segment)
		{
			p1 = ((Segment)d1).getP1();
			p2 = ((Segment)d1).getP2();
		} else if(d1 instanceof Cirkel)
		{
			p1 = ((Cirkel)d1).getRadius();
			p2 = ((Cirkel)d1).getRadius2();
		}
		Numbers x = Numbers.sub(p1.getX(),p2.getX());
		Numbers y = Numbers.sub(p1.getY(),p2.getY());
		value = Numbers.add(Numbers.sqr(x), Numbers.sqr(y));
		org = value;
		value = Numbers.div(value, getScale());
		value = Numbers.sqrt(value);
		if(d1 instanceof Cirkel)
		{
			value = Numbers.mul(Numbers.TWO, value);
			//org = value; // geen goed idee
			String s = Numbers.toString(value) + " π";
			value = Numbers.mul(Numbers.PI, value);
			return s;
		}
		return Numbers.toString(value);
	}

	private Numbers getScale() {
		Vector punten = getModel().getPunten();
		if(punten.size()>=2)
		{
			Punt o = getModel().getO(); // dit is O
			Punt u = getModel().getU();	// dit is U
			Numbers x = Numbers.sub(u.getX(),o.getX());
			Numbers  y = Numbers.sub(u.getY(),o.getY());
			Numbers result = Numbers.add(Numbers.sqr(x), Numbers.sqr(y));
			return result;
		}
		return Numbers.ONE;
	}

	public boolean define(Label l) {
		Destroyable[] depend = l.getDepend();
		if(depend[1] == null)
			depend[1] = depend[0];
		if(depend[1].getIndex() < depend[0].getIndex())
		{
			Destroyable tmp = depend[0];
			depend[0] = depend[1];
			depend[1] = tmp;
		}
		return super.define(l);
	}

	
}
