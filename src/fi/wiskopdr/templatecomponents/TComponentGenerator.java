package fi.wiskopdr.templatecomponents;

import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.wiskopdr.tekstobjects.TekstVak;

public interface TComponentGenerator {

	public void generateComponent(TekstVak tekstVak);
	
	public TekstInteractiePanelVak generateAndReturnComponent(TekstVak tekstVak);
	
	public void edit(TekstInteractiePanelVak tipv);
	
	public void editMenu(TekstInteractiePanelVak tipv);
	
	public void closeEditMenu();
}
