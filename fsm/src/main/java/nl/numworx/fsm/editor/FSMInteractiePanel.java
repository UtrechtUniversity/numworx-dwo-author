package nl.numworx.fsm.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.Constants;
import org.cbook.cbookif.LessonMode;
import org.cbook.cbookif.SuccessStatus;

import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.euclides.util.DefaultAdapter;

public class FSMInteractiePanel extends JPanel implements InteractiePanel, CBookContext, CBookAware {

	private  final CBookEvent CHECK = new CBookEvent(this, Constants.CHECK);
	private Instance instance;
	private Hashtable launchdata;
	ResourceBundle rb;
	private int scoreMax;

	public FSMInteractiePanel(ResourceBundle rb) {
		super(new BorderLayout());
		this.rb = rb;
		
		instance = new Instance(this);
		add(instance, BorderLayout.CENTER);
	}

	@Override
	public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues) {
		setLaunchData(b, randomValues);
	}

	private void setLaunchData(Hashtable b, Map randomValues) {
		this.launchdata = b;
		instance.setLaunchData(b, randomValues);
		scoreMax = ((Number) ((Map)b).getOrDefault("scoreMax", 0)).intValue();
	}
		
	@Override
	public void setState(Hashtable b) {
		instance.setState(b);
	}

	@Override
	public void setEditState(Hashtable b) {
		setLaunchData(b, Collections.emptyMap());
	}

	@Override
	public Hashtable getState() {		
		return new Hashtable(instance.getState());
	}

	@Override
	public Hashtable getEditState() {
		return launchdata;
	}

	@Override
	public InteractieEditPanel getEditPanel() {
		return new FSMInterActieEditPanel(this);
	}

	@Override
	public void wis() {
		instance.reset();
	}

	@Override
	public void zetMaat() {
		instance.asComponent().doLayout();
	}

	@Override
	public int getIpId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[][] getScoreObjectives() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getScoreMax() {
		return scoreMax;
	}

	@Override
	public boolean isCorrect() {
		return instance.getSuccessStatus() == SuccessStatus.PASSED;
	}

	@Override
	public boolean isFout() {
		return instance.getSuccessStatus() == SuccessStatus.FAILED;
	}

	@Override
	public void zetMode(int mode) {
		instance.setAssessmentMode(AssessmentMode.values()[mode]);
	}

	@Override
	public void zetNagekeken(boolean b) {
        CBookEvent ev = new CBookEvent(this, Constants.CHECK, Collections.singletonMap(Constants.CHECKED, Boolean.valueOf(b)));
        //instance.acceptCBookEvent(ev);

	}

	@Override
	public void stop() {
		instance.stop();
	}

	@Override
	public void start() {
		instance.start();

	}

	@Override
	public void destroy() {
		instance.destroy();

	}

	@Override
	public void opnieuw() {
		instance.reset();

	}

	@Override
	public void kijkNa() {
		//instance.acceptCBookEvent(CHECK);
	}

	@Override
	public void kijkNa(int stapNr) {
		kijkNa();
	}

	@Override
	public void addActionListener(ActionListener al) {
	}

	@Override
	public Object getProperty(String key) {
		if (context == null)
			return null;
		return context.getProperty(key);
	}
	
	private CBookContext context = null;

	@Override
	public void setCBookContext(CBookContext context) {
	    this.context = context;
	    Object o = context.getProperty("lessonMode");
	    if (o instanceof LessonMode) {
	      instance.lessonMode = (LessonMode) o;
	    }
	    DefaultAdapter.getDefault(instance.viewer).put(CBookContext.class, context);
	}

	@Override
	public void acceptCBookEvent(CBookEvent event) {
		
		
	}

	@Override
	public void addCBookEventListener(CBookEventListener listener, String command) {
		instance.addCBookEventListener(listener, command);
		
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener, String command) {
		instance.removeCBookEventListener(listener, command);
	}

	@Override
	public String[] getSendCmds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAcceptedCmds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalizedCmd(String cmd) {
		// TODO Auto-generated method stub
		return null;
	}

}
