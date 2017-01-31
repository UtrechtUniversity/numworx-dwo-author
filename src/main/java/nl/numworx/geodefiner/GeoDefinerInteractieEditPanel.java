package nl.numworx.geodefiner;

import java.awt.BorderLayout;
import java.util.Collections;
import java.util.Hashtable;

import javax.swing.JPanel;

import org.cbook.cbookif.CBookContext;

import fi.beans.wiskopdrbeans.InteractieEditPanel;

public class GeoDefinerInteractieEditPanel extends JPanel implements
		InteractieEditPanel, CBookContext {

	private static final long serialVersionUID = 4981417988189908524L;

	Editor editor;
	
	GeoDefinerInteractieEditPanel() {
		super(null);
		editor = new Editor(this);
		editor.setLocation(0, 0);
		editor.setInstanceWidth(500);
		editor.setInstanceHeight(450);
		editor.setLaunchData(Collections.EMPTY_MAP);
		add(editor);
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		editor.setSize(width, height);
		editor.invalidate();
		editor.doLayout();
	}

	public void setEditState(Hashtable b) {
		editor.setLaunchData(b);
		boolean[][] logObjectives = (boolean[][]) b.get("logObjectives");
		if (logObjectives != null)
			editor.setChoices(logObjectives);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Hashtable getEditState() {
		Hashtable map = new Hashtable( editor.getLaunchData() );
		int scoreMax = editor.getMaxScore();
		boolean[][] logObjectives;
		logObjectives = editor.getChoices();
		map.put("scoreMax", scoreMax);
        if(logObjectives!=null)
        {	map.put("logObjectives",logObjectives);
			int[][] scoreMaxObjectives;
    		scoreMaxObjectives = new int[logObjectives.length][];
    		for(int j=0 ; j<scoreMaxObjectives.length; j++)
    		{	scoreMaxObjectives[j] = new int[logObjectives[j].length];
				for(int i=0 ; i<scoreMaxObjectives[j].length ; i++)
				{	if(logObjectives[j][i]) scoreMaxObjectives[j][i] = scoreMax;
				}
    		}
        	map.put("scoreMaxObjectives",scoreMaxObjectives);
        }
		return map;
	}

	public void zetBreedte(int b) {
		editor.setInstanceWidth(b);
	}

	public void zetHoogte(int h) {
		editor.setInstanceHeight(h);
	}

	public void stop() {
		editor.stop();
	}

	public void start() {
		editor.start();
	}

	public Object getProperty(String arg0) {
		if("randomVars".equals(arg0))
			return Collections.EMPTY_MAP; // FIXME
		return null;
	}

}
