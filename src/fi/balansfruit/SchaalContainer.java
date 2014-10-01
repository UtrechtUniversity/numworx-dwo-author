package fi.balansfruit;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import fi.beans.lwmobjects_swing.*;

/**
 * De onzichtbare Container voor fruit op een van de twee schalen van de balans.
 * Doet verder:
 * - berekent het totaalgewicht van fruit op de schaal.
 *
 * @author Paul Bergervoet
 *
 * @version 0, 28 augustus 2000
 */

class SchaalContainer extends LWMContainer
{	
	public SchaalContainer(int w, int h)
	{	super(w, h);
		setMovable(false);
		setKeepInside(true);
		setGravity(GOSOUTH);
	}
	
	public double getTotalWeight()
	{	double tweight = 0;
		for ( int teller = 0; teller < getComponentCount(); teller++ )
		{	tweight = tweight + ((FruitObject)getComponent(teller)).getWeight();
		}
		return tweight;
	}
}
