package fi.statistiek;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JFrame;

import fi.beans.wiskopdrbeans.InteractiePanel;
import fi.beans.wiskopdrbeans.WiskOpdrApplet;
import fi.statistiek.boxplot.BoxplotController;
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
	static DecimalFormatSymbols dfs;
	public static DecimalFormat df;

	// Name all StatistiekViews here, and add them to the createView method
	public static String[] VIEWS;// = {"Table", "Histogram", "Dotplot",
								 // "Frequentietabel", "Frequentiepolygoon",
								 // "Boxplot"};
	public static String[] VIEWS_translated;// = {"Table", "Histogram",
											// "Dotplot", "Frequentietabel",
											// "Frequentiepolygoon", "Boxplot"};

	public Statistiek()
	{
		Locale language = new Locale("nl", "");
		rb = ResourceBundle.getBundle("fi.statistiek.text.Text", language);
		dfs = new DecimalFormatSymbols();
		if (language.toString().equals("nl"))
			dfs.setDecimalSeparator(',');
		else
			dfs.setDecimalSeparator('.');
		df = new DecimalFormat("0.0####", dfs);
		initViews();
	}

	static void initViews()
	{
		VIEWS_translated = new String[6];
		VIEWS_translated[0] = Statistiek.rb.getString("tableOption");
		VIEWS_translated[1] = Statistiek.rb.getString("histogramOption");
		VIEWS_translated[2] = Statistiek.rb.getString("dotplotOption");
		VIEWS_translated[3] = Statistiek.rb.getString("frequencytableOption");
		VIEWS_translated[4] = Statistiek.rb.getString("frequencypolygonOption");
		VIEWS_translated[5] = Statistiek.rb.getString("boxplotOption");

		VIEWS = new String[6];
		VIEWS[0] = "Table";
		VIEWS[1] = "Histogram";
		VIEWS[2] = "Dotplot";
		VIEWS[3] = "Frequentietabel";
		VIEWS[4] = "Frequentiepolygoon";
		VIEWS[5] = "Boxplot";
	}

	public Statistiek(Locale language)
	{
		rb = ResourceBundle.getBundle("fi.statistiek.text.Text", language);
		dfs = new DecimalFormatSymbols();
		if (language.toString().equals("nl"))
			dfs.setDecimalSeparator(',');
		else
			dfs.setDecimalSeparator('.');
		df = new DecimalFormat("0.0####", dfs);
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
		StatEditPanelController c = new StatEditPanelController();
		Hashtable oudeState = c.getEditState();

		try
		{
			FileInputStream fin = new FileInputStream("C:\\StatistiekIrisSet.epic");
			ObjectInputStream ois = new ObjectInputStream(fin);
			Hashtable h = (Hashtable) ois.readObject();
			c.setEditState(h);
			System.out.println("Iris dataset loaded");
			ois.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			c.setEditState(oudeState);
		}

		frame.setContentPane(c);

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
		StatTableModel model, int startVar,
		StatInteractiePanel statInteractiePanel)
	{
		System.out.println("Statistiek.createView(viewType=" + viewType + ", viewName=" + viewName 
			+ ", identityHashCode(statTableModel)=" + identityHashCode(model) + ")");

		// VIEWS[0]=Statistiek.rb.getString("tableOption");
		// VIEWS[1]=Statistiek.rb.getString("histogramOption");
		// VIEWS[2]=Statistiek.rb.getString("dotplotOption");
		// VIEWS[3]=Statistiek.rb.getString("frequencytableOption");
		// VIEWS[4]=Statistiek.rb.getString("frequencypolygonOption");
		// VIEWS[5]=Statistiek.rb.getString("boxplotOption");

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
	public static Container getTopLevelAcestor(JComponent c)
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
	 * bins
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
		/*
		 * if(noBins == 1) { ArrayList<Double> ret = new ArrayList<Double>(2);
		 * ret.add(new Double(min)); ret.add(new Double(max)); return ret; }
		 */
		double b = (max - min) / (double) (Math.max(noBins - 1, 1));
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
		if (step == b)
		{
			step++;
		}

		// System.out.println("e = " + e);
		// System.out.println("step = " + step);

		double start = (Math.ceil(min / step) - 1) * step;

		if ((start + noBins * step) <= max)
		{
			step = (1 + Math.ceil(b * Math.pow(10, -e))) * Math.pow(10, e);
		}

		// build arraylist
		ArrayList<Double> boundaries = new ArrayList<Double>();
		for (int i = 0; i <= noBins; i++)
		{
			double d = start + (double) i * step;
			d = round(d, -e);
			boundaries.add(d);
		}
		return boundaries;
	}

	/**
	 * Determine appropriate bin boundaries from given min, max and 
	 * bin width, and determine the number of bins.
	 * 
	 * @param min
	 *            The minimum value in the dataset
	 * @param max
	 *            The maximum value in the dataset
	 * @param binWidth
	 *            The desired bin width
	 * @return ArrayList containing appropriate bin boundaries
	 */
	public static ArrayList<Double> appropriateBoundariesFromBinWidth(
		double min,	double max, double binWidth)
	{
		double start;
		int noBins;
		
		start = Math.floor(min - 1);
		
		// The maximum bin boundary should be larger than the maximum value
		// so (max + 1) to determine the number of bins
		noBins = (int) Math.ceil(((max + 1) - start)/binWidth);

		// build arraylist
		ArrayList<Double> boundaries = new ArrayList<Double>();
		
		for (int i = 0; i <= noBins; i++)
		{
			double d = start + (double) i * binWidth;
			d = round(d, -0);
			boundaries.add(d);
		}

		return boundaries;
	}
	
	/**
	 * Determine appropriate bin boundaries from given min, max and 
	 * the minimum bin boundary, and determine the number of bins.
	 * 
	 * @param min
	 *            The minimum value in the dataset
	 * @param max
	 *            The maximum value in the dataset
	 * @param minBoundary
	 *            The minimum bin boundary
	 * @return ArrayList containing appropriate bin boundaries
	 */
	public static ArrayList<Double> appropriateBoundariesFromMinBoundary(
		double min, double max, double minBoundary)
	{
		double start;
		int noBins;
		
		start = minBoundary;
		
		// TODO: hieronder aanpassen...
		
		// The maximum bin boundary should be larger than the maximum value
		// so (max + 1) to determine the number of bins
//		noBins = (int) Math.ceil(((max + 1) - start)/binWidth);
//
//		// build arraylist
		ArrayList<Double> boundaries = new ArrayList<Double>();
//		
//		for (int i = 0; i <= noBins; i++)
//		{
//			double d = start + (double) i * binWidth;
//			d = round(d, -0);
//			boundaries.add(d);
//		}

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
		for (int i = 0; i < decimals; i++)
		{
			number *= 10;
		}
		
		number = Math.round(number);
		
		for (int i = 0; i < decimals; i++)
		{
			number /= (double) 10;
		}
		
		return number;
	}
}
