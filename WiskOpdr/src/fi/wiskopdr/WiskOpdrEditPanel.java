package fi.wiskopdr;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import fi.beans.base64code.*;
import fi.wiskopdr.formuleobjects.FormuleVakHouder;
import fi.wiskopdr.formuleobjects.Tablet;
import fi.wiskopdr.formuleobjects.TabletOwner;
import fi.wiskopdr.opdrnav.*;
import fi.wiskopdr.tekstobjects.ShareAction;
import fi.wiskopdr.tekstobjects.TekstImageVak;
	

public class WiskOpdrEditPanel extends JPanel implements TabletOwner
{
	private Hashtable launchData;
	private String launchDataString;
	private OpdrNavStructEdit onsEdit;
	private fi.wiskopdr.opdrnav.MyOpdrEditContainer opdrContainer;
	private static int defaultEditorWidth = 800;
	private static int defaultEditorHeight = 350;
	private static int defaultDocumentWidth = 800;
	private static int defaultDocumentHeight = 300;
	
	public WiskOpdrEditPanel(String launchDataString, WiskOpdr applet)
	{
		this(launchDataString, applet, defaultEditorWidth, defaultEditorHeight, defaultDocumentWidth, defaultDocumentHeight);
	}
	
	public WiskOpdrEditPanel(String launchDataString, WiskOpdr applet, int editorWidth, int editorHeight, int documentWidth, int documentHeight)
	{	setLayout(null);
		setOpaque(false);
		super.setSize(500,350);
		
		this.launchDataString = launchDataString;
		launchData = null;
		if (launchDataString != null) {
			Object o = StringCodeObject.decodeStringToObject(launchDataString);
			launchData = (Hashtable) o;
		}
		if (launchData == null) {
			launchData = applet.makeDefaultParamValues(0);
			//launchData.put("scheidingX", new Integer(400));
			Hashtable instellingen = new Hashtable();
			instellingen.put("docWidth",new Integer(documentWidth));
			instellingen.put("docHeight",new Integer(documentHeight));
			launchData.put("instellingen", StringCodeObject.encodeObjectToString(instellingen));
		}

		ShareAction.init((String)launchData.get(ShareAction.SHARE_MAP));
		TekstImageVak.setImageMapString((String)launchData.get(TekstImageVak.IMAGE_MAP));
		
//		String langArg = getParameter("language");
//        if ( langArg == null || langArg.equals("")) langArg = "en";
//        Locale language = new Locale (langArg, "");
//        WiskOpdr.rb = ResourceBundle.getBundle("fi.wiskopdr.text.Text",language);
		
	    if(WiskOpdr.lookAndFeel==null) WiskOpdr.lookAndFeel = UIManager.getLookAndFeel();
	    opdrContainer =  new fi.wiskopdr.opdrnav.MyOpdrEditContainer(editorWidth,editorHeight); 
		onsEdit = new OpdrNavStructEdit(opdrContainer,0,0,editorWidth, editorHeight, launchData);
		onsEdit.removeMouseListener(onsEdit);
		opdrContainer.setPlainEditor(this);
		add(opdrContainer);

	}
	
	public WiskOpdrEditPanel(Hashtable Hashtable, WiskOpdr applet)
	{
		this(Hashtable, applet, defaultEditorWidth, defaultEditorHeight);
	}

	
	public WiskOpdrEditPanel(Hashtable launchData, WiskOpdr applet, int editorWidth, int editorHeight)
	{	setLayout(null);
		setOpaque(false);
		super.setSize(800,350); 
		
		this.launchData = launchData;
		ShareAction.init((String)launchData.get(ShareAction.SHARE_MAP));
		TekstImageVak.setImageMapString((String)launchData.get(TekstImageVak.IMAGE_MAP));
		
		String langArg = getParameter("language");
        if ( langArg == null || langArg.equals("")) langArg = "en";
        Locale language = new Locale (langArg, "");
        WiskOpdr.rb = ResourceBundle.getBundle("fi.wiskopdr.text.Text",language);
		
	    if(WiskOpdr.lookAndFeel==null) WiskOpdr.lookAndFeel = UIManager.getLookAndFeel();
	    opdrContainer =  new fi.wiskopdr.opdrnav.MyOpdrEditContainer(editorWidth,editorHeight); 
		onsEdit = new OpdrNavStructEdit(opdrContainer,0,0,editorWidth, editorHeight, launchData);
		onsEdit.removeMouseListener(onsEdit);
		opdrContainer.setPlainEditor(this);
		add(opdrContainer);
	}
	
	
	public void setSize(int b, int h)
	{	super.setSize(b,h);
		if (opdrContainer!=null) {
		  
		  opdrContainer.setBounds(0,0,b,h);
		  opdrContainer.setEditPanelSize();
		}
	}
	
	public String getParameter(String name)
	{	String value = (String)launchData.get(name);
		return value;
	}
	
	public Hashtable getLaunchData()
    {   if(!WiskOpdr.launchDataChanged)return launchData;
		Hashtable h = onsEdit.getEditState();
		ShareAction.prepareShareMap(h);
    	String language = getParameter("language");
    	String bgcolor = getParameter("bgcolor");
    
    	if(language!=null) h.put("language",language);
    	if(bgcolor!=null) h.put("bgcolor",bgcolor);
    	String imageMapString = TekstImageVak.getImageMapString();
		if(imageMapString!=null)h.put(TekstImageVak.IMAGE_MAP, imageMapString);
		String shareMapString = ShareAction.getSharedLaunchData();
		if(shareMapString != null) h.put(ShareAction.SHARE_MAP, shareMapString);
    	return h;
	}
	
	public String getText()
	{	if(!WiskOpdr.launchDataChanged)
			return launchDataString;
		launchData = getLaunchData();
		launchDataString = StringCodeObject.encodeObjectToString(launchData);
		return launchDataString;
	}
	
	public Component getComponent()
	{   return this;
	} 
	
	public void end()
    {   if(onsEdit!=null) onsEdit.destroy();
	}
	
    public void reset()
    {   if(onsEdit!=null)onsEdit.stop();
	}

//	@Override
//	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void removeTablet() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void zetTabletUser(FormuleVakHouder formuleVakHouder) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public Tablet getTablet() {
//		// TODO Auto-generated method stub
//		return null;
//	}	
    
    private Tablet tablet;
    private FormuleVakHouder tabletUser;
    private boolean tabletAdded;
    
    public void zetTabletUser(FormuleVakHouder formuleVakHouder) {
		if (tablet == null)
			return;
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}

	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y) {
		if (tablet == null) {
			tablet = new Tablet(formuleVakHouder);
			tablet.setLocation(x, y);
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
		tabletUser = formuleVakHouder;
	}

	public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y) {
		if (tablet == null) {
			tablet = new Tablet(formuleVakHouder);
		}
		if (!tabletAdded) {
			//this.setLayer(tablet, JLayeredPane.PALETTE_LAYER.intValue());
			this.add(tablet, 0);
			tablet.setLocation(x, y);
			tabletAdded = true;
			repaint();
		}
		tablet.zetFormuleVakHouder(formuleVakHouder);
	}

	public void removeTablet() {
		if (tablet == null)
			return;
		remove(tablet);
		repaint();
		tabletAdded = false;
	}

	public Tablet getTablet() {
		return tablet;
	}
}
