package fi.beans.iconan;

import java.applet.Applet;
import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import fi.beans.iconan.text.Text;

@SuppressWarnings("serial")
public class Iconan extends JPanel implements ActionListener, FocusListener, ListSelectionListener {

	private Component component;
	private Hashtable<String,Object> namemap;
	private Hashtable<String,Image> imagemap;
	private ActionListener al;
	private JButton newBtn, okBtn, cancelBtn, rmBtn, urlBtn, chngBtn;
	private JTextField widthField, heightField;
	int previewWidth = 32, previewHeight = 32;
	private JPanel previewCanvas = new JPanel() { 
		
		public void paint(Graphics g) {
			if(preview != null)
			{
				int w = previewWidth;
				int h = previewHeight;
// scale down to fit.
				if(w > getWidth())
				{
					h = h * getWidth()/w;
					w = getWidth();
				}
				if (h > getHeight())
				{
					w = w * getHeight()/h;
					h = getHeight();
				}
				int x = (getWidth() - w)/2;
				int y = (getHeight() - h)/2;

				if(w < getWidth() || h < getHeight())
				{
					g.setColor(getBackground());
					g.fillRect(0, 0, getWidth(), getHeight());
				}
				
				g.drawImage(preview, x, y, w, h, getBackground(), this);
			}
			else {
				g.setColor(Color.green);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		}
	};
	
	private Applet applet;
	
	class MyListRenderer extends DefaultListCellRenderer implements Icon {

		private Image image;

		public int getIconHeight() {
			return 16;
		}

		public int getIconWidth() {
			return 16;
		}
// TODO w en h in verhouding
		public void paintIcon(Component c, Graphics g, int x, int y) {
			if(image != null)
				g.drawImage(image, x, y, getIconWidth(), getIconHeight(), list);
		}

		public Component getListCellRendererComponent(JList<String> list, String value,
				int index, boolean isSelected, boolean cellHasFocus) {
			String item = value;
			image = getImage(item);
			Object u = namemap.get(item + "/u");
			//if(u == null)
			//	u = namemap.get(item + "/f");
			if(u != null)
				item = item + " " + u;
			super.getListCellRendererComponent(list, item, index, isSelected,
					cellHasFocus);
			if(image != null)
				setIcon(this);
			return this;
		}
	}
	
	private boolean setSize(Object source)
	{
		if(source==widthField)
		{
			previewWidth = Integer.parseInt(widthField.getText());
			String name = previewName;
			if(name != null)
				namemap.put(name + "/w", new Integer(previewWidth));
			previewCanvas.repaint();
			return true;
		}
		if(source==heightField)
		{
			previewHeight = Integer.parseInt(heightField.getText());
			String name = previewName;
			if(name != null)
				namemap.put(name + "/h", new Integer(previewWidth));
			previewCanvas.repaint();
			return true;
		}
		return false;
	}
	
	
	public void actionPerformed(ActionEvent e) {
		
		if(setSize(e.getSource()))
			return;
		
		if(e.getSource()==chngBtn)
		{
			String selected = (String) list.getSelectedValue();
			if(null != selected)
				try {
					edit(selected);
					int index = list.getSelectedIndex();
					dataModel.setElementAt(selected, index);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		} else
		
		if(e.getSource()==urlBtn)
		{
			try {
				insert(newURLImage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else
		if(e.getSource()==newBtn)
		{
			try {
				insert(newImage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource()==okBtn)
		{
			if(al != null)
			{
				String selected = (String) list.getSelectedValue();
				if(null != selected) {
					namemap.put(selected + "/w", new Integer(widthField.getText()));
					namemap.put(selected + "/h", new Integer(heightField.getText()));
				} else {
					selected = "";
				}				
				al.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, selected));
			}
		} else if(e.getSource()==cancelBtn)
		{
			if(al != null)
			{
				al.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
			}				
		} else if(e.getSource()==getComponent())
		{
			rebuildList();
		} else if (e.getSource()==rmBtn)
		{
			String selected = (String) list.getSelectedValue();
			if(null != selected)
			{
				dataModel.remove(list.getSelectedIndex());
				previewName = null;
				setPreview(null);
				imagemap.remove(selected);
				namemap.remove(selected + "/w");
				namemap.remove(selected + "/h");
				namemap.remove(selected + "/u");
				namemap.remove(selected + "/f");
				namemap.remove(selected + "/t");
				namemap.remove(selected);
			}
		}

	}


	private void edit(String selected) throws IOException {
		String su = (String) namemap.get(selected + "/u");
		if(su != null)
		{
			String filename = (String) JOptionPane.showInputDialog(this, rb.getString(Text.EDIT_URL), rb.getString(Text.WIJZIG), JOptionPane.QUESTION_MESSAGE, null, null, su);
			if(filename != null)				// "URL van plaatje"
			{
				last = filename;
				URL u = new URL(getCDN(), filename);
		// with imageredirector
				URLConnection uc = openConnection(u);
				String type = uc.getContentType();
				if( !type.startsWith("image/" ))
					throw new IOException(u + ":" + type);
				byte[] data = EMPTY;
				namemap.put(selected, data);
				namemap.remove(selected + "/w");
				namemap.remove(selected + "/h");
		// als 'u' niet begint met codebase, bewaar volledige naam
				namemap.put(selected + "/u", getURI(u));
				selectPreview(selected);
			}
			return;
		}
// by file
		URI suri = (URI) namemap.get(selected + "/f");
		if(suri != null)
		{
			if(fd == null)
			{
				fd = new JFileChooser();
			}
			fd.setSelectedFile(new File(suri));
			String t = fd.getDialogTitle();
			fd.setDialogTitle(selected + ": " + suri.getPath());
			int result = fd.showOpenDialog(this);
			fd.setDialogTitle(t);
			if( result != JFileChooser.APPROVE_OPTION)
				return;
			File file = fd.getSelectedFile();
			URL u = file.toURI().toURL();
			URLConnection uc = openConnection(u);
			String type = uc.getContentType();
			if(! type.startsWith("image/"))
				throw new IOException(file + ":" + type);

			InputStream fis = uc.getInputStream();
			int len = fis.available();
			byte[] data = new byte[len];
			fis.read(data);
			fis.close();
			// strip extension.
			namemap.put(selected, data);
			namemap.remove(selected + "/w");
			namemap.remove(selected + "/h");
			namemap.remove(selected + "/u");
			namemap.put(selected + "/f", file.toURI());
			selectPreview(selected);
			return;
		}
		
		
	}


	private void insert(String name) {
		if(name != null)
		{
			list.setSelectedIndex(add(name));
			selectPreview(name);
		}
	}


	public void selectPreview(String name) {
		previewName = name;
		setPreview(getImage(name));
		setSizes(name);
	}

	/**
	 * @param name
	 */
	private void setSizes(String name) {
		previewWidth = getWidth(name);
		widthField.setText(String.valueOf(previewWidth));
		previewHeight = getHeight(name);
		heightField.setText(String.valueOf(previewHeight));
	}

	private void rebuildList() {
		list.removeAll();
		Enumeration<String> keys = namemap.keys();
		while (keys.hasMoreElements()) {
			String string = keys.nextElement();
			if(string.indexOf('/')<0)
				add(string);
		}
	}

	private int add(String string) {
		for(int i = 0; i < dataModel.getSize(); i++)
		{
			String item = (String) dataModel.getElementAt(i);
			if(item.equalsIgnoreCase(string))
			{
				return i;
			}			
			if(item.compareToIgnoreCase(string)>0)
			{
				dataModel.add(i, string);
				return i;
			}
		}
		dataModel.addElement(string);
		return dataModel.getSize()-1;
	}
	/**
	 * Gooi item weg uit list. 
	 * Catch IllegalArgumentException uit {@link java.awt.List#remove(String)}
	 * @param item to remove
	 */
	private void remove(String item) {
		try {
			DefaultListModel<String> model = dataModel;
			model.removeElement(item);
		} catch (IllegalArgumentException e) {
		}
	}
	
	static final private byte[] EMPTY = new byte[0];
	public void removeAllData() {
		Enumeration<String> iter = namemap.keys();
		while (iter.hasMoreElements()) {
			String key = iter.nextElement();
			if(key.indexOf('/')<0)
				namemap.put(key, EMPTY);
		}
	}

	public Image getImage(String name)
	{
		byte[] data = (byte[]) namemap.get(name);
		if(data == null)
			return null;
		Image result = imagemap.get(name);
		if(result != null)
			return result;

		String mime = (String) namemap.get(name + "/t");
		if( mime.contains("svg")) return null; // No preview
		
		if(data.length==0 && namemap.containsKey(name + "/u"))
		{
			try {
				URL url = new URL(getCDN(), namemap.get(name + "/u").toString());
				result = getImage(url);
			} catch (MalformedURLException e) {
				return null;
			}
			return result;
		} else
		if(data.length==0 && namemap.containsKey(name + "/f"))
		{
			try {
				URL url = ((URI)namemap.get(name + "/f")).toURL();
				
				result = getImage(url);
			} catch (MalformedURLException e) {
				return null;
			}
		}	
		else
			result = getToolkit().createImage(data);
		if(result != null)
			imagemap.put(name, result);
		return result;
	}


	static final private String redirect = "/servlet/fi.servlet.imageredirector.ImageRedirector?url=";


	/**
	 * Fixing JDK bug. No redirects allowed in Toolkit.getImage(URL).
	 * @param url
	 * @return same url
	 */
	private static URL getFinalURL(URL url) {
		if(url.getProtocol().startsWith("http"))
	    try {
			HttpURLConnection con = (HttpURLConnection) (url).openConnection();
			con.setInstanceFollowRedirects(false);
			con.connect();
			InputStream is = con.getInputStream();
			int responseCode = con.getResponseCode();
// 301, 302, 303?, 307?
			if (responseCode == HttpURLConnection.HTTP_MOVED_PERM 
					|| responseCode == HttpURLConnection.HTTP_MOVED_TEMP
// newer codes:
					|| responseCode == 303 || responseCode == 307
					) {
			    String location = con.getHeaderField("Location");
			    is.close();
				URL redirectUrl = new URL(url, location);
			    return getFinalURL(redirectUrl);
			}
			is.close();
		} catch (Exception e) {
			java.util.logging.Logger.getLogger("fi.beans.iconan.Iconan").severe("getFinalURL " + url + ":" + e);
		}
	    return url;
	}
	
	
	/**
	 * Met imageredirector als fallback.
	 * @param url
	 * @return an Image
	 * @throws MalformedURLException
	 */
	private Image getImage0(URL url) throws MalformedURLException {
		try {
			url = getFinalURL(url);
			//if(true)throw new SecurityException();
			return getToolkit().getImage(url);
		} catch(RuntimeException e)
		{
			url = redirect(url);
			return getToolkit().getImage(url);
		}
	}
	private Image getImage(URL url) throws MalformedURLException {
		Image img = getImage0(url);
		if(img == null)
		{
			String last =  url.getPath();
			int l = last.lastIndexOf('/');
			if(l >= 0)
				last = last.substring(l+1);
			url = new URL(getDocumentBase(), last);
			img = getImage0(url);
		}
		return img;
	}
	
	
	

	private URL redirect(URL url) throws MalformedURLException {
		try {
			return new URL(getCodeBase(), redirect + URLEncoder.encode(url.toExternalForm(), "ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			throw new MalformedURLException(e.toString());
		}
	}

	class NameObserver implements ImageObserver {
		String name;

		public boolean imageUpdate(Image img, int infoflags, int x, int y,
				int width, int height) {
			if((infoflags & (ImageObserver.ABORT|ImageObserver.ERROR)) != 0)
			{	
System.err.println("Error in imageUpdate " + name + " flag = " + infoflags);				
				synchronized(Iconan.this) {
					imagemap.remove(name);
					namemap.remove(name);
					namemap.remove(name +"/w");
					namemap.remove(name +"/h");
					namemap.remove(name +"/u");
					namemap.remove(name +"/f");
					Iconan.this.remove(name);
					Iconan.this.notifyAll();
				}
				return false;
			}
			synchronized(Iconan.this) {
				if((infoflags & ImageObserver.WIDTH) != 0)
				{
					namemap.put(name + "/w", new Integer(width));
					if(img == preview)
					{
						previewWidth = width;
						widthField.setText(String.valueOf(width));
						previewCanvas.repaint();
					}
					Iconan.this.notifyAll();
				}
				if((infoflags & ImageObserver.HEIGHT) != 0)
				{
					namemap.put(name + "/h", new Integer(height));
					if(img == preview)
					{
						previewHeight = height;
						heightField.setText(String.valueOf(height));
						previewCanvas.repaint();
					}
					Iconan.this.notifyAll();
				}
			}
			return true;
		}

		/**
		 * @param name
		 */
		NameObserver(String name) {
			this.name = name;
		}
		
	}
	
	
	public synchronized int getWidth(String name)
	{
		Integer w = (Integer) namemap.get(name + "/w");
		if(w != null)
			return w.intValue();
		Image img = getImage(name);
		if(img != null)
			return getWidth(name, img);
		return -1;
	}

	/**
	 * @param name
	 * @param img
	 * @return
	 */
	private synchronized int getWidth(String name, Image img) {
		int result = -1;
		while ( inNamemap(name) && (result = img.getWidth(new NameObserver(name))) < 0 && inNamemap(name) )
		{
			try {
				wait();
			} catch (InterruptedException e) {
				return result;
			}
		}	
		return result;
	}

	private boolean inNamemap(String name) {
		return namemap.containsKey(name);
	}


	public synchronized int getHeight(String name)
	{
		Integer w = (Integer) namemap.get(name + "/h");
		if(w != null)
			return w.intValue();
		Image img = getImage(name);
		if(img != null)
			return getHeight(name, img);
		return -1;
	}

	/**
	 * @param name
	 * @param img
	 * @return
	 */
	private synchronized int getHeight(String name, Image img) {
		int result = -1;
		while ( inNamemap(name) && (result=img.getHeight(new NameObserver(name)))<0 && inNamemap(name)) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	

	/**
	 * @param component
	 * @param namemap
	 */
	@SuppressWarnings("rawtypes")
	public Iconan(Applet component, Hashtable namemap) {
		this(component, component, namemap);
	}
	
	/**
	 * @deprecated 
	 */
	@SuppressWarnings("rawtypes")
	public Iconan(Component component, Hashtable namemap) {
		this(null, component, namemap);
	}
	
	/**
	 * Werkpaard constructor.
	 * 
	 * @param applet
	 * @param component
	 * @param namemap
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Iconan(Applet Applet, Component component, Hashtable namemap) {	
		this.applet = Applet;
		this.component = component;
		this.namemap = namemap;
		setLocale(component.getLocale());
		imagemap = new Hashtable<>();
		initialize();
		reLocale();
	
	}

	/**
	 * @deprecated gebruik Iconan(Applet)
	 */
	public Iconan() {
		this(null);
	}
	
	public Iconan(Applet applet) {
		this.applet = applet;
		namemap = new Hashtable<>();
		imagemap = new Hashtable<String, Image>();
		initialize();
	}

	private void initialize() {
		setLayout(new GridBagLayout());
		buildList();
		Insets ring = new Insets(10,10,10,10);
        GridBagConstraints listConstraints = new GridBagConstraints();

        listConstraints.gridx = 0;
        listConstraints.gridy = 0;
        listConstraints.gridwidth = 2;
        listConstraints.gridheight = 2;
        listConstraints.insets = ring;
        listConstraints.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(list), listConstraints);
        
		newBtn = new JButton(Text.FILE);
		urlBtn = new JButton(Text.URL);
		okBtn  = new JButton(Text.OK);
		newPnl = new JPanel(new FlowLayout());
		newPnl.add(newBtn);
		newPnl.add(urlBtn);
		newPnl.setBorder(BorderFactory.createTitledBorder(Text.NIEUW));
		
		
		cancelBtn = new JButton(Text.ANNULEER);
		
		rmBtn = new JButton(Text.REMOVE);
		chngBtn = new JButton(Text.WIJZIG);
		editPnl = new JPanel();
		editPnl.add(chngBtn);
		editPnl.add(rmBtn);
		editPnl.setBorder(BorderFactory.createTitledBorder(Text.EDIT));
		
        GridBagConstraints newConstraints = new GridBagConstraints();
        newConstraints.gridx = 0;
        newConstraints.gridy = 2;
        newConstraints.gridwidth = 1;
        newConstraints.gridheight = 1;
        newConstraints.anchor = GridBagConstraints.EAST;
        newConstraints.insets = ring;
        GridBagConstraints rmConstraints = new GridBagConstraints();
        rmConstraints.gridx = 1;
        rmConstraints.gridy = 2;
        rmConstraints.gridwidth = 1;
        rmConstraints.gridheight = 1;
        rmConstraints.anchor = GridBagConstraints.WEST;
        rmConstraints.insets = ring;

        GridBagConstraints okConstraints = new GridBagConstraints();
        okConstraints.gridx = 2;
        okConstraints.gridy = 2;
        okConstraints.gridwidth = 1;
        okConstraints.gridheight = 1;
        okConstraints.insets = ring;
        GridBagConstraints cancelConstraints = new GridBagConstraints();
        cancelConstraints.gridx = 3;
        cancelConstraints.gridy = 2;
        cancelConstraints.gridwidth = 2;
        cancelConstraints.gridheight = 1;
        cancelConstraints.anchor = GridBagConstraints.WEST;
        cancelConstraints.insets = ring;

		add(newPnl, newConstraints);
		add(editPnl, rmConstraints);
		add(okBtn, okConstraints); 
		add(cancelBtn,cancelConstraints);
		newBtn.addActionListener(this);
		urlBtn.addActionListener(this);
		okBtn.addActionListener(this);	
		cancelBtn.addActionListener(this);
		rmBtn.addActionListener(this);
		chngBtn.addActionListener(this);
		previewCanvas.setSize(128,128);
		previewCanvas.setPreferredSize(previewCanvas.getSize());
        GridBagConstraints previewConstraints = new GridBagConstraints();
        previewConstraints.gridx = 2;
        previewConstraints.gridy = 0;
        previewConstraints.gridwidth = 1;
        previewConstraints.gridheight = 2;

		add(previewCanvas, previewConstraints);
		
		widthField = new JTextField("16");
		widthField.setColumns(5);
		heightField = new JTextField("16");
		heightField.setColumns(5);
		
        GridBagConstraints wlConstraints = new GridBagConstraints();
        Insets wl = new Insets(10,0,1,0);
        Insets hl = new Insets(1,0,10,0);
        wlConstraints.gridx = 3;
        wlConstraints.gridy = 0;
        wlConstraints.gridwidth = 1;
        wlConstraints.gridheight = 1;
        wlConstraints.anchor = GridBagConstraints.EAST;
        wlConstraints.insets=wl;
        add(new JLabel("b"), wlConstraints);
        GridBagConstraints hlConstraints = new GridBagConstraints();
        hlConstraints.gridx = 3;
        hlConstraints.gridy = 1;
        hlConstraints.gridwidth = 1;
        hlConstraints.gridheight = 1;
        hlConstraints.anchor = GridBagConstraints.NORTHEAST;
        hlConstraints.insets = hl;
        
        add(new JLabel("h"), hlConstraints);
        GridBagConstraints wConstraints = new GridBagConstraints();
        wConstraints.gridx = 4;
        wConstraints.gridy = 0;
        wConstraints.gridwidth = 1;
        wConstraints.gridheight = 1;
        wConstraints.anchor = GridBagConstraints.WEST;
        wConstraints.insets = wl;

        GridBagConstraints hConstraints = new GridBagConstraints();
        hConstraints.gridx = 4;
        hConstraints.gridy = 1;
        hConstraints.gridwidth = 1;
        hConstraints.gridheight = 1;
        hConstraints.anchor = GridBagConstraints.NORTHWEST;
        hConstraints.insets = hl;

		add(widthField, wConstraints);
		add(heightField, hConstraints);
		
		widthField.addActionListener(this);
		heightField.addActionListener(this);
		widthField.addFocusListener(this);
		heightField.addFocusListener(this);
		
	}

	/**
	 * @return the component
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * @param component the component to set
	 */
	public void setComponent(Component component) {
		this.component = component;
		setLocale(component.getLocale());
		reLocale();
	}

	private ResourceBundle rb = new Text();
	private void reLocale() {
		rb = ResourceBundle.getBundle(Text.class.getName(), getLocale());
		okBtn.setText(rb.getString(Text.OK));
		newBtn.setText(rb.getString(Text.FILE));
		urlBtn.setText(rb.getString(Text.URL));
		newPnl.setBorder(BorderFactory.createTitledBorder(rb.getString(Text.NIEUW)));
		editPnl.setBorder(BorderFactory.createTitledBorder(rb.getString(Text.EDIT)));
		cancelBtn.setText(rb.getString(Text.ANNULEER));
		//title = rb.getString(Text.TITEL);
		rmBtn.setText(rb.getString(Text.REMOVE));
		chngBtn.setText(rb.getString(Text.WIJZIG));
	}

	/**
	 * @return the namemap
	 */
	@SuppressWarnings("rawtypes")
	public Hashtable getNamemap() {
		return namemap;
	}

	/**
	 * @param namemap the namemap to set
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setNamemap(Hashtable namemap) {
		this.namemap = namemap;
		imagemap.clear();
		rebuildList();
	}
	
	private JFileChooser fd;
	//private String title = Text.TITEL;
	/**
	 * Open file dialog. 
	 * @return
	 * @throws IOException 
	 */
	private String newImage() throws IOException {
		if(fd == null)
		{
			fd = new JFileChooser();
		}
		
		if( fd.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
			return null;
		File file = fd.getSelectedFile();
		String filename = file.getName();
		URL u = file.toURI().toURL();
		URLConnection uc = openConnection(u);
		String type = uc.getContentType();
// Not automatic. Why?
		InputStream mime = getClass().getClassLoader().getResourceAsStream("META-INF/mime.types");
		MimetypesFileTypeMap map = new MimetypesFileTypeMap(mime);
		mime.close();
		String type4 = map.getContentType(filename);
		if( type4 != null && type4.startsWith("image"))
			type = type4;		
		
		if(! type.startsWith("image/"))
			throw new IOException(file + ":" + type);

		InputStream fis = uc.getInputStream();
		int len = fis.available();
		byte[] data = new byte[len];
		fis.read(data);
		fis.close();
		// strip extension.
		int ext = filename.lastIndexOf('.');
		if(ext > 0)
			filename = filename.substring(0, ext);
		namemap.put(filename, data);
		namemap.put(filename + "/t", type);
		namemap.remove(filename + "/w");
		
		namemap.remove(filename + "/h");
		namemap.remove(filename + "/u");
		namemap.put(filename + "/f", file.toURI());
		return filename;
	}

	private String last;
	private String newURLImage() throws IOException {
		if(last == null)
			last = getCDN().toString();
		String filename = (String) JOptionPane.showInputDialog(this, rb.getString(Text.EDIT_URL), rb.getString(Text.NIEUW), JOptionPane.QUESTION_MESSAGE, null, null, last);
		if(filename == null)
			return null;
		last = filename;
		URL u = new URL(getCDN(), filename); // FIXME ook hier fallback
// with imageredirector
		URLConnection uc = openConnection(u);
		String type = uc.getContentType();
		if( !type.startsWith("image/" ))
			throw new IOException(u + ":" + type);
		int start = filename.lastIndexOf('/');
		if(start >= 0)
		{
			filename = filename.substring(start+1);
		}
		int ext = filename.lastIndexOf('.');
		if(ext > 0)
			filename = filename.substring(0, ext);
		namemap.put(filename, EMPTY);
		namemap.put(filename + "/t", type);
		namemap.remove(filename + "/w");
		namemap.remove(filename + "/h");
		namemap.remove(filename + "/f");
// als 'u' niet begint met codebase, bewaar volledige naam
		namemap.put(filename + "/u", getURI(u));
		return filename;
	}


	private String getURI(URL u) {
		URL cb = getCDN();
		if(cb.getHost() .equals (u.getHost()) && cb.getPort() == u.getPort())
			return u.getFile();
		else
			return u.toExternalForm();
	}

/**
 * Met imageredirector als fallback.
 * @param u
 * @return
 * @throws IOException
 */
	private URLConnection openConnection(URL u) throws IOException {
		try {
			return u.openConnection();
		} catch (RuntimeException e)
		{
			return redirect(u).openConnection();
		}
	}
	
	private URL getCDN() { try {
		return new URL("http://cdn.dwo.nl/");
	} catch (MalformedURLException e) {
		return null; // Should not happen!
	} }

	private URL getCodeBase() {
		
		Applet applet = getApplet(component);
		if(applet != null)
		{
			URL u = applet.getCodeBase();
			if(u != null && u.getProtocol().startsWith("http"))
				return u;
		}
		return getCDN();
	}
	
	private URL getDocumentBase() {
		Applet applet = getApplet(component);
		if(applet != null)
			return applet.getDocumentBase();
		else
			try {
				return new File(".").toURI().toURL();
			} catch (MalformedURLException e) {
				return null;
			}
			
	}
	
	

	private Applet getApplet(Component parent) {
		if(applet != null)
			return applet;
		while(parent != null && !(parent instanceof Applet))
			parent = parent.getParent();
		return applet = (Applet)parent;
	}

	private JList<String> buildList() {
		dataModel = new DefaultListModel<String>();
		list = new JList<String>(dataModel);
		list.addListSelectionListener(this);
		list.setCellRenderer(new MyListRenderer());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Enumeration<String> keys = namemap.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if(key.indexOf('/')<0)
				add(key);
		}
		return list;
	}
	
	private Image preview; String previewName;
	private JList<String> list;
	private JPanel newPnl, editPnl;
	private DefaultListModel<String> dataModel;
	
	
	public static void main(String[] args) { 
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocale(new Locale("en"));
		Hashtable<Object, Object> hashtable = new Hashtable<Object, Object>();
		Iconan i = new Iconan(f, hashtable);
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(i);
		f.setSize(100,100);
		f.pack();
		f.setVisible(true);
	}

	/**
	 * @return the preview
	 */
	Image getPreview() {
		return preview;
	}

	/**
	 * @param preview the preview to set
	 */
	void setPreview(Image preview) {
		this.preview = preview;
		previewCanvas.repaint();
	}
	
	
	public void addActionListener(ActionListener listener)
	{
		al = AWTEventMulticaster.add(al, listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{
		al = AWTEventMulticaster.remove(al, listener);
	}

	public void select(String imagename) {
		rebuildList();
		ListModel<String> model = list.getModel();
		for(int i = 0; i < model.getSize(); i++)
		{
			if(model.getElementAt(i).equals(imagename))
			{
				list.setSelectedIndex(i);
				selectPreview(imagename);
				return;
			}
		}
	}

	public void focusGained(FocusEvent e) {
	}

	public void focusLost(FocusEvent e) {
		setSize(e.getSource());
	}

	public void valueChanged(ListSelectionEvent e) {
		int index = list.getSelectedIndex();
		if(index >= 0)
		{
			String item = (String) dataModel.get(index);	
			selectPreview(item);
		} else {
			deselectPreview();
		}
	}


	private void deselectPreview() {
		setPreview(null);
		widthField.setText("");
		heightField.setText("");
	}
	
}

