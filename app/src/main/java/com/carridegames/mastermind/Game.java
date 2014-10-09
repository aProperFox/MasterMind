package com.carridegames.mastermind;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Tyler on 9/29/2014.
 */
public class Game {


    public static enum Peg {
        RED, BLUE, GREEN, BLACK, PURPLE, ORANGE, YELLOW, GRAY, NONE;

        public int getDrawable(Peg peg) {
            switch(peg) {
                case RED:
                    return R.drawable.red;
                case BLUE:
                    return R.drawable.blue;
                case GREEN:
                    return R.drawable.green;
                case BLACK:
                    return R.drawable.black;
                case PURPLE:
                    return R.drawable.purple;
                case ORANGE:
                    return R.drawable.orange;
                case YELLOW:
                    return R.drawable.yellow;
                case GRAY:
                    return R.drawable.gray;
                default:
                    return R.drawable.white;

            }
        }
    };

    public class WinState {
        public int reds;
        public int whites;
        public WinState(int reds, int whites) {
            this.reds = reds;
            this.whites = whites;
        }

        public int getReds() {
            return reds;
        }

        public int getWhites() {
            return whites;
        }

        public void setReds(int reds) {
            this.reds = reds;
        }

        public void setWhites(int whites) {
            this.whites = whites;
        }

    }

    protected boolean areRepeatsAllowed;

    protected ArrayList<Peg> answer = new ArrayList<Peg>(4);

    // Constructor, if repeats are allowed, it will generate a list of 4 Pegs with any colors
    public Game(boolean areRepeatsAllowed) {
        this.areRepeatsAllowed = areRepeatsAllowed;
        generateRandomGuess(this.areRepeatsAllowed);

        ArrayList<Peg> sampleGuess = new ArrayList<Peg>(4);
        ArrayList<Peg> sampleAnswer = new ArrayList<Peg>(4);

        sampleGuess.add(Peg.ORANGE);
        sampleGuess.add(Peg.YELLOW);
        sampleGuess.add(Peg.GRAY);
        sampleGuess.add(Peg.PURPLE);

        sampleAnswer.add(Peg.GRAY);
        sampleAnswer.add(Peg.BLACK);
        sampleAnswer.add(Peg.BLUE);
        sampleAnswer.add(Peg.GRAY);

    }

    protected void generateRandomGuess(boolean repeatsAllowed) {
        Random rand = new Random();
        if(repeatsAllowed) {
            for(int i = 0; i < 4; i++) {
                answer.add(Peg.values()[rand.nextInt(8)]);
            }

        } else {
            ArrayList<Peg> allPegs = new ArrayList<Peg>(8);
            for(int i = 0; i < 8; i++)
                allPegs.add(Peg.values()[i]);


            for(int i = 0; i < 4; i++) {
                int randomPeg = rand.nextInt(allPegs.size());
                answer.add(allPegs.get(randomPeg));
                allPegs.remove(randomPeg);
            }
        }
    }

    protected ArrayList<Peg> getAnswer() {
        return answer;
    }

    protected WinState checkAnswer(ArrayList<Peg> guess) {

        int reds = 0, whites = 0;

        ArrayList<Peg> tempGuess = new ArrayList<Peg>(4);
        tempGuess = (ArrayList<Peg>)guess.clone();
        ArrayList<Peg> tempAnswer = new ArrayList<Peg>(4);
        tempAnswer = (ArrayList<Peg>)answer.clone();

        // Overwrite exact guesses
        for(int i =0; i < 4; i++) {
            if(tempAnswer.get(i).equals(tempGuess.get(i))) {
                reds++;
                tempAnswer.set(i, Peg.NONE);
                tempGuess.set(i, Peg.NONE);
            }
        }

        // Find right color wrong position guesses
        for(int i =0; i < 4; i++) {
            for (int j=0; j < 4; j++) {
                // We already checked this case above!
                if(j != i) {
                    if (tempGuess.get(i) != Peg.NONE && tempAnswer.get(j) != Peg.NONE && tempAnswer.get(j).equals(tempGuess.get(i))) {
                        whites++;
                        tempAnswer.set(j, Peg.NONE);
                        tempGuess.set(i, Peg.NONE);
                    }
                }
            }
        }

        return new WinState(reds, whites);
    }

    private void assertGuess(ArrayList<Peg> guess, ArrayList<Peg> answer, WinState winState) {
        this.answer = answer;
        Assert.assertEquals(checkAnswer(guess).getReds(), winState.getReds());
        Assert.assertEquals(checkAnswer(guess).getWhites(), winState.getWhites());
    }

}
