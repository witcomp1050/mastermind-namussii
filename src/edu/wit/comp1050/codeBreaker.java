package edu.wit.comp1050;

import java.util.ArrayList;

public interface codeBreaker
{
    int getCorrectGuesses(int[]guess, ArrayList<Integer> c);

    int getCorrectColors(int[]guess, ArrayList<Integer> c);
}
