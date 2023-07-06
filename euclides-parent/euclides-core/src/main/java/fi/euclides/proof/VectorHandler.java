package fi.euclides.proof;

import java.util.Vector;

import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.Ray;
import fi.euclides.model.Segment;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;

public class VectorHandler extends LabelValue {

	public static final String TYPE = "V";

	public VectorHandler(String string) {
		super(string);
	}

	public String getSymbolicValue(Label l) {
		String xtra = "";
		Destroyable[] depend = l.getDepend();
		if(depend[1]!= null) xtra = s(depend[1]);
		return "-" + s(depend[0]) +xtra + "->" ;
	}

	public Destroyable[] createDepend() {
		return new Destroyable[4]; // Ray, null, point, point
	}

	public String getSubKey() {
		return TYPE;
	}

	public void update(Observable observable, Object arg) {
		Label l = (Label) observable;
		if(arg != Destroyable.DESTROY && arg != Destroyable.VISIBLE && arg != Destroyable.RENAME)
		{   
			setString(l);
			l.notifyObservers();
		}
	}

	private void setString(Label l) {
		Destroyable[] depend = l.getDepend();
		Numbers rdx;
		Numbers rdy;
		if(depend[0] instanceof Ray) 
		{	Ray r = (Ray) depend[0];
			rdx = r.getDXn();
			rdy = r.getDYn();
		} else {
			Punt p1 = (Punt) depend[0];
			Punt p2 = (Punt) depend[1];
			rdx = Numbers.sub(p2.getX(), p1.getX());
			rdy = Numbers.sub(p2.getY(), p1.getY());
		}
		Punt o = getModel().getO();
		Punt u = getModel().getU();
		l.setX(Numbers.add(o.getX(), rdx));
		l.setY(Numbers.add(o.getY(), rdy));
		Numbers odx = Numbers.sub(u.getX(), o.getX());
		Numbers ody = Numbers.sub(u.getY(), o.getY());
		Numbers x = Numbers.add(Numbers.mul(rdx, odx), Numbers.mul(rdy,ody));
		Numbers y = Numbers.sub(Numbers.mul(rdx, ody), Numbers.mul(rdy,odx));
		Numbers scale = Numbers.add(Numbers.sqr(odx), Numbers.sqr(ody));
		x = Numbers.div(x, scale);
		y = Numbers.div(y, scale);
		DefaultAdapter.getDefault(l).put(new Numbers[] { rdx, rdy });
		setStringValue(l,Numbers.createComplex(x, y));
		l.setState(Label.VECTOR);
	}

	public void command() {
		Vector select = getModel().getSelect();
		Punt o = getModel().getO();
		Punt u = getModel().getU();
		if(select.size()==1 && select.firstElement() instanceof Ray)
		{
			Label l = new Label();
			Ray s = (Ray) select.firstElement();
			l.register(this);
			l.setDepend( new Destroyable[] { s, null, o, u });
			setString(l);
			getModel().add(l);
		} else if(select.size()==2 && select.firstElement() instanceof Punt && select.lastElement() instanceof Punt)
		{
			Label l = new Label();
			l.register(this);
			Punt p1 = (Punt) select.firstElement();
			Punt p2 = (Punt) select.lastElement();
			l.setDepend( new Destroyable[] { p1, p2, o, u });
			setString(l);
			getModel().add(l);
			
		}
	}

}
