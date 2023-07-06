package fi.euclides.formuleobjects;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;

public class TokenTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParse0() throws ParseException {
		FormuleParser p = new FormuleParser("a=i+x");
		List<Token> tokens = p.tokens();
		assertEquals("size tokens", 5, tokens.size());
		System.out.println(tokens);
		renameTokens(tokens, Collections.singletonMap("a", "b"));
		assertEquals("rename", "b=i+x", toString(tokens));		
	}

	   @Test
	    public void testParse5() throws ParseException {
	       String source;
	        FormuleParser p = new FormuleParser(source="a= i + x ");
	        List<Token> tokens = p.tokens();
	        assertEquals("size tokens", 5, tokens.size());
	        System.out.println(tokens);
	        tokens = p.insertSpecials(tokens);
	        assertEquals("size + specials" , 9, tokens.size()) ;
	        System.out.println(tokens);
	        assertEquals("toString", source, toString(tokens));
	    }

	
	
	@Test
	public void testParseP1() throws ParseException {
		FormuleParser p = new FormuleParser("P1=P11");
		List<Token> tokens = p.tokens();
		System.out.println(tokens);
		renameTokens(tokens, Collections.singletonMap("P1", "P2"));
		assertEquals("rename", "P2=P11", toString(tokens));		
	}

	
	
	private void renameTokens(List<Token> tokens, Map<String, String> map) {
		tokens.forEach(t -> renameToken(t, map));		
	}

	private void renameToken(Token t, Map<String, String> map) {
		if (t.kind == FormuleParserConstants.VARIABLE)
			t.image = map.getOrDefault(t.image, t.image);
		else if (t.kind == FormuleParserConstants.STRING)
			t.image = renameString(t.image, map);
		return;
	}

	private String renameString(String string, Map<String, String> map) {
		String plain = string;
		if(plain.contains("{") && plain.contains("}"))
		{
			plain = FormuleParser.unescape(plain);
			plain = plain.replace("{", "\",").replace("}",",\"");
			FormuleParser p = new FormuleParser("[\""+plain+"\"]");
			try {
				Token head = p.getToken(1);
				OMApplication oma = p.bracket();
				Token tail = p.getToken(1);
				List<Token> result = new ArrayList<>();
				do { 
					result.add(head);
					head = head.next;
				} while( head != tail );
				renameTokens(result, map);
				StringBuilder sb = new StringBuilder();
				for(Token t: result.subList(1, result.size()-1)) {
					int kind = t.kind;
					if (kind == FormuleParserConstants.STRING) 
					{
						t.image = FormuleParser.unescape(t.image);
						t.image = t.image.replace("\"", "\\\"");
						t.image = "}" + t.image + "{";
					}
					sb.append(t.toString());			
				}
				plain = sb.toString();
				plain = plain.replace("{,",  "{").replace(",}","}");
				if(plain.startsWith("}")) plain = plain.substring(1);
				if(plain.endsWith("{")) plain = plain.substring(0, plain.length()-1);
				return plain + '"';
				
			} catch (ParseException e) {
				// log.fine(e.toString())
				;
			} catch (TokenMgrError tme) {							
			}
		}
		return string;
	}

	@Test
	public void testParse1() throws ParseException {
		FormuleParser p = new FormuleParser("f(a)=a");
		List<Token> tokens = p.tokens();
		assertEquals("size tokens", 6, tokens.size());
		System.out.println(tokens);	
	}
	@Test
	public void testParse2() throws ParseException {
		FormuleParser p = new FormuleParser("t=text(\"{1}{2}3*{a+point(1,3)}\",P)");
		List<Token> tokens = p.tokens();
		assertEquals("size tokens", 8, tokens.size());
		System.out.println(tokens);
		Token t4 = tokens.get(4);
		assertEquals("string" , FormuleParserConstants.STRING, t4.kind);
		renameTokens(tokens, Collections.singletonMap("a", "aa"));
		assertEquals("rename string","t=text(\"{1}{2}3*{aa+point(1,3)}\",P)" , toString(tokens));
	}

	@Test // D I Y parsing:
	public void testParse3() throws ParseException {
		FormuleParser p = new FormuleParser("a-1+x");
		Token head = p.getToken(1);
		p.expr();
		Token tail = p.getToken(1);
		assertEquals("eof", FormuleParserConstants.EOF, tail.kind);
		List<Token> result = new ArrayList<>();
		do { 
			result.add(head);
			head = head.next;
		} while( head != tail );

		assertEquals("size tokens", 5, result.size());
		System.out.println(result);
		
	}

	@Test
	public void testParse4() throws ParseException {
		String source = "a= \"1 \\\"{b} \"+\r x";
		FormuleParser p = new FormuleParser(source);
		List<Token> tokens = p.tokens();
		assertEquals("size tokens", 5, tokens.size());
		System.out.println(tokens);
		tokens = p.insertSpecials(tokens);
		assertEquals("size + specials" , 8, tokens.size()) ;
		System.out.println(tokens);
		assertEquals("toString", source, toString(tokens));
		renameTokens(tokens, Collections.singletonMap("b", "B"));
		String s = toString(tokens);
		assertEquals("rename", source, toString(tokens));
	}

	@Test
	public void testParseVectorNotatie() throws Exception {
		String source = "$zP@=point($zAB@,1)";
		FormuleParser p = new FormuleParser(source);
		OMObject o = p.assign();
		System.out.println(o);
		OMApplication oma = (OMApplication) o;
		OMSymbol symbol = (OMSymbol) oma.firstElement();
		assertEquals("assignment", symbol.getName());
		
	}
	
	
	public String toString(List<Token> tokens) {
		StringBuilder sb = new StringBuilder();
		for(Token t: tokens) {
			int kind = t.kind;
			if (kind == FormuleParserConstants.STRING) 
				sb.append('"');
			sb.append(t.toString());			
		}
		return sb.toString();
	}
	
	@Test
	public void testRandom() throws ParseException {
		String source= "#a#21";
		FormuleParser p = new FormuleParser(source);
		OMObject n = p.element();
		assertEquals( "OMI", n.getType());
		p = new FormuleParser(source);
		List<Token> tokens = p.tokens_expr();
		assertEquals(2, tokens.size());
		assertEquals(FormuleParserConstants.RANDOM, tokens.get(0).kind);
		p = new FormuleParser("\u00A9oops\u00A9");
		n = p.element();
		assertEquals("OME", n.getType());
		
	}
}
