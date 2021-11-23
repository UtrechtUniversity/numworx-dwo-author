package fi.beans.iconan;

import java.applet.Applet;
import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import fi.beans.iconan.text.Text;
import fi.beans.numworxlf.JCheckBox;
import fi.wiskopdr.WiskOpdr;
import fi.wiskopdr.WiskOpdrButton;
import fi.wiskopdr.WiskOpdrTextField;

@SuppressWarnings("serial")
public class Iconan extends JPanel implements ActionListener, FocusListener, ListSelectionListener {

	private Component component;
	final Hashtable<String,Object> namemap, xnamemap;
	
	private Strategy imageStrategy, svgStrategy;
	
	Hashtable<String,Image> imagemap;
	private ActionListener al;
	private JButton newBtn, okBtn, cancelBtn, closeBtn, rmBtn, urlBtn, chngBtn, downBtn;
	JTextField widthField, heightField;
	JCheckBox  volBreedteCB;
	JLabel widthLabel, heightLabel;
	int previewWidth = 32, previewHeight = 32;
	private JPanel previewCanvas = new JPanel(new BorderLayout()); //{ previewCanvas.setBorder(BorderFactory.createEtchedBorder()); }
	private Applet applet;
	private boolean emptyStart = true;
	private boolean chooseImage = true;
	long suffix;
	private static long nextSuffix;
	static final char SUFFIX = '\f';
	
	String suffix(String name) {
	  if (suffix == 0||name.indexOf(SUFFIX)>0) return name;
	  return name + SUFFIX + suffix;
	}
	String strip(String name) {
	  int i = name.indexOf(SUFFIX);
	  if (i >0) return name.substring(0,i);
	  return name;
	}
	
	private JDialog imageDialog;
	
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
			{
			  //namemap.put(name + "/w", (previewWidth));
			  xnamemap.put(suffix(name)+"/w", (previewWidth));
			}
			previewCanvas.repaint();
			return true;
		}
		if(source==heightField)
		{
			previewHeight = Integer.parseInt(heightField.getText());
			String name = previewName;
			if(name != null)
			{
			  //namemap.put(name + "/h", (previewHeight));
              xnamemap.put(suffix(name) + "/h", (previewHeight));
			}
			previewCanvas.repaint();
			return true;
		}
		if (source == volBreedteCB) {
		  Boolean volBreedte = volBreedteCB.isSelected();
		  String name = previewName;
		  if (name != null) {
		    //namemap.put(name + "/v", volBreedte);
		    xnamemap.put(suffix(name), volBreedte);
		  }
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
//					namemap.put(selected + "/w", Integer.parseInt(widthField.getText()));
//					namemap.put(selected + "/h", Integer.parseInt(heightField.getText()));
//					namemap.put(selected + "/v", volBreedteCB.isSelected());
//					Iterator<String> keys = namemap.keySet().iterator();
//					while (keys.hasNext()) {
//                      String string = (String) keys.next();
//                      if (string.contains("" + SUFFIX + suffix + "/")) keys.remove();                     
//                    }
					xnamemap.put(suffix(selected) + "/w", Integer.parseInt(widthField.getText()));
					xnamemap.put(suffix(selected) + "/h", Integer.parseInt(heightField.getText()));
					xnamemap.put(suffix(selected) + "/v", volBreedteCB.isSelected());
				} else {
					selected = "";
					al.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "cancel"));
					return;
				}	
				int w = Integer.parseInt(widthField.getText());
				int h = Integer.parseInt(heightField.getText());
				String v = volBreedteCB.isSelected() ? "v":"";
				String selected_suffix = selected + SUFFIX + "w" + w + "h" + h + v;
				al.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, selected_suffix));
			}
			if(imageDialog!=null)
				imageDialog.setVisible(false);
		} else if(e.getSource()==cancelBtn || e.getSource()==imageDialog || e.getSource()==closeBtn)
		{
			if(al != null)
			{
			  String selected = (String) list.getSelectedValue();
              if(null == selected || "".equals(selected) || emptyStart)
				al.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "cancel"));
              else
                al.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
			}
			if(imageDialog!=null)
				imageDialog.setVisible(false);
		} else if(e.getSource()==getComponent())
		{
			rebuildList();
		} else if (e.getSource()==rmBtn)
		{
			int[] selectedList =  list.getSelectedIndices();
			for(int i = selectedList.length-1; i >= 0; i--) 
			{
			    int index = selectedList[i];
			    String selected = dataModel.getElementAt(index);
				dataModel.remove(index);
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
		} else if (e.getSource() == downBtn) {
            String selected = (String) list.getSelectedValue();
            if (selected == null) return;
            if (fd == null) fd = new JFileChooser();
            URI suri = (URI) namemap.get(selected + "/f");
            if (suri != null) fd.setSelectedFile(new File(suri));
            else {
              String url = (String) namemap.get(selected + "/u");
              int index = url.indexOf('#');
              if (index >= 0) url = url.substring(0,index);
              index = url.indexOf('?');
              if (index >= 0) url = url.substring(0,index);
              index = url.lastIndexOf('/');
              if (index >= 0) url = url.substring(index+1);
              if (url.isEmpty()) url = selected;
              fd.setSelectedFile(new File(url));
            }
            String t = fd.getDialogTitle();
            fd.setDialogTitle("Download " + selected);
            int result = fd.showSaveDialog(this);
            fd.setDialogTitle(t);
            if( result != JFileChooser.APPROVE_OPTION)
              return;
            File file = fd.getSelectedFile();
            try (FileOutputStream out = new FileOutputStream(file)) {
              byte[] data = (byte[]) namemap.get(selected);
              if (data.length == 0) {
                String url = (String) namemap.get(selected + "/u");
                URLConnection uc = openConnection(new URL(getCDN(),url));
                int len = uc.getContentLength();
                DataInputStream in = new DataInputStream(uc.getInputStream());
                data = new byte[len];
                in.readFully(data);
                in.close();
              }
              out.write(data);
            } catch (FileNotFoundException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            } catch (IOException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
            }
		}

	}


	private void edit(String selected) throws IOException {
		String su = (String) namemap.get(selected + "/u");
		if(su != null)
		{
			String filename = (String) JOptionPane.showInputDialog(this, WiskOpdr.rb.getString("editUrlLabel"), WiskOpdr.rb.getString("wijzigKnopLabel"), JOptionPane.QUESTION_MESSAGE, null, null, su);
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
				namemap.put(selected + "/t", type);
				strategies.remove(selected);
				imagemap.remove(selected);
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
			type = retype(file.getName(), type);
			if(! type.startsWith("image/"))
				throw new IOException(file + ":" + type);

			InputStream fis = uc.getInputStream();
			int len = fis.available();
			byte[] data = new byte[len];
			fis.read(data);
			fis.close();
			// strip extension.
			namemap.put(selected, data);
			imagemap.remove(selected);
            strategies.remove(selected);
			namemap.remove(selected + "/w");
			namemap.remove(selected + "/h");
			namemap.remove(selected + "/u");
			namemap.put(selected + "/f", file.toURI());
			namemap.put(selected + "/t", type);
			selectPreview(selected);
			return;
		}
		
		
	}

	public void editImage(String imageName, Component parent, ActionListener aListener) {
        
		if(imageDialog == null) {
        	Window f = (Window) WiskOpdr.getWindowForComponent(parent);
        	imageDialog = new JDialog(f,""); imageDialog.setModal(true);
			imageDialog.setLayout(new BorderLayout());
			//imageDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			if(!chooseImage) {
				imageDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			}
			JPanel headerPanel = new JPanel();
			headerPanel.setBackground(WiskOpdr.colorBlue1);
			JLabel iconanTitleLabel = new JLabel(WiskOpdr.rb.getString("kiesAfbeeldingLabel"));
			if(!chooseImage) {
				iconanTitleLabel.setText(WiskOpdr.rb.getString("afbeeldingenBeherenLabel"));
			}
			iconanTitleLabel.setFont(new Font("SansSerif",Font.PLAIN, 24));
			iconanTitleLabel.setForeground(WiskOpdr.colorGray3);
			headerPanel.add(iconanTitleLabel);
			imageDialog.add(headerPanel, BorderLayout.NORTH);
			
			JPanel bottomPanel = new JPanel(new BorderLayout());
			bottomPanel.setBackground(WiskOpdr.colorGray2);
			bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			Component[] comp = {okBtn, hst(20), cancelBtn, hgl(), widthLabel, hst(5), widthField, hst(10), heightLabel, hst(5), heightField, hst(5), volBreedteCB };
			Component[] compBeheer = {closeBtn, hst(20), downBtn, hgl(), widthLabel, hst(5), widthField, hst(10), heightLabel, hst(5), heightField};
			if(chooseImage) 
				bottomPanel.add(hb(comp));
			else
				bottomPanel.add(hb(compBeheer));
			imageDialog.add(bottomPanel, BorderLayout.SOUTH);
			
            imageDialog.add(this);
            imageDialog.pack();
            Dimension screenSize = WiskOpdr.applet.getToolkit().getScreenSize();
    	    int xD = (screenSize.width-imageDialog.getSize().width)/2;
    	    int yD = (screenSize.height-imageDialog.getSize().height)/2;
    	    imageDialog.setLocation(xD, yD);
            addActionListener(aListener);
            
           imageDialog.addWindowListener(new WindowListener() {
            	@Override
				public void windowOpened(WindowEvent e) {
				}

				@Override
				public void windowClosing(WindowEvent e) {
					if(al!=null) {
						String selected = (String) list.getSelectedValue();
			            if(null == selected || "".equals(selected) || emptyStart)
			            	actionPerformed(new ActionEvent(imageDialog, ActionEvent.ACTION_PERFORMED, "cancel"));
			            else
			                actionPerformed(new ActionEvent(imageDialog, ActionEvent.ACTION_PERFORMED, ""));
					}
				}

				@Override
				public void windowClosed(WindowEvent e) {
					
				}

				@Override
				public void windowIconified(WindowEvent e) {
				}

				@Override
				public void windowDeiconified(WindowEvent e) {
				}

				@Override
				public void windowActivated(WindowEvent e) {
				}

				@Override
				public void windowDeactivated(WindowEvent e) {
				}
            });
		}
        select(imageName);
        imageDialog.setVisible(true);
    }
	
	private Box hb(Component[] c) {
		Box box = Box.createHorizontalBox();
		for(int i=0 ; c!=null && i<c.length ; i++) 
			box.add(c[i]);
		return box;
	}
	
	private Component hgl() {
		return Box.createHorizontalGlue();
	}
	
	private Component hst(int n) {
		return Box.createHorizontalStrut(n);
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
		setPreview(getStrategy(name).getPreviewPanel(name));
		setSizes(name);
	}

	/**
	 * @param name
	 */
	private void setSizes(String name) {
		previewWidth = getWidth(name);
		if (!namemap.containsKey(name + "/w"))
		  namemap.put(name + "/w", previewWidth);
		widthField.setText(String.valueOf(previewWidth));
	    widthField.setColumns(5);
	    widthField.revalidate();

		previewHeight = getHeight(name);
		if (!namemap.containsKey(name + "/h"))
		  namemap.put(name + "/h", previewHeight);
		heightField.setText(String.valueOf(previewHeight));
		
		volBreedteCB.setSelected(isVolBreedte(name));
	}

	public boolean isVolBreedte(String name) {
      if(xnamemap.containsKey(suffix(name) + "/v")) {
        return Boolean.TRUE.equals(xnamemap.get(suffix(name) + "/v"));
      }
      if(name.contains(SUFFIX+"w")) return name.endsWith("v");

      if(namemap.containsKey(suffix(name) + "/v")) {
        return Boolean.TRUE.equals(namemap.get(suffix(name) + "/v"));
      }
      return false;
  }

  Map<String,Strategy> strategies = new TreeMap<String,Strategy>();
	
	public int getWidth(String name) {
      return getStrategy(name).getWidth(name);
  }


  protected Strategy getStrategy(String name) {
    String strip = strip(name);
    Strategy s = strategies.get(strip);
    if(s == null) {
      Object mime = namemap.get(strip + "/t");
      if("image/svg+xml".equals(mime)) {
        s = svgStrategy;
      } else {
        s = imageStrategy;
      }
      strategies.put(strip, s);
    }
    return s;
  }

	public int getHeight(String name) {
	  return getStrategy(name).getHeight(name);
	}

  private void rebuildList() {
		list.removeAll();
		Iterator<String> keys = namemap.keySet().iterator();
		while (keys.hasNext()) {
			String string = keys.next();
			if(string.indexOf('/')<0 && string.indexOf(SUFFIX)<0)
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
	void remove(String item) {
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
	    name = strip(name);
		byte[] data = (byte[]) namemap.get(name);
		if(data == null)
			return null;
		Image result = imagemap.get(name);
		if(result != null)
			return result;

		String mime = (String) namemap.get(name + "/t");
		if( mime != null && mime.contains("svg")) return null; // No preview
		
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

	public JComponent getComponent(String name) {
	    return getStrategy(name).getComponent(name);
	}

	public Icon getIcon(String name) {
	    return getStrategy(name).getIcon(name);
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
	
	public Iconan(Applet Applet, Component component, Hashtable namemap) {
	  this(Applet,component, namemap, true);
	}
	
	/**
	 * Werkpaard constructor.
	 * 
	 * @param applet
	 * @param component
	 * @param namemap
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Iconan(Applet Applet, Component component, Hashtable namemap, boolean chooseImage) {	
		this.applet = Applet;
		this.component = component;
		this.namemap = namemap;
		this.xnamemap = new Hashtable<String, Object>();
		this.chooseImage = chooseImage;
		setLocale(component.getLocale());
		imagemap = new Hashtable<>();
		initialize();
		reLocale();
		nextSuffix();
	}

	private void nextSuffix() {
	  nextSuffix = 0;
      if (namemap != null) {
        for(String name: namemap.keySet()) {
          int index = name.indexOf(SUFFIX);
          if (index > 0) {
            name = name.substring(index+1);
            index = name.indexOf('/');
            if (index >=0) {
              name = name.substring(0,index);
            }
            nextSuffix = Math.max(nextSuffix, Long.parseLong(name));
          }
        }
      }
    
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
		xnamemap = new Hashtable<>();
		imagemap = new Hashtable<String, Image>();
		initialize();
	}

	private void initialize() {
		setLayout(new GridBagLayout());
		setBackground(WiskOpdr.colorGray3);
		setBorder(BorderFactory.createEmptyBorder(10,10,20,20));
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
        
		newBtn = new WiskOpdrButton(WiskOpdr.rb.getString("fileKnopLabel"));
		newBtn.setMaximumSize(new Dimension(120,22));
		newBtn.setPreferredSize(new Dimension(80,22));
		
		urlBtn = new WiskOpdrButton(WiskOpdr.rb.getString("urlKnopLabel"));
		urlBtn.setMaximumSize(new Dimension(120,22));
		urlBtn.setPreferredSize(new Dimension(80,22));
		
		okBtn  = new WiskOpdrButton(WiskOpdr.rb.getString("okKnopLabel"));
		okBtn.setBackground(WiskOpdr.colorBlue1);
		okBtn.setForeground(WiskOpdr.colorGray3);
		okBtn.setPreferredSize(new Dimension(70,24));
		okBtn.setMaximumSize(new Dimension(70,24));
		
		newPnl = new JPanel(new FlowLayout());
		newPnl.add(newBtn);
		newPnl.add(urlBtn);
		TitledBorder border = BorderFactory.createTitledBorder(WiskOpdr.rb.getString("toevoegenLabel"));
		border.setTitleColor(WiskOpdr.colorBlue1);
		newPnl.setBorder(border);
		
		
		cancelBtn = new WiskOpdrButton(WiskOpdr.rb.getString("annuleerKnopLabel"));
		cancelBtn.setBackground(WiskOpdr.colorBlue1);
		cancelBtn.setForeground(WiskOpdr.colorGray3);
		cancelBtn.setPreferredSize(new Dimension(70,24));
		cancelBtn.setMaximumSize(new Dimension(70,24));
		
		closeBtn = new WiskOpdrButton(WiskOpdr.rb.getString("sluitKnopLabel"));
		closeBtn.setBackground(WiskOpdr.colorBlue1);
		closeBtn.setForeground(WiskOpdr.colorGray3);
		closeBtn.setPreferredSize(new Dimension(70,24));
		closeBtn.setMaximumSize(new Dimension(70,24));

        downBtn = new WiskOpdrButton("Export");
        downBtn.setBackground(WiskOpdr.colorBlue1);
        downBtn.setForeground(WiskOpdr.colorGray3);
        downBtn.setPreferredSize(new Dimension(70,24));
        downBtn.setMaximumSize(new Dimension(70,24));
        downBtn.setEnabled(false);
        downBtn.setVisible(WiskOpdr.isExperimental() && WiskOpdr.isPremium());
		
		rmBtn = new WiskOpdrButton(WiskOpdr.rb.getString("verwijderKnopLabel"));
		rmBtn.setMaximumSize(new Dimension(120,22));
		rmBtn.setPreferredSize(new Dimension(80,22));
		
		chngBtn = new WiskOpdrButton(WiskOpdr.rb.getString("wijzigKnopLabel"));
		chngBtn.setMaximumSize(new Dimension(120,22));
		chngBtn.setPreferredSize(new Dimension(80,22));
		
		editPnl = new JPanel();
		editPnl.add(chngBtn);
    	editPnl.add(rmBtn);
		
    	border = BorderFactory.createTitledBorder(WiskOpdr.rb.getString("bewerkenLabel"));
		border.setTitleColor(WiskOpdr.colorBlue1);
		editPnl.setBorder(border);
		
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
		if(chooseImage) {
    		add(okBtn, okConstraints); 
    		add(cancelBtn,cancelConstraints);
		}
		newBtn.addActionListener(this);
		urlBtn.addActionListener(this);
		okBtn.addActionListener(this);	
		cancelBtn.addActionListener(this);
		closeBtn.addActionListener(this);
		downBtn.addActionListener(this);
		rmBtn.addActionListener(this);
		chngBtn.addActionListener(this);
        previewCanvas.setSize(250,250);
        previewCanvas.setPreferredSize(previewCanvas.getSize());
        previewCanvas.setMinimumSize(previewCanvas.getSize());
        
        GridBagConstraints previewConstraints = new GridBagConstraints();
        previewConstraints.gridx = 2;
        previewConstraints.gridy = 0;
        previewConstraints.gridwidth = 1;
        previewConstraints.gridheight = 2;

		add(previewCanvas, previewConstraints);
		
		widthField = new WiskOpdrTextField("16");
		widthField.setEnabled(true);
		widthField.setColumns(5);
		widthField.setMinimumSize(widthField.getPreferredSize());
		heightField = new WiskOpdrTextField("16");
		heightField.setEnabled(true);
		heightField.setColumns(5);
		heightField.setMinimumSize(heightField.getPreferredSize());
		
		volBreedteCB = new JCheckBox(WiskOpdr.rb.getString("volleBreedteLabel"));
		volBreedteCB.setOpaque(false);
		
        GridBagConstraints wlConstraints = new GridBagConstraints();
        Insets wl = new Insets(10,0,1,0);
        Insets hl = new Insets(1,0,10,0);
        wlConstraints.gridx = 3;
        wlConstraints.gridy = 0;
        wlConstraints.gridwidth = 1;
        wlConstraints.gridheight = 1;
        wlConstraints.anchor = GridBagConstraints.EAST;
        wlConstraints.insets=wl;
        widthLabel = new JLabel(WiskOpdr.rb.getString("breedteLabel"));
        widthLabel.setFont(WiskOpdr.tekstFont);
        widthLabel.setForeground(WiskOpdr.colorBlue1);
        add(widthLabel, wlConstraints);
        GridBagConstraints hlConstraints = new GridBagConstraints();
        hlConstraints.gridx = 3;
        hlConstraints.gridy = 1;
        hlConstraints.gridwidth = 1;
        hlConstraints.gridheight = 1;
        hlConstraints.anchor = GridBagConstraints.NORTHEAST;
        hlConstraints.insets = hl;
        heightLabel = new JLabel(WiskOpdr.rb.getString("hoogteLabel"));
        widthLabel.setFont(WiskOpdr.tekstFont);
        heightLabel.setForeground(WiskOpdr.colorBlue1);
        add(heightLabel, hlConstraints);
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
		volBreedteCB.addActionListener(this);
		
		
		svgStrategy = new SVGStrategy(this);
		imageStrategy = new ImageStrategy(this);
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
		okBtn.setText(WiskOpdr.rb.getString("okKnopLabel"));
		newBtn.setText(rb.getString(WiskOpdr.rb.getString("fileKnopLabel")));
		urlBtn.setText(rb.getString(WiskOpdr.rb.getString("urlKnopLabel")));

		TitledBorder border = BorderFactory.createTitledBorder(WiskOpdr.rb.getString("toevoegenLabel"));
		border.setTitleColor(WiskOpdr.colorBlue1);
		newPnl.setBorder(border);
		
		border = BorderFactory.createTitledBorder(WiskOpdr.rb.getString("bewerkenLabel"));
		border.setTitleColor(WiskOpdr.colorBlue1);
		editPnl.setBorder(border);
		
		cancelBtn.setText(rb.getString(WiskOpdr.rb.getString("annuleerKnopLabel")));
		closeBtn.setText(rb.getString(WiskOpdr.rb.getString("sluitKnopLabel")));
		//title = rb.getString(Text.TITEL);
		rmBtn.setText(rb.getString(WiskOpdr.rb.getString("verwijderKnopLabel")));
		chngBtn.setText(rb.getString(WiskOpdr.rb.getString("wijzigKnopLabel")));
	}

	/**
	 * @return the namemap
	 */
	@SuppressWarnings("rawtypes")
	public Hashtable getNamemap() {
		return namemap;
	}

//	/**
//	 * @param namemap the namemap to set
//	 */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public void setNamemap(Hashtable namemap) {
//		this.namemap = namemap;
//		imagemap.clear();
//		rebuildList();
//	}
	
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
		type = retype(filename, type);
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
		strategies.remove(filename);
		imagemap.remove(filename);
		namemap.put(filename + "/t", type);
		namemap.remove(filename + "/w");
		namemap.remove(filename + "/v");		
		namemap.remove(filename + "/h");
		namemap.remove(filename + "/u");
		namemap.put(filename + "/f", file.toURI());
		return filename;
	}
  private String retype(String filename, String type) {
    // Not automatic. Why?
		try {
		InputStream mime = getClass().getClassLoader().getResourceAsStream("META-INF/mime.types");
// FIXME java.activation not available by default
		MimetypesFileTypeMap map = new MimetypesFileTypeMap(mime);
		mime.close();
		String type4 = map.getContentType(filename);
		
		if( type4 != null && type4.startsWith("image"))
			type = type4;		
		} catch (Throwable e) {
		  Logger.getLogger(getClass().getName()).warning("MimeTypesFileTypeMap: " + e );
		}
    return type;
  }

	private String last;
	private String newURLImage() throws IOException {
		if(last == null)
			last = getCDN().toString();
		String filename = (String) JOptionPane.showInputDialog(this, WiskOpdr.rb.getString("editUrlLabel"), WiskOpdr.rb.getString("toevoegenLabel"), JOptionPane.QUESTION_MESSAGE, null, null, last);
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
		namemap.remove(filename + "/v");
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
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		Enumeration<String> keys = namemap.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if(key.indexOf('/')<0)
				add(key);
		}
		return list;
	}
	
	String previewName;
	private JList<String> list;
	private JPanel newPnl, editPnl;
	private DefaultListModel<String> dataModel;
	
	


	/**
	 * @param jComponent the preview to set
	 */
	void setPreview(JComponent jComponent) {
		previewCanvas.removeAll();
		if(jComponent != null) {
		  previewCanvas.add(jComponent,BorderLayout.CENTER);
		  jComponent.setBounds(0, 0, previewCanvas.getWidth(), previewCanvas.getHeight());
		  previewCanvas.revalidate();
		}
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
	    emptyStart = "EMPTY".equals(imagename);
		rebuildList();
		String shortname = imagename;
		if (emptyStart||imagename == null) {
		  suffix = ++nextSuffix;
		} else {
		  int s = imagename.indexOf(SUFFIX);
		  if (s>0) {
		    String param = imagename.substring(s+1);
            shortname = imagename.substring(0,s);
		    try {
		      if (param.startsWith("w")) {
		        Scanner scan = new Scanner(param);
		        scan.useDelimiter("[hwv]");
		        suffix = ++nextSuffix;
		        int wint = scan.nextInt();
		        int hint = scan.nextInt();
		        scan.useDelimiter("");
		        boolean v = scan.hasNext();
                xnamemap.put(suffix(shortname) + "/w", wint);
                xnamemap.put(suffix(shortname) + "/h", hint);
                xnamemap.put(suffix(shortname) + "/v", v);
                scan.close();
		      } else
                suffix = Long.parseLong(param);
            } catch (NumberFormatException e) {
              suffix = ++nextSuffix;
            }
		  } else {
		    suffix = ++nextSuffix;
		  }
		}
		ListModel<String> model = list.getModel();
		list.getSelectionModel().clearSelection();
		widthField.setText("");
		heightField.setText("");
		volBreedteCB.setSelected(false);
		widthField.setEnabled(imagename != null);
		heightField.setEnabled(imagename != null);
		volBreedteCB.setEnabled(imagename != null);
		
		for(int i = 0; i < model.getSize(); i++)
		{
			if(model.getElementAt(i).equals(shortname))
			{
				list.setSelectedIndex(i);
				selectPreview(shortname);
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
	    int[] indices = list.getSelectedIndices();
	    boolean single = indices.length == 1;
	    chngBtn.setEnabled(single);
	    downBtn.setEnabled(single);
	    okBtn.setEnabled(indices.length < 2); // only if single/none

	    if (!single) {
	      deselectPreview();
	      return;
	    }
	  
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
		widthField.setColumns(5);
		heightField.setText("");
	}


  public void dispose() {
    svgStrategy.dispose();
    imageStrategy.dispose();
  }

  public Integer getSuffix(String name, String ext) {
    if (name.contains(SUFFIX+"w")) {
      int i = name.indexOf(SUFFIX);
      name = name.substring(i+1);
      try (Scanner scan = new Scanner(name)) {
        scan.useDelimiter("[vwh]");
        i = scan.nextInt();
        if ("/w".equals(ext)) return i;
        return scan.nextInt(); // "/h"
      }
    }
    
    Integer w = (Integer) xnamemap.get(suffix(name) + ext);
    if (w == null)
      w = (Integer) namemap.get(suffix(name) + ext);
      if (w == null)
        w = (Integer)namemap.get(strip(name) + ext);
     return w;
  }
	
}

