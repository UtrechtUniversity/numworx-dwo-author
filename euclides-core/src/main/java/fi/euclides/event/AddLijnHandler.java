package fi.euclides.event;

import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Destroyable;
import fi.euclides.model.LijnTrack;
import fi.euclides.model.Model;
import fi.euclides.model.OpObject;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.Ray;
import fi.euclides.model.Segment;
import fi.euclides.model.Track;
import fi.euclides.model.math.Numbers;

public class AddLijnHandler extends EventHandler {

	public static final char LINE = 'L';
	public static final char SEGMENT = 'S';
	public static final char RAY = 'R';
	private int state;
	private Punt p;

	// enum: Line, Segment, Ray, etc
	char s;

	public AddLijnHandler(char s) {
		this();
		this.s = s;
	}

	/**
	 * @param s
	 * @deprecated gebruik {@link #AddLijnHandler(char)}
	 */
	public AddLijnHandler(boolean s) {
		this();
		this.s = s?SEGMENT: LINE;
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.event.EventHandler#pointerPressed(double, double)
	 */
	public void pointerPressed(Numbers x, Numbers y, TrackerContext context) {
		if(state == 1)
		{
		    Track track = new LijnTrack(p.getX(), p.getY(), getTrackable());
			context.setTrack(track);
			pointerDragged(x,y,context);
		} else if(state == 0)
		{	
			Track track=new Track(x, y);
			context.setTrack(track);
			pointerDragged(x,y,context);
		}
		
	}

	/* (non-Javadoc)
	 * @see fi.euclides.model.event.EventHandler#pointerReleased(double, double)
	 */
	public void pointerReleased(Numbers x, Numbers y, TrackerContext context) {
		pointerDragged(x,y,context);
		context.setTrack(null);
		Model model = getModel();
		Vector<Destroyable> select = model.getSelect();
		if (state == 1)
		{	Destroyable p1;
			if(select.size()==1 && select.firstElement() instanceof Punt)
				p1 = select.elementAt(0);
			else {
//				if(s == SEGMENT && select.isEmpty())
//					model.toggle(p);
				p1 = visit(model.buildPunt(x, y));
				model.toggle(p1);
			}
			model.clearSelection();
			model.toggle(p);
			model.toggle(p1);
			
			build();
			command();
		} else if (state == 0)
		{			
			if (select.isEmpty() 
				|| select.firstElement() instanceof OpObject
			) {
				Destroyable p = visit(model.buildPunt(x, y));
				model.toggle(p);
			} 
				
			command();
		}
	}

	private void build() {
		switch(s) {
		case SEGMENT:
			visit(getModel().buildSegment());;
			return;
		case RAY:
			visit(getModel().buildRay());
			return;
		default:
			visit(getModel().buildLijn());
		}
		testLijn = true;
	}

	public AddLijnHandler() {
		super(Messages.getString("AddLijnHandler.0")); //$NON-NLS-1$
		this.testPunt = true;
		this.testLijn = true;
	}


	public void command() {
		Vector select = getModel().getSelect();
		state = select.size();
		if(state == 1)
		{
			final Object object = select.firstElement();
			if(object instanceof Segment || object instanceof Ray)
			{
				build();
				return;
			}
			if(object instanceof Punt)
			{	testLijn=true;	
				p = (Punt) object;
				getTracker().setPointerHandler(this);
				setStatus(Messages.getString("AddLijnHandler.1")); //$NON-NLS-1$
				return;
			} else {
				state = 0;
				getModel().clearSelection();
			}
		}
		if(state == 0)
		{
			super.command();
			return;				
		}
		build();
	}

	private PuntenLijn getTrackable() {
		switch(s)
		{
		case SEGMENT: return new Segment();
		case RAY: return new Ray();
		default: return new PuntenLijn();
		}
	}

	/* (non-Javadoc)
	 * @see fi.euclides.event.EventHandler#allowSelection(java.util.Vector)
	 */
	public boolean allowSelection(Vector selection) {
		return selection.isEmpty() ||
			selection.size() == 1 && selection.firstElement() instanceof Punt ||
			selection.size() == 2 && selection.firstElement() instanceof Punt && selection.lastElement() instanceof Punt;
		
	}

}
