package fi.statistiek;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;



public class DialogButton extends JButton implements ActionListener, ComponentListener
{
	private JDialog dialog;
	private JPanel content;
	private String title;
	Dimension preferred = new Dimension(100,400);
	

	public DialogButton(String string, JPanel content){	
		super(string);
		title = string;
		setFont(Statistiek.font);
		this.content = content;
		preferred = content.getSize();
		addActionListener(this);
	}
	
	public void setDialogSize(int w, int h){
		preferred = new Dimension(w,h);
	}
	
    public void makeDialog(){
    	if(dialog==null) {
        	dialog = new JDialog((Frame) null, title, true);
        	dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        	dialog.getContentPane().setLayout(null);
            dialog.getContentPane().setLayout(new BorderLayout());
        }
        //dialog.setPreferredSize(preferred);
    	content.setLocation(0,0);
        dialog.getContentPane().add(content);
        dialog.setSize(preferred);
        dialog.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = getLocationOnScreen().x + Math.min(0,screenSize.width - (getLocationOnScreen().x + getWidth()));
		int y = getLocationOnScreen().y + Math.min(0,screenSize.height - (getLocationOnScreen().y + getHeight()));
		dialog.setLocation(x,y);

        dialog.setVisible(true);
	    
    }
    
    public void closeDialog() {
    	dialog.setVisible(false);
		dialog.dispose();
    }
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource().equals(this)){	
			makeDialog();
		}
		
	}

	public void componentResized(ComponentEvent e) {
		if(e.getSource()==content) {
			preferred = content.getPreferredSize();
			dialog.setSize(preferred);
	        dialog.pack();
	        dialog.setVisible(true);
		}
		
	}

	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}   
}

