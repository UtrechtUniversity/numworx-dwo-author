package fi.statistiek.boxplot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import fi.statistiek.Statistiek;
import fi.statistiek.DialogButton;
import fi.statistiek.histogram.HistogramModel;
import fi.statistiek.histogram.HistogramUserOptionsPanel;

/**
 * MVC View for statistiekview Boxplot
 * 
 * @author ManuDrijvers
 *
 */
public class BoxplotView extends JPanel implements Observer {
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
	 * @param model the model
	 * @param controller the controller
	 */
	public BoxplotView(BoxplotModel model, BoxplotController controller) {
		super.setLayout(new BorderLayout());
		
		this.model = model;
		this.model.addObserver(this);
		this.controller = controller;
		
		/*this.userOptionsPanel = new JPanel(new GridLayout(2,3));
		this.userOptionsPanel1 = new JPanel();
		
		this.columnLabel = new JLabel(Statistiek.rb.getString("variableLabel"));
		this.userOptionsPanel.add(this.columnLabel);

		this.splitButton = new JButton(Statistiek.rb.getString("splitdataButton"));
		this.splitButton.setActionCommand("splitButton");
		this.splitButton.addActionListener(this.controller);
		this.userOptionsPanel.add(this.splitButton);
		
		this.verticalBoxesButton = new JRadioButton(Statistiek.rb.getString("verticalboxplotsRadio"));
		this.verticalBoxesButton.setActionCommand("verticalBoxesButton");
		this.verticalBoxesButton.addActionListener(this.controller);
		this.userOptionsPanel.add(this.verticalBoxesButton);
		
		this.columnBox = new JComboBox();
		this.columnBox.setActionCommand("columnBox");
		this.columnBox.addActionListener(controller);
		this.userOptionsPanel.add(this.columnBox);
		
		this.userOptionsPanel.add(new JLabel());
		
		this.horizontalBoxesButton = new JRadioButton(Statistiek.rb.getString("horizontalboxplotsRadio"));
		this.horizontalBoxesButton.setActionCommand("horizontalBoxesButton");
		this.horizontalBoxesButton.addActionListener(this.controller);
		this.userOptionsPanel.add(this.horizontalBoxesButton);
		ButtonGroup group = new ButtonGroup();
		group.add(horizontalBoxesButton);
		group.add(verticalBoxesButton);*/
		
		userOptionsPanel = new BoxplotUserOptionsPanel(this, controller, model);
		dialogButton = userOptionsPanel.getDialogButton();
		super.add(dialogButton, BorderLayout.SOUTH);
		
		this.mainPanel = new JPanel();
		super.add(this.mainPanel, BorderLayout.CENTER);
		
		
		this.update(null, null);
	}
	
	/**
	 * Gets the string that is currently selected in the column box
	 * @return
	 */
	public String getColumnBoxSelectedString() {
		return (String)this.userOptionsPanel.getColumnBoxSelectedString();
	}
	
	public boolean isVerticalBoxesButtonSelected() {
		return this.userOptionsPanel.isVerticalBoxesButtonSelected();
	}
	
	public int getSplitVarBoxSelectedIndex() {
		return userOptionsPanel.getSplitVarBoxSelectedIndex();
	}
	
	public int getSplitBinsBoxSelectedInt() {
		return userOptionsPanel.getSplitBinsBoxSelectedInt();
	}
	
	
	public double getSplitminBoundary() {
		return userOptionsPanel.getSplitminBoundary();
	}
	
	
	public double getSplitBinWidth() {
		return userOptionsPanel.getSplitBinWidth();
	}
	

	public int getDependentAxisWidth() {
		if(this.dependentAxis == null) {
			return 0;
		}
		else {
			return this.dependentAxis.getWidth();
		}
	}
	
	public int getDependentAxisHeight() {
		if(this.dependentAxis == null) {
			return 0;
		}
		else {
			return this.dependentAxis.getHeight();
		}
	}
	
	public int getIndependentAxisWidth() {
		if(this.independentAxis == null) {
			return 0;
		}
		else {
			return this.independentAxis.getWidth();
		}
	}
	
	public Dimension getMainPanelSize() {
		return this.mainPanel.getSize();
	}
	
	public void setModel(BoxplotModel model) {
		this.model = model;
		this.model.addObserver(this);
		userOptionsPanel.setModel(model);
		this.update(null, null);
	}
	
	public void update(Observable arg0, Object arg1) {
		System.out.println("Updating boxplotview");
		this.dialogButton.setVisible(this.model.getTableModel().isViewsEditable());
		
		userOptionsPanel.update();
		
		/*
		this.verticalBoxesButton.setSelected(this.model.isVerticalBoxplots());
		this.horizontalBoxesButton.setSelected(!this.model.isVerticalBoxplots());
		
		this.columnBox.removeActionListener(this.controller);
		this.columnBox.removeAllItems();
		
		for(int column = 0; column < this.model.getTableModel().getColumnCount(); column++) {
			if(this.model.getTableModel().getColumnTypes().get(column).getType().isNumber()) {
				this.columnBox.addItem(this.model.getTableModel().getColumnNames().get(column));
			}
		}
		if(this.model.getTableModel().isColumnIndexValid(this.model.getColumnIndex())) {
			this.columnBox.setSelectedItem(this.model.getTableModel().getColumnNames().get(this.model.getColumnIndex()));
		}
		else {
			//set no item selected
			this.columnBox.setSelectedIndex(-1);
		}
		this.columnBox.addActionListener(this.controller);
		//this.columnSplitBox.addActionListener(this.controller);
		*/
		this.mainPanel.removeAll();
		this.mainPanel.setBackground(Color.WHITE);
		if(this.model.getTableModel().isColumnIndexValid(this.model.getColumnIndex())) { //&&this.model.getTableModel().isColumnIndexValid(this.model.getColumnSplitIndex())
			if(!this.model.getTableModel().isColumnIndexValid(this.model.getColumnSplitIndex())) {
				
				if(this.model.isVerticalBoxplots()) {
					JPanel boxplotsPanel = new JPanel(new GridLayout(1, 1));
					boxplotsPanel.setBackground(Color.WHITE);
					this.mainPanel.setLayout(new BorderLayout());
					
						SingleBoxplotView v = new SingleBoxplotView(
								this.model.getMinValue(0),
								this.model.getLowerQuartile(0),
								this.model.getMedian(0),
								this.model.getUpperQuartile(0),
								this.model.getMaxValue(0),
								this.model.getDataMinValue(),
								this.model.getDataMaxValue(),
								this.model.isVerticalBoxplots());
						System.out.println(v);
						boxplotsPanel.add(v);
						
					JPanel eastPanel = new JPanel() {
						public Dimension getPreferredSize() {
							return new Dimension(BoxplotView.KEEP_CLEAR_WIDTH, 0);
						};
					};
					eastPanel.setBackground(Color.WHITE);
					this.mainPanel.add(eastPanel, BorderLayout.EAST);
					this.dependentAxis = new BoxplotDependentAxis(this.model.getDataMinValue(), this.model.getDataMaxValue(), this.model.isVerticalBoxplots(), this, this.model.getTableModel().getColumnName(this.model.getColumnIndex()));
					this.mainPanel.add(this.dependentAxis, BorderLayout.WEST);
		
					this.mainPanel.add(boxplotsPanel, BorderLayout.CENTER);
					this.mainPanel.setBounds(this.mainPanel.getBounds());
				}
				else {
					JPanel boxplotsPanel = new JPanel(new GridLayout(1, 1));
					boxplotsPanel.setBackground(Color.WHITE);
					this.mainPanel.setLayout(new BorderLayout());
						SingleBoxplotView v = new SingleBoxplotView(
								this.model.getMinValue(0),
								this.model.getLowerQuartile(0),
								this.model.getMedian(0),
								this.model.getUpperQuartile(0),
								this.model.getMaxValue(0),
								this.model.getDataMinValue(),
								this.model.getDataMaxValue(),
								this.model.isVerticalBoxplots());
						System.out.println(v);
						boxplotsPanel.add(v);
					JPanel centerPanel = new JPanel(new BorderLayout());
					JPanel northPanel = new JPanel() {
						public Dimension getPreferredSize() {
							return new Dimension(0, BoxplotView.KEEP_CLEAR_WIDTH);
						};
					};
					northPanel.setBackground(Color.WHITE);
					centerPanel.add(northPanel, BorderLayout.NORTH);
					this.dependentAxis = new BoxplotDependentAxis(this.model.getDataMinValue(), this.model.getDataMaxValue(), this.model.isVerticalBoxplots(), this, this.model.getTableModel().getColumnName(this.model.getColumnIndex()));
					centerPanel.add(this.dependentAxis, BorderLayout.SOUTH);
					centerPanel.setBackground(Color.WHITE);
					centerPanel.add(boxplotsPanel, BorderLayout.CENTER);
					this.mainPanel.add(centerPanel, BorderLayout.CENTER);
					this.mainPanel.setBounds(this.mainPanel.getBounds());
				}
			}
			else {
				System.out.println("columns valid");
				int splitClasses = this.model.getSplitClasses();
				System.out.println("SPlitclasses: " + splitClasses);
				
				if(this.model.isVerticalBoxplots()) {
					JPanel boxplotsPanel = new JPanel(new GridLayout(1, splitClasses));
					boxplotsPanel.setBackground(Color.WHITE);
					this.dependentAxis = new BoxplotDependentAxis(this.model.getDataMinValue(), this.model.getDataMaxValue(), this.model.isVerticalBoxplots(), this, this.model.getTableModel().getColumnName(this.model.getColumnIndex()));
					this.mainPanel.add(this.dependentAxis, BorderLayout.WEST);
					for(int i = 0; i < splitClasses; i++) {
						SingleBoxplotView v = new SingleBoxplotView(
								this.model.getMinValue(i),
								this.model.getLowerQuartile(i),
								this.model.getMedian(i),
								this.model.getUpperQuartile(i),
								this.model.getMaxValue(i),
								this.model.getDataMinValue(),
								this.model.getDataMaxValue(),
								this.model.isVerticalBoxplots());
						System.out.println(v);
						boxplotsPanel.add(v);
						
					}
					JPanel eastPanel = new JPanel() {
						public Dimension getPreferredSize() {
							return new Dimension(BoxplotView.KEEP_CLEAR_WIDTH, 0);
						};
					};
					eastPanel.setBackground(Color.WHITE);
					this.mainPanel.add(eastPanel, BorderLayout.EAST);
		
					this.mainPanel.add(boxplotsPanel, BorderLayout.CENTER);
					this.independentAxis = new BoxplotIndependentAxis(this.model, this, this.model.isVerticalBoxplots(), this.model.getTableModel().getColumnName(this.model.getColumnSplitIndex()));
					//independentAxis.setSize(independentAxis.getPreferredSize());
					this.mainPanel.add(this.independentAxis, BorderLayout.SOUTH);
					this.mainPanel.setBounds(this.mainPanel.getBounds());
				}
				else {
					JPanel boxplotsPanel = new JPanel(new GridLayout(splitClasses, 1));
					boxplotsPanel.setBackground(Color.WHITE);
					this.mainPanel.setLayout(new BorderLayout());
					JPanel centerPanel = new JPanel(new BorderLayout());
					
					this.dependentAxis = new BoxplotDependentAxis(this.model.getDataMinValue(), this.model.getDataMaxValue(), this.model.isVerticalBoxplots(), this, this.model.getTableModel().getColumnName(this.model.getColumnIndex()));
					centerPanel.add(this.dependentAxis, BorderLayout.SOUTH);
					for(int i = splitClasses-1; i >= 0; i--) {
						SingleBoxplotView v = new SingleBoxplotView(
								this.model.getMinValue(i),
								this.model.getLowerQuartile(i),
								this.model.getMedian(i),
								this.model.getUpperQuartile(i),
								this.model.getMaxValue(i),
								this.model.getDataMinValue(),
								this.model.getDataMaxValue(),
								this.model.isVerticalBoxplots());
						System.out.println(v);
						boxplotsPanel.add(v);
					}
					JPanel northPanel = new JPanel() {
						public Dimension getPreferredSize() {
							return new Dimension(0, BoxplotView.KEEP_CLEAR_WIDTH);
						};
					};
					northPanel.setBackground(Color.WHITE);
					centerPanel.add(northPanel, BorderLayout.NORTH);
					
					centerPanel.add(boxplotsPanel, BorderLayout.CENTER);
					this.mainPanel.add(centerPanel, BorderLayout.CENTER);
					this.independentAxis = new BoxplotIndependentAxis(
							this.model, 
							this, 
							this.model.isVerticalBoxplots(), 
							this.model.getTableModel().getColumnName(this.model.getColumnSplitIndex()));
					this.mainPanel.add(this.independentAxis, BorderLayout.WEST);
					this.mainPanel.setBounds(this.mainPanel.getBounds());
				}
			}
		}
		this.repaint();
	}
}
