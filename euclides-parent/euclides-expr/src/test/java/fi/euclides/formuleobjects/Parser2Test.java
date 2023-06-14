package fi.euclides.formuleobjects;

import java.util.Hashtable;
import java.util.Locale;

import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.expressies.Expressie;
import nl.tue.win.riaca.openmath.lang.OMBinding;
import nl.tue.win.riaca.openmath.lang.OMFloat;
import nl.tue.win.riaca.openmath.lang.OMObject;
import junit.framework.TestCase;

public class Parser2Test extends TestCase {
	public void testLambda() throws Exception {
		FormuleParser p = new FormuleParser("x->x+2");
		OMObject o = p.expr();
		assertNotNull(o);
		assertTrue(o instanceof OMBinding);
		System.out.println(o);
	}
	
	public void testDivMul() throws Exception {
		FormuleParser p = new FormuleParser("x/y*x");
		OMObject o = p.expr();
		Expressie e = fi.wiskopdr.formuleobjects.FormuleParser.parse("x/y*x");
		// compare e met o
		System.out.println(e);
		System.out.println(o);	
	}
	
	public void testRandom() throws Exception {
		WiskOpdr.language = new Locale("en"); // "nl" breaks
		String tekst = "$f#rnd(a/2_1)#@";
		Hashtable randomValues = new Hashtable();
		randomValues.put("a", 1);
		String[] randomVars = { "a" };
// out is locale dependend!!!!!
		String out = fi.wiskopdr.formuleobjects.FormuleParser.randomizeTekstVakString(tekst, randomVars, randomValues);
 		FormuleParser p = new FormuleParser(out.substring(2));
 		OMObject o = p.element();
 		assertTrue(o instanceof OMFloat);
 		System.out.println(o);
	}
}
