package fi.statistiek.histogram;

import java.util.ArrayList;

import fi.statistiek.StatTableModel;

public interface StatBinsModel
{
	public StatTableModel getStatTableModel();

	public int getColumnIndex();

	public ArrayList<Number> getBinBoundaries();
}
