package fi.euclides.proof;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

import fi.euclides.model.Triangle;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.DComparator;
import fi.euclides.util.DefaultAdapter;

public class SimTri extends LabelTester implements Comparator {
	public static final String TYPE = "ST";
	
	public SimTri(String string) {
		super(string);
	}

	public Destroyable[] createDepend() {
		return new Triangle[2];
	}

	public boolean define(Label l) {
		if(test(l))
		{
			Destroyable[] depend = l.getDepend();
			DComparator.sort(depend);
			if(depend[0].equals(depend[1]))
				return false;

			setString(l);
			return true;
		}
		return false;
	}

	private void setString(Label l) {
		if(l.isDefined())
		{
			Destroyable[] d = l.getDepend();
			l.setString( s(d[0]) + " ~ " + s(d[1]));
		} else
			l.setString("");
	}

	public String getSubKey() {
		return TYPE;
	}

	public void command() {
		Vector select = getModel().getSelect();
		Destroyable[] depend = createDepend();
		depend[0] = (Destroyable) select.firstElement();
		depend[1] = (Destroyable) select.lastElement();
		addLabel(depend);
	}
	
	protected boolean test(Label l)
	{
		Triangle t[] = (Triangle[])l.getDepend();
		Punt[] ta = (Punt[])t[0].getDepend();
		Punt[] tb = (Punt[])t[1].getDepend();
		
		Numbers va[] = new Numbers[3];
		Numbers vb[] = new Numbers[3];
		vectors(ta, va);
		vectors(tb, vb);
		Arrays.sort(va, this);
		Arrays.sort(vb, this);
//		Numbers v0 = Numbers.div(va[0], vb[0]);
//		Numbers v1 = Numbers.div(va[2], vb[2]);
		Numbers r = Numbers.sub(Numbers.mul(va[0], vb[2]), Numbers.mul(va[2], vb[0]));
		return setState(l, r, 0.0001);
	}
	

	private void vectors(Punt[] ta, Numbers[] va) {
		Numbers v0 = Numbers.createComplex(ta[0].getX(), ta[0].getY());
		Numbers v1 = Numbers.createComplex(ta[1].getX(), ta[1].getY());
		Numbers v2 = Numbers.createComplex(ta[2].getX(), ta[2].getY());
		va[0] = Numbers.sub(v1, v0);
		va[1] = Numbers.sub(v2, v1);
		va[2] = Numbers.sub(v0, v2);
	}

	protected boolean test_org(Label l) {
		Triangle t[] = (Triangle[])l.getDepend();
		Punt[] ta = (Punt[]) t[0].getDepend();
		Punt[] tb = (Punt[]) t[1].getDepend();
		Numbers[] da = new Numbers[3];
		lengths(ta, da);
		Numbers[] db = new Numbers[3];
		lengths(tb, db);
		Arrays.sort(da, this);
		Arrays.sort(db, this);
		
		//da[0] = Numbers.div(da[0], db[0]);
		//da[1] = Numbers.div(da[1], db[1]);
		//da[2] = Numbers.div(da[2], db[2]);

		da[0] = Numbers.sub(Numbers.mul(da[0], db[1]), Numbers.mul(da[1], db[0]));
		da[1] = Numbers.sub(Numbers.mul(da[1], db[2]), Numbers.mul(da[2], db[1]));
		l.value = Numbers.add(da[0], da[1]);
		DefaultAdapter.getDefault(l).put(new Numbers[] { da[0], da[1] });
		if(da[0].equals(Numbers.ZERO) &&
		   da[1].equals(Numbers.ZERO)		
		)
		{
			l.setState(Label.EXACT);
			return true;
		}
		
		return false;
	}

	/**
	 * @param ta
	 * @param da
	 */
	private void lengths(Punt[] ta, Numbers[] da) {
		da[0] = Numbers.add( 
				Numbers.sqr(Numbers.sub(ta[0].getX(),ta[1].getX()))
			  , Numbers.sqr(Numbers.sub(ta[0].getY(),ta[1].getY()))
			  );
		da[1] = Numbers.add( 
				Numbers.sqr(Numbers.sub(ta[2].getX(),ta[1].getX()))
			  , Numbers.sqr(Numbers.sub(ta[2].getY(),ta[1].getY()))
			  );
		da[2] = Numbers.add( 
				Numbers.sqr(Numbers.sub(ta[0].getX(),ta[2].getX()))
			  , Numbers.sqr(Numbers.sub(ta[0].getY(),ta[2].getY()))
			  );
	}

//	/**
//	 * Unittest.
//	 * @param args 
//	 */
//	public static void main(String[] args) {
//		System.out.println("starting");
//		Numbers.setFactory(new ExactImpl());
//		final Model m = new Model();
//		Punt o = m.buildPunt(Numbers.ZERO, Numbers.ZERO);
//		Punt u = m.buildPunt(Numbers.ONE,  Numbers.ZERO);
//		Punt p3 = m.buildPunt(Numbers.ZERO, Numbers.TWO);
//		m.toggle(o); m.toggle(u); m.toggle(p3);
//		Triangle t1 = m.buildTriangle();
//		Numbers ten = Numbers.createInteger(10);
//		Punt p4 = m.buildPunt(ten, Numbers.ZERO);
//		Punt p5 = m.buildPunt(Numbers.ZERO, Numbers.mul(Numbers.TWO, ten));
//		m.toggle(o); m.toggle(p5); m.toggle(p4);
//		Triangle t2 = m.buildTriangle();
//		
//		m.toggle(t1); m.toggle(t2);
//		SimTri tester = new SimTri("test");
//		tester.setTracker(new Tracker() {
//
//			public boolean contains(double x, double y) {
//				return false;
//			}
//
//			public String describe(Destroyable d) {
//				return "";
//			}
//
//			public NameMapper getMapper() {
//				return m;
//			}
//
//			public Model getModel() {
//				return m;
//			}
//
//			public void paint() {
//			}
//
//			public void setPointerHandler(EventHandler eventHandler) {
//			}
//
//			public void setStatus(String string) {
//				System.out.println(string);				
//			}
//
//			public void setTrack(Track track) {
//			}});
//		
//		tester.command();
//		System.out.println(m.getLijnen());
//		
//		
//
//	}

	public int compare(Numbers n1, Numbers n2)
	{
		return Numbers.signum(Numbers.sub(Numbers.abs(n1), Numbers.abs(n2)));
	}
	
	public int compare(Object arg0, Object arg1) {	
		return compare( (Numbers)arg0, (Numbers)arg1);
	}

}
