package nl.numworx.geodefiner;

import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.SuccessStatus;

import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.euclides.event.NameMapper;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.Model;
import fi.euclides.proof.LabelValue;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class GeoDefinerInteractiePanel extends JPanel implements
		InteractiePanel, CBookContext, CBookAware {

	private static final long serialVersionUID = -4868744357817393056L;

	private  final CBookEvent CHECK = new CBookEvent(this, Constants.CHECK);

	private Instance instance;

	private class CBookActionListener implements CBookEventListener {

		private ActionListener al;
		private ActionEvent event = new ActionEvent(GeoDefinerInteractiePanel.this, ActionEvent.ACTION_PERFORMED, "changed");
		@Override
		public void acceptCBookEvent(CBookEvent arg0) {
			if(al != null) {
				al.actionPerformed(event);
			}
		}
	}
	
	private Hashtable launchData;
	
	GeoDefinerInteractiePanel() {
		super(new BorderLayout());
		instance = new Instance();
		add(instance.asComponent(), BorderLayout.CENTER);
		all = new CBookActionListener();
		instance.addCBookEventListener(all, Constants.CHECKED); // ons kent ons
	}

	public void zetOpdracht(Hashtable b, String[] randomVars,
			Hashtable randomValues) {
		launchData = b;
		doLayout(); // Assume size is valid.
		instance.init();
		Map randomvars = launchRandomVars();
		randomvars.putAll(randomValues);
		instance.setLaunchData(b, randomvars);
	}

	public void setState(Hashtable b) {
		instance.setState(b);
	}

	public void setEditState(Hashtable b) {
		instance.init();
		this.launchData = b;
		Map randomvars = launchRandomVars();
		instance.setLaunchData(b, randomvars);
	}

	private Map launchRandomVars() {
		// TODO zonder panel.
				RandomPanel randompanel = new RandomPanel();
				randompanel.setText((String)launchData.get("random"));
				Map randomvars = randompanel.getRandomVars();
		return randomvars;
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
		return instance.getMaxScore();
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
		instance.acceptCBookEvent(CHECK);
	}

	public void kijkNa(int stapNr) {
		kijkNa();
	}

	private CBookActionListener all;

	public synchronized void addActionListener(ActionListener al) {
		all.al = AWTEventMulticaster.add(al, all.al);
	}

	public Object getProperty(String key) {
		return null;
	}

	public void acceptCBookEvent(CBookEvent ev) {
		instance.acceptCBookEvent(ev);
	}

	public void addCBookEventListener(CBookEventListener listener, String command) {
		instance.addCBookEventListener(listener, command);
	}

	public String[] getAcceptedCmds() {
		List<String> cmds;
		cmds = new ArrayList<String>();
		AbstractViewer v = instance.getViewer();
		Model m = v.getModel();
		NameMapper mapper = v.getMapper();
		for(Destroyable item : m.getLijnen()) {
			if(item instanceof Label) {
				Label label = (Label) item;
				boolean isValue = label.getRegistered() instanceof LabelValue;
				String naam = mapper.toString(label);
				if(isValue) 
				{
					cmds.add("double." + naam);
				}
			}
		}
		return cmds.toArray(new String[cmds.size()]);
	}

	public String getLocalizedCmd(String cmd) {
		return cmd;
	}

	public String[] getSendCmds() {
		return getAcceptedCmds();
	}

	public void removeCBookEventListener(CBookEventListener arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

}
