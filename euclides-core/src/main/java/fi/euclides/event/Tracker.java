package fi.euclides.event;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Model;
import fi.euclides.model.Track;
import fi.euclides.proof.LabelDelegate;

public interface Tracker {

	void setTrack(Track track);

	void setPointerHandler(EventHandler eventHandler);

	void setStatus(String string);

	Model getModel();

	void paint();

	boolean contains(double x, double y);
	
	String describe(Destroyable d);
	NameMapper getMapper();

	void register(String key, LabelDelegate delegate);
	LabelDelegate getRegistered(String key);
	
	HitTester getHitTester();
	
	<T> T adapt(Class<T> cls); // extension point
}
