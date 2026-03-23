package nl.numworx.fsm.editor;

import java.awt.Color;

import fi.euclides.event.AbstractDecorator;
import fi.euclides.model.Boog;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Visitor;
import fi.euclides.util.DefaultAdapter;

public class FSMDecorator extends AbstractDecorator implements Visitor {

	@Override
	public void visitPunt(Punt p) {
		DefaultAdapter adapter = DefaultAdapter.getDefault(p);
		adapter.put(Float.valueOf(75));
		adapter.put(Color.gray);
	}

	@Override
	public void visitSegment(Segment s) {
		DefaultAdapter adapter = DefaultAdapter.getDefault(s);
		super.visitSegment(s);
	}

	@Override
	public void visitBoog(Boog b) {
		DefaultAdapter adapter = DefaultAdapter.getDefault(b);
		super.visitBoog(b);
	}

	

}
