package fi.statistiek.frequencytable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import fi.statistiek.ColorGenerator;
import fi.statistiek.ColorPreviewer;
import fi.statistiek.Statistiek;
import fi.statistiek.histogram.HistogramModel.FrequencyTuple;
import fi.statistiek.types.AllowedTypes;
import fi.statistiek.types.ColumnType;

/**
 * MVC View for statistiekView FrequencyTable
 * 
 * @author Manu Drijvers
 * 
 */
public class FrequencyTableView extends JPanel implements Observer
{
	private FrequencyTableModel model;
	private FrequencyTableController controller;
	private FrequencyTableUserOptionsPanel userOptionsPanel;
	private JButton dialogButton;

	private JPanel mainPanel;
	private JScrollPane scrollPane;
	private FrequencyTablePanel[] splitClassPanels;
	private int numberOfSplitClasses = 1;

	private int mainPanelRows;
	private int mainPanelColumns;
	private int minRowHeight = 30;
	private int splitClassLabelPanelHeight = 60; 

	private Color[][] rowColors;
	private int maxRows = 100;
	public static final int MAIN_GRID_HGAP = 8;
	public static final int MAIN_GRID_VGAP = 8;
	public static final Color SELECTED_COLOR = ColorGenerator.SELECTION_COLOR;

	/**
	 * Constructor
	 * 
	 * @param model
	 *            MVC Model
	 * @param controller
	 *            MVC Controller
	 */
	public FrequencyTableView(FrequencyTableModel model,
		FrequencyTableController controller)
	{
		super(new BorderLayout());
		super.setBackground(Color.WHITE);

		this.model = model;
		this.model.addObserver(this);
		this.controller = controller;

		this.mainPanel = new JPanel();
		
		this.scrollPane = new JScrollPane();
		this.scrollPane.setViewportView(mainPanel);
		this.scrollPane.getVerticalScrollBar().setUnitIncrement(Statistiek.scrollSpeedUnit);
		
		super.add(this.scrollPane, BorderLayout.CENTER);

		this.mainPanel.setBackground(Color.WHITE);
		this.mainPanel.addMouseListener(new RowClickListener());

		this.userOptionsPanel = new FrequencyTableUserOptionsPanel(this, controller, model);

		dialogButton = userOptionsPanel.getDialogButton();

		super.add(dialogButton, BorderLayout.SOUTH);

		this.update(null, null);
	}

	private class FrequencyTablePanel extends JPanel
	{
		private int splitClass;
		
		public FrequencyTablePanel(LayoutManager l, int splitClass)
		{
			super(l);
			super.setBackground(Color.WHITE);
			this.splitClass = splitClass;
		}
		
		@Override
		public Dimension getPreferredSize()
		{
//			System.out.println("FrequencyTableView.FrequencyTablePanel.getPreferredSize(): h = "
//				+ super.getPreferredSize().height + ", w = "
//				+ super.getPreferredSize().width);
			
			return super.getPreferredSize();
		}
		
		@Override
		public void setPreferredSize(Dimension d)
		{
//			System.out.println("FrequencyTableView.FrequencyTablePanel.setPreferredSize(): d = "
//				+ d);
			
			super.setPreferredSize(d);
		}		
		
		public void paintComponent(Graphics g)
		{
			if (!FrequencyTableView.this.model.columnIndexValid())
			{
				// variable is not valid, so there is nothing to paint
				return;
			}
			else if (FrequencyTableView.this.mainPanelRows > FrequencyTableView.this.maxRows)
			{
				return;
			}

//			System.out.println("FrequencyTableView.FrequencyTablePanel.paintComponent(): rows = " + FrequencyTableView.this.mainPanelRows);

			g.setColor(Color.BLACK);

			double columnWidth = this.getWidth()
				/ (double) FrequencyTableView.this.mainPanelColumns;
			int rowAreaHeight = this.getHeight()
				- (FrequencyTableView.this.mainPanelRows + 2)
				* FrequencyTableView.MAIN_GRID_HGAP;

			double rowHeight = (rowAreaHeight / (double) (FrequencyTableView.this.mainPanelRows + 2))
				+ MAIN_GRID_HGAP;
			int roundRowHeight = (int) rowHeight;

			g.setColor(Color.WHITE);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());

			// draw selection color of rows
			if (FrequencyTableView.this.rowColors != null)
			{
				for (int i = 0; i < FrequencyTableView.this.rowColors.length; i++)
				{
					g.setColor(FrequencyTableView.this.rowColors[i][this.splitClass]);
					g.fillRect(
						0,
						(int) ((i + 1) * rowHeight),
						this.getWidth(), roundRowHeight);
				}
			}

			// draw vertical lines
			// first line black
			g.setColor(Color.BLACK);
			if (FrequencyTableView.this.mainPanelColumns > 0)
			{
				int x = (int) (columnWidth);
				g.drawLine(x, 0, x, this.getHeight());
			}
			
			// other lines grey
			g.setColor(ColorGenerator.getGreyLineColor());
			for (int i = 2; i < FrequencyTableView.this.mainPanelColumns; i++)
			{
				int x = (int) (columnWidth * i);
				g.drawLine(x, 0, x, this.getHeight());
			}

			// draw horizontal lines
			g.setColor(Color.BLACK);
			// lijn boven t.b.v. meerdere tabellen bij split
			g.drawLine(0, 0, this.getWidth(), 0);

			// lijn na eerste header-rij
			g.drawLine(0, roundRowHeight, this.getWidth(), roundRowHeight);

			// lijn boven totaalrij
			int y = (int) ((FrequencyTableView.this.mainPanelRows + 1)
					* rowHeight);
			g.drawLine(0, y, this.getWidth(), y);
			// lijn onder t.b.v. meerdere tabellen bij split
//			System.out.println("FrequencyTableView.FrequencyTablePanel.paintComponent(): h = " + this.getHeight()
//				+ ", w = " + this.getWidth());
			g.setColor(ColorGenerator.getGreyLineColor());
			g.drawLine(0, this.getHeight() - 1, this.getWidth(), this.getHeight() - 1);
		}
	}
	
	public double getBinWidth()
	{
		return this.userOptionsPanel.getBinWidth();
	}

	public double getMinBoundary()
	{
		return this.userOptionsPanel.getMinBoundary();
	}
	
//	public JTextField getNoBinsField()
//	{
//		return this.noBinsField;
//	}
//
//	public String getNoBinsFieldText()
//	{
//		return this.noBinsField.getText();
//	}
//
	public boolean isShowPercBoxSelected()
	{
		return this.userOptionsPanel.isShowPercBoxSelected();
	}

	public boolean isShowCumulativeBoxSelected()
	{
		return this.userOptionsPanel.isShowCumulativeBoxSelected();
	}

	public int varBoxSelectedIndex()
	{
		return this.userOptionsPanel.getVarXBoxSelectedIndex();
	}

	/**
	 * @return The sum of all elements on even indices
	 */
	private int arrayEvenSum(int[] array)
	{
		int sum = 0;
		for (int i = 0; i < array.length; i += 2)
		{
			sum += array[i];
		}
		return sum;
	}

	private int tupleArraySum(FrequencyTuple[] array)
	{
		int sum = 0;
		for (FrequencyTuple ft : array)
		{
			sum += ft.frequency;
		}
		return sum;
	}

	public void update(Observable arg0, Object arg1)
	{
		try
		{
	//		this.noBinsField.setText(Integer.toString(this.model.getBinBoundaries()
	//			.size() - 1));
	//
			// this.userOptionsPanel.setVisible(this.model.getTableModel().isViewsEditable());
			this.dialogButton.setVisible(this.model.getTableModel()
				.isViewsEditable());
			
			this.mainPanel.removeAll();
			if (this.model.columnIndexValid())
			{
				ColumnType cType = this.model.getTableModel().getColumnTypes()
					.get(this.model.getColumnIndex());
				AllowedTypes type = cType.getType();
	
				this.mainPanelColumns = 2;
				if (this.model.isShowCumulative())
				{
					this.mainPanelColumns++;
				}
				if (this.model.isShowPercentage())
				{
					this.mainPanelColumns++;
					if (this.model.isShowCumulative())
					{
						this.mainPanelColumns++;
					}
				}
	
				FrequencyTuple[][] frequencyTuple = null;
				int[] frequencies = null;
				numberOfSplitClasses = this.model.getTableModel().splitVarClasses(
					this.model.getSplitOptions());
				splitClassPanels = new FrequencyTablePanel[numberOfSplitClasses];
				
				if (type.isNumber())
				{
					this.mainPanelRows = this.model.getBinBoundaries().size() - 1;
					if (this.mainPanelRows < 0)
					{
						this.mainPanelRows = 0;
					}
					
					this.rowColors = new Color[this.model.numberClassFrequency()[0].length / 2][numberOfSplitClasses];
				}
				else
				{
					frequencyTuple = this.model.enumClassFrequency();
					this.mainPanelRows = frequencyTuple[0].length;
					
					this.rowColors = new Color[this.mainPanelRows][numberOfSplitClasses];
				}
				
				//System.out.println("FrequencyTableView.update(): rows = " + this.mainPanelRows);
				if (this.mainPanelRows > this.maxRows)
				{
					String s = Statistiek.rb.getString("messageNrRowsMoreThan") + this.maxRows
						+ ". " + Statistiek.rb.getString("messageChooseOtherVar");
					JLabel message = new JLabel(s);
					message.setFont(Statistiek.font);
					this.mainPanel.add(message);
					userOptionsPanel.update();
					this.mainPanel.revalidate();
					this.repaint();
					return;
				}
	
				// set gridlayout, +2 rows for header and sum rows
				GridLayout gl = new GridLayout(this.mainPanelRows + 2,
					this.mainPanelColumns);
				gl.setHgap(FrequencyTableView.MAIN_GRID_HGAP);
				gl.setVgap(FrequencyTableView.MAIN_GRID_VGAP);
				//System.out.println("FrequencyTableView.update(): gl = " + gl);
				this.mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
				
				Dimension dimension;
	//			int noBins = this.model.getNoBins();
				
				for (int i = 0; i < numberOfSplitClasses; i++)
				{
					splitClassPanels[i] = new FrequencyTablePanel(gl, i);
					
	//				System.out.println("FrequencyTableView.update(): noBins = "
	//					+ noBins + ", scrollPane.h = " + this.scrollPane.getHeight()
	//					+ ", scrollPane.viewPort.h = " 
	//					+ this.scrollPane.getViewport().getHeight() 
	//					+ ", scrollPane.w = " + this.scrollPane.getViewport().getWidth());
					
					int h = this.scrollPane.getViewport().getHeight();
					int w = this.scrollPane.getViewport().getWidth();
					if (h <= 0) // for some reason scrollPane is 0 sometimes...
						h = 340; // hardcoded...
					if (w <= 0)
						w = 653;
					
		    		if (this.mainPanelRows * minRowHeight > h)
		    		{
		    			dimension = new Dimension(w, this.mainPanelRows * minRowHeight);
		    		}
		    		else
		    		{
		    			dimension = new Dimension(w, h);
		    		}
	
	//				System.out.println("... FrequencyTableView.update(): splitClassPanel.setPreferredSize(h = " 
	//					+ dimension.height + ", w = " + dimension.width + ")");
	
					splitClassPanels[i].setPreferredSize(dimension);
					
					makeHeaderRow(splitClassPanels[i]);
					
					// make middle part
					int sum;
					if (type.isNumber())
					{
						frequencies = this.model.numberClassFrequency()[i];
						sum = this.arrayEvenSum(frequencies);
					}
					else
					{
						sum = this.tupleArraySum(frequencyTuple[i]);
	
					}
	
					if (frequencyTuple == null)
						makeMiddlePart(splitClassPanels[i], type, frequencies, null, sum);				
					else
						makeMiddlePart(splitClassPanels[i], type, frequencies, frequencyTuple[i], sum);
					
					makeTotalRow(splitClassPanels[i], sum);
	
					// set colors for background
					if (type.isNumber())
					{
						for (int j = 0; j < frequencies.length; j += 2)
						{
							double d = (frequencies[j] == 0 ? 0.0
								: (double) frequencies[j + 1] / (double) frequencies[j]);
	//						System.out.println("FrequencyTableView.update(): " + frequencies[i + 1] + " "
	//							+ frequencies[i] + " " + d);
							this.rowColors[j / 2][i] = ColorPreviewer.mixColors(
								Color.WHITE, SELECTED_COLOR, d);
						}
					}
					else
					{
						for (int k = 0; k < frequencyTuple[i].length; k++)
						{
							FrequencyTuple ft = frequencyTuple[i][k];
							double d = (ft.frequency == 0 ? 0
								: (double) ft.selectionFrequency
									/ (double) ft.frequency);
	//						System.out.println("FrequencyTableView.update(): " + d);
							this.rowColors[k][i] = ColorPreviewer.mixColors(Color.WHITE,
								SELECTED_COLOR, d);
						}
					}
	
					this.mainPanel.add(splitClassPanels[i]);
					
					if (numberOfSplitClasses > 1)
					{
						// add extra panel with splitclass label
						JPanel labelPanel = new JPanel();
						labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
						labelPanel.setPreferredSize(new Dimension(
							this.mainPanel.getWidth(), this.splitClassLabelPanelHeight));
						labelPanel.setBackground(Color.WHITE);
						String splitVar = this.model.getTableModel()
							.getColumnName(this.model.getSplitOptions().getColumnSplitIndex());
						JLabel label = new JLabel(splitVar + ": " + this.model.getSplitOptions()
							.getSplitClassLabel(i, this.model.getTableModel()));
						label.setFont(Statistiek.font);	
						labelPanel.add(label);
						this.mainPanel.add(labelPanel);
					}
				}
	
				this.mainPanel.setBackground(Color.WHITE);
				
			} //columnIndexValid()
			
			userOptionsPanel.update();
	
			// set mainPanel size op het eind zodat splitClassPanels bestaan
			this.setMainPanelSize();
	
			this.mainPanel.revalidate();
			
			this.repaint();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void makeTotalRow(JPanel panel, int sum)
	{
		//System.out.println("FrequencyTableView.makeTotalRow(sum=" + sum + ")");
		
		JLabel totalLabel = new JLabel(Statistiek.rb.getString("totalLabel"));
		totalLabel.setFont(Statistiek.font);
		panel.add(totalLabel);
		
		JLabel freqLabel = new JLabel(Integer.toString(sum),
			SwingConstants.TRAILING);
		freqLabel.setFont(Statistiek.font);
		panel.add(freqLabel);
    	
    	if (this.model.isShowPercentage())
    	{
    		String percString;
    		if (sum == 0) // if there are no cases in the table, then total percentage is 0%
    			percString = "0%";
    		else
    			percString = "100%";
    		JLabel percLabel = new JLabel(percString, SwingConstants.TRAILING);
    		percLabel.setFont(Statistiek.font);
    		panel.add(percLabel);
    	}
    	
    	if (this.model.isShowCumulative())
    	{
    		JLabel cumulLabel = new JLabel(Integer.toString(sum),
    			SwingConstants.TRAILING);
    		cumulLabel.setFont(Statistiek.font);
    		panel.add(cumulLabel);
    	}
    	
    	if (this.model.isShowPercentage() && this.model.isShowCumulative())
    	{
    		String percString;
    		if (sum == 0) // if there are no cases in the table, then total percentage is 0%
    			percString = "0%";
    		else
    			percString = "100%";
    		JLabel cumulPercLabel = new JLabel(percString, SwingConstants.TRAILING);
    		cumulPercLabel.setFont(Statistiek.font);
    		panel.add(cumulPercLabel);
    	}		
	}

	/**
	 * Make the header row on panel.
	 * @param panel
	 */
	private void makeHeaderRow(JPanel panel)
	{
		JLabel variableName = new JLabel(this.model.getTableModel()
			.getColumnName(this.model.getColumnIndex()));
		variableName.setFont(Statistiek.font);
		// test syl: toon gridlines m.b.v. lineborders
//		variableName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.add(variableName);
		
//		System.out.println("FrequencyTableView.makeHeaderRow(panel): panel.getLayout() = " 
//			+ panel.getLayout() + ", variableName.getPreferredSize() = " + variableName.getPreferredSize());
		
		JLabel freq = new JLabel("Freq.", SwingConstants.TRAILING);
		freq.setFont(Statistiek.font);
		panel.add(freq);

		if (this.model.isShowPercentage())
		{
			JLabel freqPerc = new JLabel("Freq.%", SwingConstants.TRAILING);
			freqPerc.setFont(Statistiek.font);
			panel.add(freqPerc);
		}
		if (this.model.isShowCumulative())
		{
			JLabel cumul = new JLabel("Cumul.", SwingConstants.TRAILING);
			cumul.setFont(Statistiek.font);
			panel.add(cumul);
    	}
    	
    	if (this.model.isShowPercentage() && this.model.isShowCumulative())
    	{
			JLabel cumulPerc = new JLabel("Cumul.%",
				SwingConstants.TRAILING);
			cumulPerc.setFont(Statistiek.font);
			panel.add(cumulPerc);
		}
	}

	/**
	 * Make the middle part of the frequency table view on panel.
	 * @param panel
	 * @param type
	 * @param frequencies
	 * @param frequencyTuple
	 * @param sum
	 */
	private void makeMiddlePart(JPanel panel, AllowedTypes type, int[] frequencies, 
		FrequencyTuple[] frequencyTuple, double sum)
	{
		int cumulative = 0;
		int freq;
		
		//System.out.println("FrequencyTableView.makeMiddlePart(): mainPanelRows = " + this.mainPanelRows);
		
		for (int bin = 0; bin < this.mainPanelRows; bin++)
		{
			if (type.isNumber())
			{
				freq = frequencies[bin * 2];
				JLabel label = new JLabel(this.model.getBinBoundaries()
					.get(bin).toString()
					+ " - "
					+ this.model.getBinBoundaries().get(bin + 1).toString());
				label.setFont(Statistiek.font);
				panel.add(label);
			}
			else
			{
				freq = frequencyTuple[bin].frequency;
				JLabel label = new JLabel(frequencyTuple[bin].label);
				label.setFont(Statistiek.font);
				panel.add(label);
			}

			JLabel freqLabel = new JLabel(Integer.toString(freq),
				SwingConstants.TRAILING);
			freqLabel.setFont(Statistiek.font);
			panel.add(freqLabel);

			if (this.model.isShowPercentage())
			{
				double d = freq * 100 / (double) sum;
				d = Math.round(d * 100) / (double) 100;
				JLabel percLabel = new JLabel(getStringValue(d) + "%",
					SwingConstants.TRAILING);
				percLabel.setFont(Statistiek.font);
				panel.add(percLabel);
			}
			if (this.model.isShowCumulative())
			{
				cumulative += freq;
				JLabel cumulLabel = new JLabel(Integer.toString(cumulative),
					SwingConstants.TRAILING);
				cumulLabel.setFont(Statistiek.font);
				panel.add(cumulLabel);
	    	}
	    	
	    	if (this.model.isShowPercentage() && this.model.isShowCumulative())
	    	{
				double d = (double) cumulative * 100 / (double) sum;
				d = Math.round(d * 100) / (double) 100;
				JLabel cumulPercLabel = new JLabel(getStringValue(d) + "%",
					SwingConstants.TRAILING);
				cumulPercLabel.setFont(Statistiek.font);
				panel.add(cumulPercLabel);
			}
		}
	}

	/**
	 * Get the string value of waarde. If waarde is an integer
	 * then a string without decimals is returned. 
	 * @param waarde
	 * @return
	 */
	private String getStringValue(double waarde)
	{
		String waardeString;
		
		// Test of waarde een integer is 
		if ((waarde == Math.floor(waarde)) && !Double.isInfinite(waarde))
		{
			// als integer, dan zonder decimalen
			waardeString = String.valueOf((int) waarde);
		}
		else
		{
			waardeString = String.valueOf(waarde);
		}
		
		return waardeString;
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// clear panel
		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		if (!this.model.columnIndexValid())
		{
			// variable is not valid, so there is nothing to paint
			return;
		}
	}
	
	private void setMainPanelSize()
	{
		int noBins;
		
		if (this.model.columnIndexValid())
		{
			ColumnType cType = this.model.getTableModel().getColumnTypes()
				.get(this.model.getColumnIndex());
			AllowedTypes type = cType.getType();
			if (type.isNumber())
			{
				noBins = this.model.getNoBins();
			}
		} 
		
		noBins = this.mainPanelRows;
			
		Dimension dimension;
		
//		System.out.println("frequencyTableView.setMainPanelSize(): numberOfSplitClasses = "
//			+ numberOfSplitClasses);
		
		if (this.model.getSplitOptions().getColumnSplitIndex() == -1)
		{
    		if (noBins * minRowHeight > this.scrollPane.getHeight())
    		{
    			dimension = new Dimension(this.scrollPane.getViewport()
    				.getWidth(), noBins * minRowHeight);
    		}
    		else
    		{
    			dimension = new Dimension(
    				this.scrollPane.getViewport().getWidth(), 
    				this.scrollPane.getViewport().getHeight());
    		}
		}
		else // er is een split
		{
			int correctieBreedte = 20;
			int correctieHoogte = 20;

			if (noBins * minRowHeight > this.scrollPane.getHeight())
    		{
    			dimension = new Dimension(this.scrollPane
    				.getWidth() - correctieBreedte, numberOfSplitClasses
    				* (noBins * minRowHeight) + this.splitClassLabelPanelHeight
    				- correctieHoogte);
    		}
    		else
    		{
    			dimension = new Dimension(this.scrollPane
    				.getWidth() - correctieBreedte, numberOfSplitClasses
    				* (this.scrollPane.getHeight() - 5) + this.splitClassLabelPanelHeight
    				- correctieHoogte);
    		}
		}
		
		// Vreemde situatie: scrollPane = 0x0 als we na een paginawissel direct een frequentietabel tonen
		if (scrollPane.getHeight() == 0 || scrollPane.getWidth() == 0)
		{
//			System.out.println("....... scrollPane.getViewPort().setPreferredSize(671, 343)!");
//			scrollPane.getViewport().setPreferredSize(new Dimension(671, 343));
//			scrollPane.setPreferredSize(new Dimension(671, 343));
			// even hardcoded op iets groots...
			int h;
			int h_singlePanel = 340;
			int w = 651;
			if (splitClassPanels != null)
			{
				// test syl: de hoogte hardcoded zetten, want splitClassPanels hebben hoogte 0...
				h = h_singlePanel * splitClassPanels.length;
//				System.out.println("FrequencyTableView.setMainPanelSize(): --------- RESET! h = " 
//					+ h + ", splitClassPanels.length = " + splitClassPanels.length
//					+ ", splitClassPanels[0].getHeight() = " + splitClassPanels[0].getHeight());
			}
			else
			{
				h = h_singlePanel;
//				System.out.println("FrequencyTableView.setMainPanelSize(): --------- RESET! h = " 
//					+ h);
			}
			dimension.setSize(w, h);
		}

//		System.out.println("FrequencyTableView.setMainPanelSize(): mainPanel.setPreferredSize(h = " 
//			+ dimension.height	+ ", w = " + dimension.width + ")");
//		System.out.println("FrequencyTableView.setMainPanelSize(): scrollPane.h = " 
//			+ scrollPane.getHeight()
//			+ ", scrollPane.w = " + scrollPane.getWidth());
		
		this.mainPanel.setPreferredSize(dimension);
	}
	
	// Override setBound
	public void setBounds(int x, int y, int w, int h)
	{
//		System.out.println("FrequencyTableView.setBounds(x=" + x + ", y=" + y 
//			+ ", w=" + w + ", h=" + h + ")");

		super.setBounds(x, y, w, h);

		this.setMainPanelSize();
		
		this.scrollPane.setViewportView(mainPanel);

		// System.out.println("HistogramView.setBounds(): Size histogram: " +
		// this.getBounds().toString()
		// + ", scrollbarVisible=" +
		// scrollPane.getVerticalScrollBar().isVisible());
		
//		 System.out.println("HistogramView.setBounds(): scrollPane w="
//			 + scrollPane.getWidth()
//			 + ", h=" + scrollPane.getHeight());
	}

	public void setModel(FrequencyTableModel model)
	{
		this.model = model;
		this.model.addObserver(this);
		userOptionsPanel.setModel(model);
		this.update(null, null);
	}

	public int getSplitVarBoxSelectedIndex()
	{
		return userOptionsPanel.getSplitVarBoxSelectedIndex();
	}

	public int getSplitBinsBoxSelectedInt()
	{
		return userOptionsPanel.getSplitBinsBoxSelectedInt();
	}


	
	/*
	 * RowClickListener is used on mainPanel which may contain several splitClassPanels
	 * in case of a split.
	 */
	private class RowClickListener implements MouseListener
	{

		public void mouseClicked(MouseEvent arg0)
		{
			if (FrequencyTableView.this.splitClassPanels[0] != null)
			{
				int y = arg0.getPoint().y;
				int y_transformed = y;
				int heightSplitClassPanel = FrequencyTableView.this.splitClassPanels[0].getHeight();
				int heightLabelPanel = FrequencyTableView.this.splitClassLabelPanelHeight;
				// total height of a split class frequency table
				int heightSplitFrequencyTable = heightSplitClassPanel + heightLabelPanel;
				// number of data rows in a frequency table
				int numberOfRows = FrequencyTableView.this.mainPanelRows;
				
				int count = 0;
				// transform y
				while (y_transformed > heightSplitFrequencyTable)
				{
					y_transformed = y_transformed - heightSplitFrequencyTable;
					count++;
	//				System.out.println("FrequencyTableView.RowClickListener.mouseClicked(): y_transformed = "
	//					+ y_transformed + ", count = " + count);
				}
				
				double rowHeight = (heightSplitClassPanel / (double) (numberOfRows + 2));
				// int roundRowHeight = (int)Math.round(rowHeight);
				int roundRowHeight = (int) rowHeight;
	
				int clicked = (y_transformed / roundRowHeight) - 1;
	
	//			System.out.println("FrequencyTableView.RowClickListener.mouseClicked(): y = "
	//				+ y + ", y_transformed = " + y_transformed + ", count = " + count
	//				+ ", heightSplitClassPanel = " + heightSplitClassPanel
	//				+ ", rowHeight = " + rowHeight
	//				+ ", roundRowHeight = " + roundRowHeight
	//				+ ", clicked = " + clicked);			
	
				if (clicked >= 0 && clicked < FrequencyTableView.this.mainPanelRows)
				{
					int clicked_transformed = clicked + (count * numberOfRows);
					this.rowClicked(clicked_transformed);
				}
			} // splitClassPanels != null
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
			// TODO Auto-generated method stub

		}

		public void mouseReleased(MouseEvent arg0)
		{
			// TODO Auto-generated method stub

		}

		/*
		 * The user clicked on row rowNumber. When there is a split, rowNumber has a maximum
		 * of numberOfBins * numberOfSplitClasses.
		 */
		private void rowClicked(int rowNumber)
		{
			if (!FrequencyTableView.this.model.columnIndexValid())
			{
				return;
			}

			int bins = FrequencyTableView.this.model.getTableModel()
				.splitVarClasses(FrequencyTableView.this.model.getColumnIndex(),
					FrequencyTableView.this.model.getBinBoundaries());
			int row = rowNumber % bins;
			int splitClass = rowNumber / bins;

//			System.out.println("FrequencyTableView.RowClickListener.rowClicked(): rowNumber = " 
//				+ rowNumber + ", row " + row + " in splitClass " + splitClass);

			ColumnType cType = FrequencyTableView.this.model.getTableModel()
				.getColumnTypes()
				.get(FrequencyTableView.this.model.getColumnIndex());
			AllowedTypes type = cType.getType();
			if (type.isNumber())
			{
				ArrayList<Boolean> selectionList = new ArrayList<Boolean>(
					FrequencyTableView.this.model.getTableModel().getRowCount());
				for (int i = 0; i < FrequencyTableView.this.model
					.getTableModel().getRowCount(); i++)
				{
					Object o = FrequencyTableView.this.model.getTableModel()
						.getValueAt(i,
							FrequencyTableView.this.model.getColumnIndex());

					selectionList.add(!o.equals(ColumnType.WILDCARD)
						&& FrequencyTableView.this.model.binOfNumber(Double
							.parseDouble((String) o)) == row
						&& FrequencyTableView.this.model.getTableModel()
							.classifyObject(i,
								FrequencyTableView.this.model.getSplitOptions()) == splitClass);

				}

				FrequencyTableView.this.model.getTableModel().setSelectionList(
					selectionList);
			}
			else
			{
				String clicked;
				if (type.equals(AllowedTypes.ENUM))
				{
					clicked = cType.getEnumOptions()[row];
					int wildcardIndex = Arrays.asList(cType.getEnumOptions())
						.indexOf(ColumnType.WILDCARD);
					if (wildcardIndex <= 0 && wildcardIndex < row)
					{
						clicked = cType.getEnumOptions()[row + 1];
					}
				}
				else
				{
					// stringColumnOptions staan niet in alfabetische volgorde; neem enumClassFrequency
					FrequencyTuple[] freqTuple = FrequencyTableView.this.model.enumClassFrequency()[splitClass];
					clicked = freqTuple[row].label;
//					System.out.println("FrequencyTableView.RowClickListener.rowClicked("
//						+ rowNumber + ") in splitClass " + splitClass);
				}

				ArrayList<Boolean> selectionList = new ArrayList<Boolean>(
					FrequencyTableView.this.model.getTableModel().getRowCount());
				for (int i = 0; i < FrequencyTableView.this.model
					.getTableModel().getRowCount(); i++)
				{
					Object o = FrequencyTableView.this.model.getTableModel()
						.getValueAt(i,
							FrequencyTableView.this.model.getColumnIndex());

					selectionList
					.add(!o.equals(ColumnType.WILDCARD)
						&& ((String) o).equals(clicked)
						&& FrequencyTableView.this.model.getTableModel()
							.classifyObject(i,
								FrequencyTableView.this.model.getSplitOptions()) == splitClass);
				}
				FrequencyTableView.this.model.getTableModel().setSelectionList(
					selectionList);
			}
			
			FrequencyTableView.this.mainPanel.revalidate();
		}
	}
}
