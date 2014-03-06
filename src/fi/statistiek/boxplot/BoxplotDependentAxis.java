package fi.statistiek.boxplot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import fi.statistiek.dotplot.DotplotView;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * 
 * Component that displays the dependent axis in a boxplot view
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 * 
 */
public class BoxplotDependentAxis extends JPanel
{
	private double dataMinValue; // the minimum value in all the data
	private double dataMaxValue; // the maximum value in all the data
	private boolean verticalBoxplots;
	private BoxplotView boxplotView;

	private String columnName;

	private double firstMarker;
	private int step;
	private double max;

	/**
	 * Constructor
	 * 
	 * @param dataMinValue
	 *            the lowest value in the data
	 * @param dataMaxValue
	 *            the highest value in the data
	 * @param verticalBoxplots
	 *            true if using vertical boxplots, false otherwise
	 * @param boxplotView
	 *            the BoxplotView that uses this
	 * @param columnName
	 *            the name of the column thats represented by this
	 */
	public BoxplotDependentAxis(double dataMinValue, double dataMaxValue,
		boolean verticalBoxplots, BoxplotView boxplotView, String columnName)
	{
		this.dataMaxValue = dataMaxValue;
		this.dataMinValue = dataMinValue;
		this.verticalBoxplots = verticalBoxplots;
		this.boxplotView = boxplotView;
		this.columnName = columnName;

		this.determineScale();
		super.setPreferredSize(new Dimension(50, 50));
	}

	/**
	 * Determines the y-coordinate on the screen in this component of value d
	 * 
	 * @return the y-coordinate on the screen in this component of value d
	 */
	private int valueToScreenLocation(double d)
	{
		double a = (this.dataMaxValue - d)
			/ (this.dataMaxValue - this.dataMinValue);
		double y = (a * SingleBoxplotView.HEIGHT_FILL_FRACTION)
			* (this.verticalBoxplots ? super.getHeight() : super.getWidth());
		y += (1.0 - SingleBoxplotView.HEIGHT_FILL_FRACTION) * 0.5
			* (this.verticalBoxplots ? super.getHeight() : super.getWidth());

		if (!this.verticalBoxplots)
		{
			y = super.getWidth() - y;
		}
		return (int) Math.round(y);
	}

	/**
	 * Determine an appropriate scale
	 */
	private void determineScale()
	{
		int base = 1;
		int exp = (int) Math.log10(this.dataMaxValue - this.dataMinValue) - 1;
		this.step = (int) (base * Math.pow(10, exp));
		while ((this.dataMaxValue - this.dataMinValue) / this.step > 8)
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
			this.step = (int) (base * Math.pow(10, exp));
		}

		double min = this.dataMinValue
			- (1 - SingleBoxplotView.HEIGHT_FILL_FRACTION) * 0.5
			* (this.dataMaxValue - this.dataMinValue);
		this.max = this.dataMaxValue
			+ (1 - SingleBoxplotView.HEIGHT_FILL_FRACTION) * 0.5
			* (this.dataMaxValue - this.dataMinValue);
		this.firstMarker = Math.ceil(min / this.step) * this.step;

		// Math.ceil can give -0.0, this step turns that into 0.0
		if (firstMarker == 0)
		{
			firstMarker = 0.0;
		}
	}

	public void paintComponent(Graphics g)
	{
		g.clearRect(0, 0, super.getWidth(), super.getHeight());

		FontMetrics fm = g.getFontMetrics();
		Font normalFont = g.getFont();

		if (this.verticalBoxplots)
		{
			double p = this.firstMarker;
			while (p < max)
			{
				int y = this.valueToScreenLocation(p);
				g.drawLine(super.getWidth() - 5, y, super.getWidth(), y);
				
				String pString;
				String type;
				int columnIndex = boxplotView.getModel().getColumnIndex();
				type = boxplotView.getModel().getTableModel().getColumnTypes().get(columnIndex).toString();
				
//				System.out.println("BoxplotDependentAxis.paintComponent(): columnIndex = " + columnIndex 
//					+ ", type = " + type + ", AllowedTypes.INTEGER.toString()=" + AllowedTypes.INTEGER.toString());
				
//				if (Math.rint(p) == p) // is het getal gelijk met en zonder cijfers na de komma?
				// check of integer
				if (type.equals(AllowedTypes.INTEGER.toString()))
				{
					pString = String.valueOf((int) p);
				}
				else
				{
					pString = Double.toString(p);
				}
				
//				g.drawString(Double.toString(p),
//					super.getWidth() - 7 - fm.stringWidth(Double.toString(p)),
//					y + (int) (0.5 * fm.getHeight()));
				g.drawString(pString,
					super.getWidth() - 7 - fm.stringWidth(pString),
					y + (int) (0.5 * fm.getHeight()));

				p += step;
			}

			g.drawLine(super.getWidth() - 1, 0, super.getWidth() - 1,
				super.getHeight());

			AffineTransform at = new AffineTransform();
			at.rotate(Math.PI * 1.5);
			Font rotateFont = g.getFont().deriveFont(at);
			// geen bold voor variabelelabels
			//rotateFont = rotateFont.deriveFont(Font.BOLD);
			g.setFont(rotateFont);
//			g.drawString(this.columnName, fm.getHeight() + 2, super.getHeight()
//				/ 2 + fm.stringWidth(columnName));
			// iets meer naar de rand
			g.drawString(this.columnName, fm.getHeight()/2 + 4, super.getHeight()
				/ 2 + fm.stringWidth(columnName));
			g.setFont(normalFont);
		} // vertical boxes
		else
		{
			double p = this.firstMarker;
			while (p < max)
			{
				int x = this.valueToScreenLocation(p);
				g.drawLine(x, 0, x, 5);
				
				String pString;
				String type;
				int columnIndex = boxplotView.getModel().getColumnIndex();
				type = boxplotView.getModel().getTableModel().getColumnTypes().get(columnIndex).toString();
				
//				System.out.println("BoxplotDependentAxis.paintComponent(): columnIndex = " + columnIndex 
//					+ ", type = " + type);
				
//				if (Math.rint(p) == p)
				// check of integer
				if (type.equals(AllowedTypes.INTEGER.toString()))
				{
					pString = String.valueOf((int) p);
				}
				else
				{
					pString = Double.toString(p);
				}
				
//				g.drawString(Double.toString(p),
//					x - (int) (fm.stringWidth(Double.toString(p)) / 2),
//					7 + fm.getHeight());
				g.drawString(pString,
					x - (int) (fm.stringWidth(pString) / 2),
					7 + fm.getHeight());
				
				p += step;
			}

			g.drawLine(0, 0, super.getWidth(), 0);

			// geen bold voor variabelelabels
			// Font boldFont = normalFont.deriveFont(Font.BOLD);
			// g.setFont(boldFont);

			// label iets verder van de onderrand
			// g.drawString(this.columnName, super.getWidth()/2-fm.stringWidth(columnName), super.getHeight()-2);
			g.drawString(this.columnName,
				super.getWidth() / 2 - fm.stringWidth(columnName),
				super.getHeight() - 6);
		}

	}
}
