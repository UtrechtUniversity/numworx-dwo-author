package fi.wiskopdr.tekstobjects;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import fi.beans.base64code.StringCodeObject;
import fi.wiskopdr.WiskOpdr;

public class ShareAction extends AbstractAction {

		private static final String SHARE_KEY = "shareKey";
		private static final long serialVersionUID = 1L;
		public static final String SHARE_MAP = "shareMap";
		private EditInteractiePanelDialog dialog;
		private Icon unshare,share;

		public ShareAction(Icon unshare, EditInteractiePanelDialog dialog) {
			super(null, unshare);
			this.dialog = dialog;
			this.unshare = unshare;
			this.share = new ImageIcon(WiskOpdr.loadImage("resources/share.png"));
		}
		
		void setShared(boolean b)
		{
			putValue(this.LARGE_ICON_KEY, b?share:unshare);
		}

		private String shareKey = "";
		private static Hashtable<String,Hashtable<String,Object>> shareMap = new Hashtable();
		private static Hashtable<String,Object> stateMap = new Hashtable();
				
		@SuppressWarnings("unchecked")
		@Override
		public void actionPerformed(ActionEvent e) {
			Object message = SHARE_KEY;
			String result = JOptionPane.showInputDialog(dialog, message, shareKey);
			if(result != null &&!shareKey.equals(result) ) {
				shareKey = result;
				if(shareKey.isEmpty())
				{
					setShared(false);
					return;
				}
				Hashtable<String, Object> map = shareMap.get(shareKey);
				if(map == null) {
					putEditState(dialog.getEditState_int());
				} else {
					setEditState(map);	
				}
				setShared(true);
			}
		}

		private void putEditState(Hashtable map) {
			map.remove(SHARE_KEY);
			map.remove("subscriptions");
			map.remove("crossWidgetId");
			// XXX more?
			shareMap.put(shareKey, deepClone(map));
		}

		@SuppressWarnings("rawtypes")
		private Hashtable deepClone(Hashtable map) {
			String s = StringCodeObject.encodeObjectToString(map);
			if(s != null)
			{
				Object o = StringCodeObject.decodeStringToObject(s);
				if(o instanceof Hashtable) return (Hashtable) o;
			}
			return map;
		}

		void setEditState(Hashtable<String, Object> map) {
			map = deepClone(map);
			map.put("subscriptions", dialog.subscriptions);
			map.remove("crossWidgetId");
			
			dialog.soortAntwoordVakKeuze.setSelectedIndex(0); // void interactie panel
			int setNr = ((Number) map.get("setNr")).intValue();
			dialog.soortAntwoordVakKeuze.removeAllItems();
			dialog.initSet(setNr);
// prepare setNr...
			dialog.addInteractieEditPanel(map);
		}
		
		Hashtable wrap(Hashtable map) {
			if(!shareKey.isEmpty())
			{
				putEditState(map);
				map.put(SHARE_KEY, shareKey);
			}
			return map;
		}
		
		Hashtable unwrap(Hashtable map) {
			shareKey = "";
			setShared(false);
			if(map == null) return map;
			String key = (String) map.get(SHARE_KEY);
			if(key != null && !key.isEmpty()) {
				Hashtable other = shareMap.get(key);
				if(other != null) {
					shareKey = key;
					map.putAll(other);
					setShared(true);
				}
			}
			return map;
		}

		static private String getShareKey(Hashtable map) {
			String s = (String) map.get(SHARE_KEY);
			if(s == null || !shareMap.containsKey(s))
				return null;
			return s;
		}
		
		static Hashtable unwrapLaunchData(Hashtable map) {
			String key = (String)map.get(SHARE_KEY);
			if(key != null && !key.isEmpty()) {
				Hashtable other = shareMap.get(key);
				if(other != null) {
					map.putAll(other);
				}
			}
			return map;
		}
		
		static Hashtable unwrapState(Hashtable map, Hashtable state) {
			String key = getShareKey(map);
			if(state == null) state = new Hashtable();
			if(key == null) return state;
			Hashtable value = (Hashtable) stateMap.get(key);
			if(value != null)
			{
				state.putAll(value);
			}
			return state;
		}
				
		private static PropertyChangeSupport support = new PropertyChangeSupport(stateMap);

		public static void putStateValue(String key, Object value) {
			if(key == null) return;
			Object old;
			if(value == null) {
				old = stateMap.remove(key);
			} else 
				old = stateMap.put(key, value);
			support.firePropertyChange(key, old, value);
		}

		public static Object getStateValue(String key) {
			if(key == null) return null;
			return stateMap.get(key);
		}
			
		public void addPropertyChangeListener(PropertyChangeListener listener, String key) {
			support.addPropertyChangeListener(key, listener);
		}
		
		public void removePropertyChangeListener(PropertyChangeListener listener, String key) {
			support.removePropertyChangeListener(key, listener);
		}
		
		public static void init(String string) {
			Hashtable shareMap = (Hashtable) StringCodeObject.decodeStringToObject(string);
			if(shareMap == null) shareMap = new Hashtable();
			ShareAction.shareMap = shareMap;
			setSharedState(null);
		}
		
		public static String getSharedLaunchData() {
			if(shareMap == null || shareMap.isEmpty()) return null;
			return StringCodeObject.encodeObjectToString(shareMap);
		}

		public static void setSharedState(Hashtable stateMap) {
			if(stateMap == null) stateMap = new Hashtable();
			ShareAction.stateMap = stateMap;
			support = new PropertyChangeSupport(stateMap);
		}
		
		public static void clearSharedState() {
			if(stateMap != null) {
				stateMap.clear();
			}
		}

		public static Hashtable getSharedState() {
			if(stateMap == null||stateMap.isEmpty())
				return null;
			return stateMap;
		}

		public Hashtable getShareMap() {
			return shareMap;
		}
		
		
		static Hashtable wrapState(Hashtable map, Hashtable state) {
			String key = getShareKey(map);
			if(key != null) {
				putStateValue(key, state);
				//if(state != null)state = new Hashtable(); don't!!!
			}
			return state;
		}
		
	}