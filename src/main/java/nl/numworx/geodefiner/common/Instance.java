package nl.numworx.geodefiner.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.uibinder.client.UiFactory;

import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectList;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.SelectHandler;
import fi.euclides.event.Tracker;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.MP;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.Segment;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.Const;
import fi.euclides.proof.FlipFlop;
import fi.euclides.proof.LabelDelegate;
import fi.euclides.proof.LabelTester;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;

public abstract class Instance /*implements Observer*/ {

	protected Logger logger = Logger.getLogger(getClass().getName());
	protected UIModelFactory uiModelFactory;
	protected Definitions definitions;
	protected Tracker viewer;
	protected boolean nagekeken;
	protected CheckObjectList checkObjects;
	
	//protected int width, height;
	
	protected final SelectHandler selector = new SelectHandler() {

		@Override
		public void visitLabel(Label l) {
			if (click && testLabel && l.getRegistered() instanceof FlipFlop)  { 
				flip(l);
				return;
			}
			if (click && testLabel && l.getRegistered() instanceof Interval) {
				Animator anima = l.adapt(Animator.class);
				if(anima != null) {
					anima.command();
					return;
				}
			}
			if (click && testLabel && l.getRegistered() instanceof Animator) {
				Animator anima = (Animator) l.getRegistered();
				anima.command();
				return;
			}
			if (!click && getTrack() == null && l.adapt(StepValue.class) != null) {
				setTrack(new IntervalLabelTrack(l, lastx, lasty));
				return;
			}
			super.visitLabel(l);
		}

		@Override
		protected boolean freePuntenLine(Lijn l) {			
			return super.freePuntenLine(l) && !Boolean.TRUE.equals(l.adapt(Boolean.class));
		}

		@Override
		protected boolean freeCombiLijn(Lijn l) {
			return super.freeCombiLijn(l) && !Boolean.TRUE.equals(l.adapt(Boolean.class));
		}

		@Override
		protected boolean freeCirkel(Cirkel c) {
			return super.freeCirkel(c) && !Boolean.TRUE.equals(c.adapt(Boolean.class));
		}

		@Override
		protected boolean freeMP(MP l) {
			return super.freeMP(l) && !Boolean.TRUE.equals(l.adapt(Boolean.class));
		}

		@Override
		public void visitPunt(Punt p) {
			if (!click && getTrack() == null && p.adapt(Label.class) != null) {
				setTrack(new IntervalLabelTrack(p.adapt(Label.class), lastx, lasty));
				setGravity(false);
				return;
			}
			boolean gOff = isGOff(p);
			super.visitPunt(p);
			if (gOff && getTrack() != null) { setGravity(false); }
		}
		
		boolean isGOff(Punt p ) {
			Model m = getTracker().getModel();
			return 	!click && 
					getTrack() == null &&
					freePunt(p) && 
					( m.getO() == p || m.getU() == p);
		}
		
		
		boolean gravity;
		private void saveGravity() {
			Snapper snap = getTracker().adapt(Snapper.class);
			gravity = snap.isGravity();
		}
		
		private void restoreGravity() {
			setGravity(gravity);
		}
		
		private void setGravity(boolean gravity) {
			Snapper snap = getTracker().adapt(Snapper.class);
			snap.setGravity(gravity);
		}

		/* (non-Javadoc)
		 * @see fi.euclides.event.SelectHandler#pointerPressed(fi.euclides.model.math.Numbers, fi.euclides.model.math.Numbers)
		 */
		@Override
		public void pointerPressed(Numbers x, Numbers y) {
			saveGravity();
			super.pointerPressed(x, y);
		}

		/* (non-Javadoc)
		 * @see fi.euclides.event.SelectHandler#pointerReleased(fi.euclides.model.math.Numbers, fi.euclides.model.math.Numbers)
		 */
		@Override
		public void pointerReleased(Numbers x, Numbers y) {
			super.pointerReleased(x, y);
			restoreGravity();
		}
 		
	};
	
	protected ObjectMap launchData, state;
	protected Map<String, Number> random;
	
	public void setLaunchData(Map<String, ? extends Object> launchData, Map<String, Number> random) {
		this.launchData = JSONUtilities.wrapMap(launchData);
		this.random = random;		
		installLaunchData();
	}

	protected void installLaunchData() {
		installPositions(); // of O,e 
		createDefinitions();
		installAxes();
		installConfiguration();
		installPositions(); // Prepare the rest
		installCheckDWO();
		installToolbox();
		installCheckObjects();
		installPrepare();
	}

	protected void installCheckObjects() {
		ObjectList list = launchData.getObjectList("checkObjects");
		if(list != null && list.size() > 0) {
			checkObjects = new CheckObjectList(viewer);
			checkObjects.fromList(list);
			if(checkObjects.getMaxScore() == 0) 
				checkObjects = null;
		}
		fetchScore();
	}

	List<Destroyable> resetItems;
	public void installPrepare() {
		Model m = viewer.getModel();
		int size = m.getPunten().size() + m.getLijnen().size();
		resetItems = new ArrayList<Destroyable>(size);
		resetItems.addAll(m.getPunten());
		resetItems.addAll(m.getLijnen());
		// no sort?
	}

	protected void installToolbox() {
		
	}

	protected boolean installCheckDWO() {
		if(launchData.containsKey("checkDWO"))
		{
			checkDWO = new Check_DWO(viewer);
			checkDWO.fromMap(launchData.getObjectMap("checkDWO"));
			fetchScore();
			return checkDWO.isCheck();
		} else 
			checkDWO = null;
			return false;
	}

	void flip(Label l) {
		boolean flip = l.getState() == Label.FALSE;
		l.setValue(flip ? Numbers.ZERO:Numbers.ONE);
		((FlipFlop) l.getRegistered()).test(l);
	}

	private void installPositions() {
		if(launchData.containsKey("positions"))
			setPositions(launchData.getObjectMap("positions"));
	}

	private void installConfiguration() {
		if (!this.launchData.containsKey("configuration")) return;
		ObjectMap configuration = this.launchData.getObjectMap("configuration");
		install(configuration);
	}

	private void installAxes() {
		if (!this.launchData.containsKey("axes")) return;
		ObjectMap configuration =  this.launchData.getObjectMap("axes");
		install(configuration);
	}

	protected void install(ObjectMap configuration) {
		if(configuration != null) {
			for( String name : configuration.keySet()) {
				ObjectMap value = configuration.getObjectMap(name);
				Destroyable d = viewer.getMapper().fromString(name);
				if(d == null) continue;
				UIModel<?, ?> model = uiModelFactory.build(d);
				model.fromMap(value);
				model.install();
				CELL cell = d.adapt(CELL.class);
				if(cell == null) {
					cell = new CELL("$f@",d, name);
					DefaultAdapter.getDefault(d).put(cell);
				}
				cell.config = model;
				definitions.update(cell);
			}
		}
	}
	
	private void createDefinitions() {
		if(!this.launchData.containsKey("definitions")) return;
		List<String> strings = this.launchData.getStringList("definitions");
		if(strings != null)
		for (Iterator<String> iterator = strings.iterator(); iterator.hasNext();) {
			String text = iterator.next();
			OMObject object;
			try {
				String toParse = randomize(this.random, text);
				object = new FormuleParser(toParse.substring(2)).parse();
				definitions.define(text, object);
			} catch (Throwable e) {
				logger.log(Level.WARNING, "define " + text , e);
				//break;
			}
		}
	}

	public String randomize(Map<String, Number> random, String text) {
		for(Map.Entry<String, Number> entry: random.entrySet()) {
			String key = "#" + entry.getKey() + "#";
			text = text.replaceAll(key, "(" + entry.getValue().toString() + ")");
		}
		return text;
	}

	protected void setPositions(Object object) {
		if(object instanceof ObjectMap) {
			ObjectMap positions = (ObjectMap) object;
			List<Destroyable> punten = new ArrayList<Destroyable>(viewer.getModel().getPunten());
			punten.addAll(viewer.getModel().getLijnen());
			for (Iterator<Destroyable> iterator = punten.iterator(); iterator.hasNext();) {
				Destroyable punt = iterator.next();
				String name = viewer.getMapper().toString(punt);
				FreePoint fp = punt.adapt(FreePoint.class);
				if(positions.containsKey(name) && fp != null) {
					try {
						ObjectList n = positions.getObjectList(name);
						NumberIO in = new NumberIO(n);
						Numbers x = in.readNumber();
						Numbers y = in.readNumber();
						boolean b = fp.isFree();
						try {
							fp.setFree(true); // temporary movable
							fp.moveTo(x, y);
						} finally {
							fp.setFree(b);
						}
						
					} catch (IOException e) {
						// should not happen!
					}
				}
			}
		}
	}

	protected int score;
	protected Check_DWO checkDWO;
	private Boolean status;

	public Boolean getStatus() {
		return status;
	}

	public int getScore() {
		return score;
	}

	protected Map<String, Object> getState(Map<String, Object> map) {
		Map<String, List<Object>> positions = new HashMap<String, List<Object>>();
		Map<String, List<Object>> values = new HashMap<String, List<Object>>();
		List<Destroyable> punten;
		punten = new ArrayList<Destroyable> (viewer.getModel().getPunten());
		punten.addAll(viewer.getModel().getLijnen());
		for (Iterator<Destroyable> iterator = punten.iterator(); iterator.hasNext();) {
			Destroyable next = iterator.next();
			FreePoint punt = next.adapt(FreePoint.class);
// a rigid coordinate, no saving.
			if(punt != null && !punt.isFree() && next instanceof Coordinaten)
				punt = null;
			String name = viewer.getMapper().toString(next);
			if(punt != null && !name.startsWith("%")) {
				try {
					NumberIO io = new NumberIO();
					punt.getX().writeNumber(io);
					punt.getY().writeNumber(io);
					positions.put(name, io.toList());
				} catch (IOException e) {
				}
			}
			if(next instanceof Label && !"i".equals(name)) {
				Label label = (Label) next;
				String subkey = label.getSubKey();
// Which values to save
				if (FlipFlop.TYPE == subkey|| Const.TYPE == subkey) {
				try { NumberIO io = new NumberIO();
					label.value.writeNumber(io);
					io.writeUTF(label.getString());
					values.put(name, io.toList());
				} catch (IOException e) {
				}}
			}
			
		}
		map.put("values",  values);
		map.put("positions", positions);
		if(launchData != null) {
		
		ObjectList toolbox = launchData.getObjectList("toolbox");
		if(toolbox != null && toolbox.size() > 0) {
			List modelState = getModelState();
			if(modelState != null) map.put("model", modelState);
		}
		}
		if(nagekeken) map.put("nagekeken", Boolean.TRUE);
		return map;
	}

	List getModelState() {
		if(resetItems == null) return null;
		Memento m = new Memento(viewer);
		m.prepare(resetItems);
		try {
			m.writeModel(viewer.getModel());
		} catch (IOException e) {
			// TODO should not happen
		}
		
		return m.toList();
	}
	
	void setModelState(ObjectList list, ObjectList toolbox) {
		if(list == null && (toolbox == null || toolbox.size() == 0) ) 
			return;
		Memento m = new Memento(viewer);
		m.prepare(resetItems);
		try {
			m.fromList(list);
			m.readModel(viewer);
		} catch (IOException e) {
			// TODO should not happen
		}
	}
	

	protected Model createModel(Model m, int width, int height) {
		m.destroyAll();
		int mx = width/2;
		int my = height/2;
		Punt O = m.buildPunt(Numbers.createInteger(mx), Numbers.createInteger(my));
		DefaultAdapter.getDefault(O).put("O");
		Punt U = new HorizontalPunt(Numbers.createInteger(mx+50), O.getX(), O);
		DefaultAdapter.getDefault(U).put("e");
		m.add(U);
		Vector<Destroyable> select = m.getSelect();
		select.add(U);
		select.add(O);
		Lijn xas = m.buildLijn();
		DefaultAdapter.getDefault(xas).put("x");
		select.add(O);
		select.add(xas);
		Lijn yas = m.buildLoodlijn();
		DefaultAdapter.getDefault(yas).put("y");
		
		Grid grid = new Grid(viewer);
		DefaultAdapter.getDefault(grid).put("$#@");
		UIModel<?, ?> uimodel = uiModelFactory.build(grid);
		uimodel.install();
		m.add(grid);
		
		Label i = new Label();
		DefaultAdapter.getDefault(i).put(State.INITIAL);
	    i.setValue(Numbers.createComplex(Numbers.ZERO, Numbers.ONE));
	    i.register(new Const());
	    i.setString("i");
	    i.setVisible(false);
	    DefaultAdapter.getDefault(i).put("i");
	    m.add(i);
		
		return m;
	}

	public void setState(Map<String, ?> state) {
		if(state == null) state = Collections.emptyMap();
		this.state = JSONUtilities.wrapMap(state);
		setValues(this.state.getObjectMap("values"));
		setPositions(this.state.getObjectMap("positions"));
		setModelState(this.state.getObjectList("model"), this.state.getObjectList("toolbox"));
		nagekeken = this.state.getBoolean("nagekeken", false);
		if(nagekeken) {
			viewer.getModel().executeDelay(); // essentieel.
			fetchScore();
		}
	}

	private void setValues(ObjectMap objectMap) {
		if(objectMap == null || objectMap.isEmpty()) return;
		for(Destroyable d : viewer.getModel().getLijnen()) {
			String name = viewer.getMapper().toString(d);
			if ( objectMap.containsKey(name) && d instanceof Label) {
				Label l = (Label) d;
				NumberIO io = new NumberIO(objectMap.getObjectList(name));
				try {
					Numbers v = io.readNumber();
					l.setString(io.readUTF());
					l.setValue(v);
					if(l.registered instanceof FlipFlop) 
						((FlipFlop) l.registered).test(l);
				} catch (IOException e) {
				}
				
			}
		}
		
	}

	public void update(Observable observable, Object arg) {
		if("changed".equals(arg)) {
			fetchScore();
		}
	}

	protected void fetchScore() {
		score = checkDWO.getScore();
		status = checkDWO.isStatus(); 
		if(checkObjects != null) {
			checkObjects.verify();
			score += checkObjects.getScore();
			if(Boolean.TRUE == status) {
				status = checkObjects.isStatus();
			}
		}
	}

	public int getMaxScore() {
		return checkDWO.getMaxScore() +
			(checkObjects == null ? 0 : checkObjects.getMaxScore());
	}

	public void start() {
		Model m = viewer.getModel();
		m.getO().adapt(FreePoint.class).setFree(false);
		m.getU().adapt(FreePoint.class).setFree(false);
		List<Destroyable> list = new ArrayList<Destroyable>(m.getPunten());
		list.addAll(m.getLijnen());
		for (Destroyable destroyable : list) {
			if(destroyable instanceof Label) {
				Label label = (Label) destroyable;
				Punt fp = label.getP();
				if(fp instanceof Volgpunt) {
					((Volgpunt)fp).setFree(false);
				}
				if (label.getRegistered() instanceof Interval) {
					@SuppressWarnings("unchecked")
					Segment s = ((PuntOp<Segment>) fp).getOp();
					s.getP1().adapt(FreePoint.class).setFree(false);
					s.getP2().adapt(FreePoint.class).setFree(false);
				}
				if(fp.getIndex() == 0 && fp.adapt(FreePoint.class) != null) {
					fp.adapt(FreePoint.class).setFree(false);
				}
			}
		}
		
		viewer.paint();
	}
	
	protected void reset() {
	}
	
}
