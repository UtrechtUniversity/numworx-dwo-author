package test.fi.statistiek.descriptives;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test.fi.statistiek.StatTableModelTest;
import fi.statistiek.SplitOptions;
import fi.statistiek.StatTableModel;
import fi.statistiek.Statistiek;
import fi.statistiek.descriptives.DescriptivesController;
import fi.statistiek.descriptives.DescriptivesModel;
import fi.statistiek.descriptives.DescriptivesView;
import fi.statistiek.histogram.HistogramModel.FrequencyTuple;

public class DescriptivesModelTest
{
	private DescriptivesView view;
	private DescriptivesModel model;
	private DescriptivesController controller;
	private static double delta = 1e-15; // error in comparing doubles 

	@Before
	public void setUp() throws Exception
	{
		this.setUpTestCase1();
	}

	/**
	 * Set up test case 1:
	 * 		10 cases
	 * 		columnIndex = 1 (gewicht)
	 * 		columnSplitIndex = 5 (geslacht)
	 * 		cases 0-2 selected
	 */
	private void setUpTestCase1()
	{
		Statistiek statistiek = new Statistiek();
		Statistiek.setLocale(Locale.getDefault());

		// use the test data
		StatTableModelTest statTableModelTest = new StatTableModelTest();
		StatTableModel statTableModel = 
			statTableModelTest.getStatTableModelWithSelection();
		
		this.controller = new DescriptivesController(
			statTableModel, "Kengetallen", 1);
		this.model = new DescriptivesModel(
			statTableModel, "Kengetallen");
		view = new DescriptivesView(this.model, this.controller);
		// columnIndex = -1!
		this.model.setColumnIndex(1); // gewicht
		this.setSplitGeslacht();
		this.view.update(null, null);		
	}

	/**
	 * Set up test case 2:
	 * 		12 cases
	 * 		columnIndex = 1 (gewicht)
	 * 		columnSplitIndex = 5 (geslacht)
	 * 		cases 0-2 and 10 selected
	 */
	private void setUpTestCase2()
	{
		Statistiek statistiek = new Statistiek();
		Statistiek.setLocale(Locale.getDefault());

		// use the test data
		StatTableModelTest statTableModelTest = new StatTableModelTest();
		StatTableModel statTableModel = 
			statTableModelTest.getStatTableModelWithSelectionAndWildcards();
		
		this.controller = new DescriptivesController(
			statTableModel, "Kengetallen", 1);
		this.model = new DescriptivesModel(
			statTableModel, "Kengetallen");
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
	public void testGetBinBoundaries()
	{
		double min = this.model.getStatTableModel().getColumnMin(1); // columnIndex = 1 (gewicht)
		double max = this.model.getStatTableModel().getColumnMax(1); // columnIndex = 1 (gewicht)
		ArrayList<Double> expected = new ArrayList(Arrays.asList(min, max + 10)); // verschil in tientallen, dus 1 tiental meer voor bovengrens
		ArrayList<Double> actual = this.model.getBinBoundaries();
		assertArrayEquals("", expected.toArray(), actual.toArray());
	}

	@Test
	public void testGetViewName()
	{
		String expected = "Kengetallen";
		String actual = this.model.getViewName();
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnIndex()
	{
		int expected = 1;
		int actual = this.model.getColumnIndex();
		
		assertEquals("", expected, actual, delta);
	}

	@Test
	public void testNumberClassFrequency()
	{
		int[][] expected = this.getExpectedNumberClassFrequency(); 
		
		int[][] actual = this.model.numberClassFrequency();
		
		int numberOfColumnClasses = 1; // columnIndex = 1 (gewicht)
		int numberOfSplitClasses = 2; // split = geslacht
		for (int i = 0; i < numberOfSplitClasses; i++)
		{
			for (int j = 0; j < numberOfColumnClasses; j++)
			{
				assertEquals("", expected[i][j], actual[i][j]); // frequency
				assertEquals("", expected[i][j + 1], actual[i][j + 1], delta); // selection frequency
			}
		}
	}

	private int[][] getExpectedNumberClassFrequency()
	{
		int numberOfColumnClasses = 1; // columnIndex = 1 (gewicht)
		int numberOfSplitClasses = 2; // split = geslacht

		int[][] expected = new int[numberOfSplitClasses][]; // split = geslacht
		
		int[][] frequencies = {{3}, {7}}; // m, v
		int[][] freqSelection = {{0}, {3}}; // m, v
		
		for (int i = 0; i < numberOfSplitClasses; i++)
		{
			expected[i] = new int[numberOfColumnClasses * 2];
			for (int j = 0; j < numberOfColumnClasses; j++)
			{
				expected[i][j] = frequencies[i][j];
				expected[i][j + 1] = freqSelection[i][j];
			}
		}
		
		return expected;
	}

	@Test
	public void testEnumClassFrequencyNull()
	{
		FrequencyTuple[][] expected = null; // columnIndex = 1 (gewicht), not enum
		FrequencyTuple[][] actual = this.model.enumClassFrequency();
		
		assertArrayEquals("", expected, actual);
	}

	@Test
	public void testEnumClassFrequencyEnum()
	{
		// set enum column index
		this.model.setColumnIndex(4); // columnIndex = 4 (profiel), enum
		
		FrequencyTuple[][] expected = this.getExpectedEnumClassFrequencyEnum(); 
		
		FrequencyTuple[][] actual = this.model.enumClassFrequency();
		
		int numberOfColumnClasses = 4; // columnIndex = 4 (profiel)
		int numberOfSplitClasses = 2; // split = geslacht
		for (int i = 0; i < numberOfSplitClasses; i++)
		{
			for (int j = 0; j < numberOfColumnClasses; j++)
			{
				assertEquals("", expected[i][j].label, actual[i][j].label);
				assertEquals("", expected[i][j].frequency, actual[i][j].frequency, delta);
				assertEquals("", expected[i][j].selectionFrequency, actual[i][j].selectionFrequency, delta);
			}
		}
		
		// reset
		this.model.setColumnIndex(1); // columnIndex = 1 (gewicht)
	}

	private FrequencyTuple[][] getExpectedEnumClassFrequencyEnum()
	{
		int numberOfColumnClasses = 4; // columnIndex = 4 (profiel)
		int numberOfSplitClasses = 2; // split = geslacht

		FrequencyTuple[][] expected = new FrequencyTuple[numberOfSplitClasses][]; // split = geslacht
		
		String[] labels = {"NG", "NT", "EM", "CM"}; // non-alphabetic order: "NG", "NT", "EM", "CM"
		// was CM, EM, NG, NT
		int[][] frequencies = {{3, 0, 0, 0}, {3, 1, 2, 1}}; // m, v
		int[][] freqSelection = {{0, 0, 0, 0}, {1, 1, 0, 1}}; // m, v
		
		for (int i = 0; i < numberOfSplitClasses; i++)
		{
			expected[i] = new FrequencyTuple[numberOfColumnClasses];
			for (int j = 0; j < numberOfColumnClasses; j++)
			{
				expected[i][j] = new FrequencyTuple(labels[j], frequencies[i][j],
					freqSelection[i][j]);
			}
		}
		
		return expected;
	}

	@Test
	public void testEnumClassFrequencyString()
	{
		// set enum column index
		this.model.setColumnIndex(0); // columnIndex = 0 (naam), string
		
		FrequencyTuple[][] expected = this.getExpectedEnumClassFrequencyString(); 
		
		FrequencyTuple[][] actual = this.model.enumClassFrequency();
		
		int numberOfColumnClasses = 10; // columnIndex = 0 (naam)
		int numberOfSplitClasses = 2; // split = geslacht
		for (int i = 0; i < numberOfSplitClasses; i++)
		{
			for (int j = 0; j < numberOfColumnClasses; j++)
			{
//				System.out.println("(" + i + "," + j + ")");
				assertEquals("", expected[i][j].label, actual[i][j].label);
				assertEquals("", expected[i][j].frequency, actual[i][j].frequency, delta);
				assertEquals("", expected[i][j].selectionFrequency, actual[i][j].selectionFrequency, delta);
			}
		}
		
		// reset
		this.model.setColumnIndex(1); // columnIndex = 1 (gewicht)
	}

	private FrequencyTuple[][] getExpectedEnumClassFrequencyString()
	{
		int numberOfColumnClasses = 10; // columnIndex = 0 (naam)
		int numberOfSplitClasses = 2; // split = geslacht

		FrequencyTuple[][] expected = new FrequencyTuple[numberOfSplitClasses][];
		
		String[] labels = {"Ada", "Al", "Mariozee", "Mieke", "Peter", "Sietske", 
			"Susanne", "Sylvia", "Thea", "Wim"}; // alphabetic order
		int[][] frequencies = {{0, 1, 0, 0, 1, 0, 0, 0, 0, 1}, {1, 0, 1, 1, 0, 1, 1, 1, 1, 0}}; // m, v
		int[][] freqSelection = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 1, 0, 1, 0, 1, 0, 0}}; // m, v
		
		for (int i = 0; i < numberOfSplitClasses; i++)
		{
			expected[i] = new FrequencyTuple[numberOfColumnClasses];
			for (int j = 0; j < numberOfColumnClasses; j++)
			{
				expected[i][j] = new FrequencyTuple(labels[j], frequencies[i][j],
					freqSelection[i][j]);
			}
		}
		
		return expected;
	}

	@Test
	public void testGetSplitOptions()
	{
		SplitOptions expected = new SplitOptions();
		expected.setColumnSplitIndex(5); // geslacht
		
		SplitOptions actual = this.model.getSplitOptions();
		
		assertArrayEquals("", 
			expected.getBinBoundaries().toArray(), 
			actual.getBinBoundaries().toArray());
		assertEquals("", 
			expected.getColumnSplitIndex(), 
			actual.getColumnSplitIndex());
		
		int numberOfSplitClasses = 2;
		for (int splitClass = 0; splitClass < numberOfSplitClasses; splitClass++)
		{
			assertEquals("", 
				expected.getSplitClassLabel(splitClass, this.model.getStatTableModel()), 
				actual.getSplitClassLabel(splitClass, this.model.getStatTableModel()));
		}
	}

	@Test
	public void testGetColumnSplitIndex()
	{
		int expected = 5;
		int actual = this.model.getColumnSplitIndex();
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnModeSplit0NoSelection()
	{
		String expected = Statistiek.rb.getString("notAvailable");
		String actual = this.model.getColumnMode(
			1, 0, false); // columnIndex = 1 (gewicht), split 0 (m), no selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnModeSplit1NoSelection()
	{
		String expected = "55";
		String actual = this.model.getColumnMode(
			1, 1, false); // columnIndex = 1 (gewicht), split 1 (v), no selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnModeSplit0Selection()
	{
		String expected = Statistiek.rb.getString("notAvailable");
		String actual = this.model.getColumnMode(
			1, 0, true); // columnIndex = 1 (gewicht), split 0 (m), selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnModeSplit1Selection()
	{
		String expected = Statistiek.rb.getString("notAvailable");
		String actual = this.model.getColumnMode(
			1, 1, true); // columnIndex = 1 (gewicht), split 1 (v), selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnMaxSplit0NoSelection()
	{
		String expected = "70";
		String actual = this.model.getColumnMax(
			1, 0, false); // columnIndex = 1 (gewicht), split 0 (m), no selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnMaxSplit1NoSelection()
	{
		String expected = "56";
		String actual = this.model.getColumnMax(
			1, 1, false); // columnIndex = 1 (gewicht), split 1 (v), no selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnMaxSplit0Selection()
	{
		String expected = Statistiek.rb.getString("notAvailable");
		String actual = this.model.getColumnMax(
			1, 0, true); // columnIndex = 1 (gewicht), split 0 (m), selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnMaxSplit1Selection()
	{
		String expected = "55";
		String actual = this.model.getColumnMax(
			1, 1, true); // columnIndex = 1 (gewicht), split 1 (v), selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnMinSplit0NoSelection()
	{
		String expected = "51";
		String actual = this.model.getColumnMin(
			1, 0, false); // columnIndex = 1 (gewicht), split 0 (m), no selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnMinSplit1NoSelection()
	{
		String expected = "40";
		String actual = this.model.getColumnMin(
			1, 1, false); // columnIndex = 1 (gewicht), split 1 (v), no selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnMinSplit0Selection()
	{
		String expected = Statistiek.rb.getString("notAvailable");
		String actual = this.model.getColumnMin(
			1, 0, true); // columnIndex = 1 (gewicht), split 0 (m), selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnMinSplit1Selection()
	{
		String expected = "40";
		String actual = this.model.getColumnMin(
			1, 1, true); // columnIndex = 1 (gewicht), split 1 (v), selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnMeanSplit0NoSelection()
	{
		String expected = "59";
		String actual = this.model.getColumnMean(
			1, 0, false); // columnIndex = 1 (gewicht), split 0 (m), no selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnMeanSplit1NoSelection()
	{
		double expected = 51.86;
		double actual = Statistiek.round(Statistiek.parseDouble(this.model.getColumnMean(
			1, 1, false)), 2); // columnIndex = 1 (gewicht), split 1 (v), no selection
		
		assertEquals("", expected, actual, delta);
	}

	@Test
	public void testGetColumnMeanSplit0Selection()
	{
		String expected = Statistiek.rb.getString("notAvailable");
		String actual = this.model.getColumnMean(
			1, 0, true); // columnIndex = 1 (gewicht), split 0 (m), selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnMeanSplit1Selection()
	{
		String expected = "49";
		String actual = this.model.getColumnMean(
			1, 1, true); // columnIndex = 1 (gewicht), split 1 (v), selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnSDSplit0NoSelection()
	{
		Double expected = 8.04;
		Double actual = Statistiek.round(Statistiek.parseDouble(this.model.getColumnSD(
			1, 0, false)), 2); // columnIndex = 1 (gewicht), split 0, no selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnSDSplit1NoSelection()
	{
		Double expected = 5.11;
		Double actual = Statistiek.round(Statistiek.parseDouble(this.model.getColumnSD(
			1, 1, false)), 2); // columnIndex = 1 (gewicht), split 1, no selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnSDSplit0Selection()
	{
		String expected = Statistiek.rb.getString("notAvailable");
		String actual = this.model.getColumnSD(1, 0, true); // columnIndex = 1 (gewicht), split 0, selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnSDSplit1Selection()
	{
		Double expected = 6.48;
		Double actual = Statistiek.round(Statistiek.parseDouble(this.model.getColumnSD(
			1, 1, true)), 2); // columnIndex = 1 (gewicht), split 1, selection
		
		assertEquals("", expected, actual);
	}

	@Test
	public void testGetColumnMedianSplit1Selection()
	{
		Double expected = 52.0;
		Double actual = Statistiek.round(this.model.getColumnMedian(
			1, 1, true), 2); // columnIndex = 1 (gewicht), split 1, selection
		
		assertEquals("", expected, actual);
	}

}
