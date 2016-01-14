package fi.statistiek;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JFrame;

import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.statistiek.boxplot.BoxplotController;
import fi.statistiek.crosstabulationtable.CrossTabulationTableController;
import fi.statistiek.descriptives.DescriptivesController;
import fi.statistiek.dotplot.DotplotController;
import fi.statistiek.frequencytable.FrequencyTableController;
import fi.statistiek.histogram.HistogramController;

/**
 * Statistische representaties
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class Statistiek implements WiskOpdrApplet
{
	public static ResourceBundle rb;
	public static Font font = new Font("SansSerif", Font.PLAIN, 12);
	public static Font font_bold = new Font("SansSerif", Font.BOLD, 12);
	static DecimalFormatSymbols dfs;
	public static DecimalFormat df;
	public static int scrollSpeedUnit = 16;
	public static Color backgroundColor = Color.WHITE;
	public static double BIN_WIDTH_DEFAULT = 1;

	// Name all StatistiekViews here, and add them to the createView method
	public static String[] VIEWS;// = {"Table", "Histogram", "Dotplot",
								 // "Frequentietabel", "Frequentiepolygoon",
								 // "Boxplot", "Kruistabel", "Spreidingsdiagram",
								 // "Kengetallen"};
	public static String[] VIEWS_translated;// = {"Table", "Histogram",
											// "Dotplot", "Frequentietabel",
											// "Frequentiepolygoon", "Boxplot", "Crosstab",
											// "Scatterplot", "Descriptive statistics"};
	private static Locale language;

	public Statistiek()
	{
		Locale language = new Locale("nl", "");
		this.language = language;
		rb = ResourceBundle.getBundle("fi.statistiek.text.Text", language);
		
		dfs = DecimalFormatSymbols.getInstance(language);
		df = new DecimalFormat("0.#", dfs);
		initViews();
	}

	static void initViews()
	{
		VIEWS_translated = new String[9];
		VIEWS_translated[0] = Statistiek.rb.getString("tableOption");
		VIEWS_translated[1] = Statistiek.rb.getString("histogramOption");
		VIEWS_translated[2] = Statistiek.rb.getString("dotplotOption");
		VIEWS_translated[3] = Statistiek.rb.getString("frequencytableOption");
		VIEWS_translated[4] = Statistiek.rb.getString("frequencypolygonOption");
		VIEWS_translated[5] = Statistiek.rb.getString("boxplotOption");
		VIEWS_translated[6] = Statistiek.rb.getString("crosstabOption");
		VIEWS_translated[7] = Statistiek.rb.getString("scatterplotOption");
		VIEWS_translated[8] = Statistiek.rb.getString("descriptivesOption");

		VIEWS = new String[9];
		VIEWS[0] = "Table";
		VIEWS[1] = "Histogram";
		VIEWS[2] = "Dotplot";
		VIEWS[3] = "Frequentietabel";
		VIEWS[4] = "Frequentiepolygoon";
		VIEWS[5] = "Boxplot";
		VIEWS[6] = "Kruistabel";
		VIEWS[7] = "Spreidingsdiagram";
		VIEWS[8] = "Kengetallen";
	}

	public Statistiek(Locale language)
	{
		Statistiek.language = language;
		rb = ResourceBundle.getBundle("fi.statistiek.text.Text", language);
		dfs = DecimalFormatSymbols.getInstance(language);
		df = new DecimalFormat("0.#", dfs);
		initViews();
	}

	/**
	 * Testdingen
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		// HistogramModel.appropriateBoundaries(-1, 22, 20);
		String langArg = "nl";
		Locale language = new Locale(langArg, "");
		rb = ResourceBundle.getBundle("fi.statistiek.text.Text", language);

		initViews();

		JFrame frame = new JFrame();
		// edit panel
//		StatEditPanelController statEditPanelController = new StatEditPanelController();
//		Hashtable oudeState = statEditPanelController.getEditState();
		
		// interactie panel
		StatInteractiePanel statInteractiePanel = new StatInteractiePanel();
		Statistiek s = new Statistiek();

		// try to set a specific state in edit panel
//		try
//		{
//			FileInputStream fin = new FileInputStream("C:\\StatistiekIrisSet.epic");
//			ObjectInputStream ois = new ObjectInputStream(fin);
//			Hashtable h = (Hashtable) ois.readObject();
//			c.setEditState(h);
//			System.out.println("Iris dataset loaded");
//			ois.close();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			statEditPanelController.setEditState(oudeState);
//		}

//		frame.setContentPane(statEditPanelController);
		frame.setContentPane(statInteractiePanel);

		frame.setVisible(true);
		frame.setSize(new Dimension(1024, 768));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * Function that creates instances of StatistiekViews
	 * 
	 * @param viewType
	 *            The desired type of StatistiekView
	 * @param viewName
	 *            The initial viewName
	 * @param model
	 *            The model containing the data that the view will show
	 * @param startVar
	 *            The index of the column that will be shown in the view
	 * @param statInteractiePanel
	 * 			  The statInteractiePanel to pass through 
	 * @return a new statistiekView
	 */

	// syl: Param statInteractiePanel toegevoegd
	public static StatistiekView createView(String viewType, String viewName,
		StatTableModel model, int startVar, int startVar2,
		StatInteractiePanel statInteractiePanel)
	{
//		System.out.println("Statistiek.createView(viewType=" + viewType + ", viewName=" + viewName 
//			+ ", identityHashCode(statTableModel)=" + identityHashCode(model) + ")");

		if (viewType.equals("Table"))
		{
			return new StatTable(model, statInteractiePanel, viewName);
		}
		else if (viewType.equals("Histogram"))
		{
			return new HistogramController(model, viewName, false, startVar);
		}
		else if (viewType.equals("Dotplot"))
		{
			return new DotplotController(model, viewName, startVar);
		}
		else if (viewType.equals("Frequentietabel"))
		{
			return new FrequencyTableController(model, viewName, startVar);
		}
		else if (viewType.equals("Frequentiepolygoon"))
		{
			return new HistogramController(model, viewName, true, startVar);
		}
		else if (viewType.equals("Boxplot"))
		{
			return new BoxplotController(model, viewName, startVar);
		}
		else if (viewType.equals("Kruistabel"))
		{
//			System.out.println("Statistiek.createView(): viewName = " + viewName);
			CrossTabulationTableController controller = new CrossTabulationTableController(model, viewName, startVar, startVar2);
			// set the split variable (i.e., the column variable)
			controller.setSplit(startVar2);
			return controller;
		}
		else if (viewType.equals("Spreidingsdiagram"))
		{
			//System.out.println("Statistiek.createView(): viewName = " + viewName);
			return new DotplotController(model, viewName, startVar, startVar2);
		}
		else if (viewType.equals("Kengetallen"))
		{
			//System.out.println("Statistiek.createView(): viewName = " + viewName);
			return new DescriptivesController(model, viewName, startVar);
		}
		else
		{
			return null;
		}
	}

	private static int identityHashCode(Object o)
	{
		return System.identityHashCode(o);
	}

	/**
	 * Implementation of WiskOpdrApplet
	 */
	public InteractiePanel getInteractiePanel()
	{
		return new StatInteractiePanel();
	}

	/**
	 * get the top level ancestor of a JComponent This implementation differs
	 * from JComponent.getTopLevelAncestor because this doesn't stop at an
	 * applet
	 * 
	 * @param c
	 *            The component to get the top level ancestor of
	 * @return c's top level ancestor
	 */
	public static Container getTopLevelAncestor(JComponent c)
	{
		Container container = c.getTopLevelAncestor();
		while (container.getParent() != null)
		{
			container = container.getParent();
		}
		return container;
	}
	
	/**
	 * Determine appropriate bin boundaries from given min, max and number of
	 * bins.
	 * 
	 * @param min
	 *            The minimum value in the dataset
	 * @param max
	 *            The maximum value in the dataset
	 * @param noBins
	 *            The desired number of bins
	 * @return ArrayList containing appropriate bin boundaries
	 */
	public static ArrayList<Double> appropriateBoundaries(double min,
		double max, int noBins)
	{
		ArrayList<Double> boundaries;
		
		if ((min == 0) && (max == 0))
		{
			boundaries = new ArrayList<Double>();
			boundaries.add(0.0);
			boundaries.add(0.0);
		}
		else
		{
			// calculate decimal bin boundaries smaller than 1 
			if (((Math.abs(min) < 1) && (Math.abs(max) < 1))
				|| (((max - min) < 1) && ((max - min) != 0)))
			{
				// determine the number of decimals of min and max
				String minString = String.valueOf(min);
				int decimalPlacesMin = Statistiek.getNumberOfDecimals(minString);
				
				String maxString = String.valueOf(max);
				int decimalPlacesMax = Statistiek.getNumberOfDecimals(maxString);
	
				int numberOfDecimals = Math.max(decimalPlacesMin, decimalPlacesMax);
				
				min = min * (Math.pow(10, numberOfDecimals)); 
				max = max * (Math.pow(10, numberOfDecimals));
				
				ArrayList<Double> binBoundaries = appropriateBoundaries(min, max, noBins);
				// divide each bin boundary by Math.pow(10, numberOfDecimals)
				for (int i = 0; i < binBoundaries.size(); i++)
				{
					// divide the bin boundary by Math.pow(10, numberOfDecimals)
					// and round to the correct number of decimals (because of possible rounding errors)
					double newValue = round(binBoundaries.get(i) / (Math.pow(10, numberOfDecimals)), numberOfDecimals);
					binBoundaries.set(i, newValue);
				}
				return binBoundaries; 
			}
			
			double b = (max - min) / (double) (Math.max(noBins - 1, 1));
			
			if ((Math.abs(min) > 1) || (Math.abs(max) > 1))
			{
				// neem integer waarde
				b = (int) b;
			}
			
			int e;
			if (noBins == 1)
			{
				e = (int) Math.ceil(Math.log10(b));
			}
			else
			{
				e = (int) Math.floor(Math.log10(b));
			}
	
			double step = Math.ceil(b * Math.pow(10, -e)) * Math.pow(10, e);
			
			if (step == 0)
				step++;
	
			// System.out.println("e = " + e);
			// System.out.println("step = " + step);
	
			double start;
			
			if (min == Math.round(min) || ((Math.abs(min) < 1) && (Math.abs(max) < 1)))
			{
				start = min;
			}
			else
			{
				start = (Math.ceil(min / step) - 1) * step;
			}
	
			// make sure step is not too large
			// use min value instead of start to be more constraining 
			if (step > 1)
			{
				while (min + step >= max)
				{
					step = Math.ceil(step / 2);
				}
			}
			
			// make sure the maximum value is covered by the bins
			while ((start + noBins * step) <= max)
			{
				step = increaseStep(step);
			}
			
			// build arraylist
			boundaries = new ArrayList<Double>();
			// correct afronden op basis van decimalen in start en binWidth
			String startString = String.valueOf(start);
			int decimalPlacesStart = Statistiek.getNumberOfDecimals(startString);//startString.length() - startString.indexOf('.') - 1;
			String binWidthString = String.valueOf(step);
			int decimalPlacesBinWidth = Statistiek.getNumberOfDecimals(binWidthString);//binWidthString.length() - binWidthString.indexOf('.') - 1;
			int numberOfDecimals = Math.max(decimalPlacesStart, decimalPlacesBinWidth);
			for (int i = 0; i <= noBins; i++)
			{
				double d = start + (double) i * step;
				d = round(d, numberOfDecimals);
				boundaries.add(d);
			}
		}
		
		return boundaries;
	}

	/**
	 * Increase the step with a value that is reasonable taking
	 * into account the size of the step.
	 * 
	 * @param step
	 * @return
	 */
	private static double increaseStep(double step)
	{
		double newStep = step;
		boolean found = false;
		
		if (divisibleBy(step, 10)) // tiental
		{
			int count = 0;
			double result = step;
			
			while (divisibleBy(result, 10))
			{
				// deel door 10 tot niet meer mogelijk
				result = result / 10;
				count++;
			}
			
			if (result >= 3)
			{
				// bepaal de nieuwe step, bijv.
				// step = 30  -> newStep = 40
				// step = 300 -> newStep = 400
				newStep = step + Math.pow(10, count);
				found = true;
			}
		}
		
		if (!found)
		{
			if (divisibleBy(step, 5)) // vijftal (inclusief tiental < 30)
			{
				if (step / 5 > 1) // vijftal > 5
				{
					newStep = step + 5;
				}
				else
				{
					// step = 5 -> newStep = 6
					newStep = step + 1;
				}
			}
			else
			{
				newStep = step + 1;
			}
		}
		
		return newStep;
	}

	private static boolean divisibleBy(double step, int factor)
	{
		boolean divisible = false;
		
		if ((int) step % factor == 0)
			divisible = true;
		
		return divisible;
	}

	/**
	 * Determine appropriate bin boundaries from given min, max, bin width and 
	 * the minimum bin boundary, and determine the number of bins.
	 * If the bin boundaries cannot be calculated for the given parameters
	 * null is returned.
	 * 
	 * @param min
	 *            The minimum value in the dataset
	 * @param max
	 *            The maximum value in the dataset
	 * @param binWidth
	 *            The desired bin width
	 * @param minBoundary
	 *            The minimum bin boundary
	 * @return ArrayList containing appropriate bin boundaries. If the bin
	 * boundaries cannot be calculated from the given parameters, null is
	 * returned.
	 */
	public static ArrayList<Double> appropriateBoundariesFromBinSettings(
		double min, double max, double binWidth, double minBoundary)
	{
		ArrayList<Double> boundaries;
		
		if ((min == 0) && (max == 0))
		{
			boundaries = new ArrayList<Double>();
			boundaries.add(0.0);
			boundaries.add(0.0);
		}
		else
		{
			// check if parameters binWidth and minBoundary are valid
			if (((binWidth <= 0) 
				|| (binWidth < (max - min)/100)) 
				&& (max != min)) // if max = min binwidth is not restricted
				return null;
			
			if ((minBoundary > min) 
				&& (max != min)) // if max = min binwidth is not restricted
				return null;
			
			// calculate decimal bin boundaries smaller than 1 
			if (((Math.abs(min) < 1) && (Math.abs(max) < 1))
				|| (((max - min) < 1) && ((max - min) != 0)))
			{
				// use number of decimals of start and binwidth
				double start;
				if (minBoundary <= min)
				{
					start = minBoundary;
				}
				else
				{
					start = min;
				}
	
				String startString = String.valueOf(start);
				int decimalPlacesStart = Statistiek.getNumberOfDecimals(startString);//startString.length() - startString.indexOf('.') - 1;
				String binWidthString = String.valueOf(binWidth);
				int decimalPlacesBinWidth = Statistiek.getNumberOfDecimals(binWidthString);//binWidthString.length() - binWidthString.indexOf('.') - 1;
				int numberOfDecimals = Math.max(decimalPlacesStart, decimalPlacesBinWidth);
	
				min = min * (Math.pow(10, numberOfDecimals)); 
				max = max * (Math.pow(10, numberOfDecimals));
				binWidth = binWidth * (Math.pow(10, numberOfDecimals));
				minBoundary = minBoundary * (Math.pow(10, numberOfDecimals));
				
				ArrayList<Double> binBoundaries = appropriateBoundariesFromBinSettings(min, max, binWidth, minBoundary);
				
				if (binBoundaries != null)
				{
					// divide each bin boundary by Math.pow(10, numberOfDecimals)
					for (int i = 0; i < binBoundaries.size(); i++)
					{
						// divide the bin boundary by Math.pow(10, numberOfDecimals)
						// and round to the correct number of decimals (because of possible rounding errors)
						double newValue = round(binBoundaries.get(i) / (Math.pow(10, numberOfDecimals)), numberOfDecimals);
						binBoundaries.set(i, newValue);
					}
				}
				return binBoundaries; 
			}
			
			double start;
			int noBins;
			
			if (minBoundary <= min)
			{
				start = minBoundary;
			}
			else
			{
				start = min;
			}
			
			if (binWidth == 0)
				binWidth++;
			
			// The maximum bin boundary should be larger than the maximum value
			// so (max + 1) to determine the number of bins
			noBins = (int) Math.ceil(((max + 1) - start)/binWidth);
			while (start + noBins * binWidth <= max)
			{
				noBins++;
			}
	
			// build arraylist
			boundaries = new ArrayList<Double>();
			// correct afronden op basis van decimalen in start en binWidth
			String startString = String.valueOf(start);
			int decimalPlacesStart = Statistiek.getNumberOfDecimals(startString);//startString.length() - startString.indexOf('.') - 1;
			String binWidthString = String.valueOf(binWidth);
			int decimalPlacesBinWidth = Statistiek.getNumberOfDecimals(binWidthString);//binWidthString.length() - binWidthString.indexOf('.') - 1;
			int numberOfDecimals = Math.max(decimalPlacesStart, decimalPlacesBinWidth);
			
			for (int i = 0; i <= noBins; i++)
			{
				double d = start + (double) i * binWidth;
				d = round(d, numberOfDecimals);
				boundaries.add(d);
			}
		}
		
		return boundaries;
	}

	/**
	 * Rounds a number to a certain number of decimals
	 * 
	 * @param number
	 *            the number to round
	 * @param decimals
	 *            the number of decimals to round to. If negative, the number
	 *            will be rounded to zero decimals.
	 * @return the rounded number
	 */
	public static double round(double number, int decimals)
	{
//		number = number * (Math.pow(10, decimals));
//		
//		number = Math.round(number);
//		
//		number = number / (Math.pow(10, decimals));

	    if (decimals < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(number);
	    bd = bd.setScale(decimals, RoundingMode.HALF_UP);
	    number = bd.doubleValue(); 

		return number;
	}
	
	/**
	 * Get the string value of double. If the value is an integer value
	 * a string is returned without decimals.
	 * The decimal format related to the language is used,
	 * with the number of decimals of d.
	 *  
	 * @param d The double value
	 * @return The string value
	 */
	public static String getStringValue(double d)
	{
		String s;
		if ((d == Math.floor(d)) && !Double.isInfinite(d))
			s = String.valueOf((int) d);
		else
		{
			DecimalFormat decimalFormat = Statistiek.getDecimalFormat(d);
			s = String.valueOf(decimalFormat.format(d)); // use decimal format for the correct decimal separator
		}
		
		return s;
	}
	
	/**
	 * Get the string value of double. If the value is an integer value
	 * a string is returned without decimals.
	 * The default decimal format related to language is used, with one decimal.
	 *  
	 * @param d The double value
	 * @return The string value
	 */
	public static String getStringValueWithOneDecimal(double d)
	{
		String s;
		if ((d == Math.floor(d)) && !Double.isInfinite(d))
			s = String.valueOf((int) d);
		else
			s = String.valueOf(df.format(d)); // use the default decimal format for the correct decimal separator
		
		return s;
	}
	
	/**
	 * Get the default decimal format, with one decimal.
	 */
	public static DecimalFormat getDefaultDecimalFormat()
	{
		return df;
	}
	
	/**
	 * Get the decimal format with number of decimals of the given double.
	 * 
	 * @param d The double 
	 */
	public static DecimalFormat getDecimalFormat(Double d)
	{
		String value = String.valueOf(d);
		int numberOfDecimals = Statistiek.getNumberOfDecimals(value);
		
		if (Statistiek.language.toString().equals("nl"))
			dfs.setDecimalSeparator(',');
		else
			dfs.setDecimalSeparator('.');
		String pattern = "0";
		
		if (numberOfDecimals > 0)
			pattern = pattern + ".";
		for (int i = 0; i < numberOfDecimals; i++)
		{
			pattern = pattern + "#"; 
		}
		DecimalFormat decimalFormat = new DecimalFormat(pattern, dfs);

		return decimalFormat;
	}
	
	/**
	 * Parse the double value in doubleString to double 
	 * using the locale language settings.
	 * 
	 * @param doubleString
	 * @return
	 */
	public static double parseDouble(String doubleString)
	{
		double d;
		
		NumberFormat format = NumberFormat.getInstance(Statistiek.getLocale());
		Number number = null;
		try
		{
			number = format.parse(doubleString);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		d = number.doubleValue();
		
		return d;
	}
	
	/**
	 * Get locale with language setting of statistiek.
	 * 
	 * @return
	 */
	public static Locale getLocale()
	{
		return Statistiek.language;
	}
	
	/**
	 * Set locale with language setting of statistiek.
	 * 
	 * @return
	 */
	public static void setLocale(Locale l)
	{
		Statistiek.language = l;
	}
	
	/**
	 * Get the number of decimals of the given double string.
	 * 
	 * @param d
	 * @return
	 */
	public static int getNumberOfDecimals(String doubleString)
	{
		int decimalPlaces = 0;
		boolean isScientificNotation = doubleString.indexOf("E") > -1;
		
		if (!isScientificNotation)
		{
			int integerPlaces = doubleString.indexOf('.');
			if (integerPlaces > -1)
			{
				decimalPlaces = doubleString.length() - integerPlaces - 1;
			}
		}
		else
		{
			// met regular expressions
			String patternString = "(?:\\.(\\d+))?(?:[eE]([+-]?\\d+))";
			
			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(doubleString);
			if (matcher.find())
			{
				// doubleString heeft een goed formaat
				decimalPlaces = 
					// Number of digits right of decimal point.
				    ((matcher.group(1) != null) && (!matcher.group(1).toString().equals("0")) ? matcher.group(1).length() : 0)
				    // Adjust for scientific notation.
				    - (matcher.group(2) != null ? Integer.valueOf(matcher.group(2).toString()) : 0);
			}
		}
		
		return decimalPlaces;
	}
	
	/**
	 * Get the binWidth based on the values in bins. The number of 
	 * decimals of d will be the maximum number of decimals
	 * among the values in bins.
	 * 
	 * @param bins An arraylist of bin boundaries
	 * @return The string value of the bin width
	 */
	public static String getFormattedBinWidth(ArrayList<Double> bins)
	{
		String formattedValueString;
		int maxNumberOfDecimals = 0;
		String binValueString;
		
		Double d = bins.get(1) - bins.get(0);
		
		for (int i = 0; i < bins.size(); i++)
		{
			binValueString = String.valueOf(bins.get(i));
			int numberOfDecimals = Statistiek.getNumberOfDecimals(binValueString);//binValueString.length() - binValueString.indexOf('.') - 1;
			
			if (numberOfDecimals > maxNumberOfDecimals)
				maxNumberOfDecimals = numberOfDecimals;
		}

		// get format with numberOfDecimals
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		char separator = Statistiek.getDecimalSeparator();
		dfs.setDecimalSeparator(separator);
		String pattern = "0";
		
		if (maxNumberOfDecimals > 0)
		{
			// set the pattern according to the number of decimals
			pattern = pattern + ".";
			for (int i = 0; i < maxNumberOfDecimals; i++)
			{
				pattern = pattern + "#";
			}
		}
		DecimalFormat df = new DecimalFormat(pattern, dfs);
		formattedValueString = df.format(d.doubleValue());
		
		return formattedValueString;
	}

	private static char getDecimalSeparator()
	{
		char separator = '.';
		if (language.toString().equals("nl"))
		{
			separator = ',';
		}
		
		return separator;
	}
}
