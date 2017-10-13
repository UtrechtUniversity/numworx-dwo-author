package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.Codec;
import fi.euclides.model.LijnPuntCombi;
import fi.euclides.model.Poollijn;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp2;
import fi.euclides.model.math.Numbers;

public class RaakLijnConic extends LijnPuntCombi<Kegelsnede2> {

	public static final String TYPE = "lK";
	private PuntOp2 sp;
	
	public RaakLijnConic() {
		super();
	}
	public RaakLijnConic(Kegelsnede2 k, Punt p, Numbers x, Numbers y) {
		super(k,p);
		Poollijn pl = new Poollijn(p,k);
		sp = new SnijpuntLijn(k,pl,x,y);
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
		return super.isDefined() && sp.isDefined();
	}

	public boolean same(Object obj) {
		return super.equals(obj) &&  sp.getFuse() ==  ((RaakLijnConic)obj).sp.getFuse();
	}

	public void read(Codec codec) throws IOException {
		super.read(codec);
		sp = (PuntOp2) codec.readPunt();
	}

	public void write(Codec codec) throws IOException {
		super.write(codec);
		codec.writePunt(sp);
	}

}
