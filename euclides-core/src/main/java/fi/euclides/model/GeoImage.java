package fi.euclides.model;

import fi.euclides.model.math.Numbers;

public abstract class GeoImage extends Destroyable {

	public static final String TYPE = "i";
	
	public GeoImage() {
	}

	@Override
	public void visit(Visitor v) {
		v.visitImage(this);		
	}

	@Override
	public String key() {
		return TYPE;
	}

	public abstract Punt imageCenter();
	public abstract Punt center();
	public abstract Numbers rotation(); // a complex rotation
	public abstract Destroyable shape(); // the outline
	public Destroyable[] getDepend() {
		return shape().getDepend();
	}

	public abstract boolean isMove(Numbers x, Numbers y);
}
