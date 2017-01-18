package fi.euclides.model;

import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Triangle;
import fi.euclides.model.math.Numbers;


public class LijnTrack extends Track {

	Punt p2;
	Destroyable l;
	
	public void destroy()
	{
		p2.destroy();
		l.destroy();
	}
	
	
	public LijnTrack(Numbers d, Numbers e) {
		this(d,e, new PuntenLijn());
	}
	
	public LijnTrack(Numbers d, Numbers e, PuntenLijn l) {
		super(d, e);
		p2 = new VrijPunt(d, e);
		this.l = l;
		l.setP2(p);
		l.setP1(p2);
	}
	
	public LijnTrack(Numbers d, Numbers e, LijnPuntCombi l)
	{
		super(d, e);
		this.l = l;
		l.setPunt(p);
		p2 = p;
	}

	public LijnTrack(Numbers d, Numbers e, Cirkel l)
	{
		super(d, e);
		this.l = l;
		l.setCenter(p);
		p2 = p;
	}
	
	public LijnTrack(Numbers d, Numbers e, MiddelPunt l)
	{
		super(d, e);
		this.l = l;
		l.setP1(p);
		p2 = new VrijPunt(d,e);
		l.setP2(p2);		
	}
	
	
	public LijnTrack(Numbers x, Numbers y, Bissectrice bs) {
		super(x,y);
		this.l = bs;
		bs.setP3(p);
		p2 = bs.getP2();
	}

	public LijnTrack(Numbers x, Numbers y, SpiegelPunt sp)
	{
		super(x,y);
		this.l = sp;
		sp.setPunt(p);
		p2 = p;
	}

	public LijnTrack(Numbers x, Numbers y, Triangle t) {
		super(x,y);
		this.l = t;
		t.setC(p);
		p2 = p;
	}

	public LijnTrack(Punt p, Kegelsnede2 k)
	{
		super(p);
		p2 = p;
	}

	/* (non-Javadoc)
	 * @see Track#visit(euclides.Visitor)
	 */
	public void visit(Visitor v) {
//		if(true || p.isDefined())
			l.visit(v);
//		else
//			p2.visit(v);
	}

}
