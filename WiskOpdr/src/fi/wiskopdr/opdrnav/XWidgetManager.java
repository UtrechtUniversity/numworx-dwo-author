package fi.wiskopdr.opdrnav;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.wiskopdr.InteractiePanelContainerIF;
import fi.wiskopdr.tekstobjects.BasisTekstVak;
import fi.wiskopdr.tekstobjects.EditInteractiePanelDialog;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak;
import fi.wiskopdr.tekstobjects.TekstInteractiePanelVak.Connector;
import fi.wiskopdr.tekstobjects.TekstVak;

/**
 * Singleton pattern?
 * @author wim
 *
 */
public class XWidgetManager {

	public interface HasWidgetManager {
		XWidgetManager getXWidgetManager();
	}

	protected XWidgetManager() {};
	
	public XWidgetManager(BasisTekstVak basisTekstVak) {
		setBasisVak(basisTekstVak);
	}

	Map<String, TekstInteractiePanelVak> widgets = new HashMap<String, TekstInteractiePanelVak>();
	BasisTekstVak basisVak;

	public TekstInteractiePanelVak getWidgetContainer(String key) {
		TekstInteractiePanelVak result = widgets.get(key);
		if(result == null)
		{
			result = basisVak.getWidgetContainer(key);
			widgets.put(key, result);
		}
		return result;
	}

	public void clear() {
		widgets.clear();
	}
	
	/**
	 * @return the basisVak
	 */
	public BasisTekstVak getBasisVak() {
		return basisVak;
	}

	/**
	 * @param basisVak the basisVak to set
	 */
	public void setBasisVak(BasisTekstVak basisVak) {
		this.basisVak = basisVak;
	}

	public void setCrossWidgetView(
			TekstInteractiePanelVak tekstInteractiePanelVak) {

		basisVak.setCrossWidgetView(tekstInteractiePanelVak);
		if(tekstInteractiePanelVak.getCrossWidgetId0() != null)
			updateCrossWidgetId(tekstInteractiePanelVak);
	}

	public void updateCrossWidgetId(
			TekstInteractiePanelVak tekstInteractiePanelVak) {
		String key = tekstInteractiePanelVak.getCrossWidgetId(this);
		widgets.put(key, tekstInteractiePanelVak);
	}
	
	public String getWidgetID() {
		Set<String> pool = keySet();
 		String uniq = getUUID(pool);
 		widgets.put(uniq, null);
 		return uniq;
	}

	public void newCrossWidgetId(TekstInteractiePanelVak tekstInteractiePanelVak) {
		Set<String> pool = keySet();
 		String uniq = getUUID(pool);
		tekstInteractiePanelVak.setCrossWidgetId(uniq);
		widgets.put(uniq, tekstInteractiePanelVak);	
	}

	public void newCrossWidgetId(
			EditInteractiePanelDialog editInteractiePanelDialog) {
		Set<String> pool = keySet();
 		String uniq = getUUID(pool);
 		editInteractiePanelDialog.setCrossWidgetId(uniq);
		widgets.put(uniq, null);	
	}

	/**
	 * @param pool
	 * @return
	 */
	protected String getUUID(Set<String> pool) {
		String uniq;
		do {
			uniq = Long.toHexString(Double.doubleToLongBits(Math.random()));
		} while(pool.contains(uniq));
		return uniq;
	}

	public void remove(TekstInteractiePanelVak tekstInteractiePanelVak) {
		if(tekstInteractiePanelVak == widgets.get(tekstInteractiePanelVak.getCrossWidgetId0()))
				widgets.remove(tekstInteractiePanelVak.getCrossWidgetId0());
		
	}

	public Set<String> keySet() {
		return widgets.keySet();
	}

	public boolean isEmpty() {
		return widgets.isEmpty();
	}

	public void renameDups(Set<String> keySet) {
		keySet = new HashSet<String>(keySet);
		Map<String,String> oldnew = new HashMap<String,String>();
		Set<Map.Entry<String, TekstInteractiePanelVak>> entrySet = widgets.entrySet();
		for (Map.Entry<String,TekstInteractiePanelVak> entry: entrySet) {
			String oldKey = entry.getKey();
			String newKey = oldKey;
			if(keySet.contains(oldKey))
				newKey = getUUID(keySet);
			keySet.add(newKey);
			oldnew.put(oldKey, newKey);
			entry.getValue().setCrossWidgetId(newKey);
			entry.getValue().getConnections().clear(); // TODO !!	
		}

		List<?> vector = basisVak.geefInteractiePanels();
		renameSenders(oldnew, vector);
	}

	private void renameSenders(Map<String, String> oldnew, List<?> vector) {
		for(Object e: vector)
		{
			if(e instanceof InteractiePanelContainerIF) {
				List<?> recurse = ((InteractiePanelContainerIF) e).geefInteractiePanels();
				if(recurse != null && ! recurse.isEmpty())
					renameSenders(oldnew, recurse);
			}
			if (e instanceof TekstInteractiePanelVak) {
				TekstInteractiePanelVak vak = (TekstInteractiePanelVak)e;
			
			Map<String, Set<Connector>> map = vak.getSubscriptions();
			if(map != null)
				for(Set<Connector> set : map.values()) {
				for(Connector c: set) {
					String key = c.getKey();
					String renameKey = oldnew.get(key);
					if (renameKey == null)
						continue;
					String value = c.getValue();
					System.out.println("key = " + key + ", value = " + value + " , newkey = " + renameKey);
					c.setKey(renameKey);
				}
			}}
		}
	}

	
	
	
	
}
