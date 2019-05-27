package fi.ivmdrawgwt.client;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.function.Predicate;

//import org.apache.commons.math3.fitting.PolynomialCurveFitter;
//import org.apache.commons.math3.linear.MatrixUtils;
//import org.apache.commons.math3.linear.RealMatrix;
//import org.apache.commons.math3.fitting.WeightedObservedPoints;

/**
 * TODO: Not sure if still required with Matrix class.
 */
public class LineData {
    private double xsInput[];
    private double ysInput[];
    private int size;

    public double[] getXs() {
        return xsInput;
    }

    public double[] getYs() {
        return ysInput;
    }

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


    @Override
    public String toString() {
        return "LineData: (" + Arrays.toString(xsInput) + ", " + Arrays.toString(ysInput) + ")";
    }
}
