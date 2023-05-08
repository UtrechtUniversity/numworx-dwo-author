/*
 * File:    Feedback.java
 *
 * Creates String values that can be communicated to the user.
 */


package fi.ivmdrawgwt.client;

import java.util.Random;


public class Feedback {
    private static Random generator = new Random();

    /**
     * Randomly picks a sentence containing the information that the answer is correct.
     * @return String value stating that the answer is correct.
     */
    private static String randomCorrect() {
        int randomInt = generator.nextInt(5);
        String feedback = "";

        switch (randomInt) {
            case 0:
                feedback = IVMdrawGWT.rb.randomCorrect0();
                break;
            case 1:
                feedback = IVMdrawGWT.rb.randomCorrect1();
                break;
            case 2:
                feedback = IVMdrawGWT.rb.randomCorrect2();
                break;
            case 3:
                feedback = IVMdrawGWT.rb.randomCorrect3();
                break;
            case 4:
                feedback = IVMdrawGWT.rb.randomCorrect4();
                break;
        }

        return feedback;
    }


    /**
     * Randomly picks a sentence containing the information that the answer is incorrect.
     * @return String value stating that the answer is incorrect.
     */
    private static String randomIncorrect() {
        int randomInt = generator.nextInt(5);
        String feedback = "";

        switch (randomInt) {
            case 0:
                feedback = IVMdrawGWT.rb.randomIncorrect0();
                break;
            case 1:
                feedback = IVMdrawGWT.rb.randomIncorrect1();
                break;
            case 2:
                feedback = IVMdrawGWT.rb.randomIncorrect2();
                break;
            case 3:
                feedback = IVMdrawGWT.rb.randomIncorrect3();
                break;
            case 4:
                feedback = IVMdrawGWT.rb.randomIncorrect4();
                break;
        }

        return feedback;

    }


    /**
     * Constructs a feedback sentence based on the drawn degree, convexity and wether or not the answer is classified
     * as corect.
     * @param degree integer of the degree that is dertermined by the classifier.
     * @param convex Boolean for the convexity of the drawn input (determined by the classifier)
     *               true=convex, false=concave
     * @param correct Boolean for if the drawn line is correct or incorrect (determined by the classifier).
     * @return
     */
    public static String feedback(int degree, boolean convex, boolean correct, boolean goedFoutVisible) {
        String feedback = "";

        switch (degree) {
            case 1:
                feedback += IVMdrawGWT.feedbackCase1;
                break;
            case 2:
                //feedback += "Bij jouw grafiek neemt de stijging van de hoogte van het water in de vaas steeds";

                if (convex) {
                    feedback += IVMdrawGWT.feedbackCase2a;
                } else {
                    feedback += IVMdrawGWT.feedbackCase2b;
                }
                break;
            case 3:
                //feedback += "Bij jouw grafiek neemt de stijging van de hoogte van het water in de vaas eerst";

            	if (convex) {
                    feedback += IVMdrawGWT.feedbackCase3a;
                } else {
                    feedback += IVMdrawGWT.feedbackCase3b;
                }
                break;
            case 4:
                //feedback += "Bij jouw grafiek neemt de stijging van de hoogte van het water in de vaas ";

            	if (convex) {
                    feedback += IVMdrawGWT.feedbackCase4a;
                } else {
                    feedback += IVMdrawGWT.feedbackCase4b;
                }
                break;
        }
        if(goedFoutVisible) {
	        if (correct) {
	            feedback += " "+randomCorrect();
	        } else {
	            feedback += " "+randomIncorrect();
	        }
        }

        return feedback;
    }

    public static String feedback(int degree, boolean convex, boolean correct) {
    		return feedback( degree,  convex,  correct, true);
    }

    /**
     * States that the feedback is incorrect since the drawn input is a decreasing line.
     * @return String containing the feedback.
     */
    public static String decreasingLine() {
        String feedback = IVMdrawGWT.feedbackCase5;

        return feedback;
    }


    /**
     * States that the feedback is incorrect since the x values are decreasing.
     * @return String containing the feedback.
     */
    public static String decreasingXs() {
        String feedback = IVMdrawGWT.feedbackCase6;

        return feedback;
    }

}
