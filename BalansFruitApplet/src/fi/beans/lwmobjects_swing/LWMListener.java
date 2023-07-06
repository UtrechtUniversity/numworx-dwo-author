// $Id: LWMListener.java 327 2006-11-21 09:45:07Z wim $
package fi.beans.lwmobjects_swing;

/**
 * Interface LWMListener
 * Voor het reageren op verplaatsingen van lwmobjects.
 *
 * @author Paul Bergervoet
 *
 * @version 1, 12 maart 2001
 */

public interface LWMListener
{

/**
 * Wordt aangeroepen als een LWMComponent verplaatst is.
 * NB: niet bij een illegale poging tot verplaatsen, als het object weer teruggezet wordt!
 * 
 * @param obj De verplaatste LWMComponent
 * @param from De LWMContainer waar de component vandaan gekomen is.
 * @param to De LWMContainer waar de component heen gegaan is.
 */

	public void componentMoved(LWMComponent obj, LWMContainer from, LWMContainer to);
}
