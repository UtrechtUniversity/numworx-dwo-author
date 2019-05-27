package fi.ivmdrawgwt.client;

import fi.ivmdrawgwt.client.Matrix;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO: Hoe implementatie van weighted least squares.
 * Provides data smoothing functionality for matrices.
 */
public class Smoothing {

    private static Logger logger = Logger.getLogger("meuk");


    /**
     * Calculates the coefficients for the least square solution of the given order.
     * @param xs
     * @param ys
     * @param order
     * @return
     */
    public static Matrix leastSquaresParams(double[] xs, double[] ys, int order) {
        double[][] vandermonde  = new double[xs.length][order+1];

        for (int i = 0; i < xs.length; i++) {
            for (int j = 0; j <= order; j++) {
                vandermonde[i][j] = Math.pow(xs[i], j);
            }
        }

        Matrix aM = new Matrix(vandermonde);

        /* Creates a vector of the y values. */
        double[][] b = new double[ys.length][1];
        for (int i = 0; i < ys.length; i++) {
            b[i][0] = ys[i];
        }

        Matrix bM = new Matrix(b);

        /* Standard least square matrix definition. */
        return aM.transpose().dot(aM).invert().dot(aM.transpose()).dot(bM);
    }


    private static double minValue(double[] values) {
        double minValue = Double.MAX_VALUE;

        for (int i = 0; i < values.length; i++) {
            if (values[i] < minValue) {
                minValue = values[i];
            }
        }

        return minValue;
    }

    private static double maxValue(double[] values) {
        double maxValue = Double.MIN_VALUE;

        for (int i = 0; i < values.length; i++) {
            if (values[i] > maxValue) {
                maxValue = values[i];
            }
        }

        return maxValue;
    }


    /**
     * Uses the given parameters to smooth a dataset by fitting the least square solution.
     * TODO: original implementation uses linspace between xmin and xmax instead of given x values
     * @param params
     * @param xs
     * @param ys
     * @param pointAmount
     * @return
     */
    public static Matrix poly1d(double[] params, double[] xs, int pointAmount) {
        double[] newYs = new double[pointAmount];

        for (int i = 0; i < xs.length; i++) {
            for (int p = 0; p < params.length; p++) {
                newYs[i] += params[p] * Math.pow(xs[i], p);
            }
        }

        return new Matrix(xs, newYs);
    }


    /**
     * Calculate the best fit via least square solution of the given order.
     * @param xs
     * @param ys
     * @param order
     * @param pointAmount
     * @return
     */
    public static Matrix leastSquares(double[] xs, double[] ys, int order, int pointAmount) {
        double[] params = leastSquaresParams(xs, ys, order).transpose().values()[0]; // Stores the params in single array.
        Matrix fittedPoints = poly1d(params, xs, pointAmount);

//        logger.log(Level.SEVERE, "" + xs.length + ", " + pointAmount);

        return fittedPoints;
    }


    public static Matrix leastSquares(double[] xs, double[] ys, int order) {
        return leastSquares(xs, ys, order, ys.length);
    }


    public static Matrix leastSquares(Matrix inputPoints, int order) {
        Matrix transpose = inputPoints.transpose();

        return leastSquares(transpose.values()[0], transpose.values()[1], order);
    }


}
