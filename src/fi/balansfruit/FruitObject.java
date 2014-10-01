package fi.balansfruit;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import fi.beans.lwmobjects_swing.*;

/**
 * Een stukje fruit.
 * Doet verder:
 * - heeft een extra variabele 'weight'
 * - roept na verplaatsing BalansFruit.checkBalance() aan.
 *
 * @author Paul Bergervoet
 *
 * @version 0, 28 augustus 2000
 */

class FruitObject extends LWMObject
{	// variables: properties
	private double weight;			// het gewicht van het fruit!
	
	public FruitObject(Image i, double wgh)
	{	super(i);
		weight = wgh;
	}
	
	public double getWeight()
	{	return weight;
	}
	
	public void setWeight(double wgh)
	{	weight = wgh;
	}
}
