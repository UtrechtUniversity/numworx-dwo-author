package nl.numworx.fsm.editor;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.LessonMode;
import org.cbook.cbookif.SuccessStatus;


import fi.euclides.event.AddLijnHandler;
import fi.euclides.event.AddPuntHandler;
import fi.euclides.event.DestroyHandler;
import fi.euclides.event.EventHandler;
import fi.euclides.event.SelectHandler;
import fi.euclides.model.Model;
import fi.euclides.util.Hashtable;
import nl.numworx.fsm.shared.AddBoogHandler;
import nl.numworx.fsm.shared.EdgeMover;
import nl.numworx.fsm.shared.Hoekpunt;
import nl.numworx.fsm.shared.Memento;
import nl.numworx.fsm.shared.MidBoogPunt;
import nl.numworx.fsm.shared.UnifiedHandler;


public class Instance extends JPanel implements CBookWidgetInstanceIF {

	public class MoveAction extends AbstractAction implements Action {

		private EventHandler handler;

		public MoveAction() {
			super("M");
			handler = new SelectHandler();
			handler.setDecorator(decorator);
			handler.setTracker(viewer);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			handler.command();
		}
	}
	public class NodeAction extends AbstractAction implements Action {

		private EventHandler handler;

		public NodeAction() {
			super("N");
			handler = new AddPuntHandler();
			handler.setDecorator(decorator);
			handler.setTracker(viewer);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			handler.command();
		}
	}
	public class EMAction extends AbstractAction implements Action {

		private EventHandler handler;

		public EMAction() {
			super("X");
			handler = new EdgeMover();
			handler.setTracker(viewer);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			handler.command();
		}
	}
	
	public class EdgeAction extends AbstractAction implements Action {

		private EventHandler handler;

		public EdgeAction() {
			super("E");
			handler = new AddLijnHandler(AddLijnHandler.SEGMENT);
			handler.setDecorator(decorator);
			handler.setTracker(viewer);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			handler.command();
		}
	}
	
	public class InnerAction extends AbstractAction implements Action {

		private EventHandler handler;

		public InnerAction() {
			super("I");
			handler = new AddBoogHandler();
			handler.setDecorator(decorator);
			handler.setTracker(viewer);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			handler.command();
		}
	}
	
	
	public class RemoveAction extends AbstractAction implements Action {

		private EventHandler handler;

		public RemoveAction() {
			super("R");
			handler = new DestroyHandler();
			handler.setDecorator(decorator);
			handler.setTracker(viewer);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			handler.command();
		}
	}

	public class TextAction extends AbstractAction implements Action {

		private EventHandler handler;

		public TextAction() {
			super("T");
			handler = new TextHandler();
			handler.setDecorator(decorator);
			handler.setTracker(viewer);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			handler.command();
		}
	}

	private static final long serialVersionUID = 1L;
	private final CBookContext context;
	final InstanceViewer viewer;
	private final Memento memento;
	private final Model model;
	
	private JToolBar tools;
	private FSMDecorator decorator = new FSMDecorator();
	public LessonMode lessonMode;
	public UnifiedHandler initHandler;

	@Inject
	public Instance(CBookContext context) {
		super(new BorderLayout());
		this.context = context;
		Hoekpunt.addCreator();
		MidBoogPunt.addCreator();
		memento = new Memento();
		model = memento.getModel();
		viewer = new InstanceViewer(model, this);
		addMouseListener(viewer);
		addMouseMotionListener(viewer);
		setOpaque(false);setBackground(null);
		tools = new JToolBar();
		add(tools, BorderLayout.NORTH);
		MoveAction m;
		tools.add(m = new MoveAction());
		NodeAction s;
		tools.add(s = new NodeAction());
		s.handler.command();
		tools.add(new EdgeAction());
		tools.add(new InnerAction());
		tools.add(new TextAction());
		tools.add(new RemoveAction());
		tools.add(new EMAction());
		
		initHandler = new UnifiedHandler("FSM");
		initHandler.setTracker(viewer);
		initHandler.setDecorator(decorator);
		viewer.setPointerHandler(initHandler);
		m.handler = initHandler; // in plaats van selectHandler
		
	}

	@Override
	public JComponent asComponent() {
		return this;
	}

	@Override
	public void setLaunchData(Map<String, ?> data, Map<String, Number> randomValues) {
		setState(data);
	}

	@Override
	public void setAssessmentMode(AssessmentMode mode) {
	}

	@Override
	public void setState(Map<String, ?> state) {
		Object data = state.get("model");
		if (data == null) {
			//model.destroyAll();
		} else {
			memento.setDataInputStream(new Input(data));
			try {
				memento.readModel(viewer);
				Object names = state.get("names");
				memento.readNames(new Input(names));
				Object accepted = state.get("accepted");
				memento.readAccepted(new Input(accepted));
			} catch (Exception e) {
				e.printStackTrace(); // should not happen!!!!
			}
		}
		viewer.paint();
	}

	@Override
	public Map<String, ?> getState() {
		Object model, names, accepted;
		this.model.clearSelection();
		Output dos = new Output();
		memento.setDataOutputStream(dos);
		Map<String, Object> result = new Hashtable<>();
		try {
			memento.writeModel(viewer);
			model = dos.getData();
			result.put("model", model);
			dos = new Output();
			memento.writeNames(dos);
			names = dos.getData();
			result.put("names", names);
			dos = new Output();
			memento.writeAccepted(dos);
			accepted = dos.getData();
			result.put("accepted", accepted);
			
		} catch (IOException e) {
			// does not occur
		}		
		return result;
	}

	@Override
	public int getScore() {
		return 0;
	}

	@Override
	public SuccessStatus getSuccessStatus() {
		return SuccessStatus.PASSED;
	}

	@Override
	public void addCBookEventListener(CBookEventListener listener, String command) {
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener, String command) {
	}

	@Override
	public void init() {
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void reset() {
	}

	
	@Override
	public CBookEventListener asEventListener() {
		return null;
	}

	@Override
	protected void paintComponent(Graphics g) {
		viewer.paint(g);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		boolean same = viewer.height == height && viewer.width == width;
		viewer.height = height;
		viewer.width = width;
		if(!same)
			viewer.notifyViewport();
	}

}
