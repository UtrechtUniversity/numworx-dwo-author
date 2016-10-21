package nl.numworx.geodefiner;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import fi.euclides.event.SelectHandler;
import fi.euclides.model.Destroyable;
import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.Lijn;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.math.Numbers;
import fi.euclides.swing.AWTViewer;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.SuccessStatus;


class Instance extends JPanel implements CBookWidgetInstanceIF, CBookEventListener {

	private final class InstanceViewer extends AWTViewer implements Observer {
		@Override
		public void paint() {
			repaint();
		}

		@Override
		public void setModel(Model model) { // Never null!
			Model old = getModel();
			old.deleteObserver(this);
			super.setModel(model);
			model.addObserver(this);
		}

		public void update(Observable observable, Object arg) {
			if(observable == getModel())
				paint();
		}

		@Override
		public void setStatus(String string) {
			// aanwijzingen...
		}
		
	}

	private CBookEventHandler handler = new CBookEventHandler(this);
	
	private final AWTViewer viewer = new InstanceViewer();
	private final SelectHandler selector = new SelectHandler();

	@Override
	protected void paintComponent(Graphics g) {
		viewer.paint(g);
	}

	public AWTViewer getViewer() {
		return viewer;
	}

	private Map<String, Number> launchData;

	private Map<String, ?> state;
	
	public Instance() {
		setBackground(Color.white);
		setBorder(BorderFactory.createEtchedBorder());
		selector.setTracker(viewer);
		addMouseListener(viewer);
		addMouseMotionListener(viewer);
	}

	public void addCBookEventListener(CBookEventListener listener, String command) {
		handler.addCBookEventListener(listener, command);
	}

	public JComponent asComponent() {
		return this;
	}

	public CBookEventListener asEventListener() {
		return this;
	}

	public void destroy() {
		getViewer().getModel().destroy();
	}

	public int getScore() {
		return 0;
	}

	public Map<String, ?> getState() {
		return new Hashtable<String,Object>();
	}

	public SuccessStatus getSuccessStatus() {
		return SuccessStatus.PASSED;
	}

	// Assume getSize() is okay.
	public void init() {
		Model model = createModel();
		viewer.setModel(model);
		viewer.height = getHeight();
		viewer.width = getWidth();
		selector.command();
	}

	private Model createModel() {
		Model m = new Model();
		int mx = getWidth()/2;
		int my = getHeight()/2;
		Punt O = m.buildPunt(Numbers.createInteger(mx), Numbers.createInteger(my));
		DefaultAdapter.getDefault(O).put("O");
		Punt U = new HorizontalPunt(Numbers.createInteger(mx+50), O.getX(), O);
		DefaultAdapter.getDefault(O).put("U");
		m.add(U);
		Vector<Destroyable> select = m.getSelect();
		select.add(U);
		select.add(O);
		Lijn xas = m.buildLijn();
		DefaultAdapter.getDefault(xas).put("x-as");
		select.add(O);
		select.add(xas);
		Lijn yas = m.buildLoodlijn();
		DefaultAdapter.getDefault(yas).put("y-as");
		return m;
	}

	public void removeCBookEventListener(CBookEventListener listener, String command) {
		handler.removeCBookEventListener(listener, command);
	}

	public void reset() {
		// TODO Auto-generated method stub

	}

	public void setAssessmentMode(AssessmentMode mode) {
		// TODO Auto-generated method stub

	}

	public void setLaunchData(Map<String, ?> arg0, Map<String, Number> launchData) {
		this.launchData = launchData;
	}

	public void setState(Map<String, ?> state) {
		this.state = state;
	}

	public void start() {
		// TODO Auto-generated method stub

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public void acceptCBookEvent(CBookEvent ev) {
		// TODO Auto-generated method stub
		
	}

}
