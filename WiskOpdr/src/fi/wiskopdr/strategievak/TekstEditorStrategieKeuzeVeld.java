package fi.wiskopdr.strategievak;

import java.awt.event.ActionEvent;
import fi.wiskopdr.tekstobjects.TekstEditor;

public class TekstEditorStrategieKeuzeVeld extends TekstEditor {

  private StrategieVakEditPanel parent;
  private int rangNr;
  
  public TekstEditorStrategieKeuzeVeld(StrategieVakEditPanel parent, int rangNr)
  {
    super();
    withIconan();
    this.parent = parent;
    this.rangNr = rangNr;
  }
  
  
  @Override
  public void actionPerformed(ActionEvent e)
  { 
    super.actionPerformed(e);
    if(tekstVak != null && e.getSource() == tekstVak)
      parent.moveStepFocus(rangNr);
      
  }
}
