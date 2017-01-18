package fi.euclides.proof;

import java.io.IOException;
import java.util.Vector;

import fi.euclides.model.Triangle;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DComparator;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class OppHandler extends LabelValue implements Observer {

	public static final String TYPE = "S";
	public static final String SIGNED = "SA";
	private final boolean signed;

	public String getSubKey() {
		return signed ? SIGNED : TYPE;
	}
	
	public OppHandler(String string, boolean signed) {
		super(string);
		this.signed = signed;
	}
	
	public OppHandler(String string)
	{
		this(string, false);
	}
	

	public String getSymbolicValue(Label l) {
		Destroyable depend[] = l.getDepend();
		char pfx = signed ? 'S' : 'O';
		if(depend[0] instanceof Punt)
			return pfx + "(" + s(depend[0]) +","+ s(depend[1]) + ","+s(depend[2]) +")";
		else
			return pfx + "(" + s(depend[0]) + ")";
	}

	public void update(Observable observable, Object arg) {
		Label l = (Label) observable;
		if(arg != Destroyable.DESTROY && arg != Destroyable.VISIBLE)
		{
			if(l.getDepend()[0] instanceof Punt) {
				Destroyable[] punten = l.getDepend();
				getString((Punt)punten[0], (Punt)punten[1], (Punt)punten[2], l);
			} else if (l.getDepend()[0] instanceof Triangle)
			{
				Punt[] punten = (Punt[]) l.getDepend()[0].getDepend();
				getString(punten[0], punten[1], punten[2], l);
			} else {
				Cirkel c  = (Cirkel) l.getDepend()[0];
				getString(c, l);
			}
			l.notifyObservers();
		}
	}

	/**
	 * @return
	 */
	public Destroyable[] createDepend() {
		Destroyable[] depend = new Destroyable[5];
		depend[3] = getModel().getO();
		depend[4] = getModel().getU();
		return depend;
	}
	
	

	public Destroyable[] createDepend(Codec codec, Label label)
			throws IOException {
		return new Destroyable[5];
	}

	private void getString(Cirkel c, Label l) {
		Numbers r = c.getR2n();
		DefaultAdapter.getDefault(l).put(Numbers.class, r);
		r  = Numbers.div(Numbers.mul(Numbers.TWO,r) ,getScale());
		l.value = Numbers.mul(r, Numbers.PI);
		if(l.isDefined())
			l.setString(Numbers.toString(r) + " π");
		else
			l.setString("");
	}

	public void command() {
		Vector select = getModel().getSelect();
		if(select.size() == 1 && select.firstElement() instanceof Cirkel)
		{
			Label l = new Label();
			Cirkel c = (Cirkel)select.firstElement();
			l.setDepend(new Destroyable[] { c, null, null, getModel().getO(), getModel().getU() });
			l.register(this);
			l.setX(c.getRadius().getX());
			l.setY(c.getCenter().getY());
			getString(c, l);
			getModel().add(l);
		} else
		if(select.size() == 1 && select.firstElement() instanceof Triangle)
		{
			Label l = new Label();
			Triangle t = (Triangle) select.firstElement();
			Destroyable[] depend = createDepend();
			depend[0] = t;
			depend[3] = getModel().getO();
			depend[4] = getModel().getU();
			l.setDepend(depend);
			l.register(this);
			l.setX((t.getA().getXd()+ t.getB().getXd() + t.getC().getXd())/3.0);
			l.setY((t.getA().getYd()+t.getB().getYd() + t.getC().getYd())/3.0);
			getString(t.getA(), t.getB(), t.getC(), l);
			getModel().add(l);
			
		} else
		if(select.size()==3 && select.firstElement() instanceof Punt && select.lastElement() instanceof Punt)
		{
			Label l = new Label();
			Punt pn[] = new Punt[3];
			pn[0] = (Punt)select.firstElement();
			pn[1] = (Punt)select.elementAt(1);
			pn[2] = (Punt)select.lastElement();
			DComparator.sort(pn);
			l.register(this);
			l.setDepend(new Punt[] { pn[0], pn[1], pn[2], getModel().getO(), getModel().getU() });
			l.setX((pn[0].getXd()+ pn[1].getXd() + pn[2].getXd())/3.0);
			l.setY((pn[0].getYd()+pn[1].getYd() + pn[2].getYd())/3.0);
			getString(pn[0], pn[1], pn[2], l);
			getModel().add(l);
		} else {
			setStatus(string);
		}
	}

	private Numbers getScale() {
		Vector punten = getModel().getPunten();
		if(punten.size()>=2)
		{
			Punt o = getModel().getO(); // dit is O
			Punt u = getModel().getU();	// dit is U
			Numbers x = Numbers.sub(u.getX(),o.getX());
			Numbers y = Numbers.sub(u.getY(),o.getY());
			Numbers result = Numbers.add(Numbers.sqr(x),Numbers.sqr(y));
			return Numbers.mul(result, Numbers.TWO);
		}
		return Numbers.ONE;
	}

	private void getString(Punt p1, Punt p2, Punt p3, Label l) {
		Numbers result = DrieOpEenRij.bracketn(p1, p3, p2);
		DefaultAdapter.getDefault(l).put(Numbers.class, result);
		if(!signed)
			result = Numbers.abs(result);
		result = Numbers.div(result , getScale());
		setStringValue(l, result);
	}

}
