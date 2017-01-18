package fi.euclides.event;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Punt;

public interface NameMapper {
	public Destroyable fromString(String name);
	public Punt getO();
	public Punt getU();
	public String toString(Destroyable destroyable);

	public void rename(Destroyable p, String name);
}
