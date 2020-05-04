package com.example.hangman;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class PlayGameActivity extends AppCompatActivity {

    HangmanModel game;
    TextView instructions, status;
    TextView showWord;
    EditText makeAGuess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        Log.d("lifecycle", "onCreate " + this);

        String gameSetup = getIntent().getStringExtra("game");
        setupGame(gameSetup);
        initializeGUI();
    }

    void setupGame(String gameSetup) {

        // load words from resources
        Random random = new Random();
        String[] words = getResources().getStringArray(R.array.words);
        String word = words[random.nextInt(words.length)];

        if (gameSetup != null && gameSetup.equals("new")) {
            // start new game
            game = new HangmanModel(null, word);
        } else {
            // try to load saved game
            // (gameSetup == null) happens when you use the navigation bar to get back from
            // the status activity, and that means there is a game to load
            SharedPreferences prefs = getSharedPreferences("Hangman", MODE_PRIVATE);
            game = new HangmanModel(prefs, word);
        }
    }

    void initializeGUI() {
        instructions = findViewById(R.id.instructionsView);
        instructions.setText(getString(R.string.instructions, game.wordToGuess.length()));
        status = findViewById(R.id.statusView);
        showWord = findViewById(R.id.showWordView);
        makeAGuess = findViewById(R.id.guessView);

        // set callback for when a guess has been made
        makeAGuess.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                Log.d("lifecycle", "makeAGuess.onEditorAction: " + v.getText() );
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    handleInput(v.getText());
                    v.setText("");
                    handled = true;
                }

                return handled;
            }
        });

        updateUIAndSaveStatus();
    }

    void handleInput(CharSequence input) {
        if (input.length() > 0) {
            int message = game.handleInput(input);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            updateUIAndSaveStatus();
        }
    }

    void updateUIAndSaveStatus() {
        
        status.setText(getString(R.string.status, game.hangRound));
        showWord.setText(new String(game.showWord));

        game.checkProgress();

        if (game.finished) {
            
            if (game.won) {
                Toast.makeText(this, "WON!", Toast.LENGTH_SHORT).show();
                status.setText(getString(R.string.status_won, game.hangRound));
            }
            
            if (game.lost) {
                Toast.makeText(this, "LOST!", Toast.LENGTH_SHORT).show();
                status.setText(getString(R.string.status_lost));
            }

            // remove saved game
            SharedPreferences.Editor editor = getSharedPreferences("Hangman", MODE_PRIVATE).edit();

            editor.clear();
            editor.apply();
            editor.commit();

        } else {
            // update saved game
            saveStatus();
        }
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("lifecycle", "onDestroy " + this);
    }
    
    void saveStatus() {
        SharedPreferences.Editor editor = getSharedPreferences("Hangman", MODE_PRIVATE).edit();

        StringBuilder usedLetters = new StringBuilder();

        for (int i = 0; i < game.usedLetters.size(); i ++) {
            usedLetters.append(game.usedLetters.get(i));
        }

        editor.putString("wordToGuess", game.wordToGuess);
        editor.putString("showWord", new String(game.showWord));
        editor.putInt("hangRound", game.hangRound);
        editor.putString("usedLetters", usedLetters.toString());

        editor.apply();
        editor.commit();
    }

    public void showStatus(View view) {
        Intent intent = new Intent(this, StatusActivity.class);
        intent.putExtra("hangRound", game.hangRound);
        intent.putExtra("usedLetters", String.valueOf(game.usedLetters));
        intent.putExtra("showWord", new String(game.showWord));
        startActivity(intent);
    }
}
