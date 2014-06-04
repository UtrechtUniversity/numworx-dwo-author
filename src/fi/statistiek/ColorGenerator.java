/**
 * 
 */
package fi.statistiek;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Sylvia van Borkulo
 * 
 */
public class ColorGenerator
{
	// Dark2 van http://colorbrewer2.org/:
	private static final Color[] COLORS = {
		new Color(117, 112, 179), // paars
		new Color(230, 171, 2), // geel
		new Color(102, 166, 30), // groen
		new Color(217, 95, 2), // oranje
		new Color(231, 41, 138), // roze
		new Color(102, 102, 102), // grijs
		new Color(27, 158, 119), // groenblauw
		new Color(166, 118, 29) // bruin
	};
	
	private static final Color LIGHT_GREY = new Color(240, 240, 240);
	private static final Color GREY = new Color(220, 220, 220);
	public static final Color DEFAULT_VIEW_ELEMENT_COLOR = new Color(67,147,195); // blue
	public static final Color SELECTION_COLOR = Color.LIGHT_GRAY; // 
	
	private static ArrayList<Color> colorList = 
		new ArrayList<Color>(Arrays.asList(COLORS));
	
	private static Random random = new Random();

	/**
	 * Get color for displaying multiple split groups in a single view
	 * 
	 * @param number
	 *            the number of the split group
	 * @return the color in which this split group will be displayed
	 */
	public static Color getColor(int number)
	{
		if (number < colorList.size())
		{
			return colorList.get(number);
		}
		else
		{
			Color c = new Color(random.nextInt(256),
				random.nextInt(256), random.nextInt(256));
			colorList.add(c);
			return c;
		}
	}

	/**
	 * Get random color
	 * 
	 * @return the random color
	 */
	public static Color getColor()
	{
		Color c = new Color(random.nextInt(256),
			random.nextInt(256), random.nextInt(256));
		colorList.add(c);
		return c;
	}
	
	/**
	 * Get grey line color
	 * 
	 * @return the grey line color
	 */
	public static Color getGreyLineColor()
	{
		return GREY;
	}
	
	/**
	 * Get the default view element color
	 * 
	 * @return the default view element color
	 */
	public static Color getDefaultViewElementColor()
	{
		return DEFAULT_VIEW_ELEMENT_COLOR;
	}
}
