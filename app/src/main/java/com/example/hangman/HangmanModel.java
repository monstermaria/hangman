package com.example.hangman;


import java.util.ArrayList;

class HangmanModel {

    final int ACTIVE = 0;
    final int WON = 1;
    final int LOST = 2;

    private String wordToGuess;
    private ArrayList<Character> usedLetters = new ArrayList<>(30);
    private int gameState = ACTIVE;
    private int hangRound = 0;
    private int secondsLeft = 60;


    HangmanModel(String word) {
        wordToGuess = word;
    }

    HangmanModel(String word, String letters, int round, int seconds) {
        wordToGuess = word;
        setUsedLetters(letters);
        hangRound = round;
        secondsLeft = seconds;
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
            // shortcut to win
            gameState = WON;
            return R.string.letter_in_word;
        } else {
            // word is not correct, one try spent
            hangRound++;
            return R.string.letter_not_in_word;
        }
    }

    void updateGameState() {
        if (getShowWord().equals(wordToGuess)) {
            gameState = WON;
        }
        if (hangRound >= 10 || secondsLeft <= 0) {
            gameState = LOST;
        }
    }

    // getters & setters

    int getGameState() {
        updateGameState();
        return gameState;
    }

    String getWordToGuess() {
        return wordToGuess;
    }

    String getShowWord() {
        StringBuilder showWord = new StringBuilder();

        for (int i = 0; i < wordToGuess.length(); i++) {
            char c = wordToGuess.charAt(i);

            if (usedLetters.contains(c)) {
                showWord.append(c);
            } else {
                showWord.append('_');
            }
        }

        return showWord.toString();
    }

    String getUsedLetters() {
        StringBuilder usedLettersString = new StringBuilder();

        for (int i = 0; i < usedLetters.size(); i ++) {
            usedLettersString.append(usedLetters.get(i));
        }

        return usedLettersString.toString();
    }

    private void setUsedLetters(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            usedLetters.add(letters.charAt(i));
        }
    }

    int getHangRound() {
        return hangRound;
    }

    int getSecondsLeft() {
        return secondsLeft;
    }

    void setSecondsLeft(int seconds) {
        secondsLeft = seconds;
        if (secondsLeft <= 0) {
            gameState = LOST;
        }
    }
}
