package nl.numworx.geodefiner.common.math;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FaculteitTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		for(int i = 30 ; i < 200; i ++) {
			double d = Faculteit.gamma(i+1);
			double w = i, v = i-1; while(v > 1) { w *= v; v-=1; }
			double err = Math.abs(d - w) / w;
			System.out.println(i + " : " + d + " ~ " + w + " +- " + err );
		}
	}

}
