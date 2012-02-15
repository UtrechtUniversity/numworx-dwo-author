package fi.grafiek3dtest.formuleobjects;


public interface TabletOwner {
	
	public void zetTablet(FormuleVakHouder formuleVakHouder, int x, int y);
	
	public void addTablet(FormuleVakHouder formuleVakHouder, int x, int y);
	
	public void removeTablet();
	
	public void zetTabletUser(FormuleVakHouder formuleVakHouder);
	
	public Tablet getTablet();
	
}
