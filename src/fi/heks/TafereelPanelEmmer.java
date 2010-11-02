package fi.heks;

import java.awt.*;
import java.applet.*;
import java.util.*;
import java.awt.event.*;
import fi.heks.scobjects.*;
import fi.beans.appletutil.*;
import fi.heks.vectortek.*;

public class TafereelPanelEmmer extends ScPanel implements ActionListener
{	
	private AppletUtil au;
	private ScLWButton opnieuwKnop, opdrachtKnop, werkKnop;
	private ScLabel titelLabel, maalLabel, opdrachtTitel;
	private OefenTafereelPanelEmmer oefenTafereelPanel;
	private Emmer emmer;
	private GetalComponent emmerTeller;
	private ScTekstContainer uitleg, opdracht;
	ScTextArea textArea;
	
	
		
	public TafereelPanelEmmer(int x, int y, int b, int h, Applet applet)
	{	super(x,y,b,h);
		//setBackground(new Color(255,255,220));
		setBackground(getBackground());
		zetVastePlaats(true);
				
		au = new AppletUtil(applet);
				
		opnieuwKnop = new ScLWButton(110,425,100,25,Heks.rb.getString("opnieuwKnopLabel"));
		opnieuwKnop.addActionListener(this);
		opnieuwKnop.setVisible(false);
		add(opnieuwKnop);
		
		titelLabel = new ScLabel(10,20,290,40,"Emmers met blokjes");
		//add(titelLabel);
		
		uitleg = new ScTekstContainer(5,10,400,20,9,Heks.rb.getString("TafereelPanelEmmerUitleg"));
		 
		//"Met een emmer kun je meer blokjes /tegelijk in de ketel doen of eruit halen./ /
		//Klik op de emmer boven de ketel. Vul er een aantal warme of koude blokjes in. Gooi een aantal emmers in de ketel en kijk wat er gebeurt.
		//Klik op de emmer in de ketel. Vul er een aantal warme of koude blokjes in. Haal een aantal emmers uit de ketel en kijk wat er gebeurt.


		uitleg.lijnUit(ScLabel.LINKS);
		add(uitleg);
		
		oefenTafereelPanel = new OefenTafereelPanelEmmer(300,0,b-300,h-5, applet);
		oefenTafereelPanel.addActionListener(this);
		add(oefenTafereelPanel,0);
		
		emmer = new Emmer(150,250,110,125,applet);
		emmer.zetInstelbaar(false);
		add(emmer);
		emmer.setVisible(false);
		
		maalLabel = new ScLabel(100,300,50,50,"X");
		add(maalLabel);
		maalLabel.setVisible(false);
		
		emmerTeller = new GetalComponent(50,300,50,50);
		emmerTeller.zetWaarde(0);
		add(emmerTeller);
		emmerTeller.setVisible(false);
		
		opdrachtTitel = new ScLabel(20,200,200,40,Heks.rb.getString("opdrachtTitelLabel"));
		opdrachtTitel.setVisible(false);
		add(opdrachtTitel);
		
		opdracht = new ScTekstContainer(5,250,285,20,4,Heks.rb.getString("TafereelPanelEmmerOpdracht"));
		opdracht.lijnUit(ScLabel.LINKS);
		opdracht.setVisible(false);
		add(opdracht);
		
		textArea = new ScTextArea(5,340,270,150,0,0,TextArea.SCROLLBARS_VERTICAL_ONLY,""); 
		textArea.setVisible(false);
		add(textArea);
			
		opdrachtKnop = new ScLWButton(5,510,270,35,Heks.rb.getString("opdrachtKnopLabel"));
		opdrachtKnop.addActionListener(this);
		add(opdrachtKnop);
		
		werkKnop = new ScLWButton(5,510,270,35,Heks.rb.getString("werkEmmersKnopLabel"));
		werkKnop.addActionListener(this);
		textArea.setVisible(false);
		add(werkKnop);
		
				
	}
	
	public void setState(Hashtable h)
	{	String tekst = (String)h.get("tekst");
				
		textArea.setText(tekst);
	}
	
	public Hashtable getState()
	{	String tekst = null;
			
		tekst = textArea.getText();
		
		Hashtable h = new Hashtable();
	    h.put("tekst", tekst);
	    
	    return h;
	}
	
	public double getScore()
	{	if(textArea.getText() != null && textArea.getText().length()>100) return 100;
		if(textArea.getText() != null && textArea.getText().length()>5) return 10;
		return 0;
	}
	
	public void start()
	{	oefenTafereelPanel.zetOpnieuw();
		emmer.zetInhoud(0);
		emmer.setVisible(false);
		emmerTeller.zetWaarde(0);
		emmerTeller.setVisible(false);
		maalLabel.setVisible(false);
		opnieuwKnop.setVisible(false);
	}
	
	public void stop()
	{	oefenTafereelPanel.stop();
	}
	
	public void actionPerformed(ActionEvent e)
	{	if(e.getSource()==opdrachtKnop)
		{	oefenTafereelPanel.zetOpnieuw();
			oefenTafereelPanel.zetActief(false);
			emmer.zetInhoud(0);
			emmer.setVisible(false);
			emmerTeller.zetWaarde(0);
			emmerTeller.setVisible(false);
			maalLabel.setVisible(false);
			opnieuwKnop.setVisible(false);
			opdrachtKnop.setVisible(false);
			
			opdracht.setVisible(true);
			textArea.setVisible(true);
			opdrachtTitel.setVisible(true);
			werkKnop.setVisible(true);
		}
		else if(e.getSource()==werkKnop)
		{	opdrachtKnop.setVisible(true);
			oefenTafereelPanel.zetActief(true);
		
			opdracht.setVisible(false);
			textArea.setVisible(false);
			opdrachtTitel.setVisible(false);
			werkKnop.setVisible(false);
		}
		else if(e.getSource()==opnieuwKnop)
		{	if(opdracht.isVisible())return;
			oefenTafereelPanel.zetOpnieuw();
			emmer.zetInhoud(0);
			emmer.setVisible(false);
			emmerTeller.zetWaarde(0);
			emmerTeller.setVisible(false);
			maalLabel.setVisible(false);
			opnieuwKnop.setVisible(false);
		}
		else if(e.getSource()==oefenTafereelPanel)
		{	if(opdracht.isVisible())return;
			opnieuwKnop.setVisible(true);
			if(e.getActionCommand().equals("erin"))
			{	emmerTeller.verhoog();
				emmer.zetInhoud(oefenTafereelPanel.geefEmmerInhoud());
				emmer.setVisible(true);
				emmerTeller.setVisible(true);
				maalLabel.setVisible(true);
			}
			else if(e.getActionCommand().equals("eruit"))
			{	emmerTeller.verlaag();
				emmer.zetInhoud(oefenTafereelPanel.geefEmmerInhoud());
				emmer.setVisible(true);
				emmerTeller.setVisible(true);
				maalLabel.setVisible(true);
			}
			
		}
		
	}
}
