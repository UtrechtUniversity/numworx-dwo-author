package nl.numworx.fsm.shared;

import java.util.Vector;

import fi.euclides.event.EventHandler;
import fi.euclides.event.TrackerContext;
import fi.euclides.model.Boog;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Dpunt;
import fi.euclides.model.Model;
import fi.euclides.model.Punt;
import fi.euclides.model.Track;
import fi.euclides.model.math.Numbers;

public class AddBoogHandler extends EventHandler {

	public AddBoogHandler() {
		super("");
		testPunt = true;
	}

	@Override
	public void pointerPressed(Numbers x, Numbers y, TrackerContext context) {
		Track track=new Track(x, y);
		context.setTrack(track);
		pointerDragged(x,y,context);
	}

	@Override
	public void pointerReleased(Numbers x, Numbers y, TrackerContext context) {
		pointerDragged(x,y,context);
		context.setTrack(null);
		Model model = getModel();
		Vector<Destroyable> select = model.getSelect();
		Punt p1;
		if(select.size()==1 && select.firstElement() instanceof Punt)
			p1 = (Punt) select.elementAt(0);
		else if (select.isEmpty()) {
			p1 = visit(model.buildPunt(x, y));			
			return; // jammer dan
		} else 
			return; // nopppes
		model.clearSelection();
		x = p1.getX();
		y = p1.getY();
		Numbers vz = Numbers.createInteger(75);
		Numbers vz2 = Numbers.div(vz, Numbers.TWO);
		
		
		
		
		Numbers vz3 = Numbers.mul(vz, Numbers.createRational(5, 6));
		Punt start, middle, end;
		middle = new Dpunt(Numbers.add(x, vz3), Numbers.sub(y, vz3), p1);
		Numbers vz4 = Numbers.createDouble(Math.sqrt(2)*0.22);
		start = new Hoekpunt(p1, middle, vz4, vz4.neg(vz4));
		end = new Hoekpunt(p1, middle, vz4, vz4);
		
		//model.add(middle);
		//model.add(start);
		
		model.add(new Boog(start, middle, end));
	}

}
