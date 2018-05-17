package nl.numworx.geodefiner.common;

import fi.euclides.event.EventHandler;
import fi.euclides.event.HitTester;
import fi.euclides.event.NameMapper;
import fi.euclides.event.Tracker;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Model;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.Const;
import fi.euclides.proof.FlipFlop;
import fi.euclides.util.Adaptee;
import fi.euclides.util.Adapter;
import fi.euclides.util.DefaultAdapter;

public class MockTracker  extends AbstractViewer implements Tracker, Adaptee {
	Model model = new Model();
	
	
	{
		new Const().setTracker(this);
		new FlipFlop().setTracker(this);
	}

	@Override
	public void setPointerHandler(EventHandler eventHandler) {
	}

	@Override
	public void setStatus(String string) {
	}

	@Override
	public Model getModel() {
		return model;
	}

	@Override
	public void paint() {
	}

	@Override
	public boolean contains(double x, double y) {
		return false;
	}

	@Override
	public String describe(Destroyable d) {
		return d.toString();
	}

	@Override
	public NameMapper getMapper() {
		return model;
	}

	@Override
	public HitTester getHitTester() {
		return null;
	}

	DefaultAdapter t = new DefaultAdapter();
	{
		t.put(AbstractViewer.class, this);
	}
	@Override
	public <T> T adapt(Class<T> cls) {
		return t.adapt(cls);
	}

	@Override
	public Adapter getAdapter() {
		return t;
	}

	@Override
	public void setAdapter(Adapter result) {
		
	}

	@Override
	public Numbers clipTop() {
		return Numbers.ZERO;
	}

	@Override
	public Numbers clipBottom() {
		return Numbers.createInteger(1000);
	}

	@Override
	public Numbers clipLeft() {
		return Numbers.ZERO;
	}

	@Override
	public Numbers clipRight() {
		return Numbers.createInteger(1000);
	}

	@Override
	protected void drawLine(double x1, double y1, double x2, double y2) {
		
	}

	@Override
	protected void fillCircle(double x, double y, double w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawCircle(double x, double y, double w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setColor(int magenta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawPoint(double x, double y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawString(String string, double x, double y) {
		// TODO Auto-generated method stub
		
	}

}