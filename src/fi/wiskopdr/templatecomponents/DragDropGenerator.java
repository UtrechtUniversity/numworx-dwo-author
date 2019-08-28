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
import fi.wiskopdr.TekstVakPanel;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.wiskopdr.tekstobjects.TekstVak;

public class DragDropGenerator implements TComponentGenerator, ActionListener {

	private TekstInteractiePanelVak tipvEdit;
	private DragDropEditor ddEditor;
	
	private JPopupMenu editChoice;
	private JMenuItem[] editChoiceItems;
	String[] editChoiceStrings = {WiskOpdr.rb.getString("TCOMP_edit"),
                                  WiskOpdr.rb.getString("TCOMP_addItem"),
                                  WiskOpdr.rb.getString("TCOMP_removeItem"),
                                  WiskOpdr.rb.getString("TCOMP_decompose")};
	
	public static int initialItemCount = 4;
	public static int initialItemWidth = 120;
	public static int initialItemHeight = 40;
	public static int initialRowSpace = 20;
	public static int initialDescrWidth = 100;
	
	private Hashtable<String,Object> initPreferences;
	private boolean decompose = false;
		
	public DragDropGenerator() {
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
	
	public void generateComponent(TekstVak tekstVak) {
		TekstInteractiePanelVak tComponent = new TekstInteractiePanelVak(tekstVak, makeTComponentLD(initPreferences));
		
		TekstVak tComponentTV = ((TekstVakPanel)tComponent.getInteractiePanel()).geefTekstVak(0, 0);	
		TekstInteractiePanelVak dragComponent = new TekstInteractiePanelVak(tComponentTV, makeDragComponentLD(initPreferences));
		
		TekstVak dragComponentTV0 = ((TekstVakPanel)dragComponent.getInteractiePanel()).geefTekstVak(0, 0);
		TekstInteractiePanelVak descrList = new TekstInteractiePanelVak(dragComponentTV0, makeDescrListLD(initPreferences));
		
		TekstVak dragComponentTV1 = ((TekstVakPanel)dragComponent.getInteractiePanel()).geefTekstVak(0, 1);
		for(int i=0 ; i<initialItemCount ; i++) {
			TekstInteractiePanelVak dragTarget = new TekstInteractiePanelVak(dragComponentTV1, makeDragTargetLD(initPreferences,i,dragComponentTV1));
			dragComponentTV1.insert(dragTarget.toCompleteString());
		}
		for(int i=0 ; i<initialItemCount ; i++) {
			TekstInteractiePanelVak dragObject = new TekstInteractiePanelVak(dragComponentTV1, makeDragObjectLD(initPreferences,i,dragComponentTV1));
			dragComponentTV1.insert(dragObject.toCompleteString());
		}
		
		TekstInteractiePanelVak checkUnit = new TekstInteractiePanelVak(tComponentTV, makeCheckSleepUnitLD(initPreferences));
		
		dragComponentTV0.insert(descrList.toCompleteString());
		
		TekstVakPanel tvp = (TekstVakPanel)dragComponent.getInteractiePanel();
		tvp.setEditState(tvp.getEditState());
		
		tComponentTV.insert(dragComponent.toCompleteString());
		tComponentTV.insert("\n\n"+checkUnit.toCompleteString());
		tekstVak.insertDups(tComponent.toCompleteString());
	}
	
	public void editComponent(TekstInteractiePanelVak oldTComponent, Hashtable<String,Object> preferences) {
		
		TekstVak tekstVak = oldTComponent.getTekstVak();
		int oldItemCount = ((Integer)((Hashtable)oldTComponent.getEditState().get("TComponentPreferences")).get("itemCount")).intValue();
		TekstVak oldTComponentTV = ((TekstVakPanel)oldTComponent.getInteractiePanel()).geefTekstVak(0,0);
		TekstInteractiePanelVak oldDragComponent = (TekstInteractiePanelVak)oldTComponentTV.geefInteractiePanels().elementAt(0);
		TekstVak oldDragComponentTV0 = ((TekstVakPanel)oldDragComponent.getInteractiePanel()).geefTekstVak(0, 0);
		TekstVak oldDragComponentTV1 = ((TekstVakPanel)oldDragComponent.getInteractiePanel()).geefTekstVak(0, 1);
		TekstInteractiePanelVak oldDescrList = (TekstInteractiePanelVak)oldDragComponentTV0.geefInteractiePanels().elementAt(0);
		
		TekstInteractiePanelVak tComponent = new TekstInteractiePanelVak(tekstVak, makeTComponentLD(preferences));
		
		TekstVak tComponentTV = ((TekstVakPanel)tComponent.getInteractiePanel()).geefTekstVak(0, 0);		
		TekstInteractiePanelVak dragComponent = new TekstInteractiePanelVak(tComponentTV, makeDragComponentLD(preferences));
		
		TekstVak dragComponentTV0 = ((TekstVakPanel)dragComponent.getInteractiePanel()).geefTekstVak(0, 0);
		TekstInteractiePanelVak descrList = new TekstInteractiePanelVak(dragComponentTV0, makeDescrListLD(preferences));
		
		int itemCount = ((Integer)preferences.get("itemCount")).intValue();
		for(int i=0 ; i<itemCount ; i++) {
			if(i<oldItemCount) {
				TekstVak oldDescrListTV = ((TekstVakPanel)oldDescrList.getInteractiePanel()).geefTekstVak(i, 0);
				String s = "";
				s = oldDescrListTV.toCompleteString();
				if(s.charAt(s.length()-1)=='\n')
					s = s.substring(0,s.length()-1);
				TekstVak descrListTV = ((TekstVakPanel)descrList.getInteractiePanel()).geefTekstVak(i,0);
				descrListTV.insert(s);
			}
		}
		
		TekstVak dragComponentTV1 = ((TekstVakPanel)dragComponent.getInteractiePanel()).geefTekstVak(0, 1);
		
		for(int i=0 ; i<itemCount ; i++) {
			TekstInteractiePanelVak dragTarget = new TekstInteractiePanelVak(dragComponentTV1, makeDragTargetLD(preferences,i,dragComponentTV1));
			
			if(i<oldItemCount) {
				TekstInteractiePanelVak oldDragTarget = (TekstInteractiePanelVak)oldDragComponentTV1.geefInteractiePanels().elementAt(i);
				TekstVak oldDragTargetTV = ((TekstVakPanel)oldDragTarget.getInteractiePanel()).geefTekstVak(0, 0);
				String s = "";
				s = oldDragTargetTV.toCompleteString();
				if(s.charAt(s.length()-1)=='\n')
					s = s.substring(0,s.length()-1);
				TekstVak dragTargetTV = ((TekstVakPanel)dragTarget.getInteractiePanel()).geefTekstVak(0,0);
				dragTargetTV.insert(s);
			}
			dragComponentTV1.insert(dragTarget.toCompleteString());
		}
		for(int i=0 ; i<itemCount ; i++) {
			TekstInteractiePanelVak dragObject = new TekstInteractiePanelVak(dragComponentTV1, makeDragObjectLD(preferences,i,dragComponentTV1));
			
			if(i<oldItemCount) {
				TekstInteractiePanelVak oldDragObject = (TekstInteractiePanelVak)oldDragComponentTV1.geefInteractiePanels().elementAt(oldItemCount+i);
				TekstVak oldDragObjectTV = ((TekstVakPanel)oldDragObject.getInteractiePanel()).geefTekstVak(0, 0);
				String s = "";
				s = oldDragObjectTV.toCompleteString();
				if(s.charAt(s.length()-1)=='\n')
					s = s.substring(0,s.length()-1);
				TekstVak dragObjectTV = ((TekstVakPanel)dragObject.getInteractiePanel()).geefTekstVak(0,0);
				dragObjectTV.insert(s);
			}
			dragComponentTV1.insert(dragObject.toCompleteString());
		}
		
		TekstInteractiePanelVak checkUnit = new TekstInteractiePanelVak(tComponentTV, makeCheckSleepUnitLD(preferences));
		
		dragComponentTV0.insert(descrList.toCompleteString());
		tComponentTV.insert(dragComponent.toCompleteString());
		tComponentTV.insert("\n\n"+checkUnit.toCompleteString());
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
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		ipLaunchState.put("tekst", "");
		ipLaunchState.put("pasAanH", new Boolean(true));
		if(!decompose) ipLaunchState.put("templateModeEdit", new Boolean(true));
		
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("volledigeBreedte", new Boolean(true));
		if(!decompose) launchData.put("TComponent", "DragDrop");
		if(!decompose) launchData.put("TComponentPreferences", preferences);
		
		return launchData;
	}
	
	private Hashtable<String,Object> makeDragComponentLD(Hashtable<String,Object> preferences) {
		int descrWidth = ((Integer)preferences.get("descrWidth")).intValue();
		int itemCount = ((Integer)preferences.get("itemCount")).intValue();
		int rowSpace = ((Integer)preferences.get("rowSpace")).intValue();
		int itemHeight = ((Integer)preferences.get("itemHeight")).intValue();
		
		
		double[] breedtes = new double[2];
		for(int i=0 ; i<2 ; i++) {
			breedtes[i] = descrWidth;
		} 
		
		double[] hoogtes = new double[1];
		for(int i=0 ; i<1 ; i++) {
			hoogtes[i] = itemCount*(itemHeight + rowSpace) + rowSpace; 
		}
		
		String[][] teksten = new String[1][2];
		for(int i=0 ; i<1 ; i++) {
			for(int j=0 ; j<2 ; j++) {
				teksten[i][j] = "";
			}
		}
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		ipLaunchState.put("bovenMarge",new Integer(rowSpace));
		ipLaunchState.put("breedtes", breedtes);
		ipLaunchState.put("hoogtes", hoogtes);
		ipLaunchState.put("teksten", teksten);
		ipLaunchState.put("pasAanH", new Boolean(true));
		ipLaunchState.put("cellSpaceColumn", new Integer(2));
		if(!decompose) ipLaunchState.put("templateModeEdit", new Boolean(true));
		if(!decompose) ipLaunchState.put("templateModeFill", new Boolean(true));
				
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("breedte", new Integer(300));
		launchData.put("hoogte", new Integer((int)hoogtes[0]));
		launchData.put("volledigeBreedte", new Boolean(true));
		
		return launchData;
	}
	
	private Hashtable<String,Object> makeDescrListLD(Hashtable<String,Object> preferences) {
		int itemCount = ((Integer)preferences.get("itemCount")).intValue();
		int rowSpace = ((Integer)preferences.get("rowSpace")).intValue();
		int itemHeight = ((Integer)preferences.get("itemHeight")).intValue();
		
		
		double[] breedtes = new double[1];
		for(int i=0 ; i<1 ; i++) {
			breedtes[i] = 1;
		} 
		
		double[] hoogtes = new double[itemCount];
		for(int i=0 ; i<itemCount ; i++) {
			hoogtes[i] = itemHeight; 
		}
		
		String[][] teksten = new String[itemCount][1];
		for(int i=0 ; i<itemCount ; i++) {
			for(int j=0 ; j<1 ; j++) {
				teksten[i][j] = "";//"Description "+(i+1);
			}
		}
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		ipLaunchState.put("breedtes", breedtes);
		ipLaunchState.put("hoogtes", hoogtes);
		ipLaunchState.put("teksten", teksten);
		ipLaunchState.put("pasAanH", new Boolean(false));
		ipLaunchState.put("centerV", new Boolean(true));
		ipLaunchState.put("cellSpaceRow", new Integer(rowSpace));
		if(!decompose) ipLaunchState.put("templateModeEdit", new Boolean(true));
				
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("breedte", new Integer(300));
		launchData.put("hoogte", new Integer(itemCount*(itemHeight + rowSpace) - rowSpace));
		launchData.put("volledigeBreedte", new Boolean(true));
		
		return launchData;
	}
	
	private Hashtable<String,Object> makeDragTargetLD(Hashtable<String,Object> preferences, int i, TekstVak tv) {
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
		ipLaunchState.put("styleString", "drag-target");
		if(!decompose) ipLaunchState.put("templateModeEdit", new Boolean(true));
		ipLaunchState.put("zwevend", new Boolean(true));
		ipLaunchState.put("sleepdoel", new Boolean(true));
		ipLaunchState.put("locationX", new Integer(20));
		ipLaunchState.put("locationY", new Integer(rowSpace + i*(itemHeight+rowSpace)));
		ipLaunchState.put("ipId", new Integer(-1*(i+1)));
		ipLaunchState.put("anderFont", new Boolean(true));
		ipLaunchState.put("font", new Font("SansSerif", Font.BOLD, 14));
		
		
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("locationX", new Integer(20));
		launchData.put("locationY", new Integer(rowSpace + i*(itemHeight+rowSpace)));
		launchData.put("breedte", new Integer(itemWidth));
		launchData.put("hoogte", new Integer(itemHeight));
		
		return launchData;
	}
	
	private Hashtable<String,Object> makeDragObjectLD(Hashtable<String,Object> preferences, int i, TekstVak tv) {
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
		ipLaunchState.put("locationX", new Integer(xPositie));
		ipLaunchState.put("locationY", new Integer(rowSpace + i*(itemHeight+rowSpace)));
		ipLaunchState.put("ipId", new Integer(i+1));
		
		
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(9));
		launchData.put("setNr", new Integer(3));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("locationX", new Integer(xPositie));
		launchData.put("locationY", new Integer(rowSpace + i*(itemHeight+rowSpace)));
		launchData.put("breedte", new Integer(itemWidth));
		launchData.put("hoogte", new Integer(itemHeight));
		
		return launchData;
	}
	
	private Hashtable<String,Object> makeCheckSleepUnitLD(Hashtable<String,Object> preferences) {
		int itemCount = ((Integer)preferences.get("itemCount")).intValue();
		
		boolean[] juisteSelecties = new boolean[itemCount];
		
		Hashtable<String,Object> ipLaunchState = new Hashtable<String,Object>();
		ipLaunchState.put("aantalSleepObjects", new Integer(itemCount));
		ipLaunchState.put("aantalDoelObjects", new Integer(itemCount));
		ipLaunchState.put("randomizePositions", new Boolean(true));
		ipLaunchState.put("snapToTarget", new Boolean(true));
		ipLaunchState.put("acceptedMarge", new Integer(15));
		ipLaunchState.put("relocate",new Boolean(true));
		ipLaunchState.put("knopImageString", "controleerknop");
				
		Hashtable<String,Object> launchData = new Hashtable<String,Object>();
		launchData.put("soortInteractiePanel", new Integer(16));
		launchData.put("interactiePanelLaunchState", ipLaunchState);
		launchData.put("breedte", new Integer(100));
		launchData.put("hoogte", new Integer(30));
		launchData.put("volledigeBreedte", new Boolean(true));
		return launchData;
	}
	
	public void edit(TekstInteractiePanelVak tipv) {
		tipvEdit = tipv;
		if(ddEditor==null) {
			ddEditor = new DragDropEditor(tipv.getTekstVak());
			ddEditor.addActionListener(this);
		}
		else {
			ddEditor.setTekstVak(tipv.getTekstVak());
		}
		Hashtable<String,Object> preferences = (Hashtable<String,Object>)tipv.getEditState().get("TComponentPreferences");
		ddEditor.setPreferences(preferences);
		ddEditor.show();
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
			if(ddEditor==null) {
				ddEditor = new DragDropEditor(tipvEdit.getTekstVak());
				ddEditor.addActionListener(this);
			}
			Hashtable<String,Object> h = (Hashtable<String,Object>)tipvEdit.getEditState().get("TComponentPreferences");
			ddEditor.setPreferences(h);
			Hashtable<String,Object> preferences = ddEditor.getPreferences();
			int itemCount = ((Integer)preferences.get("itemCount")).intValue();
			itemCount++;
			preferences.put("itemCount", new Integer(itemCount));
			editComponent(tipvEdit, preferences);
		}
		if(e.getSource().equals(editChoiceItems[2])) {
			if(ddEditor==null) {
				ddEditor = new DragDropEditor(tipvEdit.getTekstVak());
				ddEditor.addActionListener(this);
			}
			Hashtable<String,Object> h = (Hashtable<String,Object>)tipvEdit.getEditState().get("TComponentPreferences");
			ddEditor.setPreferences(h);
			Hashtable<String,Object> preferences = ddEditor.getPreferences();
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
