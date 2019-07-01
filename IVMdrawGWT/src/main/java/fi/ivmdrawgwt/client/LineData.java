/*
 * File:    LineData.java
 *
 * Mainly used for preperation of data, checking validity and converting between coordinate
 * systems.
 */

package fi.ivmdrawgwt.client;

import com.google.gwt.canvas.dom.client.Context2d;

import java.util.ArrayList;
import java.util.Arrays;


/**

 */
public class LineData {
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


    /**
     * Get the max value of a given sequence of double values.
     * @param values
     * @return double (max value)
     */
    public static double maxValue(double[] values) {
        double max = Integer.MIN_VALUE;

        for (int i = 0; i < values.length; i++) {
            if (values[i] > max) {
                max = values[i];
            }
        }

        return max;
    }


    /**
     * Get the min value of a given sequence of double values.
     * @param values
     * @return double (min value)
     */
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
     * @return boolean true of input is valid, false otherwise
     */
    public boolean validInput() {
        return (maxDecreasingSequence(xsInput) < 20) && (maxDecreasingSequence(ysInput) < 20);
    }

    /**
     * Find the largest amout of consecutive values that are decreasing.
     * @param values
     * @return the amount of decreasing consecutive values
     */
    public static int maxDecreasingSequence(double[] values) {
        double prevVal = values[0];
        int maxSequence = 0;
        int curSequence = 0;


        for (int i = 1; i < values.length; i++) {
            if (values[i] < prevVal) {
                curSequence++;
            } else {
                if (curSequence > maxSequence) {
                    maxSequence = curSequence;
                }
                curSequence = 0;
            }

            prevVal = values[i];
        }

        return maxSequence;
    }

    /**
     * Application has origin positioned at the top-left. This convert the values to a coordinate system
     * with the origin positioned at the bottom-left.
     */
    private void switchToCanvasCoordinates() {
        double[] ysCopy = new double[this.ysInput.length];
        int ctr = 0;

        for (double p : ysInput) {
            ysCopy[ctr] = (double) IVMdrawGWT.hoogte - ysInput[ctr++];
        }

        this.ysInput = ysCopy;
    }


    @Override
    public String toString() {
        return "LineData: (" + Arrays.toString(xsInput) + ", " + Arrays.toString(ysInput) + ")";
    }
}
