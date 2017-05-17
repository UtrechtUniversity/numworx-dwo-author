package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Label;

interface Indexed {
	Destroyable asDestroyable();
	void setGrp(Groep grp);
	void setIdx(Label idx);
	void recalc();
}
