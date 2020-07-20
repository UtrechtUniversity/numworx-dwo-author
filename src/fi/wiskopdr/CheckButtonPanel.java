package fi.wiskopdr;

import java.awt.AWTEventMulticaster;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JPanel;

import org.cbook.cbookif.CBookEvent;
import org.cbook.cbookif.CBookEventHandler;
import org.cbook.cbookif.CBookEventListener;
import org.cbook.cbookif.Constants;

import fi.beans.iconan.Iconan;
import fi.beans.wiskopdrbeans.CBookAware;
import fi.beans.wiskopdrbeans.InteractieEditPanel;
import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.wiskopdr.formuleobjects.FormuleButton;
import fi.wiskopdr.tekstobjects.TekstElement;
import fi.wiskopdr.tekstobjects.TekstImageVak;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;

public class CheckButtonPanel extends JPanel implements InteractiePanel, ActionListener, CBookAware {

	
	FormuleButton checkButton;
	boolean fout = false;
	
	boolean nakijkenVak=true;
	boolean nakijkenPagina=false;
	boolean nakijkenXWidget=false;
	boolean actieBewaren=false;
	boolean actieAfronden=false;
	boolean actionNextPage=false;
	
	public CheckButtonPanel() {
		
		setLayout(null);
		setOpaque(false);
		
		checkButton = new FormuleButton(WiskOpdr.rb.getString("klaarKnopLabel"));
		checkButton.setBounds(0,0,80,20);
		checkButton.addActionListener(this);
		add(checkButton);
		
		setSize(checkButton.getWidth(), checkButton.getHeight());
	}
	
	
	
	
	
	
	@Override
	public void zetOpdracht(Hashtable h, String[] randomVars, Hashtable randomValues) {
		
		String knopImageString = "";
		boolean nakijkenVak=true;
		boolean nakijkenPagina=false;
		boolean nakijkenXWidget=false;
		boolean actieBewaren=false;
		boolean actieAfronden=false;
		boolean nakijken = true;
		boolean actionNextPage=false;
		
		if(h.containsKey("knopImageString")) knopImageString = (String)h.get("knopImageString");
		if(h.containsKey("nakijkenPagina")) nakijkenPagina = ((Boolean)h.get("nakijkenPagina")).booleanValue();
		if(h.containsKey("nakijkenVak")) nakijkenVak = ((Boolean)h.get("nakijkenVak")).booleanValue();
		if(h.containsKey("nakijken")) nakijkenVak = ((Boolean)h.get("nakijken")).booleanValue();
		if(h.containsKey("nakijkenXWidget")) nakijkenXWidget = ((Boolean)h.get("nakijkenXWidget")).booleanValue();
		if(h.containsKey("actieBewaren")) actieBewaren = ((Boolean)h.get("actieBewaren")).booleanValue();
		if(h.containsKey("actieAfronden")) actieAfronden = ((Boolean)h.get("actieAfronden")).booleanValue();
		if(h.containsKey("actionNextPage")) actionNextPage = ((Boolean)h.get("actionNextPage")).booleanValue();
		
		this.nakijkenPagina = nakijkenPagina && nakijken;
		this.nakijkenVak = nakijkenVak && nakijken;
		this.nakijkenXWidget = nakijkenXWidget && nakijken;
		this.actieBewaren = actieBewaren;
		this.actieAfronden = actieAfronden;
		this.actionNextPage = actionNextPage;
		
		if(knopImageString!=null && !"".equals(knopImageString))
       	{  	Iconan iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
			Icon knopImage = iconman.getIcon(knopImageString);
	    	checkButton.setPopupButtonIcon(knopImage);
		    int imWidth = iconman.getWidth(knopImageString);
			int imHeight = iconman.getHeight(knopImageString);
			if(imWidth == -1) imWidth = 80;
			if(imHeight == -1) imHeight = 20;
			checkButton.setSize(imWidth,imHeight);
			zetMaat();
	    }
	}

	@Override
	public void setState(Hashtable b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEditState(Hashtable h) {
		
		String knopImageString = null;
		
		if(h.containsKey("knopImageString")) knopImageString = (String)h.get("knopImageString");
		
		if(knopImageString!=null && !"".equals(knopImageString))
		{   Iconan iconman = new Iconan(WiskOpdr.applet, (Component)this, (Hashtable)TekstImageVak.getImageMap());
			Icon knopImage = iconman.getIcon(knopImageString);
	    	checkButton.setPopupButtonIcon(knopImage);
	    	int imWidth = iconman.getWidth(knopImageString);
			int imHeight = iconman.getHeight(knopImageString);
			if(imWidth == -1) imWidth = 80;
			if(imHeight == -1) imHeight = 20;
			checkButton.setSize(imWidth,imHeight);
			zetMaat();
	    }

	}

	@Override
	public Hashtable getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Hashtable getEditState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InteractieEditPanel getEditPanel() {
		return new CheckButtonEditPanel();
	}

	@Override
	public void wis() {
		// TODO Auto-generated method stub

	}

	@Override
	public void zetMaat(){
		setSize(checkButton.getWidth(), checkButton.getHeight());
		if(getParent()instanceof TekstElement)((TekstElement)getParent()).zetMaat();
	}
	
	public int geefAsHoogte()
	{	return checkButton.getHeight()/2+11;
	}

	@Override
	public int getIpId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[][] getScoreObjectives() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getScoreMax() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isCorrect() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isFout() {
		// TODO Auto-generated method stub
		return fout;
	}

	@Override
	public void zetMode(int mode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void zetNagekeken(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void opnieuw() {
		// TODO Auto-generated method stub

	}

	@Override
	public void kijkNa() {
		// TODO Auto-generated method stub

	}

	
	public void kijkNa(int stapNr) {
		// TODO Auto-generated method stub

	}

	

	@Override
	public void actionPerformed(ActionEvent e) {
		fout = false;
		
		if(nakijkenVak)
		{	Vector v = ((TekstInteractiePanelVak)getParent()).zoekInteractiePanels();
			for (int i = 0; i < v.size(); i++)
			{	((InteractiePanelContainerIF) v.elementAt(i)).kijkNa();
				((InteractiePanelContainerIF) v.elementAt(i)).zetNagekeken(true);
				fout = fout || ((InteractiePanelContainerIF) v.elementAt(i)).isFout();
				produceAction("checked");
				// TODO  dit werkt nog niet goed in geval attempts en errors in de log belangrijk zijn
			}
		}
		if(nakijkenPagina)
		{
			
		}
		if(nakijkenXWidget)
		{
			handler.fire(Constants.CHECK);
		}
		if(actieBewaren)
		{
			
		}
		if(actieAfronden)
		{
			
		}
		if(actionNextPage)
		{
			
		}
			
	}
	
	//ActionProducer
		private ActionListener actionListener = null;
		
		@Override
		public void addActionListener(ActionListener l) 
	 	{	actionListener = AWTEventMulticaster.add(actionListener,l);
	 	}
	 	
	 	public void removeActionListener(ActionListener l)
	 	{	actionListener = AWTEventMulticaster.remove(actionListener, l);
	 	}	
	 	
	 	public void produceAction(String command)
	 	{	if (actionListener != null)
	 		{	actionListener.actionPerformed( new ActionEvent(this, 0, command) );
	 		}
	 	}
	 	//end ActionProducer






		@Override
		public void acceptCBookEvent(CBookEvent event) {
			// TODO Auto-generated method stub
			
		}






		@Override
		public void addCBookEventListener(CBookEventListener listener,
				String command) {
			handler.addCBookEventListener(listener, command);		
		}



		CBookEventHandler handler = new CBookEventHandler(this);


		@Override
		public void removeCBookEventListener(CBookEventListener listener,
				String command) {
			handler.removeCBookEventListener(listener, command);			
		}






		@Override
		public String[] getSendCmds() {
			if(nakijkenXWidget)
				return new String[] { "check" };
			return null;
		}






		@Override
		public String[] getAcceptedCmds() {
			return null;
		}






		@Override
		public String getLocalizedCmd(String cmd) {
			// TODO Auto-generated method stub
			return null;
		}

}
