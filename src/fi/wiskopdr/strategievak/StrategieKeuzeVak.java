package fi.wiskopdr.strategievak;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JLayeredPane;
import javax.swing.JToolBar;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleParser;

public class StrategieKeuzeVak extends JLayeredPane
{
  
  StrategieVakPanel parent;
  
  private JToolBar stepKV;
  private String[] stepContents;  
  
  //for later: make sure logging is taken care of
  //private boolean logOption;
  //private String logID;
  
  public StrategieKeuzeVak()
  {
      setLayout(null);

      stepKV = new JToolBar(); stepKV.setFloatable(false); stepKV.setRollover(true);
      stepKV.setBounds(0, 0, 180, 24);
      stepKV.setFont(WiskOpdr.tekstFont);

      add(stepKV);

      setSize(stepKV.getWidth(), 24);

  }

  
  
  public void setParent(StrategieVakPanel parent)
  {
    this.parent = parent;
  }
  
  class MyAction extends AbstractAction {

    final private String stepcontent;
    final int index;
    public MyAction(String name, Icon icon, String stepcontent, int index) {
      super(name, icon);
      this.stepcontent = stepcontent;
      this.index = index;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
      parent.makeStep(stepcontent);
      parent.addSelectedStep(index);
    }
  }
  
  
  public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
  {
    Hashtable[] steps = null;
    
     if(h.containsKey("steps"))
      steps = (Hashtable[])h.get("steps");
    
    int aantalKeuzes = 0;
    if (steps != null)
      aantalKeuzes = steps.length;
        
    stepContents = new String[aantalKeuzes];
    for (int i = 0; i < aantalKeuzes; i++)
    {
        ArrayList<String> content = (ArrayList<String>) steps[i].get("stepContent");
        for(int j = 0; j < content.size(); j++)
        {
          if(content.get(j).startsWith("H4sIAAAAAAAAA"))
          {  String toReplace = content.get(j);
             content.set(j, "$V" + toReplace + "@");
          }
        }
        String stepContent = "";
        for(int j = 0; j < content.size(); j++)
          stepContent = stepContent + content.get(j);
        stepContents[i] = stepContent;
      
      
//      String content = (String) steps[i].get("stepContent");
//      if(content.startsWith("H4sIAAAAAAAAA"))
//      {   content = "$V"+content+"@";
//      }
//      stepContents[i] = content;
    }
    for (int i = 0; i < aantalKeuzes; i++)
    {
      String keuze = (String) steps[i].get("keuze");
      try
        {
            keuze = FormuleParser.randomizeTekstVakString(keuze, randomVars, randomValues);
        }
      catch (Exception e)
      {
      }
      
      //stepKV.addItem(keuze.trim());     
      MyAction btn = new MyAction(keuze.trim(), null, stepContents[i], i);
      stepKV.add(btn);
    }
    
  }
  
  @Override
  public void setBounds(int x, int y, int width, int height)
  {
    super.setBounds(x, y, width, height);
    stepKV.setSize(width, height);
  }
}
