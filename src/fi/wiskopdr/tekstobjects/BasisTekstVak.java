package fi.wiskopdr.tekstobjects;

import fi.wiskopdr.opdrnav.XWidgetManager;

public class BasisTekstVak extends TekstVak implements XWidgetManager.HasWidgetManager {
	
	TekstVak tekstVakMetSelectie;
	
	/**
	 * @param manager
	 */
	public BasisTekstVak(XWidgetManager manager) {
		super();
		this.manager = manager;
	}

	private final XWidgetManager manager;
	
	public BasisTekstVak() {
		manager = new XWidgetManager(this);
	}

	/**
	 * @return the manager
	 */
	public XWidgetManager getXWidgetManager() {
		return manager;
	}
	
	public void setTekstVakMetSelectie(TekstVak tv) {
		tekstVakMetSelectie = tv;
	}
	
	public void showTekstVakPopup(int x, int y) {
		if(tekstVakMetSelectie!=null)
			tekstVakMetSelectie.showPopup(x,y);
	}
	
	public void copyStoredSelection() {
		if(tekstVakMetSelectie!=null)
			tekstVakMetSelectie.copySelection();
	}
	
	public void cutStoredSelection() {
		if(tekstVakMetSelectie!=null) {
			tekstVakMetSelectie.copySelection();
			tekstVakMetSelectie.deleteSelection();
		}
	}
	
	public boolean hasStoredSelection() {
		if(tekstVakMetSelectie!=null)
			return tekstVakMetSelectie.hasSelection();
		return false;
	}

	
}
