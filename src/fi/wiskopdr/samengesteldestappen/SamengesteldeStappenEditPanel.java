package fi.wiskopdr.samengesteldestappen;

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

public class SamengesteldeStappenEditPanel extends JPanel implements InteractieEditPanel, ActionListener, FocusListener, MouseListener{

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
	JCheckBox statistiekCB;
	private TekstEditor[] keuzeVelden;
	private JLabel[] nrLabels;
	private PlusMinKnop stapPositieKnop;
    private JPanel keuzeVeldenPanel;
    private JScrollPane scrollPaneKeuzeVelden;
    private JPanel basisKeuzeVeldenPanel;
	private int aantalKeuzes = 3;
    private JLabel aantalKeuzesLabel;
    private JTextField aantalKeuzesTF;
    
    private TekstEditor antwoordvak;
    private OpdrachtNrRij tabbladTab;
    private String[] stepContents;
    private int keuzeNr = 0;
    
	
	public SamengesteldeStappenEditPanel()
	{
	    setLayout(null);
	    setBackground(WiskOpdr.bgcolor);

	    int currentX = stappenBreedte + 2 * offset;
		int currentY = offset;
		
		statistiekCB = new JCheckBox(WiskOpdr.rb.getString("Steps_statistics"));
        statistiekCB.setBounds(currentX, currentY, cbWidth, cbHeight);
        statistiekCB.setOpaque(false);
        statistiekCB.setFont(theFont);
        add(statistiekCB);
        
        currentY += cbHeight + offset;
        
		aantalKeuzesLabel = new JLabel(WiskOpdr.rb.getString("Steps_nrOfSteps"));
        aantalKeuzesLabel.setBounds(currentX,currentY,180,cbHeight);
        add(aantalKeuzesLabel);
        
        currentX += 180;
        aantalKeuzesTF = new JTextField("" + aantalKeuzes);
        aantalKeuzesTF.setBounds(currentX,currentY,40,cbHeight);
        aantalKeuzesTF.addActionListener(this);
        aantalKeuzesTF.addFocusListener(this);
        add(aantalKeuzesTF);
        
        currentX -= 180;
        currentY += cbHeight + offset;
        
        makeLabel(currentX,currentY,520,20,WiskOpdr.rb.getString("Steps_name"),true);
        
        currentY += cbHeight + offset;
        
        //currentX += 20;
        basisKeuzeVeldenPanel = new JPanel();
        basisKeuzeVeldenPanel.setBounds(currentX,currentY,350,400);
        basisKeuzeVeldenPanel.setLayout(new BorderLayout());
        add(basisKeuzeVeldenPanel);
        
        keuzeVeldenPanel = new JPanel();
        keuzeVeldenPanel.setLayout(null);
        keuzeVeldenPanel.setBounds(0,0,190,100);
        keuzeVeldenPanel.setBackground(WiskOpdr.bgcolor);
        
        scrollPaneKeuzeVelden = new JScrollPane(keuzeVeldenPanel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneKeuzeVelden.setBorder(BorderFactory.createEmptyBorder());
        basisKeuzeVeldenPanel.add(scrollPaneKeuzeVelden);
        
        stapPositieKnop = new PlusMinKnop(0,12,16,20,PlusMinKnop.VERTIKAAL);
        stapPositieKnop.setBackground(new Color(210,210,210));
        stapPositieKnop.addActionListener(this);
        keuzeVeldenPanel.add(stapPositieKnop,0);

        
        maakKeuzeVelden();
			
        currentX += 350 + offset;
        currentY -= cbHeight + offset;
        
        makeLabel(currentX,currentY,520,20,WiskOpdr.rb.getString("Steps_content"),true);
        currentY += cbHeight + offset;
        
        tabbladTab = new OpdrachtNrRij(aantalKeuzes, currentX, currentY);
        tabbladTab.setSize(tabbladTab.getSize().width, 23);
        tabbladTab.setTab(true);
        tabbladTab.setScoresVisible(false);
        tabbladTab.addActionListener(this);
        tabbladTab.setBackground(new Color(210,210,210));
        tabbladTab.setSelected(1);
        add(tabbladTab,0);
        
        currentY += 22;
        
        antwoordvak = new TekstEditor(true, true);
        antwoordvak.setBounds(currentX,currentY,520,150);
        antwoordvak.addActionListener(this);
        add(antwoordvak);
        antwoordvak.setResizable(true);
        
        
        stepContents = new String[aantalKeuzes];
        for(int i = 0; i < aantalKeuzes; i++)
          stepContents[i] = "";
                
    }

	public void plaatsComponenten()
	{
		if(symboolKeuzeBox == null)
			return;
		statistiekCB.setLocation(2 * offset, statistiekCB.getLocation().y);
	}
	
	public JLabel makeLabel(int x, int y, int b, int h, String text, boolean visible)
    {   JLabel label = new JLabel(text);
        label.setBounds(x,y,b,h);
        label.setVisible(visible);
        add(label,0);
        return label;
    }
	
	private void setStepContent(String content)
	{
        antwoordvak.zetTekst("");  
        antwoordvak.geefTekstVak().setCaret(0);
        antwoordvak.geefTekstVak().insert(content);
        antwoordvak.geefTekstVak().setCaret(content.length());
        antwoordvak.layoutTekst();
            
    }
    
    private void getStepContent()
    {   if(stepContents==null)
          return;
        else if(keuzeNr >= stepContents.length)
          return;
        stepContents[keuzeNr] = antwoordvak.getCompleteText().trim(); 
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
            antwoordvak.geefTekstVak().requestFocus();
        }
		else if(e.getSource() == stapPositieKnop)
        {   int posX = (int) stapPositieKnop.getLocation().getX();
            int posY = (int) stapPositieKnop.getLocation().getY();
            String resKeuze = "";
            if(e.getActionCommand().equals("min") && keuzeNr<aantalKeuzes-1) 
            {   
                resKeuze = keuzeVelden[keuzeNr].getCompleteText();
                String resStepContent = antwoordvak.getCompleteText().trim();
                
                keuzeVelden[keuzeNr].zetTekst(keuzeVelden[keuzeNr + 1].getCompleteText());
                keuzeVelden[keuzeNr + 1].zetTekst(resKeuze);
                
                stepContents[keuzeNr] = stepContents[keuzeNr + 1];
                stepContents[keuzeNr + 1] = resStepContent;
                                
                keuzeVelden[keuzeNr].layoutTekst();
                keuzeNr++;//adjust keuzeNr here to avoid that moveStepFocus (fired from layoutTekst) changes too much 
                keuzeVelden[keuzeNr].layoutTekst();
                
                tabbladTab.setSelected(keuzeNr+1);
                stapPositieKnop.setLocation(posX, posY + 55);
            }
            if(e.getActionCommand().equals("plus") && keuzeNr>0) 
            {   resKeuze = keuzeVelden[keuzeNr].getCompleteText();
                String resStepContent = antwoordvak.getCompleteText().trim();
            
                keuzeVelden[keuzeNr].zetTekst(keuzeVelden[keuzeNr - 1].getCompleteText());
                keuzeVelden[keuzeNr - 1].zetTekst(resKeuze);
                
                stepContents[keuzeNr] = stepContents[keuzeNr-1];
                stepContents[keuzeNr-1] = resStepContent;
                
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
      aantalKeuzes = Math.min(20, Integer.parseInt(aantalKeuzesTF.getText()));
      if(aantalKeuzes < keuzeNr + 1)
      {  int verschil = keuzeNr + 1 - aantalKeuzes; 
         keuzeNr = aantalKeuzes - 1;
         setStepContent();
         stapPositieKnop.setLocation(posX, posY - verschil * 55);
      }
      maakKeuzeVelden();
      maakFeedbackVelden();
      if(aantalKeuzes > keuzeNr)
      {  keuzeNr = oldKeuzeNr;
         stapPositieKnop.setLocation(posX, posY);
         tabbladTab.setSelected(keuzeNr+1);
      }
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
          if(keuzeVelden != null)
          { 
            oldKeuzeVelden = new TekstEditor[keuzeVelden.length];
            for(int i = 0; i < keuzeVelden.length; i++)
            { keuzeVelden[i].removeMouseListener(this);  
              nrLabels[i].removeMouseListener(this);
              oldKeuzeVelden[i] = keuzeVelden[i];
            
            }
            
            if(aantalKeuzes < keuzeVelden.length)
            {
              for(int i = aantalKeuzes; i < keuzeVelden.length; i++)
              {
                if(keuzeVelden[i] != null)
                {  keuzeVeldenPanel.remove(keuzeVelden[i]);
                   keuzeVeldenPanel.remove(nrLabels[i]); 
                }
              }
            }
          }
          
          nrLabels = new JLabel[aantalKeuzes];
          Border border = BorderFactory.createMatteBorder(1, 1, 1, 0, new Color(128, 128, 128));
          keuzeVelden = new TekstEditorForKeuzeVeld[aantalKeuzes];
          for(int i=0 ; i<aantalKeuzes ; i++)
          {
              nrLabels[i] = new JLabel("" + (i+1), SwingConstants.CENTER);
              nrLabels[i].setBounds(20,i * (veldHeight + offset), 20, 50);
              nrLabels[i].setOpaque(true);
              nrLabels[i].setBackground(new Color(230,230,230));
              nrLabels[i].setBorder(border);
              nrLabels[i].setFont(theFont);
              nrLabels[i].addMouseListener(this);
              
              keuzeVeldenPanel.add(nrLabels[i]);
              
              
              if(oldKeuzeVelden != null && i < oldKeuzeVelden.length)
              {
                keuzeVelden[i] = oldKeuzeVelden[i];
              }
              else
              {
                keuzeVelden[i] = new TekstEditorForKeuzeVeld(this, i);
                keuzeVelden[i].setBounds(40,i*(veldHeight + offset),veldWidth - 30,veldHeight);
                keuzeVeldenPanel.add(keuzeVelden[i],0);
              }
              keuzeVelden[i].addMouseListener(this);
          }
          keuzeVeldenPanel.setPreferredSize(new Dimension(veldWidth,aantalKeuzes*(veldHeight + offset)));
          keuzeVeldenPanel.scrollRectToVisible(new Rectangle(0,0, 10, 100));
          keuzeVeldenPanel.revalidate();
          keuzeVeldenPanel.doLayout();
          
        repaint();
    }
	
	public void maakFeedbackVelden()
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
      String[] stepContentsNew = new String[aantalKeuzes];
      for(int i = 0; i < stepContentsNew.length; i++)
        stepContentsNew[i] = "";
      for(int i = 0; i < Math.min(aantalKeuzes, stepContents.length); i++)
      {
        stepContentsNew[i] = stepContents[i];
      }
      stepContents = stepContentsNew;
      repaint();
	}
	
	
	@Override
	public void setEditState(Hashtable h) {
		boolean statistiek = false;
		Hashtable[] steps = null;
		
		if(h.containsKey("ideasStatistiek"))
			statistiek = ((Boolean) h.get("ideasStatistiek")).booleanValue();
		
		statistiekCB.setSelected(statistiek);
		
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
        this.stepContents = new String[steps.length];
        
        for(int i = 0; i < aantalKeuzes; i++)
        {
          String content = ((String) steps[i].get("stepContent"));
          if(content.startsWith("H4sIAAAAAAAAA"))
          {   content = "$V"+content+"@";
          }
          this.stepContents[i] = content;
        }
        
        maakFeedbackVelden();
        keuzeNr = 0;
        setStepContent();
        tabbladTab.setSelected(keuzeNr+1);
        int posX = (int) stapPositieKnop.getLocation().getX();
        int posY = (int) stapPositieKnop.getLocation().getY();
        stapPositieKnop.setLocation(posX, posY - (aantalKeuzes - 1) * 55);
	}

	@Override
	public Hashtable getEditState() {
		
	    String[] stepContents = null;
	    boolean ideasStatistiek = false;
        
	    getStepContent();
        stepContents = this.stepContents;
        if(stepContents!=null)setStepContent(stepContents[0]);
        
        for(int i = 0; i < stepContents.length; i++)
        {
          if(stepContents[i].startsWith("$V"))
            stepContents[i] = stepContents[i].substring(2, stepContents[i].length() - 1);
        }
        Hashtable[] steps = new Hashtable[aantalKeuzes];
        for(int i = 0; i < aantalKeuzes; i++)
        {
          steps[i] = new Hashtable();
          steps[i].put("keuze", keuzeVelden[i].getCompleteText());
          
          steps[i].put("stepContent", stepContents[i]);
        }
        
        ideasStatistiek = statistiekCB.isSelected();
        
        Hashtable h = new Hashtable();
        h.put("steps", steps);
        h.put("ideasStatistiek", new Boolean(ideasStatistiek));
        
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
