package nl.numworx.geodefiner.common;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.Lambda;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.model.Locus;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.math.Numbers;
import fi.euclides.openmath.Expression;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.util.DefaultAdapter;
import nl.numworx.geodefiner.common.locus.Builder;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;

public class LocusTest {

	private MockTracker tracker;
	private Memento memento;
	private Definitions def;
	private Expression expression;
	@Before
	public void setUp() throws Exception {
		tracker = new MockTracker();
		LabelDelegate.setAllTracker(tracker);
		memento = new Memento(tracker);
		memento.fromList(JSONUtilities.wrapList(memento.toList())); // shortcut
		Locus.BUILDER = new Builder();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testlocus() throws IOException {
		Model m = tracker.getModel();
		Punt p1 = m.buildPunt(Numbers.ZERO, Numbers.TWO);
		Punt p2 = m.buildPunt(Numbers.createInteger(100), Numbers.TWO);
		m.toggle(p1);m.toggle(p2);
		Segment s = m.buildSegment();
		m.toggle(s);
		Punt po = m.buildPunt(Numbers.createInteger(30),Numbers.TWO);
		Punt o = m.buildPunt(Numbers.ZERO, Numbers.ZERO);
		m.toggle(o);m.toggle(po);
		Punt pm = m.buildMiddelPunt();
		m.toggle(po);m.toggle(pm);
		Locus l = m.buildLocus();
		memento.writeModel(m);
		tracker.model = new Model();
		memento.readModel(tracker);
		assertEquals(2,tracker.model.getLijnen().size());
	}
	@Test
	public void testy() throws IOException, ParseException {
		expression = new Expression(tracker);
		Lambda lambda = new Lambda();
		lambda.setTracker(tracker);
		DefaultAdapter.getDefault(tracker).put(expression);
		def = new Definitions(tracker);
		Model m = tracker.getModel();
		Punt p1 = m.buildPunt(Numbers.ZERO, Numbers.ZERO);
		Punt p2 = m.buildPunt(Numbers.TWO, Numbers.ZERO);
		def.define("$fy=x+1@", new FormuleParser("y=x+1").parse());
		memento.writeModel(m);
		tracker.model = new Model();
		memento.readModel(tracker);
		assertEquals(2,tracker.model.getLijnen().size());
	}
	@Test
	public void testfx() throws IOException, ParseException {
		expression = new Expression(tracker);
		DefaultAdapter.getDefault(tracker).put(expression);
		def = new Definitions(tracker);
		Model m = tracker.getModel();
		Punt p1 = m.buildPunt(Numbers.ZERO, Numbers.ZERO);
		Punt p2 = m.buildPunt(Numbers.TWO, Numbers.ZERO);
		def.define("$ff(a)=a+1@", new FormuleParser("f(a)=a+1").parse());
		memento.writeModel(m);
		tracker.model = new Model();
		memento.readModel(tracker);
		assertEquals(2,tracker.model.getLijnen().size());
	}
	@Test
	public void testx() throws IOException, ParseException {
		expression = new Expression(tracker);
		Lambda lambda = new Lambda();
		lambda.setTracker(tracker);
		DefaultAdapter.getDefault(tracker).put(expression);
		def = new Definitions(tracker);
		Model m = tracker.getModel();
		Punt p1 = m.buildPunt(Numbers.ZERO, Numbers.ZERO);
		Punt p2 = m.buildPunt(Numbers.TWO, Numbers.ZERO);
		def.define("$fx=y+1@", new FormuleParser("x=y+1").parse());
		memento.writeModel(m);
		tracker.model = new Model();
		memento.readModel(tracker);
		assertEquals(3,tracker.model.getLijnen().size());
	}
}
