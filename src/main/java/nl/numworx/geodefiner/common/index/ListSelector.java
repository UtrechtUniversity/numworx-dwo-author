package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Punt;
import fi.euclides.model.Ray;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class ListSelector implements Visitor, Observer {
	
	private Indexed indexed;
	private Groep grp;
	private Label index;

	public ListSelector(Groep grp, Label index) {
		this.grp = grp;
		this.index = index;
		Destroyable element = grp.elementAt(0);
		if(element instanceof Groep) 
			indexed = new GroepIndex(this);
		else
			element.visit(this);
		recalc();
	}
	
	public Destroyable get() {
		return indexed.asDestroyable();
	}

	@Override
	public void visitPunt(Punt p) {
		indexed =  new PuntIndex(this);
	}

	@Override
	public void visitLijn(Lijn l) {
		if(l instanceof Ray) {
			indexed = new RayIndex(this);
		} else
			indexed = new LijnIndex(this);
			
	}

	@Override
	public void visitCirkel(Cirkel c) {
		indexed = new CirkelIndex(this);
	}

	@Override
	public void visitSegment(Segment s) {
		indexed = new SegmentIndex(this);		
	}

	@Override
	public void visitLabel(Label label) {
		indexed = new LabelIndex(this);
	}

	@Override
	public void visitTriangle(Triangle t) {
		indexed = new TriangleIndex(this);
	}

	@Override
	public void visitKegelsnede(Kegelsnede2 k) {
		indexed = new KegelsnedeIndex(this);
	}

	@Override
	public void visitLocus(Locus l) {
		indexed = new LocusIndex(this);
	}

	@Override
	public void visitBoog(Boog b) {
		indexed = new BoogIndex(this);
	}

	
	public Destroyable[] getDepend() {
		return new Destroyable[] { grp, index };
	}

	/**
	 * @param grp the grp to set
	 */
	void setGrp(Groep grp) {
		this.grp = grp;
		if(grp != null) grp.addObserver(this);
	}

	/**
	 * @param index the index to set
	 */
	void setIdx(Label index) {
		this.index = index;
		if(index != null) index.addObserver(this);
	}
	/* (non-Javadoc)
	 * @see fi.euclides.model.Punt#update(fi.euclides.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object arg) {
		if(arg == Destroyable.DESTROY)
			indexed.destroy();
		else if(arg == null || arg == Label.STATE) {
			if(observable == grp || observable == index)
				recalc();
			else if(observable == indexed.getDelegate()) {
				indexed.setDefined(indexed.getDelegate().isDefined());
			}
			indexed.changed();
			indexed.notifyObservers();
		} else if(arg == Destroyable.VISIBLE) {
			indexed.setVisible(indexed.getDelegate().isVisible());
		}
	}

	@SuppressWarnings("unchecked")
	void recalc() {
		if( grp != null && index != null) {
			int i = (int) Numbers.round(index.value).longValue();
			if(i >= 1 && i <= grp.size()) {
				if(indexed.getDelegate() != null) indexed.getDelegate().deleteObserver(this);
				Destroyable delegate = grp.elementAt(i-1);
				delegate.addObserver(this);
				indexed.setDefined(delegate.isDefined());
				indexed.setDelegate(delegate);
				return;
			}
		} 
		indexed.setDefined(false);		
	}

}
