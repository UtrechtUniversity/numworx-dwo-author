package nl.numworx.geodefiner.common;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import nl.uu.fi.dwo.interaction.client.JSONUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Lijn;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.LabelDelegate;

public class MementoTest {

	private MockTracker tracker;
	private Memento memento;
	@Before
	public void setUp() throws Exception {
		tracker = new MockTracker();
		memento = new Memento(tracker);
		memento.fromList(JSONUtilities.wrapList(memento.toList())); // shortcut
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReadPunt() throws IOException {
		Punt p = tracker.getModel().buildPunt(Numbers.ONE, Numbers.ONE);
		memento.writePunt(p);
		final Punt p2 = memento.readPunt();
		assertEquals(p.getIndex(), p2.getIndex());
		assertEquals(p.key(), p2.key());
		assertEquals(p.getX(), p2.getX());
		assertEquals(p.getY(), p2.getY());
	}

	@Test
	public void testWritePunt() throws IOException {
		Punt p = tracker.getModel().buildPunt(Numbers.ONE, Numbers.TWO);
		memento.writePunt(p);
	}

	@Test
	public void testReadNumber() throws IOException {
		final double value = 123.456;
		memento.writeNumber(Numbers.createDouble(value));
		assertEquals(value, memento.readNumber().doubleValue(), 0.00001);
	}

	@Test
	public void testWriteNumber() throws IOException {
		final double value = 123.456;
		memento.writeNumber(Numbers.createDouble(value));
	}

	@Test
	public void testReadDestroyable() throws IOException {
		Cirkel c = tracker.getModel().buildCirkel();
		memento.writeDestroyable(c);
		assertEquals(c, memento.readDestroyable());
	}

	@Test
	public void testWriteDestroyable() throws IOException {
		Cirkel c = tracker.getModel().buildCirkel();
		memento.writeDestroyable(c);
	}

	@Test
	public void testWriteVector() throws IOException {
		Vector v = new Vector();
		memento.writeVector(v);
	}

	@Test
	public void testReadVector() throws IOException {
		Vector v = new Vector();
		memento.writeVector(v);
		Vector vv = memento.readVector();
		assertEquals(v, vv);
	}

	@Test
	public void testReadLijn() throws IOException {
		Lijn l = tracker.getModel().buildLijn();
		memento.writeLijn(l);
		Lijn ll = memento.readLijn();
		assertEquals(l, ll);
	}

	@Test
	public void testWriteLijn() throws IOException {
		Lijn l = tracker.getModel().buildLijn();
		memento.writeLijn(l);
	}

	@Test
	public void testWriteUTF() throws IOException {
		final String test = "test string";
		memento.writeUTF(test);
	}

	@Test
	public void testReadUTF() throws IOException {
		final String test = "test string";
		memento.writeUTF(test);
		assertEquals(test, memento.readUTF());
	}

	@Test
	public void testWriteDelegate() throws IOException {
		memento.writeDelegate(tracker.getRegistered("?"));
	}

	@Test
	public void testReadDelegate() throws IOException {
		memento.writeDelegate(tracker.getRegistered("?"));
		assertEquals(tracker.getRegistered("?"), memento.readDelegate());
	}

	@Test
	public void testGetModel() {
		assertEquals(tracker.getMapper(), memento.getModel());
	}

	@Test
	public void testWrite() throws IOException {
		Destroyable[] source = new Destroyable[2];
		memento.write(source);
	}

	@Test
	public void testRead() throws IOException {
		Destroyable[] source = new Destroyable[0];
		memento.write(source);
		Destroyable[] dest = memento.read(source);
		assertArrayEquals(source, dest);
	}

	@Test
	public void testReadModel() throws IOException {
		final Model m = tracker.getModel();
		Punt p1 = m.buildPunt(Numbers.ONE, Numbers.ZERO);
		Punt p2 = m.buildPunt(Numbers.PI, Numbers.ZERO);
		m.toggle(p2);m.toggle(p1);
		Punt p3 = m.buildMiddelPunt();
		memento.writeModel(m);
		tracker.model = new Model();
		memento.readModel(tracker);
		assertEquals(m.getPunten().size(), tracker.model.getPunten().size());
	}

	@Test
	public void testReadModel2() throws IOException {
		final Model m = tracker.getModel();
		Punt p1 = m.buildPunt(Numbers.ONE, Numbers.ZERO);
		Punt p2 = m.buildPunt(Numbers.PI, Numbers.ZERO);
		m.toggle(p2);m.toggle(p1);
		Punt p3 = m.buildMiddelPunt();
		memento.writeModel(m);
		//tracker.model = new Model();
		memento.readModel(tracker);
		assertEquals(3, tracker.model.getPunten().size());
	}

	@Test
	public void testWriteModel() throws IOException {
		final Model m = tracker.getModel();
		Punt p1 = m.buildPunt(Numbers.ONE, Numbers.ZERO);
		Punt p2 = m.buildPunt(Numbers.PI, Numbers.ZERO);
		m.toggle(p2);m.toggle(p1);
		Punt p3 = m.buildMiddelPunt();
		memento.writeModel(m);
		List l = memento.toList();
		System.out.println(l);
	}
	
	@Test
	public void testWriteModel2() throws IOException {
		final Model m = tracker.getModel();
		Punt p1 = m.buildPunt(Numbers.ONE, Numbers.ZERO);
		Punt p2 = m.buildPunt(Numbers.PI, Numbers.ZERO);
		m.toggle(p2);m.toggle(p1);
		Punt p3 = m.buildMiddelPunt();
		memento.prepare(new ArrayList<Destroyable>(m.getPunten()));
		memento.writeModel(m);
		List l = memento.toList();
		System.out.println(l);
	}
	
}
