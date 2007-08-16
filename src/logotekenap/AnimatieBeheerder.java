package logotekenap;

import java.awt.Button;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimatieBeheerder extends Panel implements ActionListener, Runnable
{
  	private Button animatieknop;
  	private Thread animatie;
  	boolean animatieAan;
	TekenApplet eigenaar;
	TraceBeheerder trb;

	public AnimatieBeheerder(TekenApplet ap)
	{	eigenaar = ap;
		animatieAan = false;
		animatieknop = new Button("animatie");
		animatieknop.addActionListener(this);
		add(animatieknop);
	}
	//-------------------------------------------------------------------------------------------
	//de TraceBeheerder maakt zich met deze methode kenbaar aan de AnimatieBeheerder 
	//-------------------------------------------------------------------------------------------
	public void meldTraceBeheerder(TraceBeheerder trb)
	{	this.trb = trb;
	}
	//-------------------------------------------------------------------------------------------
	//afhandeling van de animatieknop actie, en het starten van de animatiedraad 
	//-------------------------------------------------------------------------------------------
	public void actionPerformed(ActionEvent e)
	{	if(animatieknop.getLabel()=="animatie")
		{	beginAnimatie();
		}
		else
		{	onderbreekAnimatie();
			if(trb!=null)trb.setEnableTraceKnop(true);
			animatieknop.setLabel("animatie");
		}
	}
	public void run()
	{	while(eigenaar.tb.bezigMetTekenen)pauze(1);
		eigenaar.animatie();
		if(trb!=null)trb.setEnableTraceKnop(true);
		animatieknop.setLabel("animatie");
	}

		
	//-------------------------------------------------------------------------------------------
	//deze methoden worden behalve door de Animatiebeheerder zelf, ook gebruikt door de 
	//TraceBeheerder en MuisBeheerder 
	//-------------------------------------------------------------------------------------------
	public void onderbreekAnimatie()
	{	animatieAan=false;
		try
		{	animatie.join();
		}
		catch(InterruptedException e) {}
	}
	public void beginAnimatie()
	{	animatieAan=true;
		animatie = new Thread(this);
		animatie.start();
		if(trb!=null)trb.setEnableTraceKnop(false);
		animatieknop.setLabel("stoppen");
	}	

	void setEnableAnimatieKnop(boolean b)
	{	animatieknop.setEnabled(b);
	}
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in de animatie- en muishandlers van het leerlingenprogramma.
	//"animatieStatus()" wordt ook gebruikt door de TraceBeheerder en MuisBeheerder.
	//-------------------------------------------------------------------------------------------
	public boolean animatieLopend()
	{	return animatieAan;
	}
	public void pauze(int millisec)
	{  try
    	{   Thread.sleep(millisec);
		}
    	catch(InterruptedException e)    // geen ;
		{   }
	}
}

