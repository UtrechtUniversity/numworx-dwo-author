package fi.wiskopdr.expressies.repr;

import java.io.StringReader;

import org.mathpiper.mpreduce.Interpreter2;

import fi.wiskopdr.expressies.Aftrekking;
import fi.wiskopdr.expressies.BasisExpressie;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.expressies.Integraal;
import fi.wiskopdr.expressies.Limiet;
import fi.wiskopdr.expressies.Macht;
import fi.wiskopdr.expressies.Prv;
import fi.wiskopdr.formuleobjects.FormuleParser;

import junit.framework.TestCase;

public class ReduceTest extends TestCase {

	private Interpreter2 interpreter;

	protected void setUp() throws Exception {
		interpreter = MPReduce.getInstance();
	}

	public void testEvaluate() throws Throwable {
		Expressie e = FormuleParser.parse("3*(x^2-x)");
		Expressie x = FormuleParser.parse("x");
		Expressie nul = FormuleParser.parse("0");
		Expressie een = FormuleParser.parse("1");
		e = new Integraal(e, nul, een,x);
		String command = e.visit(MPReduceConverter.getInstance()).toString();
		String result = interpreter.evaluate(command);
		e = (Expressie) new MPReduceParser(new StringReader(result)).start();
		assertEquals(-0.5, e.geefWaarde(), 0.0001);
	}

	public void testEvaluate2() throws Throwable {
		Expressie e = FormuleParser.parse("cos(x)*(1-sin^2*(x))");
		Expressie x = FormuleParser.parse("x");
		Expressie nul = FormuleParser.parse("\u03c0/3");
		Expressie een = FormuleParser.parse("5*\u03c0/6");
		e = new Integraal(e, nul, een,x);
		String command = e.visit(MPReduceConverter.getInstance()).toString();
		String result = interpreter.evaluate(command);
		e = (Expressie) new MPReduceParser(new StringReader(result)).start();
		assertEquals(-0.1911857, e.geefWaarde(), 0.0001);
	}
	
	public void testLimit() throws Throwable {
		Expressie e = FormuleParser.parse("cos(x)");
		Expressie x = new BasisExpressie("x");
		Expressie inf = new BasisExpressie("\u221e");
		e = new Limiet(e, x, inf, new BasisExpressie(0)); // Ordening
		String command = e.visit(MPReduceConverter.getInstance()).toString();
		String result = interpreter.evaluate(command);
		Object r =  new MPReduceParser(new StringReader(result)).start();
		assertEquals(e.toString(), r.toString());
	}
	public void testLimit2() throws Throwable {
		Expressie e = FormuleParser.parse("cos(x)");
		Expressie x = new BasisExpressie("x");
		Expressie inf = new BasisExpressie("\u221e");
		e = new Limiet(e, x, inf, new BasisExpressie(2)); // Ordening
		String command = e.visit(MPReduceConverter.getInstance()).toString();
		String result = interpreter.evaluate(command);
		Object r =  new MPReduceParser(new StringReader(result)).start();
		assertEquals(e.toString(), r.toString());
	}

	public void testLimitPlus() throws Throwable {
		Expressie e = FormuleParser.parse("abs(x)/x");
		Expressie x = new BasisExpressie("x");
		Expressie nul = new BasisExpressie(0);
		e = new Limiet(e, x, nul, new BasisExpressie(1)); // Ordening (naar beneden)
		String cas = e.toStringStrikt(); System.out.println(cas);
		String command = e.visit(MPReduceConverter.getInstance()).toString();
		String result = interpreter.evaluate(command);
		Object r =  new MPReduceParser(new StringReader(result)).start();
		assertEquals("1", r.toString());
	}
	
	public void testPrv() throws Throwable {
		Expressie bot = new BasisExpressie(1);
		Expressie top = new BasisExpressie(2);
		Expressie x   = new BasisExpressie("x");
		Expressie f   = FormuleParser.parse("x^2");
		Expressie e = new Prv(f, bot, top, x);
		String command = e.visit(MPReduceConverter.getInstance()).toString();
		String result = interpreter.evaluate(command);
		Object r =  new MPReduceParser(new StringReader(result)).start();
		assertEquals("3", r.toString());
	}

}
