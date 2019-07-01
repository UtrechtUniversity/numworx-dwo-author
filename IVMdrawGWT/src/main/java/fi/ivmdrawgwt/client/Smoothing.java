/*
 * File:    Smoothing.java
 *
 * Provides data smoothing functionality.
 *
 */

package fi.ivmdrawgwt.client;

/**
 */
public class Smoothing {

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


    /**
     *
     * @param xs
     * @param ys
     * @param order
     * @return
     */
    public static Matrix leastSquaresParams(double[] xs, double[] ys, int order) {
        double[] gauss = complGaussianWindow(xs.length, xs.length/2, 2.0, 100);

        gauss[0] = 10000;

        if (order != 1) {
            gauss[3] = 10000;
        }
        return leastSquaresParams(xs, ys, order, gauss);
    }


    /**
     * Get a Matrix containing the given weights on the diagonals.
     * @param weights value of the weights
     * @return Matrix containing weights on diagonals.
     */
    public static Matrix getWeightMatrix(double[] weights) {
        double[][] wMatrix = new double[weights.length][weights.length];

        for (int i = 0; i < weights.length; i++) {
            wMatrix[i][i] = weights[i];
        }

        return new Matrix(wMatrix);
    }


    /**
     * Creates a complemented normal distribution functions which is used as weights for the weighted least squares.
     * @param n amount of points
     * @param mu as defined in the PDF of the normal distribution.
     * @param std as defined in the PDF of the normal distribution.
     * @param scale outputvalues are multiplied by this scale
     * @return
     */
    public static double[] complGaussianWindow(int n, int mu, double std, int scale) {
        double[] values = new double[n];

        for (int i = 0; i < n; i++) {
            values[i] = scale * (1 - (1 / Math.sqrt(2 * Math.PI * std)) * Math.pow(Math.E, - ((i - mu)*(i - mu)) / (2 * std)));
        }

        return values;
    }


    /**
     * Uses the given parameters to smooth a dataset by fitting the least square solution.
     * @return Matrix of new datapoints
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
     * @param xs x values
     * @param ys y values
     * @param order order of the polynomial to be fitted
     * @param pointAmount amount of new points
     * @return Matrix of fittedpoints
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
