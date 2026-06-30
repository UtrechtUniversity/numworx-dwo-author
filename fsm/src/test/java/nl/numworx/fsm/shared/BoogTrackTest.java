package nl.numworx.fsm.shared;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fi.euclides.model.Boog;
import fi.euclides.model.Punt;
import fi.euclides.model.VrijPunt;
import fi.euclides.model.math.Numbers;

public class BoogTrackTest {

	private static final Numbers N100 = Numbers.createInteger(100);
	private static final Numbers N0 = Numbers.ZERO;
	Boog tb;
	BoogTrack t;
	Punt mid;
	@Before
	public void setUp() throws Exception {
		Punt start = new VrijPunt(0,0);
		Punt end   = new VrijPunt(N100, N0);
		mid   = new VrijPunt(50,50);
		tb = new Boog(start, mid, end);
		t = new BoogTrack(mid.getX(), mid.getY(), tb);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSetXY() {
		t.setXY(N100, N100);
		assertEquals(50, mid.getXd(), 0.1);
		assertEquals(100, mid.getYd(), 0.1);
		
	}

	@Test
	public void testCalc() {
		t.calc();
		assertEquals(50, mid.getXd(), 0.1);
		assertEquals(50, mid.getYd(), 0.1);
	}

	@Test
	public void testSchuin() {
		Punt end = Boog.endOf(tb);
		end.moveTo(N100, N100);
		t.setXY(N0, N100);
		assertEquals(0, mid.getXd(), 0.1);
		assertEquals(100, mid.getYd(), 0.1);
		
	}
	@Test
	public void testSchuin2() {
		Punt end = Boog.endOf(tb);
		end.moveTo(N100, N100);
		t.setXY(N0, N0);
		assertEquals(50, mid.getXd(), 0.1);
		assertEquals(50, mid.getYd(), 0.1);
		
	}
	@Test
	public void testSchuin3() {
		Punt end = Boog.endOf(tb);
		end.moveTo(N100, N100);
		t.setXY(N0, Numbers.createInteger(25));
				
		assertEquals(37.5, mid.getXd(), 0.1);
		assertEquals(62.5, mid.getYd(), 0.1);
		
	}

}
