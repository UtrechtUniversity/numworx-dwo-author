package fi.statistiek.dotplot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import fi.statistiek.ColorLegend;
import fi.statistiek.ColorPreviewer;
import fi.statistiek.DialogButton;
import fi.statistiek.Statistiek;
import fi.statistiek.histogram.HistogramModel;
import fi.statistiek.histogram.HistogramUserOptionsPanel;
import fi.statistiek.histogram.HistogramView;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * MVC View for StatistiekView Scatterplot
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
	public static final int Y_AS_OFFSET = 55;
	public static final double KEEP_CLEAR_PART = 0.05;
	public static final Color SELECTION_RECTANGLE_COLOR = new Color(153, 204,
		255);
	public static final Color DEFAULT_DOT_COLOR = new Color(220, 160, 0);

	private DotClickListener dotClickListener;

	private JScrollPane scrollPane;

	// variables for painting, set every time paint is called
	private double xMin;
	private double xMax;
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

	private Random random;

	private Color[] COLORS =
		{ Color.RED, Color.GREEN, Color.BLUE };
	private ArrayList<Color> colorList;

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

		// Create GUI
		// GridLayout gl = new GridLayout(4,3);
		// gl.setHgap(5);
		// this.userOptionsPanel = new JPanel(gl);
		// this.userOptionsPanel1 = new JPanel();

		userOptionsPanel = new DotplotUserOptionsPanel(this, controller, model);
		dialogButton = userOptionsPanel.getDialogButton();

		super.add(dialogButton, BorderLayout.SOUTH);
		// dialogButton.makeDialog();

		this.mainPanel = new DotPanel();
		this.scrollPane = new JScrollPane(this.mainPanel);
		super.add(this.scrollPane, BorderLayout.CENTER);

		this.dotClickListener = new DotClickListener();
		this.mainPanel.addMouseListener(this.dotClickListener);
		this.mainPanel.addMouseMotionListener(this.dotClickListener);

		this.colorLegend = new ColorLegend("", null, null);
		super.add(this.colorLegend, BorderLayout.EAST);
		// this.colorLegend.setVisible(false);

		this.random = new Random();
		this.colorList = new ArrayList<Color>(Arrays.asList(COLORS));
	}

	private ArrayList<String> getXStringOptions()
	{
		return this.model.getTableModel().getStringOptions(
			this.model.getColumnXIndex());
	}

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
		int ret = this.getWidth() - 20
			- (this.colorLegend.isVisible() ? this.colorLegend.getWidth() : 0);
		if (this.model.columnYIndexValid())
		{
			ret -= DotplotView.Y_AS_OFFSET;
		}
		if (this.splitClasses > 1)
		{
			ret -= 20;
		}
		return ret;
	}

	/**
	 * Determine the height of the area where the dots are painted
	 * 
	 * @return the height of the area where the dots are painted
	 */
	private int dotAreaHeight()
	{
		int ret = this.getHeight();
		if (this.model.columnXIndexValid())
		{
			ret -= DotplotView.X_AS_OFFSET;
		}
		if (this.model.getTableModel().isViewsEditable())
		{
			ret -= DotplotView.KEUZEBALK_HOOGTE;
		}
		return ret;
	}

	public void setModel(DotplotModel model)
	{
		this.model = model;
		this.model.addObserver(this);
		userOptionsPanel.setModel(model);
		this.update(null, null);
	}

	private void setMainPanelSize()
	{
		int splitClasses = this.model.getTableModel().splitVarClasses(
			this.model.getSplitOptions());
		int colorLegendWidth = this.colorLegend.isVisible() ? this.colorLegend
			.getPreferredSize().width : 0;
		if (this.model.splitInSingleView())
		{
			this.mainPanel.setPreferredSize(new Dimension(this.scrollPane
				.getWidth() - colorLegendWidth - 20, this.scrollPane
				.getHeight() - 5));
		}
		else
		{
			this.mainPanel.setPreferredSize(new Dimension(this.scrollPane
				.getWidth() - colorLegendWidth - 20, splitClasses
				* (this.scrollPane.getHeight() - 5) + 1));
		}
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
		this.setTypes();
		this.setMinMax();

		if (this.model.columnSplitIndexValid())
		{
			this.setSplitType();
			// this.splitBinsBox.setSelectedIndex(this.splitVarClasses()-1);
			// this.splitBinsBox.setEnabled(this.model.getTableModel().getColumnTypes().get(this.model.getColumnSplitIndex()).getType().isNumber());
		}
		else
		{
			// this.splitBinsBox.setEnabled(false);
		}

		this.dialogButton.setVisible(this.model.getTableModel()
			.isViewsEditable());

		if (this.model.columnSplitIndexValid())
		{
			splitClasses = this.splitVarClasses();
		}
		else
		{
			splitClasses = 1;
		}

		if (this.updateColorLegend() || true)
		{
			this.setMainPanelSize();
		}

		userOptionsPanel.update();

		this.repaint();
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

	public double getSplitminBoundary()
	{
		return userOptionsPanel.getSplitminBoundary();
	}

	public double getSplitBinWidth()
	{
		return userOptionsPanel.getSplitBinWidth();
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
		// if not using a colorscale, the color is always black
		return DotplotView.DEFAULT_DOT_COLOR;
	}

	/**
	 * Determine where the dot representing an object has to be painted
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
	 * Determine where a numeric value would be painted in the scatterplot
	 * 
	 * @param d
	 *            a numeric value
	 * @return the x-coordinate of where the value would be painted
	 */
	private int determineXCoordNumClass(double d)
	{
		// return DotplotView.Y_AS_OFFSET + (int)((DotplotView.KEEP_CLEAR_PART +
		// (1-2*DotplotView.KEEP_CLEAR_PART)*((d-xMin)/(xMax-xMin)))*(this.getWidth()-DotplotView.Y_AS_OFFSET));
		int ret = (int) ((DotplotView.KEEP_CLEAR_PART + (1 - 2 * DotplotView.KEEP_CLEAR_PART)
			* ((d - xMin) / (xMax - xMin))) * this.dotAreaWidth());
		if (this.model.columnYIndexValid())
		{
			ret += DotplotView.Y_AS_OFFSET;
		}
		return ret;
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

		if (cType.getEnumOptions().length == 2)
		{
			if (this.model.columnYIndexValid())
			{
				return DotplotView.Y_AS_OFFSET
					+ (this.getWidth() - DotplotView.Y_AS_OFFSET) / 2;
			}
			else
			{
				return this.getWidth() / 2;
			}
		}
		int index = cType.indexOfStringInEnum(value);
		if (cType.indexOfStringInEnum(ColumnType.WILDCARD) < index)
		{
			index--;
		}

		double d = (double) index
			/ (double) (cType.getEnumOptions().length - 2);
		int ret = (int) ((DotplotView.KEEP_CLEAR_PART + (1 - 2 * DotplotView.KEEP_CLEAR_PART)
			* d) * this.dotAreaWidth());
		if (this.model.columnYIndexValid())
		{
			ret += DotplotView.Y_AS_OFFSET;
		}
		return ret;
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
				return DotplotView.Y_AS_OFFSET
					+ (this.getWidth() - DotplotView.Y_AS_OFFSET) / 2;
			}
			else
			{
				return this.getWidth() / 2;
			}
		}
		double d = (double) this.getXStringOptions().indexOf(value)
			/ (double) (this.getXStringOptions().size() - 1);
		int ret = (int) ((DotplotView.KEEP_CLEAR_PART + (1 - 2 * DotplotView.KEEP_CLEAR_PART)
			* d) * (this.getWidth() - DotplotView.Y_AS_OFFSET));
		if (this.model.columnYIndexValid())
		{
			ret += DotplotView.Y_AS_OFFSET;
		}
		return ret;
	}

	/**
	 * Determine where the dot representing an object has to be painted
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
	 * Determine where a numeric value would be painted in the scatterplot
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
		g.setColor(this.determineColor(rowIndex));
		g.fillOval(x - this.dotSize, y - this.dotSize, 2 * this.dotSize,
			2 * this.dotSize);
		g.setColor(Color.BLACK);
		g.drawOval(x - this.dotSize, y - this.dotSize, 2 * this.dotSize,
			2 * this.dotSize);

		this.objectLocations.set(rowIndex, new Point(x, y));
	}

	private void drawPointAtLocation(Graphics2D g, int x, int y, int rowIndex,
		Color c)
	{
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
		g.fillOval(x - this.dotSize, y - this.dotSize, 2 * this.dotSize,
			2 * this.dotSize);
		g.setColor(Color.BLACK);
		g.drawOval(x - this.dotSize, y - this.dotSize, 2 * this.dotSize,
			2 * this.dotSize);

		this.objectLocations.set(rowIndex, new Point(x, y));
	}

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
		g.drawString(s1, fm.getHeight() - 2, this.scrollPane.getHeight() / 2
			+ fm.stringWidth(s1) / 2 + yOffset);
		g.setFont(font);
		g.drawString(s2,
			(this.getWidth() - this.Y_AS_OFFSET - fm.stringWidth(s2)) / 2
				+ this.Y_AS_OFFSET, this.scrollPane.getHeight()
				+ this.X_AS_OFFSET - 37 + yOffset);

		if (this.model.getTableModel().splitVarClasses(
			this.model.getSplitOptions()) > 1
			&& !this.model.splitInSingleView())
		{
			String name = this.model.getTableModel().getColumnName(
				this.model.getSplitOptions().getColumnSplitIndex());
			String s = name
				+ ": "
				+ this.model.getSplitOptions().getSplitClassLabel(splitClass,
					this.model.getTableModel());
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
		FontMetrics fm = g.getFontMetrics();

		ColumnType columnType = this.model.getTableModel().getColumnTypes()
			.get(this.model.getColumnXIndex());

		int y = this.model.getTableModel().isViewsEditable() ? this.getHeight()
			- DotplotView.KEUZEBALK_HOOGTE - DotplotView.X_AS_OFFSET : this
			.getHeight() - DotplotView.X_AS_OFFSET;
		if (this.model.columnYIndexValid())
		{
			g.drawLine(DotplotView.Y_AS_OFFSET, y + heightOffset,
				this.getWidth(), y + heightOffset);
		}
		else
		{
			g.drawLine(0, y + heightOffset, this.getWidth(), y + heightOffset);
		}

		if (this.xType.equals(AllowedTypes.ENUM))
		{
			for (int i = 0; i < columnType.getEnumOptions().length; i++)
			{
				String option = columnType.getEnumOptions()[i];
				if (option.equals(ColumnType.WILDCARD))
				{
					continue;
				}
				int x = this.determineXCoordEnumClass(option);
				g.drawLine(x, y + heightOffset, x, y + 5 + heightOffset);
				g.drawString(option, x - (int) (0.5 * fm.stringWidth(option)),
					y + 5 + fm.getHeight() + heightOffset);
			}

		}
		else if (this.xType.equals(AllowedTypes.STRING))
		{
			int amountOfOptions = this.getXStringOptions().size();
			for (int i = 0; i < amountOfOptions; i++)
			{
				String option = this.getXStringOptions().get(i);
				int x = this.determineXCoordStringClass(option);

				g.drawLine(x, y + heightOffset, x, y + 5 + heightOffset);
				g.drawString(option, x - (int) (0.5 * fm.stringWidth(option)),
					y + 5 + fm.getHeight() + heightOffset);
			}
		}
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

			int minorStep;
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
					minorStep = (int) (2 * Math.pow(10, exp - 1));
					minorStepsPerMajorStep = 5;
					break;
				default:
					minorStep = 1;
					minorStepsPerMajorStep = step;
			}

			double min = this.xMin - DotplotView.KEEP_CLEAR_PART
				* (this.xMax - this.xMin);
			double max = this.xMax + DotplotView.KEEP_CLEAR_PART
				* (this.xMax - this.xMin);

			double p = Math.ceil(min / step) * step;

			// Math.ceil can give -0.0, this step turns that into 0.0
			if (p == 0)
			{
				p = 0.0;
			}

			while (p < max)
			{
				int x = this.determineXCoordNumClass(p);
				g.drawLine(x, y + heightOffset, x, y + 2 + heightOffset);

				p += minorStep;
			}

			p = Math.ceil(min / step) * step;

			// Math.ceil can give -0.0, this step turns that into 0.0
			if (p == 0)
			{
				p = 0.0;
			}

			while (p < max)
			{
				int x = this.determineXCoordNumClass(p);
				g.drawLine(x, y + heightOffset, x, y + 5 + heightOffset);
				
				// get the right string value for integer or double
				String pString = getStringValue(p);
//				g.drawString(Double.toString(p),
//					x - (int) (0.5 * fm.stringWidth(Double.toString(p))), y + 5
//						+ fm.getHeight() + heightOffset);
				g.drawString(pString,
					x - (int) (0.5 * fm.stringWidth(pString)), y + 5
						+ fm.getHeight() + heightOffset);

				p += step;
			}
		}
	}

	/*
	 * Get the string value of p according to the type (Integer or Double).
	 */
	private String getStringValue(double p)
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

	/**
	 * Paint the y-axis
	 * 
	 * @param g
	 *            The graphics in which the x-axis will be painted
	 */
	private void paintYAxis(Graphics g, int heightOffset)
	{
		g.setColor(Color.BLACK);
		FontMetrics fm = g.getFontMetrics();

		ColumnType columnType = this.model.getTableModel().getColumnTypes()
			.get(this.model.getColumnYIndex());

		int x = DotplotView.Y_AS_OFFSET;
		int drawHeight = this.dotAreaHeight();
		g.drawLine(x, 5 + heightOffset, x, drawHeight + heightOffset);

		if (yType.equals(AllowedTypes.ENUM))
		{
			for (int i = 0; i < columnType.getEnumOptions().length; i++)
			{
				String option = columnType.getEnumOptions()[i];
				if (option.equals(ColumnType.WILDCARD))
				{
					continue;
				}
				int y = this.determineYCoordEnumClass(option);
				g.drawLine(x - 5, y + heightOffset, x, y + heightOffset);
				g.drawString(option, x - 7 - fm.stringWidth(option), (int) (y
					+ 0.5 * fm.getHeight() - 3)
					+ heightOffset);
			}

		}
		else if (this.yType.equals(AllowedTypes.STRING))
		{
			int amountOfOptions = this.getYStringOptions().size();
			for (int i = 0; i < amountOfOptions; i++)
			{
				String option = this.getYStringOptions().get(i);
				int y = this.determineYCoordStringClass(option);
				g.drawLine(x - 5, y + heightOffset, x, y + heightOffset);
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

			// Math.ceil can give -0.0, this step turns that into 0.0
			if (p == 0)
			{
				p = 0.0;
			}

			while (p < max)
			{
				int y = this.determineYCoordNumClass(p);
				if ((y + (int) (0.5 * fm.getHeight() - 3)) < 0)
				{
					continue;
				}
				g.drawLine(x - 5, y + heightOffset, x, y + heightOffset);
				
				// get the right string value for integer or double
				String pString = getStringValue(p);
//				g.drawString(Double.toString(p),
//					x - 7 - fm.stringWidth(Double.toString(p)), y
//						+ (int) (0.5 * fm.getHeight() - 3) + heightOffset);
				g.drawString(pString,
					x - 7 - fm.stringWidth(pString), y
						+ (int) (0.5 * fm.getHeight() - 3) + heightOffset);

				p += step;
			}
		}
	}

	/**
	 * Find the mean value of a column In case of an Enum type column, it find
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
	private double columnMean(int column, ArrayList<String> options)
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
	 * Finds a column's standard deviation In case of an Enum type column, this
	 * uses the index in the enumoptions instead of the enum value In case of a
	 * String type column, this uses the index in the options arraylist instead
	 * of the string value
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
	private double stdDev(int column, double mean, ArrayList<String> options)
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

		return Math.sqrt(sum / (double) count);
	}

	/**
	 * Find the covariance between two columns
	 * 
	 * @param columnA
	 *            Index of the first column
	 * @param meanA
	 *            mean value of the first column
	 * @param optionsA
	 *            All possible unique elements of the first column, only
	 *            necessary for string columns
	 * @param columnB
	 *            Index of the first column
	 * @param meanB
	 *            mean value of the first column
	 * @param optionsB
	 *            All possible unique elements of the first column, only
	 *            necessary for string columns
	 * @return The covariance between columnA and columnB
	 */
	private double covariance(int columnA, double meanA,
		ArrayList<String> optionsA, int columnB, double meanB,
		ArrayList<String> optionsB)
	{
		ColumnType columnTypeA = this.model.getTableModel().getColumnTypes()
			.get(columnA);
		AllowedTypes typeA = columnTypeA.getType();
		ColumnType columnTypeB = this.model.getTableModel().getColumnTypes()
			.get(columnB);
		AllowedTypes typeB = columnTypeB.getType();

		int count = 0;
		double sum = 0;
		for (int i = 0; i < this.model.getTableModel().getRowCount(); i++)
		{
			String valueStringA = (String) this.model.getTableModel()
				.getValueAt(i, columnA);
			if (valueStringA.equals(ColumnType.WILDCARD))
			{
				continue;
			}
			double valueA;
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
			for (int j = 0; j < this.model.getTableModel().getRowCount(); j++)
			{
				String valueStringB = (String) this.model.getTableModel()
					.getValueAt(i, columnB);
				if (valueStringB.equals(ColumnType.WILDCARD))
				{
					continue;
				}

				double valueB;
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

				count++;
				sum += (valueA - meanA) * (valueB - meanB);
			}
		}

		return sum / (double) count;
	}

	/**
	 * Paint the correlation
	 * 
	 * @param g
	 *            The graphics in which the correlation will be painted
	 */
	private void drawCorrelation(Graphics g)
	{
		if (this.model.isShowCorrelation()
			&& !this.model.columnSplitIndexValid())
		{
			g.setColor(Color.BLACK);

			int columnA = this.model.getColumnXIndex();
			int columnB = this.model.getColumnYIndex();
			double meanA = this.columnMean(columnA, this.getXStringOptions());
			double meanB = this.columnMean(columnB, this.getYStringOptions());

			double covar = this.covariance(columnA, meanA,
				this.getXStringOptions(), columnB, meanB,
				this.getYStringOptions());
			double sdA = this.stdDev(columnA, meanA, this.getXStringOptions());
			double sdB = this.stdDev(columnB, meanB, this.getYStringOptions());
			double correlation = covar / (sdA * sdB);

			int y = this.model.getTableModel().isViewsEditable() ? this
				.getHeight() - 10 - DotplotView.KEUZEBALK_HOOGTE : this
				.getHeight() - 3;
			g.drawString(
				"Corr(X,Y) = "
					+ Double.toString(Math.round(correlation * 100) / 100.0),
				3, y);
		}
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

			// find the lowest y value for point i such that the distance to all
			// other points is
			// greater than DotplotView.KEEP_CLEAR_PART
			int y = (int) ((1 - DotplotView.KEEP_CLEAR_PART) * drawHeight); // initial
																			// y

			for (int j = 0; j < i; j++)
			{
				if (!this.model.getTableModel()
					.getValueAt(j, this.model.getColumnXIndex())
					.equals(ColumnType.WILDCARD)
					&& splitClasses[i] == splitClasses[j]
					&& (Math.pow(coords[j][0] - coords[i][0], 2) + Math.pow(
						coords[j][1] - y, 2)) < dotSizeSquared)
				{
					// some other object is too close, so start over with
					// smaller y
					y -= 2;
					j = -1; // j = -1 will cause the for loop to start over with
							// j = 0
				}
			}

			coords[i][1] = y;
		}

		return coords;
	}

	/**
	 * Determine the coordinates for all objects Used only in single variable
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
			int x = (int) (DotplotView.Y_AS_OFFSET + (DotplotView.KEEP_CLEAR_PART * (this
				.getWidth() - DotplotView.Y_AS_OFFSET)));

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
	private int splitVarClasses()
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
		if (number < this.colorList.size())
		{
			return this.colorList.get(number);
		}
		else
		{
			Color c = new Color(this.random.nextInt(256),
				this.random.nextInt(256), this.random.nextInt(256));
			this.colorList.add(c);
			return c;
		}
	}

	public void paintComponent(Graphics g)
	{
		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		// Graphics2D g2D = (Graphics2D)g;
		// g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);

		if (this.model.isUseColorScale() && !this.model.columnColorIndexValid())
		{
			return;
		}

		if (this.model.splitInSingleView())
			this.mainPanel.setPreferredSize(new Dimension(this.scrollPane
				.getWidth() - 20, this.scrollPane.getHeight() - 5));
		else
			this.mainPanel.setPreferredSize(new Dimension(this.scrollPane
				.getWidth() - 20, splitClasses
				* (this.scrollPane.getHeight() - 5)));
		this.scrollPane.setViewportView(mainPanel);
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
					ArrayList<Boolean> selectionList = new ArrayList<Boolean>(
						pointList.size());
					for (int j = 0; j < pointList.size(); j++)
					{
						selectionList.add(i == j);
					}
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
			ArrayList<Boolean> selectionList = new ArrayList<Boolean>(
				pointList.size());
			for (int i = 0; i < pointList.size(); i++)
			{
				Point p = pointList.get(i);
				selectionList.add(p != null && r.contains(p));
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
	}

	private class DotPanel extends JPanel
	{
		public void paintComponent(Graphics g)
		{
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
			// this.drawSplitGroupLabels(g2d);

			if (DotplotView.this.model.columnXIndexValid()
				&& DotplotView.this.model.columnYIndexValid()
				&& (!DotplotView.this.model.isUseColorScale() || DotplotView.this.model
					.columnColorIndexValid()))
			{
				// if(DotplotView.this.model.columnXIndexValid() &&
				// DotplotView.this.model.columnYIndexValid() &&
				// (!(DotplotView.this.model.columnZIndexValid() &&
				// !DotplotView.this.model.splitInSingleView()) ||
				// DotplotView.this.model.columnColorIndexValid())) {
				// all variables are valid, draw a scatterplot
				// this.setStringOptions();
				DotplotView.this.determineDotSize();
				DotplotView.this.objectLocations = new ArrayList<Point>(
					DotplotView.this.model.getTableModel().getRowCount());
				for (int i = 0; i < DotplotView.this.model.getTableModel()
					.getRowCount(); i++)
				{
					DotplotView.this.objectLocations.add(null);
				}

				DotplotView.this.drawCorrelation(g);

				for (int row = 0; row < DotplotView.this.model.getTableModel()
					.getRowCount(); row++)
				{
					DotplotView.this.drawPoint(g2d, row);
				}

				for (int i = 0; i < DotplotView.this.splitClasses; i++)
				{
					DotplotView.this.paintXAxis(g, i
						* (DotplotView.this.scrollPane.getHeight() - 5));
					DotplotView.this.paintYAxis(g, i
						* (DotplotView.this.scrollPane.getHeight() - 5));
				}
			}
			// else if(DotplotView.this.model.columnXIndexValid() &&
			// (!DotplotView.this.model.isUseColorScale() ||
			// DotplotView.this.model.columnColorIndexValid())) {
			else if (DotplotView.this.model.columnXIndexValid()
				&& (!DotplotView.this.model.isUseColorScale() || DotplotView.this.model
					.columnColorIndexValid()))
			{
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
					if (!DotplotView.this.model
						.getTableModel()
						.getValueAt(i, DotplotView.this.model.getColumnXIndex())
						.equals(ColumnType.WILDCARD))
					{
						int splitClass = DotplotView.this.getSplitClass(i);
						if (DotplotView.this.model.splitInSingleView())
							splitClass = 0;
						if (splitClass >= 0)
						{
							int heightOffset = (splitClass)
								* (DotplotView.this.scrollPane.getHeight() - 5);
							if (DotplotView.this.model.getColumnSplitIndex() > -1
								&& DotplotView.this.model.splitInSingleView())
							{
								heightOffset = 0;
								DotplotView.this.drawPointAtLocation(g2d,
									coords[i][0], coords[i][1] + heightOffset,
									i);

							}
							else if (DotplotView.this.model
								.getColumnSplitIndex() > -1)
							{
								DotplotView.this.drawPointAtLocation(g2d,
									coords[i][0], coords[i][1] + heightOffset,
									i);
							}
							else
							{
								DotplotView.this.drawPointAtLocation(g2d,
									coords[i][0], coords[i][1] + heightOffset,
									i);
							}

						}
					}
				}

				for (int i = 0; i < DotplotView.this.splitClasses; i++)
				{
					DotplotView.this.paintXAxis(g, i
						* (DotplotView.this.scrollPane.getHeight() - 5));

				}

			}
			else if (DotplotView.this.model.columnYIndexValid()
				&& (!DotplotView.this.model.isUseColorScale() || DotplotView.this.model
					.columnColorIndexValid()))
			{
				// only the y-column variable is valid, draw a single variable
				// dot plot with the variable on the y-axis
				// this.setStringOptions();
				DotplotView.this.determineDotSize();
				DotplotView.this.dotSize = DotplotView.this.dotSize * 2;
				DotplotView.this.objectLocations = new ArrayList<Point>(
					DotplotView.this.model.getTableModel().getRowCount());
				for (int i = 0; i < DotplotView.this.model.getTableModel()
					.getRowCount(); i++)
				{
					// int heightOffset =
					// (DotplotView.this.splitClasses-DotplotView.this.getSplitClass(i)-1)*(DotplotView.this.scrollPane.getHeight()-5);
					DotplotView.this.objectLocations.add(null);
				}

				int[][] coords = DotplotView.this.determineCoordsYSingleVar();
				for (int i = 0; i < coords.length; i++)
				{
					if (!DotplotView.this.model
						.getTableModel()
						.getValueAt(i, DotplotView.this.model.getColumnYIndex())
						.equals(ColumnType.WILDCARD))
					{
						int splitClass = DotplotView.this.getSplitClass(i);
						// if(splitClass >= 0) {
						// int heightOffset =
						// (DotplotView.this.splitClasses-splitClass-1)*(DotplotView.this.scrollPane.getHeight()-5);
						// DotplotView.this.drawPointAtLocation(g2d,
						// coords[i][0], coords[i][1]+heightOffset, i);
						//
						// }
						if (DotplotView.this.model.splitInSingleView())
							splitClass = 0;
						if (splitClass >= 0)
						{
							int heightOffset = (splitClass)
								* (DotplotView.this.scrollPane.getHeight() - 5);
							if (DotplotView.this.model.getColumnSplitIndex() > -1
								&& DotplotView.this.model.splitInSingleView())
							{
								heightOffset = 0;
								DotplotView.this.drawPointAtLocation(g2d,
									coords[i][0], coords[i][1] + heightOffset,
									i);

							}
							else if (DotplotView.this.model
								.getColumnSplitIndex() > -1)
							{
								DotplotView.this.drawPointAtLocation(g2d,
									coords[i][0], coords[i][1] + heightOffset,
									i);
							}
							else
							{
								DotplotView.this.drawPointAtLocation(g2d,
									coords[i][0], coords[i][1] + heightOffset,
									i);
							}
						}
					}
				}

				for (int i = 0; i < DotplotView.this.splitClasses; i++)
				{
					DotplotView.this.paintYAxis(g, i
						* (DotplotView.this.scrollPane.getHeight() - 5));
				}
			}

			// draw the bottom line
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
	}
}
