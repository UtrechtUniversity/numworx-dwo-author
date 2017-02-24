package nl.numworx.geodefiner.common;

import fi.euclides.event.Tracker;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.Segment;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;
import fi.euclides.util.Timer;

public class Animator implements Observer {
	final public Animate animate;
	final public int interval;

	private PuntOp<Segment> pos;
	private Punt Pmin, Pmax;
	private Numbers Xmin;
	private Numbers Xmax;
	private Numbers Xstep = Numbers.ONE;
	private volatile boolean dir;
	private volatile T running;
	
	private class T extends Timer {
		@Override
		public void run() {
			do1step();
			tracker.paint();
		}
	}
	
	private Tracker tracker;
	private int period;
	
	public Animator(Animate animate, int interval) {
		this.animate = animate;
		this.interval = interval;
		dir = animate != Animate.SAW;
	}

	public synchronized void start() {
		if(running == null && pos != null)
		{
			running = new T();
			running.scheduleRepeating(period);
		}
	}
	
	public synchronized void stop() {
		if(running != null) {
			running.cancel();
			running = null;
		}
	}
	
	public synchronized void command() {
		if (running != null) stop();
		else start();
	}
	

	@SuppressWarnings("unchecked")
	public synchronized void install(Label label) {
		stop();
		pos = null;
		if(Pmin != null) Pmin.deleteObserver(this);
		if(Pmax != null) Pmax.deleteObserver(this);
		if(label != null) {
			pos = (PuntOp<Segment>) label.getP();
			Segment s = pos.getOp();
			Pmax = s.getP2();
			Pmin = s.getP1();
			Xmax = Pmax.getX();
			Xmin = Pmin.getX();
			period = 100; // 10 Hz
			Xstep = Numbers.div(s.getDXn(), Numbers.createInteger(interval/period));
			if(Xstep.doubleValue() < 1.0) {
				Xstep = Numbers.ONE;
			}
			Pmax.addObserver(this);
			Pmin.addObserver(this);
			tracker = label.getRegistered().getTracker();
		}
	}
		
	private synchronized void do1step() {
		if(pos == null) { stop(); return; }
		Numbers xCur = pos.getX();
		if(dir) {
			xCur = Numbers.add(xCur, Xstep);
			if(Numbers.signum(Numbers.sub(xCur, Xmax))>=0) {
				if(animate == Animate.SEE) {
					xCur = Xmin;
				} else {
					dir = false;
					xCur = Xmax;
				}
			}
		} else {
			xCur = Numbers.sub(xCur,  Xstep);
			if(Numbers.signum(Numbers.sub(xCur, Xmin))<=0) {
				if(animate == Animate.SAW) {
					xCur = Xmax;
				} else {
					dir = true;
					xCur = Xmin;
				}
			}
		}
		pos.setX(xCur);
		pos.notifyObservers();
	}

	@Override
	public void update(Observable observable, Object arg) {
		if(arg == Destroyable.DESTROY) {
			install(null);
			return;
		}
		if( observable == Pmax) {
			Xmax = Pmax.getX();
		} 
		if (observable == Pmin) {
			Xmin = Pmin.getX();
		}
 	}
	
}
