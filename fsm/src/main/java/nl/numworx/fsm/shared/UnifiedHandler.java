package nl.numworx.fsm.shared;

import java.util.Optional;
import java.util.Vector;
import java.util.logging.Logger;

import fi.euclides.event.EventHandler;
import fi.euclides.event.HumanContext;
import fi.euclides.event.TrackerContext;
import fi.euclides.model.Boog;
import fi.euclides.model.Destroyable;
import fi.euclides.model.LijnTrack;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Track;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DefaultAdapter;

public class UnifiedHandler extends EventHandler {
	
	private static final long TEN_SECS = 10000L;
	private Logger LOG = Logger.getLogger(getClass().getName());
	private Punt start;

	public UnifiedHandler(String string) {
		super(string);
		testPunt = true;
		testLijn = true;
		
	}

	@Override
	public void command() {
		LOG.info("command()");
		super.command();
	}

	@Override
	public void pointerDragged(Numbers x, Numbers y, TrackerContext context) {
		HumanContext hc = context.getAdapter().adapt(HumanContext.class);
		LOG.info("pointerDragged " + x + "," + y + " shift:" + hc.isShiftDown() + " ts" + hc.getTimestamp());
		reset(Numbers.NaN, Numbers.NaN, 0L);
		super.pointerDragged(x, y, context);
		if (hc.isShiftDown())
			getTracker().paint();
	}

	@Override
	public void pointerPressed(Numbers x, Numbers y, TrackerContext context) {
		HumanContext hc = context.getAdapter().adapt(HumanContext.class);
		LOG.info("pointerPressed " + x + "," + y + " shift:" + hc.isShiftDown() + " ts" + hc.getTimestamp());
		testHits(x.doubleValue(), y.doubleValue(), context);
		if (!context.selection().isEmpty()) {
			Destroyable first = (Destroyable) context.selection().firstElement();
			if (first instanceof Punt) {
				Punt p = (Punt) first;
				if (hc.isShiftDown()) {
					Segment s = new Segment(); s.visit(decorator);
					Track track = new LijnTrack(p.getX(), p.getY(), s);
					context.setTrack(track);
					start = p;
				} else {
					Track t = new Track(p);
					context.setTrack(t);
				}
			}
		}
	}

	@Override
	public void pointerReleased(Numbers x, Numbers y, TrackerContext context) {
		HumanContext hc = context.getAdapter().adapt(HumanContext.class);
		LOG.info("pointerReleased " + x + "," + y + " shift:" + hc.isShiftDown() + " ts" + hc.getTimestamp());
		if (context.getTrack() instanceof LijnTrack) {
			testLijn = false;
			testHits(x.doubleValue(), y.doubleValue(), context);
			testLijn = true;
			if (!context.selection().isEmpty()) {
				Punt p = (Punt) context.selection().firstElement();
				if (p != start) {
					getModel().buildSegment(new Punt[] { start, p}, Optional.of(decorator));
				} else {
					// add booghandler?
					Boog b = AddBoogHandler.buildBoog(p);
					b.visit(decorator);
					getModel().add(b);
				}
				start = null;
			}
		}
		
		context.setTrack(null);
	}

	public void pointerClickedLong(Numbers x, Numbers y, TrackerContext tc) {
		reset(x, y, System.currentTimeMillis());
		tc.setTrack(null);
		getModel().destroy();
	}

	private Numbers lastX = Numbers.NaN;
	private Numbers lastY = Numbers.NaN;
	private long lastClick;
	private int clickCount;
	
	private boolean near(Numbers x, Numbers y, long when) {
		Numbers dist = Numbers.hypot(Numbers.sub(x, lastX), Numbers.sub(y, lastY));
		dist = Numbers.sub(dist, Numbers.createInteger(10));
		return Numbers.signum(dist) <= 0 && when < lastClick;
	}
	private int accept(Numbers x, Numbers y, long when) {
		lastX = x;
		lastY = y;
		lastClick = when + TEN_SECS; // 10 seconds;
		return clickCount += 1;
	}
	
	private void reset(Numbers x, Numbers y, long when) {
		lastX = x;
		lastY = y;
		lastClick = when + TEN_SECS;
		clickCount = 0;
	}
	
	@Override
	public void pointerClicked(Numbers x, Numbers y, TrackerContext context) {
		HumanContext hc = context.getAdapter().adapt(HumanContext.class);
		//LOG.info("pointerClicked " + x + "," + y + " shift:" + hc.isShiftDown() + " ts" + hc.getTimestamp());
		long when = hc.getTimestamp();
		if (near(x, y, when))
		{
			int cnt = accept(x, y, when);
			//LOG.info("Clickcount = " + cnt);
			if (cnt == 1) {
				Vector<Destroyable> selection = context.selection();
				if (selection.size()==1 && selection.get(0) instanceof Punt) {
					DefaultAdapter a = DefaultAdapter.getDefault(selection.get(0));
					boolean accept = Boolean.TRUE.equals(a.adapt(Boolean.class));
					a.put(Boolean.valueOf(!accept)); // toggle accept state
				} else {				
					getModel().clearSelection();
					Punt p = getModel().buildPunt(x,y);
					p.visit(decorator);
				}
			}
		} else {
			reset(x,y,when);
			startPointerClicked(x, y, context);
		}
		
		super.pointerClicked(x, y, context);
	}

	protected void startPointerClicked(Numbers x, Numbers y, TrackerContext context) {
		// TODO Delay single click
		
	}

}
