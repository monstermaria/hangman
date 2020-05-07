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

        updateGameState();
    }

    int handleInput(CharSequence input) {
        int result;

        if (input.length() == 1) {
            result = handleLetterInput(input);
        } else if (input.length() == wordToGuess.length()) {
            result = handleWordInput(input);
        } else {
            result = R.string.wrong_number_of_letters;
        }

        updateGameState();

        return result;
    }

    private int handleLetterInput(CharSequence input) {
        int result;

        char letter = input.charAt(0);
        if (Character.isLetter(letter)) {
            letter = Character.toLowerCase(letter);
            if (usedLetters.contains(letter)) {
                result = R.string.letter_already_used;
            } else {
                usedLetters.add(letter);
                if (wordToGuess.contains(String.valueOf(letter))) {
                    result = R.string.letter_in_word;
                } else {
                    // letter is not part of the word, one try spent
                    hangRound++;
                    result = R.string.letter_not_in_word;
                }
            }
        } else {
            result = R.string.not_a_letter;
        }

        return result;
    }

    private int handleWordInput(CharSequence input) {
        int result;

        String word = input.toString().toLowerCase();
        if (word.equals(wordToGuess)) {
            // shortcut to win
            gameState = WON;
            result = R.string.letter_in_word;
        } else {
            // word is not correct, one try spent
            hangRound++;
            result = R.string.letter_not_in_word;
        }

        return result;
    }

    private void updateGameState() {
        if (getShowWord().equals(wordToGuess)) {
            gameState = WON;
        }
        if (hangRound >= 10 || secondsLeft <= 0) {
            gameState = LOST;
        }
    }

    // getters & setters

    int getGameState() {
        return gameState;
    }

    String getWordToGuess() {
        return wordToGuess;
    }

    String getShowWord() {
        if (gameState == WON) {
            return wordToGuess;
        }

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
