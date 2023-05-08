package fi.euclides.proof;

import java.util.Vector;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class RatioHandler extends LabelValue {

	public static final String TYPE = "R";
	public static final String SIGNED = "R2";
	private boolean signed;
	
	public RatioHandler(String string, boolean signed) {
		super(string);
		this.signed = signed;
	}

	public RatioHandler(String string) {
		this(string, false);
	}
	
	public String getSymbolicValue(Label l) {
		char sep;
		if(signed) sep = '/'; else sep = ':';
		Destroyable[] depend = l.getDepend();
		if(depend[2] == null)
			return s(depend[0]) + sep + s(depend[1]);
		else
			return s(depend[0]) + s(depend[1]) + sep + s(depend[2]) + s(depend[3]);
	}

	public Destroyable[] createDepend() {
		return new Destroyable[4];
	}

	public String getSubKey() {
		return signed?SIGNED:TYPE;
	}

	
	public void command() {
		Vector select = getModel().getSelect();
		if(select.size()==2)
		{
			Object first = select.firstElement();
			Object last = select.lastElement();
			if(first instanceof Segment && last instanceof Segment)
			{
				Segment s0 = (Segment) first;
				Segment s1 = (Segment) last;
				Label l = defineWith(s0,s1, null, null);
				l.setX( (s0.getX1() + s0.getX2() + s1.getX1() + s1.getX2())/4.0);
				l.setY( (s0.getY1() + s0.getY2() + s1.getY1() + s1.getY2())/4.0);			
				return;
			}
			if(first instanceof Label && last instanceof Label)
			{
				Label s0 = (Label) first;
				Label s1 = (Label) last;
				Label l = defineWith(s0, s1, null, null);
				l.setX( (s0.getXd() + s1.getXd() )/2.0);
				l.setY( (s0.getYd() + s1.getYd())/2.0);			
				return;
			}
		} else if(select.size()==4) {
			Object first = select.firstElement();
			Object last = select.lastElement();
			Punt p0 = (Punt) first; Punt p3 = (Punt) last;
			Punt p1 = (Punt) select.elementAt(1);
			Punt p2 = (Punt) select.elementAt(2);
			Label l = defineWith(p0,p1,p2,p3);
			l.setX((p0.getXd()+p1.getXd()+p2.getXd() + p3.getXd())/4.0);
			l.setY((p0.getYd()+p1.getYd()+p2.getYd() + p3.getYd())/4.0);
		} else if(select.size()==3) {
			Object first = select.firstElement();
			Object last = select.lastElement();
			Punt p0 = (Punt) first; Punt p3 = (Punt) last;
			Punt p1 = (Punt) select.elementAt(1);
			Punt p2 = p0;
			Label l = defineWith(p0,p1,p2,p3);
			l.setX((p0.getXd()+p1.getXd()+p3.getXd())/3.0);
			l.setY((p0.getYd()+p1.getYd()+p3.getYd())/3.0);
		}
		setStatus(string);
	}

	private Label defineWith(Destroyable s0, Destroyable s1, Punt p2, Punt p3) {
		Label l = new Label();
		l.register(this);
		Destroyable[] depend = createDepend();
		depend[0] = s0;
		depend[1] = s1;
		depend[2] = p2;
		depend[3] = p3;
		l.setDepend(depend);
		l.setState(Label.VALUE);
		setString(l);
		if(l.isDefined())
			getModel().add(l);
		else
			setStatus("Undefined");
		return l;
	}
				
	private void setString(Label l) {
		Destroyable[] s = l.getDepend();
		if(s[0] instanceof Segment)
		{
			setString(l, (Segment)s[0], (Segment)s[1]);
		} else if(s[0] instanceof Label)
		{
			setString(l, ((Label)s[0]).value, ((Label)s[1]).value);
		} else // Punt!
		{ 
			setString(l, (Punt) s[0], (Punt) s[1], (Punt) s[2], (Punt) s[3]);
		}
	}

	private void setString(Label l, Punt pA, Punt pB, Punt pC, Punt pD) {
		Numbers cA = Numbers.createComplex(pA.getX(), pA.getY());
		Numbers cB = Numbers.createComplex(pB.getX(), pB.getY());
		Numbers cC = Numbers.createComplex(pC.getX(), pC.getY());
		Numbers cD = Numbers.createComplex(pD.getX(), pD.getY());
		setString(l, Numbers.sub(cB, cA), Numbers.sub(cD, cC));
	}

	private void setString(Label l, Numbers t, Numbers n) {
		Numbers v = Numbers.div(t, n);
		if(!signed)
			v = Numbers.abs(v);
		else 
		{	l.setDefined( Math.abs(Numbers.imag(v).doubleValue()) < 0.000001);
			v = Numbers.real(v);
		}
		setStringValue(l,v);
	}

	private void setString(Label l, Segment t, Segment n) {
		Numbers tt = Numbers.createComplex(t.getDXn(), t.getDYn());
		Numbers nn = Numbers.createComplex(n.getDXn(), n.getDYn());
		setString(l, tt, nn);
	}

	public void update(Observable observable, Object arg) {
		Label l = (Label) observable;
		if(arg != Destroyable.DESTROY && arg != Destroyable.VISIBLE && arg != Destroyable.RENAME)
		{   
			setString(l);
		}
	}

}
