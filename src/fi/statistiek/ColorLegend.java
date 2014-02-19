package fi.statistiek;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Legend component
 * @author Manu Drijvers
 *
 */
public class ColorLegend extends JPanel {
	private String columnName;
	private ArrayList<String> splitStrings;
	private ArrayList<Color> splitColors;
	
	private ArrayList<JLabel> labels;
	private ArrayList<JLabel> colorPreviews;
	
	private JLabel columnLabel;
	private JPanel labelsPanel;
	private JScrollPane scrollPane;
	private int maxLength;
	
	public static final int LABEL_HEIGHT = 20;
	public static final int COLOR_PREVIEW_WIDTH = 30;
	public static final int LABEL_HGAP = 10;
	
	/**
	 * Constructor
	 * @param columnName Name of the split column
	 * @param splitStrings Names of the split groups
	 * @param splitColors Colors of the split groups
	 */
	public ColorLegend(String columnName, ArrayList<String> splitStrings, ArrayList<Color> splitColors) {
		super(new BorderLayout());
		super.setBackground(new Color(240,240,240));
		this.columnName = columnName;
		this.columnLabel = new JLabel(this.columnName);
		this.columnLabel.setFont(Statistiek.font);
		super.add(this.columnLabel, BorderLayout.NORTH);
		
		this.splitStrings = splitStrings;
		this.splitColors = splitColors;
		this.labelsPanel = new JPanel(null);
		this.labelsPanel.setBackground(new Color(240,240,240));
		this.makeJLabels();
		this.updatePreferredSize();
		this.placeComponents();
		this.scrollPane = new JScrollPane(this.labelsPanel);
		super.add(this.scrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * Set new split groups
	 * @param splitStrings names of the split groups
	 * @param splitColors colors of the split groups
	 */
	public void setColors(ArrayList<String> splitStrings, ArrayList<Color> splitColors) {
		this.splitStrings = splitStrings;
		this.splitColors = splitColors;
		this.labelsPanel.removeAll();
		this.makeJLabels();
		this.updatePreferredSize();
		this.placeComponents();
	}
	
	/**
	 * Update the column name
	 * @param columnName Name of the split column
	 */
	public void setColumnString(String colummName) {
//		System.out.println("columnString set to " + columnName);
		this.columnName = colummName;
		this.columnLabel.setText(this.columnName);
	}
	
	/**
	 * Create the JLabels and add them to labelsPanel
	 */
	private void makeJLabels() {
		this.labels = new ArrayList<JLabel>();
		this.colorPreviews = new ArrayList<JLabel>();
		
		if(this.splitColors != null && this.splitStrings != null) {
			
			for(String s : this.splitStrings) {
				JLabel label = new JLabel(s);
				label.setFont(Statistiek.font);
				this.labels.add(label);
				this.labelsPanel.add(label);
			}
			
			for(Color c : this.splitColors) {
				JLabel label = new JLabel();
				this.colorPreviews.add(label);
				this.labelsPanel.add(label);
				label.setBackground(c);
				label.setOpaque(true);
			}
		}
	}
	
	/**
	 * Place all components at the right location
	 */
	private void placeComponents() {
		if(this.splitStrings == null || this.splitColors == null) {
			return;
		}
		this.labelsPanel.setPreferredSize(new Dimension(super.getWidth()-5, this.splitStrings.size()*LABEL_HEIGHT + (this.splitStrings.size()-1)*LABEL_HGAP));
		for(int i = 0; i < this.splitStrings.size(); i++) {
			this.labels.get(i).setBounds(5, i*(LABEL_HEIGHT+LABEL_HGAP)+5, this.maxLength, LABEL_HEIGHT);
			this.colorPreviews.get(i).setBounds(this.maxLength + 10, i*(LABEL_HEIGHT+LABEL_HGAP)+5, COLOR_PREVIEW_WIDTH, LABEL_HEIGHT);
		}
	}
	
	/**
	 * Set the preferred size
	 */
	private void updatePreferredSize() {
		this.maxLength = 0;
		for(JLabel label : this.labels) {
			this.maxLength = Math.max(this.maxLength, label.getPreferredSize().width);
		}
		super.setPreferredSize(new Dimension(this.maxLength + COLOR_PREVIEW_WIDTH + 20, 0));
	}
	
	public void setBounds(Rectangle r) {
		super.setBounds(r);
		this.placeComponents();
	}
}
