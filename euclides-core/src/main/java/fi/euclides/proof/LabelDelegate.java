package fi.euclides.proof;


import fi.euclides.util.Arrays;
import fi.euclides.util.DefaultAdapter;

import java.io.IOException;
import java.util.Enumeration;

import fi.euclides.util.Hashtable;
import fi.euclides.event.EventHandler;
import fi.euclides.event.Tracker;
import fi.euclides.model.Codec;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.util.Observer;

public abstract class LabelDelegate extends EventHandler implements Observer {

	@Override
	public void setTracker(Tracker tracker) {
		tracker.register(getSubKey(), this);
		super.setTracker(tracker);
	}

//	private static Hashtable map = new Hashtable();

	public LabelDelegate(String string) {
		super(string);
	}

	public abstract Destroyable[] createDepend();

	/* (non-Javadoc)
	 * @see fi.euclides.model.LabelDelegate#hashCode(fi.euclides.model.Label)
	 */
	public int hashCode(Label label) {
		return label.getSubKey().hashCode() /*^ Arrays.hashCode(label.getDepend())*/;
	}

	public boolean equals(Label label, Label other) {
		if(label.getSubKey() != other.getSubKey() || label.getIndex() != other.getIndex())
			return false;
		Destroyable[] ld = label.getDepend();
		Destroyable[] od = other.getDepend();
		return Arrays.equals(ld, od);
	}
	
	/**
	 * FIXME singleton antipattern
	 * @param t
	 */
	public static void setAllTracker(Tracker t) {
		LabelDelegate[] standards = {
				new PointOnObject(),
				new DrieOpEenRij(),
				new MidpointTester(),
				new VierOpEenCirkel(),
				new LijnLijnTest(" \u2225 ", true),
				new LijnLijnTest(" \u22A5 ", false),
				new Equidistant(),
				new Const(),
				new FlipFlop(),
		};
		for (int i = 0; i < standards.length; i++) {
			LabelDelegate delegate = standards[i];
			delegate.setTracker(t);
			t.register(delegate.getSubKey(), delegate);
		}
	}

	public void prepareDepend(Codec codec, Label label) throws IOException {
	}

	public Destroyable[] createDepend(Codec codec, Label label) throws IOException {
		return createDepend();
	}

	public Destroyable[] createDepend(int args)
	{
		return createDepend();
	}
	
	
	public String getSubKey() { return string; } 	
	public boolean define(Label l)
	{
		update(l, null);
		return true;
	}

	public Label define(Destroyable[] depend) {
		Label result = new Label();
		result.register(this);
		result.setDepend(depend);
		define(result);
		return result;
	}
	
}
