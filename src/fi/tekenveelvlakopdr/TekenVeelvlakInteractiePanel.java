package fi.tekenveelvlakopdr;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.JPanel;

//import fi.beans.tekstobjects.TekstArea;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class TekenVeelvlakInteractiePanel extends JPanel implements InteractiePanel
{
    private TekenVeelvlak tekenVeelvlak;
    Viewer3d viewer;
    VaktekPanel vaktek;
    
    //private boolean viewOnly;
    
    boolean viewerOnly = false;
    boolean profilesOnly = false;
    
    boolean vlakkenKleurenOptie = false;
    boolean profielenKleurenOptie = true;
    //boolean viewerKleurenOptie = false;
    int aantalVlakkenRood = 0;
    
    static int MOVEABLE = 0;
    static int FRONTVIEW = 1;
    static int BACKVIEW = 2;
    static int TOPVIEW = 3;
    static int BOTTOMVIEW = 4;
    static int LEFTVIEW = 5;
    static int RIGHTVIEW = 6;
    
    int viewerPosition = 0;
    
    //int basisFiguur = 1;
    int aantalHulppunten = 0;
    boolean toonVooraanzichtPijl = false;

    Hashtable tvState;
    
    boolean correct;
    
    TekenVeelvlakInteractieEditPanel editMode = null;
    
    public TekenVeelvlakInteractiePanel()
    {
        setLayout(null);
        
        tekenVeelvlak = new TekenVeelvlak(this);
        tekenVeelvlak.setBounds(0,0,500,400);
        tekenVeelvlak.init();
        add(tekenVeelvlak);
        
        viewer = new Viewer3d(0,0,350,350);
        viewer.setVisible(false);
        add(viewer);
        
        vaktek = new VaktekPanel(0,0,384,254);
        vaktek.setVisible(false);
        add(vaktek);
    }
    
    public void setBackground(Color c)
    {
        super.setBackground(c);
        if (tekenVeelvlak!=null)
        	tekenVeelvlak.setBackground(c);
        if (viewer!=null)
        	viewer.setBackground(c);
    }
    
    public void addActionListener(ActionListener al) {
        // TODO Auto-generated method stub
        
    }

    
    public void destroy() {
        // TODO Auto-generated method stub
        
    }

    
    public int geefAsHoogte() {
        // TODO Auto-generated method stub
        return 0;
    }

    
    public InteractieEditPanel getEditPanel() 
    {
        return new TekenVeelvlakInteractieEditPanel();
    }

    
    public Hashtable getEditState() 
    {
    	Hashtable tvState = new Hashtable();
    	
    	//if (viewerOnly)
    	//	viewer.getState();
    	//else	
    		tvState = tekenVeelvlak.getState();
    	int basisFiguur = tekenVeelvlak.geefBasisFiguur();
    	
    	Hashtable h = new Hashtable();
    	
        h.put("tvState", tvState);
        h.put("viewerOnly", new Boolean(viewerOnly));
        h.put("profilesOnly", new Boolean(profilesOnly));
        h.put("viewerPosition", new Integer(viewerPosition));
        h.put("basisFiguur", new Integer(basisFiguur));
        h.put("aantalHulppunten", new Integer(aantalHulppunten));
        h.put("toonVooraanzichtPijl", new Boolean(toonVooraanzichtPijl));
        
        h.put("vlakkenKleurenOptie", new Boolean(vlakkenKleurenOptie));
        h.put("profielenKleurenOptie", new Boolean(profielenKleurenOptie));
        //h.put("viewerKleurenOptie", new Boolean(viewerKleurenOptie));
        
        return h;
    }

    public void setViewerOnly(boolean b)
    {	viewerOnly = b;
    	if (viewerOnly)
    	{	tekenVeelvlak.setVisible(false);
    		vaktek.setVisible(false);
    		viewer.setVisible(true);
    		tvState = tekenVeelvlak.getState();
    		viewer.setState(tvState);
    		viewer.muisAan = (viewerPosition == 0);
    		viewer.repaint();
    	}	
    	else
    	{	viewer.setVisible(false);
    		if (!profilesOnly)
    		{	tekenVeelvlak.setVisible(true);
				//viewer.setVisible(false);
    			tekenVeelvlak.setState(tvState);
				if (viewer != null)
				{	//tekenVeelvlak.setState(viewer.getState());
					tekenVeelvlak.zetBeginHoeken(viewer.geefDraaiX(), viewer.geefDraaiY());
				}
				tekenVeelvlak.repaint();
    		}	
    	}
    	
    }

    public void setProfilesOnly(boolean b)
    {	profilesOnly = b;
    	if (profilesOnly)
    	{	tekenVeelvlak.setVisible(false);
    		viewer.setVisible(false);
    		vaktek.setVisible(true);
       	 	if (tekenVeelvlak.tv != null)
       	 	{	vaktek.zetVeelvlak(tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv);
       	 	}
//       	 	else
//System.out.println("tvv.tv == null");       	 		
    		vaktek.repaint();
    	}	
    	else
    	{	vaktek.setVisible(false);
    		if (!viewerOnly)
    		{	
    			tekenVeelvlak.setVisible(true);
			
    		//viewer.setVisible(false);
    			//if (viewer.getState() != null)
    			//{	tekenVeelvlak.setState(viewer.getState());
    			//}
    			tekenVeelvlak.repaint();
    		}	
    	}
    	
    }

    public void zetVlakkenKleurenOptie(boolean b)
    {	vlakkenKleurenOptie = b;
    	tekenVeelvlak.kleurVlakKnop.setEnabled(b);
    	tekenVeelvlak.wisKleurKnop.setEnabled(b);
    	viewer.zetVlakkenKleurenOptie(b);
    	vaktek.zetVlakkenKleurenOptie(b);
    	zetProfielenKleurenOptie(profielenKleurenOptie);
    	
    }
    public void zetProfielenKleurenOptie(boolean b)
    {	profielenKleurenOptie = b;
//    	viewerKleurenOptie = !b;
    	boolean leerling = (editMode == null);
//System.out.println("tvip leerling = " + leerling);

    	viewer.zetProfielenKleurenOptie(b,leerling);
    	vaktek.zetProfielenKleurenOptie(b,leerling);
    	//viewer.zetViewerKleurenOptie(!b);
    	//vaktek.zetViewerKleurenOptie(!b);
    	
    }
    
/*    
    public void zetViewerKleurenOptie(boolean b)
    {	viewerKleurenOptie = b;
    	profielenKleurenOptie = !b;
    	//viewer.zetViewerKleurenOptie(b);
    	//vaktek.zetViewerKleurenOptie(b);
    	viewer.zetProfielenKleurenOptie(!b);
    	vaktek.zetProfielenKleurenOptie(!b);
    }
*/    
    
    public void zetViewerPosition(int vPosition)
    {	viewerPosition = vPosition;
    	viewer.zetViewerPosition(viewerPosition);  	
    	tekenVeelvlak.zetViewerPosition(viewer.viewerPosition, viewer.muisAan);
    }
    
    public void zetAantalHulppunten(int aantalHulppunten)
    {
    	this.aantalHulppunten = aantalHulppunten; 
//System.out.println("tvip zetAantalHulppunten = " + aantalHulppunten);

    	int basisFiguur = tekenVeelvlak.geefBasisFiguur();
    	tekenVeelvlak.zetBasis(basisFiguur,aantalHulppunten);
    	Hashtable h = tekenVeelvlak.getState();
    	tekenVeelvlak.setState(h);
    	
    	
    }

    public void toonVooraanzichtPijl(boolean b)
    {	toonVooraanzichtPijl = b;
    
//System.out.println("tvip tvpijl " + b);

    	if (toonVooraanzichtPijl)
    	{	Veelvlak tvPijl = maakPijl();
    		Veelvlak v3dPijl = maakPijl();
    		tekenVeelvlak.toonVoorkantPijl(tvPijl);
    		viewer.voegVooraanzichtPijlToe(v3dPijl);
    	}
    	else
    	{	tekenVeelvlak.toonVoorkantPijl(null);
    		viewer.verwijderVooraanzichtPijl();
    	}
    	
    	
    }
  
    // origineel
    //double[] hp = {0, -0.5, -0.85,	0.01, -0.5, -0.85,	0.01, -0.5, -0.65, 0, -0.5, -0.65,
	//		       -0.05, -0.5, -0.75,	 0.05, -0.5, -0.75};
	public Veelvlak maakPijl()
	{	
		
		double[] hp = {0, -0.6, -0.85,	0.01, -0.6, -0.85,	0.01, -0.6, -0.65, 0, -0.6, -0.65,
					   -0.05, -0.6, -0.75,	 0.05, -0.6, -0.75};
		int[] vl = {4,
					4,	0,1,2,3,
					4,	0,3,2,1,
					3,	3,4,5,
					3,	3,5,4};
		Veelvlak v = new Veelvlak(hp,vl);
		for (int i = 0; i < v.aantalVlakken; i++)
		{	v.vlakken[i].vulkleur="zwart";
		}
		return v;
	}

    
    
    
    
    
    public int getIpId() {
        // TODO Auto-generated method stub
        return 0;
    }

    
    public int getScore() {
        // TODO Auto-generated method stub
        return 0;
    }

    
    public int getScoreMax() {
        // TODO Auto-generated method stub
        return 0;
    }

    
    public boolean isCorrect() {
        // TODO Auto-generated method stub
        return true;
    }

    
    public boolean isFout() {
        // TODO Auto-generated method stub
        return false;
    }

    
    public void kijkNa() 
    {
    	if (vlakkenKleurenOptie && profielenKleurenOptie)
    	{
    		correct = vaktek.evalueer(aantalVlakkenRood);
    	}
    	else if (vlakkenKleurenOptie && !profielenKleurenOptie)
    	{
    		correct = viewer.evalueer();
    	}
        
    }

    
    public void kijkNa(int stapNr) 
    {
        correct = viewer.evalueer(stapNr);
        
    }

    
    public void opnieuw() {
        // TODO Auto-generated method stub
        
    }

    
    public void setBounds(int x, int y, int b, int h) 
    {
    	
        super.setBounds(x,y,b,h);
        tekenVeelvlak.setBounds(0,0,b,h);
        tekenVeelvlak.setButtonHeights(h/2-170);
        //houdt de viewer vierkant
        int vb = b - 150; 
        int vh = h; 
        int vs = Math.min(vb,vh);
        
        viewer.setBounds(0,0,vs,vs);
        
    }

    
    public void setEditState(Hashtable h) 
    {
//System.out.println("tvip setEditState");
//if (editMode == null)
//System.out.println("editMode == null");
//else
//System.out.println("editMode not null");	

    	
    	 tvState = new Hashtable();
         boolean viewerOnly = false;
         boolean profilesOnly = false;
         int viewerPosition = 0;
         int basisFiguur = 1;
         int aantalHulppunten = 0;
         
         boolean toonVooraanzichtPijl = false;
         
         boolean vlakkenKleurenOptie = false;
         boolean profielenKleurenOptie = true;
         
         int aantalVlakkenRood = 0;
         
         if (h.containsKey("tvState"))
         {	 tvState = (Hashtable)h.get("tvState");
//System.out.println("h contains tvState");         
         }
         if(h.containsKey("viewerOnly"))
        	 viewerOnly = ((Boolean)h.get("viewerOnly")).booleanValue();
         if(h.containsKey("profilesOnly"))
        	 profilesOnly = ((Boolean)h.get("profilesOnly")).booleanValue();
         
         if(h.containsKey("viewerPosition"))
         	 viewerPosition = ((Integer) h.get("viewerPosition")).intValue();
         if(h.containsKey("basisFiguur"))
        	 basisFiguur = ((Integer)h.get("basisFiguur")).intValue();
         if(h.containsKey("aantalHulppunten"))
        	 aantalHulppunten = ((Integer)h.get("aantalHulppunten")).intValue();
         
         if(h.containsKey("toonVooraanzichtPijl"))
        	 toonVooraanzichtPijl = ((Boolean)h.get("toonVooraanzichtPijl")).booleanValue();
         
         if(h.containsKey("vlakkenKleurenOptie"))
        	 vlakkenKleurenOptie = ((Boolean)h.get("vlakkenKleurenOptie")).booleanValue();
         if(h.containsKey("profielenKleurenOptie"))
        	 profielenKleurenOptie = ((Boolean)h.get("profielenKleurenOptie")).booleanValue();
         
         if (tvState.containsKey("aantalVlakkenRood"))
        	 aantalVlakkenRood = ((Integer) tvState.get("aantalVlakkenRood")).intValue();
         
         this.aantalVlakkenRood = aantalVlakkenRood;
         
         toonVooraanzichtPijl(toonVooraanzichtPijl);
         
         viewer.zetViewerPosition(viewerPosition);
         
         this.viewerOnly = viewerOnly;
         this.profilesOnly = profilesOnly;
         
         zetVlakkenKleurenOptie(vlakkenKleurenOptie);
         zetProfielenKleurenOptie(profielenKleurenOptie);
         
         tekenVeelvlak.zetKiesV(basisFiguur);
    	 tekenVeelvlak.zetBasis(basisFiguur,aantalHulppunten);
         tekenVeelvlak.setState(tvState);
         
         if (viewerOnly)
         {	 
//System.out.println("viewerOnly");

        	 tekenVeelvlak.setVisible(false);
        	 vaktek.setVisible(false);
        	 viewer.setVisible(true);
        	 viewer.setState(tvState);
         }
         else if (profilesOnly)
         {
        	 tekenVeelvlak.setVisible(false);
        	 viewer.setVisible(false);
        	 vaktek.setVisible(true);
       	 	 if (tekenVeelvlak.tv != null)
      	 	 {   vaktek.zetVeelvlak(tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv);
       	 	 }
       	 	 else
System.out.println("tvv.tv == null");       	 		
        	 
         }
         else
         {
        	 viewer.setVisible(false);
        	 vaktek.setVisible(false);
        	 tekenVeelvlak.setVisible(true);
        	 //tekenVeelvlak.zetKiesV(basisFiguur);
        	 //tekenVeelvlak.zetBasis(basisFiguur,aantalHulppunten);
	         //tekenVeelvlak.setState(tvState);
	         tekenVeelvlak.begin = true;
	         tekenVeelvlak.tekenOpnieuw();
	         
	         
         }
        
    }

    public void start() {
        //tekenVeelvlak.start();
        
    }

    
    public void stop() {
        tekenVeelvlak.stop();
        
    }

    
    public void wis() {
        // TODO Auto-generated method stub
        
    }

    
    public void zetMaat() {
        // TODO Auto-generated method stub
        
    }

    
    public void zetMode(int mode) {
        // TODO Auto-generated method stub
        
    }

    
    public void zetNagekeken(boolean b) {
        // TODO Auto-generated method stub
        
    }

    public Hashtable getState() 
    {
    	
System.out.println("tvip getState");

    	Hashtable h = tekenVeelvlak.getState();
    	double viewerDraaiX = viewer.geefDraaiX();
    	double viewerDraaiY = viewer.geefDraaiY();
    	h.put("viewerDraaiX", new Double(viewerDraaiX));
    	h.put("viewerDraaiY", new Double(viewerDraaiY));
    	String[] viewerKleuren = viewer.getViewerKleuren();
    	h.put("viewerKleuren", viewerKleuren);
    	String[] vaKleuren = vaktek.getVaKleuren();
    	h.put("vaKleuren", vaKleuren);
    	String[] raKleuren = vaktek.getRaKleuren();
    	h.put("raKleuren", raKleuren);
    	String[] laKleuren = vaktek.getLaKleuren();
    	h.put("laKleuren", laKleuren);
    	String[] baKleuren = vaktek.getBaKleuren();
    	h.put("baKleuren", baKleuren);
   		
    	
kijkNa();    	
System.out.println("correct = " + correct);

    	return h;
        
    }

    public void setState(Hashtable h) 
    {
//System.out.println("tvip setState");    	
    	tekenVeelvlak.setState(h);
    	double viewerDraaiX = ((Double) h.get("draaiX")).doubleValue();
    	double viewerDraaiY = ((Double) h.get("draaiY")).doubleValue();
    	if (h.containsKey("viewerDraaiX"))
    		viewerDraaiX = ((Double) h.get("viewerDraaiX")).doubleValue(); 
    	if (h.containsKey("viewerDraaiY"))
    		viewerDraaiY = ((Double) h.get("viewerDraaiY")).doubleValue();
    	if (viewerPosition == TekenVeelvlakInteractiePanel.MOVEABLE)
    		viewer.zetBeginHoeken(viewerDraaiX, viewerDraaiY);
    			
    	String[] viewerKleuren = null;
    	String[] vaKleuren = null;
    	String[] raKleuren = null;
    	String[] laKleuren = null;
    	String[] baKleuren = null;
    	if (h.containsKey("viewerKleuren"))
    		viewerKleuren = (String[]) h.get("viewerKleuren");
    	if (h.containsKey("vaKleuren"))
    		vaKleuren = (String[]) h.get("vaKleuren");
    	if (h.containsKey("raKleuren"))
    		raKleuren = (String[]) h.get("raKleuren");
    	if (h.containsKey("laKleuren"))
    		laKleuren = (String[]) h.get("laKleuren");
    	if (h.containsKey("baKleuren"))
    		baKleuren = (String[]) h.get("baKleuren");
    	
    	if (viewerOnly && vlakkenKleurenOptie && !profielenKleurenOptie)
    	{	viewer.setViewerKleuren(viewerKleuren);
    	}
    	else if (profilesOnly && vlakkenKleurenOptie && profielenKleurenOptie)	
    	{	vaktek.setVaKleuren(vaKleuren);
    		vaktek.setRaKleuren(raKleuren);
    		vaktek.setLaKleuren(laKleuren);
    		vaktek.setBaKleuren(baKleuren);
    	}
        
    }
    
    public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) 
    {
    	
//System.out.println("tvip zetOpdracht");
//if (editMode == null)
//System.out.println("editMode == null");
//else
//System.out.println("editMode not null");	

        Hashtable tvState = new Hashtable();
        boolean viewerOnly = false; 
        boolean profilesOnly = false;
        int viewerPosition = 0;
        int basisFiguur = 1;
        int aantalHulppunten = 0;
        
        boolean toonVooraanzichtPijl = false;
        
        boolean vlakkenKleurenOptie = false;
        boolean profielenKleurenOptie = true;
        
        if(h.containsKey("tvState"))
        	tvState = (Hashtable)h.get("tvState");
        if(h.containsKey("viewerOnly"))
        	viewerOnly = ((Boolean)h.get("viewerOnly")).booleanValue();
        if(h.containsKey("profilesOnly"))
        	profilesOnly = ((Boolean)h.get("profilesOnly")).booleanValue();
        if(h.containsKey("viewerPosition"))
        	viewerPosition = ((Integer)h.get("viewerPosition")).intValue();
        if(h.containsKey("basisFiguur"))
        	basisFiguur = ((Integer)h.get("basisFiguur")).intValue();
        if(h.containsKey("aantalHulppunten"))
        	aantalHulppunten = ((Integer)h.get("aantalHulppunten")).intValue();

        if(h.containsKey("toonVooraanzichtPijl"))
       	 toonVooraanzichtPijl = ((Boolean)h.get("toonVooraanzichtPijl")).booleanValue();
        
        if(h.containsKey("vlakkenKleurenOptie"))
       	 vlakkenKleurenOptie = ((Boolean)h.get("vlakkenKleurenOptie")).booleanValue();
        if(h.containsKey("profielenKleurenOptie"))
       	 profielenKleurenOptie = ((Boolean)h.get("profielenKleurenOptie")).booleanValue();

        if (tvState.containsKey("aantalVlakkenRood"))
       	 aantalVlakkenRood = ((Integer) tvState.get("aantalVlakkenRood")).intValue();
        
        this.aantalVlakkenRood = aantalVlakkenRood;
        
        toonVooraanzichtPijl(toonVooraanzichtPijl);
        
        this.viewerOnly = viewerOnly;
        this.profilesOnly = profilesOnly;

        zetVlakkenKleurenOptie(vlakkenKleurenOptie);
        zetProfielenKleurenOptie(profielenKleurenOptie);

        viewer.zetViewerPosition(viewerPosition);

        //tekenVeelvlak.zetKiesV(basisFiguur);
   	 	tekenVeelvlak.zetBasis(basisFiguur,aantalHulppunten);
        tekenVeelvlak.setState(tvState);
        tekenVeelvlak.rg.remove(tekenVeelvlak.kiesV);
        
        if (viewerOnly)
        {	tekenVeelvlak.setVisible(false);
   	 		vaktek.setVisible(false); 
        	viewer.setVisible(true);
	       	 
	       	viewer.setState(tvState);

        }
        else if (profilesOnly)
        {
       	 	tekenVeelvlak.setVisible(false);
       	 	viewer.setVisible(false);
       	 	vaktek.setVisible(true);
       	 	if (tekenVeelvlak.tv != null)
       	 	{	vaktek.zetVeelvlak(tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv);
       	 	}
       	 	else
System.out.println("tvv.tv == null");       	 		
        }

        else
        {
	       	viewer.setVisible(false);
	       	vaktek.setVisible(false);
	       	tekenVeelvlak.setVisible(true);
	       	//tekenVeelvlak.zetBasis(basisFiguur,aantalHulppunten);
	        //tekenVeelvlak.zetOpdracht(tvState,randomVars,randomValues);
	        tekenVeelvlak.begin = true;
	        tekenVeelvlak.tekenOpnieuw();
        }
        
    }

}
