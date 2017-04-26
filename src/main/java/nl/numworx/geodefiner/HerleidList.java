package nl.numworx.geodefiner;

import java.io.IOException;

import fi.euclides.event.Tracker;
import fi.euclides.expr.List;
import fi.euclides.model.Label;
import fi.euclides.util.Observable;
import fi.wiskopdr.expressies.*;

public class HerleidList extends List {

	public HerleidList() {
	}

	public HerleidList(Tracker viewer) {
		setTracker(viewer);
	}

	@Override
	public void update(Observable observable, Object arg) {
		super.update(observable, arg);
	}

	@Override
	protected StringBuilder createBuffer() {
		return new StringBuilder("$f");
	}

	protected void setString(Label l, StringBuilder sb) {
		sb.append("@");
		String s1 = sb.toString();
		Expressie e1 = fi.wiskopdr.FormuleParser.geefExpressie(s1);
		if(e1!=null)
		{	
			e1 = Algebra.herleidMild(e1, true);
			sb.setLength(0);
			sb.append(e1);
		} else {
			sb.delete(0, 2);
			sb.setLength(sb.length()-1);
		}
		super.setString(l, sb);
	}

}
