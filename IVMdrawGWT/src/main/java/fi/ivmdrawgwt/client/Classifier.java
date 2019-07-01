/*
 * File:    Classifier.java
 *
 * Uses both a Matrix object containing the drawn input and a vase number to determine if the drawn line is correct.
 * Classification is done by calculating the most likely degree and convexity. These two will be compared
 * with what is expected from a correct answer.
 */

package fi.ivmdrawgwt.client;


/**
 * Provides functionality to classify a drawn line (repr. by Matrix) as either correct or incorrect.
 */
public class Classifier {
    private Matrix inputPoints;
    private Matrix fittedPoints;

    private double[] lsParams;
    private double[] MSEs;

    private int inputDegree;
    private int correctDegree;

    private boolean inputConvexity;
    private boolean correctConvexity;

    public Matrix unclearPoints1;
    public Matrix unclearPoints2;

    /**
     * Constructor to create a Classifier object based on a Matrix of inputpoints (drawn line) and the correct vaasnr.
     * @param inputPoints
     * @param correctVaasNummer
     */
    Classifier(Matrix inputPoints, int correctVaasNummer) {
        this.inputPoints = inputPoints;
        this.setAnswerValues(correctVaasNummer);

        this.inputDegree = this.findDegree();
        this.lsParams = Smoothing.leastSquaresParams(this.inputPoints.xValues(),
                                                     this.inputPoints.yValues(),
                                                     this.inputDegree).transpose().values()[0];

        this.fittedPoints = Smoothing.leastSquares(this.inputPoints, this.inputDegree);
        this.inputConvexity = this.convexOrConcave();
    }


    /**
     * Harcoded solution to pair vasenumber to expected classifier output.
     * @param correctVaasNummer Integer of the asked vasequestion.
     */
    public void setAnswerValues(int correctVaasNummer) {
        switch (correctVaasNummer) {
            case 1:
                this.correctConvexity = false;
                this.correctDegree = 3;
                break;
            case 2:
                this.correctConvexity = false;
                this.correctDegree = 2;
                break;
            case 3:
                this.correctConvexity = true;
                this.correctDegree = 1;
                break;
            case 4:
                this.correctConvexity = true;
                this.correctDegree = 3;
                break;
            case 5:
                this.correctConvexity = true;
                this.correctDegree = 2;
                break;
            default:
                this.correctConvexity = true;
                this.correctDegree = 1;
                break;
        }
    }


    /**
     * The function that determines of the drawn input line is correct.
     * @return Boolean (true if correct, false otherwise)
     */
    public boolean classify() {
        boolean correctClassified = false;
        boolean classifiedDegree = this.classifyDegree();
        boolean classifiedConvex = this.classifyConvexity();

        if (classifiedDegree && classifiedConvex) {
            correctClassified = true;
        }

        return correctClassified;
    }


    /**
     * return feedback based on te drawn line and correct answer.
     * @return String representing the feedback.
     */
    public String getFeedback() {
        return Feedback.feedback(this.inputDegree, this.inputConvexity, this.classify());
    }


    /**
     * Find the degree by comparing MSE values of each possible degree.
     * @return
     */
    public int findDegree() {
        int upToDegree = 4;
        double[] errors = new double[upToDegree];

        for (int d = 0; d < upToDegree; d++) {
            Matrix fittedPoints = Smoothing.leastSquares(this.inputPoints, d + 1);
            errors[d] = meanSquaredError(this.inputPoints.yValues(), fittedPoints.yValues());
        }

        this.MSEs = errors;

        if (errors[0] < 100) {
            return 1;
        } else {
            int indx = indexOfLargestDiff(errors);

            return indx + 1;
        }
    }


    /**
     * Helper function to find the largest consecutive difference in a given array.
     * @param values (double array)
     * @return position where the largest value change occurs.
     */
    private static int indexOfLargestDiff(double[] values) {
        double largestDiff = 0.0;
        int location = 0;
        double previousValue = values[0];

        for (int i = 1; i < values.length; i++) {
            double diff = previousValue - values[i];

            if (diff > largestDiff) {
                largestDiff = diff;
                location = i;
            }

            previousValue = values[i];
        }

        return location;
    }


    /**
     * Calculate the mean squared error between input and fitted y-values.
     * Since x-values are for both are expected to be equal, only y-values are given.
     * @param inputYs y-values of the input points.
     * @param fittedYs y-values of the fitted points.
     * @return MSE value
     */
    private static double meanSquaredError(double[] inputYs, double[] fittedYs) {
        double error = 0.0;

        for (int i = 0; i < inputYs.length; i++) {
            error += Math.pow(inputYs[i] - fittedYs[i], 2);
        }

        return error / inputYs.length;
    }


    /**
     * Check if the degree is correct.
     * @return Boolean (true if correct, false otherwise)
     */
    private boolean classifyDegree() {
        return this.inputDegree == this.correctDegree;
    }


    /**
     * Check if the convexity is correct.
     * @return Boolean (true if correct, false otherwise)
     */
    private boolean classifyConvexity() {
        return this.inputConvexity == this.correctConvexity;
    }


    /**
     * Determine whether the drawn line is either convex or concave.
     * @return Boolean (true if concave, false if convex)
     */
    public boolean convexOrConcave() {
        double[] xs = this.inputPoints.xValues();
        double[] firstDerivParams = Derivatives.paramDerivative(this.lsParams, xs);
        double[] secondDerivParams = Derivatives.paramDerivative(firstDerivParams, xs);

        Matrix secondDeriv = Smoothing.poly1d(secondDerivParams, xs, xs.length);

        int amountBelowZero = 0;

        for (int i = 0; i < 10; i++) {
            if (secondDeriv.yValues()[0] < 0.0) {
                amountBelowZero += 1;
            }
        }

        return (amountBelowZero < 5);
    }
}