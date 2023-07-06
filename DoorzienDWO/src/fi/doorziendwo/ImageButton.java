package fi.doorziendwo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.*;
//additional classes 
//add when needed
public class ImageButton extends JComponent
{
 boolean pressed = false;
 boolean enabled = true;
 Image imageOn, imageOff, image;

 public ImageButton(Image imageOn, Image imageOff, int width, int height) 
 {   this.imageOn = imageOn;
     if (imageOff != null)
         this.imageOff = imageOff;
     else
         this.imageOff = imageOn;        
     this.image = imageOn;
     setSize(width, height);
 } // ImageButton

 public ImageButton(Image imageOn, Image imageOff) 
 {   this.imageOn = imageOn;
     if (imageOff != null)    
         this.imageOff = imageOff;
     else
         this.imageOff = imageOn;        
     this.image = imageOn;
 } // ImageButton

 public void setOn(boolean b)
 {   enabled = b;
     if (enabled)
         setImage(imageOn);
     else
         setImage(imageOff);
     
     pressed = false;
 }    

 public void setPressed(boolean b)
 {   pressed = b;
     repaint();
 }    

 public void setImage(Image image)
 {
      this.image = image;
      repaint();
 
 }  

 public void update(Graphics g)
 {    paint(g);
 
 }  

 public void paintComponent(Graphics g) 
 {    
      if (image == null)
          return;
      if  (
          (getSize().width > image.getWidth(this)) &&
          (getSize().height > image.getHeight(this))
          ) 
      {// de image is kleiner dan de button, centreren
 	     g.drawImage(image, 
	    	       (getSize().width - image.getWidth(this))/2, 
		    	   (getSize().height - image.getHeight(this))/2, 
			        this);
//System.out.println("centering");			        
      } 
      else 
      {
    	  int dw = (getSize().width - image.getWidth(this))/2;
    	  int dh = (getSize().height - image.getHeight(this))/2;
    	  g.drawImage(image, dw, dh, this); 
    	  
    	  // schalen
	      //   g.drawImage(image, 1, 1, 
	      //      getSize().width - 3, getSize().height - 3, this);
//System.out.println("scaling");			        	         
      }
      
      // button outline 
      if (pressed)
      {
      g.setColor(Color.black);
      g.drawLine(0, 0, getSize().width - 1, 0);         
      g.drawLine(0, 0, 0, getSize().height - 1);         
      g.setColor(Color.white);
      g.drawLine(0, getSize().height - 1, getSize().width - 1, getSize().height - 1);         
      g.drawLine(getSize().width - 1, 0, getSize().width - 1, getSize().height - 1);         
      }
      else
      {
      g.setColor(Color.white);
      g.drawLine(0, 0, getSize().width - 1, 0);         
      g.drawLine(0, 0, 0, getSize().height - 1);         
      g.setColor(Color.black);
      g.drawLine(0, getSize().height - 1, getSize().width - 1, getSize().height - 1);         
      g.drawLine(getSize().width - 1, 0, getSize().width - 1, getSize().height - 1);         
      }
      

      
      
 } // paint

} // class ImageButton
