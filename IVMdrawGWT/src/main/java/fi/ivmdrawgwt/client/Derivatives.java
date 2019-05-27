package fi.ivmdrawgwt.client;

public class Derivatives {

    public static double[] paramDerivative(double[] coefficients, double[] xs) {
        double[] newCoeff = new double[coefficients.length - 1];

        for(int i = 1; i < coefficients.length; i++) {
            newCoeff[i - 1] = i * coefficients[i];
        }

        return newCoeff;
    }


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
     * TODO: misschien moet ik eerst zorgen dat points 500 elementen al heeft zodat stapgrootte altijd gelijk is.
     * @param points
     * @return
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
