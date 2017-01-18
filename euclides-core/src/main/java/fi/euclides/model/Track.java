package fi.euclides.model;

import fi.euclides.model.math.Numbers;


public class Track {

	Punt p;
		
	public Track(Numbers d, Numbers e) 
	{
		p = new VrijPunt(d, e);
	}
	
	public Track(Punt p)
	{
		this.p = p;
	}

	public void visit(Visitor v) {
		p.visit(v);
	}

	public void setXY(Numbers x, Numbers y) {
		p.moveTo(x, y);
	}

}
