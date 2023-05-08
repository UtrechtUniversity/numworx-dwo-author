package fi.euclides.expr;

import java.io.IOException;

import fi.euclides.model.MP;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.util.Observable;

public class List extends LabelDelegate implements Visitor {

	private StringBuilder sb;

	protected List() {
		super("[]");
	}

	protected List(String string) {
		super(string);
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
			sb = createBuffer();
			for (int i = 0; i < depend.length; i++) {
				depend[i].visit(this);
			}
			setString(l, sb);
		}
		
	}

	protected StringBuilder createBuffer() {
		return new StringBuilder();
	}

	protected void setString(Label l, StringBuilder sb) {
		l.setString(sb.toString());
		l.notifyObservers();
	}

	public void visitLabel(Label label) {
		sb.append(label.getString());
	}

	protected void appendName(Destroyable c) {
		sb.append(getTracker().getMapper().toString(c));
	}
	
	public void visitCirkel(Cirkel c) {
		appendName(c);
	}

	public void visitLijn(Lijn l) {
		appendName(l);
	}

	public void visitMP(MP l) {
		appendName(l);
	}

	public void visitPunt(Punt p) {
		appendName(p);
	}

	public void visitSegment(Segment s) {
		appendName(s);
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

  @Override
  public void visitTriangle(Triangle t) {
    visitMP(t);
  }

  @Override
  public void visitKegelsnede(Kegelsnede2 k) {
    visitMP(k);
  }

  @Override
  public void visitLocus(Locus l) {
    visitMP(l);
  }

  @Override
  public void visitBoog(Boog b) {
    appendName(b);
  }	
}
