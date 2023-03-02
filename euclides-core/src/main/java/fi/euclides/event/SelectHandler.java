package fi.euclides.event;

import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Messages;
import fi.euclides.model.MP;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Cirkel3;
import fi.euclides.model.Destroyable;
import fi.euclides.model.GeoImage;
import fi.euclides.model.Label;
import fi.euclides.model.LabelTrack;
import fi.euclides.model.Lijn;
import fi.euclides.model.LijnPuntCombi;
import fi.euclides.model.Poollijn;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.Track;
import fi.euclides.model.Visitor;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Numbers;

public class SelectHandler extends EventHandler {
	
	static class ImageRotor extends Punt {
		final private GeoImage pl;
		final private Punt[] ps;
		final private Punt center;
		
		public ImageRotor(Numbers x, Numbers y, GeoImage image) {
			super(x,y);
			center = image.center();			
			pl = image;
			ps = (Punt[]) image.getDepend();
		}
		@Override
		public boolean isFree() {
			return true;
		}

		public void moveTo(Numbers x, Numbers y) {
			Numbers ndx = Numbers.sub(x, center.getX());
			Numbers ndy = Numbers.sub(y, center.getY());
			Numbers odx = Numbers.sub(getX(), center.getX());
			Numbers ody = Numbers.sub(getY(), center.getY());

			double ot = Math.atan2(ody.doubleValue(), odx.doubleValue());
			double nt = Math.atan2(ndy.doubleValue(), ndx.doubleValue());
			double angle = nt - ot;

			Numbers cos = Numbers.createDouble(Math.cos(angle));
			Numbers sin = Numbers.createDouble(Math.sin(angle));
			
// als p[1] is dependend on p[0] dan versterkt de move zich.
			Numbers[] orgs = new Numbers[ps.length*2];
			for(int i = 0; i < ps.length; i++) {
				Punt p1 = ps[i];
				orgs[i*2]= Numbers.sub(p1.getX(), center.getX());
				orgs[i*2+1] = Numbers.sub(p1.getY(), center.getY());
			}
// rotate around center			
			for(int i = 0; i < ps.length; i++) {
				Numbers xi = orgs[i*2];
				Numbers yi = orgs[i*2+1];
				orgs[i*2] = Numbers.sub(Numbers.mul(xi, cos), Numbers.mul(yi, sin));
				orgs[i*2+1] = Numbers.add(Numbers.mul(xi, sin), Numbers.mul(yi, cos));			
			}
			
			for (int i = 0; i < ps.length; i++) {
				ps[i].moveTo(Numbers.add(center.getX(), orgs[i*2]), Numbers.add(center.getY(), orgs[i*2+1]));
			}
			super.moveTo(x, y);
		}
		/* (non-Javadoc)
		 * @see fi.euclides.model.Punt#visit(fi.euclides.model.Visitor)
		 */
		public void visit(Visitor v) {
			pl.visit(v);
		}
		
	}
	
	
	
	
	static class LineMover extends Punt {
		private Destroyable pl;
		private Punt[] ps;

		public LineMover(Numbers x, Numbers y, Destroyable l, Punt[] movers) {
		    super(x,y);
		    pl = l;
		    ps = movers;
		}
		public LineMover(Numbers x, Numbers y, Destroyable l) {
			super(x,y);
			pl = l;
			ps = (Punt[]) pl.getDepend();
		}
	
		@Override
		public boolean isFree() {
			return true;
		}

		public LineMover(Numbers x, Numbers y, LijnPuntCombi l) {
			super(x, y);
			pl = l;
			ps = new Punt[] { l.getPunt() };
		}

		public LineMover(Numbers x, Numbers y, Cirkel c) {
			super(x, y);
			pl = c;
			if(c instanceof Cirkel3)
				ps = (Punt[])c.getDepend();
			else {
				if(c.isr2c())
				{
					ps = new Punt[2];
					ps[1] = c.getRadius();
				} else
					ps = new Punt[1];
				ps[0] = c.getCenter();
			}
		}
		
		public LineMover(Numbers lastx, Numbers lasty, GeoImage image) {
			super(lastx, lasty);
			pl = image;
			ps = (Punt[]) image.getDepend();
		}
		/* (non-Javadoc)
		 * @see fi.euclides.model.Punt#moveTo(fi.euclides.model.math.Numbers, fi.euclides.model.math.Numbers)
		 */
		public void moveTo(Numbers x, Numbers y) {
			Numbers dx = Numbers.sub(x, getX());
			Numbers dy = Numbers.sub(y, getY());
// als p[1] is dependend on p[0] dan versterkt de move zich.
			Numbers[] orgs = new Numbers[ps.length*2];
			for(int i = 0; i < ps.length; i++) {
				Punt p1 = ps[i];
				orgs[i*2]= p1.getX();
				orgs[i*2+1] = p1.getY();
			}
			for (int i = 0; i < ps.length; i++) {
				ps[i].moveTo(Numbers.add(dx, orgs[i*2]), Numbers.add(dy, orgs[i*2+1]));
			}
			super.moveTo(x, y);
		}
		/* (non-Javadoc)
		 * @see fi.euclides.model.Punt#visit(fi.euclides.model.Visitor)
		 */
		public void visit(Visitor v) {
			pl.visit(v);
		}

	}


	public SelectHandler() {
		super(Messages.getString("SelectHandler.0")); //$NON-NLS-1$
		testPunt=true;
		testLabel=true;
		testLijn =true;
	}
	

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerClicked(double, double)
	 */
	public void pointerClicked(Numbers x, Numbers y, TrackerContext context) {
		getSelectContext(context).click=true;
		//testLijn=true;
		context.enter();
		testHits(x.doubleValue(),y.doubleValue(),context);
		context.exit();
		getSelectContext(context).click=false;
		//testLijn=false;
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerPressed(double, double)
	 */
	public void pointerPressed(Numbers x, Numbers y, TrackerContext context) {
	  InSelectContext selectContext = getSelectContext(context);
      selectContext.lastx = x;
	  selectContext.lasty = y;
	  selectContext.track = null;
	  testHits(x.doubleValue(), y.doubleValue(),context);
	  context.setTrack(selectContext.track);
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#pointerDragged(fi.euclides.model.math.Numbers, fi.euclides.model.math.Numbers)
	 */
	public void pointerDragged(Numbers x, Numbers y, TrackerContext context) {
		if(getSelectContext(context).track == null)  // for capture effect.
		{
		  getSelectContext(context).lastx = x; 
		  getSelectContext(context).lasty = y;
		}
		super.pointerDragged(x, y,context);
		if (context.getTrack() != null) {
          tracker.adapt(AbstractViewer.class).dragging();
		}
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerReleased(double, double)
	 */
	public void pointerReleased(Numbers x, Numbers y, TrackerContext context) {
	  getSelectContext(context).track = null;
	  context.setTrack(null);
      tracker.adapt(AbstractViewer.class).dragging();
	}


	/**
	 * @param p
	 * @return
	 */
	protected boolean freePunt(Punt p) {
		return p.isFree();
	}
	
	protected boolean freePunt(Punt[] ps)
	{
		for (int i = 0; i < ps.length; i++) {
			Punt p = ps[i];
			if(!freePunt(p))
				return false;
		}
		return true;
	}


	protected boolean freeCombiLijn(Lijn l) {
		if (l instanceof Poollijn)
			return false;
		if (l instanceof LijnPuntCombi)
		{
			LijnPuntCombi<?> lp = (LijnPuntCombi<?>)l;
			return freePunt(lp.getPunt());
		}
		return false;
	}

	protected boolean freePuntenLine(Lijn l) {
		if(l instanceof PuntenLijn)
		{
			return freePunt((Punt[])l.getDepend());
		}
		return false;
	}
	
	protected boolean freeCirkel(Cirkel c)
	{
		if(c instanceof Cirkel3)
			return freePunt((Punt[])c.getDepend());
		return freePunt(c.getCenter()) && (!c.isr2c() || freePunt(c.getRadius()));
	}

	protected InSelectContext createContext(TrackerContext context) {
	  return new InSelectContext(context);
	}
	
	@Override protected final Visitor inContext(TrackerContext c) {
	  return getSelectContext(c);
	}

	protected InSelectContext getSelectContext(TrackerContext context) {
	  DefaultAdapter t = DefaultAdapter.getDefault(context);
	  InSelectContext r = t.adapt(InSelectContext.class);
	  if (r == null) {
	    r = createContext(context);
	    t.put(InSelectContext.class, r);
	  }
	  return r;
	}
  protected class InSelectContext extends InContext {

    protected boolean click;
    protected Numbers lastx;
    protected Numbers lasty;
    protected Track track;
 
    protected InSelectContext(TrackerContext context) {
      super(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see euclides.event.EventHandler#visitPunt(euclides.Punt)
     */
    public void visitPunt(Punt p) {
      if (click) {
        super.visitPunt(p);
        return;
      }

      if (freePunt(p)) {
        if (track == null) track = new Track(p);
        done = true;
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see fi.euclides.event.EventHandler#visitLijn(fi.euclides.model.Lijn)
     */
    public void visitLijn(Lijn l) {
      if (click)
        super.visitLijn(l);
      else if (track == null) {

        if (freePuntenLine(l)) {
          track = new Track(new LineMover(lastx, lasty, (PuntenLijn) l));
        } else if (freeCombiLijn(l)) {
          track = new Track(new LineMover(lastx, lasty, (LijnPuntCombi<?>) l));
        }
      }
    }

    public void visitLabel(Label l) {
      if (click)
        super.visitLabel(l);
      else if (track == null) {
        track = new LabelTrack(l, lastx, lasty);
      }
    }

    
    
    
    @Override
	public void visitImage(GeoImage image) {
		if (click)
			super.visitImage(image);
		else if (track == null) {
			if (image.isMove(lastx, lasty)) 
				track = new Track(new LineMover(lastx, lasty, image));
			else
				track = new Track(new ImageRotor(lastx, lasty, image));
		}
	}

	/*
     * (non-Javadoc)
     * 
     * @see fi.euclides.event.EventHandler#visitCirkel(fi.euclides.model.Cirkel)
     */
    public void visitCirkel(Cirkel c) {
      if (click)
        super.visitCirkel(c);
      else if (track == null && freeCirkel(c)) track = new Track(new LineMover(lastx, lasty, c));
    }

    /*
     * (non-Javadoc)
     * 
     * @see fi.euclides.event.EventHandler#visitMP(fi.euclides.locus.MP)
     */
    public void visitMP(MP l) {
      if (click)
        super.visitMP(l);
      else if (track == null && freeMP(l)) track = new Track(new LineMover(lastx, lasty, l));
    }

    protected boolean freeMP(MP l) {
      return l.getDepend() instanceof Punt[] && freePunt((Punt[]) l.getDepend());
    }

    @Override
    public void visitBoog(Boog b) {
      Punt[] movers;
      if (click) super.visitBoog(b);
      else if (track == null && null != (movers=freeBoog(b))) track = new Track(new LineMover(lastx, lasty, b, movers));
    }


  }

	/* Empty...
	 * @see euclides.event.EventHandler#clear()
	 */
	@Override
	void clear(TrackerContext context) {
	}


    protected Punt[] freeBoog(Boog b) {
      Destroyable depend[] = b.getDepend();
      Punt[] p ;
      if (depend instanceof Punt[]) { // arc(P1,P2,P3)
        p = (Punt[]) depend;
      } else
      if (depend[0] instanceof Punt && depend[1] instanceof Punt ) { //arc(P1, P2, angle);
        p = new Punt[] { (Punt) depend[0], (Punt) depend[1] };
      } else  {
        p = new Punt[] { (Punt) depend[0] };
      }
      if (freePunt(p)) return p;
      return null;
    }



}
