package fi.wiskopdr.cbook;

import java.awt.Dimension;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.cbook.cbookif.AssessmentMode;
import org.cbook.cbookif.CBookContext;
import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.CBookWidgetEditIF;
import org.cbook.cbookif.CBookWidgetInstanceIF;
import org.cbook.cbookif.SuccessStatus;

class ErrorWidget extends JPanel implements CBookWrap , CBookWidgetInstanceIF, CBookWidgetEditIF, CBookEventListener{
	private String className;
	private Throwable message; 
	private Dimension instanceSize = new Dimension(100,100);
	ErrorWidget(String className, Throwable e) {
		super();
		this.className = className;
		this.message = e;
	}

	@Override
	public String toString() {
		return "Exception for " + className + ":" + message;
	}

	private Map<String, ?> data;
	private Map<String, ?> state;
	
	@Override
	public CBookWidgetInstanceIF getInstance(CBookContext context) {
		return this;
	}

	@Override
	public CBookWidgetEditIF getEditor(CBookContext context) {
		return this;
	}

	@Override
	public Icon getIcon() {
		return null;
	}

	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public JComponent asComponent() {
		return this;
	}

	@Override
	public void setLaunchData(Map<String, ?> data,
			Map<String, Number> randomValues) {
		this.data = data;
	}

	@Override
	public void setAssessmentMode(AssessmentMode mode) {
	}

	@Override
	public void setState(Map<String, ?> state) {
		this.state = state;
	}

	@Override
	public Map<String, ?> getState() {
		return state;
	}

	@Override
	public int getScore() {
		return 0;
	}

	@Override
	public SuccessStatus getSuccessStatus() {
		return SuccessStatus.UNKNOWN;
	}

	@Override
	public void addCBookEventListener(CBookEventListener listener,
			String command) {
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener,
			String command) {
	}

	@Override
	public void init() {
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void reset() {
	}

	@Override
	public CBookEventListener asEventListener() {
		return this;
	}

	@Override
	public Map<String, ?> getLaunchData() {
		return data;
	}

	@Override
	public void setLaunchData(Map<String, ?> data) {
		this.data = data;
	}

	@Override
	public int getMaxScore() {
		return 0;
	}

	@Override
	public void setInstanceWidth(int width) {	
		instanceSize.width = width;
	}

	@Override
	public void setInstanceHeight(int height) {
		instanceSize.height = height;
	}

	@Override
	public Dimension getInstanceSize() {
		return new Dimension(instanceSize);
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
	public void acceptCBookEvent(CBookEvent event) {
	}

	public String getLocalizedCmd(String command) {
		return command;
	}
	
}