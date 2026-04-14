package nl.numworx.fsm.editor;

import java.awt.Dimension;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;

import fi.beans.numworxlf.JCheckBox;
import fi.beans.numworxlf.JFormattedTextField;
import fi.beans.numworxlf.JLabel;
import fi.beans.numworxlf.JScrollPane;

public class Editor extends JPanel implements CBookWidgetEditIF {

	private static final long serialVersionUID = 1L;
	private final CBookContext context;
	private JFormattedTextField maxScoreField;
	private int iwidth = 400;
	private int iheight = 400;
	private Instance sample;
	

	@Inject
	public Editor(CBookContext context, ResourceBundle rb) {
		this.context = context;
		maxScoreField = new JFormattedTextField(Integer.valueOf(0));
		maxScoreField.setColumns(4);

		Box line = Box.createHorizontalBox();
		
		line.add(new JLabel(rb.getString("maxScore")));
		line.add(maxScoreField);
		
		sample = new Instance(context);
		resizeSample();
		
		add(line);
		add(new JScrollPane(sample));
		
	}

	@Override
	public JComponent asComponent() {
		return this;
	}

	@Override
	public Map<String, ?> getLaunchData() {
		Map<String, Object> launchData = new HashMap<>();
		int maxScore = getMaxScore();
		launchData.put("scoreMax", maxScore);
		if (maxScore > 0) {
			launchData.put("checkDocent", true);
		}
		launchData.putAll(sample.getState());
		return launchData;
	}

	@Override
	public void setLaunchData(Map<String, ? extends Object> data) {
		Number maxScore = (Number) ((Map)data).getOrDefault("scoreMax", 0);
		maxScoreField.setValue(maxScore.intValue());
		sample.setState(data);
	}

	@Override
	public int getMaxScore() {
		try {
			maxScoreField.commitEdit();
		} catch (ParseException e) {
		}
		return ((Number) maxScoreField.getValue()).intValue();
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public void setInstanceWidth(int width) {
		this.iwidth = width;
		resizeSample();
	}

	@Override
	public void setInstanceHeight(int height) {
		this.iheight = height;
		resizeSample();
	}

	private void resizeSample() {
		sample.setSize(iwidth, iheight);
		sample.setPreferredSize(sample.getSize());
		sample.invalidate();
	}

	@Override
	public Dimension getInstanceSize() {
		return new Dimension(iwidth, iheight);
	}

	@Override
	public String[] getAcceptedCmds() {
		return null;
	}

	@Override
	public String[] getSendCmds() {
		return null;
	}

	@Override
	public String getLocalizedCmd(String command) {
		return command;
	}

}
