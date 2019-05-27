package fi.ivmdrawgwt.client;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * TODO: Misschien ipv Matrix van correct points opslaan gewoon enkel de degree en convexity opslaan.
 * Provides functionality to classify a drawn line (repr. by Matrix) as either correct or incorrect.
 */
public class Classifier {
    private Logger logger = Logger.getLogger("Classifier");

    private Matrix inputPoints;
    private Matrix fittedPoints;
    private double[] lsParams;

    private int inputDegree;
    private int correctDegree;

    private boolean inputConvexity;
    private boolean correctConvexity;


    Classifier(Matrix inputPoints, int correctDegree, boolean correctConvexity) {
        this.inputPoints = inputPoints;
        this.correctDegree = correctDegree;
        this.correctConvexity = correctConvexity;

        this.inputDegree = this.findDegree();
        this.lsParams = Smoothing.leastSquaresParams(this.inputPoints.xValues(),
                                                     this.inputPoints.yValues(),
                                                     this.inputDegree).transpose().values()[0];

        this.fittedPoints = Smoothing.leastSquares(this.inputPoints, this.inputDegree);
        this.inputConvexity = this.convexOrConcave();
    }


    public boolean classify() {
        boolean correctClassified = false;

//        boolean inputConvexity = convexOrConcave(input);

        boolean classifiedDegree = this.classifyDegree();
        boolean classifiedConvex = this.classifyConvexity();

        if (classifiedDegree && classifiedConvex) {
            correctClassified = true;
        }

        // TODO: hier feedback callen
        String feedback = Feedback.feedback(inputDegree, inputConvexity, correctClassified);

        return correctClassified;

    }

    public String getFeedback() {
        return Feedback.feedback(this.inputDegree, this.inputConvexity, this.classify());
    }


    public int findDegree() {
        int upToDegree = 4;
        double[] errors = new double[upToDegree];

        for (int d = 0; d < upToDegree; d++) {
            Matrix fittedPoints = Smoothing.leastSquares(this.inputPoints, d + 1);

            errors[d] = meanSquaredError(this.inputPoints.yValues(), fittedPoints.yValues());
        }

//        logger.log(Level.SEVERE, this.inputPoints.toString());
//        logger.log(Level.SEVERE, "Errors: " + Arrays.toString(errors));

        if (errors[0] < 50) {
            logger.log(Level.SEVERE, "Degree: " + 1);

            return 1;
        } else {
            logger.log(Level.SEVERE, "Degree: " + (indexOfLargestDiff(errors) + 1));
            return indexOfLargestDiff(errors) + 1;
        }
    }


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


    private static double meanSquaredError(double[] inputYs, double[] fittedYs) {
        double error = 0.0;

        for (int i = 0; i < inputYs.length; i++) {
            error += Math.pow(inputYs[i] - fittedYs[i], 2);
        }

        return error / inputYs.length;
    }



    private boolean classifyDegree() {
        return this.inputDegree == this.correctDegree;
    }



    private boolean classifyConvexity() {
        return this.inputConvexity == this.correctConvexity;
    }


    public boolean convexOrConcave() {
//        Matrix secondDeriv = Derivatives.gradientDerivative(this.fittedPoints, 2);
        double[] xs = this.inputPoints.xValues();
        double[] firstDerivParams = Derivatives.paramDerivative(this.lsParams, xs);
        double[] secondDerivParams = Derivatives.paramDerivative(firstDerivParams, xs);

        Matrix secondDeriv = Smoothing.poly1d(secondDerivParams, xs, xs.length);

        if (secondDeriv.yValues()[0] < 0.0) {
            this.logger.log(Level.SEVERE, "Convex: True");
            return true;
        }

        logger.log(Level.SEVERE, "Convex: False");


        return false;
    }


}
