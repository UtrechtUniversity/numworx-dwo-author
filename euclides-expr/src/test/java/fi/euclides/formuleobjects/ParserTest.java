package fi.euclides.formuleobjects;

import fi.wiskopdr.expressies.BasisExpressie;
import fi.wiskopdr.expressies.Bin;
import fi.wiskopdr.expressies.Expressie;
import fi.wiskopdr.expressies.NdeLog;
import fi.wiskopdr.formuleobjects.AbsVak;
import fi.wiskopdr.formuleobjects.AftrekVak;
import fi.wiskopdr.formuleobjects.BinVak;
import fi.wiskopdr.formuleobjects.BreukVak;
import fi.wiskopdr.formuleobjects.ConjugVak;
import fi.wiskopdr.formuleobjects.FormuleElement;
import fi.wiskopdr.formuleobjects.FormuleVak;
import fi.wiskopdr.formuleobjects.HaakjesVak;
import fi.wiskopdr.formuleobjects.IntegraalVak;
import fi.wiskopdr.formuleobjects.NdeLogVak;
import fi.wiskopdr.formuleobjects.NdeWortelVak;
import fi.wiskopdr.formuleobjects.PowerVak;
import fi.wiskopdr.formuleobjects.PrimitieveVak;
import fi.wiskopdr.formuleobjects.RegelVak;
import fi.wiskopdr.formuleobjects.SigmaVak;
import fi.wiskopdr.formuleobjects.WortelVak;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMInteger;
import nl.tue.win.riaca.openmath.lang.OMObject;
import junit.framework.TestCase;

public class ParserTest extends TestCase {

	FormuleVak vak;
	
	protected void setUp() throws Exception {
		vak = new FormuleVak();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testParser() throws ParseException {
		StringStream in = new StringStream ("1+(2+3)*4");
		FormuleParser p = new FormuleParser(in);
		OMObject result = p.expr();
		assertNotNull(result);
		System.out.println(result);
	}
	
	// Aftrekvak, binvak, breukvak, conjugvak,diff(partial)vak 
	// integraalvak,limietvak,machtvak,ndelogvak,ndewortelvak,optelvak,powervak
	// prvvak,sigmavak,stelselvak,subscriptvak,vergelijkingvak,wortelvak
	
	public void testSingleVak() throws Exception {
		test1(new WortelVak(vak));
		test1(new ConjugVak(vak));
		test1(new HaakjesVak(vak));
		test1(new AbsVak(vak));
	}
	public void testDuoVak() throws Exception {
		test2(new AftrekVak(vak));
		test2(new BinVak(vak));
		test2(new BreukVak(vak));
		test2(new PowerVak(vak));
		test2(new NdeWortelVak(vak));
		test2(new NdeLogVak(vak));
	}
	
	public void testIntegraalVak() throws Exception {
		RegelVak fe = (new PrimitieveVak(vak));
		fe.vulVak("1$nx@");parse(fe);
		fe = (new IntegraalVak(vak));
		fe.vulVak("1$n2$k3$lx@@@");parse(fe);
	}
	
	public void testNdeLog() throws Exception {
		Expressie e2 = new BasisExpressie(2);
		Expressie e1 = new BasisExpressie(1);
		NdeLog ndelog = new NdeLog(e1, e2);
		double waarde = ndelog.geefWaarde();
		System.out.println(ndelog + " = " + waarde);
		assertEquals(0, waarde, 0.0001);
		FormuleParser p = new FormuleParser(ndelog.toString());
		OMObject result = p.sum();
		OMInteger twee = (OMInteger) ((OMApplication) result).getElementAt(1);
		assertEquals("2", twee.getInteger());
	}
	public void testBin() throws Exception {
		Expressie e2 = new BasisExpressie(4);
		Expressie e1 = new BasisExpressie(5);
		Bin bin = new Bin(e1, e2);
		double waarde = bin.geefWaarde();
		System.out.println(bin + " = " + waarde);
		assertEquals(5, waarde, 0.0001);
		FormuleParser p = new FormuleParser(bin.toString());
		OMObject result = p.sum();
		System.out.println(result);
		OMInteger twee = (OMInteger) ((OMApplication) result).getElementAt(1);
		assertEquals("5", twee.getInteger());
	}
	
	
	public void testMacht() throws Exception {
		
		String s = "-3$m-2@";
		StringStream in = new StringStream (s);
		FormuleParser p = new FormuleParser(in);
		OMObject result = p.expr();
		assertNotNull(result);
		System.out.println(result);
		Expressie e = fi.wiskopdr.formuleobjects.FormuleParser.geefExpressie("$f" + s +"@");
		System.out.println(e.getClass());
// FIXME NIET MEER vanwege -3^2^2 uitgezet
//		s = "-3^-2";
//		in = new StringStream (s);
//		p = new FormuleParser(in);
//	    OMObject result2 = p.expr();
//		assertNotNull(result2);
//		assertEquals(s, result.toString(), result2.toString());
//		e = fi.wiskopdr.formuleobjects.FormuleParser.geefExpressie("$f" + s +"@");
//		System.out.println(e);

	}
	
	
	private void test2(RegelVak fe) throws Exception {
		fe.vulVak("1$n2@");parse(fe);
	}

	private void test1(RegelVak fe) throws Exception {
		fe.vulVak("1"); parse(fe);
	}

	private void parse(FormuleElement elem) throws Exception {
		String string = "a=" + elem.toString();
		StringStream in = new StringStream (string);
		FormuleParser p = new FormuleParser(in);
		OMObject result = p.parse();
		assertNotNull(string, result);
		System.out.println(result);
	}
	
	public void testBreuk() throws Exception {
		StringStream in = new StringStream ("1$b3$n4@@");
		FormuleParser p = new FormuleParser(in);
		OMObject result = p.expr();
		assertNotNull(result);
		System.out.println(result);
	}
	
	public void testFun() throws Exception {
		StringStream in = new StringStream ("f(x)=x+1");
		FormuleParser p = new FormuleParser(in);
		OMObject result = p.parse();
		assertNotNull(result);
		System.out.println(result);	
	}
	
	public void testInvisibleTimes() throws Exception {
		StringStream in = new StringStream ("q=5*f(x,y, 1.2 )*1.2+f\u2061a");
		FormuleParser p = new FormuleParser(in);
		OMObject result = p.parse();
		assertNotNull(result);
		System.out.println(result);	
	}
	public void testString() throws Exception {
		StringStream in = new StringStream ("q=test(\"xxx\",P)");
		FormuleParser p = new FormuleParser(in);
		OMObject result = p.parse();
		assertNotNull(result);
		System.out.println(result);			
	}
	
	public void testInvisible() throws ParseException {
		FormuleParser p = new FormuleParser("xy");
		OMObject result = p.sum();
		assertNotNull(result);
		System.out.println(result);			
		 p = new FormuleParser("5a");
		 result = p.sum();
		assertNotNull(result);
		assertEquals("OMA", result.getType());
		System.out.println(result);
		p = new FormuleParser("5e");
		result = p.sum();
		assertNotNull(result);
		assertEquals("OMA", result.getType());
		System.out.println(result);			
		 p = new FormuleParser("5\u03c0");
		 result = p.sum();
		assertNotNull(result);
		System.out.println(result);
		assertEquals("OMA", result.getType());
		 p = new FormuleParser("x$h3+2@");
		 result = p.sum();
		assertNotNull(result);
		System.out.println(result);
		assertEquals("OMA", result.getType());
	}
	
	public void testSigmaVak() throws Exception { 
		RegelVak fe = (new SigmaVak(vak));
		fe.vulVak("1$ni$ka$lb@@@");parse(fe);
	}
	
	public void testFac() throws Exception {
		FormuleParser p = new FormuleParser("5!");
		OMObject result = p.sum();
		assertNotNull(result);
		System.out.println(result);
		assertEquals("OMA", result.getType());
		
	}
}
