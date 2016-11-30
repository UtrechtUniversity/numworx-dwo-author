package nl.numworx.geodefiner.common;

import nl.tue.win.riaca.openmath.lang.OMObject;
import nl.tue.win.riaca.openmath.lang.OMSymbol;
import nl.uu.fi.dwo.interaction.client.json.ObjectMap;
import fi.euclides.event.Tracker;
import fi.euclides.formuleobjects.FormuleParser;
import fi.euclides.formuleobjects.ParseException;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Label;
import fi.euclides.util.Observable;
import fi.euclides.util.Observer;

public class Check_DWO implements Observer {

	private Tracker tracker;
	private int maxScore, score;
	private boolean status;
	private boolean check;
	public Check_DWO(Tracker tracker) {
		this.tracker = tracker;
		status = true;
		maxScore = 0;
		score = 0;
		check = false;
	}

	public void fromMap(ObjectMap map) {
		check = map.getBoolean("check", false);
		if(!check) {
			maxScore = 0;
			return;
		}
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
		Destroyable target = expr.interpret(logic, new Label(), tracker.getMapper());
		if(target instanceof Label)
		{	update(target, null);
			target.addObserver(this);
		}
	}
	
	@Override
	public void update(Observable observable, Object arg) {
		if(arg == Destroyable.DESTROY)
			observable.deleteObserver(this);
		else if(arg == null) {
			Label label = (Label)observable;
			status = !check || label.isDefined() && label.getState() != Label.FALSE;
			score = status ? maxScore : 0;
		}
	}

}
