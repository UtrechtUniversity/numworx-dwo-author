package logotekenap;

import java.awt.*;
import java.awt.event.*;

/**
 * Een InvoerVariabele kun je gebruiken om je tekenprogramma met verschillende invoerwaarden
 * te laten uitvoeren. Een InvoerVariabele .... xxx
 * <br>
 * Je kunt een InvoerVariabele maken door:
 * <ul>
 * <li>
 * Eerst de InvoerVariabele te declareren en daarna 
 * <li>
 * in de methode initialiseer() de InvoerVariabele aan te maken.
 * </ul>
 * Bijvoorbeeld: een InvoerVariabele voor de breedte van een vierkant:
 * <PRE>
 *	InvoerVariabele breedte;
 *	....
 *
 *	void intialiseer()
 *	{	.....
 *		breedte = new InvoerVariabele("breedte van zijde", 10, 100, 50);
 *		maakInvoerMogelijk(breedte);
 *		....
 *	}
 * </PRE>
 * Hiermee maak je een InvoerVariabele die begint op 50, die als laagste waarde 10 mag
 * hebben en als hoogste waarde 100. De methode maakZichtbaar(...) zorgt ervoor
 * dat er naast het logo-veld een invoerveld met de tekst "breedte van zijde" komt te staan.
 * <br>
 * Er zijn zes verschillende methodes om iets met een InvoerVariabele te doen. Zie de 'Method Index'
 * hieronder. Als voorbeeld staat hier het opvragen van de waarde:
 * <PRE>
 *	.......
 *	vierkant( breedte.geefWaarde() );
 *	......
 * </PRE>
 *
 * @author Paul Bergervoet
 * @version 3  10 juni 2000
 */

public class InvoerVariabele extends InvoerVar
		implements MouseListener
 {	// NB: Variabelen voor waarde etc gedefinieerd in InvoerVar
 	
	private NumOnlyField wtekst;
	private ArrowCanvas warrow; 
	//private ArrowRunner r;
	private int stap;					// size of step +/- at arrowclick
	private int dir;					// 1 = up, -1 = down
	private int maxStepFactor;
	private Label lgh;	
/**
 * Constructor for InvoerVariabele.
 * Creates a InvoerVariabele with the two arrows left of the textfield.
 *
 * @param mn minimum value of the numberarrow
 * @param mx maximum value of the numberarrow
 * @param w  initial value
 * @param s  step of the number arrow (change of value at arrow click)
 * @param nm  naam
 */
	public InvoerVariabele( 	String nm,		// parameter name
								int mn, 		// minimum
								int mx, 		// max
								int w			// value == initial value
						)
	{	minw = mn;
		maxw = mx;
		waarde = w;
		initw = w;
		stap = 1;
		maxStepFactor = ((maxw-minw)/20)+1; 		// versnelling hangt af van verschil
		naam = nm;
		
		// set up lay out
		GridBagLayout gridbag = new GridBagLayout();	// set up data-area
		GridBagConstraints c = new GridBagConstraints();
		setLayout(gridbag);

		// component, Label: parameter name
		c.gridwidth = GridBagConstraints.REMAINDER; 	// line holding Name
		c.anchor = GridBagConstraints.WEST; 			// left
		lgh = new Label(naam);
		gridbag.setConstraints(lgh, c);
		add(lgh);

		// components: Arrows, NumOnlyField and Units
		c.gridwidth = 1;	
		c.insets = new Insets(5, 0, 0, 0); 		// space above TextField
		c.anchor = GridBagConstraints.EAST; 
		c.fill = GridBagConstraints.NONE;
		warrow = new ArrowCanvas();
		warrow.addMouseListener(this);
		gridbag.setConstraints(warrow, c);
		add(warrow);
		
		c.anchor = GridBagConstraints.WEST; 		// put left
		wtekst = new NumOnlyField( minw, maxw, waarde, this);
		wtekst.setEnabled(true);				// also sets color....
		gridbag.setConstraints(wtekst, c);
		add(wtekst);
	}
	
	public void zetInvoerAan()
	{	enabled = true;
		wtekst.setEnabled(true);
		warrow.setEnabled(true);
		repaint();
	}
	
	public void zetInvoerUit()
	{	enabled = false;
		wtekst.setEnabled(false);
		warrow.setEnabled(false);
		repaint();
	}
	
	
	public void zetWaarde(int newval)
	{	if ( newval > maxw )
		{ 	waarde = maxw;
		} else if (newval < minw )
		{ 	waarde = minw;
		} else
		{ 	waarde = newval; 
		}
		wtekst.setValue( waarde );
	}	


/**
 * zit al in super, evenals verhoog/verlaag
	public int geefWaarde()
	{	return waarde;
	}
 */

// Event handling
// 1. MouseListener

/**
 * Invoked by timer when mouse is held on an arrow.
 *
 * @param count Counter of kicks, for accelaration.
 */
	protected void kick(int count)								// mouse held
	{	zetWaarde(waarde + dir*(int)Math.min(count, maxStepFactor)*stap);
		baas.invoerVarActie(this);
	}
	
/**
 * Invoked when mouse button is pressed on the InvoerVariabele.
 * Does step and starts ArrowRunner.
 */
	public void mousePressed(MouseEvent e)
	{	dir = warrow.getArrow( e.getY() );	// get height of click to decide up or down
		if ( enabled && dir!=0 )
		{	zetWaarde(waarde + dir*stap);	// 1 or -1 times stap
			//r = new ArrowRunner(this);	// will kick as long as mouse is held
			//r.start();
								baas.invoerVarActie(this);
		}
	}

 /**
  * Invoked when mouse button is released over the InvoerVariabele.
  * stops ArrowRunner.
  */
	public void mouseReleased(MouseEvent e)
	{	//if ( r !=null )
		//{	r.stop();
			warrow.setInactive();
		//	r = null;
		//}
	}
	
 /**
  * Invoked when mouse button is clicked on the InvoerVariabele.
  * Does nothing.
  */
	public void mouseClicked(MouseEvent e)
	{	// do nothing
	}
	
 /**
  * Invoked when mouse has entered the InvoerVariabele.
  * Does nothing.
  */
	public void mouseEntered(MouseEvent e)
	{	// do nothing
	}
	
 /**
  * Invoked when mouse has exited  the InvoerVariabele.
  * stops ArrowRunner.
  */
	public void mouseExited(MouseEvent e)
	{	//if ( r !=null )
		//{	r.stop();
			warrow.setInactive();
		//	r = null;
		//}
	}
	
 // 2. React to change in NumOnlyField
 
/**
 * Invoked when the number in the textfield has changed.
 *
 * @param s Dummy string: name of the NumOnlyTextField.
 * @param w Double value of the textfield.
 */
	public void numberTyped(int w)		// dummy String from NumOnlyField
	{	waarde = w;
		// no need to do anything with arrows
		baas.invoerVarActie(this);
	}
}
 
