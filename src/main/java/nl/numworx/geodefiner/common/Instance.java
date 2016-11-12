package nl.numworx.geodefiner.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectList;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.SelectHandler;
import fi.euclides.event.Tracker;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.model.AbstractViewer;
import fi.euclides.model.Destroyable;
import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.Lijn;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.math.Numbers;
import fi.euclides.persist.Memento;
import fi.euclides.util.DefaultAdapter;

public abstract class Instance {

	protected UIModelFactory uiModelFactory;
	protected Definitions definitions;
	protected Tracker viewer;
	//protected int width, height;
	
	protected final SelectHandler selector = new SelectHandler();
	
	protected ObjectMap launchData, state;
	protected Map<String, Number> random;
	
	public void setLaunchData(Map<String, ? extends Object> launchData, Map<String, Number> random) {
		this.launchData = JSONUtilities.wrapMap(launchData);
		this.random = random;		
		createDefinitions();
		installAxes();
		installConfiguration();
		installPositions();
	}

	private void installPositions() {
		if(launchData.containsKey("positions"));
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
				UIModel<?, ?> model = uiModelFactory.build(d);
				model.fromMap(value);
				model.install();
				CELL cell = d.adapt(CELL.class);
				if(cell == null) {
					cell = new CELL("$f@",d);
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
				object = new FormuleParser(text.substring(2)).parse();
				definitions.define(text, object);
			} catch (Throwable e) {
				break;
			}
		}
	}

	public static String randomize(Map<String, Number> random, String text) {
		for(Map.Entry<String, Number> entry: random.entrySet()) {
			String key = "#" + entry.getKey() + "#";
			text = text.replaceAll(key, entry.getValue().toString());
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
						fp.setXY(x, y);
					} catch (IOException e) {
						// should not happen!
					}
				}
			}
		}
	}

	int score;
	public int getScore() {
		return score;
	}

	protected Map<String, Object> getState(Map<String, Object> map) {
		Map<String, List<Object>> positions = new HashMap<String, List<Object>>();
		List<Destroyable> punten;
		punten = new ArrayList<Destroyable> (viewer.getModel().getPunten());
		punten.addAll(viewer.getModel().getLijnen());
		for (Iterator<Destroyable> iterator = punten.iterator(); iterator.hasNext();) {
			Destroyable next = iterator.next();
			FreePoint punt = next.adapt(FreePoint.class);
			if(punt != null) {
				try {
					NumberIO io = new NumberIO();
					punt.getX().writeNumber(io);
					punt.getY().writeNumber(io);
					positions.put(viewer.getMapper().toString(next), io.toList());
				} catch (IOException e) {
				}
			}
		}
		map.put("positions", positions);
		return map;
	}

	protected Model createModel(Model m, int width, int height) {
		m.getSelect().addAll(m.getPunten());
		m.getSelect().addAll(m.getLijnen());
		m.destroy();
		int mx = width/2;
		int my = height/2;
		Punt O = m.buildPunt(Numbers.createInteger(mx), Numbers.createInteger(my));
		DefaultAdapter.getDefault(O).put("O");
		Punt U = new HorizontalPunt(Numbers.createInteger(mx+50), O.getX(), O);
		DefaultAdapter.getDefault(U).put("U");
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
		m.add(grid);
		return m;
	}

	public void setState(Map<String, ?> state) {
		if(state == null) state = Collections.emptyMap();
		this.state = JSONUtilities.wrapMap(state);
		setPositions(this.state.getObjectMap("positions"));
	}

}
