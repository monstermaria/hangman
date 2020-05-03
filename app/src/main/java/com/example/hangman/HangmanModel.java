package com.example.hangman;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Random;

class HangmanModel {

    String[] words = { "rock", "scissors", "paper" };
    Random random = new Random();

    String wordToGuess;
    char[] showWord;
    int numberOfTries = 10, hangRound = 0;
    boolean won = false, lost = false, finished = false;

    ArrayList<Character> usedLetters = new ArrayList<Character>(30);

    HangmanModel(SharedPreferences prefs) {
        if (prefs != null) {
            Log.d("lifecycle", prefs.toString());
            // restore game

            String savedInfo =  prefs.getString("wordToGuess", null);
            if (savedInfo == null) {
                // start new game
                wordToGuess = words[random.nextInt(words.length)];
                setupGame(wordToGuess);
            } else {
                Log.d("savedInfo", savedInfo);
                wordToGuess = savedInfo;
                savedInfo = prefs.getString("showWord", null);
                showWord = savedInfo.toCharArray();
                hangRound = prefs.getInt("hangRound", 0);
                savedInfo = prefs.getString("usedLetters", null);
                usedLetters = new ArrayList<Character>(30);
                for (int i = 0; i < savedInfo.length(); i++) {
                    usedLetters.add(savedInfo.charAt(i));
                }
            }
        }
    }

    void checkProgress() {
        if (new String(showWord).equals(wordToGuess)) {
            won = true;
            finished = true;
        }
        if (hangRound == numberOfTries) {
            lost = true;
            finished = true;
        }
    }


    public void oldMain() {

        boolean continueGame = true;
        while (continueGame) {

            String command = "getCommand()";

            switch (command) {
                case "s":
                    int triesLeft = numberOfTries - hangRound;
                    System.out.print("Så här ser det ut just nu: ");
                    System.out.println(showWord);
                    printHangman();
                    System.out.println("Du har förbrukat " + hangRound + " försök.");
                    System.out.println("Du har " + triesLeft + " försök kvar.");
                    System.out.print("Du har använt dessa bokstäver: ");
                    System.out.println(usedLetters);
                    break;
            }

            if (numberOfTries - hangRound <= 0) {

                System.out.println("Tyvärr så har du nu använt alla dina gissningar. Bättre lycka nästa gång!");
                continueGame = false;
            }
        }
    }

    private void setupGame(String wordToGuess) {

        showWord = wordToGuess.toCharArray();

        for (int i = 0; i < wordToGuess.length(); i++) {

            showWord[i] = '_';
        }

//        System.out.println("Välkommen till Hangman!");
//        System.out.println("Ordet du ska gissa är " + showWord.length + " bokstäver långt.");
//        System.out.println("Du har " + numberOfTries + " försök på dig. Lycka till!");
    }

    int handleLetterInput(CharSequence input) {
        if (input.length() > 1) {
           return R.string.too_many_letters;
        }

        char letter = input.charAt(0);
        if (Character.isLetter(letter)) {
            letter = Character.toLowerCase(letter);
            if (usedLetters.contains(letter)) {
                return R.string.letter_already_used;
            }

            usedLetters.add(letter);
            if (wordToGuess.contains(String.valueOf(letter))) {
                for (int i = 0; i < showWord.length; i++) {
                    if (wordToGuess.charAt(i) == letter) {
                        showWord[i] = letter;
                    }
                }
                return R.string.letter_in_word;
            } else {
                // letter is not part of the word, one try spent
                hangRound++;
                return R.string.letter_not_in_word;
            }
        } else {
            return R.string.not_a_letter;
        }
    }

    int handleWordInput(CharSequence input) {
        String word = input.toString().toLowerCase();
        if (word.equals(wordToGuess)) {
            for (int i = 0; i < showWord.length; i++) {
                showWord[i] = word.charAt(i);
            }
            return R.string.letter_in_word;
        } else {
            // word is not correct, one try spent
            hangRound++;
            return R.string.letter_not_in_word;
        }
    }

    void win() {

        System.out.println("Du gissade rätt! Grattis till vinsten!");
        System.out.println("Du gissade fel " + hangRound + " gånger.");
    }

    void fail() {

        hangRound++;
        printHangman();
        System.out.println("Tyvärr, det var fel.");
        System.out.println("Du har använt " + hangRound + " försök.");
    }

    void printHangman() {

        String[] hangman = {
                "\n\n\n\n\n\n",
                "\n\n\n\n\n\n/|\\",
                "\n |\n |\n |\n |\n |\n/|\\",
                " ____\n |\n |\n |\n |\n |\n/|\\",
                " ____\n |  |\n |\n |\n |\n |\n/|\\",
                " ____\n |  |\n |  o\n |\n |\n |\n/|\\",
                " ____\n |  |\n |  o\n |  |\n |\n |\n/|\\",
                " ____\n |  |\n |  o\n | /|\n |\n |\n/|\\",
                " ____\n |  |\n |  o\n | /|\\\n |\n |\n/|\\",
                " ____\n |  |\n |  o\n | /|\\\n | /\n |\n/|\\",
                " ____\n |  |\n |  o\n | /|\\\n | / \\\n |\n/|\\"
        };

        System.out.println(hangman[hangRound]);

    }
}
