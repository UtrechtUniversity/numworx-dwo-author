package fi.statistiek;

/**
 * Interface for classes that listen to selection of StatTableModel.
 * 
 * @author Manu Drijvers, Sylvia van Borkulo
 *
 */
public interface SelectionListener {
	
	/**
	 * Called when selection is changed
	 */
	public void selectionChanged();
	
	/**
	 * Called when the set of marked outliers is changed
	 */
	public void outliersChanged();
}
