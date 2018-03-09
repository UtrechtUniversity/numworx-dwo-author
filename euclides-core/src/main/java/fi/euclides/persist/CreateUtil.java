package fi.euclides.persist;

import fi.euclides.model.Bissectrice;
import fi.euclides.model.CarryingLine;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Cirkel3;
import fi.euclides.model.CirkelLijnSnijpunt;
import fi.euclides.model.CirkelSnijpunt;
import fi.euclides.model.ConflictLijn;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Dpunt;
import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.LoodLijn;
import fi.euclides.model.MiddelPunt;
import fi.euclides.model.ParallelLijn;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.Ray;
import fi.euclides.model.Segment;
import fi.euclides.model.SnijPunt;
import fi.euclides.model.SpiegelPunt;
import fi.euclides.model.Triangle;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.ZwaartePunt;
import fi.euclides.util.Hashtable;

public class CreateUtil {

	public final static Hashtable<String, Creator> buildmap = new Hashtable<String, Creator>(); 
	static {
		buildmap.put(Punt.TYPE, new Creator() {
			public Destroyable create() {
				return new VrijPunt();
			}});
		buildmap.put(MiddelPunt.TYPE, new Creator() {
			public Destroyable create() {
				return new MiddelPunt();
			}});
		buildmap.put(Label.TYPE, new Creator() {
			public Destroyable create() {
				return new Label();
			}});
		buildmap.put(PuntenLijn.TYPE, new Creator() {
			public Destroyable create() {
				return new PuntenLijn();
			}});
		buildmap.put(Segment.TYPE, new Creator() {
			public Destroyable create() {
				return new Segment();
			}});
		buildmap.put(Cirkel.TYPE, new Creator() {
			public Destroyable create() {
				return new Cirkel();
			}});
		buildmap.put(Cirkel3.TYPE, new Creator() {
			public Destroyable create() {
				return new Cirkel3();
			}});
		buildmap.put(Lijn.PUNTOP, new Creator() {
			@SuppressWarnings("rawtypes")
			public Destroyable create() {
				return new PuntOp();
			}});
		buildmap.put(Cirkel.PUNTOP, buildmap.get(Lijn.PUNTOP));

		buildmap.put(LoodLijn.TYPE, new Creator() {
			public Destroyable create() {
				return new LoodLijn();
			}});
		buildmap.put(ParallelLijn.TYPE, new Creator() {
			public Destroyable create() {
				return new ParallelLijn();
			}});
		buildmap.put(Dpunt.TYPE, new Creator() {
			public Destroyable create() {
				return new Dpunt();
			}});
		buildmap.put(Bissectrice.TYPE, new Creator() {
			public Destroyable create() {
				return new Bissectrice();
			}});
		buildmap.put(SnijPunt.TYPE, new Creator() {
			public Destroyable create() {
				return new SnijPunt();
			}});
		buildmap.put(CirkelSnijpunt.TYPE, new Creator() {
			public Destroyable create() {
				return new CirkelSnijpunt();
			}});
		buildmap.put(CirkelLijnSnijpunt.TYPE, new Creator() {
			public Destroyable create() {
				return new CirkelLijnSnijpunt();
			}});
		buildmap.put(ZwaartePunt.TYPE, new Creator() {
			public Destroyable create() {
				return new ZwaartePunt();
			}});
		buildmap.put(ConflictLijn.TYPE, new Creator() {
			public Destroyable create() {
				return new ConflictLijn();
			}});
		buildmap.put(CarryingLine.TYPE, new Creator() {
			public Destroyable create() {
				return new CarryingLine();
			}});
		buildmap.put(Ray.TYPE, new Creator() {
			public Destroyable create() {
				return new Ray();
			}});
		buildmap.put(SpiegelPunt.TYPE, new Creator() {
			public Destroyable create() {
				return new SpiegelPunt();
			}});
		buildmap.put(Locus.TYPE, new Creator() {
			public Destroyable create() {
				return new Locus();
			}});
		buildmap.put(Kegelsnede2.TYPE, new Creator() {
			public Destroyable create() {
				return new Kegelsnede2();
			}});
		buildmap.put(Triangle.TYPE, new Creator() {
			public Destroyable create() {
				return new Triangle();
			}});
		buildmap.put(HorizontalPunt.TYPE, new Creator() {
			@Override
			public Destroyable create() {
				return new HorizontalPunt();
			}
		});
		buildmap.put(Coordinaten.TYPE, new Creator() {
			public Destroyable create() {
				return new Coordinaten();
			}
		});
	}

	public static Destroyable create(String q) {	
		try {
			return buildmap.get(q).create();
		} catch (RuntimeException e) {
			System.err.println("create " + q + "," + e);
			throw e;
		}
	}

	abstract public static interface Creator {
		public abstract Destroyable create();
	}

	
	
}
