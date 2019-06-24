package fi.ivmdrawgwt.client;

import fi.ivmdrawgwt.client.Matrix;

//import javax.sound.sampled.LineEvent;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
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
    public static Matrix leastSquaresParams(double[] xs, double[] ys, int order, double[] weights) {
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
        Matrix wM = getWeightMatrix(weights);

        /* Standard least square matrix definition. */
        return (aM.transpose().dot(wM).dot(aM)).invert().dot(aM.transpose()).dot(wM).dot(bM);
    }


    public static Matrix leastSquaresParams(double[] xs, double[] ys, int order) {
        double[] gauss = complGaussianWindow(xs.length, xs.length/2, 2.0, 100);
//        logger.log(Level.INFO, "Order: " + order);

        gauss[0] = 10000;

        if (order != 1) {
            // NOTE: evt kan dit ook 4 of 5 ofzo worden, later maar ff testen
            gauss[3] = 10000;
        }
        return leastSquaresParams(xs, ys, order, gauss);
    }


    public static Matrix getWeightMatrix(double[] weights) {
        double[][] wMatrix = new double[weights.length][weights.length];

        for (int i = 0; i < weights.length; i++) {
            wMatrix[i][i] = weights[i];
        }

        return new Matrix(wMatrix);
    }


    public static double[] complGaussianWindow(int n, int mu, double std, int scale) {
        double[] values = new double[n];

        for (int i = 0; i < n; i++) {
            values[i] = scale * (1 - (1 / Math.sqrt(2 * Math.PI * std)) * Math.pow(Math.E, - ((i - mu)*(i - mu)) / (2 * std)));
        }

        return values;
    }


    /**
     * Uses the given parameters to smooth a dataset by fitting the least square solution.
     * TODO: original implementation uses linspace between xmin and xmax instead of given x values
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
