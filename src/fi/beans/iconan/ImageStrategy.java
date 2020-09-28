package fi.beans.iconan;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;


class ImageStrategy implements Strategy {

  private class ImageComponent extends JComponent {

    private Image image;

    public ImageComponent(Image image, int width, int height) {
      setSize(Math.max(0, width), Math.max(height,0));
      setPreferredSize(getSize());
      this.image = image;
      setOpaque(false); // transparant!
    }

    public void paintComponent(Graphics g) {
      try {
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
      } catch (Exception e) {
        java.util.logging.Logger.getLogger(getClass().getName()).severe("drawImage " + e);
      }
    }
  }

  private class NameObserver implements ImageObserver {
    String name;

    public boolean imageUpdate(Image img, int infoflags, int x, int y,
            int width, int height) {
        if((infoflags & (ImageObserver.ABORT|ImageObserver.ERROR)) != 0)
        {   
System.err.println("Error in imageUpdate " + name + " flag = " + infoflags);                
            synchronized(ImageStrategy.this) {
                parent.imagemap.remove(name);
                parent.namemap.remove(name);
                parent.namemap.remove(name +"/w");
                parent.namemap.remove(name +"/h");
                parent.namemap.remove(name +"/u");
                parent.namemap.remove(name +"/f");
                parent.remove(name);
                ImageStrategy.this.notifyAll();
            }
            return false;
        }
        synchronized(ImageStrategy.this) {
            if((infoflags & ImageObserver.WIDTH) != 0)
            {
            	parent.namemap.put(name + "/w", new Integer(width));
                if(img == preview)
                {
                    parent.previewWidth = width;
                    parent.widthField.setText(String.valueOf(width));
                    parent.widthField.setColumns(5);

                    previewCanvas.repaint();
                }
                ImageStrategy.this.notifyAll();
            }
            if((infoflags & ImageObserver.HEIGHT) != 0)
            {
            	parent.namemap.put(name + "/h", new Integer(height));
                if(img == preview)
                {
                    parent.previewHeight = height;
                    parent.heightField.setText(String.valueOf(height));
                    previewCanvas.repaint();
                }
                ImageStrategy.this.notifyAll();
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
  Image preview; 
  JPanel previewCanvas = new JPanel(null) { 
    
    public void paint(Graphics g) {
        if(preview != null)
        {
            int w = parent.previewWidth;
            int h = parent.previewHeight;
//scale down to fit.
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

    final Iconan parent;
  
    ImageStrategy(Iconan parent) {
        this.parent = parent;
    }
    
    public synchronized int getWidth(String name)
    {
        Integer w = (Integer) parent.namemap.get(parent.suffix(name) + "/w");
        if (w == null)
          w = (Integer)parent.namemap.get(parent.strip(name) + "/w");
        if(w != null)
            return w.intValue();
        Image img = parent.getImage(name);
        if(img != null)
            return getWidth(name, img);
        return -1;
    }

    public synchronized int getHeight(String name)
    {
        Integer w = (Integer) parent.namemap.get(parent.suffix(name) + "/h");
        if (w == null)
          w = (Integer) parent.namemap.get(parent.strip(name) + "/h");
        if(w != null)
            return w.intValue();
        Image img = parent.getImage(name);
        if(img != null)
            return getHeight(name, img);
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

    private boolean inNamemap(String name) {
        return parent.namemap.containsKey(name);
    }

    @Override
    public JComponent getPreviewPanel(String name) {
      preview = parent.getImage(name);
      return previewCanvas;
    }

    public JComponent getComponent(String name) {
        JComponent result = new ImageComponent(parent.getImage(name), getWidth(name), getHeight(name));
        return result;
    }

    @Override
    public Icon getIcon(String name) {
      Image image = parent.getImage(name);
      if(image == null) return null;
      int w = getWidth(name);
      int h = getHeight(name);
      if(w > 0 && h > 0) {
          image = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
      }
      return new ImageIcon(image);
    }
}
