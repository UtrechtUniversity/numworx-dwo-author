package fi.statistiek.dotplot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import fi.statistiek.ColorGenerator;
import fi.statistiek.ColorLegend;
import fi.statistiek.ColorPreviewer;
import fi.statistiek.DialogButton;
import fi.statistiek.StatTableModel;
import fi.statistiek.Statistiek;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * MVC View for StatistiekView Dotplot
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class DotplotView extends JPanel implements Observer
{
	private DotplotModel model;
	private DotplotController controller;
	private DotplotUserOptionsPanel userOptionsPanel;
	private DialogButton dialogButton;

	// Constants
	public static final int KEUZEBALK_HOOGTE = 50;
	public static final int X_AS_OFFSET = 25;
	/**
	 * The part below the minimum value and above the maximum value respectively 
	 * that serves to create extra space. For example, 0.05 is 5% of the 
	 * space available for drawing the dots. 
	 */
	public static final double KEEP_CLEAR_PART = 0.05;
	public static final Color SELECTION_RECTANGLE_COLOR = new Color(153, 204,
		255); // blue
	
	// Table with critical values of the Pearson's product-moment 
	// correlation coefficient. 
	// See also, method getLevelOfSignificance(r, N).
	// Column 4: if r > SIGNIFICANCE_TABLE[n][3] then p < 0.001
	// Column 3: if r > SIGNIFICANCE_TABLE[n][2] then p < 0.01
	// Column 2: if r > SIGNIFICANCE_TABLE[n][1] then p < 0.05
	// Column 1: if r > SIGNIFICANCE_TABLE[n][0] then p < 0.1
	//			 if r < SIGNIFICANCE_TABLE[n][0] then p > 0.1
	// Source: http://faculty.fortlewis.edu/CHEW_B/Documents/Table%20of%20critical%20values%20for%20Pearson%20correlation.htm
	private static final double[][] SIGNIFICANCE_TABLE = {
		{0.988, 0.997, 0.9999, 0.99999}, // N = 3 (i = 0)
		{0.900, 0.950, 0.990, 0.999}, // N = 4 (i = 1)
		{0.805, 0.878, 0.959, 0.991}, // N = 5 (i = 2)
		{0.729, 0.811, 0.917, 0.974}, // N = 6 (i = 3)
		{0.669, 0.754, 0.875, 0.951}, // N = 7 (i = 4)
		{0.621, 0.707, 0.834, 0.925}, // N = 8 (i = 5)
		{0.582, 0.666, 0.798, 0.898}, // N = 9 (i = 6)
		{0.549, 0.632, 0.765, 0.872}, // N = 10 (i = 7)
		{0.521, 0.602, 0.735, 0.847}, // N = 11 (i = 8)
		{0.497, 0.576, 0.708, 0.823}, // N = 12 (i = 9)
		{0.476, 0.553, 0.684, 0.801}, // N = 13 (i = 10)
		{0.458, 0.532, 0.661, 0.780}, // N = 14 (i = 11)
		{0.441, 0.514, 0.641, 0.760}, // N = 15 (i = 12)
		{0.426, 0.497, 0.623, 0.742}, // N = 16 (i = 13)
		{0.412, 0.482, 0.606, 0.725}, // N = 17 (i = 14)
		{0.400, 0.468, 0.590, 0.708}, // N = 18 (i = 15)
		{0.389, 0.456, 0.575, 0.693}, // N = 19 (i = 16)
		{0.378, 0.444, 0.561, 0.679}, // N = 20 (i = 17)
		{0.369, 0.433, 0.549, 0.665}, // N = 21 (i = 18)
		{0.360, 0.423, 0.537, 0.652}, // N = 22 (i = 19)
		{0.352, 0.413, 0.526, 0.640}, // N = 23 (i = 20)
		{0.344, 0.404, 0.515, 0.629}, // N = 24 (i = 21)
		{0.337, 0.396, 0.505, 0.618}, // N = 25 (i = 22)
		{0.330, 0.388, 0.496, 0.607}, // N = 26 (i = 23)
		{0.323, 0.381, 0.487, 0.597}, // N = 27 (i = 24)
		{0.317, 0.374, 0.479, 0.588}, // N = 28 (i = 25)
		{0.311, 0.367, 0.471, 0.579}, // N = 29 (i = 26)
		{0.306, 0.361, 0.463, 0.570}, // N = 30 (i = 27)
		{0.283, 0.334, 0.430, 0.532}, // N = 35 (i = 28)
		{0.264, 0.312, 0.403, 0.501}, // N = 40 (i = 29)
		{0.248, 0.294, 0.380, 0.474}, // N = 45 (i = 30)
		{0.235, 0.279, 0.361, 0.451}, // N = 50 (i = 31)
		{0.214, 0.254, 0.330, 0.414}, // N = 60 (i = 32)
		{0.198, 0.235, 0.306, 0.385}, // N = 70 (i = 33)
		{0.185, 0.220, 0.286, 0.361}, // N = 80 (i = 34)
		{0.174, 0.207, 0.270, 0.341}, // N = 90 (i = 35)
		{0.165, 0.197, 0.256, 0.324}, // N = 100 (i = 36)
		{0.117, 0.139, 0.182, 0.231}, // N = 200 (i = 37)
		{0.095, 0.113, 0.149, 0.189}, // N = 300 (i = 38)
		{0.082, 0.098, 0.129, 0.164}, // N = 400 (i = 39)
		{0.074, 0.088, 0.115, 0.147}, // N = 500 (i = 40)
		{0.052, 0.062, 0.081, 0.104} // N = 1000 (i = 41)
	};

	public int yAxisOffset = 55;
	/**
	 * Correction used to determine the x coordinate of a number value.
	 */
	private int xCoordCorrection = 0;

	private DotClickListener dotClickListener;

	private JScrollPane scrollPane;

	// variables for painting, set every time paint is called
	private double xMin;
	private double xMax;
	private double xFirstMinorStep; // field to be used in determineXCoordNumClass()
	private double yMin;
	private double yMax;
	private double zMin;
	private double zMax;
	private double splitMin;
	private double splitMax;
	private int dotSize;
	private AllowedTypes xType;
	private AllowedTypes yType;
	private AllowedTypes zType;
	private AllowedTypes splitType;

	private ArrayList<Point> objectLocations;
	private int splitClasses;
	private JPanel mainPanel;

	private ColorLegend colorLegend;

	/**
	 * Constructor
	 * 
	 * @param model
	 *            MVC Model
	 * @param controller
	 *            MVC Controller
	 */
	public DotplotView(DotplotModel model, DotplotController controller)
	{
		super(new BorderLayout());
		this.model = model;
		this.model.addObserver(this);
		this.controller = controller;

		userOptionsPanel = new DotplotUserOptionsPanel(this, controller, model);
		dialogButton = userOptionsPanel.getDialogButton();

		super.add(dialogButton, BorderLayout.SOUTH);
		// dialogButton.makeDialog();

		this.mainPanel = new DotPanel();
		this.scrollPane = new JScrollPane(this.mainPanel);
		this.scrollPane.getVerticalScrollBar().setUnitIncrement(Statistiek.scrollSpeedUnit);
		super.add(this.scrollPane, BorderLayout.CENTER);

		this.dotClickListener = new DotClickListener();
		this.mainPanel.addMouseListener(this.dotClickListener);
		this.mainPanel.addMouseMotionListener(this.dotClickListener);
		// test syl: onderstaande werkt niet...
//		this.mainPanel.setBackground(Statistiek.backgroundColor); // is wit, maar wordt niet wit...
//		this.setBackground(Statistiek.backgroundColor);
		this.mainPanel.setOpaque(true);
		this.mainPanel.setBackground(Color.WHITE);
		super.setOpaque(true);
		super.setBackground(Color.WHITE);
		if (SwingUtilities.getWindowAncestor(this) != null)
		{
			System.out.println("DotplotView(): SwingUtilities.getWindowAncestor(this) != null");
			SwingUtilities.getWindowAncestor(this).setOpacity(1);
			SwingUtilities.getWindowAncestor(this).setBackground(Color.WHITE);
		}
		this.scrollPane.setOpaque(true);
		this.scrollPane.setBackground(Color.WHITE);
		
		this.colorLegend = new ColorLegend("", null, null);
		super.add(this.colorLegend, BorderLayout.EAST);
		// this.colorLegend.setVisible(false);
	}

	/**
	 * Get the string options of column X.
	 * @return
	 */
	private ArrayList<String> getXStringOptions()
	{
		return this.model.getTableModel().getStringOptions(
			this.model.getColumnXIndex());
	}

	/**
	 * Get the string options of column Y.
	 * @return
	 */
	private ArrayList<String> getYStringOptions()
	{
		return this.model.getTableModel().getStringOptions(
			this.model.getColumnYIndex());
	}

	private ArrayList<String> getColorStringOptions()
	{
		return this.model.getTableModel().getStringOptions(
			this.model.getColumnColorIndex());
	}

	private ArrayList<String> getSplitStringOptions()
	{
		return this.model.getTableModel().getStringOptions(
			this.model.getColumnSplitIndex());
	}

	/**
	 * Determine the width of the area where the dots are painted
	 * 
	 * @return the width of the area where the dots are painted
	 */
	private int dotAreaWidth()
	{
		int w = this.getWidth() - 20
			- (this.colorLegend.isVisible() ? this.colorLegend.getWidth() : 0);
		
		//System.out.println("DotplotView.dotAreaWidth(): w = " + w);
		
		if (this.model.columnYIndexValid())
		{
			w -= this.yAxisOffset;
		}
		if (this.splitClasses > 1)
		{
			w -= 20;
		}
		return w;
	}

	/**
	 * Determine the height of the area where the dots are painted
	 * 
	 * @return the height of the area where the dots are painted
	 */
	private int dotAreaHeight()
	{
		int h = this.getHeight();
		
		/**
		 * TODO: Het gebruik van X_AS_OFFSET en KEUZEBALK_HOOGTE moet beter worden uitgezocht.
		 * this.getHeight() is niet de hoogte inclusief keuzebalk.
		 * Het ziet er nu acceptabel uit. StatistiekGWT heeft het
		 * ook weer anders gefixt. Even laten voor wat het is...
		 */

		if (this.model.columnXIndexValid())
		{
			//h -= DotplotView.X_AS_OFFSET;
			h -= 50;
		}
		if (this.model.getTableModel().isViewsEditable())
		{
//			h -= DotplotView.KEUZEBALK_HOOGTE;
			h -= 25;
		}
		return h;
	}

	public void setModel(DotplotModel model)
	{
		this.model = model;
		this.model.addObserver(this);
		userOptionsPanel.setModel(model);
		this.update(null, null);
	}

	@Override
	public void setBounds(int x, int y, int w, int h)
	{
//		System.out.println("DotplotView.setBounds(x=" + x + ", y=" + y 
//			+ ", w=" + w + ", h=" + h + ")");

		super.setBounds(x, y, w, h);

		this.setMainPanelSize();
		
		this.scrollPane.setViewportView(mainPanel);

		// System.out.println("DotplotView.setBounds(): Size histogram: " +
		// this.getBounds().toString()
		// + ", scrollbarVisible=" +
		// scrollPane.getVerticalScrollBar().isVisible());
		
//		 System.out.println("DotplotView.setBounds(): scrollPane w="
//			 + scrollPane.getWidth()
//			 + ", h=" + scrollPane.getHeight());
	}

	private void setMainPanelSize()
	{
		int colorLegendWidth = this.colorLegend.isVisible() ? this.colorLegend
			.getPreferredSize().width : 0;
			
		int preferredWidth = this.scrollPane.getWidth() - colorLegendWidth - 20;
		int preferredHeight = this.determinePreferredHeight();
		
		this.mainPanel.setPreferredSize(new Dimension(preferredWidth, preferredHeight));
		
//		System.out.println("DotplotView.setMainPanelSize(): preferredWidth = "
//			+ preferredWidth + ", preferredHeight = " + preferredHeight);
	}

	private int determinePreferredHeight()
	{
		int splitClasses = this.model.getTableModel().splitVarClasses(
			this.model.getSplitOptions());
		int preferredHeight = 0;

		if (this.model.splitInSingleView())
		{
			preferredHeight = this.scrollPane.getHeight() - 5;
		}
		else
		{
			preferredHeight = splitClasses * (this.scrollPane.getHeight() - 5) + 1;
		}
		return preferredHeight;
	}

	private boolean updateColorLegend()
	{
		int splitClasses = this.model.getTableModel().splitVarClasses(
			this.model.getSplitOptions());
		if (splitClasses > 1)
		{
			this.colorLegend.setColumnString(this.model.getTableModel()
				.getColumnName(
					this.model.getSplitOptions().getColumnSplitIndex()));
			ArrayList<String> splitStrings = new ArrayList<String>(splitClasses);
			ArrayList<Color> splitColors = new ArrayList<Color>(splitClasses);
			for (int i = 0; i < splitClasses; i++)
			{
				splitStrings.add(this.model.getSplitOptions()
					.getSplitClassLabel(i, this.model.getTableModel()));
				splitColors.add(this.getColor(i));
			}
			this.colorLegend.setColors(splitStrings, splitColors);
			if (!this.colorLegend.isVisible())
			{
				this.colorLegend.setVisible(true);
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			if (this.colorLegend.isVisible())
			{
				// System.out.println("aaaa");

				this.colorLegend.setVisible(false);
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	public void update(Observable arg0, Object arg1)
	{
//		System.out.println("DotplotView.update()");
		
		this.setTypes();
		this.setMinMax();

		if (this.model.columnSplitIndexValid())
		{
			this.setSplitType();
			splitClasses = this.numberOfSplitClasses();
		}
		else
		{
			splitClasses = 1;
		}

		this.dialogButton.setVisible(this.model.getTableModel()
			.isViewsEditable());

		// updateColorLegend() retourneert boolean, maar voert ook update uit
		this.updateColorLegend();
		this.setMainPanelSize();

		userOptionsPanel.update();
		
		updateOffsets();
		
		// test syl: onderstaande zorgt voor opblazen window bij wijziging in edit mode
//		if (SwingUtilities.getWindowAncestor(this.scrollPane) != null)
//			SwingUtilities.getWindowAncestor(this.scrollPane).pack();

		this.mainPanel.revalidate();

		this.repaint();
		
//		System.out.println("DotplotView.update() mainPanel.bg RGB = " 
//			+ this.mainPanel.getBackground().getRed() + ", " + this.mainPanel.getBackground().getGreen()
//			+ ", " + this.mainPanel.getBackground().getBlue());
	}

	/**
	 * Update y-axis offsets depending on 
	 */
	private void updateOffsets()
	{
		int max = 0;
		int yIndex = this.model.getColumnYIndex();
		
		// update offset y-axis
		if (yIndex > -1)
		{
			ColumnType columnType = this.model.getTableModel().getColumnTypes()
				.get(yIndex);
			
			Font font = super.getFont().deriveFont(super.getFont().getStyle());
			FontMetrics fm = super.getFontMetrics(font);
			
			if (yType.equals(AllowedTypes.ENUM))
			{
				// determine max width of the enum option labels
				for (int i = 0; i < columnType.getEnumOptions().length; i++)
				{
					String option = columnType.getEnumOptions()[i];
					if (fm.stringWidth(option) > max)
					{
						max = fm.stringWidth(option) + 45; // + 45 for label, tick-width and some extra space
//						System.out.println("DotplotView.updateOffsets(): option = " 
//							+ option + ", max = " + max);
					}
					
					// test syl: bovenstaande werkt, maar + 45 is willekeurig. Om een of andere reden is er voor
					// langere enum-klassen-labels meer ruimte nodig dan voor kortere...
				}
			}
			else if (yType.equals(AllowedTypes.STRING))
			{
				// determine max width of the string option labels
				int amountOfOptions = this.getYStringOptions().size();

				// draw ticks and help line
				for (int i = 0; i < amountOfOptions; i++)
				{
					String option = this.getYStringOptions().get(i);
					if (fm.stringWidth(option) > max)
					{
						max = fm.stringWidth(option) + 45; // + 45 for label, tick-width and some extra space
//						System.out.println("DotplotView.updateOffsets(): option = " 
//							+ option + ", max = " + max);
					}
					
					// test syl: bovenstaande werkt, maar + 45 is willekeurig. Om een of andere reden is er voor
					// langere enum-klassen-labels meer ruimte nodig dan voor kortere...
				}
			}
			else
			{
				max = 55;
			}
		} // there is a y-axis variable
		else
		{
			max = 55;
		}

		this.yAxisOffset = max;
//		System.out.println("DotplotView.updateOffsets(): Y_AS_OFFSET = " + yAxisOffset);
	}

	public boolean isSplitSingleViewSelected()
	{
		return userOptionsPanel.isSplitSingleViewSelected();
	}

	public boolean getUseColorScaleBoxSelected()
	{
		return userOptionsPanel.isUseColorScaleBoxSelected();
	}

	public int getVarXBoxSelected()
	{
		return userOptionsPanel.getVarXBoxSelectedIndex();
	}

	public int getVarYBoxSelected()
	{
		return userOptionsPanel.getVarYBoxSelectedIndex();
	}

	public int getVarColorBoxSelected()
	{
		return userOptionsPanel.getVarColorBoxSelectedIndex();
	}

	public int getSplitVarBoxSelectedIndex()
	{
		return userOptionsPanel.getSplitVarBoxSelectedIndex();
	}

	public int getSplitBinsBoxSelectedInt()
	{
		return userOptionsPanel.getSplitBinsBoxSelectedInt();
	}

	public double getSplitMinBoundary()
	{
		return userOptionsPanel.getSplitMinBoundary();
	}

	/**
	 * Set min boundary with value min
	 * @param min
	 */
	public void setSplitMinBoundary(double min)
	{
		this.userOptionsPanel.setSplitMinBoundary(min);
	}

	public double getSplitBinWidth()
	{
		return userOptionsPanel.getSplitBinWidth();
	}

	public void setSplitBinWidth(double d)
	{
		this.userOptionsPanel.setSplitBinWidth(d);
	}

	public void setSplitBinWidth()
	{
		this.userOptionsPanel.setSplitBinWidth();
	}

	// public int getVarSplitBoxSelected() {
	// return this.varSplitBox.getSelectedIndex();
	// }

	public boolean getShowCorrelationBoxSelected()
	{
		return userOptionsPanel.isShowCorrelationBoxSelected();
	}

	/*
	 * public int getSplitBinsBoxSelectedInt() { return
	 * ((Integer)this.splitBinsBox.getSelectedItem()).intValue(); }
	 */

	/**
	 * Sets the current columnTypes to thix.xType, this.yType and this.zType
	 */
	private void setTypes()
	{
		if (this.model.columnXIndexValid())
		{
			this.xType = this.model.getTableModel().getColumnTypes()
				.get(this.model.getColumnXIndex()).getType();
		}
		if (this.model.columnYIndexValid())
		{
			this.yType = this.model.getTableModel().getColumnTypes()
				.get(this.model.getColumnYIndex()).getType();
		}

		// if(this.model.isUseColorScale()) {
		if (this.model.columnZIndexValid())
		{
			this.zType = this.model.getTableModel().getColumnTypes()
				.get(this.model.getColumnColorIndex()).getType();
		}

		if (this.model.columnSplitIndexValid())
		{
			this.setSplitType();
		}
	}

	private void setSplitType()
	{
		this.splitType = this.model.getTableModel().getColumnTypes()
			.get(this.model.getColumnSplitIndex()).getType();
	}

	/**
	 * Find the current min and max value for every numeric column
	 */
	private void setMinMax()
	{
		if (this.model.columnXIndexValid() && this.xType.isNumber())
		{
			xMin = this.model.getTableModel().getColumnMin(
				this.model.getColumnXIndex());
			xMax = this.model.getTableModel().getColumnMax(
				this.model.getColumnXIndex());
		}
		if (this.model.columnYIndexValid() && this.yType.isNumber())
		{
			yMin = this.model.getTableModel().getColumnMin(
				this.model.getColumnYIndex());
			yMax = this.model.getTableModel().getColumnMax(
				this.model.getColumnYIndex());
		}
		// if(this.model.isUseColorScale() && this.zType.isNumber()) {
		if (this.model.columnZIndexValid() && this.zType.isNumber())
		{
			zMin = this.model.getTableModel().getColumnMin(
				this.model.getColumnColorIndex());
			zMax = this.model.getTableModel().getColumnMax(
				this.model.getColumnColorIndex());
		}
		if (this.model.columnSplitIndexValid() && this.splitType.isNumber())
		{
			splitMin = this.model.getTableModel().getColumnMin(
				this.model.getColumnSplitIndex());
			splitMax = this.model.getTableModel().getColumnMax(
				this.model.getColumnSplitIndex());
		}
	}

	/**
	 * Calculate how large the dots should be
	 */
	private void determineDotSize()
	{
		double d = (this.getWidth() * this.getHeight())
			/ (double) Math.max(10, this.model.getTableModel().getRowCount());
		this.dotSize = Math.max(2, (int) (0.25 * Math.pow(d, 1.0 / 3.0)));
	}

	/**
	 * Determine what color the dot representing an object should be
	 * 
	 * @param pointIndex
	 *            The index of the object
	 * @return The color in which the dot representing the object will be
	 *         painted
	 */
	private Color determineColor(int pointIndex)
	{
		if (this.model.columnSplitIndexValid())
		{
			String s = (String) this.model.getTableModel().getValueAt(
				pointIndex, this.model.getColumnSplitIndex());
			if (s.equals(ColumnType.WILDCARD))
			{
				// TODO wildcard kleur?
				return Color.PINK;
			}
			else
			{
				if (this.splitType.equals(AllowedTypes.DOUBLE)
					|| this.splitType.equals(AllowedTypes.INTEGER))
				{
					// determine color for numeric type objects
					// double value = Double.parseDouble(s);
					// ArrayList<Double> Boundaries =
					// this.model.getSplitOptions().getBinBoundaries();
					// for(int i=1 ; i<Boundaries.size() ; i++){
					// if(value < Boundaries.get(i)){
					// return getColor(getSplitClass(pointIndex)-1);
					// }
					// }
					return getColor(getSplitClass(pointIndex));
					// return ColorPreviewer.mixColors(this.model.getColorA(),
					// this.model.getColorB(),
					// (value-this.zMin)/(this.zMax-this.zMin));
				}
				else if (this.splitType.equals(AllowedTypes.ENUM))
				{
					// determine color for enum type objects
					ColumnType columnType = this.model.getTableModel()
						.getColumnTypes().get(this.model.getColumnSplitIndex());
					int index = columnType.indexOfStringInEnum(s);
					if (columnType.indexOfStringInEnum(ColumnType.WILDCARD) < index)
					{
						index--;
					}
					return getColor(index);
				}
				else
				{
					// determine color for string type objects
					int index = this.getSplitStringOptions().indexOf(s);
					return getColor(index);
				}
			}
		}

		else if (this.model.columnZIndexValid())
		{
			String s = (String) this.model.getTableModel().getValueAt(
				pointIndex, this.model.getColumnColorIndex());
			if (s.equals(ColumnType.WILDCARD))
			{
				// TODO wildcard kleur?
				return Color.PINK;
			}
			else
			{
				if (this.zType.equals(AllowedTypes.DOUBLE)
					|| this.zType.equals(AllowedTypes.INTEGER))
				{
					// determine color for numeric type objects
					double value = Double.parseDouble(s);
					return ColorPreviewer.mixColors(this.model.getColorA(),
						this.model.getColorB(), (value - this.zMin)
							/ (this.zMax - this.zMin));
				}
				else if (this.zType.equals(AllowedTypes.ENUM))
				{
					// determine color for enum type objects
					ColumnType columnType = this.model.getTableModel()
						.getColumnTypes().get(this.model.getColumnColorIndex());
					int index = columnType.indexOfStringInEnum(s);
					if (columnType.indexOfStringInEnum(ColumnType.WILDCARD) < index)
					{
						index--;
					}
					return ColorPreviewer
						.mixColors(
							this.model.getColorA(),
							this.model.getColorB(),
							(double) index
								/ (double) (columnType.getEnumOptions().length - 2));
				}
				else
				{
					// determine color for string type objects
					int index = this.getColorStringOptions().indexOf(s);
					return ColorPreviewer
						.mixColors(
							this.model.getColorA(),
							this.model.getColorB(),
							(double) index
								/ (double) (this.getColorStringOptions().size() - 1));
				}
			}
		}
		
		// if not using a colorscale, the color is the default dot color
		return ColorGenerator.DEFAULT_VIEW_ELEMENT_COLOR;
	}

	/**
	 * Determine the x coordinate of a dot representing an object
	 * in the dotplot or scatterplot.
	 * 
	 * @param pointIndex
	 *            the index of the object represented by the dot
	 * @return The x-coordinate of the location where the dot has to be painted
	 */
	private int determineXCoord(int pointIndex)
	{
		String valueString = (String) this.model.getTableModel().getValueAt(
			pointIndex, this.model.getColumnXIndex());
		if (valueString.equals(ColumnType.WILDCARD))
		{
			return -1;
		}

		if (this.xType == null)
		{
			System.out.println("xType is null");
		}

		if (this.xType.equals(AllowedTypes.DOUBLE)
			|| this.xType.equals(AllowedTypes.INTEGER))
		{
			double d = Double.parseDouble(valueString);
			return this.determineXCoordNumClass(d);
		}
		else if (this.xType.equals(AllowedTypes.ENUM))
		{
			return this.determineXCoordEnumClass(valueString);
		}
		else
		{
			return this.determineXCoordStringClass(valueString);
		}
	}

	/**
	 * Determine the x coordinate of a numeric value in the dotplot or scatterplot.
	 * 
	 * @param d
	 *            a numeric value
	 * @return the x-coordinate of where the value would be painted
	 */
	private int determineXCoordNumClass(double d)
	{		
		int drawWidth = this.dotAreaWidth();

		// determine the minimum value on the scale
		double minValue;
		
		// if possible take the first minor step
		if (!Double.isNaN(this.xFirstMinorStep))
			minValue = this.xFirstMinorStep;
		else
			minValue = this.xMin;
		
		int x = (int) ((DotplotView.KEEP_CLEAR_PART + (1 - 2 * DotplotView.KEEP_CLEAR_PART)
			* ((d - minValue) / (xMax - minValue))) * drawWidth);
	
		if (this.model.columnYIndexValid())
		{
			if (x < 0)
			{
				// The x coordinate should be in the dot area (x >= 0), else make a correction to shift x.
				// This correction is applied in determining the x coordinate for all values
				this.xCoordCorrection = -x;
			}
			x = x + this.yAxisOffset + this.xCoordCorrection;
		}
//		System.out.println("DotplotView.determineXCoordNumClass(" + d + "): x = " + x);
		return x;
	}

	/**
	 * Determine where an enum value would be painted in the scatterplot
	 * 
	 * @param value
	 *            the enum value
	 * @return the x-coordinate of where the value would be painted
	 */
	private int determineXCoordEnumClass(String value)
	{
		ColumnType cType = this.model.getTableModel().getColumnTypes()
			.get(this.model.getColumnXIndex());

		if (cType.getEnumOptions().length == 2) // including '*' which is not shown, so 1 class
		{
			// the middle of the field
			if (this.model.columnYIndexValid())
			{
				return this.yAxisOffset
					+ (this.mainPanel.getWidth() - this.yAxisOffset) / 2;
			}
			else
			{
				return this.mainPanel.getWidth() / 2;
			}
		}
		
		int index = cType.indexOfStringInEnum(value);

		double d = (double) (index + 1) / (double) cType.getEnumOptions().length;

		int x = 0;

		if (this.model.columnYIndexValid())
		{
			x = (int) (d * (this.mainPanel.getWidth() - this.yAxisOffset)) + this.yAxisOffset;
			//System.out.println("DotplotView.determineXCoordEnumClass(" + value + ")");
		}
		else
		{
			x = (int) (d * this.mainPanel.getWidth());
			//System.out.println("DotplotView.determineXCoordEnumClass(" + value + "): x = " + x);
		}

		return x;
	}

	/**
	 * Determine where a String value would be painted in the scatterplot
	 * 
	 * @param d
	 *            a String value
	 * @return the x-coordinate of where the value would be painted
	 */
	private int determineXCoordStringClass(String value)
	{
		if (this.getXStringOptions().size() == 1)
		{
			if (this.model.columnYIndexValid())
			{
				return this.yAxisOffset
					+ (this.mainPanel.getWidth() - this.yAxisOffset) / 2;
			}
			else
			{
				return this.mainPanel.getWidth() / 2;
			}
		}
		
		double d = (double) (this.getXStringOptions().indexOf(value) + 1)
			/ (double) (this.getXStringOptions().size() + 1);
		
		int x = 0;

		if (this.model.columnYIndexValid())
		{
			x = (int) (d * (this.mainPanel.getWidth() - this.yAxisOffset)) + this.yAxisOffset;
		}
		else
		{
			x = (int) (d * this.mainPanel.getWidth());
		}
		
		return x;
	}

	/**
	 * Determine the y coordinate of a dot representing an object
	 * in the dotplot or scatterplot.
	 * 
	 * @param pointIndex
	 *            the index of the object represented by the dot
	 * @return The y-coordinate of the location where the dot has to be painted
	 */
	private int determineYCoord(int pointIndex)
	{
		String valueString = (String) this.model.getTableModel().getValueAt(
			pointIndex, this.model.getColumnYIndex());

		if (valueString.equals(ColumnType.WILDCARD))
		{
			return -1;
		}

		if (this.yType.equals(AllowedTypes.DOUBLE)
			|| this.yType.equals(AllowedTypes.INTEGER))
		{
			double d = Double.parseDouble(valueString);

			return this.determineYCoordNumClass(d);
		}
		else if (this.yType.equals(AllowedTypes.ENUM))
		{
			return this.determineYCoordEnumClass(valueString);

		}
		else
		{
			return this.determineYCoordStringClass(valueString);
		}
	}

	/**
	 * Determine where a numeric value would be painted in the scatterplot.
	 * 
	 * @param d
	 *            a numeric value
	 * @return the y-coordinate of where the value would be painted
	 */
	private int determineYCoordNumClass(double d)
	{
		int drawHeight = this.dotAreaHeight();
		return (int) (DotplotView.KEEP_CLEAR_PART * drawHeight + (1 - (d - yMin)
			/ (yMax - yMin))
			* (1 - 2 * DotplotView.KEEP_CLEAR_PART) * drawHeight);
	}

	/**
	 * Determine where an enum value would be painted in the scatterplot
	 * 
	 * @param value
	 *            the enum value
	 * @return the y-coordinate of where the value would be painted
	 */
	private int determineYCoordEnumClass(String value)
	{
		ColumnType cType = this.model.getTableModel().getColumnTypes()
			.get(this.model.getColumnYIndex());
		int drawHeight = this.dotAreaHeight();

		if (cType.getEnumOptions().length == 2)
		{
			return drawHeight / 2;
		}

		int index = cType.indexOfStringInEnum(value);
		if (cType.indexOfStringInEnum(ColumnType.WILDCARD) < index)
		{
			index--;
		}
		double d = (double) index
			/ (double) (cType.getEnumOptions().length - 2);
		return (int) (DotplotView.KEEP_CLEAR_PART * drawHeight + (1 - d)
			* (1 - 2 * DotplotView.KEEP_CLEAR_PART) * drawHeight);
	}

	/**
	 * Determine where a String value would be painted in the scatterplot
	 * 
	 * @param value
	 *            the String value
	 * @return the y-coordinate of where the value would be painted
	 */
	private int determineYCoordStringClass(String value)
	{
		int drawHeight = this.dotAreaHeight();

		if (this.getYStringOptions().size() == 1)
		{
			return drawHeight / 2;
		}
		int index = this.getYStringOptions().indexOf(value);
		double d = (double) index
			/ (double) (this.getYStringOptions().size() - 1);
		return (int) (DotplotView.KEEP_CLEAR_PART * drawHeight + (1 - d)
			* (1 - 2 * DotplotView.KEEP_CLEAR_PART) * drawHeight);
	}

	/**
	 * Paint a single point
	 * 
	 * @param g
	 *            the graphics in which the point will be painted
	 * @param rowIndex
	 *            The index of the object that will be painted
	 */
	private void drawPoint(Graphics2D g, int rowIndex)
	{
		// Determine painting location
		int x = this.determineXCoord(rowIndex);
		int y = this.determineYCoord(rowIndex);
		int splitClass = this.getSplitClass(rowIndex);

		if (x < 0 || y < 0 || splitClass < 0)
		{
			return;
		}
		else
		{
			if (this.model.columnSplitIndexValid())
			{
				if (this.model.splitInSingleView())
				{
					y += 0;
				}
				else
				{
					y += (splitClass) * (this.scrollPane.getHeight() - 5);
				}
			}
			drawPointAtLocation(g, x, y, rowIndex);
		}
	}

	private void drawPoint(Graphics2D g, int rowIndex, Color c)
	{
		// Determine painting location
		int x = this.determineXCoord(rowIndex);
		int y = this.determineYCoord(rowIndex);
		int splitClass = this.getSplitClass(rowIndex);

		if (x < 0 || y < 0 || splitClass < 0)
		{
			return;
		}
		else
		{
			if (this.model.columnSplitIndexValid())
			{
				y += (splitClass) * (this.scrollPane.getHeight() - 5);
			}
			c = getColor(splitClass);
			drawPointAtLocation(g, x, y, rowIndex, c);
		}
	}

	/**
	 * Draw a single point at a given location
	 * 
	 * @param g
	 *            the graphics in which the point will be painted
	 * @param x
	 *            the x-coordinate of the painting location
	 * @param y
	 *            the y-coordinate of the painting location
	 * @param rowIndex
	 *            the index of the object that will be painted
	 */
	private void drawPointAtLocation(Graphics2D g, int x, int y, int rowIndex)
	{
//		System.out.println("DotplotView.drawPointAtLocation(g, " + x + ", " + y + ", " + rowIndex + ")");
		
		// highlight point if the object is selected
		g.setColor(Color.BLACK);
		if (this.model.getTableModel().isRowSelected(rowIndex))
		{
			g.fillOval(x - this.dotSize - 2, y - this.dotSize - 2,
				2 * this.dotSize + 4, 2 * this.dotSize + 4); // because of transparency the highlighted dot will tone darker
			g.drawOval(x - this.dotSize - 2, y - this.dotSize - 2,
				2 * this.dotSize + 4, 2 * this.dotSize + 4);
		}

		// paint the point
		Color c = this.determineColor(rowIndex);
		g.setColor(c);
		// set transparent
		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 150));
		g.fillOval(x - this.dotSize, y - this.dotSize, 2 * this.dotSize,
			2 * this.dotSize);

		this.objectLocations.set(rowIndex, new Point(x, y));
	}

	private void drawPointAtLocation(Graphics2D g, int x, int y, int rowIndex,
		Color c)
	{
//		System.out.println("DotplotView.drawPointAtLocation(g, " + x + ", " + y + ", " + rowIndex + c.toString() + ")");
		
		// highlight point if the object is selected
		g.setColor(Color.BLACK);
		if (this.model.getTableModel().isRowSelected(rowIndex))
		{
			g.fillOval(x - this.dotSize - 2, y - this.dotSize - 2,
				2 * this.dotSize + 4, 2 * this.dotSize + 4);
			g.drawOval(x - this.dotSize - 2, y - this.dotSize - 2,
				2 * this.dotSize + 4, 2 * this.dotSize + 4);
		}

		// paint the point
		g.setColor(c);
		// set transparent
		g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 150));
		g.fillOval(x - this.dotSize, y - this.dotSize, 2 * this.dotSize,
			2 * this.dotSize);

		this.objectLocations.set(rowIndex, new Point(x, y));
	}

	/**
	 * Paint the axis text labels.
	 * 
	 * @param g
	 * @param yOffset
	 * @param splitClass
	 */
	private void paintAxisLabels(Graphics g, int yOffset, int splitClass)
	{
		Font font = super.getFont().deriveFont(super.getFont().getStyle());
		FontMetrics fm = super.getFontMetrics(font);
		AffineTransform at = new AffineTransform();
		at.rotate(Math.PI * 1.5);
		Font rotateFont = font.deriveFont(at);
		
		String s1 = "";
		String s2 = "";
		if (this.model.columnYIndexValid())
			s1 = this.model.getTableModel().getColumnName(
				this.model.getColumnYIndex());
		if (this.model.columnXIndexValid())
			s2 = this.model.getTableModel().getColumnName(
				this.model.getColumnXIndex());

		g.setColor(Color.BLACK);
		g.setFont(rotateFont);
		
		// draw label y-axis
		g.drawString(s1, fm.getHeight() - 2, this.scrollPane.getHeight() / 2
			+ fm.stringWidth(s1) / 2 + yOffset);
		g.setFont(font);

		// draw label x-axis
		g.drawString(s2,
			(this.dotAreaWidth() - this.yAxisOffset - fm.stringWidth(s2)) / 2
				+ this.yAxisOffset, this.scrollPane.getHeight()
				+ this.X_AS_OFFSET - 37 + yOffset);

		// draw split variable class (e.g., "geslacht: m")
		if (this.model.getTableModel().splitVarClasses(
			this.model.getSplitOptions()) > 1
			&& !this.model.splitInSingleView())
		{
			String name = this.model.getTableModel().getColumnName(
				this.model.getSplitOptions().getColumnSplitIndex());
			String splitClassLabel = this.model.getSplitOptions().getSplitClassLabel(splitClass,
				this.model.getTableModel());
			String s = name
				+ ": " + splitClassLabel;
			g.drawString(s, 10, this.scrollPane.getHeight() + this.X_AS_OFFSET
				- 37 + yOffset);
		}

		g.fillRect(0, this.scrollPane.getHeight() + this.X_AS_OFFSET - 28
			+ yOffset, super.getWidth(), 1);
	}

	/**
	 * Paint the x-axis
	 * 
	 * @param g
	 *            The graphics in which the x-axis will be painted
	 */
	private void paintXAxis(Graphics g, int heightOffset)
	{
		g.setColor(Color.BLACK);
		Font font = super.getFont().deriveFont(super.getFont().getStyle());
		FontMetrics fm = super.getFontMetrics(font);

		AffineTransform at = new AffineTransform();
		double theta = Math.PI * 1.95;
		at.rotate(theta); 
		Font rotateFont = super.getFont().deriveFont(at);

		ColumnType columnType = this.model.getTableModel().getColumnTypes()
			.get(this.model.getColumnXIndex());

		int y = this.model.getTableModel().isViewsEditable() ? this.getHeight()
			- DotplotView.KEUZEBALK_HOOGTE - DotplotView.X_AS_OFFSET : 
//				this.getHeight() - DotplotView.X_AS_OFFSET;
				this.getHeight() - 50;
		
		// draw x-axis
		if (this.model.columnYIndexValid())
		{
			g.drawLine(this.yAxisOffset, y + heightOffset,
				this.getWidth(), y + heightOffset);
		}
		else
		{
			g.drawLine(0, y + heightOffset, this.getWidth(), y + heightOffset);
		}

		if (this.xType.equals(AllowedTypes.ENUM))
		{
			boolean normalFit = this.determineNormalFitForEnum();
			// draw ticks and help lines, and labels
			for (int i = 0; i < columnType.getEnumOptions().length; i++)
			{
				String option = columnType.getEnumOptions()[i];
				if (option.equals(ColumnType.WILDCARD))
				{
					continue;
				}
				int x = this.determineXCoordEnumClass(option);
				
				g.drawLine(x, y + heightOffset, x, y + heightOffset + 5);
				// draw help line
				drawHelpLine(g, x, heightOffset + 5, y - 5, 0);
			    
				if (!normalFit)
					g.setFont(rotateFont);

				g.drawString(option, x - (int) (0.5 * fm.stringWidth(option)),
					y + 5 + fm.getHeight() + heightOffset);
				g.setFont(font);
			}

		} // enum
		else if (this.xType.equals(AllowedTypes.STRING))
		{
			boolean normalFit = this.determineNormalFitForString();

			int amountOfOptions = this.getXStringOptions().size();

			// draw ticks and help lines
			for (int i = 0; i < amountOfOptions; i++)
			{
				String option = this.getXStringOptions().get(i);
				int x = this.determineXCoordStringClass(option);

				g.drawLine(x, y + heightOffset, x, y + heightOffset + 5);
				// draw help line
				drawHelpLine(g, x, heightOffset + 5, y - 5, 0);
			    
				if (!normalFit)
					g.setFont(rotateFont);

				g.drawString(option, x - (int) (0.5 * fm.stringWidth(option)),
					y + 5 + fm.getHeight() + heightOffset);
				g.setFont(font);
			}
		} // string
		else
		{
			// xType is int or double

			int base = 1;
			int exp = (int) Math.log10(this.xMax - this.xMin) - 1;
			int step = (int) (base * Math.pow(10, exp));
			while ((this.xMax - this.xMin) / step > 8)
			{
				switch (base)
				{
					case 1:
						base = 2;
						break;
					case 2:
						base = 5;
						break;
					case 5:
						base = 1;
						exp++;
						break;
				}
				step = (int) (base * Math.pow(10, exp));
			}

			double minorStep;
			int minorStepsPerMajorStep;
			switch (base)
			{
				case 5:
					minorStep = (int) Math.pow(10, exp);
					minorStepsPerMajorStep = 5;
					break;
				case 2:
					minorStep = (int) (5 * Math.pow(10, exp - 1));
					minorStepsPerMajorStep = 4;
					break;
				case 1:
//					minorStep = (int) (2 * Math.pow(10, exp - 1));
					minorStep = 2 * Math.pow(10, exp - 1);
					minorStepsPerMajorStep = 5;
					break;
				default:
					minorStep = 1;
					minorStepsPerMajorStep = step;
			}
			
			if (minorStep == 0)
			{
				System.out.println("DotplotView.paintXAxis(): minorStep == 0!");
			}
			
			double min = this.xMin - DotplotView.KEEP_CLEAR_PART
				* (this.xMax - this.xMin);
			double max = this.xMax + DotplotView.KEEP_CLEAR_PART
				* (this.xMax - this.xMin);

//			double p = Math.ceil(min / step) * step;
			double p = determineFirstMinorStep(min, minorStep);
			
			// field to be used in determineXCoordNumClass()
			this.xFirstMinorStep = p;

			// Math.ceil can give -0.0, this step turns that into 0.0
			if (p == 0)
			{
				p = 0.0;
			}

//			System.out.println("DotplotView.paintXAxis(): base = "
//				+ base + ", step = " + step + ", minorStep = "
//				+ minorStep + ", minorStepsPerMajorStep = " 
//				+ minorStepsPerMajorStep + ", min = " + min
//				+ ", max = " + max + ", p = " + p);
			
			// draw minor ticks
			while (p < max)
			{
				int x = this.determineXCoordNumClass(p);
				g.drawLine(x, y + heightOffset, x, y + 2 + heightOffset);

				p += minorStep;
				
				// check for invalid value of minorStep
				if (minorStep == 0)
					break;
			}

			p = Math.ceil(min / step) * step;

			// Math.ceil can give -0.0, this step turns that into 0.0
			if (p == 0)
			{
				p = 0.0;
			}

			// draw major ticks and help lines
			while (p < max)
			{
				int x = this.determineXCoordNumClass(p);
				g.drawLine(x, y + heightOffset, x, y + heightOffset + 5);
				
				// draw help line
				drawHelpLine(g, x, heightOffset + 5, y - 5, 0);
			    
				// get the right string value for integer or double
				String pString = Statistiek.getStringValue(p);//getStringValueForXVar(p);
				g.drawString(pString,
					x - (int) (0.5 * fm.stringWidth(pString)), y + 5
						+ fm.getHeight() + heightOffset);

				p += step;
			}
		} // numerical xType
	}

	private boolean determineNormalFitForString()
	{
		boolean normalFit = true;
		
		int amountOfOptions = this.getXStringOptions().size();

		Font font = super.getFont().deriveFont(super.getFont().getStyle());
		FontMetrics fm = super.getFontMetrics(font);
		
		int tickWidth = 0;
		// determine tick width and normal fit
		for (int i = 0; i < amountOfOptions; i++)
		{
			String option = this.getXStringOptions().get(i);
			if (option.equals(ColumnType.WILDCARD))
			{
				continue;
			}

			int x = this.determineXCoordStringClass(option);

			if (i == 0)
				tickWidth = x;
			if (i == 1)
				tickWidth = x - tickWidth; // tickWidth = x1 - x0
			
			int width = fm.stringWidth(option);
			if ((i > 0) && (width > tickWidth))
			{
				normalFit = false;
				break;
			}
		}
		
		return normalFit;
	}

	private boolean determineNormalFitForEnum()
	{
		boolean normalFit = true;
		
		ColumnType columnType = this.model.getTableModel().getColumnTypes()
			.get(this.model.getColumnXIndex());
		Font font = super.getFont().deriveFont(super.getFont().getStyle());
		FontMetrics fm = super.getFontMetrics(font);
		
		int tickWidth = 0;
		// determine tick width and normal fit
		for (int i = 0; i < columnType.getEnumOptions().length; i++)
		{
			String option = columnType.getEnumOptions()[i];
			if (option.equals(ColumnType.WILDCARD))
			{
				continue;
			}
			int x = this.determineXCoordEnumClass(option);
			
			if (i == 0)
				tickWidth = x;
			if (i == 1)
				tickWidth = x - tickWidth; // tickWidth = x1 - x0
			
			int width = fm.stringWidth(option);
			if ((i > 0) && (width > tickWidth))
			{
				normalFit = false;
				break;
			}
		}
		
		return normalFit;
	}

	/**
	 * Draws a help help line in grey, starting from (x,y) with given length.
	 * @param g
	 * @param x
	 * @param y
	 * @param length
	 * @param orientation
	 * 	0 is vertical, 1 is horizontal
	 */
	private void drawHelpLine(Graphics g, int x, int y, int length, int orientation)
	{
		g.setColor(ColorGenerator.getGreyLineColor());

		// draw the line
		if (orientation == 0)
			g.drawLine(x, y, x, y + length);
		else if (orientation == 1)
			g.drawLine(x, y, x + length, y);
		else
			System.out.println("DotplotView.drawHelpLine(): wrong parameter! orientation = " + orientation);
		
		// reset the graphics
		g.setColor(Color.BLACK);
	}

	private double determineFirstMinorStep(double min, double minorStep)
	{
		double first = 0;
		
		first = min - (min % minorStep);
		
		return first;
	}

	/*
	 * Get the string value of p according to the type of the x variable (Integer or Double).
	 */
	private String getStringValueForXVar(double p)
	{
		String s;
		
		if (this.xType.equals(AllowedTypes.INTEGER))
		{
			s = String.valueOf((int) p);
		}
		else if (this.xType.equals(AllowedTypes.DOUBLE))
		{
			s = Double.toString(p);
		}
		else
			s = "";
		
		return s;
	}

	/*
	 * Get the string value of p according to the type of the y variable (Integer or Double).
	 */
	private String getStringValueForYVar(double p)
	{
		String s;
		
		if (this.yType.equals(AllowedTypes.INTEGER))
		{
			s = String.valueOf((int) p);
		}
		else if (this.yType.equals(AllowedTypes.DOUBLE))
		{
			s = Double.toString(p);
		}
		else
			s = "";
		
		return s;
	}

	/**
	 * Paint the y-axis and its value labels.
	 * 
	 * @param g
	 *            The graphics in which the x-axis will be painted
	 */
	private void paintYAxis(Graphics g, int heightOffset)
	{
		g.setColor(Color.BLACK);
		Font font = super.getFont().deriveFont(super.getFont().getStyle());
		FontMetrics fm = super.getFontMetrics(font);

		ColumnType columnType = this.model.getTableModel().getColumnTypes()
			.get(this.model.getColumnYIndex());

		int x = this.yAxisOffset;
//		System.out.println("DotplotView.paintYAxis(): yAxisOffset = " + yAxisOffset);

		int drawHeight = this.dotAreaHeight();
		
		// draw y-axis
		g.drawLine(x, 5 + heightOffset, x, drawHeight + heightOffset);

		if (yType.equals(AllowedTypes.ENUM))
		{
			// draw ticks and help lines
			for (int i = 0; i < columnType.getEnumOptions().length; i++)
			{
				String option = columnType.getEnumOptions()[i];
				if (option.equals(ColumnType.WILDCARD))
				{
					continue;
				}

				int y = this.determineYCoordEnumClass(option);
				g.drawLine(x - 5, y + heightOffset, x, y + heightOffset);
				// draw help line
				drawHelpLine(g, x, y + heightOffset, this.getWidth() - this.yAxisOffset, 1);

				// draw enum option label
				g.drawString(option, x - 7 - fm.stringWidth(option), (int) (y
					+ 0.5 * fm.getHeight() - 3)
					+ heightOffset);
			}

		}
		else if (this.yType.equals(AllowedTypes.STRING))
		{
			int amountOfOptions = this.getYStringOptions().size();

			// draw ticks and help line
			for (int i = 0; i < amountOfOptions; i++)
			{
				String option = this.getYStringOptions().get(i);
				int y = this.determineYCoordStringClass(option);
				g.drawLine(x - 5, y + heightOffset, x, y + heightOffset);
				// draw help line
				drawHelpLine(g, x, y + heightOffset, this.getWidth() - this.yAxisOffset, 1);

				g.drawString(option, x - 7 - fm.stringWidth(option), (int) (y
					+ 0.5 * fm.getHeight() - 3)
					+ heightOffset);
			}

		}
		else
		{
			// yType is int or double

			int base = 1;
			int exp = (int) Math.log10(this.yMax - this.yMin) - 1;
			int step = (int) (base * Math.pow(10, exp));
			while ((this.yMax - this.yMin) / step > 8)
			{
				switch (base)
				{
					case 1:
						base = 2;
						break;
					case 2:
						base = 5;
						break;
					case 5:
						base = 1;
						exp++;
						break;
				}
				step = (int) (base * Math.pow(10, exp));
			}

			double min = this.yMin - DotplotView.KEEP_CLEAR_PART
				* (this.yMax - this.yMin);
			double max = this.yMax + DotplotView.KEEP_CLEAR_PART
				* (this.yMax - this.yMin);
			double p = Math.ceil(min / step) * step;

			// test syl: TODO draw minor ticks
			
			// Math.ceil can give -0.0, this step turns that into 0.0
			if (p == 0)
			{
				p = 0.0;
			}

			// draw major ticks and help lines
			while (p < max)
			{
				int y = this.determineYCoordNumClass(p);
				if ((y + (int) (0.5 * fm.getHeight() - 3)) < 0)
				{
					continue;
				}

				g.drawLine(x - 5, y + heightOffset, x, y + heightOffset);
				// draw help line
				drawHelpLine(g, x, y + heightOffset, this.getWidth() - this.yAxisOffset, 1);
				
				// get the right string value for integer or double
				String pString = Statistiek.getStringValue(p);//getStringValueForYVar(p);
				g.drawString(pString,
					x - 7 - fm.stringWidth(pString), y
						+ (int) (0.5 * fm.getHeight() - 3) + heightOffset);

				p += step;
			}
		}
	}

	/**
	 * Get the mean value of a column. In case of an Enum type column, it find
	 * the average index in the enum options In case of a String type column, it
	 * finds the average index in the options list
	 * 
	 * @param column
	 *            The index of the column
	 * @param options
	 *            All possible unique elements of this column, only necessary
	 *            for string columns
	 * @return The mean value of the column
	 */
	private double getColumnMean(int column, ArrayList<String> options)
	{
		ColumnType columnType = this.model.getTableModel().getColumnTypes()
			.get(column);
		AllowedTypes type = columnType.getType();
		int count = 0;
		double sum = 0;
		for (int i = 0; i < this.model.getTableModel().getRowCount(); i++)
		{
			String valueString = (String) this.model.getTableModel()
				.getValueAt(i, column);
			if (valueString.equals(ColumnType.WILDCARD))
			{
				continue;
			}
			else
			{
				count++;

				if (type.equals(AllowedTypes.DOUBLE)
					|| type.equals(AllowedTypes.INTEGER))
				{
					sum += Double.parseDouble(valueString);
				}
				else if (type.equals(AllowedTypes.ENUM))
				{
					sum += columnType.indexOfStringInEnum(valueString);
				}
				else
				{
					sum += options.indexOf(valueString);
				}
			}
		}

		return sum / (double) count;
	}

	/**
	 * Get the sum value of a column. In case of an Enum type column, it returns
	 * the average index in the enum options. In case of a String type column, it
	 * returns the average index in the options list.
	 * 
	 * @param column
	 *            The index of the column
	 * @param options
	 *            All possible unique elements of this column, only necessary
	 *            for string columns
	 * @return The mean value of the column
	 */
	private double getColumnSum(int column, ArrayList<String> options)
	{
		ColumnType columnType = this.model.getTableModel().getColumnTypes()
			.get(column);
		AllowedTypes type = columnType.getType();
		int count = 0;
		double sum = 0;
		for (int i = 0; i < this.model.getTableModel().getRowCount(); i++)
		{
			String valueString = (String) this.model.getTableModel()
				.getValueAt(i, column);
			if (valueString.equals(ColumnType.WILDCARD))
			{
				continue;
			}
			else
			{
				count++;

				if (type.equals(AllowedTypes.DOUBLE)
					|| type.equals(AllowedTypes.INTEGER))
				{
					sum += Double.parseDouble(valueString);
				}
				else if (type.equals(AllowedTypes.ENUM))
				{
					sum += columnType.indexOfStringInEnum(valueString);
				}
				else
				{
					sum += options.indexOf(valueString);
				}
			}
		}

		return sum;
	}

	/**
	 * Finds a column's standard deviation. In case of an Enum type column, this
	 * uses the index in the enumoptions instead of the enum value. In case of a
	 * String type column, this uses the index in the options arraylist instead
	 * of the string value.
	 * 
	 * @param column
	 *            The index of the column
	 * @param mean
	 *            The mean value of this column
	 * @param options
	 *            All possible unique elements of this column, only necessary
	 *            for string columns
	 * @return
	 */
	private double getStdDev(int column, double mean, ArrayList<String> options)
	{
		ColumnType columnType = this.model.getTableModel().getColumnTypes()
			.get(column);
		AllowedTypes type = columnType.getType();
		int count = 0;
		double sum = 0;
		for (int i = 0; i < this.model.getTableModel().getRowCount(); i++)
		{
			String valueString = (String) this.model.getTableModel()
				.getValueAt(i, column);
			if (valueString.equals(ColumnType.WILDCARD))
			{
				continue;
			}
			else
			{
				count++;

				if (type.equals(AllowedTypes.DOUBLE)
					|| type.equals(AllowedTypes.INTEGER))
				{
					sum += Math.pow(Double.parseDouble(valueString) - mean, 2);
				}
				else if (type.equals(AllowedTypes.ENUM))
				{
					sum += Math.pow(columnType.indexOfStringInEnum(valueString)
						- mean, 2);
				}
				else
				{
					sum += Math.pow(options.indexOf(valueString) - mean, 2);
				}
			}
		}
		
		// test syl: algorithm (John Cook, see http://www.johndcook.com/standard_deviation.html)
		// does not seem to be correct now...
//		double oldMean = 0;
//		double newMean = 0;
//		double oldS = 0;
//		double newS = 0;
//		double value;
//		for (int i = 0; i < this.model.getTableModel().getRowCount(); i++)
//		{
//			String valueString = (String) this.model.getTableModel()
//				.getValueAt(i, column);
//			if (valueString.equals(ColumnType.WILDCARD))
//			{
//				continue;
//			}
//			else
//			{
//				count++;
//
//				if (type.equals(AllowedTypes.DOUBLE)
//					|| type.equals(AllowedTypes.INTEGER))
//				{
//					value = Double.parseDouble(valueString);
//				}
//				else if (type.equals(AllowedTypes.ENUM))
//				{
//					value = columnType.indexOfStringInEnum(valueString);
//				}
//				else
//				{
//					value = options.indexOf(valueString);
//				}
//			}
//			
//			if (count ==1)
//			{
//				oldMean = value;
//				newMean = value;
//				oldS = 0.0;
//			}
//			else
//			{
//				newMean = oldMean + (value - oldMean)/count;
//				newS = oldS + (value - oldMean) * (value - newMean);
//			}
//		} // new algorith
//		
//		double variance;
//		if (count > 1)
//			variance = newS/(count - 1);
//		else
//			variance = 0;
//		double stdDev = Math.sqrt(variance);
//		return stdDev; // new algorithm's return value

		return Math.sqrt(sum / (double) count);
	}

	/**
	 * Get the covariance between two columns.
	 * 
	 * @param indexColumnA
	 *            Index of the first column
	 * @param meanA
	 *            mean value of the first column
	 * @param optionsA
	 *            All possible unique elements of the first column, only
	 *            necessary for string columns
	 * @param indexColumnB
	 *            Index of the first column
	 * @param meanB
	 *            mean value of the first column
	 * @param optionsB
	 *            All possible unique elements of the first column, only
	 *            necessary for string columns
	 * @return The covariance between columnA and columnB
	 */
	private double getCovariance(int indexColumnA, double meanA,
		ArrayList<String> optionsA, int indexColumnB, double meanB,
		ArrayList<String> optionsB)
	{
		ColumnType columnTypeA = this.model.getTableModel().getColumnTypes()
			.get(indexColumnA);
		AllowedTypes typeA = columnTypeA.getType();
		ColumnType columnTypeB = this.model.getTableModel().getColumnTypes()
			.get(indexColumnB);
		AllowedTypes typeB = columnTypeB.getType();

		int count = 0;
		
//		 old algorithm is extremely slow for large datasets...
//		double sum = 0;
//		for (int i = 0; i < this.model.getTableModel().getRowCount(); i++)
//		{
//			System.out.println("DotplotView.getCovariance(): i = " + i);
//			
//			String valueStringA = (String) this.model.getTableModel()
//				.getValueAt(i, indexColumnA);
//			if (valueStringA.equals(ColumnType.WILDCARD))
//			{
//				continue;
//			}
//			double valueA;
//			if (typeA.equals(AllowedTypes.DOUBLE)
//				|| typeA.equals(AllowedTypes.INTEGER))
//			{
//				valueA = Double.parseDouble(valueStringA);
//			}
//			else if (typeA.equals(AllowedTypes.ENUM))
//			{
//				valueA = columnTypeA.indexOfStringInEnum(valueStringA);
//			}
//			else
//			{
//				valueA = optionsA.indexOf(valueStringA);
//			}
//			for (int j = 0; j < this.model.getTableModel().getRowCount(); j++)
//			{
//				String valueStringB = (String) this.model.getTableModel()
//					.getValueAt(i, indexColumnB);
//				if (valueStringB.equals(ColumnType.WILDCARD))
//				{
//					continue;
//				}
//
//				double valueB;
//				if (typeB.equals(AllowedTypes.DOUBLE)
//					|| typeB.equals(AllowedTypes.INTEGER))
//				{
//					valueB = Double.parseDouble(valueStringB);
//				}
//				else if (typeB.equals(AllowedTypes.ENUM))
//				{
//					valueB = columnTypeB.indexOfStringInEnum(valueStringB);
//				}
//				else
//				{
//					valueB = optionsB.indexOf(valueStringB);
//				}
//
//				count++;
//				sum += (valueA - meanA) * (valueB - meanB);
//			} // for j-loop
//		} // for i-loop
		
		// new algorithm (see http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance)
		int sumAB = 0;
		double sumA = this.getColumnSum(indexColumnA, optionsA);
		double sumB = this.getColumnSum(indexColumnB, optionsB);
		
		for (int i = 0; i < this.model.getTableModel().getRowCount(); i++)
		{
			String valueStringA = (String) this.model.getTableModel()
				.getValueAt(i, indexColumnA);
			String valueStringB = (String) this.model.getTableModel()
				.getValueAt(i, indexColumnB);
			
			if (valueStringA.equals(ColumnType.WILDCARD) || valueStringB.equals(ColumnType.WILDCARD))
			{
				continue;
			}
			
			count++;
			double valueA;
			double valueB;
			
			if (typeA.equals(AllowedTypes.DOUBLE)
				|| typeA.equals(AllowedTypes.INTEGER))
			{
				valueA = Double.parseDouble(valueStringA);
			}
			else if (typeA.equals(AllowedTypes.ENUM))
			{
				valueA = columnTypeA.indexOfStringInEnum(valueStringA);
			}
			else
			{
				valueA = optionsA.indexOf(valueStringA);
			}
			
			if (typeB.equals(AllowedTypes.DOUBLE)
				|| typeB.equals(AllowedTypes.INTEGER))
			{
				valueB = Double.parseDouble(valueStringB);
			}
			else if (typeB.equals(AllowedTypes.ENUM))
			{
				valueB = columnTypeB.indexOfStringInEnum(valueStringB);
			}
			else
			{
				valueB = optionsB.indexOf(valueStringB);
			}
			
			sumAB += valueA * valueB;
		}
		
		double covariance = (sumAB - sumA * sumB / count) / count;
		return covariance; // new algorithm's return value

		//return sum / (double) count;
	}

	/**
	 * Draw the correlation. Pearson's product-moment correlation coefficient
	 * is calculated, or Pearson's r.
	 * 
	 * @param g
	 *            The graphics in which the correlation will be painted
	 */
	private void drawCorrelation(Graphics g)
	{
		if (this.model.isShowCorrelation()
			&& !this.model.columnSplitIndexValid())
		{
			double correlation;
			double r;
			
			g.setColor(Color.BLACK);

			int columnAIndex = this.model.getColumnXIndex();
			int columnBIndex = this.model.getColumnYIndex();
//			double meanA = this.getColumnMean(columnAIndex, this.getXStringOptions());
//			double meanB = this.getColumnMean(columnBIndex, this.getYStringOptions());

//			double covar = this.getCovariance(columnAIndex, meanA,
//				this.getXStringOptions(), columnBIndex, meanB,
//				this.getYStringOptions());
//			double sdA = this.getStdDev(columnAIndex, meanA, this.getXStringOptions());
//			double sdB = this.getStdDev(columnBIndex, meanB, this.getYStringOptions());
//			correlation = covar / (sdA * sdB);
			
//			System.out.println("DotplotView.drawCorrelation(): covar = " + covar
//				+ ", sdA = " + sdA
//				+ ", sdB = " + sdB
//				+ ", corr = " + correlation);

//			int y = (this.model.getTableModel().isViewsEditable() ? this
//				.getHeight() - 10 - DotplotView.KEUZEBALK_HOOGTE : this
//				.getHeight() - 3);
			
			//r = Math.round(correlation * 100) / 100.0;
			
			String correlationInfoString;
			
			double[][] data = this.model.getTableModel().getDataColumnsForCorrelation(columnAIndex, columnBIndex);
			String pString;
			// calculate significance met Common math package
//			PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
			
			// voor algoritme zie http://onlinestatbook.com/2/describing_bivariate_data/calculation.html
			correlation = this.getCorrelation(data[0], data[1]);
			r = Math.round(correlation * 100) / 100.0;
			int n = data[0].length;
//			double t = correlation * Math.sqrt(n - 2) / Math.sqrt(1 - Math.pow(correlation, 2)); 
			// getting p value from t distribution without using apache.commons is not trivial..., so use significance table
			
			try
			{
//				RealMatrix pValues = pearsonCorrelation.getCorrelationPValues();
//				double p1 = Statistiek.round((double) pValues.getEntry(0, 1), 3);

				// determine p value with significance table
				double p = getLevelOfSignificance(correlation, n);
				
				if (p == 0)
				{
					// for the significance value explicit precision is shown (in case of precise calculation with Commons Math)
					pString = "0.000";
				}
				else
				{
					pString = String.valueOf(p);
				}
//				double r2 = pearsonCorrelation.getCorrelationMatrix().getEntry(0, 1);
				
				if (p == 1)
					correlationInfoString = "r=" + Double.toString(r) 
//						+ ", p_common=" + p1
						+ ", p>0.1";
				else if (p == -1)
					correlationInfoString = "r=" + Double.toString(r) 
//						+ ", p_common=" + p1
						+ ", " + Statistiek.rb.getString("significanceNoShow");
				else
					correlationInfoString = 
						"r=" + Double.toString(r) 
//						+ ", p_common=" + p1
						+ ", p<" + pString;
				
			}
			catch (Exception e)
			{
				System.out.println("degrees of freedom is 0; er is te weinig data om significantie te berekenen");
				correlationInfoString = Statistiek.rb.getString("correlationNoShow");
			}
			
			g.drawString(correlationInfoString,
				3, this.scrollPane.getHeight()
				+ this.X_AS_OFFSET - 37);
		}
	}

	/**
	 * Get Pearson's correlation coefficient r for column X and Y. 
	 * For algorithm, see: http://onlinestatbook.com/2/describing_bivariate_data/calculation.html
	 * 
	 * @param columnX An array with valid values of column X.
	 * @param columnY An array with valid values of column Y.
	 */
	private double getCorrelation(double[] columnX, double[] columnY)
	{
		// column A and B should be of the same length, providing valid value pairs
		int n = Math.min(columnX.length, columnY.length);
		
		double[] xy = new double[n];
		double[] xSquared = new double[n];
		double[] ySquared = new double[n];
		for (int i = 0; i < n; i++)
		{
		    xy[i] = columnX[i] * columnY[i];
		    xSquared[i] = Math.pow(columnX[i], 2);
		    ySquared[i] = Math.pow(columnY[i], 2);
		}
			    
		double sumX = this.sum(columnX);
		double sumY = this.sum(columnY);
		double sumXY = this.sum(xy);
		double sumXSquared = this.sum(xSquared);
		double sumYSquared = this.sum(ySquared);

		return (sumXY - ((sumX * sumY)/n)) / 
			(Math.sqrt(sumXSquared - (Math.pow(sumX, 2)/n)) 
				* Math.sqrt(sumYSquared - (Math.pow(sumY, 2)/n)));
	}

	/**
	 * Sum the elements in the doubles array.
	 * @param doubles
	 * @return
	 */
	private double sum(double[] doubles)
	{
		double sum = 0;
		
		for (double value:doubles)
		     sum += value;
		
		return sum;
	}

	/**
	 * Get the level of significance of Pearson's product-moment correlation 
	 * coefficient r and n cases. This means that correlation r has
	 * significance p < level of significance.
	 * 
	 * @param r Pearson's product-moment correlation coefficient
	 * @param n The number of cases
	 * 
	 * @return The level of significance, i.e., correlation r has significance 
	 * p < level of significance. If the level cannot be determined, -1 is returned.
	 */
	private double getLevelOfSignificance(double r, int n)
	{
		double level;
		// the index for reading SIGNIFICANCE_TABLE
		int i = -1; 
		// help variable for checking valid values of n
		double[] values;
		
		// determine the index for reading SIGNIFICANCE_TABLE
		if (n < 3)
			i = -1; // not valid
		else if (n <= 30)
			i = n - 3;
		else if ((n > 30) && (n <= 35))
			i = 28;
		else if ((n > 35) && (n <= 40))
			i = 29;
		else if ((n > 40) && (n <= 45))
			i = 30;
		else if ((n > 45) && (n <= 50))
			i = 31;
		else if ((n > 50) && (n <= 60))
			i = 32;
		else if ((n > 60) && (n <= 70))
			i = 33;
		else if ((n > 70) && (n <= 80))
			i = 34;
		else if ((n > 80) && (n <= 90))
			i = 35;
		else if ((n > 90) && (n <= 100))
			i = 36;
		else if ((n > 100) && (n <= 200))
			i = 37;
		else if ((n > 200) && (n <= 300))
			i = 38;
		else if ((n > 300) && (n <= 400))
			i = 39;
		else if ((n > 400) && (n <= 500))
			i = 40;
		else if ((n > 500))//test syl && (n <= 1000))
			i = 41;
			
		// read the table for row i
		if (i > -1)
		{
			r = Math.abs(r);
			if (r > SIGNIFICANCE_TABLE[i][3])
				level = 0.001;
			else if (r > SIGNIFICANCE_TABLE[i][2])
				level = 0.01;
			else if (r > SIGNIFICANCE_TABLE[i][1])
				level = 0.05;
			else if (r > SIGNIFICANCE_TABLE[i][0])
				level = 0.1;
			else if (r < SIGNIFICANCE_TABLE[i][0])
				level = 1; // p > 0.1
			else
				level = -1; // not valid
		}
		else
			level = -1;
		
		return level;
	}

	/**
	 * Determine the coordinates for all objects Used only in single variable
	 * cases, with that variable on the x-axis
	 * 
	 * @return a matrix containing the x and y coordinate for all objects
	 */
	private int[][] determineCoordsXSingleVar()
	{
		int[][] coords = new int[this.model.getTableModel().getRowCount()][2];
		int[] splitClasses = new int[this.model.getTableModel().getRowCount()];
		int[][] sortedData = null;

		/*
		 * int drawHeight = this.getHeight() - DotplotView.X_AS_OFFSET;
		 * if(this.model.isShowUserOptions()) { drawHeight -=
		 * DotplotView.KEUZEBALK_HOOGTE; }
		 */
		int drawHeight = this.dotAreaHeight();
		int dotSizeSquared = (int) (Math.pow(2 * this.dotSize + 1, 2));
		for (int i = 0; i < this.model.getTableModel().getRowCount(); i++)
		{
			if (this.model.getTableModel()
				.getValueAt(i, this.model.getColumnXIndex())
				.equals(ColumnType.WILDCARD))
			{
				// skip wildcard objects
				splitClasses[i] = -1;
				continue;
			}
			splitClasses[i] = this.getSplitClass(i);

			// voor verdeling binnen 1 veld:
			if (DotplotView.this.model.splitInSingleView())
				splitClasses[i] = 0;

			coords[i][0] = this.determineXCoord(i);
		}
		
		// sortedData = [x, y, split, original index]
		sortedData = new int[this.model.getTableModel().getRowCount()][4];
		for (int i = 0; i < this.model.getTableModel().getRowCount(); i++)
		{
			sortedData[i][0] = coords[i][0];
			sortedData[i][2] = splitClasses[i];
			sortedData[i][3] = i;
		}
		
		Arrays.sort(sortedData, new Comparator<int[]>() {
            @Override
            /**
             * Compare [x1, y1, split1] to [x2, y2, split2] on x-coordinate
             * and split. 
             * @param o1
             * @param o2
             * @return
             */
            public int compare(int[] o1, int[] o2) 
            {
            	// compare the x coordinates
            	if (o1[0] < o2[0])
            		return -1;
            	else if (o1[0] > o2[0])
            		return 1;
            	else // same x coordinates
            	{
            		if (o1[2] < o2[2])
            			return -1;
            		else if (o1[2] > o2[2])
            			return 1;
            		else // same split
            			return 0;
            	}
            }
        });
		
		// bepaal de y-correctie die zo nodig gedaan wordt per split: max y / max aantal dots per x
		int[] maxFrequencyXPerSplit = this.getMaxFrequencyXPerSplit(sortedData);
		// correction in double values to avoid rounding errors
		double[] correctionYPerSplit = new double[this.splitClasses]; 
			
		for (int split = 0; split < this.splitClasses; split++)
		{
			correctionYPerSplit[split] = Math.min(
				this.dotSize * 2, 
				((1 - DotplotView.KEEP_CLEAR_PART) * drawHeight - 2 * this.dotSize) / maxFrequencyXPerSplit[split]);
		}

		// set initial y
		double y_initial = (1 - DotplotView.KEEP_CLEAR_PART) * drawHeight;
		double y = y_initial;
		// use double values to get a precise calculation for large data sets
		double[] y_doubles = new double[this.model.getTableModel().getRowCount()];
		
		for (int i = 0; i < this.model.getTableModel().getRowCount(); i++)
		{
			if (i > 0)
			{
				// look at the previous x-coordinate...
				// if x-coord and split the same, then adjust y-coord
				if (sortedData[i][0] == sortedData[i-1][0]) // same x coordinate
				{
					if ((sortedData[i][2] > -1)//!= -1) // skip wildcards; sortedData[i][2] == -2 if row i contains a wildcard
						&& (sortedData[i][2] == sortedData[i-1][2])) // same split
					{
						// dot with the same x-coordinate in the same split, so calculate y based on 
						// the previous y
						y = y_doubles[i-1] - correctionYPerSplit[sortedData[i][2]];
					}
					else // same x coordinate, next split 
					{
						// reset y to initial value
						y = y_initial;
					}
				}
				else // different x coordinate
				{
					// reset y to initial value
					y = y_initial;
				}
			}
			
			y_doubles[i] = y;
		} // i-loop

		// round double values to int coordinates
		for (int i = 0; i < this.model.getTableModel().getRowCount(); i++)
		{
			sortedData[i][1] = (int) y_doubles[i];
		}
		
		// zet sortedData in coords in de originele volgorde
		for (int i = 0; i < sortedData.length; i++)
		{
			int index = sortedData[i][3]; // index within the original order
			coords[index][0] = sortedData[i][0];
			coords[index][1] = sortedData[i][1];
		}
		
		return coords;
	}

	/**
	 * Get the frequency of the most frequently occurring value in sortedData[0].
	 * 
	 * @param sortedData
	 * @return
	 * 		The frequency of the most frequently occurring value in sortedData[0].
	 */
	private int[] getMaxFrequencyXPerSplit(int[][] sortedData)
	{
	    int[] maxCount = new int [this.splitClasses];
		
		if ((sortedData != null) && (sortedData.length != 0))
		{
			for (int split = 0; split < this.splitClasses; split++)
			{
			    int previous = sortedData[0][0];
			    int count = 1;
		
				for (int i = 1; i < sortedData.length; i++)
				{
			        if (sortedData[i][0] == previous) // x coordinate same as previous one
			        {
			        	if (sortedData[i][2] == split) //  in split
			        		count++;
			        }
			        else // different x coordinate
			        {
			            if (count > maxCount[split]) 
			            {
			                maxCount[split] = count;
			            }
			            previous = sortedData[i][0];
			            count = 1;
			        }
			    }
	            if (count > maxCount[split]) 
	            {
	                maxCount[split] = count;
	            }
			}
		}
		
		return maxCount;
	}

	/**
	 * Determine the coordinates for all objects. Used only in single variable
	 * cases, with that variable on the y-axis
	 * 
	 * @return a matrix containing the x and y coordinate for all objects
	 */
	private int[][] determineCoordsYSingleVar()
	{
		int[][] coords = new int[this.model.getTableModel().getRowCount()][2];
		int[] splitClasses = new int[this.model.getTableModel().getRowCount()];

		/*
		 * int drawHeight = this.getHeight() - DotplotView.X_AS_OFFSET;
		 * if(this.model.isShowUserOptions()) { drawHeight -=
		 * DotplotView.KEUZEBALK_HOOGTE; }
		 */
		int dotSizeSquared = (int) (Math.pow(2 * this.dotSize + 1, 2));
		for (int i = 0; i < this.model.getTableModel().getRowCount(); i++)
		{
			if (this.model.getTableModel()
				.getValueAt(i, this.model.getColumnYIndex())
				.equals(ColumnType.WILDCARD))
			{
				// skip wildcard cases
				splitClasses[i] = -1;
				continue;
			}
			splitClasses[i] = this.getSplitClass(i);

			// voor verdeling binnen 1 veld:
			if (DotplotView.this.model.splitInSingleView())
				splitClasses[i] = 0;

			coords[i][1] = this.determineYCoord(i);

			// find the lowest x value for point i such that the distance to all
			// other points is
			// greater than DotplotView.KEEP_CLEAR_PART
			int x = (int) (this.yAxisOffset + (DotplotView.KEEP_CLEAR_PART * (this
				.getWidth() - this.yAxisOffset)));

			for (int j = 0; j < i; j++)
			{
				if (!this.model.getTableModel()
					.getValueAt(j, this.model.getColumnYIndex())
					.equals(ColumnType.WILDCARD)
					&& splitClasses[i] == splitClasses[j]
					&& (Math.pow(coords[j][0] - x, 2) + Math.pow(coords[j][1]
						- coords[i][1], 2)) < dotSizeSquared)
				{
					// some other object is too close, so start over with
					// smaller y
					x += 2;
					j = -1; // j = -1 will cause the for loop to start over with
							// j = 0
				}
			}
			coords[i][0] = x;
		}

		return coords;
	}

	/**
	 * Determine in how many classes the split variable splits the data
	 * 
	 * @return the amount of classes in which the split variable splits the data
	 */
	private int numberOfSplitClasses()
	{
		if (!this.model.columnSplitIndexValid())
		{
			return 1;
		}
		else
		{
			ColumnType cType = this.model.getTableModel().getColumnTypes()
				.get(this.model.getColumnSplitIndex());
			if (this.splitType.isNumber())
			{
				if (this.model.getSplitBinBoundaries() == null)
					return 1;
				else
					return this.model.getSplitBinBoundaries().size() - 1;
			}
			else if (this.splitType.equals(AllowedTypes.ENUM))
			{
				return cType.getEnumOptions().length - 1;
			}
			else
			{
				return this.model.getTableModel()
					.getStringOptions(this.model.getColumnSplitIndex()).size();
			}
		}
	}

	/**
	 * Determine in which split class the object at rowIndex is
	 * 
	 * @param rowIndex
	 *            the index of the object to classify
	 * @return the split class in which the object at rowIndex is
	 * 		Is -1 if the object cannot be classified.
	 * 		Is -2 if the object is a wildcard.
	 */
	private int getSplitClass(int rowIndex)
	{
		return this.model.getTableModel().classifyObject(rowIndex,
			this.model.getSplitOptions());
		/*
		 * if(!this.model.columnSplitIndexValid()) { return 0; }
		 * 
		 * String s = (String)this.model.getTableModel().getValueAt(rowIndex,
		 * this.model.getColumnSplitIndex()); if(s.equals(ColumnType.WILDCARD))
		 * { return -1; }
		 * 
		 * if(this.splitType.isNumber()) { Double d = Double.parseDouble(s); int
		 * bin = -1; while(bin < this.model.getSplitBinBoundaries().size()-1 &&
		 * d > this.model.getSplitBinBoundaries().get(bin+1)) { bin++; } return
		 * bin; } else if(this.splitType.equals(AllowedTypes.ENUM)) { int index
		 * =
		 * Arrays.asList(this.model.getTableModel().getColumnTypes().get(this.model
		 * .getColumnSplitIndex()).getEnumOptions()).indexOf(s); int
		 * wildcardIndex =
		 * Arrays.asList(this.model.getTableModel().getColumnTypes
		 * ().get(this.model
		 * .getColumnSplitIndex())).indexOf(ColumnType.WILDCARD);
		 * if(wildcardIndex >= 0 && wildcardIndex < index) { index--; }
		 * 
		 * return index; } else { return
		 * this.model.getTableModel().getStringOptions
		 * (this.model.getColumnSplitIndex()).indexOf(s); }
		 */
	}

	private Color getColor(int number)
	{
		return ColorGenerator.getColor(number);
	}

	public void paintComponent(Graphics g)
	{
		// test syl: tbv background
//		System.out.println("DotplotView.paintComponent()");
		super.paintComponent(g);

		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		// Graphics2D g2D = (Graphics2D)g;
		// g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	/**
	 * Get the type of the x variable.
	 * @return The allowed type
	 */
	public AllowedTypes getXType()
	{
		return this.xType;
	}

	/**
	 * Get the type of the y variable.
	 * @return The allowed type
	 */
	public AllowedTypes getYType()
	{
		return this.yType;
	}

	/**
	 * MouseListener that enables the user to select objects by clicking on one
	 * or by dragging the mouse
	 * 
	 * @author Manu Drijvers
	 * 
	 */
	private class DotClickListener implements MouseListener,
		MouseMotionListener
	{
		private Point startDrag;
		private Point currentDragLocation;
		private boolean inDrag = false;

		public void mouseClicked(MouseEvent arg0)
		{
			if (DotplotView.this.objectLocations == null)
			{
				return;
			}

			ArrayList<Point> pointList = DotplotView.this.objectLocations;
			if (arg0.getButton() == MouseEvent.BUTTON1)
			{
				int i;
				// check if a dot was clicked
				for (i = 0; i < pointList.size(); i++)
				{
					Point p = pointList.get(i);
					if (p != null && p.distance(arg0.getPoint()) <= dotSize)
					{
						break;
					}
				}

				if (i < pointList.size())
				{
					// if i < pointsList.size() then a dot was clicked, so
					// select the object
					// create a new selection list
					ArrayList<Boolean> selectionList;
					
					// detect control click
					boolean controlClicked = false;
					if ((arg0.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK) 
						controlClicked = true;
					
					if (controlClicked)
					{
						// get the current selection list
						selectionList = model.getTableModel().getSelectionList();
						
						for (int j = 0; j < pointList.size(); j++)
						{
							if (i == j)
								// add selection to current selectionlist
								selectionList.set(j, true);
						}
					}
					else
					{
						// new selection list
						selectionList = new ArrayList<Boolean>(
							pointList.size());
					
						for (int j = 0; j < pointList.size(); j++)
						{
							selectionList.add(i == j);
						}
					}
					
					// update the selection
					model.getTableModel().setSelectionList(selectionList);
				}
			}

		}

		public void mouseEntered(MouseEvent arg0)
		{
			// TODO Auto-generated method stub
		}

		public void mouseExited(MouseEvent arg0)
		{
			// TODO Auto-generated method stub
		}

		public void mousePressed(MouseEvent arg0)
		{
			if (DotplotView.this.objectLocations == null)
			{
				return;
			}
			// store the location where the moues drag started
			this.startDrag = arg0.getPoint();
			this.currentDragLocation = this.startDrag;
			this.inDrag = true;

		}

		public void mouseReleased(MouseEvent arg0)
		{
			if (!this.inDrag || DotplotView.this.objectLocations == null)
			{
				return;
			}

			// the mousedrag ended
			ArrayList<Point> pointList = DotplotView.this.objectLocations;

			int x1 = this.startDrag.x;
			int x2 = arg0.getPoint().x;
			int y1 = this.startDrag.y;
			int y2 = arg0.getPoint().y;

			// create the rectangle between the drag start location and the drag
			// release location
			Rectangle r = new Rectangle(Math.min(x1, x2), Math.min(y1, y2),
				Math.abs(x1 - x2), Math.abs(y1 - y2));

			// determine which points were selected
			ArrayList<Boolean> selectionList;
			
			// detect control click
			boolean controlClicked = false;
			if ((arg0.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK) 
				controlClicked = true;
			
			if (controlClicked)
			{
				// get the current selection list
				selectionList = model.getTableModel().getSelectionList();

				for (int i = 0; i < pointList.size(); i++)
				{
					Point p = pointList.get(i);
					if (p != null && r.contains(p))
						selectionList.set(i, true);
				}
			}
			else
			{
				// new selection list
				selectionList = new ArrayList<Boolean>(
					pointList.size());

				for (int i = 0; i < pointList.size(); i++)
				{
					Point p = pointList.get(i);
					selectionList.add(p != null && r.contains(p));
				}
			}

			// update the selection
			DotplotView.this.model.getTableModel().setSelectionList(
				selectionList);

			this.inDrag = false;
		}

		public void mouseDragged(MouseEvent arg0)
		{
			// repaint the view to show the user which points would be selected
			// if he releases the mouse
			this.currentDragLocation = arg0.getPoint();
			DotplotView.this.repaint();
		}

		public void mouseMoved(MouseEvent arg0)
		{

		}
	} // DotClickListener class

	private class DotPanel extends JPanel
	{
		public void paintComponent(Graphics g)
		{
			// test syl: tbv background white
//			System.out.println("DotplotView$DotPanel.paintComponent(): w = " 
//				+ this.getWidth() + ", h = " + this.getHeight());
			super.paintComponent(g);
			DotplotView.super.paintComponent(g);

			g.clearRect(0, 0, this.getWidth(), this.getHeight());
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

			// if the user is dragging the mouse, draw the rectangle that will
			// be selected if
			// the user would release the mouse
			if (DotplotView.this.dotClickListener.inDrag)
			{
				int x1 = DotplotView.this.dotClickListener.startDrag.x;
				int y1 = DotplotView.this.dotClickListener.startDrag.y;
				int x2 = DotplotView.this.dotClickListener.currentDragLocation.x;
				int y2 = DotplotView.this.dotClickListener.currentDragLocation.y;

				g.setColor(SELECTION_RECTANGLE_COLOR);
				g.fillRect(Math.min(x1, x2), Math.min(y1, y2),
					Math.abs(x1 - x2), Math.abs(y1 - y2));
			}

			g.setColor(Color.BLACK);
			
			// sorteer de rij-indices zodat selected rijen op het eind staan
			// en de bijbehorende dots als laatste worden getekend
			final StatTableModel tableModel = DotplotView.this.model.getTableModel();
			int nrRows = tableModel.getRowCount();
			Integer[] indexSortedOnSelected = new Integer[nrRows];
			for (int i = 0; i < nrRows; i++)
			{
				indexSortedOnSelected[i] = i;
			}
			
			Arrays.sort(indexSortedOnSelected, new Comparator<Integer>() {
	            @Override
	            /**
	             * Compare integers i1 and i2 on being selected.
	             * and split. 
	             * @param i1
	             * @param i2
	             * @return
	             */
	            public int compare(Integer i1, Integer i2) 
	            {
	            	// if both rows are selected, the order doesn't matter
            		if (tableModel.isRowSelected(i1) && tableModel.isRowSelected(i2))
            			return 0;
	            	// indices of selected rows are always larger 
            		else if (tableModel.isRowSelected(i2))
	            		return -1;
	            	else if (tableModel.isRowSelected(i1))
	            		return 1;
	            	else // if none of the rows is selected, the order doesn't matter
            			return 0;
	            }
	        });

			if (DotplotView.this.model.columnXIndexValid()
				&& DotplotView.this.model.columnYIndexValid())
			{
				// all variables are valid, draw a scatterplot
				DotplotView.this.determineDotSize();
				DotplotView.this.objectLocations = new ArrayList<Point>(
					DotplotView.this.model.getTableModel().getRowCount());
				for (int i = 0; i < DotplotView.this.model.getTableModel()
					.getRowCount(); i++)
				{
					DotplotView.this.objectLocations.add(null);
				}

				DotplotView.this.drawCorrelation(g);

				for (int i = 0; i < DotplotView.this.splitClasses; i++)
				{
					DotplotView.this.paintXAxis(g, i
						* (DotplotView.this.scrollPane.getHeight() - 5));
					DotplotView.this.paintYAxis(g, i
						* (DotplotView.this.scrollPane.getHeight() - 5));
				}

				for (int row = 0; row < DotplotView.this.model.getTableModel()
					.getRowCount(); row++)
				{
//					DotplotView.this.drawPoint(g2d, row);
					// use the ordered indices so that selected dots will be drawn at last
					DotplotView.this.drawPoint(g2d, indexSortedOnSelected[row]);
				}
			} // scatterplot
			else if (DotplotView.this.model.columnXIndexValid())
			{
				// X variable is valid, so draw dotplot
				for (int i = 0; i < DotplotView.this.splitClasses; i++)
				{
					DotplotView.this.paintXAxis(g, i
						* (DotplotView.this.scrollPane.getHeight() - 5));

				}
				
				// only the x-column variable is valid, draw a single variable
				// dot plot with the variable on the x-axis
				// this.setStringOptions();
				DotplotView.this.determineDotSize();
				DotplotView.this.dotSize = DotplotView.this.dotSize * 2;
				DotplotView.this.objectLocations = new ArrayList<Point>(
					DotplotView.this.model.getTableModel().getRowCount());
				for (int i = 0; i < DotplotView.this.model.getTableModel()
					.getRowCount(); i++)
				{
					DotplotView.this.objectLocations.add(null);
				}

				int[][] coords = DotplotView.this.determineCoordsXSingleVar();
				for (int i = 0; i < coords.length; i++)
				{
					// use the ordered indices so that selected dots will be drawn at last
					int index = indexSortedOnSelected[i];

					if (!DotplotView.this.model
						.getTableModel()
						.getValueAt(index, DotplotView.this.model.getColumnXIndex())
						.equals(ColumnType.WILDCARD))
					{
						int splitClass = DotplotView.this.getSplitClass(index);
						if (DotplotView.this.model.splitInSingleView())
							splitClass = 0;
						if (splitClass >= 0)
						{
							int heightOffset;
							if (DotplotView.this.model.getColumnSplitIndex() > -1
								&& DotplotView.this.model.splitInSingleView())
							{
								heightOffset = 0;
							}
							else
							{
								heightOffset = (splitClass)
										* (DotplotView.this.scrollPane.getHeight() - 5);
							}

							DotplotView.this.drawPointAtLocation(g2d,
								coords[index][0], coords[index][1] + heightOffset,
								index);
						}
					}
				}
			} // dotplot
			else if (DotplotView.this.model.columnYIndexValid())
			{
				// Y variable is valid, so draw dotplot

				for (int i = 0; i < DotplotView.this.splitClasses; i++)
				{
					DotplotView.this.paintYAxis(g, i
						* (DotplotView.this.scrollPane.getHeight() - 5));
				}
				
				// only the y-column variable is valid, draw a single variable
				// dot plot with the variable on the y-axis
				DotplotView.this.determineDotSize();
				DotplotView.this.dotSize = DotplotView.this.dotSize * 2;
				DotplotView.this.objectLocations = new ArrayList<Point>(
					DotplotView.this.model.getTableModel().getRowCount());
				for (int i = 0; i < DotplotView.this.model.getTableModel()
					.getRowCount(); i++)
				{
					DotplotView.this.objectLocations.add(null);
				}

				int[][] coords = DotplotView.this.determineCoordsYSingleVar();
				for (int i = 0; i < coords.length; i++)
				{
					// use the ordered indices so that selected dots will be drawn at last
					int index = indexSortedOnSelected[i];

					if (!DotplotView.this.model
						.getTableModel()
						.getValueAt(index, DotplotView.this.model.getColumnYIndex())
						.equals(ColumnType.WILDCARD))
					{
						int splitClass = DotplotView.this.getSplitClass(index);
						if (DotplotView.this.model.splitInSingleView())
							splitClass = 0;
						if (splitClass >= 0)
						{
							int heightOffset;
							if (DotplotView.this.model.getColumnSplitIndex() > -1
								&& DotplotView.this.model.splitInSingleView())
							{
								heightOffset = 0;
							}
							else
							{
								heightOffset = (splitClass)
									* (DotplotView.this.scrollPane.getHeight() - 5);
							}

							// test syl: TODO use sorted indices indexdataRows[]?
							DotplotView.this.drawPointAtLocation(g2d,
								coords[index][0], coords[index][1] + heightOffset,
								index);
						}
					}
				}
			}

			// draw the bottom line and labels
			for (int i = 0; i < (DotplotView.this.isSplitSingleViewSelected() ? 1
				: splitClasses); i++)
			{
				int ySplitOffset = i
					* (DotplotView.this.scrollPane.getHeight() - 5);
				DotplotView.this.paintAxisLabels(g, ySplitOffset, i);
			}
		}

		private void drawSplitGroupLabels(Graphics g)
		{
			if (!DotplotView.this.model.columnSplitIndexValid())
			{
				return;
			}
			String columnName = DotplotView.this.model.getTableModel()
				.getColumnName(DotplotView.this.model.getColumnSplitIndex());
			ColumnType cType = DotplotView.this.model.getTableModel()
				.getColumnTypes()
				.get(DotplotView.this.model.getColumnSplitIndex());
			ArrayList<Double> boundaries = DotplotView.this.model
				.getSplitBinBoundaries();
			String s;
			for (int i = 0; i < DotplotView.this.splitClasses; i++)
			{
				if (cType.getType().isNumber())
				{
					s = boundaries.get(i) + " - " + boundaries.get(i + 1);
				}
				else if (cType.getType().equals(AllowedTypes.ENUM))
				{
					s = cType.getEnumOptions()[i];
				}
				else
				{
					s = DotplotView.this.model
						.getTableModel()
						.getStringOptions(
							DotplotView.this.model.getColumnSplitIndex())
						.get(i);
				}

				int y = (DotplotView.this.splitClasses - i)
					* (DotplotView.this.scrollPane.getHeight() - 5);
				if (DotplotView.this.model.columnXIndexValid())
				{
					g.drawString(columnName + ": " + s, 3, y - 1);
				}
				else
				{
					g.drawString(
						columnName + ": " + s,
						DotplotView.this.scrollPane.getWidth()
							- 20
							- g.getFontMetrics().stringWidth(
								columnName + " : " + s), y - 2);
				}
				g.fillRect(0, y + 2,
					DotplotView.this.scrollPane.getWidth() - 20, 1);
			}
		}
	} // DotPanel class
}
