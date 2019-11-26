package nl.numworx.geodefiner.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.numworx.geodefiner.common.math.Expression;
import nl.tue.win.riaca.openmath.lang.OMApplication;
import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMVariable;
import nl.uu.fi.dwo.interaction.client.JSONUtilities;
import nl.uu.fi.dwo.interaction.client.json.ObjectList;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.HitTester;
import fi.euclides.event.NameMapper;
import fi.euclides.event.SelectHandler;
import fi.euclides.event.Tracker;
import fi.euclides.event.TrackerContext;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.model.Boog;
import fi.euclides.model.Cirkel;
import fi.euclides.model.Coordinaten;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Groep;
import fi.euclides.model.HorizontalPunt;
import fi.euclides.model.Label;
import fi.euclides.model.Lijn;
import fi.euclides.model.MP;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.PuntOp;
import fi.euclides.model.PuntenLijn;
import fi.euclides.model.Segment;
import fi.euclides.model.Track;
import fi.euclides.model.Visitor;
import fi.euclides.model.algo.FreePoint;
import fi.euclides.model.math.Numbers;
import fi.euclides.proof.Const;
import fi.euclides.proof.FlipFlop;
import fi.euclides.util.Adapter;
import fi.euclides.util.DefaultAdapter;
import fi.euclides.util.Observable;

public abstract class Instance /*implements Observer*/ {

	static {
		Volgpunt.addCreator();
		Polygon.addCreator();
	}
	public final class Selector extends SelectHandler {
      boolean gravity;
      boolean hasTools;

    /**
	 * @return the hasTools
	 */
	public boolean isHasTools() {
		return hasTools;
	}


	public void destroyContext(TrackerContext context) {
      DefaultAdapter.getDefault(context).put(InSelectContext.class, null);
    }
      
      
    protected SelectHandler.InSelectContext createContext(TrackerContext context) {
      return new InInstanceSelectContext(wrap(context));
    }

    private TrackerContext wrap(final TrackerContext context) {
      if(hasTools) return context;
      return new TrackerContext() {

        @Override
        public Adapter getAdapter() {
          return context.getAdapter();
        }

        @Override
        public void setAdapter(Adapter result) {
          context.setAdapter(result);
        }

        @Override
        public void setTrack(Track track) {
          context.setTrack(track);
        }

        @Override
        public Track getTrack() {
          return context.getTrack();
        }

        @Override
        public HitTester getHitTester() {
          return context.getHitTester();
        }

        @Override
        public void clearSelection() {
          context.clearSelection();
        }

        @Override
        public void toggle(Destroyable d) {
        }

        @Override
        public Vector<Destroyable> selection() {
          return context.selection();
        }};
    }

    class InInstanceSelectContext extends SelectHandler.InSelectContext {

      protected InInstanceSelectContext(TrackerContext context) {
        super(context);
      }

      @Override
      public void visitLabel(Label l) {
        if (click && testLabel && l.getRegistered() instanceof FlipFlop) {
          flip(l);
          return;
        }
        if (click && testLabel && l.getRegistered() instanceof Interval) {
          Animator anima = l.adapt(Animator.class);
          if (anima != null) {
            anima.command();
            return;
          }
        }
        if (click && testLabel && l.getRegistered() instanceof Animator) {
          Animator anima = (Animator) l.getRegistered();
          anima.command();
          return;
        }
        if (!click && track == null && l.adapt(StepValue.class) != null) {
          track = (new IntervalLabelTrack(l, lastx, lasty));
          return;
        }
        super.visitLabel(l);
      }

      @Override
      protected boolean freeMP(MP l) {
        return super.freeMP(l) && !Boolean.TRUE.equals(l.adapt(Boolean.class));
      }

      boolean isGOff(Punt p) {
        Model m = getTracker().getModel();
        return !click && track == null && freePunt(p) && (m.getO() == p || m.getU() == p);
      }

      @Override
      public void visitPunt(Punt p) {
        if (!click && track == null && p.adapt(Label.class) != null) {
          track = (new IntervalLabelTrack(p.adapt(Label.class), lastx, lasty));
          setGravity(false);
          return;
        }
        boolean gOff = isGOff(p);
        super.visitPunt(p);
        if (gOff && track != null) {
          setGravity(false);
        }
      }
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
    protected boolean freePuntenLine(Lijn l) {
      return anyFreePuntenLijn(l) && !Boolean.TRUE.equals(l.adapt(Boolean.class));
    }


	private boolean anyFreePuntenLijn(Lijn l) {
		if(l instanceof PuntenLijn)
		{
			Punt[] depend = (Punt[])l.getDepend();
			return freePunt(depend[0]) || freePunt(depend[1]); // any instead of all
		}
		return false;
	}

    @Override
    protected Punt[] freeBoog(Boog b) {
      if (Boolean.TRUE.equals(b.adapt(Boolean.class)))
        return null;
      return super.freeBoog(b);
    }
    
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

    /*
     * (non-Javadoc)
     * 
     * @see fi.euclides.event.SelectHandler#pointerPressed(fi.euclides.model.math.Numbers,
     * fi.euclides.model.math.Numbers)
     */
    @Override
    public void pointerPressed(Numbers x, Numbers y, TrackerContext context) {
      saveGravity();
      super.pointerPressed(x, y, context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see fi.euclides.event.SelectHandler#pointerReleased(fi.euclides.model.math.Numbers,
     * fi.euclides.model.math.Numbers)
     */
    @Override
    public void pointerReleased(Numbers x, Numbers y, TrackerContext context) {
      super.pointerReleased(x, y, context);
      restoreGravity();
    }
  }
  protected Logger logger = Logger.getLogger(getClass().getName());
	protected UIModelFactory uiModelFactory;
	protected Definitions definitions;
	protected Tracker viewer;
	private Boolean nagekeken;
	private int errorCount;
	public CheckObjectList checkObjects;
	
	
  public final Selector selector = new Selector();
	
	protected ObjectMap launchData, state;
	protected DefaultRandomizer random = new DefaultRandomizer();
	
	public void setLaunchData(Map<String, ? extends Object> launchData, Map<String, Number> random) {
		this.launchData = JSONUtilities.wrapMap(launchData);
		this.random.setRandom(random);		
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
			checkObjects.fromList(list);
			if(checkObjects.getMaxScore() == 0) 
				checkObjects .clear();
		} else {
			checkObjects.clear();
		}
		fetchScore();
	}

	protected List<Destroyable> resetItems;
	public void installPrepare() {
		Model m = viewer.getModel();
		int size = m.getPunten().size() + m.getLijnen().size();
		resetItems = new ArrayList<Destroyable>(size);
		resetItems.addAll(m.getPunten());
		resetItems.addAll(m.getLijnen());
		// no sort?
	}

	protected void installToolbox() {
      if( launchData.containsKey("toolbox")) {
        ObjectList list = launchData.getObjectList("toolbox");
        selector.hasTools = list.size() > 0;
      } else
        selector.hasTools = false;
	}

	protected boolean installCheckDWO() {
		if(launchData.containsKey("checkDWO"))
		{
			checkDWO = new Check_DWO(viewer);
			checkDWO.fromMap(launchData.getObjectMap("checkDWO"));
			checkDWO.setLogID(launchData.getString("logID"));
			checkDWO.setLogOption(launchData.getBoolean("logOption", false));
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

	protected boolean hasTrail;
	protected void install(ObjectMap configuration) {
	    hasTrail = false;
		if(configuration != null) {
			for( String name : configuration.keySet()) {
				ObjectMap value = configuration.getObjectMap(name);
				if(!hasTrail &&value.getBoolean("trail", false)) hasTrail = true;
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
				String toParse = random.randomize(text);
				object = new FormuleParser(toParse.substring(2)).parse();
				definitions.define(text, object);
			} catch (Throwable e) {
				logger.log(Level.WARNING, "define " + text , e);
				//break;
			}
		}
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
				} else if (positions.containsKey(name) && punt instanceof Groep) {
					Groep groep = (Groep) punt;
					ObjectList n = positions.getObjectList(name);
					NumberIO in = new NumberIO(n);
					Enumeration<Destroyable> e = groep.elements();
					while (e.hasMoreElements()) {
						Destroyable destroyable = e.nextElement();
						if (destroyable instanceof Punt) {
							fp  = destroyable.adapt(FreePoint.class);
							if (fp == null) continue;
							boolean b = fp.isFree();
							try {
								fp.setFree(true);
								Numbers x = in.readNumber(); Numbers y = in.readNumber();
								fp.moveTo(x, y);
							} catch (IOException e1) {
								
							} finally {
								fp.setFree(b);
							}
							
						}
						
					}
				}
			}
		}
	}

	protected int score;
	protected Check_DWO checkDWO;
	private Boolean status;
    protected Map<String,String> expressions;

	public Boolean getStatus() {
		return status;
	}

	public int getScore() {
		return score;
	}

	protected Map<String, Object> getState(Map<String, Object> map) {
      if (!expressions.isEmpty())
      {
        map.put("expressions", new ArrayList<>(expressions.values()));
        Map<String,Integer> indices = new HashMap<>();
        map.put("indices", indices);
        int r = definitions.readonly;
        NameMapper m = viewer.getMapper();
        for (Destroyable d: resetItems) {
          int index = d.getIndex();
          String name = //d.adapt(String.class);
              m.toString(d);
          if (index>r) {
            indices.put(name, index);
          }
        }
      }
	  
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
			if (next instanceof Groep) {
				Groep groep = (Groep) next;
				NumberIO io = new NumberIO();
				Enumeration<Destroyable> e = groep.elements();
				try {
					while (e.hasMoreElements()) {
						Destroyable destroyable = e.nextElement();
						FreePoint fp = destroyable.adapt(FreePoint.class);						
						if (fp != null) {
							fp.getX().writeNumber(io);
							fp.getY().writeNumber(io);
						}
					}
					positions.put(name, io.toList());
				} catch (IOException e1) {
					logger.log(Level.WARNING, "save groups position", e1);
				}
				
			}
			
		}
		map.put("values",  values);
		map.put("positions", positions);
		if(launchData != null) {
		
		ObjectList toolbox = launchData.getObjectList("toolbox");
		if(toolbox != null && toolbox.size() > 0 || hasTrail) {
			List modelState = getModelState();
			if(modelState != null) map.put("model", modelState);
		}
		}
		if (nagekeken != null) 
			map.put("nagekeken", nagekeken);
		if(errorCount > 0) map.put("errorCount", errorCount);
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
	   protected void updateResetItems(int modelcount) {
	        Model model = viewer.getModel();
	        List<Destroyable> objects = new ArrayList<>();
	        List<Punt> punten = model.getPunten();
	        List<Destroyable> lijnen = model.getLijnen();
	        objects.addAll(lijnen);
	        objects.addAll(punten);
	        resetItems.retainAll(objects); // destroy old definitions
	        for(Destroyable d: objects) {
	          if(d.getIndex() > modelcount) {
	            resetItems.add(d);         // add new definitions.
	          }
	        }
	  }

	protected void setModelState(ObjectList list, ObjectList toolbox) {
	    if(state.containsKey("expressions")) {
	      ObjectList expressions = state.getObjectList("expressions");
	      ObjectMap  indices = state.getObjectMap("indices");
	      int size = expressions.size();
	      for (int i = 0; i < size; i++) {
	        String expr = expressions.getString(i);
	        String name = expr.substring(0, expr.indexOf('='));
	        OMObject object = null;
	        try {
	          object = new fi.euclides.formuleobjects.FormuleParser(expr).parse();
	        } catch (ParseException e) { // should not happen.
	        }
	        int old = definitions.readonly;
	        definitions.readonly = Definitions.PREDEFINED_INDEX;
	        int modelcount = viewer.getModel().getIndex();
	        definitions.define("$f" + expr, object);
	        definitions.redefine(random);
	        this.expressions.remove(name);
	        this.expressions.put(name, expr);
	        updateResetItems(modelcount);
	        definitions.readonly = old;
	      }
	      Set<String> set = indices.keySet();
	      int max = viewer.getModel().getIndex();
	      for(String name : set) {
	        Destroyable d = viewer.getMapper().fromString(name);
	        int i = indices.getInt(name);
	        max = Math.max(max, i);
	        d.setIndex(i);
	      }
	      viewer.getModel().setIndex(max);
	    }
		if(list == null || (toolbox == null || toolbox.size() == 0) && !hasTrail) 
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
		viewer.getModel().executeDelay(); // essentieel.
		setModelState(this.state.getObjectList("model"), this.launchData.getObjectList("toolbox"));
		if(this.state.containsKey("nagekeken"))
			setNagekeken(this.state.getBoolean("nagekeken"));
		if(this.state.containsKey("errorCount"))
			errorCount = this.state.getInt("errorCount");
		else 
			errorCount = 0;
		if(isNagekeken()) {
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

	public void fetchScore() {
		score = checkDWO.getScore();
		status = checkDWO.isStatus(); 
		if(checkObjects.size() != 0) {
			checkObjects.verify();
			score += checkObjects.getScore();
			if(score == getMaxScore()) // if maxscore == 0, return TRUE
				status = Boolean.TRUE;
			else if (score == 0)
				status = Boolean.FALSE;
			else
				status = null;
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

	protected void observeNewItems(Visitor... observers) {
		Model model = viewer.getModel();
		Collection<Destroyable> newItems = new HashSet<Destroyable>();
		newItems.addAll(model.getLijnen());
		newItems.addAll(model.getPunten());
		newItems.removeAll(resetItems);
		for(Destroyable item: newItems) {
			for(Visitor observer: observers)
				item.visit(observer);
		}
	}

	protected boolean isNagekeken() {
		return nagekeken != null && nagekeken.booleanValue();
	}

	public void setNagekeken(boolean nagekeken) {
		if(!nagekeken && this.nagekeken == null) return; 
		this.nagekeken = nagekeken;
		viewer.adapt(Expression.class).CHECKED.setValue(isNagekeken());
	}
	
	public int getErrorCount() {
		return errorCount;
	}
	public void incErrorCount() {
		if(! Boolean.TRUE.equals(status))
			errorCount ++ ;
	}

  protected void acceptExpressionEvent(String name, String expr) {
    expr = expr.substring(2);
    String x = "x"; // var of expr
    try {
      OMObject o = new fi.euclides.formuleobjects.FormuleParser(expr).expr();
      Collection<String> vars = varsOf(o, new HashSet<String>());
      vars.remove("i"); // i is a var, but cannot be used.
      if(vars.size() == 1) 
        x = vars.iterator().next();
      else if (!vars.isEmpty())
        return;
    } catch (ParseException e1) {
      logger.log(Level.WARNING, "expression " + name, e1);
      return;
    }
    
    expr = name + "=" + x + "->" + expr;
  
    int readonly = definitions.readonly;
    try {
      OMObject object = new fi.euclides.formuleobjects.FormuleParser(expr).parse();
      definitions.readonly = Definitions.PREDEFINED_INDEX;
      int modelcount = viewer.getModel().getIndex();
      definitions.define("$f" + expr, object);
      definitions.redefine(random);
      expressions.remove(name);
      expressions.put(name, expr);
      updateResetItems(modelcount);
    } catch (Exception e) {
      logger.log(Level.SEVERE, "expression " + name, e);
    } finally {
      definitions.readonly = readonly;
    }
  }

  protected Collection<String> varsOf(OMObject o, HashSet<String> set) {
    if(o instanceof OMVariable) {
      set.add(((OMVariable) o).getName());
    }
    if (o instanceof OMApplication) {
      @SuppressWarnings("unchecked")
      List<OMObject> elements = ((OMApplication) o).getElements();
      elements = elements.subList(1, elements.size()); // not first. in case of $f($x)
      for (OMObject p: elements) varsOf(p, set);      
    }
    return set;
  }
}
