package nl.numworx.fsm.editor;

import java.awt.Dimension;
import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookWidgetEditIF;

public class Editor extends JPanel implements CBookWidgetEditIF {

	private static final long serialVersionUID = 1L;
	private final CBookContext context;
	private int maxScore;
	private int iwidth = 400;
	private int iheight = 400;

	@Inject
	public Editor(CBookContext context) {
		this.context = context;
	}

	@Override
	public JComponent asComponent() {
		return this;
	}

	@Override
	public Map<String, ?> getLaunchData() {
		return Collections.emptyMap();
	}

	@Override
	public void setLaunchData(Map<String, ?> data) {
	}

	@Override
	public int getMaxScore() {
		return maxScore;
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

	}

	@Override
	public void setInstanceHeight(int height) {
		this.iheight = height;

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
