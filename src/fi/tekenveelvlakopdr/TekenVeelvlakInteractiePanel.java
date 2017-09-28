package fi.tekenveelvlakopdr;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Vector;

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
    boolean checkExternalDraaihoek = false;
    boolean checkExternalVlakken = false;
        
    double docentDraaihoekX = 1e5d;
    double docentDraaihoekY = 1e5d;
    
    String[] docentKleuren;
    String[] viewerKleuren; 
    
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
    
	private boolean nagekeken;
	private int mode;
	boolean correct = false;
	boolean fout = false;
	boolean ingevuld = false;
	
	int score = 0;
    int scoreMax = 10;
    
    TekenVeelvlakInteractieEditPanel editMode = null;
    
    Vector listeners = new Vector();
    
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
    
    public void addActionListener(ActionListener al) 
    {
    	listeners.addElement(al);
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
		double viewerDraaiX = viewer.geefDraaiX();
		double viewerDraaiY = viewer.geefDraaiY();

		if ((editMode != null) && editMode.nakijkOptiesPanel.isVisible() && 
			 editMode.kijkDraaihoekNaCB.isSelected() && !editMode.dezeDraaihoekRB.isSelected())
		{
			viewerDraaiX = editMode.tvipDraaiX;
			viewerDraaiY = editMode.tvipDraaiY;
			
			//let nog even op dat de muis aanstaat    		
		}

		if ((editMode != null) && editMode.nakijkOptiesPanel.isVisible() && 
			 editMode.kijkVlakkenNaCB.isSelected() && editMode.tvipProfilesOnly)
		{	
			viewerOnly = false;
    		profilesOnly = true;
		}
		
    	Hashtable tvState = new Hashtable();
    	
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
//        h.put("profielenKleurenOptie", new Boolean(profielenKleurenOptie));
        //h.put("viewerKleurenOptie", new Boolean(viewerKleurenOptie));
        
        h.put("kijkDraaihoekNa", new Boolean(kijkDraaihoekNa));
        h.put("checkExternalDraaihoek", new Boolean(checkExternalDraaihoek));
        h.put("docentDraaihoekX", new Double(viewerDraaiX));
        h.put("docentDraaihoekY", new Double(viewerDraaiY));
        
        h.put("kijkVlakkenNa", new Boolean(kijkVlakkenNa));
        h.put("checkExternalVlakken", new Boolean(checkExternalVlakken));
        
        if (docentKleuren != null)
        {	ArrayList<String> docentKleurenAL = new ArrayList<String>();
			for (int dk = 0; dk < docentKleuren.length; dk++)
				docentKleurenAL.add(docentKleuren[dk]);
			h.put("docentKleuren", docentKleurenAL);
        }
        
    	if (viewerKleuren != null)
    	{	ArrayList<String> viewerKleurenAL = new ArrayList<String>();
    		for (int vk = 0; vk < viewerKleuren.length; vk++)
    			viewerKleurenAL.add(viewerKleuren[vk]);
    		h.put("viewerKleuren", viewerKleurenAL);
    	}	


        return h;
    }

    public void resetColors()
    {
    	//viewer.resetColors();
    	//vaktek.resetColors();
    }
    public void setViewerOnly(boolean b)
    {	viewerOnly = b;
    	if (viewerOnly)
    	{    		
    		tekenVeelvlak.setVisible(false);
    		vaktek.setVisible(false);
    		//remove(vaktek);
    		profilesOnly = false;
    		//viewer.setVisible(true);
    		viewerPanel.setVisible(true);
    		tvState = tekenVeelvlak.getState();
    		viewer.setState(tvState);
    		viewer.muisAan = (viewerPosition == 0);
 
    		if (viewerKleuren != null)
    		{	viewer.zetKleuren(viewerKleuren);
//System.out.println("VO true vk != null");    		
    		}
    		else
    		{
    			viewerKleuren = tekenVeelvlak.getKleuren();
//System.out.println("VO true vk = null");    			
    		}
     		
    		viewer.repaint();
    		if (editMode != null)
    		{	editMode.showNakijkOpties(true);
    		
    			editMode.vlakkenKleurenCB.setEnabled(true);
    		}
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
				
				viewerKleuren = null;
				
				tekenVeelvlak.repaint();
				
	    		if (editMode != null)
	    		{
	    			editMode.showNakijkOpties(false);
	    		
	    			editMode.vlakkenKleurenCB.setEnabled(false);
	    		}
    		}	
    	}
    	
    }
    
    public void setProfilesOnly(boolean b)
    {
    	profilesOnly = b;
    	if (profilesOnly)
    	{
    		tekenVeelvlak.setVisible(false);
    	
    		tvState = tekenVeelvlak.getState();
    		//viewer.setVisible(false);
    		viewerPanel.setVisible(false);
    		viewerOnly = false;
    		vaktek.setVisible(true);
    		//add(vaktek);
       	 	if (tekenVeelvlak.tv != null)
       	 	{
       	 		vaktek.zetVeelvlak(tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv);
       	 	}
       	 	
    		if (viewerKleuren != null)
    			vaktek.setVaktekKleuren(viewerKleuren);
    		else
    			viewerKleuren = tekenVeelvlak.getKleuren();

    		vaktek.repaint();
    		if (editMode != null)
    		{
    			editMode.showNakijkOpties(true);
    		
    			editMode.vlakkenKleurenCB.setEnabled(true);
    		}

    	}	
    	else
    	{
    		vaktek.setVisible(false);
    		//remove(vaktek);
    		if (!viewerOnly)
    		{	
    			tekenVeelvlak.setVisible(true);
			
    			tekenVeelvlak.setState(tvState);
    			
    			viewerKleuren = null;
    			
    			tekenVeelvlak.repaint();
        		if (editMode != null)
        		{
        			editMode.showNakijkOpties(false);
        			editMode.vlakkenKleurenCB.setEnabled(false);	
        		}
    		}	
    	}
    }

    public void zetVlakkenKleurenOptie(boolean b)
    {
    	vlakkenKleurenOptie = b;
    	viewer.zetVlakkenKleurenOptie(b);
    	vaktek.zetVlakkenKleurenOptie(b);
    	viewer.zetKlikAan(b);
    	vaktek.zetKlikAan(b);
    	//zetProfielenKleurenOptie(profielenKleurenOptie);
    }
    
/*    
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
*/    
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
    {
    	viewerPosition = vPosition;
    	viewer.zetViewerPosition(viewerPosition);
    	viewerMuisAan = viewer.muisAan;
    	//tekenVeelvlak.zetViewerPosition(viewer.viewerPosition, viewer.muisAan);
    }
    
    public void zetAantalHulppunten(int aantalHulppunten)
    {
    	this.aantalHulppunten = aantalHulppunten; 

    	int basisFiguur = tekenVeelvlak.geefBasisFiguur();
    	tekenVeelvlak.zetBasis(basisFiguur,aantalHulppunten);
    	Hashtable h = tekenVeelvlak.getState();
    	tekenVeelvlak.setState(h);
    }

    public void toonVooraanzichtPijl(boolean b)
    {
    	toonVooraanzichtPijl = b;
    
    	if (toonVooraanzichtPijl)
    	{
    		Veelvlak tvPijl = maakPijl();
    		Veelvlak v3dPijl = maakPijl();
    		tekenVeelvlak.toonVoorkantPijl(tvPijl);
    		viewer.voegVooraanzichtPijlToe(v3dPijl);
    	}
    	else
    	{
    		tekenVeelvlak.toonVoorkantPijl(null);
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
		{
			v.vlakken[i].vulkleur="zwart";
		}
		return v;
	}

    public void zetKijkDraaihoekNa(boolean b)
    {
    	kijkDraaihoekNa = b;
    	kijkNaActief = isNakijkModus();
    }
    
    public void zetKijkVlakkenNa(boolean b)
    {
    	kijkVlakkenNa = b;
    	kijkNaActief = isNakijkModus();
    }
    
    public void zetCheckExternalDraaihoek(boolean b)
    {  	
    	checkExternalDraaihoek = b;
    	kijkNaActief = isNakijkModus();
    }
    
    public void zetCheckExternalVlakken(boolean b)
    {  	
    	checkExternalVlakken = b;
    	kijkNaActief = isNakijkModus();
    }
    
    private boolean isNakijkModus()
    {
    	boolean b = false;
    	
    	b = kijkDraaihoekNa || kijkVlakkenNa || checkExternalDraaihoek || checkExternalVlakken;
    	
    	return b;
    }
    
    private boolean isCheckExternalModus()
    {
    	boolean b = false;
    	
    	b = checkExternalDraaihoek || checkExternalVlakken;
    	
    	return b;
    }
    
    private boolean isVlakkenNakijkModus()
    {
    	boolean b = false;
    	
    	b = kijkVlakkenNa || checkExternalVlakken;
    	
    	return b;
    }
    
    private boolean isDraaihoekNakijkModus()
    {
    	boolean b = false;
    	
    	b = kijkDraaihoekNa || checkExternalDraaihoek;
    	
    	return b;
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

    
	public int getScore()
	{
		if (isNakijkModus())
			return score;
		return 0;
	}

    
	public int getScoreMax()
	{
		if (isNakijkModus())
			return scoreMax;
		return 0;
	}

    
    public boolean isCorrect() 
    {
    	//if (vlakkenKleurenOptie && (profielenKleurenOptie && profilesOnly || !profielenKleurenOptie && !profilesOnly))
    	if (isNakijkModus())
    	   return correct;
    	return true;
    }

    
	public boolean isFout()
	{	
		if (isNakijkModus())
			return fout;
		else
			return false;
	}

    public void answerChanged()
    {
    	if (isNakijkModus())
    	{
    		correct = false;
    		fout = false;
    		score = 0;
    		nagekeken = false;
    		ingevuld = true;
    		fireChangeEvent();
    	}	
    }

    
    public void kijkNa() 
    {
    	if (!isNakijkModus())
    		return;

    	//if (vlakkenKleurenOptie && profielenKleurenOptie && profilesOnly)
    	if (isVlakkenNakijkModus() && profilesOnly)
    	{
    		correct = vaktek.evalueer(docentKleuren);
    		fout = !correct;
    		if (correct)
    			score = scoreMax;
    		nagekeken = true;
    	}
    	else if (isVlakkenNakijkModus() && viewerOnly)
    	{
    		correct = viewer.evalueer(docentKleuren);
    		fout = !correct;
    		if (correct)
    			score = scoreMax;	
    		nagekeken = true;
    	}
    	else if (isDraaihoekNakijkModus())
    	{
    		correct = viewer.evalueer(docentDraaihoekX, docentDraaihoekY);
    		fout = !correct;
    		if (correct)
    			score = scoreMax;	
    		nagekeken = true;
    	}

    	if (correct)
		{
    		viewer.vinkjeLabel.setVisible(true);
			vaktek.vinkjeLabel.setVisible(true);
			viewer.kruisjeLabel.setVisible(false);
			vaktek.kruisjeLabel.setVisible(false);
		}
		else
		{
			viewer.kruisjeLabel.setVisible(true);
			vaktek.kruisjeLabel.setVisible(true);
			viewer.vinkjeLabel.setVisible(false);
			vaktek.vinkjeLabel.setVisible(false);
		}
    	
    	ingevuld = true;
		fireChangeEvent();
    }

    public void fireChangeEvent()
    {
    	ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "changed");
		for (int lCnt = 0; lCnt < listeners.size(); lCnt++)
		{
			((ActionListener) listeners.elementAt(lCnt)).actionPerformed(event);
		}
    }

    public void kijkNa(int stapNr) 
    {
    }

    
    public void opnieuw()
    {
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
        viewer.kijkNaPanel.setLocation((vs-120)/2, vs-25);
        viewer.setBounds(0,0,vs,vs-25);
    }

    public void viewerMuisLos()
    {
    	if (editMode != null)
    		editMode.viewerMuisLos();
    }
    
    public void setEditState(Hashtable h) 
    {
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
         boolean checkExternalDraaihoek = false;
         boolean checkExternalVlakken = false;
         double docentDraaihoekX = 0;
         double docentDraaihoekY = 0;
         
         if (h.containsKey("tvState"))
         {	
        	 tvState = (Hashtable)h.get("tvState");
         }
         if (h.containsKey("viewerOnly"))
        	 viewerOnly = ((Boolean)h.get("viewerOnly")).booleanValue();
         if (h.containsKey("profilesOnly"))
        	 profilesOnly = ((Boolean)h.get("profilesOnly")).booleanValue();
         
         if (h.containsKey("viewerPosition"))
         	 viewerPosition = ((Integer) h.get("viewerPosition")).intValue();
         if (h.containsKey("basisFiguur"))
        	 basisFiguur = ((Integer)h.get("basisFiguur")).intValue();
         if (h.containsKey("aantalHulppunten"))
        	 aantalHulppunten = ((Integer)h.get("aantalHulppunten")).intValue();
         
         if (h.containsKey("toonVooraanzichtPijl"))
        	 toonVooraanzichtPijl = ((Boolean)h.get("toonVooraanzichtPijl")).booleanValue();
         
         if (h.containsKey("vlakkenKleurenOptie"))
        	 vlakkenKleurenOptie = ((Boolean)h.get("vlakkenKleurenOptie")).booleanValue();
//         if (h.containsKey("profielenKleurenOptie"))
//        	 profielenKleurenOptie = ((Boolean)h.get("profielenKleurenOptie")).booleanValue();
         
		if (h.containsKey("kijkDraaihoekNa"))
			kijkDraaihoekNa = ((Boolean) h.get("kijkDraaihoekNa")).booleanValue();
		if (h.containsKey("kijkVlakkenNa"))
			kijkVlakkenNa = ((Boolean) h.get("kijkVlakkenNa")).booleanValue();
		if (h.containsKey("checkExternalDraaihoek"))
			checkExternalDraaihoek = ((Boolean) h.get("checkExternalDraaihoek")).booleanValue();
		if (h.containsKey("checkExternalVlakken"))
			checkExternalVlakken = ((Boolean) h.get("checkExternalVlakken")).booleanValue();

		if (h.containsKey("docentDraaihoekX"))
			docentDraaihoekX = ((Double) h.get("docentDraaihoekX")).doubleValue();
		if (h.containsKey("docentDraaihoekY"))
			docentDraaihoekY = ((Double) h.get("docentDraaihoekY")).doubleValue();

		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();

		ArrayList<String> docentKleurenAL = null;
		ArrayList<String> viewerKleurenAL = null;
		if (h.containsKey("docentKleuren"))
			docentKleurenAL = (ArrayList<String>) h.get("docentKleuren");
		if (h.containsKey("viewerKleuren"))
			viewerKleurenAL = (ArrayList<String>) h.get("viewerKleuren");
		if (docentKleurenAL != null)
		{
			docentKleuren = new String[docentKleurenAL.size()];
			for (int dk = 0; dk < docentKleurenAL.size(); dk++)
				docentKleuren[dk] = docentKleurenAL.get(dk);
		}
		if (viewerKleurenAL != null)
		{
			viewerKleuren = new String[viewerKleurenAL.size()];
			for (int vk = 0; vk < viewerKleurenAL.size(); vk++)
				viewerKleuren[vk] = viewerKleurenAL.get(vk);
		}

		this.kijkDraaihoekNa = kijkDraaihoekNa;
		this.kijkVlakkenNa = kijkVlakkenNa;
		this.checkExternalDraaihoek = checkExternalDraaihoek;
		this.checkExternalVlakken = checkExternalVlakken;
		kijkNaActief = isNakijkModus();
		this.docentDraaihoekX = docentDraaihoekX;
		this.docentDraaihoekY = docentDraaihoekY;

		if (isDraaihoekNakijkModus())
			viewer.kijkNaPanel.setVisible(true);
		if (vlakkenKleurenOptie && isVlakkenNakijkModus())// &&
															// !profielenKleurenOptie)
		{
			viewer.kijkNaPanel.setVisible(true);
			vaktek.kijkNaPanel.setVisible(true);
		}

		toonVooraanzichtPijl(toonVooraanzichtPijl);

		this.viewerOnly = viewerOnly;
		this.profilesOnly = profilesOnly;

		zetVlakkenKleurenOptie(vlakkenKleurenOptie);
		// zetProfielenKleurenOptie(profielenKleurenOptie);

		tekenVeelvlak.zetKiesV(basisFiguur);
		tekenVeelvlak.zetBasis(basisFiguur, aantalHulppunten);
		tekenVeelvlak.setState(tvState);

		viewer.setState(tvState);
		if (tekenVeelvlak.tv != null)
		{
			vaktek.zetVeelvlak(tekenVeelvlak.tv, tekenVeelvlak.tv, tekenVeelvlak.tv, tekenVeelvlak.tv);
		}

         if (viewerOnly)
         {
        	 tekenVeelvlak.setVisible(false);
        	 vaktek.setVisible(false);
        	 viewerPanel.setVisible(true);
        	 //viewer.setState(tvState);
        	 
        	 if (viewerKleuren != null)
        		 viewer.zetKleuren(viewerKleuren);
        	 zetViewerPosition(viewerPosition);
         }
         else if (profilesOnly)
         {
        	 tekenVeelvlak.setVisible(false);
        	 viewerPanel.setVisible(false);
        	 vaktek.setVisible(true);
       	 	 //if (tekenVeelvlak.tv != null)
      	 	 //{   vaktek.zetVeelvlak(tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv);
       	 	 //}

        	 if (viewerKleuren != null)
        		 vaktek.setVaktekKleuren(viewerKleuren);
         }
         else
         {
        	 viewerPanel.setVisible(false);
        	 vaktek.setVisible(false);
        	 tekenVeelvlak.setVisible(true);
	         tekenVeelvlak.begin = true;
	         tekenVeelvlak.tekenOpnieuw();
         }
         
// niet nodig?/          
         //resetLeerlingKleuren(viewerKleuren);
         
//         resetLeerlingKleuren(vaKleuren);
//         resetLeerlingKleuren(raKleuren);
//         resetLeerlingKleuren(laKleuren);
//         resetLeerlingKleuren(baKleuren);
         
     	 if (vlakkenKleurenOptie)// && !profielenKleurenOptie)
    	 {   
    	 	 viewer.zetDocentModus(false);
    	 	 vaktek.zetDocentModus(false);
    	 	 
    	 	 viewer.zetKleuren(viewerKleuren);
    	 	 vaktek.setVaktekKleuren(viewerKleuren);
    	 	 viewer.zetKlikAan(true);
    	 	 vaktek.zetKlikAan(true);
    	 }
    }

    public void resetLeerlingKleuren(String[] kleuren)
    {
    	
    	viewer.zetKleuren(kleuren);
    	vaktek.setVaktekKleuren(kleuren);
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

	public void zetKijkNaActief(boolean b)
	{
		kijkNaActief = b;
	}

    
    public void zetMode(int mode) 
    {
    	this.mode = mode;
    	if (kijkNaActief)    
    		zetKijkNaActief(mode == 0 || mode == 1);
    }

    
    public void zetNagekeken(boolean b) {
        // TODO Auto-generated method stub
        
    }

    public Hashtable getState() 
    {
    	Hashtable h = tekenVeelvlak.getState();
    	double viewerDraaiX = viewer.geefDraaiX();
    	double viewerDraaiY = viewer.geefDraaiY();
    	
    	h.put("viewerDraaiX", new Double(viewerDraaiX));
    	h.put("viewerDraaiY", new Double(viewerDraaiY));
    	
    	String[] leerlingKleuren = null;
    	
    	if (vlakkenKleurenOptie && viewerOnly)
    	{
    		leerlingKleuren = viewer.getKleuren();
    		ArrayList<String> leerlingKleurenAL = new ArrayList<String>();
    		for (int lk = 0; lk < leerlingKleuren.length; lk++)
    			leerlingKleurenAL.add(leerlingKleuren[lk]);
    		h.put("leerlingKleuren", leerlingKleurenAL);
    	}
    	
    	if (vlakkenKleurenOptie && profilesOnly)
    	{	leerlingKleuren = vaktek.getKleuren();
    		ArrayList<String> leerlingKleurenAL = new ArrayList<String>();
    		for (int lk = 0; lk < leerlingKleuren.length; lk++)
    			leerlingKleurenAL.add(leerlingKleuren[lk]);
    		h.put("leerlingKleuren", leerlingKleurenAL);
    	}
    	
    	h.put("nagekeken", new Boolean(nagekeken));
    	//h.put("correct", new Boolean(correct));
    	h.put("ingevuld", new Boolean(ingevuld));

    	return h;
    }

    public void setState(Hashtable h) 
    {
    	tekenVeelvlak.setState(h);
    	double viewerDraaiX = 20;
    	double viewerDraaiY = -30;
    	
    	if (h.containsKey("draaiX"))
    		viewerDraaiX = ((Number) h.get("draaiX")).doubleValue();
    	if (h.containsKey("draaiY"))
    		viewerDraaiY = ((Number) h.get("draaiY")).doubleValue();
    	if (h.containsKey("viewerDraaiX"))
    		viewerDraaiX = ((Number) h.get("viewerDraaiX")).doubleValue(); 
    	if (h.containsKey("viewerDraaiY"))
    		viewerDraaiY = ((Number) h.get("viewerDraaiY")).doubleValue();
    	
    	if (viewerPosition == TekenVeelvlakInteractiePanel.MOVEABLE)
    		viewer.zetBeginHoeken(viewerDraaiX, viewerDraaiY);
    			
    	ArrayList<String> leerlingKleurenAL = null; 
    	if (h.containsKey("leerlingKleuren"))
    		leerlingKleurenAL = (ArrayList<String>) h.get("leerlingKleuren");
    	
    	if (leerlingKleurenAL != null)
    	{	
    		String[] leerlingKleuren = new String[leerlingKleurenAL.size()];
    		for (int lk = 0; lk < leerlingKleurenAL.size(); lk++)
    			leerlingKleuren[lk] = leerlingKleurenAL.get(lk);

    		if (vlakkenKleurenOptie && viewerOnly)
    		{
    			viewer.setKleuren(leerlingKleuren);
    		}
    		if (vlakkenKleurenOptie && profilesOnly)	
    		{	vaktek.setVaKleuren(leerlingKleuren);
    			vaktek.setRaKleuren(leerlingKleuren);
    			vaktek.setLaKleuren(leerlingKleuren);
    			vaktek.setBaKleuren(leerlingKleuren);
    		}
    	}	
    	
    	ingevuld = false;
    	    	
    	if (h.containsKey("nagekeken"))
    		nagekeken = ((Boolean) h.get("nagekeken")).booleanValue();
    	if (h.containsKey("ingevuld"))
    		ingevuld = ((Boolean) h.get("ingevuld")).booleanValue();

    	if (ingevuld && (nagekeken || mode == 0))
    		kijkNa();
    }
    
    public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) 
    {
        Hashtable tvState = new Hashtable();
        boolean viewerOnly = false; 
        boolean profilesOnly = false;
        int viewerPosition = 0;
        int basisFiguur = 1;
        int aantalHulppunten = 0;
        
        boolean toonVooraanzichtPijl = false;
        
        boolean vlakkenKleurenOptie = false;
        
        boolean kijkDraaihoekNa = false;
        boolean kijkVlakkenNa = false;
        boolean checkExternalDraaihoek = false;
        boolean checkExternalVlakken = false;
        double docentDraaihoekX = 0;
        double docentDraaihoekY = 0;
        
		if (h.containsKey("tvState"))
			tvState = (Hashtable) h.get("tvState");
		if (h.containsKey("viewerOnly"))
			viewerOnly = ((Boolean) h.get("viewerOnly")).booleanValue();
		if (h.containsKey("profilesOnly"))
			profilesOnly = ((Boolean) h.get("profilesOnly")).booleanValue();
		if (h.containsKey("viewerPosition"))
			viewerPosition = ((Integer) h.get("viewerPosition")).intValue();
		if (h.containsKey("basisFiguur"))
			basisFiguur = ((Integer) h.get("basisFiguur")).intValue();
		if (h.containsKey("aantalHulppunten"))
			aantalHulppunten = ((Integer) h.get("aantalHulppunten")).intValue();

		if (h.containsKey("toonVooraanzichtPijl"))
			toonVooraanzichtPijl = ((Boolean) h.get("toonVooraanzichtPijl")).booleanValue();
        
		if (h.containsKey("vlakkenKleurenOptie"))
			vlakkenKleurenOptie = ((Boolean) h.get("vlakkenKleurenOptie")).booleanValue();
		if (h.containsKey("profielenKleurenOptie"))
			profielenKleurenOptie = ((Boolean) h.get("profielenKleurenOptie")).booleanValue();

        if (h.containsKey("kijkDraaihoekNa"))
        	kijkDraaihoekNa = ((Boolean)h.get("kijkDraaihoekNa")).booleanValue();
        if (h.containsKey("kijkVlakkenNa"))
        	kijkVlakkenNa = ((Boolean)h.get("kijkVlakkenNa")).booleanValue();
        if (h.containsKey("checkExternalDraaihoek"))
        	checkExternalDraaihoek = ((Boolean)h.get("checkExternalDraaihoek")).booleanValue();
        if (h.containsKey("checkExternalVlakken"))
        	checkExternalVlakken = ((Boolean)h.get("checkExternalVlakken")).booleanValue();
                
        if (h.containsKey("docentDraaihoekX"))
        	docentDraaihoekX = ((Double) h.get("docentDraaihoekX")).doubleValue();
        if (h.containsKey("docentDraaihoekY"))
        	docentDraaihoekY = ((Double) h.get("docentDraaihoekY")).doubleValue();
        
		if (h.containsKey("scoreMax"))
			scoreMax = ((Integer) h.get("scoreMax")).intValue();

        this.kijkDraaihoekNa = kijkDraaihoekNa;
        this.kijkVlakkenNa = kijkVlakkenNa;
        this.checkExternalDraaihoek = checkExternalDraaihoek;
        this.checkExternalVlakken = checkExternalVlakken;
        kijkNaActief = isNakijkModus();
        this.docentDraaihoekX = docentDraaihoekX;
        this.docentDraaihoekY = docentDraaihoekY;

        if (isDraaihoekNakijkModus())
       	 	viewer.kijkNaPanel.setVisible(true);
        if (vlakkenKleurenOptie && isVlakkenNakijkModus())
        {
        	viewer.kijkNaPanel.setVisible(true);
       	 	vaktek.kijkNaPanel.setVisible(true);
        }
        
        // kijknabutton verbergen bij extern controleren
        if (isCheckExternalModus())
        	viewer.setVisibleKijkNaButton(false);
        if (checkExternalVlakken)
        	vaktek.setVisibleKijkNaButton(false);
        	

    	ArrayList<String> docentKleurenAL = null;
    	ArrayList<String> viewerKleurenAL = null;
    	if (h.containsKey("docentKleuren"))
    		docentKleurenAL = (ArrayList<String>) h.get("docentKleuren");
    	if (h.containsKey("viewerKleuren"))
    		viewerKleurenAL = (ArrayList<String>) h.get("viewerKleuren");
    	if (docentKleurenAL != null)
    	{
    		docentKleuren = new String[docentKleurenAL.size()];
    		for (int dk = 0; dk < docentKleurenAL.size(); dk++)
    		{
    			docentKleuren[dk] = docentKleurenAL.get(dk);
    		}
    		
    	}
    	if (viewerKleurenAL != null)
    	{
    		viewerKleuren = new String[viewerKleurenAL.size()];
    		for (int vk = 0; vk < viewerKleurenAL.size(); vk++)
        		viewerKleuren[vk] = viewerKleurenAL.get(vk);
    	}

    	toonVooraanzichtPijl(toonVooraanzichtPijl);
        
        this.viewerOnly = viewerOnly;
        this.profilesOnly = profilesOnly;

        zetVlakkenKleurenOptie(vlakkenKleurenOptie);
        
   	 	tekenVeelvlak.zetBasis(basisFiguur,aantalHulppunten);
        tekenVeelvlak.setState(tvState);
        tekenVeelvlak.rg.remove(tekenVeelvlak.kiesV);
       	viewer.setState(tvState);
       	if (tekenVeelvlak.tv != null)
   	 	{
       		vaktek.zetVeelvlak(tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv);
   	 	}
        
        if (viewerOnly)
        {
        	tekenVeelvlak.setVisible(false);
   	 		vaktek.setVisible(false); 
        	viewerPanel.setVisible(true);
	       	 
	       	//viewer.setState(tvState);
	       	
	       	if (viewerKleuren != null)
	       		viewer.zetKleuren(viewerKleuren);
	       	
	       	zetViewerPosition(viewerPosition);
        }
        else if (profilesOnly)
        {
       	 	tekenVeelvlak.setVisible(false);
       	 	viewerPanel.setVisible(false);
       	 	vaktek.setVisible(true);
       	 	//if (tekenVeelvlak.tv != null)
       	 	//{	vaktek.zetVeelvlak(tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv,tekenVeelvlak.tv);
       	 	//}
	       	if (viewerKleuren != null)
	       		vaktek.setVaktekKleuren(viewerKleuren);
        }

        else
        {
	       	viewerPanel.setVisible(false);
	       	vaktek.setVisible(false);
	       	tekenVeelvlak.setVisible(true);
	        tekenVeelvlak.begin = true;
	        tekenVeelvlak.tekenOpnieuw();
        }

    	if (vlakkenKleurenOptie)// && !profielenKleurenOptie)
    	{   
   	 	 	viewer.zetDocentModus(false);
   	 	 	vaktek.zetDocentModus(false);
    		
    		viewer.zetKleuren(viewerKleuren);
    		vaktek.setVaktekKleuren(viewerKleuren);
    		viewer.zetKlikAan(true);
    		vaktek.zetKlikAan(true);
    	}
    	
    	ingevuld = false;
    }
    
    
}
