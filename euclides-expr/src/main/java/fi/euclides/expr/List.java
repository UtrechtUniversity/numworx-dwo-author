package fi.euclides.expr;

import java.io.IOException;

import fi.euclides.model.MP;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.util.Observable;

public class List extends LabelDelegate implements Visitor {

	private StringBuffer sb;

	private List() {
		super("[]");
	}

	public static final List INSTANCE = new List();

	public Destroyable[] createDepend() {
		return Label.EMPTY;
	}


	public void update(Observable observable, Object arg) {
		Label l = (Label) observable;
		if(arg == null)
		{
			Destroyable depend[] = l.getDepend();
			sb = new StringBuffer();
			for (int i = 0; i < depend.length; i++) {
				depend[i].visit(this);
			}
			l.setString(sb.toString());
		}
		
	}

	public void visitLabel(Label label) {
		sb.append(label.getString());
	}

	public void visitCirkel(Cirkel c) {
		sb.append(getTracker().getMapper().toString(c));
	}

	public void visitLijn(Lijn l) {
		sb.append(getTracker().getMapper().toString(l));
	}

	public void visitMP(MP l) {
		sb.append(getTracker().getMapper().toString(l));
	}

	public void visitPunt(Punt p) {
		sb.append(getTracker().getMapper().toString(p));
	}

	public void visitSegment(Segment s) {
		sb.append(getTracker().getMapper().toString(s));
	}

	public void prepareDepend(Codec codec, Label label) throws IOException {
		codec.writeNumber(Numbers.createInteger(label.getDepend().length));
	}

	public Destroyable[] createDepend(Codec codec, Label label)
			throws IOException {
		int len = (int)codec.readNumber().doubleValue();
		return createDepend(len);
	}

	public Destroyable[] createDepend(int args) {
		return new Destroyable[args];
	}	
}
