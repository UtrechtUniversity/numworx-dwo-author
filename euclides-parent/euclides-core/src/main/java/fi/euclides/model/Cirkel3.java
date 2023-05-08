package fi.euclides.model;

import java.io.IOException;

import fi.euclides.util.DComparator;
import fi.euclides.util.Observable;

public class Cirkel3 extends Cirkel {
	
		public String key() {
			return TYPE;
		}

	
		public static final String TYPE = "ct";
		ConflictLijn l1, l2;
		Punt[] depend;
		public Cirkel3() {
			l1 = new ConflictLijn();
			l2 = new ConflictLijn();
			setCenter(new SnijPunt(l1,l2)); // FIXME or MiddelPunt if degenerated
			setRadius2(getCenter());
			getCenter().deleteObserver(this);
		}
		
		public Cirkel3(Punt a, Punt b, Punt c)
		{
			this(new Punt[] {a,b,c} );
		}

		public Cirkel3(Punt[] punts) {
			this();
			DComparator.sort(punts);
			depend = punts;
			setRadius(punts[0]);
			punts[1].addObserver(this);
			punts[2].addObserver(this);
			l1.p1 = punts[0]; l1.p2 = punts[1];
			l2.p1 = punts[0]; l2.p2 = punts[2];
			recalc();
		}

		/**
		 * @return the depend
		 */
		public Destroyable[] getDepend() {
			return depend;
		}
		
		void recalc() {
			getCenter().update(l1, null);
			super.recalc();
		}
		public void update(Observable observable, Object arg) {
			if(arg == DESTROY)
				destroy();
			else if(observable == depend[0] || observable==depend[1]||observable==depend[2])
			{	recalc();
				notifyObservers();
			}
		}

		public void read(Codec codec) throws IOException {
			Punt p1 = codec.readPunt();
			Punt p2 = codec.readPunt();
			Punt p3 = codec.readPunt();
			depend = new Punt[] { p1,p2,p3 };
			if(p1 != null && p1 != null && p3 != null) 
			{
				setRadius(p1);
				p2.addObserver(this);
				p3.addObserver(this);
				l1.p1 = p1; l1.p2 = p2;
				l2.p1 = p1; l2.p2 = p3;
				recalc();
			}
		}
		public void write(Codec codec) throws IOException {
			codec.write(depend);
		}

		/* (non-Javadoc)
		 * @see fi.euclides.model.Cirkel#incident(fi.euclides.model.Destroyable)
		 */
		public boolean incident(Destroyable other) {
			return super.incident(other) || other == depend[1] || other == depend[2];
		}

		public Destroyable[] getImage(Destroyable mirror)
		{
			if(mirror instanceof Cirkel)
			{
				Punt[] image = new Punt[3];
				for (int i = 0; i < depend.length; i++) {
					image[i] = depend[i].getImage(mirror, this);
					image[i].setVisible(false);
				}
				return new Destroyable[] { image[0], image[1], image[2], new Cirkel3(image) };
			}
			return super.getImage(mirror);
		}
		
		
}
