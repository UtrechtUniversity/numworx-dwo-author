package fi.euclides.proof;

import java.util.Vector;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Messages;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class HoekHandler extends LabelValue implements Observer {

	public static final String TYPE = "H";
	
	public String getSubKey() { return TYPE; }

	public HoekHandler(String string) {
		super(string);
	}
	
	public HoekHandler() {
		this(Messages.getString("Euclides.85"));
	}

	public String getSymbolicValue(Label l) {
		Destroyable depend[] = l.getDepend();
		return "\u2220(" + s(depend[0]) +","+ s(depend[1]) + (depend[2]==null?"":","+s(depend[2])) +")";
	}

	public void command() {
		Vector select = getModel().getSelect();
		if(select.size()==2 && select.firstElement() instanceof Lijn && select.lastElement() instanceof Lijn)
		{
			Label l = new Label();
			l.setState(Label.HOEK);
			Lijn l1 = (Lijn) select.firstElement();
			Lijn l2 = (Lijn) select.lastElement();
			l.setDepend(new Lijn[] { l1, l2, null});
			l.register(this);
			l.setX((l1.getX1()+l2.getX1()+l1.getX2()+l2.getX2())/4);
			l.setY((l1.getY1()+l2.getY1()+l1.getY2()+l2.getY2())/4);
			l.setString(getString(l1, l2, l));
			getModel().add(l);
			
		
		} else
		if(select.size()==3 && select.firstElement() instanceof Punt && select.lastElement() instanceof Punt)
		{
			Label l = new Label();
			l.setState(Label.HOEK);
			Punt p1 = (Punt)select.firstElement();
			Punt p2 = (Punt)select.elementAt(1);
			Punt p3 = (Punt)select.lastElement();
			l.setDepend(new Punt[] { p1, p2, p3 });
			l.register(this);
			l.setX(p1.getXd()/4+p2.getXd()/2 + p3.getXd()/4);
			l.setY(p1.getYd()/4+p2.getYd()/2 + p3.getYd()/4);
			l.setString(getString(p1, p2, p3,l));
			getModel().add(l);
		} else {
			setStatus(string);
		}
	}

	public void update(Observable observable, Object arg) {
		Label l = (Label) observable;
		if(arg != Destroyable.DESTROY && arg != Destroyable.VISIBLE)
		{
			Destroyable[] punten = l.getDepend();
			if(punten[2] !=null)
				l.setString(getString((Punt)punten[0], (Punt)punten[1], (Punt)punten[2], l));
			else 
				l.setString(getString((Lijn)punten[0],(Lijn)punten[1],l));
			l.notifyObservers();
		}
	}

	/**
	 * @return
	 */
	public Destroyable[] createDepend() {
		return new Destroyable[3];
	}

	String getString(Punt p1, Punt p2, Punt p3, Label l) {
		Numbers x1 = Numbers.sub(p1.getX(),p2.getX());
		Numbers y1 = Numbers.sub(p1.getY(),p2.getY());
		Numbers x3 = Numbers.sub(p3.getX(),p2.getX());
		Numbers y3 = Numbers.sub(p3.getY(),p2.getY());
		return calcString(x1, y1, x3, y3, l, false);
	}
	/**
	 * @param x1
	 * @param y1
	 * @param x3
	 * @param y3
	 * @param l
	 * @param isLijn 
	 * @return
	 */
	private String calcString(Numbers x3, Numbers y3, Numbers x1, Numbers y1, Label l, boolean isLijn) {
		Numbers x = Numbers.add(Numbers.mul(x1,x3),Numbers.mul( y1,y3));
		Numbers y = Numbers.sub(Numbers.mul(x1,y3), Numbers.mul(y1,x3));
		Numbers[] extra = new Numbers[] { x, y };
		DefaultAdapter.getDefault(l).put(extra);
		
		l.value = Numbers.div(x,y);
		if(!l.isDefined())
			return "";
		double xd = x.doubleValue();
		double yd = y.doubleValue();
		if(yd < 0 && isLijn)
		{
			yd = -yd;
			xd = -xd;
			extra = new Numbers[] { Numbers.neg(x), Numbers.neg(y) };
			DefaultAdapter.getDefault(l).put(extra);			
		}
		int mod = isLijn?180:360;
		//System.out.println(l + ": "+ l.value);
		double result = (Math.atan2(yd, xd));
// experiment met hoek als value 0..2pi
		if(result < 0)
			result += Math.PI*2.0;
		l.value = Numbers.createDouble(result);
		
		return hoekAsString(l.value); //JMath.round(result * 180.0 / Math.PI)%mod + "°";
	}
	
	String getString(Lijn l1, Lijn l2, Label l)
	{
		Numbers x1 = l1.getDXn();
		Numbers y1 = l1.getDYn();
		Numbers x3 = l2.getDXn();
		Numbers y3 = l2.getDYn();
		return calcString(x1, y1, x3, y3, l, true);
	}

	public boolean define(Label l) {
		l.setState(Label.HOEK);
		return super.define(l);
	}

}
