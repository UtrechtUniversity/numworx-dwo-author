package fi.wiskopdr.tekstobjects;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import fi.beans.base64code.StringCodeObject;
import fi.wiskopdr.WiskOpdr;

public class ShareAction extends AbstractAction {

		public static final String SHARE_KEY = "shareKey";
		private static final long serialVersionUID = 1L;
		public static final String SHARE_MAP = "shareMap";
		private static final String SOORT = "soortInteractiePanel";
		private EditInteractiePanelDialog dialog;
		private Icon unshare,share;

		public ShareAction(Icon unshare, EditInteractiePanelDialog dialog) {
			super(null, unshare);
			this.dialog = dialog;
			this.unshare = unshare;
			this.share = new ImageIcon(WiskOpdr.loadImage("resources/share.png"));
			this.localMap = new Hashtable<String, Hashtable<String,Object>>(shareMap);
		}
		
		void setShared(boolean b, String object)
		{
			putValue(LARGE_ICON_KEY, b?share:unshare);
			putValue(SHORT_DESCRIPTION, object);
		}

		private String shareKey = "";
		private static Hashtable<String,Hashtable<String,Object>> shareMap = new Hashtable<String, Hashtable<String, Object>>();
		private Hashtable<String,Hashtable<String,Object>> localMap;
		private static Hashtable<String,Object> stateMap = new Hashtable<String, Object>();
		private static boolean sharingPossible;
				
		@Override
		public void actionPerformed(ActionEvent e) {
			Object message = createComponent();
			String title = WiskOpdr.rb.getString(SHARE_MAP);
			Object[] options = new Object[] { 
					WiskOpdr.rb.getString("okKnopLabel"), //"Ok"
					WiskOpdr.rb.getString("deletePageMenuItem"), //"Delete", 
					WiskOpdr.rb.getString("annuleerKnopLabel") // "Annuleer" 
					};
			int ok = JOptionPane.showOptionDialog(dialog, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
			String result;
			if(ok == JOptionPane.NO_OPTION) result = "";
			else if(ok == JOptionPane.CANCEL_OPTION) result = null;
			else result = (String) shares.getSelectedItem();
			if(result != null &&!shareKey.equals(result) ) {
				shareKey = result;
				if(shareKey.isEmpty())
				{
					setShared(false, null);
					return;
				}
				Hashtable<String, Object> map = localMap.get(shareKey);
				if(map == null) {
// save state, not if state is empty
					if(dialog.soortAntwoordVakKeuze.getSelectedIndex()>0)
						putEditState(dialog.getEditState_int());
					else
					{
						setShared(false, null);
						shareKey = "";
					}
				} else {
					setEditState(map);	
				}
				setShared(true, shareKey);
			}
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private void putEditState(Hashtable map) {
			map.remove(SHARE_KEY);
			map.remove("subscriptions");
			map.remove("crossWidgetId");
			// XXX more?
			//shareMap.put(shareKey, deepClone(map));
			localMap.put(shareKey, deepClone(map));
		}

		void commit() {
			if(shareKey != null && !shareKey.isEmpty()) {
				shareMap.put(shareKey, localMap.get(shareKey));
			} 
		}
		
		
		@SuppressWarnings("unchecked")
		private <T extends Hashtable<String,?>> T deepClone(T map) {
			String s = StringCodeObject.encodeObjectToString(map);
			if(s != null)
			{
				Object o = StringCodeObject.decodeStringToObject(s);
				if(o instanceof Hashtable) return (T) o;
			}
			return map;
		}

		void setEditState(Hashtable<String, Object> map) {
			map = deepClone(map);
			if(dialog.subscriptions!= null)
				map.put("subscriptions", dialog.subscriptions);
			map.remove("crossWidgetId");
			
			dialog.soortAntwoordVakKeuze.setSelectedIndex(0); // void interactie panel
			int setNr = ((Number) map.get("setNr")).intValue();
			dialog.soortAntwoordVakKeuze.removeAllItems();
			dialog.initSet(setNr);
// prepare setNr...
			dialog.addInteractieEditPanel(map,999);
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Hashtable wrap(Hashtable map) {
			int soort = ((Number) map.get(SOORT)).intValue();
			if(soort != -1 && !shareKey.isEmpty())
			{
				putEditState(map);
				commit();
				map.put(SHARE_KEY, shareKey);
			}
			return map;
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		Hashtable unwrap(Hashtable map) {
			shareKey = "";
			setShared(false, null);
			if(map == null) return map;
			String key = (String) map.get(SHARE_KEY);
			if(key != null && !key.isEmpty()) {
				Hashtable other = shareMap.get(key);
				if(other != null) {
					shareKey = key;
					localMap.put(key, other);
					map.putAll(other);
					setShared(true, key);
				}
			}
			return map;
		}

		static private String getShareKey(Hashtable<String, ?> map) {
			if(map == null) return null; // Should not happen, but it does
			String s = (String) map.get(SHARE_KEY);
			if(s == null || !shareMap.containsKey(s))
				return null;
			return s;
		}
		
		static Set<String> getShareKeySet() {
			if(shareMap == null) 
				return Collections.emptySet();
			return new TreeSet<String> (shareMap.keySet());
		}
		
		public static void setSharingPossible(boolean b ) {
			sharingPossible = b;
		}
		
		public static boolean getSharingPossible() {
			return sharingPossible;
		}
		
		public static boolean getSharingIsUsed() {
			return (shareMap != null && !shareMap.isEmpty());
		}
		
		private Set<String> getLocalKeySet() {
			return new TreeSet<String> (localMap.keySet());
		}
		
		private JComboBox<String> shares;
		private JComponent createComponent() {
			Box vbox = Box.createVerticalBox();
			JLabel label = new JLabel(WiskOpdr.rb.getString(SHARE_KEY));
			label.setAlignmentX(0);
			vbox.add( label);
			shares = new JComboBox<String>(new Vector<String>(getLocalKeySet()));
			shares.setEditable(true);
			shares.setSelectedItem(shareKey);
			shares.setAlignmentX(0);
			vbox.add(shares);
			
			return vbox;
		}
		
		
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
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
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
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
		
		@SuppressWarnings("unchecked")
		public static void init(String string) {
			Hashtable<String, Hashtable<String,Object>> shareMap = (Hashtable<String, Hashtable<String, Object>>) StringCodeObject.decodeStringToObject(string);
			if(shareMap == null) shareMap = new Hashtable<String, Hashtable<String, Object>>();
			ShareAction.shareMap = shareMap;
			setSharedState(null);
		}
		
		
		
		
		public static void prepareLaunchData(Iterator<? extends Map<?,?>> set) {
			Collection<String> keys = new HashSet<String>();
			while (set.hasNext()) {
				Map<?, ?> map = set.next();
				String key = (String) map.get(SHARE_KEY);
				keys.add(key);
			}
			Set<String> shareKeys = shareMap.keySet();
			shareKeys.retainAll(keys);
		}
		
		public static String getSharedLaunchData() {
			if(shareMap == null || shareMap.isEmpty()) return null;
			return StringCodeObject.encodeObjectToString(shareMap);
		}

		public static void setSharedState(Hashtable<String,Object> stateMap) {
			if(stateMap == null) stateMap = new Hashtable<String, Object>();
			ShareAction.stateMap = stateMap;
			support = new PropertyChangeSupport(stateMap);
		}
		
		public static void clearSharedState() {
			if(stateMap != null) {
				stateMap.clear();
			}
		}

		public static Hashtable<String,Object> getSharedState() {
			if(stateMap == null||stateMap.isEmpty())
				return null;
			return stateMap;
		}
		
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		static Hashtable wrapState(Hashtable map, Hashtable state) {
			String key = getShareKey(map);
			if(key != null) {
				putStateValue(key, state);
				//if(state != null)state = new Hashtable(); don't!!!
			}
			return state;
		}
		
	}