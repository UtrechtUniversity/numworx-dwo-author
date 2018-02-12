package fi.statistiek.piechart;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.PieStyler.AnnotationType;
import org.knowm.xchart.style.Styler.LegendPosition;
import org.knowm.xchart.style.Styler.ToolTipType;

import fi.statistiek.Statistiek;
import fi.statistiek.histogram.HistogramModel.FrequencyTuple;

/**
 * MVC View for statistiekView PieChart
 * 
 * @author Sylvia van Borkulo
 * 
 */
public class PieChartView extends JPanel implements Observer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PieChartModel model;
	private PieChartController controller;
	private PieChartUserOptionsPanel userOptionsPanel;
	private JButton dialogButton;

	private JPanel mainPanel;
	private JScrollPane scrollPane;

	private PieChart pieChart;
	private JPanel panelChart;
	private int width;
	private int height;
	
	/**
	 * The frequencies in the column index variable
	 */
	private FrequencyTuple[][] frequencies_enum;

	/**
	 * Constructor
	 * 
	 * @param model
	 *            MVC Model
	 * @param controller
	 *            MVC Controller
	 */
	public PieChartView(PieChartModel model,
		PieChartController controller)
	{
		super(new BorderLayout());
		super.setBackground(Color.WHITE);

		this.model = model;
		this.model.addObserver(this);
		this.controller = controller;

		this.userOptionsPanel = new PieChartUserOptionsPanel(this, controller, model);
		dialogButton = userOptionsPanel.getDialogButton();
		super.add(dialogButton, BorderLayout.SOUTH);

		this.mainPanel = new JPanel();
		
		this.scrollPane = new JScrollPane(this.mainPanel);
		this.scrollPane.getVerticalScrollBar().setUnitIncrement(Statistiek.scrollSpeedUnit);
		
		super.add(this.scrollPane, BorderLayout.CENTER);

		this.mainPanel.setBackground(Color.WHITE);

		this.update(null, null);
	}
	
	public void update(Observable arg0, Object arg1)
	{
		// update the components in the useroptionspanel
		userOptionsPanel.update();

		if (this.model.columnIndexValid())
		{
			this.frequencies_enum = this.model.enumClassFrequency();

			makePieChart();				

			this.mainPanel.setBackground(Color.WHITE);
		} // columnIndexValid()

		this.dialogButton.setVisible(this.model.getStatTableModel()
			.isViewsEditable());

		this.setMainPanelSize();
		this.scrollPane.setViewportView(mainPanel);

		this.mainPanel.revalidate();

		this.repaint();
	}

	/**
	 * Make the pie chart on panel.
	 */
	private void makePieChart()
	{
		String columnName = this.model.getStatTableModel().getColumnName(model.getColumnIndex());

		int w = this.scrollPane.getViewport().getWidth();
		int h = this.scrollPane.getViewport().getHeight();
		if (w != 0)
			width = w; // save this!
		else
			w = this.width;

		if (h != 0)
			height = h; // save this!
		else
			h = this.height;
		
		// fallback on default size
//		if (w == 0)
//			w = 607;
//		if (h == 0)
//			h = 419;
		
		pieChart = new PieChartBuilder().width(w).height(h).title(columnName).build();
		
		pieChart.getStyler().setLegendPosition(LegendPosition.InsideNE);
		pieChart.getStyler().setAnnotationType(AnnotationType.Percentage); // info in de taartpunt
		pieChart.getStyler().setHasAnnotations(true);
		pieChart.getStyler().setToolTipsEnabled(true);
		pieChart.getStyler().setToolTipType(ToolTipType.xAndYLabels); // doet niets
		pieChart.getStyler().setChartBackgroundColor(Color.WHITE);
		pieChart.getStyler().setChartTitleBoxBorderColor(Color.WHITE);
		pieChart.getStyler().setPlotBorderColor(Color.WHITE);
		pieChart.getStyler().setLegendBorderColor(Color.WHITE);
		//pieChart.getStyler().setDecimalPattern("##.##"); // doet niets?
		String pattern = pieChart.getStyler().getDecimalPattern();

		FrequencyTuple[] ft = frequencies_enum[0]; // er is geen split, dus splitclass 0

		// for-loop over de categorieen
		for (int i = 0; i < ft.length; i++)
		{
			FrequencyTuple ft_category = ft[i];
			pieChart.addSeries(ft_category.label, ft_category.frequency);
		}
	    
		panelChart = new XChartPanel(pieChart);
		panelChart.setBackground(Color.green);
		mainPanel.removeAll();
		mainPanel.add(panelChart);
		mainPanel.setVisible(true);
		scrollPane.setVisible(true);
		
		System.out.println("PieChartView.makePieChart(): pieChart.getWidth() = " + pieChart.getWidth() +
			", pieChart.getHeight() = " + pieChart.getHeight());
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		// clear panel
		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		panelChart.repaint();
		Graphics2D g2D = (Graphics2D) g;
		pieChart.paint(g2D, this.getWidth(), this.getHeight());
		
		System.out.println("PieChartView.paintComponent(): this.getWidth() = " + this.getWidth() +
			", this.getHeight() = " + this.getHeight() + ", pieChart.getWidth() = " + pieChart.getWidth() +
			", pieChart.getHeight() = " + pieChart.getHeight());

		// teken pie chart met de goede maat
//		makePieChart();
	}
	
	@Override
	public void setBounds(int x, int y, int w, int h)
	{
		super.setBounds(x, y, w, h);
		this.width = w;
		this.height = h; 

		this.setMainPanelSize();
	}

	private void setMainPanelSize()
	{
//		int w = this.scrollPane.getWidth();
//		int h = this.scrollPane.getHeight() - 5;
//		int w = this.scrollPane.getWidth() - 30; // - width scrollbar
//		int h = this.scrollPane.getHeight() - dialogButton.getHeight(); // - width scrollbar
		
		int w = this.scrollPane.getViewport().getWidth();
		int h = this.scrollPane.getViewport().getHeight();

		System.out.println("PieChartView.setMainPanelSize(): w = " + w + ", h = " + h);
		
		if (w == 0)
		{
			w = width;
			h = height;
		}
		
		Dimension d = new Dimension(w, h);
		
		this.mainPanel.setPreferredSize(d);
		
//		panelChart.setPreferredSize(d);
//		panelChart.setSize(w, h);
		
		// resize piechart... hoe?
	}

	public void setModel(PieChartModel model)
	{
		this.model = model;
		this.model.addObserver(this);
		userOptionsPanel.setModel(model);
		this.update(null, null);
	}

	public int varBoxSelectedIndex()
	{
		return userOptionsPanel.getVarColumnsBoxSelectedIndex();
	}
	
	public PieChartUserOptionsPanel getUserOptionsPanel()
	{
		return userOptionsPanel;
	}
}
