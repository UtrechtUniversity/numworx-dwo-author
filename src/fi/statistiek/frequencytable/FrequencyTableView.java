package fi.statistiek.frequencytable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fi.statistiek.ColorPreviewer;
import fi.statistiek.DialogButton;
import fi.statistiek.Statistiek;
import fi.statistiek.histogram.HistogramUserOptionsPanel;
import fi.statistiek.histogram.HistogramView;
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
	// TODO deze implementeren
	//private FrequencyTableUserOptionsPanel userOptionsPanel;
	// private JButton dialogButton;

	private JPanel userOptionsPanel;

	// TODO: onderstaande naar FrequencyTableUserOptionsPanel
	private JLabel columnIndexLabel;
	private JComboBox columnIndexBox;
	private JCheckBox showPercBox;
	private JCheckBox showFreqBox;
	private JCheckBox showCumulativeBox;
	private JLabel noBinsLabel;
	private JTextField noBinsField;
	private JButton chooseBinsButton;

	private DialogButton dialogButton;

	private JPanel mainPanel;

	private int mainPanelRows;
	private int mainPanelColumns;

	private Color[] rowColors;
	public static final int MAIN_GRID_HGAP = 8;
	public static final int MAIN_GRID_VGAP = 8;
	public static final Color SELECTED_COLOR = Color.YELLOW;

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

		this.mainPanel = new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				if (!FrequencyTableView.this.model.columnIndexValid())
				{
					// variable is not valid, so there is nothing to paint
					return;
				}

				g.setColor(Color.BLACK);

				double columnWidth = this.getWidth()
					/ (double) FrequencyTableView.this.mainPanelColumns;
				int rowAreaHeight = this.getHeight()
					- (FrequencyTableView.this.mainPanelRows + 1)
					* FrequencyTableView.MAIN_GRID_HGAP;

				double rowHeight = (rowAreaHeight / (double) (FrequencyTableView.this.mainPanelRows + 2))
					+ MAIN_GRID_HGAP;
				// int roundRowHeight = (int)Math.round(rowHeight);
				int roundRowHeight = (int) rowHeight;

				g.setColor(Color.WHITE);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());

				// draw selection color of rows
				if (FrequencyTableView.this.rowColors != null)
				{
					for (int i = 0; i < FrequencyTableView.this.rowColors.length; i++)
					{
						g.setColor(FrequencyTableView.this.rowColors[i]);
						g.fillRect(
							0,
							(int) Math.round((i + 1) * rowHeight),
							this.getWidth(),
							(int) Math.round((i + 2) * rowHeight)
								- (int) Math.round((i + 1) * rowHeight));
					}
				}

				g.setColor(Color.BLACK);

				// draw vertical lines
				for (int i = 1; i < FrequencyTableView.this.mainPanelColumns; i++)
				{
					int x = (int) (columnWidth * i);
					g.drawLine(x, 0, x, this.getHeight());
				}

				// draw horizontal lines
				g.drawLine(0, roundRowHeight, this.getWidth(), roundRowHeight);

				int y = (int) Math
					.round((FrequencyTableView.this.mainPanelRows + 1)
						* roundRowHeight);
				g.drawLine(0, y, this.getWidth(), y);
			}
		};
		this.mainPanel.setBackground(Color.WHITE);
		this.mainPanel.addMouseListener(new RowClickListener());
		super.add(this.mainPanel, BorderLayout.CENTER);

		GridLayout gl = new GridLayout(2, 3);
		gl.setHgap(0);// TODO terugzetten
		gl.setVgap(0);
		this.userOptionsPanel = new JPanel(gl);

		GridLayout gl1 = new GridLayout(1, 2);
		gl1.setHgap(5);
		gl1.setVgap(5);
		JPanel p1 = new JPanel(gl1);
		this.columnIndexLabel = new JLabel(
			Statistiek.rb.getString("variableLabel"), SwingConstants.RIGHT);
		p1.add(this.columnIndexLabel);

		this.columnIndexBox = new JComboBox();
		this.columnIndexBox.setActionCommand("columnIndexBox");
		this.columnIndexBox.addActionListener(this.controller);
		p1.add(this.columnIndexBox);

		this.userOptionsPanel.add(p1);

		this.showPercBox = new JCheckBox(
			Statistiek.rb.getString("showpercentageCheckbox"), true);
		this.showPercBox.setActionCommand("showPercBox");
		this.showPercBox.addActionListener(this.controller);
		this.userOptionsPanel.add(this.showPercBox);

		this.showFreqBox = new JCheckBox(
			Statistiek.rb.getString("showfrequencyCheckbox"), true);
		this.showFreqBox.setActionCommand("showFreqBox");
		this.showFreqBox.addActionListener(this.controller);
		this.userOptionsPanel.add(this.showFreqBox);

		GridLayout gl2 = new GridLayout(1, 2);
		gl2.setHgap(5);
		gl2.setVgap(5);
		JPanel p2 = new JPanel(gl2);

		this.noBinsLabel = new JLabel(
			Statistiek.rb.getString("numberofbinsLabel"), SwingConstants.RIGHT);
		p2.add(this.noBinsLabel);

		this.noBinsField = new JTextField("2");
		this.noBinsField.setActionCommand("noBinsField");
		this.noBinsField.addActionListener(this.controller);
		this.noBinsField.addFocusListener(this.controller);
		p2.add(this.noBinsField);

		this.userOptionsPanel.add(p2);

		this.chooseBinsButton = new JButton(
			Statistiek.rb.getString("BoundariesButton"));
		this.chooseBinsButton.setActionCommand("chooseBinsButton");
		this.chooseBinsButton.addActionListener(this.controller);
		this.userOptionsPanel.add(this.chooseBinsButton);

		this.showCumulativeBox = new JCheckBox(
			Statistiek.rb.getString("showcumulativefrequencyCheckbox"), true);
		this.showCumulativeBox.setActionCommand("showCumulativeBox");
		this.showCumulativeBox.addActionListener(this.controller);
		this.userOptionsPanel.add(this.showCumulativeBox);

		// super.add(this.userOptionsPanel, BorderLayout.SOUTH);

		dialogButton = new DialogButton(
			Statistiek.rb.getString("settingsButton"), this.userOptionsPanel);
		dialogButton.setDialogSize(650, 100);

		super.add(dialogButton, BorderLayout.SOUTH);

		// dialogButton.makeDialog();

		this.update(null, null);
	}

	public JTextField getNoBinsField()
	{
		return this.noBinsField;
	}

	public String getNoBinsFieldText()
	{
		return this.noBinsField.getText();
	}

	public boolean isShowPercBoxSelected()
	{
		return this.showPercBox.isSelected();
	}

	public boolean isShowFreqBoxSelected()
	{
		return this.showFreqBox.isSelected();
	}

	public boolean isShowCumulativeBoxSelected()
	{
		return this.showCumulativeBox.isSelected();
	}

	public int varBoxSelectedIndex()
	{
		return this.columnIndexBox.getSelectedIndex();
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
		this.showPercBox.setSelected(this.model.isShowPercentage());
		this.showFreqBox.setSelected(this.model.isShowFreq());
		this.showCumulativeBox.setSelected(this.model.isShowCumulative());

		this.columnIndexBox.removeActionListener(this.controller);
		this.columnIndexBox.removeAllItems();

		for (String varName : this.model.getTableModel().getColumnNames())
		{
			this.columnIndexBox.addItem(varName);
		}

		if (this.model.columnIndexValid())
		{
			this.columnIndexBox.setSelectedIndex(this.model.getColumnIndex());
		}
		else
		{
			// set no item selected
			System.out.println("Niet valid!");
			this.columnIndexBox.setSelectedIndex(-1);
		}
		this.columnIndexBox.addActionListener(this.controller);

		this.noBinsField.setText(Integer.toString(this.model.getBinBoundaries()
			.size() - 1));

		// this.userOptionsPanel.setVisible(this.model.getTableModel().isViewsEditable());
		this.dialogButton.setVisible(this.model.getTableModel()
			.isViewsEditable());

		this.mainPanel.removeAll();
		if (this.model.columnIndexValid())
		{
			ColumnType cType = this.model.getTableModel().getColumnTypes()
				.get(this.model.getColumnIndex());
			AllowedTypes type = cType.getType();

			this.mainPanelColumns = 1;
			if (this.model.isShowFreq())
			{
				this.mainPanelColumns++;
			}
			if (this.model.isShowCumulative())
			{
				this.mainPanelColumns++;
			}
			if (this.model.isShowPercentage())
			{
				this.mainPanelColumns += 2;
			}

			FrequencyTuple[] frequencyTuple = null;

			if (type.isNumber())
			{
				this.mainPanelRows = this.model.getBinBoundaries().size() - 1;
				if (this.mainPanelRows < 0)
				{
					this.mainPanelRows = 0;
				}
			}
			else
			{
				frequencyTuple = this.model.enumClassFrequency();
				this.mainPanelRows = frequencyTuple.length;
			}

			// set gridlayout, +2 rows for header and sum rows
			GridLayout gl = new GridLayout(this.mainPanelRows + 2,
				this.mainPanelColumns);
			gl.setHgap(FrequencyTableView.MAIN_GRID_HGAP);
			gl.setVgap(FrequencyTableView.MAIN_GRID_VGAP);
			this.mainPanel.setLayout(gl);
			this.mainPanel.setBackground(Color.WHITE);
			// make header row
			this.mainPanel.add(new JLabel(this.model.getTableModel()
				.getColumnName(this.model.getColumnIndex())));
			if (this.model.isShowFreq())
			{
				this.mainPanel
					.add(new JLabel("Freq.", SwingConstants.TRAILING));
			}
			if (this.model.isShowPercentage())
			{
				this.mainPanel
					.add(new JLabel("Freq.%", SwingConstants.TRAILING));
			}
			if (this.model.isShowCumulative())
			{
				this.mainPanel
					.add(new JLabel("Cumul.", SwingConstants.TRAILING));
			}
			if (this.model.isShowPercentage())
			{
				this.mainPanel.add(new JLabel("Cumul.%",
					SwingConstants.TRAILING));
			}

			// make middle part
			int sum;
			int[] frequencies = null;
			if (type.isNumber())
			{
				frequencies = this.model.numberClassFrequency();
				sum = this.arrayEvenSum(frequencies);
			}
			else
			{
				sum = this.tupleArraySum(frequencyTuple);

			}
			int cumulative = 0;
			int freq;
			for (int bin = 0; bin < this.mainPanelRows; bin++)
			{
				if (type.isNumber())
				{
					freq = frequencies[bin * 2];
					JLabel label = new JLabel(this.model.getBinBoundaries()
						.get(bin).toString()
						+ " - "
						+ this.model.getBinBoundaries().get(bin + 1).toString());
					this.mainPanel.add(label);
				}
				else
				{
					freq = frequencyTuple[bin].frequency;
					JLabel label = new JLabel(frequencyTuple[bin].label);
					this.mainPanel.add(label);
				}

				if (this.model.isShowFreq())
				{
					this.mainPanel.add(new JLabel(Integer.toString(freq),
						SwingConstants.TRAILING));
				}
				if (this.model.isShowPercentage())
				{
					double d = freq * 100 / (double) sum;
					d = Math.round(d * 100) / (double) 100;
					this.mainPanel.add(new JLabel(Double.toString(d) + "%",
						SwingConstants.TRAILING));
				}
				if (this.model.isShowCumulative())
				{
					cumulative += freq;
					this.mainPanel.add(new JLabel(Integer.toString(cumulative),
						SwingConstants.TRAILING));
				}
				if (this.model.isShowPercentage())
				{
					double d = (double) cumulative * 100 / (double) sum;
					d = Math.round(d * 100) / (double) 100;
					this.mainPanel.add(new JLabel(Double.toString(d) + "%",
						SwingConstants.TRAILING));
				}
			}

			// make total row
			this.mainPanel
				.add(new JLabel(Statistiek.rb.getString("totalLabel")));
			if (this.model.isShowFreq())
			{
				this.mainPanel.add(new JLabel(Integer.toString(sum),
					SwingConstants.TRAILING));
			}
			if (this.model.isShowPercentage())
			{
				this.mainPanel.add(new JLabel("100%", SwingConstants.TRAILING));
			}
			if (this.model.isShowCumulative())
			{
				this.mainPanel.add(new JLabel(Integer.toString(sum),
					SwingConstants.TRAILING));
			}
			if (this.model.isShowPercentage())
			{
				this.mainPanel.add(new JLabel("100%", SwingConstants.TRAILING));
			}

			// set colors for background
			if (type.isNumber())
			{
				this.rowColors = new Color[frequencies.length / 2];
				for (int i = 0; i < frequencies.length; i += 2)
				{
					double d = (frequencies[i] == 0 ? 0.0
						: (double) frequencies[i + 1] / (double) frequencies[i]);
					System.out.println(frequencies[i + 1] + " "
						+ frequencies[i] + " " + d);
					this.rowColors[i / 2] = ColorPreviewer.mixColors(
						Color.WHITE, SELECTED_COLOR, d);
				}
			}
			else
			{
				this.rowColors = new Color[frequencyTuple.length];
				for (int i = 0; i < frequencyTuple.length; i++)
				{
					FrequencyTuple ft = frequencyTuple[i];
					double d = (ft.frequency == 0 ? 0
						: (double) ft.selectionFrequency
							/ (double) ft.frequency);
					System.out.println(d);
					this.rowColors[i] = ColorPreviewer.mixColors(Color.WHITE,
						SELECTED_COLOR, d);
				}
			}
		}

		System.out.println(this.rowColors);

		this.repaint();
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

	private class RowClickListener implements MouseListener
	{

		public void mouseClicked(MouseEvent arg0)
		{
			int rowAreaHeight = FrequencyTableView.this.mainPanel.getHeight()
				- (FrequencyTableView.this.mainPanelRows + 1)
				* FrequencyTableView.MAIN_GRID_HGAP;

			double rowHeight = (rowAreaHeight / (double) (FrequencyTableView.this.mainPanelRows + 2))
				+ MAIN_GRID_HGAP;
			// int roundRowHeight = (int)Math.round(rowHeight);
			int roundRowHeight = (int) rowHeight;

			int clicked = arg0.getPoint().y / roundRowHeight - 1;
			if (clicked >= 0 && clicked < FrequencyTableView.this.mainPanelRows)
			{
				this.rowClicked(clicked);
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
			// TODO Auto-generated method stub

		}

		public void mouseReleased(MouseEvent arg0)
		{
			// TODO Auto-generated method stub

		}

		private void rowClicked(int row)
		{
			if (!FrequencyTableView.this.model.columnIndexValid())
			{
				return;
			}

			System.out.println("Bar clicked: " + row);
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

					selectionList.add(o.equals(ColumnType.WILDCARD)
						|| FrequencyTableView.this.model.binOfNumber(Double
							.parseDouble((String) o)) == row);
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
					ArrayList<String> options = FrequencyTableView.this.model
						.getTableModel().stringColumnOptions(
							FrequencyTableView.this.model.getColumnIndex());
					System.out.println(options);
					clicked = options.get(row);
				}

				ArrayList<Boolean> selectionList = new ArrayList<Boolean>(
					FrequencyTableView.this.model.getTableModel().getRowCount());
				for (int i = 0; i < FrequencyTableView.this.model
					.getTableModel().getRowCount(); i++)
				{
					Object o = FrequencyTableView.this.model.getTableModel()
						.getValueAt(i,
							FrequencyTableView.this.model.getColumnIndex());
					selectionList.add(o.equals(ColumnType.WILDCARD)
						|| ((String) o).equals(clicked));
				}
				FrequencyTableView.this.model.getTableModel().setSelectionList(
					selectionList);

			}

		}

	}
}
