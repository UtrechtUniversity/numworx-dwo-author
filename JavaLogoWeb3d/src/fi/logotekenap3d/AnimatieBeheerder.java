package fi.logotekenap3d;

import java.awt.*;
import java.awt.event.*;

class AnimatieBeheerder extends Panel implements ActionListener, Runnable
{
  	private Button animatieknop;
  	private Thread animatie;
  	boolean animatieAan;
	TekenApplet3D eigenaar;	  

	public AnimatieBeheerder(TekenApplet3D ap)
	{	setBackground(Color.lightGray);
		eigenaar = ap;
		animatieAan = false;
		animatieknop = new Button("animatie");
		animatieknop.addActionListener(this);
		add(animatieknop);
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
			animatieknop.setLabel("animatie");
		}
	}
	
	public void run()
	{	while(eigenaar.tb.bezigMetTekenen)pauze(1);
		eigenaar.animatie();
		animatieknop.setLabel("animatie");
	}
	
	//-------------------------------------------------------------------------------------------
	//deze methoden worden behalve door de Animatiebeheerder zelf, ook gebruikt door de 
	// MuisBeheerder 
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
		animatieknop.setLabel("stoppen");
	}	
	
	//-------------------------------------------------------------------------------------------
	//deze methoden worden gebruikt in de animatie- en muishandlers van het leerlingenprogramma.
	//"animatieLopend()" wordt ook gebruikt door de TraceBeheerder en MuisBeheerder.
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