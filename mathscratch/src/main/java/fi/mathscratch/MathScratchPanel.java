package fi.mathscratch;

import java.awt.AWTEventMulticaster;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;


import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractiePanel;



public class MathScratchPanel extends JPanel implements InteractiePanel, CBookAware {

	private ActionListener listener;
	private CBookEventHandler cbookEventHandler = new CBookEventHandler(this);
	
	public MathScratchPanel(MathScratch applet) {
		setBorder(BorderFactory.createDashedBorder(Color.LIGHT_GRAY));
	}
	
	public void paintComponent(Graphics g) {
		drawBin(g);
		drawUndo(g);
	}
	
	private void drawBin(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
		
		Rectangle r = new Rectangle(getSize().width-35, 5, 26,26);
		
		GeneralPath curve = new GeneralPath();
		g.setColor(new Color(38, 115, 182));
		
		curve.moveTo(r.x, r.y+r.height/4);
		curve.lineTo(r.x+r.width, r.y+r.height/4);
		curve.moveTo(r.x+r.width/6, r.y+r.height/4);
		curve.lineTo(r.x+r.width/4, r.y+r.height);
		curve.lineTo(r.x+r.width*3/4, r.y+r.height);
		curve.lineTo(r.x+r.width*5/6, r.y+r.height/4);
		curve.moveTo(r.x+r.width/2, r.y+r.height/4);
		curve.lineTo(r.x+r.width/2, r.y+r.height);
		curve.moveTo(r.x+r.width*3/8, r.y+r.height/4);
		curve.lineTo(r.x+r.width*3/8, r.y);
		curve.lineTo(r.x+r.width*5/8, r.y);
		curve.lineTo(r.x+r.width*5/8, r.y+r.height/4);
		
		curve.moveTo(r.x, r.y+r.height/6);
		
		
		g.setStroke(new BasicStroke(3.5f));
		g.draw(curve);
	}
	
	private void drawUndo(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,RenderingHints.VALUE_STROKE_NORMALIZE);
		
		Rectangle r = new Rectangle(getSize().width-80, 5, 26,26);
		
		GeneralPath curve = new GeneralPath();
		g.setColor(new Color(38, 115, 182));
		
		g.drawArc(r.x+2, r.y+r.height/2+2, r.width-2, r.height-4, 0, 180);
		
		curve.moveTo(r.x+r.width/6, r.y+r.height);
		curve.moveTo(r.x, r.y+r.height);
		curve.lineTo(r.x, r.y+r.height*3/4);
		curve.lineTo(r.x+r.width/4, r.y+r.height);
		curve.lineTo(r.x, r.y+r.height);
		
		curve.moveTo(r.x, r.y+r.height);
		g.setStroke(new BasicStroke(4f));
		g.draw(curve);
	}
	
	public void addActionListener(ActionListener listener) {
		this.listener = AWTEventMulticaster.add(this.listener, listener);
	}
	
	public void destroy() {

	}

	public MathScratchEditPanel getEditPanel() {
		return new MathScratchEditPanel(this);
	}

	public Hashtable getEditState() {
		Hashtable editState = new Hashtable();

		return editState;
	}

	public int getIpId() {
		return 0;
	}

	public int getScore() {
		return 0;
	}

	public int getScoreMax() {
		return 0;
	}

	public int[][] getScoreObjectives() {
		return null;
	}

	public Hashtable getState() {
		Hashtable state = new Hashtable();
		return state;
	}

	public boolean isCorrect() {
		return false;
	}

	public boolean isFout() {
		return false;
	}

	public void kijkNa() {
	}

	public void kijkNa(int n) {
	}

	public void opnieuw() {
	}

	public void setEditState(Hashtable editState) {

	}

	public void setState(Hashtable state) {

	}

	public void start() {

	}

	public void stop() {

	}

	public void wis() {

	}

	public void zetMaat() {

	}

	public void zetMode(int mode) {

	}

	public void zetNagekeken(boolean nagekeken) {

	}

	public void zetOpdracht(Hashtable editState, String[] names, Hashtable random) {

	}
	
//	public void setSize(int width, int height) {
//		super.setSize(width, height);
//		setPreferredSize(new Dimension(width, height));
//	}

	@Override
	public void acceptCBookEvent(CBookEvent event) {

	}

	@Override
	public void addCBookEventListener(CBookEventListener listener, String command) {
		cbookEventHandler.addCBookEventListener(listener, command);
	}

	@Override
	public String[] getAcceptedCmds() {
		String[] commands = { "graph" };
		return commands;
	}

	@Override
	public String getLocalizedCmd(String command) {
		String localizedCmd = MathScratch.rb.getString(CBA_PREFIX + command);
		if (localizedCmd == null)
			return command;
		return localizedCmd;
	}

	@Override
	public String[] getSendCmds() {
		String[] commands = { "action.correct", "action.false", "text.feedback", "graph" };
		return commands;
	}

	@Override
	public void removeCBookEventListener(CBookEventListener listener, String command) {
		cbookEventHandler.removeCBookEventListener(listener, command);
	}
}
