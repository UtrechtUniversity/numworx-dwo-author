package fi.mathscratchgwt.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Widget;

import nl.uu.fi.dwo.interaction.client.InteractionStub;
import nl.uu.fi.dwo.interaction.client.OpdrNavIF;
import nl.uu.fi.dwo.interaction.client.Stub;

public class MathScratchGWT implements EntryPoint, InteractionStub {

  @Override
  public void onModuleLoad() {
  
    
    Stub.publish(this);
  }

  @Override
  public HashMap<String, Object> getState() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setState(HashMap<String, Object> h) {
    // TODO Auto-generated method stub
    
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
  public Boolean isCorrect() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void kijkNa() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void zetNagekeken(boolean b) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void setCommunicationRoot(OpdrNavIF comRoot) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void zetVolledigeBreedte(int breedte) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Widget asWidget() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getAsHoogte() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getHeight() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getWidth() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void setAsHoogte(int ashoogte) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void init(int width, int height, Map<String, Object> launchData,
      Map<String, Number> values) {
    // TODO Auto-generated method stub
    
  }

}
