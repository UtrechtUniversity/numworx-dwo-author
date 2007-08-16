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
 * @since JDK 1.1
 */

public class SchuifInvoerVariabele extends InvoerVar
		implements MouseListener, MouseMotionListener
 {	private NumOnlyField wtekst;
	private Slider wschuif; 
	private Label lgh;
	private Label lmin;
	private Label lmax;

/**
 * Constructor for SchuifInvoerVariabele.
 *
 * @param mn minimum value of the SchuifInvoerVariabele
 * @param mx maximum value of the SchuifInvoerVariabele
 * @param w  initial value
 * @param nm  parameter name
 */
	public SchuifInvoerVariabele(	String nm,		// parameter name and unit
							int mn, 		// min
							int mx, 		// min
							int w 		// value == initial value
						)
	{	minw = mn;
		maxw = mx;
		waarde = w;
		initw = w;
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

		// components, Labels: min and max value, min and max should be ints
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST; 			// left
		lmin = new Label( String.valueOf(minw) );
		gridbag.setConstraints(lmin, c);
		add(lmin);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.EAST; 			// right
		lmax = new Label( String.valueOf(maxw) );
		gridbag.setConstraints(lmax, c);
		add(lmax);
		
		// component slide bar
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.HORIZONTAL;
		wschuif = new Slider( valToPos(waarde) );
		wschuif.addMouseListener(this);
		wschuif.addMouseMotionListener(this);
		gridbag.setConstraints(wschuif, c);
		add(wschuif);

		// components: NumOnlyField and Units (hier niet)
		c.gridwidth = 1 ;	
		c.insets = new Insets(5, 0, 0, 0); 		// space above TextField
		c.anchor = GridBagConstraints.WEST; 
		c.fill = GridBagConstraints.NONE;
		wtekst = new NumOnlyField( minw, maxw, waarde, this);
		wtekst.setEnabled(true);				// also sets color....
		gridbag.setConstraints(wtekst, c);
		add(wtekst);	
	}

/* calculate slider position from value.
 */
	private double valToPos(int val)				// double, omdat 0<=pos<=1
	{   	return ( (double)val-minw) / (maxw-minw);
	}

/* calculate value and slider position from mouse position.
 */
	private void processSliderChange(int mousepos)
	{	double relpos = wschuif.getPos(mousepos);	// 0<=pos<=1
		if (relpos < 0 ) { relpos = 0; }
		if (relpos > 1 ) { relpos = 1; }
		waarde = (int)Math.round( minw + relpos*(maxw-minw) );
		wschuif.setSlide( valToPos(waarde) );		// die had nog niks gedaan
		wtekst.setValue( waarde );
		baas.schuifInvoerVarActie(this);
	}
	
// Public methods: Enable/disable, setValue, getValue

/**
 * Enables the NumberSlider. When disabled it becomes disabledSliderColor.
 */
	public void zetInvoerAan()
	{	enabled = true;
		wtekst.setEnabled(true);
		wschuif.setEnabled(true);
		repaint();
	}

/**
 * Enables the NumberSlider. When disabled it becomes disabledSliderColor.
 */
	public void zetInvoerUit()
	{	enabled = false;
		wtekst.setEnabled(false);
		wschuif.setEnabled(false);
		repaint();
	}
		
/**
 * Sets the value to be displayed in the number textfield & slider.
 *
 * @param newval The new double value to be displayed.
 */
	public void zetWaarde(int newval)
	{	if ( newval > maxw )
		{ 	waarde = maxw;
		} else if (newval < minw )
		{ 	waarde = minw;
		} else
		{ 	waarde = newval; 
		}
		wtekst.setValue( waarde );
		wschuif.setSlide( valToPos(waarde) );
	}
	
 /**
  * The value of the SchuifInvoerVariabele is returned as int.
  *
  * @return value of SchuifInvoerVariabele.
  *
	public int geefWaarde()
	{	return waarde;
	}
 * overbodig, zit in super, evenals verhoog/verlaag
 */

// Event handling
  // 1. MouseListener

/**
 * Invoked when mouse button is pressed on the SchuifInvoerVariabele.
 * Move slide to click location.
 */
	public void mousePressed(MouseEvent e)
	{	processSliderChange( e.getX() );
		// System.out.println("pressed, new value is "+waarde );
	}

 /**
  * Invoked when mouse button is released over the SchuifInvoerVariabele.
  * Does nothing.
  */
	public void mouseReleased(MouseEvent e)
	{	// do nothing
	}
	
 /**
  * Invoked when mouse button is clicked on the SchuifInvoerVariabele.
  * Does nothing.
  */
	public void mouseClicked(MouseEvent e)
	{	// do nothing
	}
	
 /**
  * Invoked when mouse has entered the SchuifInvoerVariabele.
  * Does nothing.
  */
	public void mouseEntered(MouseEvent e)
	{	// do nothing
	}
	
 /**
  * Invoked when mouse has exited  the SchuifInvoerVariabele.
  * Does nothing.
  */
	public void mouseExited(MouseEvent e)
	{	// do nothing
	}
	
  // 2. MouseMotionListener
  
 /**
  * Invoked when mouse is dragged the SchuifInvoerVariabele.
  * Move slide to drag location.
  */
	public void mouseDragged(MouseEvent e)
	{	processSliderChange( e.getX() );
	}
	
 /**
  * Invoked when mouse is moved the SchuifInvoerVariabele.
  * Does nothing.
  */
	public void mouseMoved(MouseEvent e)
	{	// do nothing
	}
	  
// 3. React to change in NumOnlyField
 
/**
 * Invoked when the number in the textfield has changed.
 *
 * @param w Double value of the textfield.
 */
	public void numberTyped(int w)
	{	waarde = w;
		wschuif.setSlide( valToPos(waarde) );
		baas.schuifInvoerVarActie(this);
	}
}	// end class SchuifInvoerVariabele
