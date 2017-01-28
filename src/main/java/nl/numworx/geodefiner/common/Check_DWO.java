package nl.numworx.geodefiner.common;

import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.Tracker;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.model.math.Numbers;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class Check_DWO extends Observable implements Observer {

	private Tracker tracker;
	private int maxScore, score;
	private boolean status;
	private boolean check, extern;
	
	
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
		Expression expr = new Expression(tracker);
		OMObject logic;
		try {
			logic = new FormuleParser(formule.substring(2)).logic();
		} catch (ParseException e) {
			logic = new OMSymbol("logic1", "true");
		}
		Label input = new Label();
		input.setVisible(false);
		input.setString(formule.substring(2));
		Destroyable target;
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
		else if(arg == null||Label.STATE.equals(arg)) {
			Label label = (Label)observable;
			boolean oldStatus = status;
			status = !check || label.isDefined() && label.getState() != Label.FALSE;
			score = status ? maxScore : 0;
			if(status != oldStatus) {
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


}
