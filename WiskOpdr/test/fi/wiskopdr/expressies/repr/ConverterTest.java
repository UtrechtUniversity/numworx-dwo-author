/**
 * 
 */
package fi.wiskopdr.expressies.repr;


import java.util.Locale;

import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.expressies.*;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.text.Text_nl;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author wim
 *
 */
public class ConverterTest extends TestCase {

	/**
	 * @param c
	 */
	public ConverterTest(AbstractConverter c, String name) {
		super(name);
		this.c = c;
	}

	public ConverterTest(String name) {
		this(new StringConverter(),name);
	}
	
	
	AbstractConverter c;
	Expressie e;
	
	protected void setUp() throws Exception {
		WiskOpdr.language = new Locale("nl");
		WiskOpdr.rb = new Text_nl();
		BasisExpressie x = new BasisExpressie("x");
		e = new Abs(x);
		e = new Aftrekking(new BasisExpressie(0),e);
		e = new ArcCosinus(e);
		e = new Bin(e, new ArcSinus(new PI()));
		e = new Ln(e);
		e = new Limiet(e, x,  new BasisExpressie("\u221e"), new BasisExpressie(2));
		e = new Integraal(e, new PI(), new E(), new BasisExpressie("x"));
		e = new Macht(e, new Optelling(x,new NdeLog(x,x)));
		e = new NdeWortel(e, new BasisExpressie(3));
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link fi.wiskopdr.expressies.repr.AbstractConverter#abs(java.lang.Object)}.
	 */
	public void testAbs() {
		Object visit = e.visit(c);
		assertNotNull(visit);
if(c instanceof StringConverter)
			assertEquals(e.toString(), visit.toString());
		else if(c instanceof StrictConverter)
			assertEquals(e.toStringStrikt(), visit.toString());

		System.out.println( visit );
	}
	
	public void testOptelling() {
		e = new Optelling(new BasisExpressie("12"), new Deling(new BasisExpressie(34), new BasisExpressie(56)));
		testAbs();
	}
	public void testMacht() {
		e = new Macht(new Ln(new BasisExpressie("12")), new BasisExpressie(34));
		e = new Macht( new BasisExpressie("2"), e);
		testAbs();
	}
	
	public void testBasis() {
		e = new Optelling( new BasisExpressie("1,0E100"), new BasisExpressie("1,2"));
		e = new Optelling( e, new BasisExpressie("V?(XXX)"));
		testAbs();
	}
	
	public void testVergelijking() {
		VergelijkingMeerv v = FormuleParser.parseVergelijking("$f 1 < x \u2264 2 of x \u2265 4@");
		Object visit = v.visit(c);
		assertNotNull(visit);
		System.out.println( visit );
		if(c instanceof StringConverter)
			assertEquals(v.toString(), visit.toString());
		else if(c instanceof StrictConverter)
			assertEquals(v.toStringStrikt(), visit.toString());
		
	}

	/**
	 * Test method for {@link fi.wiskopdr.expressies.repr.AbstractConverter#expressie(fi.wiskopdr.expressies.Expressie)}.
	 */
	public void testVermenigvuldiging() {
		Optelling breuk = new Optelling(new BasisExpressie("2"), new Deling(new BasisExpressie(3),new BasisExpressie(4)));
		e = new Vermenigvuldiging(new BasisExpressie("x"), new BasisExpressie("y"));
		testAbs();
		e = new Vermenigvuldiging(new BasisExpressie("x"), breuk);
		testAbs();
		e = new Vermenigvuldiging(breuk , new BasisExpressie("x") );
		testAbs();
		e = new Vermenigvuldiging(new BasisExpressie("x"), new Aftrekking(new BasisExpressie(0), new BasisExpressie("z")));
		testAbs();
		e = new Vermenigvuldiging(new BasisExpressie("4"), new Aftrekking(new BasisExpressie(0), new BasisExpressie("z")));
		testAbs();
		e = new Vermenigvuldiging(new Aftrekking(new BasisExpressie(0), new BasisExpressie("z")), new BasisExpressie("y"));
		testAbs();
		e = new Vermenigvuldiging(new Aftrekking(new BasisExpressie(0), new BasisExpressie("z")), new BasisExpressie("3"));
		testAbs();
		e = new Vermenigvuldiging(new BasisExpressie("5"), new BasisExpressie(3));
		testAbs();
	}

	public static Test suite() {
		String t = "testAbs";
		TestSuite s = new TestSuite();
		s.addTest(new ConverterTest(t));
		s.addTest(new ConverterTest(MPReduceConverter.getInstance(),t));
		s.addTest(new ConverterTest(StrictConverter.getInstance(),t));

		t = "testOptelling";
		s.addTest(new ConverterTest(t));
		s.addTest(new ConverterTest(MPReduceConverter.getInstance(),t));
		s.addTest(new ConverterTest(StrictConverter.getInstance(),t));

		t = "testVermenigvuldiging";
		s.addTest(new ConverterTest(t));
		s.addTest(new ConverterTest(MPReduceConverter.getInstance(),t));
		s.addTest(new ConverterTest(StrictConverter.getInstance(),t));
		
		t = "testMacht";
		s.addTest(new ConverterTest(t));
		s.addTest(new ConverterTest(MPReduceConverter.getInstance(),t));
		s.addTest(new ConverterTest(StrictConverter.getInstance(),t));

		t = "testBasis";
		s.addTest(new ConverterTest(t));
		s.addTest(new ConverterTest(MPReduceConverter.getInstance(),t));
		s.addTest(new ConverterTest(StrictConverter.getInstance(),t));

		t = "testVergelijking";
		s.addTest(new ConverterTest(t));
		s.addTest(new ConverterTest(MPReduceConverter.getInstance(),t));
		s.addTest(new ConverterTest(StrictConverter.getInstance(),t));
		
		return s;
	}
	
}
