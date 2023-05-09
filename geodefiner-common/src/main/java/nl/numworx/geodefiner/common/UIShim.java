package nl.numworx.geodefiner.common;

import java.util.Map;

import fi.euclides.event.Tracker;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Kegelsnede2;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.Locus;
import fi.euclides.model.Punt;
import fi.euclides.model.Segment;
import fi.euclides.model.Triangle;
import fi.euclides.model.Visitor;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

public class  UIShim<D extends Destroyable, T> implements UIModel<D, T>, Visitor {
	final UIModel<D, T> delegate;
	final private Map<String, Map<String, Object>> state;
	final private Tracker tracker;
	
	public UIShim(UIModel<D, T> delegate, Map<String, Map<String, Object>> state, Tracker tracker) {
		this.delegate = delegate;
		this.state = state;
		this.tracker = tracker;
	}


	public UIShim(UIModel<D, T> delegate, Map<String, Map<String, Object>> state, Tracker tracker,
			UIShim<D,T> point) {
		this(delegate, state, tracker);
		setChain(point);
	}

	public boolean set;
	protected UIShim<D, T> chain;

	public UIModel<D, T> init(D item) {
		return delegate.init(item);
	}

	public UIModel<D, T> init2(Destroyable d) {
		return delegate.init2(d);
	}

	public void install() {
	}

	public Map<String, Object> toMap() {
		if (set)
			return delegate.toMap();
		return null;
	}

	public void fromMap(ObjectMap value) {
		delegate.fromMap(value);
		set = true;
	}

	public T editor() {
		return delegate.editor();
	}

	public void setVisible(boolean visible) {
		delegate.setVisible(visible);
	}

	public UIModel<D, T> set(Tracker tracker) {
		return delegate.set(tracker);
	}

	public void installLight() {
	}

	public void install(Destroyable buildPunt) {
		
		if(set)
		{
			delegate.install(buildPunt);
			String name = tracker.getMapper().toString(buildPunt);
			state.put(name, delegate.toMap());
		}
	}

	@Override
	public void visitPunt(Punt p) {
		try {
			if(chain != null) p.visit(chain);
			else install(p);
		} catch(Exception oops) {}
		
	}

	@Override
	public void visitLijn(Lijn l) {
		try {
			install(l);
		} catch(Exception oops) {}
	}

	@Override
	public void visitCirkel(Cirkel c) {
		try {
			install(c);
		} catch(Exception oops) {}
	}

	@Override
	public void visitSegment(Segment s) {
		try {
			install(s);
		} catch(Exception oops) {}
	}

	@Override
	public void visitLabel(Label label) {
		try {
			install(label);
		} catch(Exception oops) {}
	}

	@Override
	public void visitTriangle(Triangle t) {
		try {
			install(t);
		} catch(Exception oops) {}
	}

	@Override
	public void visitKegelsnede(Kegelsnede2 k) {
		try {
			install(k);
		} catch(Exception oops) {}
	}

	@Override
	public void visitLocus(Locus l) {
		try {
			install(l);
		} catch(Exception oops) {}
	}

	@Override
	public void visitBoog(Boog b) {
		try {
			install(b);
		} catch(Exception oops) {}
	}

	public void setChain(UIShim<D, T> chain) {
		this.chain = chain;
	}
	
}