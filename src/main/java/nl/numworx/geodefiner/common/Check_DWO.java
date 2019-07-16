package nl.numworx.geodefiner.common;

import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;

import java.util.List;

import fi.euclides.event.Tracker;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.openmath.Expression;
import fi.euclides.openmath.LocusModelF;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class Check_DWO extends Observable implements Observer {

	private Tracker tracker;
	private int maxScore, score;
	private boolean status;
	private boolean check, extern, logOption;
	private Destroyable target;
	private String logID;
	
	
	public boolean isCheck() {
		return check;
	}

	public Check_DWO(Tracker tracker) {
		this.tracker = tracker;
		status = true;
		maxScore = 0;
		score = 0;
		check = false;
	}

	public void fromMap(ObjectMap map) {
		if(map == null) {	
			check = false;
			maxScore = 0;
			return;
		}
		check = map.getBoolean("check", false);
		if(!check) {
			maxScore = 0;
			return;
		}
		extern = map.getBoolean("extern", false);
		String formule = map.getString("formule");
		maxScore = map.getInt("score");
		score = 0;
		Expression expr = tracker.adapt(Expression.class);
		OMObject logic;
		try {
			logic = new FormuleParser(formule.substring(2)).logic();
		} catch (ParseException e) {
			logic = new OMSymbol("logic1", "true");
		}
		
		List<Destroyable> vars = LocusModelF.varsOf(logic, tracker.getMapper());
		for(Observable v: vars) {
			v.addObserver(this);
		}
		
		Label input = new Label();
		input.setVisible(false);
		input.setString(formule.substring(2));
		try {
			target = expr.interpret(logic, input, tracker.getMapper());
		} catch (Exception e) {
			e.printStackTrace();
			target = null;
		}
		if(target instanceof Label)
		{	update(target, null);
			target.addObserver(this);
		}
	}
	
	@Override
	public void update(Observable observable, Object arg) {
		if(arg == Destroyable.DESTROY)
			observable.deleteObserver(this);
		else if (observable != target && check)
		{	
			setChanged();
			notifyObservers("inbetween");
		} else 	
			if(arg == null||Label.STATE.equals(arg)) {
			Label label = (Label)observable;
			boolean oldStatus = status;
			status = !check || label.isDefined() && label.getState() != Label.FALSE;
			score = status ? maxScore : 0;
			if(status != oldStatus)
			{
				setChanged();
				notifyObservers("changed");
			}
		}
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public boolean isExtern() {
		return extern;
	}

	public void setExtern(boolean extern) {
		this.extern = extern;
	}

  /**
   * @return the logOption
   */
  public boolean isLogOption() {
    return logOption;
  }

  /**
   * @param logOption the logOption to set
   */
  public void setLogOption(boolean logOption) {
    this.logOption = logOption;
  }

  /**
   * @return the logID
   */
  public String getLogID() {
    return logID;
  }

  /**
   * @param logID the logID to set
   */
  public void setLogID(String logID) {
    this.logID = logID;
  }


}
