package fi.euclides.model;

import java.io.IOException;

import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class MiddelPunt extends Punt implements Observer {

		public static final String TYPE = "Pm";

	/* (non-Javadoc)
	 * @see euclides.Punt#key()
	 */
	public String key() {
		return TYPE;
	}

	
		/* (non-Javadoc)
	 * @see euclides.Punt#read(euclides.Codec)
	 */
	public void read(Codec codec) throws IOException {
		Punt p;
		p = codec.readPunt();
		if(p!= null) setP1(p);
		p = codec.readPunt();
		if(p != null) setP2(p);
		if(p1 != null && p2 != null) 
			recalc();
	}

	/* (non-Javadoc)
	 * @see euclides.Punt#write(euclides.Codec)
	 */
	public void write(Codec codec) throws IOException {
		codec.writePunt(getP1());
		codec.writePunt(getP2());
	}


		Punt p1, p2, depend[] = new Punt[2];
		
		/**
		 * @param p1
		 * @param p2
		 */
		public MiddelPunt(Punt p1, Punt p2) {
			if(p1.getIndex()<p2.getIndex())
			{ this.p1 = p1;
			  this.p2 = p2;
			} else {
				this.p2 = p1;
				this.p1 = p2;
			}
			depend[0] = p1;
			depend[1] = p2;
			p2.addObserver(this);
			p1.addObserver(this);
			recalc();
		}
				
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean same(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final MiddelPunt other = (MiddelPunt) obj;
			if (p1 == null) {
				if (other.p1 != null)
					return false;
			} else if (!p1.equals(other.p1))
				return false;
			if (p2 == null) {
				if (other.p2 != null)
					return false;
			} else if (!p2.equals(other.p2))
				return false;
			return true;
		}

		public MiddelPunt() {
		}

		private void recalc() {
			setXY( Numbers.div(Numbers.add(p1.getX(),p2.getX()), Numbers.TWO), 
				   Numbers.div(Numbers.add(p1.getY(),p2.getY()), Numbers.TWO ));
		}

		public boolean isDefined() {
			return p2!=null && p1 != null && p2.isDefined() && p1.isDefined();
		}

		public void moveTo(Numbers dx, Numbers dy) {
		}

		public void update(Observable o, Object arg) {
			if(arg == DESTROY)
				destroy();
			else {
				if(o == p2 || o == p1)
				{
					recalc();
					notifyObservers(arg);
				}
			}
		}


		/**
		 * @return the p1
		 */
		public Punt getP1() {
			return p1;
		}


		/**
		 * @param p1 the p1 to set
		 */
		public void setP1(Punt p1) {
			depend[0]=p1;
			this.p1 = p1;
			p1.addObserver(this);
		}


		/**
		 * @return the p2
		 */
		public Punt getP2() {
			return p2;
		}


		/**
		 * @param p2 the p2 to set
		 */
		public void setP2(Punt p2) {
			depend[1] = p2;
			this.p2 = p2;
			p2.addObserver(this);
		}


		/**
		 * @return the depend
		 */
		public Destroyable[] getDepend() {
			return depend;
		}
		
		
	
	
}
