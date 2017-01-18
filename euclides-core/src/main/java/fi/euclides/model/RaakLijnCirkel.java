package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;

public class RaakLijnCirkel extends LijnPuntCombi<Cirkel> {

	public static final String TYPE = "lC";
	
	private CirkelSnijpunt sp;
	private Cirkel c;
	
	public RaakLijnCirkel(Cirkel cirkel, Punt punt, Numbers x, Numbers y) {
		super(cirkel, punt);
		recalc(x,y);
	}

	private void recalc(Numbers x, Numbers y) {
		MiddelPunt mp = new MiddelPunt(getPunt(), getDestroyable().getCenter());
		c = new Cirkel(mp, getPunt());
		sp = new CirkelSnijpunt(getDestroyable(), c, x,y);
		c.addObserver(this); // AFTER sp
		lijn.deleteObserver(this);
		lijn.addObserver(this); // after sp
		update(c, null);
	}
	
	public RaakLijnCirkel() {
	}

	public Numbers getX2n() {
		
		return sp.getX();
	}

	public Numbers getY2n() {
		return sp.getY();
	}

	public String key() {
		return TYPE;
	}

	public boolean isDefined() {
		return super.isDefined()&& sp.isDefined();
	}

	public boolean equals(Object obj) {
		return super.equals(obj) && sp.getFuse() == ((RaakLijnCirkel)obj).sp.getFuse();
	}

	public void update(Observable o, Object arg) {
		super.update(o, arg);
		if(o == c || o == lijn)
		{
			if(Math.abs(c.getD()-getDestroyable().getR()) < 1)
			{
				sp.setDefined(getDestroyable().getD() > 1);
				Punt pc = getDestroyable().getCenter();
				sp.setXY(punt.getXd()+ (punt.getYd()-pc.getYd()), punt.getYd()- (punt.getXd()-pc.getXd()));
			}
		}
	}

	public void read(Codec codec) throws IOException {
		super.read(codec);
		sp = (CirkelSnijpunt) codec.readPunt();
		c = (Cirkel) sp.getOp2();
	}

	public void write(Codec codec) throws IOException {
		super.write(codec);
		codec.writePunt(sp);
	}



}
