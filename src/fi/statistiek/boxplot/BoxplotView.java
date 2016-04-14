package fi.statistiek.boxplot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import fi.statistiek.DialogButton;

/**
 * MVC View for statistiekview Boxplot
 * 
 * @author ManuDrijvers, Sylvia van Borkulo
 * 
 */
public class BoxplotView extends JPanel implements Observer
{
	private BoxplotModel model;
	private BoxplotController controller;
	private BoxplotUserOptionsPanel userOptionsPanel;

	private JPanel mainPanel;
	private BoxplotDependentAxis dependentAxis;
	private BoxplotIndependentAxis independentAxis;
	private DialogButton dialogButton;

	public static final int KEEP_CLEAR_WIDTH = 50;

	/**
	 * Constructor
	 * 
	 * @param model
	 *            the model
	 * @param controller
	 *            the controller
	 */
	public BoxplotView(BoxplotModel model, BoxplotController controller)
	{
		super.setLayout(new BorderLayout());

		this.model = model;
		this.model.addObserver(this);
		this.controller = controller;

		userOptionsPanel = new BoxplotUserOptionsPanel(this, controller, model);
		dialogButton = userOptionsPanel.getDialogButton();
		super.add(dialogButton, BorderLayout.SOUTH);

		this.mainPanel = new JPanel();
		super.add(this.mainPanel, BorderLayout.CENTER);

		this.update(null, null);
	}

	/**
	 * Gets the string that is currently selected in the column box
	 * 
	 * @return
	 */
	public String getColumnBoxSelectedString()
	{
		return (String) this.userOptionsPanel.getColumnBoxSelectedString();
	}

	public boolean isTukeyBoxSelected()
	{
		return this.userOptionsPanel.isTukeyBoxSelected();
	}

	public boolean isVerticalBoxesButtonSelected()
	{
		return this.userOptionsPanel.isVerticalBoxesButtonSelected();
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
		return userOptionsPanel.getSplitMinBoundary();
	}

	public void setSplitMinBoundary(double d)
	{
		this.userOptionsPanel.setSplitMinBoundary(d);
	}

	public double getSplitBinWidth()
	{
		return userOptionsPanel.getSplitBinWidth();
	}

	public void setSplitBinWidth(double d)
	{
		this.userOptionsPanel.setSplitBinWidth(d);
	}

	/**
	 *Set the split bin width based on the model's split bin boundaries. 
	 */
	public void setSplitBinWidth()
	{
		this.userOptionsPanel.setSplitBinWidth();
	}

	public int getDependentAxisWidth()
	{
		if (this.dependentAxis == null)
		{
			return 0;
		}
		else
		{
			return this.dependentAxis.getWidth();
		}
	}

	public int getDependentAxisHeight()
	{
		if (this.dependentAxis == null)
		{
			return 0;
		}
		else
		{
			return this.dependentAxis.getHeight();
		}
	}

	public int getIndependentAxisWidth()
	{
		if (this.independentAxis == null)
		{
			return 0;
		}
		else
		{
			return this.independentAxis.getWidth();
		}
	}

	public Dimension getMainPanelSize()
	{
		return this.mainPanel.getSize();
	}

	public BoxplotModel getModel()
	{
		// Deze methode is nodig om type te kunnen opvragen van de te 
		// tekenen variabele in BoxplotDependentAxis
		return this.model;
	}
	
	public void setModel(BoxplotModel model)
	{
		this.model = model;
		this.model.addObserver(this);
		userOptionsPanel.setModel(model);
		this.update(null, null);
	}

	public void update(Observable arg0, Object arg1)
	{
		// nodig?
		model.setPercentileValues();
		
		this.dialogButton.setVisible(this.model.getStatTableModel()
			.isViewsEditable());

		// check for empty data set
		if (!this.model.isEmptyBoxplot())
		{

			userOptionsPanel.update();

			this.mainPanel.removeAll();
			this.mainPanel.setBackground(Color.WHITE);
			if (this.model.getStatTableModel().isColumnIndexValid(
				this.model.getColumnIndex()))
			{
				if (!this.model.getStatTableModel().isColumnIndexValid(
					this.model.getColumnSplitIndex()))
				{
					// geen split

					SingleBoxplotView v = new SingleBoxplotView(
						this.model.isTukeyBox() ? this.model.getOutlierStrongMinValue(0) : null,
						this.model.isTukeyBox() ? this.model.getOutlierWeakMinValue(0) : null,
						this.model.getOutlierMinValue(0),
						this.model.getMinValue(0),
						this.model.getLowerQuartile(0),
						this.model.getMedian(0),
						this.model.getUpperQuartile(0),
						this.model.getMaxValue(0),
						this.model.getOutlierMaxValue(0),
						this.model.isTukeyBox() ? this.model.getOutlierWeakMaxValue(0) : null,
						this.model.isTukeyBox() ? this.model.getOutlierStrongMaxValue(0) : null,
						this.model.getDataMinValue(),
						this.model.getDataMaxValue(),
						this.model.isVerticalBoxplots(),
						this.model.isTukeyBox(),
						this.model.isEmptyBoxplot());
					
					if (this.model.isTukeyBox() 
						&& !this.getModel().isEmptyBoxplot() 
						&& v.isDrawable())
					{
						v.initializeOutlierHighlightValues();
					}

					JPanel boxplotsPanel = new JPanel(new GridLayout(1, 1));
					boxplotsPanel.setBackground(Color.WHITE);
					this.mainPanel.setLayout(new BorderLayout());
					boxplotsPanel.add(v);

					this.dependentAxis = new BoxplotDependentAxis(
						this.model.getDataMinValue(),
						this.model.getDataMaxValue(),
						this.model.isVerticalBoxplots(), this, this.model
							.getStatTableModel().getColumnName(
								this.model.getColumnIndex()));

					if (this.model.isVerticalBoxplots())
					{
						JPanel eastPanel = new JPanel()
						{
							public Dimension getPreferredSize()
							{
								return new Dimension(
									BoxplotView.KEEP_CLEAR_WIDTH, 0);
							};
						};
						eastPanel.setBackground(Color.WHITE);
						this.mainPanel.add(eastPanel, BorderLayout.EAST);
						this.mainPanel.add(this.dependentAxis,
							BorderLayout.WEST);

						this.mainPanel.add(boxplotsPanel, BorderLayout.CENTER);
						this.mainPanel.setBounds(this.mainPanel.getBounds());
					} // vertical boxplot
					else
					{
						// horizontal boxplot
						
						JPanel centerPanel = new JPanel(new BorderLayout());
						JPanel northPanel = new JPanel()
						{
							public Dimension getPreferredSize()
							{
								return new Dimension(0,
									BoxplotView.KEEP_CLEAR_WIDTH);
							};
						};
						northPanel.setBackground(Color.WHITE);
						centerPanel.add(northPanel, BorderLayout.NORTH);
						centerPanel.add(this.dependentAxis, BorderLayout.SOUTH);
						centerPanel.setBackground(Color.WHITE);
						centerPanel.add(boxplotsPanel, BorderLayout.CENTER);
						this.mainPanel.add(centerPanel, BorderLayout.CENTER);
						this.mainPanel.setBounds(this.mainPanel.getBounds());
					}
				}
				else
				{ 
					// er is een split
					
					int splitClasses = this.model.getNumberOfSplitClasses();

					this.dependentAxis = new BoxplotDependentAxis(
						this.model.getDataMinValue(),
						this.model.getDataMaxValue(),
						this.model.isVerticalBoxplots(), this, this.model
							.getStatTableModel().getColumnName(
								this.model.getColumnIndex()));

					this.independentAxis = new BoxplotIndependentAxis(
						this.model, this, this.model.isVerticalBoxplots(),
						this.model.getStatTableModel().getColumnName(
							this.model.getColumnSplitIndex()));

					if (this.model.isVerticalBoxplots())
					{
						JPanel boxplotsPanel = new JPanel(new GridLayout(1,
							splitClasses));
						boxplotsPanel.setBackground(Color.WHITE);
						this.mainPanel.add(this.dependentAxis,
							BorderLayout.WEST);
						
						for (int i = 0; i < splitClasses; i++)
						{
							SingleBoxplotView v = new SingleBoxplotView(
								this.model.isTukeyBox() ? this.model.getOutlierStrongMinValue(i) : null,
								this.model.isTukeyBox() ? this.model.getOutlierWeakMinValue(i) : null,
								this.model.getOutlierMinValue(i),
								this.model.getMinValue(i),
								this.model.getLowerQuartile(i),
								this.model.getMedian(i),
								this.model.getUpperQuartile(i),
								this.model.getMaxValue(i),
								this.model.getOutlierMaxValue(i),
								this.model.isTukeyBox() ? this.model.getOutlierWeakMaxValue(i) : null,
								this.model.isTukeyBox() ? this.model.getOutlierStrongMaxValue(i) : null,
								this.model.getDataMinValue(),
								this.model.getDataMaxValue(),
								this.model.isVerticalBoxplots(),
								this.model.isTukeyBox(),
								this.model.isEmptyBoxplot());
							
							if (this.model.isTukeyBox() 
								&& !this.getModel().isEmptyBoxplot()
								&& v.isDrawable())
							{
								v.initializeOutlierHighlightValues();
							}

							boxplotsPanel.add(v);

						}
						JPanel eastPanel = new JPanel()
						{
							public Dimension getPreferredSize()
							{
								return new Dimension(
									BoxplotView.KEEP_CLEAR_WIDTH, 0);
							};
						};
						eastPanel.setBackground(Color.WHITE);
						this.mainPanel.add(eastPanel, BorderLayout.EAST);

						this.mainPanel.add(boxplotsPanel, BorderLayout.CENTER);
						this.mainPanel.add(this.independentAxis,
							BorderLayout.SOUTH);
						this.mainPanel.setBounds(this.mainPanel.getBounds());
					} // vertical boxplot
					else
					{
						// horizontal boxplot
						
						JPanel boxplotsPanel = new JPanel(new GridLayout(
							splitClasses, 1));
						boxplotsPanel.setBackground(Color.WHITE);
						this.mainPanel.setLayout(new BorderLayout());
						JPanel centerPanel = new JPanel(new BorderLayout());
						centerPanel.add(this.dependentAxis, BorderLayout.SOUTH);
						
						for (int i = splitClasses - 1; i >= 0; i--)
						{
							SingleBoxplotView v = new SingleBoxplotView(
								this.model.isTukeyBox() ? this.model.getOutlierStrongMinValue(i) : null,
								this.model.isTukeyBox() ? this.model.getOutlierWeakMinValue(i) : null,
								this.model.getOutlierMinValue(i),
								this.model.getMinValue(i),
								this.model.getLowerQuartile(i),
								this.model.getMedian(i),
								this.model.getUpperQuartile(i),
								this.model.getMaxValue(i),
								this.model.getOutlierMaxValue(i),
								this.model.isTukeyBox() ? this.model.getOutlierWeakMaxValue(i) : null,
								this.model.isTukeyBox() ? this.model.getOutlierStrongMaxValue(i) : null,
								this.model.getDataMinValue(),
								this.model.getDataMaxValue(),
								this.model.isVerticalBoxplots(),
								this.model.isTukeyBox(),
								this.model.isEmptyBoxplot());
							
							if (this.model.isTukeyBox() 
								&& !this.getModel().isEmptyBoxplot()
								&& v.isDrawable())
							{
								v.initializeOutlierHighlightValues();
							}

							boxplotsPanel.add(v);
						}
						JPanel northPanel = new JPanel()
						{
							public Dimension getPreferredSize()
							{
								return new Dimension(0,
									BoxplotView.KEEP_CLEAR_WIDTH);
							};
						};
						northPanel.setBackground(Color.WHITE);
						centerPanel.add(northPanel, BorderLayout.NORTH);

						centerPanel.add(boxplotsPanel, BorderLayout.CENTER);
						this.mainPanel.add(centerPanel, BorderLayout.CENTER);
						this.mainPanel.add(this.independentAxis,
							BorderLayout.WEST);
						this.mainPanel.setBounds(this.mainPanel.getBounds());
					} // horizontal boxplot
					
				} // er is een split
				
			} // column index valid
			else
			{
				// column index is not valid, so nothing to draw
				this.mainPanel.removeAll();
			}

			// Wijzigingen van BoxplotUserOptionsPanel zichtbaar maken in BoxplotView
			this.mainPanel.revalidate();
			
			this.repaint();
		} // non empty dataset
		else
		{
		// empty dataset
			this.mainPanel.removeAll();
			userOptionsPanel.update();			
		}
	}
}
