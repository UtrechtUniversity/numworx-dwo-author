package nl.numworx.geodefiner.ui;

import java.awt.Color;
import java.awt.Paint;

import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public enum UserConfig implements Observer, Visitor {

	INSTANCE; // singleton.

	static final Float USER_POINT_SIZE = Float.valueOf(7f);
	static final Paint USER_TRIANGLE_PAINT = new Color(0x80dca000, true); //vulling: #dca000 + half transparant
	static final Color USER_TRIANGLE_COLOR = new Color(0xFF6e5000, false);
	
	@Override
	public void visitPunt(Punt p) {
		if ( p.adapt(Float.class) == null) 
		{
			DefaultAdapter.getDefault(p).put(USER_POINT_SIZE);
		}
	}

	@Override
	public void visitLijn(Lijn l) {
	}

	@Override
	public void visitCirkel(Cirkel c) {
	}

	@Override
	public void visitSegment(Segment s) {
	}

	@Override
	public void visitLabel(Label label) {
		DefaultAdapter.getDefault(label).put(Boolean.TRUE); 
	}

	@Override
	public void visitTriangle(Triangle t) {
		DefaultAdapter adapter = DefaultAdapter.getDefault(t);
		adapter.put(Paint.class, USER_TRIANGLE_PAINT);
		adapter.put(USER_TRIANGLE_COLOR);
	}

	@Override
	public void visitKegelsnede(Kegelsnede2 k) {
	}

	@Override
	public void visitLocus(Locus l) {
	}

	@Override
	public void visitBoog(Boog b) {
	}

	@Override
	public void update(Observable observable, Object arg) {
		if(arg instanceof Destroyable) {
			((Destroyable) arg).visit(this);
		} else if (arg == Destroyable.DESTROY) {
			// model destroys something. not itself!
		}
	}

}
