package fi.wiskopdr;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fi.wiskopdr.formuleobjects.EditorContentPanel;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.formuleobjects.Tablet;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.beans.mainframe.AppletStub;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;

public class GrafiekTekenPanel extends JPanel 
                               implements  ActionListener, InteractiePanel, 
                                           InteractieEditPanel, WiskOpdrApplet
{   
    public boolean resized = true;
    

//  private ScrollPane scrollPane;
//  private EditorContentPanel contentPane; 
    
//  private int startY;
     
//  private FunctieEditor functieEditor;
    
    private GrafiekTekenEditor grafiekTekenEditor;
    private GrafiekComponent grafiekComponent;
    
    private Font font = WiskOpdr.tekstFont; 

//  private Tablet tablet;
//  private boolean tabletAdded;
//  private FormuleVakHouder tabletUser;
    
//  private boolean edit;
    
    private String[] randomVars;
    private Hashtable randomValues;
    
//  private boolean formulesZichtbaar = true;
    private boolean zoomOptie = true;
    private boolean traceOptie = true;
    private boolean dragOptie = true;
    private boolean isButton = false;
    
    private FormuleButton button;
    private JFrame frame;
    private JPanel basisPanel;
    
    
    
    public GrafiekTekenPanel()
    {   
        super();
        setLayout(null);
        setBackground(WiskOpdr.bgcolor);
//setBackground(Color.red);
        
        basisPanel = new JPanel();
        basisPanel.setLayout(null);
        basisPanel.setOpaque(false);
        add(basisPanel);
        
        grafiekComponent = new GrafiekComponent(0,0,270,270);
        grafiekComponent.setBackground(WiskOpdr.bgcolor);
        basisPanel.add(grafiekComponent);
                        
//      functieEditor = new FunctieEditor(true);
//      functieEditor.setBounds(50,270,getSize().width-50, 120);
//      functieEditor.zetRandverhoging(false);
//      basisPanel.add(functieEditor,0);

        grafiekTekenEditor = new GrafiekTekenEditor();
        grafiekTekenEditor.setBounds(50,270,getSize().width-50, 24);
        basisPanel.add(grafiekTekenEditor,0);

        grafiekTekenEditor.zetGrafiekComponent(grafiekComponent);
        grafiekComponent.zetGrafiekTekenEditor(grafiekTekenEditor);     

//      functieEditor.zetFuncties();
//      functieEditor.zetGrafiekComponent(grafiekComponent);
//      functieEditor.requestFocus();
        
        button = new FormuleButton("uitleg");
        button.setBounds(0,0,20,20);
        button.addActionListener(this);
        
        frame = new JFrame();
        
// fout bij run     
//      frame.setLayout(null);
frame.getContentPane().setLayout(null);

        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e)
            {   frame.setVisible(false);
            }
        });
        frame.addComponentListener(new ComponentAdapter(){
            public void componentResized(ComponentEvent e)
            {   int x = 0;//frame.getInsets().left;
                int y = 0;//frame.getInsets().top;
                int b = frame.getSize().width - frame.getInsets().left - frame.getInsets().right;
                int h = frame.getSize().height - frame.getInsets().top - frame.getInsets().bottom;
                basisPanel.setBounds(x,y,b,h);
                resize();
            }
        });
        
        
    }   
    
    public GrafiekTekenEditor getGrafiekTekenEditor()
    {   return grafiekTekenEditor;
    }   
    
    public void setButton(boolean b)
    {   isButton = b;
        grafiekComponent.setPopupView(b);
        if(b)
        {   add(button);
            int breedte = basisPanel.getSize().width + frame.getInsets().left + frame.getInsets().right;
            int hoogte = basisPanel.getSize().height + frame.getInsets().top + frame.getInsets().bottom;
            frame.setSize(breedte,hoogte);
            
//fout at run           
//          frame.add(basisPanel);
frame.getContentPane().add(basisPanel);
            
        }
        else
        {   remove(button);
            add(basisPanel);
            frame.setVisible(false);
        }
    }
    
    public void zetVarNaam(String varNaam)
    {   //functieEditor.zetVarNaam(varNaam);
        grafiekTekenEditor.zetVarNaam(varNaam);
    }
/*  
    public void zetFormulesZichtbaar(boolean b)
    {   formulesZichtbaar = b;
        functieEditor.setVisible(b);
        resize();
    }
*/  
    public void zetZoomOptie(boolean b)
    {   zoomOptie = b;
        resize();
    }
    
    public void zetTraceOptie(boolean b)
    {   traceOptie = b;
        grafiekComponent.setTrace(b);
        resize();
    }
    
    public void zetDragOptie(boolean b)
    {   dragOptie = b;
        grafiekComponent.setDrag(b);
        repaint();
    }
    
/*  
    public void zetTabletUser(FormuleVakHouder formuleVakHouder)
    {   if(tablet==null) return;
        tablet.zetFormuleVakHouder(formuleVakHouder);
        tabletUser = formuleVakHouder;
        
    }
*/
/*  
    public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y)
    {   if(tablet==null) 
        {   tablet = new Tablet(formuleVakHouder);
            tablet.setLocation(x,y);
            
        }
        tablet.zetFormuleVakHouder(formuleVakHouder);
        tabletUser = formuleVakHouder;
        
        
    }
*/
/*  
    public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y)
    {   if(tablet==null) 
        {   tablet = new Tablet(formuleVakHouder);
            
            
        }
        if(!tabletAdded)
        {   add(tablet,0);
            tablet.setLocation(x,y);
            tabletAdded = true;
            //resize();
            repaint();
        }
        tablet.zetFormuleVakHouder(formuleVakHouder);
    }
*/  
    public void actionPerformed(ActionEvent e)
    {   frame.setVisible(true);
        int b = basisPanel.getSize().width + frame.getInsets().left + frame.getInsets().right;
        int h = basisPanel.getSize().height + frame.getInsets().top + frame.getInsets().bottom;
        frame.setSize(b,h);
        frame.setVisible(true);
    }
    
    
/*   
    public void removeTablet()
    {   if(tablet==null)return;
        remove(tablet);
        //resize();
        repaint();
        tabletAdded = false;
    }
*/  
    public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues)
    {
        String varNaam = "x";
        boolean zoomOptie = true;
        boolean traceOptie = true;
        boolean dragOptie = true;
        boolean buttonOptie = false;
                
        if(h.containsKey("varNaam")) 
            varNaam = (String)h.get("varNaam");
        if(h.containsKey("zoomOptie")) 
            zoomOptie = ((Boolean)h.get("zoomOptie")).booleanValue();
        if(h.containsKey("traceOptie")) 
            traceOptie = ((Boolean)h.get("traceOptie")).booleanValue();
        if(h.containsKey("dragOptie")) 
            dragOptie = ((Boolean)h.get("dragOptie")).booleanValue();
        if(h.containsKey("buttonOptie")) 
            buttonOptie = ((Boolean)h.get("buttonOptie")).booleanValue();
        
        grafiekTekenEditor.zetVarNaam(varNaam);
        zetTraceOptie(traceOptie);
        zetZoomOptie(zoomOptie);
        zetDragOptie(dragOptie);
        setButton(buttonOptie);

        int breedte = basisPanel.getSize().width + frame.getInsets().left + frame.getInsets().right;
        int hoogte = basisPanel.getSize().height + frame.getInsets().top + frame.getInsets().bottom;
        if(buttonOptie)frame.setSize(breedte,hoogte);

        this.randomVars = randomVars;
        this.randomValues = randomValues;
        
        grafiekTekenEditor.zetOpdracht(h, randomVars, randomValues);
        
    }
    
    public void setState(Hashtable h)
    {
        grafiekTekenEditor.setState(h);
    }
    
    public void setEditState(Hashtable h)
    {
        String varNaam = "x";
        boolean zoomOptie = true;
        boolean traceOptie = true;
        boolean dragOptie = true;
        boolean buttonOptie = false;
                
        if(h.containsKey("varNaam")) 
            varNaam = (String)h.get("varNaam");
        if(h.containsKey("zoomOptie")) 
            zoomOptie = ((Boolean)h.get("zoomOptie")).booleanValue();
        if(h.containsKey("traceOptie")) 
            traceOptie = ((Boolean)h.get("traceOptie")).booleanValue();
        if(h.containsKey("dragOptie")) 
            dragOptie = ((Boolean)h.get("dragOptie")).booleanValue();
        if(h.containsKey("buttonOptie")) 
            buttonOptie = ((Boolean)h.get("buttonOptie")).booleanValue();
        
        grafiekTekenEditor.zetVarNaam(varNaam);     
        zetTraceOptie(traceOptie);
        zetZoomOptie(zoomOptie);
        zetDragOptie(dragOptie);
        setButton(buttonOptie);
        
        grafiekTekenEditor.setEditState(h);
    }
    
    public Hashtable getState()
    {
        return grafiekTekenEditor.getState();
    }
    
    public Hashtable getEditState()
    {
        Hashtable h = grafiekTekenEditor.getEditState();
        return h;
    }
    
    public InteractieEditPanel getEditPanel()
    {   return new GrafiekTekenEditPanel();
    }
        
    public void zetBreedte(int b)
    {   //grafiekPanel.setSize(b,grafiekPanel.getSize().height);
    }
    public void zetHoogte(int h)
    {   //grafiekPanel.setSize(grafiekPanel.getSize().width, h);
    }
    
    public void setBounds(int x, int y, int b, int h)
    {   if(isButton)
        {   super.setBounds(x,y,20,20);
            basisPanel.setBounds(0,0,b,h);
        }
        else 
        {   super.setBounds(x,y,b,h);
            basisPanel.setBounds(0,0,b,h);
        }
        resize();
    }
    
    public void setSize(int b, int h)
    {   if(isButton)
        {   super.setSize(20,20);
            basisPanel.setSize(b,h);
        }
        else 
        {   super.setSize(b,h);
            basisPanel.setSize(b,h);
        }
        resize();
    }
    
    public void resize()
    {   int gcX = 0;
        int gcY = zoomOptie?0:(-28);
        int gcB = basisPanel.getSize().width;
        //grafiekTekenEditor is altijd zichtbaar
        int gcH = basisPanel.getSize().height - 
            120 - (zoomOptie?0:(-28));
//          (formulesZichtbaar?120:0) - (zoomOptie?0:(-28));
//System.out.println("x = " + gcX + " y = " + gcY + " b = " + gcB + " h = " + gcH);                 
        grafiekComponent.setBounds(gcX,gcY,gcB,gcH);
//      int feX = isButton?0:40;
        int feX = isButton?0:20;
        int feY = gcY+gcH;
        //int feB = basisPanel.getSize().width - (isButton?0:60);
        int feB = basisPanel.getSize().width - (isButton?0:30);
        int feH = 24;//basisPanel.getSize().height - gcY - gcH;
        grafiekTekenEditor.setBounds(feX,feY,feB,feH);
// later afmetingen laten afhangen van:
// 1) multiple graphs
// 2) punten verbinden
//System.out.println("x = " + feX + " y = " + feY + " b = " + feB + " h = " + feH);     
    }
    
    public void wis(){}
    
    public void zetMaat(){}
    
    public int geefAsHoogte(){return 0;}
    
	public int getIpId(){return 0;}
	
	public String getIpExpString(){return null;}
    
    public int getScore(){return 0;}
    
    public int[][] getScoreObjectives()
	{	return null;
	}
    
    public int getScoreMax(){return 0;}
    
    public boolean isCorrect(){return true;}
    
    public boolean isFout(){return false;}
    
    public void zetMode(int mode){}
    
    public void zetNagekeken(boolean b){}
    
    public void stop(){if(frame!=null) frame.setVisible(false);}
    
    public void start(){}
    
    public void destroy(){}
    
    public void opnieuw(){}
    
    public void kijkNa(){}
    
    public void kijkNa(int stapNr){}
    
    public void addActionListener(ActionListener al){}
    
    public InteractiePanel getInteractiePanel()
    {
        return this;
    }

    @Override
    public void setStub(AppletStub stub) {
    }
    
    
    
    }