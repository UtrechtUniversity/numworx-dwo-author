package nl.numworx.geodefiner.common;

import fi.euclides.event.Tracker;
import fi.euclides.expr.DestroyDependency;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.Segment;
import fi.euclides.model.ZwaartePunt;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelTester;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;
import fi.euclides.util.Timer;

public class Animator extends LabelTester implements Observer {
	final public Animate animate;
	final public int interval;

	private PuntOp<Segment> pos;
	private Punt Pmin, Pmax;
	private Numbers Xmin;
	private Numbers Xmax;
	private Numbers Xstep = Numbers.ONE;
	private volatile boolean dir;
	private volatile T running;
	private Label button;
	
	private class T extends Timer {
		@Override
		public void run() {
			do1step();
			getTracker().paint();
		}
	}
	
	private int period;
	private Align align = Align.BOTTOM;
	
	public Animator(Animate animate, int interval, Tracker tracker, Align align) {
		super("animate");
		setTracker(tracker);
		this.animate = animate;
		this.interval = interval;
		this.align = reverse(align);
		dir = animate != Animate.SAW;
	}

	private Align reverse(Align other) {
		switch(other) {
		case BOTTOM: return Align.TOP;
		case TOP: case BASE: return Align.BOTTOM;
		}
		return align;
	}

	public synchronized void start() {
		if(running == null && pos != null)
		{
			running = new T();
			running.scheduleRepeating(period);
			button.setString("||");
		}
	}
	
	public synchronized void stop() {
		if(running != null) {
			running.cancel();
			running = null;
			button.setString("\u25B6");
		}
	}
	
	public synchronized void command() {
		if (running != null) stop();
		else start();
	}
	
	@SuppressWarnings("unchecked")
	public synchronized Label install(Label label) {
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
			period = 1000/25; // 24 Hz, 40ms
// er moet gelden:
// period / interval = Xstep / DXn
// rekenvoorbeeld: interval = 10s, DX=100px
// periode = 1sec dan 10px per keer
// periode = 100ms dan 1px per keer periode = interval /DXn
// stap = period * DX / interval
			
			Xstep = Numbers.div(Numbers.mul(s.getDXn(), Numbers.createInteger(period)), Numbers.createInteger(interval));
			Xstep = Numbers.abs(Xstep);
			if(Xstep.doubleValue() < 1.0) {
				Xstep = Numbers.ONE;
				period = (int) Math.abs(Math.round(interval / s.getDX()));
			}
			Pmax.addObserver(this);
			Pmin.addObserver(this);
			ZwaartePunt p = new ZwaartePunt(s);
			button = define(Label.EMPTY);
			p.addObserver(new DestroyDependency(button));
			button.setString("\u25B6");
			button.setP(p);
			DefaultAdapter.getDefault(button).put(align);
			getTracker().getModel().add(button);
		} else {
			if(button != null)
			{
				button.destroy();
				button = null;
			}
		}
		return button;
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

	@Override
	public Destroyable[] createDepend() {
		return Label.EMPTY;
	}

	@Override
	protected boolean test(Label l) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean define(Label l) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
