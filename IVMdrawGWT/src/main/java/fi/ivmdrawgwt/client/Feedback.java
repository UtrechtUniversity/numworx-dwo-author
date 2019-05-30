package fi.ivmdrawgwt.client;


import java.util.Random;

public class Feedback {
    private static Random generator = new Random();

    private static String randomCorrect() {
        int randomInt = generator.nextInt(5);
        String feedback = "";

        switch (randomInt) {
            case 0:
                feedback = "En dat is correct voor deze vaas!";
                break;
            case 1:
                feedback = "Dit resulteert inderdaad in deze vaas!";
                break;
            case 2:
                feedback = "En dat is correct!";
                break;
            case 3:
                feedback = "Zo krijg je inderdaad de vaas in het plaatje!";
                break;
            case 4:
                feedback = "Dat is juist!";
                break;
        }

        return feedback;
    }


    private static String randomIncorrect() {
        int randomInt = generator.nextInt(5);
        String feedback = "";

        switch (randomInt) {
            case 0:
                feedback = "En dat is niet correct voor deze vaas.";
                break;
            case 1:
                feedback = "Dit resulteert niet in deze vaas!";
                break;
            case 2:
                feedback = "En dat is niet correct.";
                break;
            case 3:
                feedback = "Zo krijg je niet de vaas in het plaatje.";
                break;
            case 4:
                feedback = "Dit is niet juist";
                break;
        }

        return feedback;

    }


    public static String feedback(int degree, boolean convex, boolean correct) {
        String feedback = "";

        switch (degree) {
            case 1:
                feedback += "Bij jouw grafiek neemt de hoogte van de vaas met gelijke stappen toe. ";
                break;
            case 2:
                feedback += "Bij jouw grafiek neemt de stijging van de hoogte van het water in de vaas steeds";

                if (convex) {
                    feedback += " toe. ";
                } else {
                    feedback += " af. ";
                }
                break;
            case 3:
                feedback += "Bij jouw grafiek neemt de stijging van de hoogte van het water in de vaas eerst";

                if (convex) {
                    feedback += " toe en vervolgens langzaam af. ";
                } else {
                    feedback += " af en vervolgens langzaam toe. ";
                }
                break;
            case 4:
                feedback += "Bij jouw grafiek neemt de stijging van de hoogte van het water in de vaas ";

                if (convex) {
                    feedback += "eerst toe, dan af en daarna weer toe. ";
                } else {
                    feedback += "eerst af, dan toe en daarna weer af. ";
                }
                break;
        }

        if (correct) {
            feedback += randomCorrect();
        } else {
            feedback += randomIncorrect();
        }

        return feedback;
    }


    public static String decreasingLine() {
        String feedback = "Bij jouw grafiek neemt de hoogte van het water af als er meer water in de vaas komt. " +
                          "Dat is niet correct!";

        return feedback;
    }


    public static String decreasingXs() {
        String feedback = "Bij jouw grafiek ";

        return feedback;
    }

}
