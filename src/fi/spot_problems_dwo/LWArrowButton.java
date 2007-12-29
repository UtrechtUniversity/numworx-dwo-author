package fi.spot_problems_dwo;

import java.awt.*;

// a Light Weight Button with an arrow on it
public class LWArrowButton extends Component
{   // the direction of the arrow 0 = up, 1 = right, 2 = down, 3 = left
    int direction;
    // polygon for the arrow
    Polygon p;
    // flagg for being enabled
    boolean enabled = true;
    // constructor
    public LWArrowButton(int dir)
    {   // wrong direction gives up arrow
        if ( (dir >= 0) && (dir <= 3) )
            direction = dir;
        else
            direction = 0;
    }
    // paint
    public void paint(Graphics g)
    {   // construct arrow
        int[] xPoints = new int[3];
        int[] yPoints = new int[3];
        int nPoints = 3;
        switch (direction)
        {   case 0: // up arrow
            {   xPoints[0] = getSize().width / 2;
                xPoints[1] = getSize().width / 4;
                xPoints[2] = (getSize().width / 4) * 3;
                yPoints[0] = getSize().height / 4;
                yPoints[1] = (getSize().height / 4) * 3;
                yPoints[2] = (getSize().height / 4) * 3;
            }
            break;
            case 1: // right arrow
            {   xPoints[0] = getSize().width / 4;
                xPoints[1] = (getSize().width / 4) * 3;
                xPoints[2] = getSize().width / 4;
                yPoints[0] = getSize().height / 4;
                yPoints[1] = getSize().height / 2;
                yPoints[2] = (getSize().height / 4) * 3;
            }
            break;
            case 2: // down arrow
            {   xPoints[0] = getSize().width / 4;
                xPoints[1] = (getSize().width / 4) * 3;
                xPoints[2] = getSize().width / 2;
                yPoints[0] = getSize().height / 4;
                yPoints[1] = getSize().height / 4;
                yPoints[2] = (getSize().height / 4) * 3;
            }
            break;
            case 3: // left arrow
            {   xPoints[0] = (getSize().width / 4) * 3;
                xPoints[1] = getSize().width / 4;
                xPoints[2] = (getSize().width / 4) * 3;
                yPoints[0] = getSize().height / 4;
                yPoints[1] = getSize().height / 2;
                yPoints[2] = (getSize().height / 4) * 3;
            }
            break;
            default: // nothing, see constructor
        } // switch
        p = new Polygon(xPoints, yPoints, nPoints);
        // paint arrow
        g.setColor(Color.black);
        if (enabled)
            g.fillPolygon(p);
        else
            g.drawPolygon(p);
        // paint button outline
        g.setColor(Color.white);
        g.drawLine(0, 0, getSize().width - 1, 0);
        g.drawLine(1, 1, getSize().width - 2, 1);
        g.drawLine(0, 0, 0, getSize().height - 1);
        g.drawLine(1, 1, 1, getSize().height - 2);
        g.setColor(Color.black);
        g.drawLine(0, getSize().height - 1,
                   getSize().width - 1, getSize().height - 1);
        g.drawLine(1, getSize().height - 2,
                   getSize().width - 2, getSize().height - 2);
        g.drawLine(getSize().width - 1, 0,
                   getSize().width - 1, getSize().height - 1);
        g.drawLine(getSize().width - 2, 1,
                   getSize().width - 2, getSize().height - 2);
    } // paint
    // redefined method
    public void setEnabled(boolean b)
    {   enabled = b;
        super.setEnabled(b);
        repaint();
    }
    // redefined method
    public boolean isEnabled()
    {   return enabled;
    }    

} // LWArrowButton

