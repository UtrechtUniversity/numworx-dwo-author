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
	// oud
	private static Color[] COLORS =
		{ Color.RED, Color.GREEN, Color.BLUE };
	// nieuw (Dark2 van http://colorbrewer2.org/):
//	private static Color[] COLORS = {
//		new Color(27, 158, 119),
//		new Color(217,95,2),
//		new Color(117,112,179),
//		new Color(231,41,138),
//		new Color(102,166,30),
//		new Color(230,171,2),
//		new Color(166,118,29),
//		new Color(102,102,102)
//	};
	
	private static Color lightgrey = new Color(240, 240, 240);
	private static Color grey = new Color(220, 220, 220);
	
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
		return grey;
	}
}
