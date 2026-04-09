package nl.numworx.fsm.editor;

import java.awt.Component;
import java.util.Vector;

import fi.beans.numworxlf.JOptionPane;
import fi.beans.numworxlf.JTextField;
import fi.euclides.event.EventHandler;
import fi.euclides.event.NameMapper;
import fi.euclides.event.TrackerContext;
import fi.euclides.model.Destroyable;
import fi.euclides.model.Model;
import fi.euclides.model.Track;
import fi.euclides.model.math.Numbers;

public class TextHandler extends EventHandler {

	public TextHandler() {
		super("Label");
		testLijn = true;
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
		Model m = getModel();
		Vector<Destroyable> v = m.getSelect();
		if (v.size() == 1) {
			Destroyable d = v.firstElement();
			NameMapper mapper = getTracker().getMapper();
			String name = mapper.toString(d);
			JTextField message = new JTextField(name);
			Component parent = getTracker().adapt(Component.class);
			int r = JOptionPane.showConfirmDialog(parent, message, string, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
			if (r == JOptionPane.OK_OPTION) {
				name = message.getText();
				mapper.rename(d, name);
				getTracker().getModel().clearSelection();
			}
		}
	}

}
