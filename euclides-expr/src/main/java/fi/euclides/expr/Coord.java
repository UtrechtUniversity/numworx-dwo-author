package fi.euclides.expr;

import java.io.IOException;

import fi.euclides.model.Codec;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelValue;
import fi.euclides.util.Observable;

public class Coord extends LabelValue {

	public static final String xKey = "x";
	public static final String yKey = "y";
	
	public Coord(String which) {
		super(which);
	}

	public Destroyable[] createDepend() {
		Destroyable[] punts = new Destroyable[3];
		punts[1] = getModel().getO();
		punts[2] = getModel().getU();
		return punts;
	}
	
	public Destroyable[] createDepend(Codec codec, Label label) throws IOException {
		return new Destroyable[3];
	}

	public String getSymbolicValue(Label l) {
		return string + "(" + s(l.getDepend()[0]) + ")";
	}
	
	public void update(Observable observable, Object arg) {
		if(arg == Destroyable.DESTROY 
				|| arg == Destroyable.VISIBLE)
					return;
				
		Label l = (Label)observable;
		Destroyable[] ll = l.getDepend();
		Destroyable d = ll[0];
// patch voor geodefiner, als een coordinaat free is, dan is die niet at (cx,cy)
		if(d instanceof Coordinaten && !((Coordinaten) d).isFree()) {
			Coordinaten punt = (Coordinaten) d;
			Numbers value = xKey == string ? 
				  Numbers.sub(Numbers.real(punt.getCx()), Numbers.imag(punt.getCy()))
				: Numbers.add(Numbers.real(punt.getCy()), Numbers.imag(punt.getCx()));
			setStringValue(l,value);
		} else
		if(d instanceof Punt)
		{ 
			Punt punt = (Punt) d;
			Numbers div = getValue(punt, (Punt)ll[1], (Punt)ll[2]);
			setStringValue(l,div);
		} 
//		else if (d instanceof Segment)
//		{   Segment s = (Segment)d;
//			Numbers value =  string == xKey ? s.getDXn() : s.getDYn();
//			setStringValue(l,value);
//		}
		else if (d instanceof Label) {
			Numbers v = ((Label)d).value;
			Numbers value = string == xKey ? Numbers.real(v) : Numbers.imag(v);
			setStringValue(l,value);
		}
	}

	public Numbers getValue(Punt punt) {
		return getValue(punt, getModel().getO(), getModel().getU());
	}
	
	public Numbers getValue(Punt punt, Punt o, Punt u) {
		return getValue(punt, o, u, string);
	}

	public static Numbers getValue(Punt punt, Punt o, Punt u, String key) {
		
		Numbers x = punt.getX();
		Numbers y = punt.getY();
//		if(o == null)
//			o = getModel().getO();
//		if (u == null)
//			u = getModel().getU();
		if(o==punt||o==null||u==null)
			return Numbers.ZERO;
		Numbers rdx = Numbers.sub(x, o.getX());
		Numbers rdy = Numbers.sub(y, o.getY());
		Numbers odx = Numbers.sub(u.getX(), o.getX());
		Numbers ody = Numbers.sub(u.getY(), o.getY());
		x = Numbers.add(Numbers.mul(rdx, odx), Numbers.mul(rdy,ody));
		y = Numbers.sub(Numbers.mul(rdx, ody), Numbers.mul(rdy,odx));
		Numbers scale = Numbers.add(Numbers.sqr(odx), Numbers.sqr(ody));
		Numbers value = key == xKey ? x : y;
		Numbers div = Numbers.div(value, scale);
		return div;
	}


}
