package nl.numworx.geodefiner.ui;

import java.awt.BorderLayout;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.numworx.geodefiner.CellItem;
import nl.numworx.geodefiner.common.CELL;
import fi.euclides.model.Destroyable;
import fi.euclides.swing.AWTViewer;

public class Axes extends JPanel implements ChangeListener {

	CellItem o,u,x,y,g;
	Box content;
	AWTViewer viewer;
	public Axes(AWTViewer viewer) {
		super(new BorderLayout());
		Box h = Box.createVerticalBox();
		content = h;
		this.viewer = viewer;
	//	init(); when?
		
		add(h, BorderLayout.CENTER);

	}

	CellItem initItem(Destroyable d, String text, String var) {
		CELL O = d.adapt(CELL.class);
		CELL O1 = new CELL(text, d, var);
		if(O != null) O1.config = O.config;
		return new CellItem(O1, viewer, false);
	}
	
	public void init() {
		Box h = content;
		h.removeAll();
		o = initItem(viewer.getModel().getO(), "$fO = point(0,0)@", "O");
		h.add(o);
		u = initItem(viewer.getModel().getU(), "$fe = point(0,1)@", "e");
		h.add(u);
		x = initItem(viewer.getModel().getLijnen().firstElement(), "$fy=0@", "x");
		h.add(x);
		y = initItem(viewer.getModel().getLijnen().elementAt(1), "$fx=0@", "y");
		h.add(y);
		g  = initItem(viewer.getModel().getLijnen().elementAt(2), "$fgrid@", "");
		h.add(g);
		
		h.add(Box.createGlue());
	}

	public void stateChanged(ChangeEvent e) {
		if(true) // FIXME
			init();
	}
	
	private void putMap(Map<String, Object> map, CELL cell) {
		if(cell.config != null)
			map.put(viewer.toString(cell.item), cell.config.toMap());
	}
	
	public Map<String,Object> toMap() {
		TreeMap<String,Object> map = new TreeMap<String, Object>();
		if(o == null) return map;
		putMap(map, o.getCell());
		putMap(map, u.getCell());
		putMap(map, x.getCell());
		putMap(map, y.getCell());
		putMap(map, g.getCell());
		return map;
	}
	
}
