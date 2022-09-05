package fi.wiskopdr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class ScoreWidget extends JPanel implements InteractiePanel {

  public void paintComponent(Graphics g) {
    
    g.setColor(new Color(0,200,0));
    g.fillOval(3,3,14,14);
    g.setColor(new Color(200,200,200));
    g.drawOval(3,3,14,14);
  }

  @Override
  public void zetOpdracht(Hashtable b, String[] randomVars, Hashtable randomValues) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setState(Hashtable b) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setEditState(Hashtable h) {
    // TODO Auto-generated method stub
    
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
    // TODO Auto-generated method stub
    return new ScoreWidgetEditPanel();
  }

  @Override
  public void setBounds(int x, int y, int b, int h) {
    super.setBounds(x, y, b, h);
    
  }

  @Override
  public void wis() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void zetMaat() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public int getIpId() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getScore() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int[][] getScoreObjectives() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getScoreMax() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean isCorrect() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isFout() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void zetMode(int mode) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void zetNagekeken(boolean b) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void stop() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void start() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void destroy() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void opnieuw() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void kijkNa() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void kijkNa(int stapNr) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void addActionListener(ActionListener al) {
    // TODO Auto-generated method stub
    
  }

}
