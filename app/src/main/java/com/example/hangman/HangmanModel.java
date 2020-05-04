package com.example.hangman;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import java.util.ArrayList;
import java.util.Random;

class HangmanModel {

    String wordToGuess;
    char[] showWord;
    int hangRound = 0;
    boolean won = false;
    boolean lost = false;
    boolean finished = false;

    ArrayList<Character> usedLetters = new ArrayList<>(30);

    HangmanModel(SharedPreferences prefs, String word) {
//        Random random = new Random();
////        String[] words = {"kattunge", "javaprogrammering", "lokaltrafik", "blomsteräng"};
//        // trying to load words from resources
//        String[] words = getStringArray(R.array.words);
//        Resources res = getResources();
//        String[] planets = res.getStringArray(R.array.words);

        if (prefs != null && prefs.getString("wordToGuess", null) != null) {
            // restore game
            String savedInfo =  prefs.getString("wordToGuess", null);
            assert savedInfo != null;
            wordToGuess = savedInfo;
            Log.d("lifecycle", "wordToGuess: " + wordToGuess);

            savedInfo = prefs.getString("showWord", null);
            Log.d("lifecycle", "showWord: " + savedInfo);
            assert savedInfo != null;
            showWord = savedInfo.toCharArray();

            hangRound = prefs.getInt("hangRound", 0);

            savedInfo = prefs.getString("usedLetters", null);
            Log.d("lifecycle", "usedLetters: " + savedInfo);
            assert savedInfo != null;
            usedLetters = new ArrayList<>(30);
            for (int i = 0; i < savedInfo.length(); i++) {
                usedLetters.add(savedInfo.charAt(i));
            }
        } else {
            // start new game
            wordToGuess = word;

            showWord = wordToGuess.toCharArray();
            for (int i = 0; i < wordToGuess.length(); i++) {
                showWord[i] = '_';
            }
        }
    }

    void checkProgress() {
        if (new String(showWord).equals(wordToGuess)) {
            won = true;
            finished = true;
        }
        if (hangRound == 10) {
            lost = true;
            finished = true;
        }
    }

    int handleInput(CharSequence input) {
        if (input.length() == 1) {
            return handleLetterInput(input);
        } else if (input.length() == wordToGuess.length()) {
            return handleWordInput(input);
        } else {
            return R.string.wrong_number_of_letters;
        }
    }

    private int handleLetterInput(CharSequence input) {

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

    private int handleWordInput(CharSequence input) {
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
}
