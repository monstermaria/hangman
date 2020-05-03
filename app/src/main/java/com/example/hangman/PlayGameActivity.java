package com.example.hangman;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PlayGameActivity extends AppCompatActivity {

    HangmanModel game;
    TextView showWord;
    EditText guessLetter, guessWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        Log.d("lifecycle", "onCreate " + this);

//        String[] words = getResources().getStringArray(R.array.words);

        SharedPreferences prefs = getSharedPreferences("Hangman", MODE_PRIVATE);

        game = new HangmanModel(prefs);
        showWord = findViewById(R.id.showWordView);
        guessLetter = findViewById(R.id.guessLetterView);
        guessWord = findViewById((R.id.guessWordView));

        // set callback for when a letter has been guessed
        guessLetter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                Log.d("lifecycle", "guessLetter.onEditorAction: " + v.getText() );
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    handleLetterInput(v.getText());
                    handled = true;
                }

                return handled;
            }
        });

        // set callback for when the word has been guessed
        guessWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                Log.d("lifecycle", "guessWord.onEditorAction: " + v.getText() + actionId );
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handleWordInput(v.getText());
                    handled = true;
                }

                return handled;
            }
        });
    }

    void handleLetterInput(CharSequence input) {
        if (input.length() > 0) {
            int message = game.handleLetterInput(input);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            updateUI();
        }
    }

    void handleWordInput(CharSequence input) {
        if (input.length() > 0) {
            Log.d("lifecycle", this + ".handleWordInput");
            int message = game.handleWordInput(input);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            updateUI();
        }
    }

    void updateUI() {
        game.checkProgress();
        if (game.won) {
            // do this
            Toast.makeText(this, "WON!", Toast.LENGTH_SHORT).show();
        }
        if (game.lost) {
            // do that
            Toast.makeText(this, "LOST!", Toast.LENGTH_SHORT).show();
        }
        showWord.setText(new String(game.showWord));
        guessLetter.setText("");
        guessWord.setText("");
    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.d("lifecycle", "onStart " + this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("lifecycle", "onRestart " + this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("lifecycle", "onResume " + this);

        updateUI();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("lifecycle", "onPause " + this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("lifecycle", "onStop " + this);

        SharedPreferences.Editor editor = getSharedPreferences("Hangman", MODE_PRIVATE).edit();

        editor.putString("wordToGuess", game.wordToGuess);
        editor.putString("showWord", new String(game.showWord));
        editor.putInt("hangRound", game.hangRound);
        editor.putString("usedLetters", String.valueOf(game.usedLetters));

        editor.apply();
        editor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("lifecycle", "onDestroy " + this);
    }

    void showStatus(View view) {
        Intent intent = new Intent(this, StatusActivity.class);

    }
}
