package fi.ivmdrawgwt.client;

import com.google.gwt.canvas.dom.client.Context2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO: Not sure if still required with Matrix class.
 */
public class LineData {
    public static Logger logger = Logger.getLogger("linedata");
    private double xsInput[];
    private double ysInput[];
    private int size;

    /**
     * Constructor to create the LineData object via ArrayList of Point objects
     * @param points: ArrayList<Point>
     */
    public LineData(ArrayList<Point> points) {
        this.xsInput = new double[points.size()];
        this.ysInput = new double[points.size()];

        // Add points to arrays while also converting between coordinate system
        // Now the origin is in the bottom-left corner rather than top-left.
        for (Point p : points) {
            xsInput[this.size] = (double) p.getX();
            ysInput[this.size++] = (double) IVMdrawGWT.hoogte - p.getY();
        }
    }

    public LineData(Matrix m) {
        this.xsInput = m.transpose().values()[0];
        this.ysInput = m.transpose().values()[1];
        this.size = xsInput.length;
    }


    public double[] getXs() {
        return xsInput;
    }


    public double[] getYs() {
        return ysInput;
    }


    public static double maxValue(double[] values) {
        double max = Integer.MIN_VALUE;

        for (int i = 0; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
            }
        }

        return max;
    }


    public static double minValue(double[] values) {
        double min = Integer.MAX_VALUE;

        for (int i = 0; i < values.length; i++) {
            if (values[i] < min) {
                min = values[i];
            }
        }

        return min;
    }


    public double maxX() {
        return maxValue(this.xsInput);
    }


    public double minX() {
        return minValue(this.xsInput);
    }


    public double maxY() {
        return maxValue(this.ysInput);
    }


    public double minY() {
        return minValue(this.ysInput);
    }

    /**
     * Checks whether the input Points are valid x and y values are never decreasing.
     * TODO: Currently still a simple implementation, should be invariant to inputerrors.
     * @return boolean true of input is valid, false otherwise
     */
    public boolean validInput() {
        double prevX = xsInput[0];
        double prevY = ysInput[0];

        for (int i = 1; i < this.size; i++) {
            if (this.xsInput[i] < prevX || this.ysInput[i] < prevY) {
                return false;
            }
            prevX = xsInput[i];
            prevY = ysInput[i];
        }

        return true;
    }


    private void switchToCanvasCoordinates() {
        double[] ysCopy = new double[this.ysInput.length];
        int ctr = 0;

        for (double p : ysInput) {
            ysCopy[ctr] = (double) IVMdrawGWT.hoogte - ysInput[ctr++];
        }

        this.ysInput = ysCopy;
    }


    public void drawnInContext(Context2d context, int contextHeight, int contextWidth) {
        this.switchToCanvasCoordinates();

        double xAdjust = this.minX();
        double yAdjust = this.minY();

        double scaleX = contextWidth / (this.maxX() - xAdjust);
        double scaleY = contextHeight / (this.maxY() - yAdjust);

        context.moveTo((xsInput[0] - xAdjust) * scaleX, (ysInput[0] - yAdjust) * scaleY);

        for (int i = 1; i < this.size; i++) {
            double newX = (xsInput[i] - xAdjust) * scaleX;
            double newY = (ysInput[i] - yAdjust) * scaleY;

            logger.log(Level.SEVERE, "" + newX + " , " + newY);
            context.lineTo(newX, newY);

        }

        context.stroke();
    }


    @Override
    public String toString() {
        return "LineData: (" + Arrays.toString(xsInput) + ", " + Arrays.toString(ysInput) + ")";
    }
}
