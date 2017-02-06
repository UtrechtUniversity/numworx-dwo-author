package nl.numworx.geodefiner.common.math;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AantalSignTest {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		AantalSign s = new AantalSign();
		
		assertEquals(2, s.eval(11));
		assertEquals(2, s.eval(-11));
		assertEquals(2, s.eval(110000));
		assertEquals(2, s.eval(11E20));
		assertEquals(2, s.eval(11E-20));
		assertEquals(1, s.eval(0));
		assertEquals(2, s.eval(1.1));
		assertEquals(2, s.eval(0.11));
		assertEquals(2, s.eval(0.11));
	}

}
