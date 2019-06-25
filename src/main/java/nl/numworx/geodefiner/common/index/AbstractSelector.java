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
import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

abstract class AbstractSelector<T extends Destroyable> implements Visitor, Selector {
	
	Indexed indexed;
	Label index;
	T grp;
	
	public Destroyable get() {
		return indexed.asDestroyable();
	}

    /**
     * @param grp the grp to set
     */
    void setGrp(T grp) {
        this.grp = grp;
        if(grp != null) grp.addObserver(this);
    }

    public Destroyable[] getDepend() {
      return new Destroyable[] { grp, index };
    }
	@Override
	public void visitPunt(Punt p) {
	    if (p instanceof FreePoint || p.adapt(FreePoint.class) != null)
	      indexed  = new FreePointIndex(this);
	    else
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
        {
            if(observable == indexed.getDelegate()) {
                indexed.setDefined(false);
                indexed.changed();
                indexed.notifyObservers();
                return;
            }
            if(observable != grp && observable != index) return;
            if(grp != null) grp.deleteObserver(this);
            if(index != null) index.deleteObserver(this);
            if(indexed.getDelegate() != null) indexed.getDelegate().deleteObserver(this);
            indexed.destroy();
        }
        else if(arg == null || arg == Label.STATE) {
            if(observable == grp || observable == index)
                recalc();
            else if(observable == indexed.getDelegate()) {
                indexed.setDefined(indexed.getDelegate().isDefined());
            }
            indexed.changed();
            indexed.notifyObservers();
        } else if(arg == Destroyable.VISIBLE
        		&& indexed.getDelegate() != null
        		) {
            indexed.setVisible(indexed.getDelegate().isVisible());
        } 
//        else if (arg == Destroyable.VISIBLE) {
//        	System.out.println("NPE avoided");
//        }
    }

    abstract void recalc();

}
