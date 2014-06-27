package fi.wiskopdr.tekstobjects;

import fi.wiskopdr.opdrnav.XWidgetManager;

public class BasisTekstVak extends TekstVak implements XWidgetManager.HasWidgetManager {
	
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

	
}
