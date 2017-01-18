package fi.euclides.event;

import java.util.ArrayList;
import java.util.Arrays;

import fi.euclides.util.Messages;
import fi.euclides.model.MP;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Cirkel3;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.LabelTrack;
import fi.euclides.model.Lijn;
import fi.euclides.model.LijnPuntCombi;
import fi.euclides.model.Poollijn;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.Track;
import fi.euclides.model.Visitor;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Numbers;

public class SelectHandler extends EventHandler {
	
	static class LineMover extends Punt {
		private Destroyable pl;
		private Punt[] ps;
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
	
	protected boolean click;
	private Numbers lastx;
	private Numbers lasty;

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerClicked(double, double)
	 */
	public void pointerClicked(Numbers x, Numbers y) {
		click=true;
		//testLijn=true;
		testHits(x.doubleValue(),y.doubleValue());
		click=false;
		//testLijn=false;
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerPressed(double, double)
	 */
	public void pointerPressed(Numbers x, Numbers y) {
		this.lastx = x;
		this.lasty = y;
		track = null;
		testHits(x.doubleValue(), y.doubleValue());
		tracker.setTrack(track);
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#pointerDragged(fi.euclides.model.math.Numbers, fi.euclides.model.math.Numbers)
	 */
	public void pointerDragged(Numbers x, Numbers y) {
		if(track == null)  // for capture effect.
		{
			lastx = x; 
			lasty = y;
		}
		super.pointerDragged(x, y);
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#pointerReleased(double, double)
	 */
	public void pointerReleased(Numbers x, Numbers y) {
		track = null;
		tracker.setTrack(null);
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#visitPunt(euclides.Punt)
	 */
	public void visitPunt(Punt p) {
		if(click)
		{
			super.visitPunt(p);
			return;
		}
		
		if(freePunt(p) )
		{	if(track == null) track = new Track(p);
		    done = true;
		}
	}

	/**
	 * @param p
	 * @return
	 */
	private boolean freePunt(Punt p) {
		return p.isFree();
	}
	
	private boolean freePunt(Punt[] ps)
	{
		for (int i = 0; i < ps.length; i++) {
			Punt p = ps[i];
			if(!freePunt(p))
				return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#visitLijn(fi.euclides.model.Lijn)
	 */
	public void visitLijn(Lijn l) {
		if(click)
			super.visitLijn(l);
		else if(track == null) {
			
			if(freeLine(l))
			{
				track = new Track(new LineMover(lastx, lasty, (PuntenLijn)l));
			} else if(freeCombiLijn(l))
			{
				track = new Track(new LineMover(lastx, lasty, (LijnPuntCombi)l));
			}
		}
	}

	private boolean freeCombiLijn(Lijn l) {
		if (l instanceof Poollijn)
			return false;
		if (l instanceof LijnPuntCombi)
		{
			LijnPuntCombi lp = (LijnPuntCombi)l;
			return freePunt(lp.getPunt());
		}
		return false;
	}

	private boolean freeLine(Lijn l) {
		if(l instanceof PuntenLijn)
		{
			return freePunt((Punt[])l.getDepend());
		}
		return false;
	}
	
	private boolean freeCirkel(Cirkel c)
	{
		if(c instanceof Cirkel3)
			return freePunt((Punt[])c.getDepend());
		return freePunt(c.getCenter()) && (!c.isr2c() || freePunt(c.getRadius()));
	}

	public void visitLabel(Label l)
	{
		if(click)
			super.visitLabel(l);
		else if(track == null)
		{
			track = new LabelTrack(l,lastx, lasty);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#visitCirkel(fi.euclides.model.Cirkel)
	 */
	public void visitCirkel(Cirkel c) {
		if(click)
			super.visitCirkel(c);
		else if(track == null && freeCirkel(c))
			track = new Track(new LineMover(lastx, lasty, c));
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#visitMP(fi.euclides.locus.MP)
	 */
	public void visitMP(MP l) {
		if(click)
			super.visitMP(l);
		else if(track == null && l.getDepend() instanceof Punt[] && freePunt((Punt[])l.getDepend()))
			track = new Track(new LineMover(lastx, lasty, l));
	}

	/* Empty...
	 * @see euclides.event.EventHandler#clear()
	 */
	void clear() {
	}

	@Override
	public void visitBoog(Boog b) {
		if (click)
			super.visitBoog(b);
	}

}
