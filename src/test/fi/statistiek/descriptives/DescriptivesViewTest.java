package test.fi.statistiek.descriptives;

import static org.junit.Assert.*;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.fi.statistiek.StatTableModelTest;
import fi.statistiek.StatTableModel;
import fi.statistiek.Statistiek;
import fi.statistiek.descriptives.DescriptivesController;
import fi.statistiek.descriptives.DescriptivesModel;
import fi.statistiek.descriptives.DescriptivesView;

public class DescriptivesViewTest
{
	private DescriptivesView view;
	private DescriptivesModel model;
	private DescriptivesController controller;

	/**
	 * Set up test case 1. 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		this.setUpTestCase1();
	}
	
	/**
	 * Set up test case 1:
	 * 		columnIndex = 1 (gewicht)
	 * 		columnSplitIndex = 5 (geslacht)
	 * 		case 0-2 selected
	 */
	private void setUpTestCase1()
	{
		Statistiek statistiek = new Statistiek();
		StatTableModelTest statTableModelTest = new StatTableModelTest();
		StatTableModel statTableModel = 
			statTableModelTest.getStatTableModelWithSelection();
		this.controller = new DescriptivesController(
			statTableModel, "Beschrijvende statistiek", 1);
		this.model = new DescriptivesModel(
			statTableModel, "Beschrijvende statistiek");
		view = new DescriptivesView(this.model, this.controller);
		// columnIndex = -1!
		this.model.setColumnIndex(1); // gewicht
		this.setSplitGeslacht();
		this.view.update(null, null);		
	}

	private void setSplitGeslacht()
	{
		this.model.setColumnSplitIndex(5); // geslacht
		this.model.setSplitOptions(this.model.getSplitOptions());
		this.controller.setSplit(5);
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void testData()
	{
		this.view.update(null, null);
		JLabel[][][] expected = new JLabel[7][2][2]; // 7 descriptives, selection, 2 split classes
		expected = getExpectedDataTestCase1();
		JLabel[][][] actual = view.getDataLabels();
		
		for (int i = 0; i < 7; i++) // descriptive fields: nr, min, max, mean, sd, median, mode
		{
			for (int j = 0; j < 2; j++) // selection: 0 (all cases), 1 (selected cases)
			{
				for (int k = 0; k < 2; k++) // number of splits: m, v
				{
					// compare the text of the label
					assertEquals("i=" + i + ", j=" + j + ", k=" + k, expected[i][j][k].getText(), actual[i][j][k].getText());
				}
			}
		}
	}

	private JLabel[][][] getExpectedDataTestCase1()
	{
		JLabel[][][] expected = new JLabel[7][2][2];
		
		JLabel label;
		String notAvailable = Statistiek.rb.getString("notAvailable");
		Border matteBorder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		Border paddingBorder = BorderFactory.createEmptyBorder(
			this.view.GRID_TOPGAP, this.view.GRID_LEFTGAP,
			this.view.GRID_BOTTOMGAP, this.view.GRID_RIGHTGAP);
		
		String[][][] expectedStrings = new String[][][]{
			// m, all cases
			{{"3", "51", "70", "59", "8.04", "56", notAvailable},
			// m, selected cases
			{"0", notAvailable, notAvailable, notAvailable, notAvailable, notAvailable, notAvailable}},
			// v, all cases
			{{"7", "40", "56", "51.86", "5.11", "54", "55"},
			// v, selected cases
			{"3", "40", "55", "49", "6.48", "52", notAvailable}}};
		
		for (int i = 0; i < 7; i++) // descriptive fields: nr, min, max, mean, sd, median, mode
		{
			for (int j = 0; j < 2; j++) // selection: 0 (all cases), 1 (selected cases)
			{
				for (int k = 0; k < 2; k++) // number of splits: m, v
				{
					label = new JLabel(expectedStrings[k][j][i]);
					label.setFont(Statistiek.font);
					label.setBorder(BorderFactory.createCompoundBorder(matteBorder, paddingBorder));
					label.setHorizontalAlignment(SwingConstants.RIGHT);

					expected[i][j][k] = label;
				}
			}
		}
		
		return expected;
	}

}
