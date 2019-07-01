/*
 * File:    Derivatives.java
 *
 * Provides several functions for obtaining derivatives. The derivatives are mainly used for the classification,
 * which is implemented in Classifier.java.
 */


package fi.ivmdrawgwt.client;

public class Derivatives {

    /**
     * Determines the derivatives algebraically by using the coefficients and their corresponding exponents.
     * @param coefficients values representing the coefficients of the polynomial.
     * @param xs double array representing the xs.
     * @return coefficients (double[]) of the derivative of the polynomial
     */
    public static double[] paramDerivative(double[] coefficients, double[] xs) {
        double[] newCoeff = new double[coefficients.length - 1];

        for(int i = 1; i < coefficients.length; i++) {
            newCoeff[i - 1] = i * coefficients[i];
        }

        return newCoeff;
    }


    /**
     * Determines the derivative via a numerical approach.
     * @param points Matrix object containing the input points
     * @param order order of the derivative (can only be 1 or 2)
     * @return
     */
    public static Matrix gradientDerivative(Matrix points, int order) {
        if (order < 1 || order > 2) {
            throw new IllegalArgumentException("Order must be either 1 or 2.");
        }

        if (order == 1) {
            return centralPointDerivative(points);
        } else {
            return centralPointDerivative(centralPointDerivative(points));
        }
    }


    /**
     * Calculate the derivative numerically by using the central point derivative on the given input points.
     * @param points Matrix containing the input points.
     * @return Matrix points of the derivative values.
     */
    private static Matrix centralPointDerivative(Matrix points) {
        double[] newXs = new double[points.xLength() - 2];
        double[] newYs = new double[points.yLength() - 2];

        double[] givenXs = points.xValues();
        double[] givenYs = points.yValues();

        for (int i = 1; i < points.yLength()-2; i++) {
            newYs[i - 1] = (givenYs[i+1] - givenYs[i-1]) / (2);
        }

        for (int i = 0; i < points.xLength()-2; i++) {
            newXs[i] = givenXs[i+1];
        }

        return new Matrix(newXs, newYs);
    }
}
