package fi.wiskopdr.templatecomponents;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import fi.wiskopdr.InteractiePanelContainerIF;
import fi.wiskopdr.ObjectiveChoiceButton;
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.domainmodel.Constants;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.wiskopdr.tekstobjects.TekstVak;

public class DragDropGenerator_1 implements TComponentGenerator, ActionListener {

	private TekstInteractiePanelVak tipvEdit;
	private DragDropEditor_1 ddEditor;
	
	private JPopupMenu editChoice;
	private JMenuItem[] editChoiceItems;
	String[] editChoiceStrings = {WiskOpdr.rb.getString("TCOMP_edit"),
                                  WiskOpdr.rb.getString("TCOMP_addItem"),
                                  WiskOpdr.rb.getString("TCOMP_removeItem"),
                                  WiskOpdr.rb.getString("TCOMP_decompose")};
	
	public static int initialItemCount = 4;
	public static int initialItemWidth = 120;
	public static int initialItemHeight = 40;
	public static int initialRowSpace = 40;
	public static int initialDescrWidth = 20;
	
	private Hashtable<String,Object> initPreferences;
	private boolean decompose = false;
		
	public DragDropGenerator_1() {
		int itemCount = initialItemCount;
		int itemWidth = initialItemWidth;
		int itemHeight = initialItemHeight;
		int rowSpace = initialRowSpace;
		int descrWidth = initialDescrWidth;
		
		initPreferences = new Hashtable<String,Object>();
		initPreferences.put("itemCount", new Integer(itemCount));
		initPreferences.put("itemWidth", new Integer(itemWidth));
		initPreferences.put("itemHeight", new Integer(itemHeight));
		initPreferences.put("rowSpace", new Integer(rowSpace));
		initPreferences.put("descrWidth", new Integer(descrWidth));
		
		editChoice = new JPopupMenu();
		editChoiceItems = new JMenuItem[editChoiceStrings.length];
		for(int i=0 ; i<editChoiceStrings.length ; i++) {
			editChoiceItems[i] = new JMenuItem(editChoiceStrings[i]);
			editChoiceItems[i].addActionListener(this);
			editChoice.add(editChoiceItems[i]);
		}
	}
	
	public TekstInteractiePanelVak generateAndReturnComponent(TekstVak tekstVak) {
		String DDwidgetID = tekstVak.getXWidgetManager().getWidgetID();
		initPreferences.put("DDwidgetID", DDwidgetID);
		
		TekstInteractiePanelVak tComponent = new TekstInteractiePanelVak(tekstVak, makeTComponentLD(initPreferences));
		
		TekstVak tComponentTV = ((TekstVakPanel)tComponent.getInteractiePanel()).geefTekstVak(0, 0);	
		TekstInteractiePanelVak dragComponent = new TekstInteractiePanelVak(tComponentTV, makeDragComponentLD(initPreferences));
		
		TekstVak dragComponentTV0 = ((TekstVakPanel)dragComponent.getInteractiePanel()).geefTekstVak(0, 0);
		for(int i=0 ; i<initialItemCount ; i++) {
			TekstInteractiePanelVak dragTarget = new TekstInteractiePanelVak(dragComponentTV0, makeDragTargetLD(initPreferences,i,dragComponentTV0));
			dragComponentTV0.insert(dragTarget.toCompleteString());
		}
		for(int i=0 ; i<initialItemCount ; i++) {
			TekstInteractiePanelVak dragObject = new TekstInteractiePanelVak(dragComponentTV0, makeDragObjectLD(initPreferences,i,dragComponentTV0));
			dragComponentTV0.insert(dragObject.toCompleteString());
		}
		
		TekstVak dragComponentTV1 = ((TekstVakPanel)dragComponent.getInteractiePanel()).geefTekstVak(1, 0);
		TekstInteractiePanelVak descrList = new TekstInteractiePanelVak(dragComponentTV1, makeDescrListLD(initPreferences, dragComponentTV1));
		
		
		
		TekstInteractiePanelVak checkUnit = new TekstInteractiePanelVak(tComponentTV, makeCheckSleepUnitLD(initPreferences));
		
		dragComponentTV1.insert(descrList.toCompleteString());
		
		TekstVakPanel tvp = (TekstVakPanel)dragComponent.getInteractiePanel();
		tvp.setEditState(tvp.getEditState());
		
		tComponentTV.insert(dragComponent.toCompleteString());
		tComponentTV.insert("\n"+checkUnit.toCompleteString());
		
		return tComponent;
	}
	
	public void generateComponent(TekstVak tekstVak) {
		TekstInteractiePanelVak tComponent = generateAndReturnComponent(tekstVak);
		tekstVak.insertDups(tComponent.toCompleteString());
	}
	
	public void editComponent(TekstInteractiePanelVak oldTComponent, Hashtable<String,Object> preferences) {
		
		TekstVak tekstVak = oldTComponent.getTekstVak();
		int oldItemCount = ((Integer)((Hashtable)oldTComponent.getEditState().get("TComponentPreferences")).get("itemCount")).intValue();
		TekstVak oldTComponentTV = ((TekstVakPanel)oldTComponent.getInteractiePanel()).geefTekstVak(0,0);
		TekstInteractiePanelVak oldDragComponent = (TekstInteractiePanelVak)oldTComponentTV.geefInteractiePanels().elementAt(0);
		TekstVak oldDragComponentTV0 = ((TekstVakPanel)oldDragComponent.getInteractiePanel()).geefTekstVak(0, 0);
		TekstVak oldDragComponentTV1 = ((TekstVakPanel)oldDragComponent.getInteractiePanel()).geefTekstVak(1, 0);
		TekstInteractiePanelVak oldDescrList = (TekstInteractiePanelVak)oldDragComponentTV1.geefInteractiePanels().elementAt(0);
		
		TekstInteractiePanelVak tComponent = new TekstInteractiePanelVak(tekstVak, makeTComponentLD(preferences));
		
		TekstVak tComponentTV = ((TekstVakPanel)tComponent.getInteractiePanel()).geefTekstVak(0, 0);		
		TekstInteractiePanelVak dragComponent = new TekstInteractiePanelVak(tComponentTV, makeDragComponentLD(preferences));
		
		TekstVak dragComponentTV0 = ((TekstVakPanel)dragComponent.getInteractiePanel()).geefTekstVak(0, 0);
		int itemCount = ((Integer)preferences.get("itemCount")).intValue();
		for(int i=0 ; i<itemCount ; i++) {
			TekstInteractiePanelVak dragTarget = new TekstInteractiePanelVak(dragComponentTV0, makeDragTargetLD(preferences,i,dragComponentTV0));
			
			if(i<oldItemCount) {
				TekstInteractiePanelVak oldDragTarget = (TekstInteractiePanelVak)oldDragComponentTV0.geefInteractiePanels().elementAt(i);
				TekstVak oldDragTargetTV = ((TekstVakPanel)oldDragTarget.getInteractiePanel()).geefTekstVak(0, 0);
				if(oldDragTargetTV!=null) {
					String s = "";
					s = oldDragTargetTV.toCompleteString();
					if(s.charAt(s.length()-1)=='\n')
						s = s.substring(0,s.length()-1);
					TekstVak dragTargetTV = ((TekstVakPanel)dragTarget.getInteractiePanel()).geefTekstVak(0,0);
					dragTargetTV.insert(s);
				}
			}
			dragComponentTV0.insert(dragTarget.toCompleteString());
		}
		for(int i=0 ; i<itemCount ; i++) {
			TekstInteractiePanelVak dragObject = new TekstInteractiePanelVak(dragComponentTV0, makeDragObjectLD(preferences,i,dragComponentTV0));
			
			if(i<oldItemCount) {
				TekstInteractiePanelVak oldDragObject = (TekstInteractiePanelVak)oldDragComponentTV0.geefInteractiePanels().elementAt(oldItemCount+i);
				TekstVak oldDragObjectTV = ((TekstVakPanel)oldDragObject.getInteractiePanel()).geefTekstVak(0, 0);
				if(oldDragObjectTV!=null) {
					String s = "";
					s = oldDragObjectTV.toCompleteString();
					if(s.charAt(s.length()-1)=='\n')
						s = s.substring(0,s.length()-1);
					TekstVak dragObjectTV = ((TekstVakPanel)dragObject.getInteractiePanel()).geefTekstVak(0,0);
					dragObjectTV.insert(s);
				}
			}
			dragComponentTV0.insert(dragObject.toCompleteString());
		}
		
		TekstVak dragComponentTV1 = ((TekstVakPanel)dragComponent.getInteractiePanel()).geefTekstVak(1, 0);
		TekstInteractiePanelVak descrList = new TekstInteractiePanelVak(dragComponentTV1, makeDescrListLD(preferences, dragComponentTV1));
		
		for(int i=0 ; i<itemCount ; i++) {
			if(i<oldItemCount) {
				TekstVak oldDescrListTV = ((TekstVakPanel)oldDescrList.getInteractiePanel()).geefTekstVak(0,i);
				if(oldDescrListTV!=null) {
					String s = "";
					s = oldDescrListTV.toCompleteString();
					if(s.charAt(s.length()-1)=='\n')
						s = s.substring(0,s.length()-1);
					TekstVak descrListTV = ((TekstVakPanel)descrList.getInteractiePanel()).geefTekstVak(0,i);
					descrListTV.insert(s);
				}
			}
		}
		
		
		TekstInteractiePanelVak checkUnit = new TekstInteractiePanelVak(tComponentTV, makeCheckSleepUnitLD(preferences));
		
		dragComponentTV1.insert(descrList.toCompleteString());
		tComponentTV.insert(dragComponent.toCompleteString());
		tComponentTV.insert("\n"+checkUnit.toCompleteString());
		oldTComponent.setSelected(true);
		tekstVak.deleteSelection();
		tekstVak.insertDups(tComponent.toCompleteString());
	}
	
	public void decompose(TekstInteractiePanelVak oldComponent) {
		decompose = true;
		editComponent(oldComponent, (Hashtable<String,Object>)oldComponent.getEditState().get("TComponentPreferences"));
		decompose = false;
	}
	
	private Hashtable<String,Object> makeTComponentLD(Hashtable<String,Object> preferences) {
		boolean volledigeBreedte = true;
		int breedte = 500;
		String DDwidgetID = null;
		
		if(preferences.containsKey("volledigeBreedte")) volledigeBreedte = ((Boolean)preferences.get("volledigeBreedte")).booleanValue();
		if(preferences.containsKey("breedte")) breedte = ((Integer)preferences.get("breedte")).intValue();
		if(preferences.containsKey("DDwidgetID")) DDwidgetID = (String)preferences.get("DDwidgetID");
		
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		ipLaunchState.put("tekst", "");
		ipLaunchState.put("pasAanH", new Boolean(true));
		if(!decompose) ipLaunchState.put("templateModeEdit", new Boolean(true));
		if(!decompose) ipLaunchState.put("templateModeFill", new Boolean(true));
		
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("volledigeBreedte", new Boolean(volledigeBreedte));
		launchData.put("breedte", new Integer(breedte));
		if(DDwidgetID != null)
			launchData.put("DDwidgetID", DDwidgetID);
		
		if(!decompose) launchData.put("TComponent", "DragDrop_1");
		if(!decompose) launchData.put("TComponentPreferences", preferences);
		
		return launchData;
	}
	
	private Hashtable<String,Object> makeDragComponentLD(Hashtable<String,Object> preferences) {
		int descrWidth = ((Integer)preferences.get("descrWidth")).intValue();
		int itemCount = ((Integer)preferences.get("itemCount")).intValue();
		int rowSpace = ((Integer)preferences.get("rowSpace")).intValue();
		int itemHeight = ((Integer)preferences.get("itemHeight")).intValue();
		
		
		double[] breedtes = new double[1];
		for(int i=0 ; i<1 ; i++) {
			breedtes[i] = 100;
		} 
		
		double[] hoogtes = new double[2];
		//for(int i=0 ; i<1 ; i++) {
			hoogtes[0] = 2*itemHeight + 2*rowSpace;
			hoogtes[1] = descrWidth;
		//}
		
		String[][] teksten = new String[2][1];
		for(int i=0 ; i<2 ; i++) {
			for(int j=0 ; j<1 ; j++) {
				teksten[i][j] = "";
			}
		}
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		ipLaunchState.put("bovenMarge",new Integer(0));
		ipLaunchState.put("breedtes", breedtes);
		ipLaunchState.put("hoogtes", hoogtes);
		ipLaunchState.put("teksten", teksten);
		ipLaunchState.put("pasAanH", new Boolean(false));
		ipLaunchState.put("centerH", new Boolean(true));
		ipLaunchState.put("cellSpaceRow", new Integer(2));
		if(!decompose) ipLaunchState.put("templateModeEdit", new Boolean(true));
		if(!decompose) ipLaunchState.put("templateModeFill", new Boolean(true));
				
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("breedte", new Integer(300));
		launchData.put("hoogte", new Integer((int)(hoogtes[0] + hoogtes[1] + 2)));
		launchData.put("volledigeBreedte", new Boolean(true));
		
		return launchData;
	}
	
	private Hashtable<String,Object> makeDescrListLD(Hashtable<String,Object> preferences, TekstVak tv) {
		int itemCount = ((Integer)preferences.get("itemCount")).intValue();
		int rowSpace = ((Integer)preferences.get("rowSpace")).intValue();
		int itemWidth = ((Integer)preferences.get("itemWidth")).intValue();
		int descrWidth = ((Integer)preferences.get("descrWidth")).intValue();
		
		
		double[] breedtes = new double[itemCount];
		for(int i=0 ; i<itemCount ; i++) {
			breedtes[i] = (tv.getWidth()-2*itemCount)/itemCount;
		} 
		
		double[] hoogtes = new double[1];
		for(int i=0 ; i<1 ; i++) {
			hoogtes[i] = 1; 
		}
		
		String[][] teksten = new String[1][itemCount];
		for(int i=0 ; i<1 ; i++) {
			for(int j=0 ; j<itemCount ; j++) {
				teksten[i][j] = "";//"Description "+(i+1);
			}
		}
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		ipLaunchState.put("breedtes", breedtes);
		ipLaunchState.put("hoogtes", hoogtes);
		ipLaunchState.put("teksten", teksten);
		ipLaunchState.put("pasAanH", new Boolean(true));
		ipLaunchState.put("centerH", new Boolean(true));
		ipLaunchState.put("cellSpaceRow", new Integer(rowSpace));
		if(!decompose) ipLaunchState.put("templateModeEdit", new Boolean(true));
				
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("breedte", new Integer(300));
		//launchData.put("hoogte", new Integer(itemCount*(itemHeight + rowSpace) - rowSpace));
		launchData.put("volledigeBreedte", new Boolean(true));
		
		return launchData;
	}
	
	private Hashtable<String,Object> makeDragTargetLD(Hashtable<String,Object> preferences, int i, TekstVak tv) {
		int itemCount = ((Integer)preferences.get("itemCount")).intValue();
		int rowSpace = ((Integer)preferences.get("rowSpace")).intValue();
		int itemHeight = ((Integer)preferences.get("itemHeight")).intValue();
		int itemWidth = ((Integer)preferences.get("itemWidth")).intValue();
		
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		//ipLaunchState.put("tekst", "Drag target "+(i+1));
		ipLaunchState.put("bgColorZichtbaar", new Boolean(true));
		ipLaunchState.put("bgColor", new Color(210,210,210));
		ipLaunchState.put("fgColor", new Color(150,150,150));
		ipLaunchState.put("centerV", new Boolean(true));
		ipLaunchState.put("centerH", new Boolean(true));
		ipLaunchState.put("styleString", "target-object");
		if(!decompose) ipLaunchState.put("templateModeEdit", new Boolean(true));
		ipLaunchState.put("zwevend", new Boolean(true));
		ipLaunchState.put("sleepdoel", new Boolean(true));
		ipLaunchState.put("locationX", new Integer(i*(tv.getWidth()/itemCount)+tv.getWidth()/(2*itemCount)-itemWidth/2));
		ipLaunchState.put("locationY", new Integer(rowSpace/2 + itemHeight+rowSpace));
		ipLaunchState.put("ipId", new Integer(-1*(i+1)));
		ipLaunchState.put("anderFont", new Boolean(true));
		ipLaunchState.put("font", new Font("SansSerif", Font.BOLD, 14));
		
		
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("locationX", new Integer(i*(tv.getWidth()/itemCount)+tv.getWidth()/(2*itemCount)-itemWidth/2));
		launchData.put("locationY", new Integer(rowSpace/2 + itemHeight+rowSpace));
		launchData.put("breedte", new Integer(itemWidth));
		launchData.put("hoogte", new Integer(itemHeight));
		
		return launchData;
	}
	
	private Hashtable<String,Object> makeDragObjectLD(Hashtable<String,Object> preferences, int i, TekstVak tv) {
		int itemCount = ((Integer)preferences.get("itemCount")).intValue();
		int rowSpace = ((Integer)preferences.get("rowSpace")).intValue();
		int itemHeight = ((Integer)preferences.get("itemHeight")).intValue();
		int itemWidth = ((Integer)preferences.get("itemWidth")).intValue();
		
		int xPositie = Math.min(20+2*itemWidth, tv.getWidth()-itemWidth);
		
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		//ipLaunchState.put("tekst", "Drag object "+(i+1));
		ipLaunchState.put("bgColorZichtbaar", new Boolean(true));
		ipLaunchState.put("bgColor", new Color(240,240,240));
		ipLaunchState.put("randZichtbaar", new Boolean(true));
		ipLaunchState.put("randColor", new Color(150,150,150));
		ipLaunchState.put("centerV", new Boolean(true));
		ipLaunchState.put("centerH", new Boolean(true));
		ipLaunchState.put("styleString", "drag-object");
		if(!decompose) ipLaunchState.put("templateModeEdit", new Boolean(true));
		ipLaunchState.put("zwevend", new Boolean(true));
		ipLaunchState.put("sleepbaar", new Boolean(true));
		ipLaunchState.put("locationX", new Integer(i*(tv.getWidth()/itemCount)+tv.getWidth()/(2*itemCount)-itemWidth/2));
		ipLaunchState.put("locationY", new Integer(rowSpace/2));
		
		ipLaunchState.put("ipId", new Integer(i+1));
		
		
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("locationX", new Integer(i*(tv.getWidth()/itemCount)+tv.getWidth()/(2*itemCount)-itemWidth/2));
		launchData.put("locationY", new Integer(rowSpace/2));
		launchData.put("breedte", new Integer(itemWidth));
		launchData.put("hoogte", new Integer(itemHeight));
		
		return launchData;
	}
	
	private Hashtable<String,Object> makeCheckSleepUnitLD(Hashtable<String,Object> preferences) {
		boolean randomizePositions = false;
		boolean snapToTarget = true;
		int acceptedMarge = 10;
		boolean relocate = false;
		boolean view = false;
		int scoreMax = 0;
	    boolean logOption = false;
		String logID = "";
		boolean check = true;
		boolean teltMee = true;
		//boolean checkFormule = false;
		//String[] formuleStrings = null;
		String knopImageString = "";
		String crossWidgetId = null;
		
		int itemCount = ((Integer)preferences.get("itemCount")).intValue();
		if(preferences.containsKey("randomizePositions")) randomizePositions = ((Boolean)preferences.get("randomizePositions")).booleanValue();
		if(preferences.containsKey("snapToTarget")) snapToTarget = ((Boolean)preferences.get("snapToTarget")).booleanValue();
		if(preferences.containsKey("acceptedMarge")) acceptedMarge = ((Integer)preferences.get("acceptedMarge")).intValue();
		if(preferences.containsKey("relocate")) relocate = ((Boolean)preferences.get("relocate")).booleanValue();
		if(preferences.containsKey("view")) view = ((Boolean)preferences.get("view")).booleanValue();
		if(preferences.containsKey("scoreMax")) scoreMax = ((Integer)preferences.get("scoreMax")).intValue();
	    if(preferences.containsKey("logOption")) logOption = ((Boolean)preferences.get("logOption")).booleanValue();
		if(preferences.containsKey("logID")) logID = (String)preferences.get("logID");
		if(preferences.containsKey("check")) check = ((Boolean)preferences.get("check")).booleanValue();
		if(preferences.containsKey("teltMee")) teltMee = ((Boolean)preferences.get("teltMee")).booleanValue();
		//if(preferences.containsKey("checkFormule")) checkFormule = ((Boolean)preferences.get("checkFormule")).booleanValue();
		//if(preferences.containsKey("formuleStrings")) formuleStrings = (String[])preferences.get("formuleStrings");
		if(preferences.containsKey("knopImageString")) knopImageString = (String)preferences.get("knopImageString");
		if(preferences.containsKey("DDwidgetID")) crossWidgetId = (String)preferences.get("DDwidgetID");
		
		
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
        ipLaunchState.putAll(ObjectiveChoiceButton.copyEditState(preferences));
		ipLaunchState.put("aantalSleepObjects", new Integer(itemCount));
		ipLaunchState.put("aantalDoelObjects", new Integer(itemCount));
		ipLaunchState.put("randomizePositions", new Boolean(randomizePositions));
		ipLaunchState.put("snapToTarget", new Boolean(snapToTarget));
		ipLaunchState.put("acceptedMarge", new Integer(acceptedMarge));
		ipLaunchState.put("relocate",new Boolean(true));
		ipLaunchState.put("view",new Boolean(view));
		ipLaunchState.put("scoreMax", new Integer(scoreMax));
		ipLaunchState.put("logOption",new Boolean(logOption));
		ipLaunchState.put("logID",logID);
		ipLaunchState.put("check",new Boolean(check));
		ipLaunchState.put("teltMee",new Boolean(teltMee));
		//ipLaunchState.put("checkFormule",new Boolean(checkFormule));
		//ipLaunchState.put("formuleStrings", formuleStrings);
//		if("".equals(knopImageString))
//			ipLaunchState.put("knopImageString", "controleerknop");
//		else
			ipLaunchState.put("knopImageString", knopImageString);
		
				
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(16));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("breedte", new Integer(126));
		launchData.put("hoogte", new Integer(31));
		launchData.put("volledigeBreedte", new Boolean(false));
		if(crossWidgetId != null)
			launchData.put("crossWidgetId", crossWidgetId);
		return launchData;
	}
	
	public void edit(TekstInteractiePanelVak tipv) {
		tipvEdit = tipv;
		if(ddEditor!=null)
			ddEditor.dispose();
		ddEditor = new DragDropEditor_1(tipv.getTekstVak());
		ddEditor.addActionListener(this);
		
//		if(ddEditor==null) {
//			ddEditor = new DragDropEditor(tipv.getTekstVak());
//			ddEditor.addActionListener(this);
//		}
//		else {
//			ddEditor.setTekstVak(tipv.getTekstVak());
//		}
		Hashtable<String,Object> preferences = (Hashtable<String,Object>)tipv.getEditState().get("TComponentPreferences");
		ddEditor.setPreferences(preferences);
		//ddEditor.show();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(ddEditor) && e.getActionCommand().equals("ok")) {
			editComponent(tipvEdit, ddEditor.getPreferences());
		}
		if(e.getSource().equals(editChoiceItems[0])) {
			edit(tipvEdit);
		}
		if(e.getSource().equals(editChoiceItems[1])) {
			Hashtable<String,Object> p = (Hashtable<String,Object>)tipvEdit.getEditState().get("TComponentPreferences");
			Hashtable preferences = (Hashtable)p.clone();
			int itemCount = ((Integer)preferences.get("itemCount")).intValue();
			itemCount++;
			preferences.put("itemCount", new Integer(itemCount));
			editComponent(tipvEdit, preferences);
		}
		if(e.getSource().equals(editChoiceItems[2])) {
			Hashtable<String,Object> p = (Hashtable<String,Object>)tipvEdit.getEditState().get("TComponentPreferences");
			Hashtable preferences = (Hashtable)p.clone();
			int itemCount = ((Integer)preferences.get("itemCount")).intValue();
			if(itemCount>1)itemCount--;
			preferences.put("itemCount", new Integer(itemCount));
			editComponent(tipvEdit, preferences);
		}
		if(e.getSource().equals(editChoiceItems[3])) {
			decompose(tipvEdit);
		}
	}

	public void editMenu(TekstInteractiePanelVak tipv) {
		editChoice.show(tipv,tipv.getWidth(),tipv.getHeight());
		tipvEdit = tipv;
	}
	
	public void closeEditMenu() {
		//editChoice.setVisible(false);
	}
}
