package fi.wiskopdr.samengesteldestappen;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import fi.wiskopdr.tekstobjects.TekstEditor;
import fi.wiskopdr.tekstobjects.TekstVak;

public class TekstEditorForKeuzeVeld extends TekstEditor {

  private SamengesteldeStappenEditPanel parent;
  private int rangNr;
  
  public TekstEditorForKeuzeVeld(SamengesteldeStappenEditPanel parent, int rangNr)
  {
    super();
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
