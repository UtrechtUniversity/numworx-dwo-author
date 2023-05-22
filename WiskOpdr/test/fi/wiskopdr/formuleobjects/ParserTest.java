package fi.wiskopdr.formuleobjects;

import fi.wiskopdr.expressies.Expressie;
import junit.framework.TestCase;

public class ParserTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testParser1() throws Exception {
		String source = "$f60+4*-11$b1$n2@@@";
		String s1 = FormuleParser.formuleString(source);
		String s2 = FormuleParser.schoon(s1);
		Expressie e = FormuleParser.parse(s2);
		assertEquals(source, 14.0, e.geefWaarde(), 0.000001);
	}

	public void testParser1space() throws Exception {
		String source = "$f60+4*-11 $b1$n2@@@";
		String s1 = FormuleParser.formuleString(source);
		String s2 = FormuleParser.schoon(s1);
		Expressie e = FormuleParser.parse(s2);
		assertEquals(source, 14.0, e.geefWaarde(), 0.000001);
	}
	
	public void testParserSpaces() throws Exception {
		String source = "$f6 0 + 4 * - 1 1 $b1$n2@@@";
		String s1 = FormuleParser.formuleString(source);
		String s2 = FormuleParser.schoon(s1);
		Expressie e = FormuleParser.parse(s2);
		assertEquals(source, 14.0, e.geefWaarde(), 0.000001);
	}

	public void testParser2() throws Exception {
		String source = "$f60-11$b1$n2@@*4@";
		String s1 = FormuleParser.formuleString(source);
		String s2 = FormuleParser.schoon(s1);
		Expressie e = FormuleParser.parse(s2);
		assertEquals(source, 14.0, e.geefWaarde(), 0.000001);
	}

	public void testParser3() throws Exception {
		String source = "$f$b1$n2@@*4@";
		String s1 = FormuleParser.formuleString(source);
		String s2 = FormuleParser.schoon(s1);
		Expressie e = FormuleParser.parse(s2);
		assertEquals(source, 2.0, e.geefWaarde(), 0.000001);
	}
	public void testParser4() throws Exception {
		String source = "$f1$b1$n2@@*4@";
		String s1 = FormuleParser.formuleString(source);
		String s2 = FormuleParser.schoon(s1);
		Expressie e = FormuleParser.parse(s2);
		assertEquals(source, 6.0, e.geefWaarde(), 0.000001);
	}
}
