package nl.numworx.geodefiner.common.index;

import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.Label;
import fi.euclides.model.Visitor;
import fi.euclides.model.math.Numbers;

public class ListSelector extends AbstractSelector<Groep> implements Visitor, Selector {
	
	public ListSelector(Groep grp, int index) {
		setGrp(grp);
		Label l = new Label();
		l.setValue(Numbers.createInteger(index));
		setIdx(l);
		Destroyable element;
		element = grp.prototype(index-1);
		if(element instanceof Groep) 
			indexed = new GroepIndex(this);
		else
		if (element != null)
			element.visit(this);
		else
			indexed = new GeneralDestroyable(this);
		recalc();
	}
		
	public ListSelector(Groep grp, Label index) {
		setGrp(grp);
		setIdx(index);
		Destroyable element = grp.prototype();
		if(element instanceof Groep) 
			indexed = new GroepIndex(this);
		else
		if (element != null)
			element.visit(this);
		else
			indexed = new GeneralDestroyable(this);
		recalc();
	}
	

	@SuppressWarnings("unchecked")
	void recalc() {
		if( grp != null && index != null) {
			int i = Numbers.round(index.value).intValue();
			if(i >= 1 && i <= grp.size()) {
				if(indexed.getDelegate() != null) indexed.getDelegate().deleteObserver(this);
				Destroyable delegate = grp.elementAt(i-1);
				if(delegate != null) delegate.addObserver(this);
				indexed.setDefined(delegate != null && delegate.isDefined());
				indexed.setDelegate(delegate);
				return;
			}
		} 
		indexed.setDefined(false);		
	}

}
