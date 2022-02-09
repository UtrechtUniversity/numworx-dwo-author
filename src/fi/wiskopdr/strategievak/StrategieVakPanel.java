package fi.wiskopdr.strategievak;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.wiskopdr.tekstobjects.TekstRegel;
import fi.wiskopdr.tekstobjects.TekstVak;

public class StrategieVakPanel extends JPanel implements InteractiePanel, ActionListener{

    int ashoogte = 15;
    StrategieKeuzeVak keuzeVak;
    int aantalStappen = 4;
    int scoreMax = 10;
    boolean[] stepRequired = null;
    TekstVakPanel stappenVak;
    JPanel choiceLine;
    //TODO for later: support random variables
    
    int offset = 5;
    int labelWidth = 30;
    int labelHeight = 27;
    int buttonWidth = 20;
    int buttonHeight = 24;
        
    ArrayList<Integer> selectedSteps = new ArrayList<Integer>();
    
    private String[] randomVars;
    private Hashtable randomValues;
    
	public StrategieVakPanel()
	{
	  int width = 400;
	  setLayout(null);
      
      int nrOfSteps = 15;//later nog wat flexibeler?
      makeStepsTVP(nrOfSteps);
      for(int i = 0; i < nrOfSteps; i++)
      {
        TekstVak vak = stappenVak.geefTekstVak(i, 0);
        TekstInteractiePanelVak stapVak = new TekstInteractiePanelVak(vak, makeStepBox("-")); 
        vak.insert(stapVak.toCompleteString());
        final int index = i;
        vak.addMouseListener(new MouseAdapter() {

          @Override
          public void mouseReleased(MouseEvent e) {
            System.out.println("remove " + index);
          }
          
        });
      }
      this.add(stappenVak);
      stappenVak.setBounds(0, 0, stappenVak.getWidth(), stappenVak.getHeight());
      stappenVak.initStappen();
      stappenVak.zetMaat();
      
      choiceLine = new JPanel();
      choiceLine.setLayout(null);
      choiceLine.setBounds(0, 150, width, 30);
      choiceLine.setOpaque(false);
      
      
      int currentX = 0;     
      JLabel actieLabel = new JLabel("+");
      actieLabel.setBounds(currentX, 0, labelWidth, labelHeight);
      actieLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
      actieLabel.setOpaque(true);
      actieLabel.setBackground(new Color(220,220,220));
      choiceLine.add(actieLabel);
      
      currentX += labelWidth + offset;
      keuzeVak = new StrategieKeuzeVak();
      keuzeVak.setParent(this);
      keuzeVak.setBounds(currentX, 2, width - currentX - 2 * buttonWidth - 2 * offset, 24); 
      choiceLine.add(keuzeVak);
      
            
      add(choiceLine);
       
	}
	
	public void refillChoiceLine()
	{
	  int width = choiceLine.getWidth();
	  keuzeVak.setSize(width - labelWidth - 2 * offset, keuzeVak.getHeight());
	  
	}
	
	private void makeStepsTVP(int number) 
    {
      Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
      
      String[][] teksten = new String[number][2];
      for(int i = 0; i < teksten.length; i++)
      {
        for(int j = 0; j < teksten[i].length; j++)
          teksten[i][j] = "";
      }
      double[] breedtes = new double[2];
      breedtes[0] = labelWidth;
      breedtes[1] = 100;
      
      double[] hoogtes = new double[number];
      for(int i = 0; i < hoogtes.length; i++)
        hoogtes[i] = 27;
      
      ipLaunchState.put("teksten", teksten);
      ipLaunchState.put("pasAanH", new Boolean(true));
      ipLaunchState.put("cellSpaceRow", new Integer(6));
      ipLaunchState.put("breedtes", breedtes);
      ipLaunchState.put("hoogtes", hoogtes);
             
      stappenVak = new TekstVakPanel();
      stappenVak.setSize(160, 4);
      stappenVak.setEditState(ipLaunchState);
      stappenVak.setEditable(false);
      stappenVak.addActionListener(this);
    }
	
	private Hashtable<String,Object> makeStepBox(String tekst) 
    {
	  Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
      ipLaunchState.put("tekst", tekst);
      ipLaunchState.put("bgColorZichtbaar", new Boolean(true));
      ipLaunchState.put("bgColor", new Color(220,220,220));
      ipLaunchState.put("anderFont", new Boolean(true));
      ipLaunchState.put("font", new Font("SansSerif", Font.BOLD, 14));
      ipLaunchState.put("pasAanH", new Boolean(true));
      ipLaunchState.put("centerH", new Boolean(true));
      ipLaunchState.put("centerV", new Boolean(true));
      ipLaunchState.put("bovenMarge", new Integer(4));
                 
      Hashtable<String,Object> launchData = new Hashtable<String,Object>();
      launchData.put("soortInteractiePanel", new Integer(9));
      launchData.put("setNr", new Integer(3));
      launchData.put("interactiePanelLaunchState", ipLaunchState);
      launchData.put("breedte", new Integer(52));
      
      return launchData;
    }
	
			
	@Override
    public InteractieEditPanel getEditPanel()
    {
        return new StrategieVakEditPanel();
    }
	
	public void makeStep(String text)
	{
	    stappenVak.maakStap(text, randomVars, randomValues);
	    zetMaat();
	}
	  
	public void addSelectedStep(int stepNr)
	{
	  selectedSteps.add(stepNr);
	}

  @Override
  public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) {

    this.randomVars = randomVars;
    this.randomValues = randomValues;
    int scoreMax = 10;
    boolean[] stepRequired = null;
    
    if(h.containsKey("maxScore"))
      scoreMax = ((Integer)h.get("scoreMax")).intValue();
    if(h.containsKey("stepRequired"))
      stepRequired = (boolean[]) h.get("stepRequired");
    this.scoreMax = scoreMax;
    this.stepRequired = stepRequired;
    keuzeVak.zetOpdracht(h, randomVars, randomValues);
    stappenVak.zetMaat();
    
  }

  @Override
  public void setState(Hashtable h) {
    if(h.containsKey("selectedSteps"))
      this.selectedSteps = (ArrayList<Integer>) h.get("selectedSteps");
    stappenVak.setState(h);
    zetMaat();
  }

  @Override
  public void setEditState(Hashtable h) {
    zetMaat();
  }

  @Override
  public Hashtable getState() {
    Hashtable h = stappenVak.getState();
    h.put("selectedSteps", selectedSteps);
    return h;
  }

  @Override
  public Hashtable getEditState() {
    
    return null;
  }

  @Override
  public void setBounds(int x, int y, int b, int h) {
    
    super.setBounds(x, y,  b,  h);
    int width = this.getWidth();
    choiceLine.setBounds(0, stappenVak.getHeight() + offset, width, choiceLine.getHeight());
    keuzeVak.setSize(width - labelWidth - 2 * offset, keuzeVak.getHeight());
    stappenVak.zetBreedte(width);
  }

  @Override
  public void wis() {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void zetMaat() {
    
    int newHeight = stappenVak.getHeight() + offset + choiceLine.getHeight();
    Container parent = this.getParent();
    if(parent != null)
    {   parent.setSize(this.getWidth(), newHeight);
        if(parent.getParent() != null)
          ((TekstRegel) (parent.getParent())).zetMaat(); 
    }
  }

  @Override
  public int getIpId() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int getScore() {
    
    if(isCorrect())
      return scoreMax;
    return 0;
//    
//    System.out.println("selectedSteps: " + selectedSteps.toString());
//    
//    if(stappenVak.isCorrect())
//      return scoreMax;
//    return 0;
  }

  @Override
  public int[][] getScoreObjectives() {
    return stappenVak.getScoreObjectives();
  }

  @Override
  public int getScoreMax() {
    return scoreMax;
  }

  @Override
  public boolean isCorrect() {
    //Requirement 1: stappenVak is correct
    if(stappenVak.isCorrect() && stepRequired != null)
    {
      boolean correct = true;
      //Requirement 2: all required steps are present
      for(int i = 0; i < stepRequired.length; i++)
      {
        if(stepRequired[i])
        {
          if(!selectedSteps.contains(i))
            correct = false;
        }
      }
      return correct;
    }
    return false;
        //stappenVak.isCorrect();
  }

  @Override
  public boolean isFout() {
    return stappenVak.isFout();
  }

  @Override
  public void zetMode(int mode) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void zetNagekeken(boolean b) {
    stappenVak.zetNagekeken(b);
  }

  @Override
  public void stop() {
    stappenVak.stop(); 
  }

  @Override
  public void start() {
    stappenVak.start();
  }

  @Override
  public void destroy() {
    stappenVak.destroy();
  }

  @Override
  public void opnieuw() {
    stappenVak.opnieuw();
  }

  @Override
  public void kijkNa() {
    stappenVak.kijkNa();
  }

  @Override
  public void kijkNa(int stapNr) {
    
    stappenVak.kijkNa(stapNr);
  }

//ActionProducer
  private ActionListener actionListener = null;

  public void addActionListener(ActionListener l)
  {
      actionListener = AWTEventMulticaster.add(actionListener, l);
  }

  public void removeActionListener(ActionListener l)
  {
      actionListener = AWTEventMulticaster.remove(actionListener, l);
  }

  public void produceAction(String command)
  {
      if (actionListener != null)
      {
          actionListener.actionPerformed(new ActionEvent(this, 0, command));
      }
  }

  public void produceThisAction(ActionEvent e)
  {
      if (actionListener != null)
      {
          actionListener.actionPerformed(e);
      }
  }

  //end ActionProducer

  @Override
  public void actionPerformed(ActionEvent e) {
 
    produceAction(e.getActionCommand());
  }
	
}
