package fi.wiskopdr.tekstobjects;

import fi.wiskopdr.opdrnav.XWidgetManager;

public class FeedbackTekstArea extends TekstArea {
	
	private BasisTekstVak basis;
	
	private FeedbackTekstArea(BasisTekstVak basis) {
		super(basis);
		this.basis = basis;
	}

	/**
	 * @deprecated use {@link #FeedbackTekstArea(XWidgetManager)}
	 */
	public FeedbackTekstArea() {
		this(new BasisTekstVak());
	}
	
	public FeedbackTekstArea(XWidgetManager manager) {
		this( new BasisTekstVak(manager));
	}
}
