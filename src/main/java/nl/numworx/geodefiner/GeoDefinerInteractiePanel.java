package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Hashtable;

import javax.swing.JPanel;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.SuccessStatus;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class GeoDefinerInteractiePanel extends JPanel implements
		InteractiePanel, CBookContext {

	private static final long serialVersionUID = -4868744357817393056L;

	private Instance instance;

	private Hashtable launchData;
	
	GeoDefinerInteractiePanel() {
		super(new BorderLayout());
		instance = new Instance();
		add(instance.asComponent(), BorderLayout.CENTER);
	}

	public void zetOpdracht(Hashtable b, String[] randomVars,
			Hashtable randomValues) {
		instance.init();
		instance.setLaunchData(b, randomValues);
	}

	public void setState(Hashtable b) {
		instance.setState(b);
	}

	public void setEditState(Hashtable b) {
		instance.init();
		this.launchData = b;
		instance.setLaunchData(b, Collections.EMPTY_MAP);
	}

	public Hashtable getState() {
		return (Hashtable) instance.getState();
	}

	public Hashtable getEditState() {		
		return launchData;
	}

	public InteractieEditPanel getEditPanel() {
		return new GeoDefinerInteractieEditPanel();
	}

	public void wis() {
		instance.reset();
	}

	public void zetMaat() {
		instance.asComponent().doLayout();
	}

	public int getIpId() {
		return 0;
	}

	public int getScore() {
		return instance.getScore();
	}

	public int[][] getScoreObjectives() {
		return null;
	}

	public int getScoreMax() {
		return 0;
	}

	public boolean isCorrect() {
		return instance.getSuccessStatus() == SuccessStatus.PASSED;
	}

	public boolean isFout() {
		return instance.getSuccessStatus() == SuccessStatus.FAILED;
	}

	public void zetMode(int mode) {
		instance.setAssessmentMode(AssessmentMode.values()[mode]);
	}

	public void zetNagekeken(boolean b) {
		
	}

	public void stop() {
		instance.stop();
	}

	public void start() {
		instance.start();
	}

	public void destroy() {
		instance.destroy();
	}

	public void opnieuw() {
		instance.reset();
	}

	public void kijkNa() {

	}

	public void kijkNa(int stapNr) {
	}

	public void addActionListener(ActionListener al) {
	}

	public Object getProperty(String key) {
		return null;
	}

}
