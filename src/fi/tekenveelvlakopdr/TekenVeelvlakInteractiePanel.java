package fi.tekenveelvlakopdr;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.ArrayList;

import javax.swing.JPanel;


//import fi.beans.tekstobjects.TekstArea;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;

public class TekenVeelvlakInteractiePanel extends JPanel implements InteractiePanel
{
    TekenVeelvlak tekenVeelvlak;
    Viewer3d viewer;
    JPanel viewerPanel;
    VaktekPanel vaktek;
    
    //private boolean viewOnly;
    
    boolean viewerOnly = false;
    boolean profilesOnly = false;
    
    boolean vlakkenKleurenOptie = false;
    boolean profielenKleurenOptie = true;
    //boolean viewerKleurenOptie = false;
    int aantalVlakkenRood = 0;
    
    boolean kijkNaActief = false;
    boolean kijkDraaihoekNa = false;
    boolean kijkVlakkenNa = false;
        
    double docentDraaihoekX = 1e5d;
    double docentDraaihoekY = 1e5d;
    
    static int MOVEABLE = 0;
    static int FRONTVIEW = 1;
    static int BACKVIEW = 2;
    static int TOPVIEW = 3;
    static int BOTTOMVIEW = 4;
    static int LEFTVIEW = 5;
    static int RIGHTVIEW = 6;
    static int TEACHER = 7;
    
    // viewer
    int viewerPosition = 0;
    boolean viewerMuisAan;
    
    //int basisFiguur = 1;
    int aantalHulppunten = 0;
    boolean toonVooraanzichtPijl = false;

    Hashtable tvState;
    
    boolean correct;
    boolean nagekeken = false;
    
    TekenVeelvlakInteractieEditPanel editMode = null;
    
    public TekenVeelvlakInteractiePanel()
    {
        setLayout(null);
        
        tekenVeelvlak = new TekenVeelvlak(this);
        tekenVeelvlak.setBounds(0,0,500,400);
        tekenVeelvlak.init();
        add(tekenVeelvlak);
        
        viewerPanel = new JPanel();
        viewerPanel.setLayout(null);
        viewerPanel.setBackground(Color.white);
        viewerPanel.setBounds(0,0,350,350);
        viewer = new Viewer3d(0,0,350,350-25,this);
        viewerPanel.add(viewer);
        //viewer.setVisible(false);
        viewerPanel.setVisible(false);
        add(viewerPanel);
        
        vaktek = new VaktekPanel(0,0,384,254,this);
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
System.out.println("tvip getEditState");

		double viewerDraaiX = viewer.geefDraaiX();
		double viewerDraaiY = viewer.geefDraaiY();

		if ((editMode != null) && editMode.nakijkOptiesPanel.isVisible() && 
			 editMode.kijkDraaihoekNaCB.isSelected())
		{	viewerDraaiX = editMode.tvipDraaiX;
			viewerDraaiY = editMode.tvipDraaiY;
		
//let nog even op dat de muis aanstaat    		
		}


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
        
System.out.println("vp = " + viewerPosition);

        h.put("basisFiguur", new Integer(basisFiguur));
        h.put("aantalHulppunten", new Integer(aantalHulppunten));
        h.put("toonVooraanzichtPijl", new Boolean(toonVooraanzichtPijl));
        
        h.put("vlakkenKleurenOptie", new Boolean(vlakkenKleurenOptie));
        h.put("profielenKleurenOptie", new Boolean(profielenKleurenOptie));
        //h.put("viewerKleurenOptie", new Boolean(viewerKleurenOptie));
        
        h.put("kijkDraaihoekNa", new Boolean(kijkDraaihoekNa));
        h.put("docentDraaihoekX", new Double(docentDraaihoekX));
        h.put("docentDraaihoekY", new Double(docentDraaihoekY));
        
        h.put("kijkVlakkenNa", new Boolean(kijkVlakkenNa));
        
    	String[] viewerKleuren = viewer.getViewerKleuren();
    	if (viewerKleuren != null)
    	{	ArrayList<String> viewerKleurenAL = new ArrayList<String>();
    		for (int vk = 0; vk < viewerKleuren.length; vk++)
    			viewerKleurenAL.add(viewerKleuren[vk]);
    		h.put("viewerKleuren", viewerKleurenAL);
    	}	
    	
    	String[] vaKleuren = vaktek.getVaKleuren();
    	if (vaKleuren != null)
    	{	
    		ArrayList<String> vaKleurenAL = new ArrayList<String>();
    		for (int va = 0; va < vaKleuren.length; va++)
    			vaKleurenAL.add(vaKleuren[va]);
    		h.put("vaKleuren", vaKleurenAL);
    	}

    	String[] raKleuren = vaktek.getRaKleuren();
    	if (raKleuren != null)
    	{	
    		ArrayList<String> raKleurenAL = new ArrayList<String>();
    		for (int ra = 0; ra < raKleuren.length; ra++)
    			raKleurenAL.add(raKleuren[ra]);
    		h.put("raKleuren", raKleurenAL);
    	}
    	
    	String[] laKleuren = vaktek.getLaKleuren();
    	if (laKleuren != null)
    	{	
    		ArrayList<String> laKleurenAL = new ArrayList<String>();
    		for (int la = 0; la < laKleuren.length; la++)
    			laKleurenAL.add(laKleuren[la]);
    		h.put("laKleuren", laKleurenAL);
    	}
    	String[] baKleuren = vaktek.getBaKleuren();
    	if (baKleuren != null)
    	{	
    		ArrayList<String> baKleurenAL = new ArrayList<String>();
    		for (int ba = 0; ba < baKleuren.length; ba++)
    			baKleurenAL.add(baKleuren[ba]);
    		h.put("baKleuren", baKleurenAL);
    	}	
    	if (vlakkenKleurenOptie && !profielenKleurenOptie)
    	{	
    		for (int c = 0; c < viewerKleuren.length; c++)
    		{	 if (viewerKleuren[c].equals("roodoranje"))
    				aantalVlakkenRood++;
    		}	
    	} 
    	if (vlakkenKleurenOptie && profielenKleurenOptie)
    	{	
    		for (int c = 0; c < vaKleuren.length; c++)
	 	 	{	 if (vaKleuren[c].equals("roodoranje"))
	 		 		aantalVlakkenRood++;
	 	 	}
	 	 	for (int c = 0; c < raKleuren.length; c++)
	 	 	{	 if (raKleuren[c].equals("roodoranje"))
	 		 		aantalVlakkenRood++;
	 	 	}
	 	 	for (int c = 0; c < laKleuren.length; c++)
	 	 	{	if (laKleuren[c].equals("roodoranje"))
	 		 		aantalVlakkenRood++;
	 	 	}
	 	 	for (int c = 0; c < baKleuren.length; c++)
	 	 	{	if (baKleuren[c].equals("roodoranje"))
	 		 		aantalVlakkenRood++;
	 	 	}
	 	    aantalVlakkenRood = aantalVlakkenRood / 4;
    	 }
	 	 h.put("aantalVlakkenRood", aantalVlakkenRood);

System.out.println("avr = " + aantalVlakkenRood);	 	 
        return h;
    }

    public void resetColors()
    {
    	viewer.resetColors();
    	vaktek.resetColors();
    }
    public void setViewerOnly(boolean b)
    {	viewerOnly = b;
    	if (viewerOnly)
    	{	tekenVeelvlak.setVisible(false);
    		vaktek.setVisible(false);
    		profilesOnly = false;
    		//viewer.setVisible(true);
    		viewerPanel.setVisible(true);
    		tvState = tekenVeelvlak.getState();
    		viewer.setState(tvState);
    		viewer.muisAan = (viewerPosition == 0);
    		viewer.repaint();
    	}	
    	else
    	{	//viewer.setVisible(false);
    		viewerPanel.setVisible(false);
    		if (!profilesOnly)
    		{	tekenVeelvlak.setVisible(true);

    			tekenVeelvlak.setState(tvState);
				if ((viewer != null) && (viewerPosition == 0))
				{	//tekenVeelvlak.setState(viewer.getState());
					tekenVeelvlak.zetBeginHoeken(viewer.geefDraaiX(), viewer.geefDraaiY());
				}
				tekenVeelvlak.repaint();
    		}	
    	}
    	
    }
/*
    public void setDocentViewerOnly(boolean b, boolean muisAan)
    {	//viewerOnly = b;
    	if (b)
    	{	tekenVeelvlak.setVisible(false);
    		vaktek.setVisible(false);
    		viewer.setVisible(true);
    		tvState = tekenVeelvlak.getState();
    		viewer.setState(tvState);
    		viewer.muisAan = muisAan;
    		viewer.repaint();
    	}	
    	else
    	{	tekenVeelvlak.setVisible(false);
			viewer.setVisible(false);
			vaktek.setVisible(true);
   	 		if (tekenVeelvlak.tv != null)
   	 		{	vaktek.zetVeelvlak(tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv);
   	 		}
//   	 	else
//System.out.println("tvv.tv == null");       	 		
   	 		vaktek.repaint();
    			
    	}
    	
    }
*/    
    public void setProfilesOnly(boolean b)
    {	profilesOnly = b;
    	if (profilesOnly)
    	{	tekenVeelvlak.setVisible(false);
    		//viewer.setVisible(false);
    		viewerPanel.setVisible(false);
    		viewerOnly = false;
    		vaktek.setVisible(true);
       	 	if (tekenVeelvlak.tv != null)
       	 	{	vaktek.zetVeelvlak(tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv);
       	 	}
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
    	//tekenVeelvlak.kleurVlakKnop.setEnabled(b);
    	//tekenVeelvlak.wisKleurKnop.setEnabled(b);
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
    	viewerMuisAan = viewer.muisAan;
    	//tekenVeelvlak.zetViewerPosition(viewer.viewerPosition, viewer.muisAan);
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

    public void zetKijkDraaihoekNa(boolean b)
    {  	kijkDraaihoekNa = b;
    	kijkNaActief = kijkDraaihoekNa || kijkVlakkenNa;
    }
    
    public void zetKijkVlakkenNa(boolean b)
    {  	kijkVlakkenNa = b;
    	kijkNaActief = kijkDraaihoekNa || kijkVlakkenNa;
    }
    
    public void zetDocentDraaihoek(double ddhX, double ddhY)
    {
    	docentDraaihoekX = ddhX;
    	docentDraaihoekY = ddhY;
    	
    }
    
    public int getIpId() {
        // TODO Auto-generated method stub
        return 0;
    }

    
    public int getScore() {
        // TODO Auto-generated method stub
        return correct?10:0;
    }

    
    public int getScoreMax() {
        // TODO Auto-generated method stub
        return 10;
    }

    
    public boolean isCorrect() {
        // TODO Auto-generated method stub
    	if(vlakkenKleurenOptie && (profielenKleurenOptie && profilesOnly || !profielenKleurenOptie && !profilesOnly))
    	   return correct;
    	return true;//correct;
    }

    
    public boolean isFout() {
        // TODO Auto-generated method stub
        return !correct;
    }

    
    public void kijkNa() 
    {
    	if (!kijkNaActief)
    		return;
    	
    	if (vlakkenKleurenOptie && profielenKleurenOptie && profilesOnly)
    	{
    		correct = vaktek.evalueer(aantalVlakkenRood);
    		nagekeken = true;
    	}
    	else if (vlakkenKleurenOptie && !profielenKleurenOptie && !profilesOnly)
    	{
    		correct = viewer.evalueer();
    		nagekeken = true;
    	}
    	else if (kijkDraaihoekNa)
    	{
    		correct = viewer.evalueer(docentDraaihoekX, docentDraaihoekY);
    		nagekeken = true;
    	}
        
    }

    
    public void kijkNa(int stapNr) 
    {
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
        int vb = b; //b-150
        int vh = h; 
        int vs = Math.min(vb,vh);
        
        viewerPanel.setBounds(0,0,vs,vs);
        viewer.kijkNaPanel.setLocation(5, vs-25);
        viewer.setBounds(0,0,vs,vs-25);
        
    }

    public void viewerMuisLos()
    {
    	if (editMode != null)
    		editMode.viewerMuisLos();
    }
    
    public void setEditState(Hashtable h) 
    {
System.out.println("tvip setEditState");
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
         
         boolean kijkDraaihoekNa = false;
         boolean kijkVlakkenNa = false;
         double docentDraaihoekX = 0;
         double docentDraaihoekY = 0;
         
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
         if (h.containsKey("profielenKleurenOptie"))
        	 profielenKleurenOptie = ((Boolean)h.get("profielenKleurenOptie")).booleanValue();
         
         if (h.containsKey("kijkDraaihoekNa"))
         	kijkDraaihoekNa = ((Boolean)h.get("kijkDraaihoekNa")).booleanValue();
         if (h.containsKey("kijkVlakkenNa"))
         	kijkVlakkenNa = ((Boolean)h.get("kijkVlakkenNa")).booleanValue();
                 
         if (h.containsKey("docentDraaihoekX"))
         	docentDraaihoekX = ((Double) h.get("docentDraaihoekX")).doubleValue();
         if (h.containsKey("docentDraaihoekY"))
         	docentDraaihoekY = ((Double) h.get("docentDraaihoekY")).doubleValue();

        	ArrayList<String> viewerKleurenAL = new ArrayList<String>();
        	ArrayList<String> vaKleurenAL = new ArrayList<String>();
        	ArrayList<String> raKleurenAL = new ArrayList<String>();
        	ArrayList<String> laKleurenAL = new ArrayList<String>();
        	ArrayList<String> baKleurenAL = new ArrayList<String>();
        	if (h.containsKey("viewerKleuren"))
        		viewerKleurenAL = (ArrayList<String>) h.get("viewerKleuren");
        	if (h.containsKey("vaKleuren"))
        		vaKleurenAL = (ArrayList<String>) h.get("vaKleuren");
        	if (h.containsKey("raKleuren"))
        		raKleurenAL = (ArrayList<String>) h.get("raKleuren");
        	if (h.containsKey("laKleuren"))
        		laKleurenAL = (ArrayList<String>) h.get("laKleuren");
        	if (h.containsKey("baKleuren"))
        		baKleurenAL = (ArrayList<String>) h.get("baKleuren");
        	String[] viewerKleuren = new String[viewerKleurenAL.size()];
        	String[] vaKleuren = new String[vaKleurenAL.size()];
        	String[] raKleuren = new String[raKleurenAL.size()];
        	String[] laKleuren = new String[laKleurenAL.size()];
        	String[] baKleuren = new String[baKleurenAL.size()];
        	for (int vk = 0; vk < viewerKleurenAL.size(); vk++)
        		viewerKleuren[vk] = viewerKleurenAL.get(vk);
        	for (int va = 0; va < vaKleurenAL.size(); va++)
        		vaKleuren[va] = vaKleurenAL.get(va);
        	for (int ra = 0; ra < raKleurenAL.size(); ra++)
        		raKleuren[ra] = raKleurenAL.get(ra);
        	for (int la = 0; la < laKleurenAL.size(); la++)
        		laKleuren[la] = laKleurenAL.get(la);
        	for (int ba = 0; ba < baKleurenAL.size(); ba++)
        		baKleuren[ba] = baKleurenAL.get(ba);
         
        	
         this.kijkDraaihoekNa = kijkDraaihoekNa;
         this.kijkVlakkenNa = kijkVlakkenNa;
         kijkNaActief = kijkDraaihoekNa || kijkVlakkenNa;
         this.docentDraaihoekX = docentDraaihoekX;
         this.docentDraaihoekY = docentDraaihoekY;
         
         if (kijkDraaihoekNa)
        	 viewer.kijkNaPanel.setVisible(true);
         if (vlakkenKleurenOptie && !profielenKleurenOptie)
        	 viewer.kijkNaPanel.setVisible(true);
         if (vlakkenKleurenOptie && profielenKleurenOptie)
        	 vaktek.kijkNaPanel.setVisible(true);
         
/*         
     	 if (vlakkenKleurenOptie && !profielenKleurenOptie)
    	 {   
    	 	 for (int c = 0; c < viewerKleuren.length; c++)
    	 	 {	 if (viewerKleuren[c].equals("roodoranje"))
    	 			 aantalVlakkenRood++;
    	 	 }
    	 }
    	 else if (vlakkenKleurenOptie && profielenKleurenOptie)	
    	 {	 
    	 	 for (int c = 0; c < vaKleuren.length; c++)
    	 	 {	 if (vaKleuren[c].equals("roodoranje"))
    	 			 aantalVlakkenRood++;
    	 	 }
    	 	 for (int c = 0; c < raKleuren.length; c++)
    	 	 {	 if (raKleuren[c].equals("roodoranje"))
    	 			 aantalVlakkenRood++;
    	 	 }
    	 	 for (int c = 0; c < laKleuren.length; c++)
    	 	 {	 if (laKleuren[c].equals("roodoranje"))
    	 			 aantalVlakkenRood++;
    	 	 }
    	 	 for (int c = 0; c < baKleuren.length; c++)
    	 	 {	 if (baKleuren[c].equals("roodoranje"))
    	 			 aantalVlakkenRood++;
    	 	 }

    	 	 aantalVlakkenRood = aantalVlakkenRood / 4;  
    	 }

//System.out.println("avr = " + aantalVlakkenRood);

*/
         if (h.containsKey("aantalVlakkenRood"))
         	 aantalVlakkenRood = ((Integer) tvState.get("aantalVlakkenRood")).intValue();
         
         this.aantalVlakkenRood = aantalVlakkenRood;
         
         toonVooraanzichtPijl(toonVooraanzichtPijl);
         
         //zetViewerPosition(viewerPosition);
//System.out.println("vp = " + viewerPosition);         
         
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
        	 viewerPanel.setVisible(true);
        	 viewer.setState(tvState);
//HIER        	 
        	 zetViewerPosition(viewerPosition);
         }
         else if (profilesOnly)
         {
        	 tekenVeelvlak.setVisible(false);
        	 viewerPanel.setVisible(false);
        	 vaktek.setVisible(true);
       	 	 if (tekenVeelvlak.tv != null)
      	 	 {   vaktek.zetVeelvlak(tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv);
       	 	 }
       	 	 else
System.out.println("tvv.tv == null");       	 		
        	 
         }
         else
         {
        	 viewerPanel.setVisible(false);
        	 vaktek.setVisible(false);
        	 tekenVeelvlak.setVisible(true);
        	 //tekenVeelvlak.zetKiesV(basisFiguur);
        	 //tekenVeelvlak.zetBasis(basisFiguur,aantalHulppunten);
	         //tekenVeelvlak.setState(tvState);
	         tekenVeelvlak.begin = true;
	         tekenVeelvlak.tekenOpnieuw();
	         
	         
         }
         
         resetLeerlingKleuren(viewerKleuren);
         resetLeerlingKleuren(vaKleuren);
         resetLeerlingKleuren(raKleuren);
         resetLeerlingKleuren(laKleuren);
         resetLeerlingKleuren(baKleuren);
         
     	 if (vlakkenKleurenOptie && !profielenKleurenOptie)
    	 {   viewer.setViewerKleuren(viewerKleuren);
    	 	 viewer.aantalVlakkenRood = aantalVlakkenRood;
    	 	 viewer.zetDocentModus(false);
    	 	 vaktek.zetDocentModus(false);
    	 	 viewer.zetKlikAan(false);
    	 	 vaktek.zetKlikAan(false);
    	 }
    	 else if (vlakkenKleurenOptie && profielenKleurenOptie)	
    	 {	 vaktek.setVaKleuren(vaKleuren);
    		 vaktek.setRaKleuren(raKleuren);
    		 vaktek.setLaKleuren(laKleuren);
    		 vaktek.setBaKleuren(baKleuren);
    		 vaktek.va.aantalVlakkenRood = aantalVlakkenRood;
    		 vaktek.ra.aantalVlakkenRood = aantalVlakkenRood;
    		 vaktek.la.aantalVlakkenRood = aantalVlakkenRood;
    		 vaktek.ba.aantalVlakkenRood = aantalVlakkenRood;
    	 	 viewer.zetDocentModus(false);
    	 	 vaktek.zetDocentModus(false);
    	 	 viewer.zetKlikAan(false);
    	 	 vaktek.zetKlikAan(false);

    	 }	 
    }

    public void resetLeerlingKleuren(String[] kleuren)
    {
		for (int j = 0; j < kleuren.length; j++)
		{	if (kleuren[j].equals("roodoranjerood"))
			{	kleuren[j] = "roodoranje";
			}
			if (kleuren[j].equals("oranjerood"))
			{	kleuren[j] = "oranje";
			}
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

    
    public void zetMode(int mode) 
    {
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
    	if (viewerKleuren != null)
    	{	
    		ArrayList<String> viewerKleurenAL = new ArrayList<String>();
    		for (int vk = 0; vk < viewerKleuren.length; vk++)
    			viewerKleurenAL.add(viewerKleuren[vk]);
    		h.put("viewerKleuren", viewerKleurenAL);
    	}
    	
    	String[] vaKleuren = vaktek.getVaKleuren();
    	if (vaKleuren != null)
    	{
    		ArrayList<String> vaKleurenAL = new ArrayList<String>();
    		for (int va = 0; va < vaKleuren.length; va++)
    			vaKleurenAL.add(vaKleuren[va]);
    		h.put("vaKleuren", vaKleurenAL);
    	}

    	String[] raKleuren = vaktek.getRaKleuren();
    	if (raKleuren != null)
    	{
    		ArrayList<String> raKleurenAL = new ArrayList<String>();
    		for (int ra = 0; ra < raKleuren.length; ra++)
    			raKleurenAL.add(raKleuren[ra]);
    		h.put("raKleuren", raKleurenAL);
    	}

    	String[] laKleuren = vaktek.getLaKleuren();
    	if (laKleuren != null)
    	{
    		ArrayList<String> laKleurenAL = new ArrayList<String>();
    		for (int la = 0; la < laKleuren.length; la++)
    			laKleurenAL.add(laKleuren[la]);
    		h.put("laKleuren", laKleurenAL);
    	}
    	
    	String[] baKleuren = vaktek.getBaKleuren();
    	if (baKleuren != null)
    	{
    		ArrayList<String> baKleurenAL = new ArrayList<String>();
    		for (int ba = 0; ba < baKleuren.length; ba++)
    			baKleurenAL.add(baKleuren[ba]);
    		h.put("baKleuren", baKleurenAL);
    	}
    	
//kijkNa();    	
//System.out.println("get nagekeken = " + nagekeken);
    	h.put("nagekeken", new Boolean(nagekeken));
    	h.put("correct", new Boolean(correct));
    			
    			

    	return h;
        
    }

    public void setState(Hashtable h) 
    {
//System.out.println("tvip setState");    	
    	tekenVeelvlak.setState(h);
    	double viewerDraaiX = 20;
    	double viewerDraaiY = -30;
    	
    	if (h.containsKey("draaiX"))
    		viewerDraaiX = ((Double) h.get("draaiX")).doubleValue();
    	if (h.containsKey("draaiY"))
    		viewerDraaiY = ((Double) h.get("draaiY")).doubleValue();
    	if (h.containsKey("viewerDraaiX"))
    		viewerDraaiX = ((Double) h.get("viewerDraaiX")).doubleValue(); 
    	if (h.containsKey("viewerDraaiY"))
    		viewerDraaiY = ((Double) h.get("viewerDraaiY")).doubleValue();
    	
    	if (viewerPosition == TekenVeelvlakInteractiePanel.MOVEABLE)
    		viewer.zetBeginHoeken(viewerDraaiX, viewerDraaiY);
    			
    	//String[] viewerKleuren = null;
    	//String[] vaKleuren = null;
    	//String[] raKleuren = null;
    	//String[] laKleuren = null;
    	//String[] baKleuren = null;
    	ArrayList<String> viewerKleurenAL = new ArrayList<String>();
    	ArrayList<String> vaKleurenAL = new ArrayList<String>();
    	ArrayList<String> raKleurenAL = new ArrayList<String>();
    	ArrayList<String> laKleurenAL = new ArrayList<String>();
    	ArrayList<String> baKleurenAL = new ArrayList<String>();
    	if (h.containsKey("viewerKleuren"))
    		viewerKleurenAL = (ArrayList<String>) h.get("viewerKleuren");
    	if (h.containsKey("vaKleuren"))
    		vaKleurenAL = (ArrayList<String>) h.get("vaKleuren");
    	if (h.containsKey("raKleuren"))
    		raKleurenAL = (ArrayList<String>) h.get("raKleuren");
    	if (h.containsKey("laKleuren"))
    		laKleurenAL = (ArrayList<String>) h.get("laKleuren");
    	if (h.containsKey("baKleuren"))
    		baKleurenAL = (ArrayList<String>) h.get("baKleuren");
    	String[] viewerKleuren = new String[viewerKleurenAL.size()];
    	String[] vaKleuren = new String[vaKleurenAL.size()];
    	String[] raKleuren = new String[raKleurenAL.size()];
    	String[] laKleuren = new String[laKleurenAL.size()];
    	String[] baKleuren = new String[baKleurenAL.size()];
    	for (int vk = 0; vk < viewerKleurenAL.size(); vk++)
    		viewerKleuren[vk] = viewerKleurenAL.get(vk);
    	for (int va = 0; va < vaKleurenAL.size(); va++)
    		vaKleuren[va] = vaKleurenAL.get(va);
    	for (int ra = 0; ra < raKleurenAL.size(); ra++)
    		raKleuren[ra] = raKleurenAL.get(ra);
    	for (int la = 0; la < laKleurenAL.size(); la++)
    		laKleuren[la] = laKleurenAL.get(la);
    	for (int ba = 0; ba < baKleurenAL.size(); ba++)
    		baKleuren[ba] = baKleurenAL.get(ba);

    	if (viewerOnly && vlakkenKleurenOptie && !profielenKleurenOptie)
    	{	viewer.setViewerKleuren(viewerKleuren);
    	}
    	else if (profilesOnly && vlakkenKleurenOptie && profielenKleurenOptie)	
    	{	vaktek.setVaKleuren(vaKleuren);
    		vaktek.setRaKleuren(raKleuren);
    		vaktek.setLaKleuren(laKleuren);
    		vaktek.setBaKleuren(baKleuren);
    	}
    	
    	boolean nagekeken = false;
    	boolean correct = false;
    	if (h.containsKey("nagekeken"))
    		nagekeken = ((Boolean) h.get("nagekeken")).booleanValue();
    	if (h.containsKey("correct"))
    		correct = ((Boolean) h.get("correct")).booleanValue();
    	
    	this.nagekeken = nagekeken;
    	this.correct = correct; 
    	
    	if (kijkNaActief && nagekeken)
    	{
    		if (correct)
    		{	viewer.vinkjeLabel.setVisible(true);
    			vaktek.vinkjeLabel.setVisible(true);
    		}
    		else
    		{
    			viewer.kruisjeLabel.setVisible(true);
    			vaktek.kruisjeLabel.setVisible(true);
    		}
    	}
    	

 
    	
        
    }
    
    public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) 
    {
    	
System.out.println("tvip zetOpdracht");
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
        
        boolean kijkDraaihoekNa = false;
        boolean kijkVlakkenNa = false;
        double docentDraaihoekX = 0;
        double docentDraaihoekY = 0;
        
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
        
        if (h.containsKey("vlakkenKleurenOptie"))
       	 vlakkenKleurenOptie = ((Boolean)h.get("vlakkenKleurenOptie")).booleanValue();
        if (h.containsKey("profielenKleurenOptie"))
       	 profielenKleurenOptie = ((Boolean)h.get("profielenKleurenOptie")).booleanValue();

        if (h.containsKey("kijkDraaihoekNa"))
        	kijkDraaihoekNa = ((Boolean)h.get("kijkDraaihoekNa")).booleanValue();
        if (h.containsKey("kijkVlakkenNa"))
        	kijkVlakkenNa = ((Boolean)h.get("kijkVlakkenNa")).booleanValue();
                
        if (h.containsKey("docentDraaihoekX"))
        	docentDraaihoekX = ((Double) h.get("docentDraaihoekX")).doubleValue();
        if (h.containsKey("docentDraaihoekY"))
        	docentDraaihoekY = ((Double) h.get("docentDraaihoekY")).doubleValue();

        //hier nog de kleuren
        
        this.kijkDraaihoekNa = kijkDraaihoekNa;
        this.kijkVlakkenNa = kijkVlakkenNa;
        kijkNaActief = kijkDraaihoekNa || kijkVlakkenNa;
        this.docentDraaihoekX = docentDraaihoekX;
        this.docentDraaihoekY = docentDraaihoekY;

        if (kijkDraaihoekNa)
       	 viewer.kijkNaPanel.setVisible(true);
        if (vlakkenKleurenOptie && !profielenKleurenOptie)
       	 viewer.kijkNaPanel.setVisible(true);
        if (vlakkenKleurenOptie && profielenKleurenOptie)
       	 vaktek.kijkNaPanel.setVisible(true);

    	ArrayList<String> viewerKleurenAL = new ArrayList<String>();
    	ArrayList<String> vaKleurenAL = new ArrayList<String>();
    	ArrayList<String> raKleurenAL = new ArrayList<String>();
    	ArrayList<String> laKleurenAL = new ArrayList<String>();
    	ArrayList<String> baKleurenAL = new ArrayList<String>();
    	if (h.containsKey("viewerKleuren"))
    		viewerKleurenAL = (ArrayList<String>) h.get("viewerKleuren");
    	if (h.containsKey("vaKleuren"))
    		vaKleurenAL = (ArrayList<String>) h.get("vaKleuren");
    	if (h.containsKey("raKleuren"))
    		raKleurenAL = (ArrayList<String>) h.get("raKleuren");
    	if (h.containsKey("laKleuren"))
    		laKleurenAL = (ArrayList<String>) h.get("laKleuren");
    	if (h.containsKey("baKleuren"))
    		baKleurenAL = (ArrayList<String>) h.get("baKleuren");
    	String[] viewerKleuren = new String[viewerKleurenAL.size()];
    	String[] vaKleuren = new String[vaKleurenAL.size()];
    	String[] raKleuren = new String[raKleurenAL.size()];
    	String[] laKleuren = new String[laKleurenAL.size()];
    	String[] baKleuren = new String[baKleurenAL.size()];
    	for (int vk = 0; vk < viewerKleurenAL.size(); vk++)
    		viewerKleuren[vk] = viewerKleurenAL.get(vk);
    	for (int va = 0; va < vaKleurenAL.size(); va++)
    		vaKleuren[va] = vaKleurenAL.get(va);
    	for (int ra = 0; ra < raKleurenAL.size(); ra++)
    		raKleuren[ra] = raKleurenAL.get(ra);
    	for (int la = 0; la < laKleurenAL.size(); la++)
    		laKleuren[la] = laKleurenAL.get(la);
    	for (int ba = 0; ba < baKleurenAL.size(); ba++)
    		baKleuren[ba] = baKleurenAL.get(ba);
        
        if (h.containsKey("aantalVlakkenRood"))
       	 	aantalVlakkenRood = ((Integer) h.get("aantalVlakkenRood")).intValue();
        
//System.out.println("avr = " + aantalVlakkenRood);        
        this.aantalVlakkenRood = aantalVlakkenRood;

        toonVooraanzichtPijl(toonVooraanzichtPijl);
        
        this.viewerOnly = viewerOnly;
        this.profilesOnly = profilesOnly;

        zetVlakkenKleurenOptie(vlakkenKleurenOptie);
        zetProfielenKleurenOptie(profielenKleurenOptie);

        //viewer.zetViewerPosition(viewerPosition);

        //tekenVeelvlak.zetKiesV(basisFiguur);
   	 	tekenVeelvlak.zetBasis(basisFiguur,aantalHulppunten);
        tekenVeelvlak.setState(tvState);
        tekenVeelvlak.rg.remove(tekenVeelvlak.kiesV);
        
        if (viewerOnly)
        {	tekenVeelvlak.setVisible(false);
   	 		vaktek.setVisible(false); 
        	viewerPanel.setVisible(true);
	       	 
	       	viewer.setState(tvState);
	       	zetViewerPosition(viewerPosition);

        }
        else if (profilesOnly)
        {
       	 	tekenVeelvlak.setVisible(false);
       	 	viewerPanel.setVisible(false);
       	 	vaktek.setVisible(true);
       	 	if (tekenVeelvlak.tv != null)
       	 	{	vaktek.zetVeelvlak(tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv);
       	 	}
//       	 	else
//System.out.println("tvv.tv == null");       	 		
        }

        else
        {
	       	viewerPanel.setVisible(false);
	       	vaktek.setVisible(false);
	       	tekenVeelvlak.setVisible(true);
	       	//tekenVeelvlak.zetBasis(basisFiguur,aantalHulppunten);
	        //tekenVeelvlak.zetOpdracht(tvState,randomVars,randomValues);
	        tekenVeelvlak.begin = true;
	        tekenVeelvlak.tekenOpnieuw();
        }

    	if (vlakkenKleurenOptie && !profielenKleurenOptie)
    	{   viewer.setViewerKleuren(viewerKleuren);
    		viewer.zetKlikAan(true);
   	 	 	viewer.aantalVlakkenRood = aantalVlakkenRood;
    	}
    	if ((vlakkenKleurenOptie && profielenKleurenOptie))
    	{
    		vaktek.setVaKleuren(vaKleuren);
    		vaktek.setRaKleuren(raKleuren);
    		vaktek.setLaKleuren(laKleuren);
    		vaktek.setBaKleuren(baKleuren);
    		vaktek.zetKlikAan(true);
    		vaktek.va.aantalVlakkenRood = aantalVlakkenRood;
   		 	vaktek.ra.aantalVlakkenRood = aantalVlakkenRood;
   		 	vaktek.la.aantalVlakkenRood = aantalVlakkenRood;
   		 	vaktek.ba.aantalVlakkenRood = aantalVlakkenRood;
    	}

    }

}
