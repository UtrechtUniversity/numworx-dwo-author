package fi.euclides.formuleobjects;

import static org.junit.Assert.*;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMBinding;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Parser3Test {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testygtx() throws ParseException {
		FormuleParser p = new FormuleParser("y>x+2");
		OMObject o = p.parse();
		assertNotNull(o);
		assertTrue(o instanceof OMApplication);
		OMApplication oma = (OMApplication) o;
		assertTrue(oma.firstElement().isSame(new OMSymbol("relation1", "gt")));
		System.out.println(o);

	}
	@Test
	public void testxgty() throws ParseException {
		FormuleParser p = new FormuleParser("x>y+2 and x<y-4 or y > 6");
		OMObject o = p.parse();
		assertNotNull(o);
		assertTrue(o instanceof OMApplication);
		OMApplication oma = (OMApplication) o;
		assertTrue(oma.firstElement().isSame(new OMSymbol("logic1", "or")));
		System.out.println(o);
	}
	
	@Test
	public void testapprox() throws ParseException {
		FormuleParser p = new FormuleParser("a \u2248 b");
		OMObject o = p.logic();
		assertNotNull(o);
		assertTrue(o instanceof OMApplication);
		OMApplication oma = (OMApplication) o;
		assertTrue(oma.firstElement().isSame(new OMSymbol("relation1", "approx")));
		System.out.println("a \u2248 b" + " : " + o);
	}

}
