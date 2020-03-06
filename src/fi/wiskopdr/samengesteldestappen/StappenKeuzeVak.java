package fi.wiskopdr.samengesteldestappen;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JComboBox;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.formuleobjects.FormuleParser;
import fi.wiskopdr.tekstobjects.TekstArea;

public class StappenKeuzeVak extends JLayeredPane implements ActionListener
{
  
  SamengesteldeStappenPanel parent;
  
  private JComboBox stepKV;
  private String[] stepContents;  
  
  //for later: make sure logging is taken care of
  //private boolean logOption;
  //private String logID;
  
  public StappenKeuzeVak()
  {
      setLayout(null);

      stepKV = new JComboBox();
      stepKV.setBounds(0, 0, 180, 24);
      stepKV.setFont(WiskOpdr.tekstFont);

      ComboBoxFormuleRenderer renderer = new ComboBoxFormuleRenderer();
      stepKV.setRenderer(renderer);

      stepKV.setRenderer(new ComboBoxFormuleRenderer());
      
      stepKV.addItem(WiskOpdr.rb.getString("keuzeVakKiesLabel"));
      stepKV.addActionListener(this);
      add(stepKV);

      setSize(stepKV.getWidth(), 24);

  }

  class ComboBoxFormuleRenderer extends TekstArea implements ListCellRenderer
  {

      public ComboBoxFormuleRenderer()
      {

          setOpaque(true);
      }

      /*
      * This method finds the image and text corresponding
      * to the selected value and returns the label, set up
      * to display the text and image.
      */
      public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
      {
          //Get the selected index. (The index param isn't
          //always valid, so just use the value.)

                    if (isSelected)
          {
              setBackground(list.getSelectionBackground());
              setForeground(list.getSelectionForeground());
          }
          else
          {
              setBackground(list.getBackground());
              setForeground(list.getForeground());
          }

          //Set the icon and text.  If icon was null, say so.
          setSize(stepKV.getWidth() + 50, 20);
          this.setText((String) value);

          resize();
          setPreferredSize(new Dimension(stepKV.getWidth() + 50, getHeight() + 1));

          return this;
      }

  }
  
  public void setParent(SamengesteldeStappenPanel parent)
  {
    this.parent = parent;
  }
  
  @Override
  public void actionPerformed(ActionEvent e)
  {
    if(stepKV.getSelectedIndex() == 0)
      return;
    String stepContent = stepContents[stepKV.getSelectedIndex() - 1];
    parent.makeStep(stepContent);
    parent.addSelectedStep(stepKV.getSelectedIndex() - 1);
    stepKV.setSelectedIndex(0);
  }
  
  public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
  {
    Hashtable[] steps = null;
    
     if(h.containsKey("steps"))
      steps = (Hashtable[])h.get("steps");
    
    int aantalKeuzes = 0;
    if (steps != null)
      aantalKeuzes = steps.length;
        
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
      stepKV.addItem(keuze.trim());      
    }
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
    
  }
  
  @Override
  public void setBounds(int x, int y, int width, int height)
  {
    super.setBounds(x, y, width, height);
    stepKV.setSize(width, height);
  }
}
