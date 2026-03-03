package fi.wiskopdr;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JPanel;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventListener;

import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class ScoreWidget extends JPanel implements InteractiePanel, CBookAware {

  private String paginaTitel = "";
  
  public void paintComponent(Graphics g) {
    g.setFont(new Font("SansSerif",Font.PLAIN, 14));
    g.drawString(paginaTitel, 2, 14);
    int x = getWidth() - 14;
    g.setColor(new Color(0,200,0));
    g.fillOval(x,2,14,14);
    g.setColor(new Color(200,200,200));
    g.drawOval(x,2,14,14);
  }

  @Override
  public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) {
    if(h.containsKey("paginaTitel"))
      paginaTitel = (String)h.get("paginaTitel");
    
  }

  @Override
  public void setState(Hashtable b) {
    
  }

  @Override
  public void setEditState(Hashtable h) {
    if(h.containsKey("paginaTitel"))
        paginaTitel = (String)h.get("paginaTitel");
    
  }

  @Override
  public Hashtable getState() {
    Hashtable h = new Hashtable();
    return h;
  }

  @Override
  public Hashtable getEditState() {
    Hashtable h = new Hashtable();
    return h;
  }

  @Override
  public InteractieEditPanel getEditPanel() {
    return new ScoreWidgetEditPanel();
  }

  @Override
  public void setBounds(int x, int y, int b, int h) {
    super.setBounds(x, y, b, h);
    
  }

  @Override
  public void wis() {
    
  }

  @Override
  public void zetMaat() {
    
  }

  @Override
  public int getIpId() {
    return 0;
  }

  @Override
  public int getScore() {
    return 0;
  }

  @Override
  public int[][] getScoreObjectives() {
    return null;
  }

  @Override
  public int getScoreMax() {
    return 0;
  }

  @Override
  public boolean isCorrect() {
    return false;
  }

  @Override
  public boolean isFout() {
    return false;
  }

  @Override
  public void zetMode(int mode) {
    
  }

  @Override
  public void zetNagekeken(boolean b) {
    
  }

  @Override
  public void stop() {
    
  }

  @Override
  public void start() {
    
  }

  @Override
  public void destroy() {
    
  }

  @Override
  public void opnieuw() {
    
  }

  @Override
  public void kijkNa() {
    
  }

  @Override
  public void kijkNa(int stapNr) {
    
  }

  @Override
  public void addActionListener(ActionListener al) {
    
  }

  @Override
  public void acceptCBookEvent(CBookEvent event) {
  }

  @Override
  public void addCBookEventListener(CBookEventListener listener, String command) {
  }

  @Override
  public void removeCBookEventListener(CBookEventListener listener, String command) {
  }

  static final private String[] ACCEPTED = { }, SEND = { "action.passed", "action.failed" };
  @Override
  public String[] getSendCmds() {
    return SEND;
  }

  @Override
  public String[] getAcceptedCmds() {
    return ACCEPTED;
  }

  @Override
  public String getLocalizedCmd(String cmd) {
    return WiskOpdr.rb.getString(CBA_PREFIX + cmd);
  }

}
