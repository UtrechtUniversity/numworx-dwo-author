package fi.statistiek.histogram;

import java.util.ArrayList;

import fi.statistiek.StatTableModel;

public interface StatBinsModel {
	public StatTableModel getTableModel();
	public int getColumnIndex();
	public ArrayList<Double> getBinBoundaries();
}
