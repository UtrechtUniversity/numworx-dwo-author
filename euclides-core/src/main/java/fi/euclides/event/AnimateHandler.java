package fi.euclides.event;

import java.util.Enumeration;

import fi.euclides.util.Hashtable;
import fi.euclides.util.Timer;

import java.util.Random;
import java.util.Vector;

import fi.euclides.util.Messages;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Numbers;

public class AnimateHandler extends EventHandler {

	final static Numbers F = Numbers.createInteger(3);
	final static Numbers D = Numbers.createRational(8, 10);
	final static Numbers O = Numbers.neg(Numbers.div(F, Numbers.TWO));
	class T extends Timer {

		Hashtable anima = new Hashtable();
		Random random = new Random();
		
		Numbers getD()
		{
			double i  = random.nextDouble();
			Numbers r = Numbers.createDouble(i);
			return Numbers.add(O, Numbers.mul(r,F));
		}
		
		public synchronized void run() {
//System.out.println("anima" + anima);
			if(anima.isEmpty())
				cancel();
			Enumeration e = anima.keys();
			while (e.hasMoreElements()) {
				Punt object = (Punt) e.nextElement();
				Punt oldpos = (Punt) anima.get(object);
				Numbers dx = Numbers.add(getD(), Numbers.mul(D , Numbers.sub( object.getX(), oldpos.getX())));
				Numbers dy = Numbers.add(getD(), Numbers.mul(D , Numbers.sub( object.getY(), oldpos.getY())));
				Numbers nx = Numbers.add(dx , object.getX());
				Numbers ny = Numbers.add(dy , object.getY());
				//nx = Numbers.createRational(JMath.round(nx.doubleValue()*120), 120);
				//ny = Numbers.createRational(JMath.round(ny.doubleValue()*120), 120);
				oldpos.setXY(object.getX(), object.getY());
				if(tracker.contains(nx.doubleValue(), ny.doubleValue()))
				{
					object.moveTo(nx, ny);	
				} else {
				}
				
			}
			getTracker().paint();
		}
		
	}

	private static final int PERIOD = 300 * 3;
	T runner;
	public AnimateHandler() {
		super(Messages.getString("AnimateHandler.0")); //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see euclides.event.EventHandler#command()
	 */
	public void command() {
		if(runner !=null)
		{
			runner.cancel();
			runner = null;
			tracker.setStatus(Messages.getString("AnimateHandler.1")); //$NON-NLS-1$
			return;
		}
		runner = new T();
		Vector v = getModel().getSelect();
		if(v.isEmpty())
				v = getModel().getPunten();
		synchronized (runner) {
			runner.anima.clear();
			int l = v.size();
			for(int i = 0; i < l; i++)
			{
				Object o = v.elementAt(i);
				if(o instanceof Punt) {
					Punt p = (Punt)o;
					String k = p.key();
					if(k == Punt.TYPE || p instanceof PuntOp)
					{
						Destroyable oldpos = new VrijPunt(p.getX(), p.getY());
						runner.anima.put(p, oldpos);
					}
				}
			}
			if(runner.anima.isEmpty())
			{
				runner.cancel();
				runner=null;
				return;
			}
		}
		tracker.setStatus(string);
		runner.scheduleRepeating(PERIOD);
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return runner != null;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		if(selected != isSelected())
			command();
	}

}
