package fi.wiskopdr.strategievak;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.border.Border;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.opdrnav.OpdrachtNrRij;
import fi.wiskopdr.opdrnav.PlusMinKnop;
import fi.wiskopdr.opdrnav.XWidgetManager;
import fi.wiskopdr.tekstobjects.BasisTekstVak;
import fi.wiskopdr.tekstobjects.TekstEditor;
import fi.wiskopdr.tekstobjects.TekstVak;

public class StrategieVakEditPanel extends JPanel implements InteractieEditPanel, ActionListener, FocusListener, MouseListener{

    int stappenBreedte = 0;
    int stappenHoogte = 400;
    
	int editHeight = 400;
    
    Font theFont;
    FontMetrics theFM;
    
    int width = 400;
    int height = 300;
    
    int cbHeight = 20;
    int cbWidth = 200;
    
    int offset = 10;
    
	JComboBox symboolKeuzeBox;
	private TekstEditor[] keuzeVelden;
	private JLabel[] nrLabels;
	private JCheckBox[] vereistCB;
	private PlusMinKnop stapPositieKnop;
    private JPanel keuzeVeldenPanel;
    private JScrollPane scrollPaneKeuzeVelden;
    private JPanel basisKeuzeVeldenPanel;
	private int aantalKeuzes = 3;
    //private JLabel aantalKeuzesLabel;
    private JTextField aantalKeuzesTF;
    private int scoreMax = 10;
    //private JLabel scoreLabel;
    private JTextField scoreTF;
    
    private TekstEditor inhoudvak;
    private OpdrachtNrRij tabbladTab;
    //private String[] stepContents;
    private ArrayList<String>[] stepContents;
    private boolean[] stepRequired;
    private int keuzeNr = 0;
    private int maxAantalKeuzes = 20;
	
	public StrategieVakEditPanel()
	{
	    setLayout(null);
	    
	    int currentX = stappenBreedte + 2 * offset;
		int currentY = offset;
        
        makeLabel(currentX, currentY, 180, cbHeight, WiskOpdr.rb.getString("Steps_nrOfSteps"), true); 
		
        currentX += 180;
        aantalKeuzesTF = new JTextField("" + aantalKeuzes);
        aantalKeuzesTF.setBounds(currentX,currentY,40,cbHeight);
        aantalKeuzesTF.addActionListener(this);
        aantalKeuzesTF.addFocusListener(this);
        add(aantalKeuzesTF);
        
        currentX += 170 + offset;
        makeLabel(currentX, currentY, 180, cbHeight, WiskOpdr.rb.getString("scoreLabel"), true);
        
        currentX += 60;
        scoreTF = new JTextField("" + scoreMax);
        scoreTF.setBounds(currentX, currentY, 40, cbHeight);
        add(scoreTF);
        
        currentX -= 350 + offset + 60;
        currentY += cbHeight + offset;
        makeLabel(currentX,currentY,200,20,WiskOpdr.rb.getString("Steps_name"),true);
        
        currentX += 290;
        makeLabel(currentX, currentY, 60, 20, WiskOpdr.rb.getString("Steps_required"),true);
        
        currentX -= 290;
        currentY += cbHeight + offset / 2;
        
        //currentX += 20;
        basisKeuzeVeldenPanel = new JPanel();
        basisKeuzeVeldenPanel.setBounds(currentX,currentY,350,400);
        basisKeuzeVeldenPanel.setLayout(new BorderLayout());
        basisKeuzeVeldenPanel.setBackground(new Color(210,210,0));
        add(basisKeuzeVeldenPanel);
        
        keuzeVeldenPanel = new JPanel();
        keuzeVeldenPanel.setLayout(null);
        keuzeVeldenPanel.setBounds(0,0,190,100);
        
        scrollPaneKeuzeVelden = new JScrollPane(keuzeVeldenPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneKeuzeVelden.setBorder(BorderFactory.createEmptyBorder());
        scrollPaneKeuzeVelden.setBackground(new Color(0,210,210));
        basisKeuzeVeldenPanel.add(scrollPaneKeuzeVelden);
        
        stapPositieKnop = new PlusMinKnop(0,12,16,20,PlusMinKnop.VERTIKAAL);
        stapPositieKnop.setBackground(new Color(210,210,210));
        stapPositieKnop.addActionListener(this);
        keuzeVeldenPanel.add(stapPositieKnop,0);
        
        nrLabels = new JLabel[maxAantalKeuzes];
        vereistCB = new JCheckBox[maxAantalKeuzes];
        Border border = BorderFactory.createMatteBorder(1, 1, 1, 0, new Color(128, 128, 128));
        int veldHeight = 50;
        int veldOffset = 5;
        int veldWidth = 320;
        for(int i = 0; i < maxAantalKeuzes; i++)
        {
          nrLabels[i] = new JLabel("" + (i+1), SwingConstants.CENTER);
          nrLabels[i].setBounds(20,i * (veldHeight + veldOffset), 20, 50);
          nrLabels[i].setOpaque(true);
          nrLabels[i].setBackground(new Color(230,230,230));
          nrLabels[i].setBorder(border);
          nrLabels[i].setFont(theFont);
          nrLabels[i].addMouseListener(this);
          
          vereistCB[i] = new JCheckBox();
          vereistCB[i].setBounds(veldWidth - 10, i * (veldHeight + veldOffset), 20, cbHeight);
          vereistCB[i].setOpaque(false);
          vereistCB[i].addMouseListener(this);
        }

        maakKeuzeVelden();
			
        currentX += 350 + offset;
        currentY -= cbHeight + offset / 2;
        
        makeLabel(currentX,currentY,420,20,WiskOpdr.rb.getString("Steps_content"),true);
        currentY += cbHeight + offset / 2;
        
        tabbladTab = new OpdrachtNrRij(aantalKeuzes, currentX, currentY);
        tabbladTab.setSize(tabbladTab.getSize().width, 23);
        tabbladTab.setTab(true);
        tabbladTab.setScoresVisible(false);
        tabbladTab.addActionListener(this);
        tabbladTab.setBackground(new Color(210,210,210));
        tabbladTab.setSelected(1);
        add(tabbladTab,0);
        
        currentY += 22;
        
        inhoudvak = new TekstEditor(true, true);
        inhoudvak.setBounds(currentX,currentY,420,150);
        inhoudvak.addActionListener(this);
        add(inhoudvak);
        inhoudvak.setResizable(true);
        
        
        stepContents = new ArrayList[aantalKeuzes];
        for(int i = 0; i < aantalKeuzes; i++)
        { stepContents[i] = new ArrayList<String>();
        }
        stepRequired = new boolean[maxAantalKeuzes];
        for(int i = 0; i < maxAantalKeuzes; i++)
          stepRequired[i] = false;
                
    }

	public void plaatsComponenten()
	{
		if(symboolKeuzeBox == null)
			return;
	}
	
	public JLabel makeLabel(int x, int y, int b, int h, String text, boolean visible)
    {   JLabel label = new JLabel(text);
        label.setBounds(x,y,b,h);
        label.setVisible(visible);
        label.setFont(theFont);
        add(label,0);
        return label;
    }
	
	private void setStepContent(ArrayList<String> content)
	{
	    String contentString = "";
	    for(int i = 0; i < content.size(); i++)
	      contentString = contentString + content.get(i);
        inhoudvak.zetTekst("");  
        inhoudvak.geefTekstVak().setCaret(0);
        inhoudvak.geefTekstVak().insert(contentString);
        inhoudvak.geefTekstVak().setCaret(contentString.length());
        inhoudvak.layoutTekst();
            
    }
    
    private void getStepContent()
    {   if(stepContents==null)
          return;
        else if(keuzeNr >= stepContents.length)
          return;
        //stepContents[keuzeNr] = inhoudvak.getCompleteText().trim();
        String content = inhoudvak.getCompleteText().trim();
        //hier de grote kniptruc
        
        stepContents[keuzeNr] = getArrayListContentsFromString(content);
    }
    
    private ArrayList<String> getArrayListContentsFromString(String s)
    {
      System.out.println("input: " + s);
      
      ArrayList<String> list = new ArrayList<String>();
      
      int startingPoint = 0;
      for(int i = 1; i < s.length(); i++)
      { if(s.charAt(i) == '$' && i < s.length() && s.charAt(i + 1) == 'V')
        {  list.add(s.substring(startingPoint, i));
           startingPoint = i;
        }
        if(s.charAt(i) == '@')
        {
            if(s.charAt(startingPoint) == '$' && s.charAt(startingPoint + 1) == 'V')
            {
              list.add(s.substring(startingPoint, i + 1));
              startingPoint = i + 1;
            }
        }
      }
      if(startingPoint < s.length())
        list.add(s.substring(startingPoint, s.length()));
      return list;
    }
	
    private void setStepContent()
    {   if(stepContents==null)
          return;
        else if(keuzeNr >= stepContents.length)
          return;
        setStepContent(stepContents[keuzeNr]);    
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == aantalKeuzesTF)
        {
		  aantalKeuzeVeldenAction();
          
        }
		else if(e.getSource() == tabbladTab)
        {   int nr = Integer.parseInt(e.getActionCommand())-1;
            moveStepFocus(nr);
            inhoudvak.geefTekstVak().requestFocus();
        }
		else if(e.getSource() == stapPositieKnop)
        {   int posX = (int) stapPositieKnop.getLocation().getX();
            int posY = (int) stapPositieKnop.getLocation().getY();
            String resKeuze = "";
            if(e.getActionCommand().equals("min") && keuzeNr<aantalKeuzes-1) 
            {   
                resKeuze = keuzeVelden[keuzeNr].getCompleteText();
                //String resStepContent = inhoudvak.getCompleteText().trim();
                ArrayList<String> resStepContent = getArrayListContentsFromString(inhoudvak.getCompleteText().trim());
                boolean resRequired = stepRequired[keuzeNr];
                
                keuzeVelden[keuzeNr].zetTekst(keuzeVelden[keuzeNr + 1].getCompleteText());
                keuzeVelden[keuzeNr + 1].zetTekst(resKeuze);
                
                stepContents[keuzeNr] = stepContents[keuzeNr + 1];
                stepContents[keuzeNr + 1] = resStepContent;
                
                vereistCB[keuzeNr].setSelected(stepRequired[keuzeNr + 1]);
                vereistCB[keuzeNr + 1].setSelected(resRequired);
                stepRequired[keuzeNr] = stepRequired[keuzeNr + 1];
                stepRequired[keuzeNr + 1] = resRequired;
                                
                keuzeVelden[keuzeNr].layoutTekst();
                keuzeNr++;//adjust keuzeNr here to avoid that moveStepFocus (fired from layoutTekst) changes too much 
                keuzeVelden[keuzeNr].layoutTekst();
                
                tabbladTab.setSelected(keuzeNr+1);
                stapPositieKnop.setLocation(posX, posY + 55);
            }
            if(e.getActionCommand().equals("plus") && keuzeNr>0) 
            {   resKeuze = keuzeVelden[keuzeNr].getCompleteText();
                //String resStepContent = inhoudvak.getCompleteText().trim();
                ArrayList<String> resStepContent = getArrayListContentsFromString(inhoudvak.getCompleteText().trim());
                boolean resRequired = stepRequired[keuzeNr];
            
                keuzeVelden[keuzeNr].zetTekst(keuzeVelden[keuzeNr - 1].getCompleteText());
                keuzeVelden[keuzeNr - 1].zetTekst(resKeuze);
                
                stepContents[keuzeNr] = stepContents[keuzeNr-1];
                stepContents[keuzeNr-1] = resStepContent;
                
                vereistCB[keuzeNr].setSelected(stepRequired[keuzeNr - 1]);
                vereistCB[keuzeNr - 1].setSelected(resRequired);
                stepRequired[keuzeNr] = stepRequired[keuzeNr - 1];
                stepRequired[keuzeNr - 1] = resRequired;
                
                keuzeVelden[keuzeNr].layoutTekst();
                keuzeNr--;
                keuzeVelden[keuzeNr].layoutTekst();
                tabbladTab.setSelected(keuzeNr+1);
                stapPositieKnop.setLocation(posX, posY - 55);
            }
            
        }
		
	}
	
	public void aantalKeuzeVeldenAction()
	{
	  int posX = (int) stapPositieKnop.getLocation().getX();
      int posY = (int) stapPositieKnop.getLocation().getY();
      int oldKeuzeNr = keuzeNr;
      try {
        aantalKeuzes = Math.min(maxAantalKeuzes, Integer.parseInt(aantalKeuzesTF.getText()));  
      }
      catch(Exception e) {
        return;
      }
      if(aantalKeuzes < keuzeNr + 1)
      {  int verschil = keuzeNr + 1 - aantalKeuzes; 
         keuzeNr = aantalKeuzes - 1;
         setStepContent();
         stapPositieKnop.setLocation(posX, posY - verschil * 55);
      }
      maakKeuzeVelden();
      maakInhoudVelden();
      if(aantalKeuzes > oldKeuzeNr + 1)
      {  keuzeNr = oldKeuzeNr;
         stapPositieKnop.setLocation(posX, posY);
         tabbladTab.setSelected(keuzeNr+1);
      }
      for(int i = aantalKeuzes; i < maxAantalKeuzes; i++)
        stepRequired[i] = false;
	}

	@Override
	public void focusGained(FocusEvent e) {
	  
	}

	@Override
	public void focusLost(FocusEvent e) {
	    if(e.getSource() == aantalKeuzesTF)
	    {
	      aantalKeuzeVeldenAction();
	    }
	}

	public void maakKeuzeVelden()
    {
          int veldHeight = 50;
          int veldWidth = 320;
          int offset = 5;
          TekstEditor[] oldKeuzeVelden = null;
          keuzeVeldenPanel.setBounds(0, 0,veldWidth,aantalKeuzes*(veldHeight + offset));
          keuzeVeldenPanel.removeAll();
          if(keuzeVelden != null)
          { 
            oldKeuzeVelden = new TekstEditor[keuzeVelden.length];
            for(int i = 0; i < keuzeVelden.length; i++)
            { keuzeVelden[i].removeMouseListener(this);  
              oldKeuzeVelden[i] = keuzeVelden[i];
            
            }
          }
          
          keuzeVelden = new TekstEditorStrategieKeuzeVeld[aantalKeuzes];
          for(int i=0 ; i<aantalKeuzes ; i++)
          {
              
              keuzeVeldenPanel.add(nrLabels[i]);
              
              
              if(oldKeuzeVelden != null && i < oldKeuzeVelden.length)
              {
                keuzeVelden[i] = oldKeuzeVelden[i];
              }
              else
              {
                keuzeVelden[i] = new TekstEditorStrategieKeuzeVeld(this, i);
                keuzeVelden[i].setBounds(40,i*(veldHeight + offset),veldWidth - 30,veldHeight);
              }
              keuzeVeldenPanel.add(keuzeVelden[i],0);
              keuzeVelden[i].addMouseListener(this);
              keuzeVeldenPanel.add(vereistCB[i], 0);
          }
          keuzeVeldenPanel.add(stapPositieKnop);
          keuzeVeldenPanel.setPreferredSize(new Dimension(veldWidth,aantalKeuzes*(veldHeight + offset)));
          keuzeVeldenPanel.scrollRectToVisible(new Rectangle(0,0, 10, 100));
          keuzeVeldenPanel.revalidate();
          keuzeVeldenPanel.doLayout();
          
        repaint();
    }
	
	public void maakInhoudVelden()
	{
	  int tabbladX = (int) tabbladTab.getLocation().getX();
	  int tabbladY = (int) tabbladTab.getLocation().getY();
	  remove(tabbladTab);
	  tabbladTab = new OpdrachtNrRij(aantalKeuzes, tabbladX, tabbladY);
      tabbladTab.setTab(true);
      tabbladTab.setScoresVisible(false);
      tabbladTab.setSize(tabbladTab.getSize().width, 23);
      tabbladTab.addActionListener(this);
      tabbladTab.setBackground(new Color(210,210,210));
      tabbladTab.setSelected(keuzeNr+1);
      add(tabbladTab,0);
      ArrayList<String>[] stepContentsNew = new ArrayList[aantalKeuzes];
      for(int i = 0; i < stepContentsNew.length; i++)
        stepContentsNew[i] = new ArrayList<String>();
      for(int i = 0; i < Math.min(aantalKeuzes, stepContents.length); i++)
      {
        stepContentsNew[i] = stepContents[i];
      }
      stepContents = stepContentsNew;
      repaint();
	}
	
	
	@Override
	public void setEditState(Hashtable h) {
		Hashtable[] steps = null;
		int scoreMax = 10;
		boolean[] stepRequired = null;
		
		if(h.containsKey("scoreMax"))
		  scoreMax = ((Integer) h.get("scoreMax")).intValue();
		this.scoreMax = scoreMax;
		if(h.containsKey("stepRequired"))
		  stepRequired = (boolean[]) h.get("stepRequired");
		if(stepRequired != null && stepRequired.length > 0)
		{   for(int i = 0; i < stepRequired.length; i++)
		    {   this.stepRequired[i] = stepRequired[i];
		    }
		}
		
		scoreTF.setText("" + scoreMax);
		if(h.containsKey("steps"))
		    steps = (Hashtable[]) h.get("steps");
		aantalKeuzes = steps.length;
        aantalKeuzesTF.setText(""+aantalKeuzes);
        maakKeuzeVelden();
        for(int i=0 ; i<aantalKeuzes ; i++)
        {   
            String keuze = ((String) steps[i].get("keuze"));
            keuzeVelden[i].zetTekst(keuze);
            keuzeVelden[i].layoutTekst();
        }
        this.stepContents = new ArrayList[steps.length];
        
        for(int i = 0; i < aantalKeuzes; i++)
        {
          ArrayList<String> content = new ArrayList<String>();
          try{
           content = (ArrayList<String>) steps[i].get("stepContent");
          }
          catch(Exception e)
          {
            String contentString = (String) steps[i].get("stepContent");
            content = getArrayListContentsFromString(contentString);
          }
          for(int j = 0; j < content.size(); j++)
          {
            if(content.get(j).startsWith("H4sIAAAAAAAAA"))
            {   
              String toReplace = content.get(j);
              content.set(j, "$V" + toReplace + "@");
              //content = "$V"+content+"@";
            }
            
          }
          this.stepContents[i] = content;
          vereistCB[i].setSelected(stepRequired[i]);
        }
        
        maakInhoudVelden();
        keuzeNr = 0;
        setStepContent();
        tabbladTab.setSelected(keuzeNr+1);
        int posX = (int) stapPositieKnop.getLocation().getX();
        int posY = (int) stapPositieKnop.getLocation().getY();
        stapPositieKnop.setLocation(posX, posY - (aantalKeuzes - 1) * 55);
	}

	@Override
	public Hashtable getEditState() {
		
	    ArrayList<String>[] stepContents = null;
	    int scoreMax = 10;
        
	    getStepContent();
        stepContents = this.stepContents;
        if(stepContents!=null)setStepContent(stepContents[0]);
        
        for(int i = 0; i < stepContents.length; i++)
        {
          //poging over andere boeg
          setStepContent(stepContents[i]);
          TekstVakPanel tvp = new TekstVakPanel();
          tvp.zetTekst(inhoudvak.geefTekstVak().toString());
          String tvpString = tvp.toString();
          System.out.println("tvpString: " + tvpString); // dat is niet de string die ik wil hebben. Ik heb geen idee hoe ik die wel krijg.. ($V ..)
          
          
          //einde poging over andere boeg
          
          
          
          for(int j = 0; j < stepContents[i].size(); j++)
          {
            if(stepContents[i].get(j).startsWith("$V"))
            {
              String toReplace = stepContents[i].get(j);
              stepContents[i].set(j, toReplace.substring(2, toReplace.length() - 1));
            }
              //stepContents[i] = stepContents[i].substring(2, stepContents[i].length() - 1);
          }
        }
        Hashtable[] steps = new Hashtable[aantalKeuzes];
        for(int i = 0; i < aantalKeuzes; i++)
        {
          steps[i] = new Hashtable();
          steps[i].put("keuze", keuzeVelden[i].getCompleteText());
          
          steps[i].put("stepContent", stepContents[i]);
        }
        
        try
        {   scoreMax = Integer.parseInt(scoreTF.getText());
        }   
        catch(Exception ex) {}
        
        Hashtable h = new Hashtable();
        h.put("steps", steps);
        h.put("scoreMax", new Integer(scoreMax));
        h.put("stepRequired", stepRequired);
        
        return h;
	}

	
	
	@Override
	public void zetBreedte(int b) {
		stappenBreedte = b;
		setBounds(getLocation().x, getLocation().y, stappenBreedte + width + 3 * offset, Math.max(stappenHoogte, editHeight));		
		plaatsComponenten();
		
	}

	@Override
	public void zetHoogte(int h) {
		stappenHoogte = h;
		setBounds(getLocation().x, getLocation().y, stappenBreedte + width + 3 * offset, Math.max(stappenHoogte, editHeight));		
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void start() {
		
	}

  @Override
  public void mouseClicked(MouseEvent e) {
    for(int i = 0; i < keuzeVelden.length; i++)
    {   if(e.getSource() == vereistCB[i])
          stepRequired[i] = vereistCB[i].isSelected();
    }
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if(keuzeVelden != null)
    {
      for(int i = 0; i < keuzeVelden.length; i++)
      {   if(e.getSource() == keuzeVelden[i] || e.getSource() == nrLabels[i])
          {
            moveStepFocus(i);
          }
      }
    }
    
  }

  public void moveStepFocus(int i)
  {
    if(keuzeNr != i && tabbladTab != null) 
    {
        int verschil = keuzeNr - i;
        getStepContent();
        keuzeNr = i;
        setStepContent();
        tabbladTab.setSelected(keuzeNr+1);
        
        int posX = (int) stapPositieKnop.getLocation().getX();
        int posY = (int) stapPositieKnop.getLocation().getY();
        stapPositieKnop.setLocation(posX, posY - verschil * 55);
    }
  }
  
  @Override
  public void mouseReleased(MouseEvent e) {
    
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    
  }

  @Override
  public void mouseExited(MouseEvent e) {
    
  }
	
	
}
