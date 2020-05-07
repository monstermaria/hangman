package com.example.hangman;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class PlayGameActivity extends AppCompatActivity {

    HangmanModel game;
    TextView instructionsView, triesView, infoView, timerView;
    TextView showWord;
    EditText makeAGuess;
    Button newGameButton;
    CountDownTimer timer;

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

        // load words from resources, differs depending on chosen language
        String[] words = getResources().getStringArray(R.array.words);
        // get a random word
        Random random = new Random();
        String word = words[random.nextInt(words.length)];

        if (gameSetup != null && gameSetup.equals("new")) {
            // start new game
            game = new HangmanModel(word);
        } else {
            // try to load saved game
            // (gameSetup == null) happens when you use the navigation bar to get back from
            // the status activity, and that means there is a game to load
            SharedPreferences prefs = getSharedPreferences("Hangman", MODE_PRIVATE);
            boolean gameLoaded = loadGame(prefs);
            if (!gameLoaded) {
                game = new HangmanModel(word);
            }
        }

        game.updateGameState();
    }

    boolean loadGame(SharedPreferences prefs) {
        String word;
        String letters;
        int round;
        int seconds;

        // get saved values
        word =  prefs.getString("wordToGuess", null);
        letters = prefs.getString("usedLetters", null);
        round = prefs.getInt("hangRound", 0);
        seconds = prefs.getInt("secondsLeft", 0);

        Log.d("loaded values", "word = " + word);
        Log.d("loaded values", "letters = " + letters);
        Log.d("loaded values", "round = " + round);
        Log.d("loaded values", "seconds = " + seconds);

        // check values
        if (word != null && letters != null) {
            game = new HangmanModel(word, letters, round, seconds);
            return true;
        } else {
            return false;
        }
    }

    void initializeGUI() {
        instructionsView = findViewById(R.id.instructionsView);
        instructionsView.setText(getString(R.string.instructions, game.getWordToGuess().length()));
        triesView = findViewById(R.id.triesView);
        timerView = findViewById(R.id.timerView);
        infoView = findViewById(R.id.infoView);
        infoView.setText("");
        showWord = findViewById(R.id.showWordView);
        makeAGuess = findViewById(R.id.guessView);
        newGameButton = findViewById(R.id.startNewGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("start", "start new game");
                setupGame(("new"));
                initializeGUI();
                setupTimer();
            }
        });

        setupTimer();

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

        checkStatusAndUpdateUI();
    }

    void handleInput(CharSequence input) {

        if (input.length() > 0) {
            int message = game.handleInput(input);
            infoView.setText(message);
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            checkStatusAndUpdateUI();
        }
    }

    void checkStatusAndUpdateUI() {

        game.updateGameState();

        triesView.setText(getString(R.string.status, game.getHangRound()));
        showWord.setText(game.getShowWord());
        // update timer view
        timerView.setText(getString(R.string.timer_text, game.getSecondsLeft()));

        if (game.getGameState() != game.ACTIVE) {
            
            if (game.getGameState() == game.WON) {
                infoView.setText(getString(R.string.status_won, game.getHangRound()));
            }
            
            if (game.getGameState() == game.LOST) {
                infoView.setText(getString(R.string.status_lost));
            }

            // stop the timer
            timer.cancel();

            // hide input field so the player can't continue to make guesses
            makeAGuess.setVisibility(View.INVISIBLE);

            // show button to start new game
            newGameButton.setVisibility(View.VISIBLE);

            // remove saved game
            SharedPreferences.Editor editor = getSharedPreferences(
                    "Hangman",
                    MODE_PRIVATE
            ).edit();

//            editor.clear();
            editor.putString("wordToGuess", null);
            editor.apply();
//            editor.commit();

            Log.d("save", "deleting game");
        } else {
            // show input field
            makeAGuess.setVisibility(View.VISIBLE);

            // hide button to start new game
            newGameButton.setVisibility(View.INVISIBLE);
        }
    }

    void setupTimer() {

        long milliSeconds = game.getSecondsLeft() * 1000;
        timer = new CountDownTimer(milliSeconds, 1000) {

            public void onTick(long millisUntilFinished) {
                // update game model
                int seconds = (int) (millisUntilFinished / 1000);
                game.setSecondsLeft(seconds);

                // update UI
                checkStatusAndUpdateUI();
            }

            public void onFinish() {
                // update game model
                game.setSecondsLeft(0);

                // update UI
                checkStatusAndUpdateUI();
            }
        };
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

        setupTimer();
        timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("lifecycle", "onPause " + this);

        timer.cancel();

        // update saved game
        saveStatus();
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
        SharedPreferences.Editor editor = getSharedPreferences(
                "Hangman",
                MODE_PRIVATE
        ).edit();

        editor.putString("wordToGuess", game.getWordToGuess());
        editor.putString("usedLetters", game.getUsedLetters());
        editor.putInt("hangRound", game.getHangRound());
        editor.putInt("secondsLeft", game.getSecondsLeft());

        editor.apply();
//        editor.commit();

        Log.d("save", "saving game");
    }

    public void showStatus(View view) {
        Intent intent = new Intent(this, StatusActivity.class);
        intent.putExtra("hangRound", game.getHangRound());
        intent.putExtra("usedLetters", game.getUsedLetters());
        intent.putExtra("showWord", game.getShowWord());
        intent.putExtra("secondsLeft", game.getSecondsLeft());
        startActivityForResult(intent, 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("result", "We got a result!");

        if (requestCode == 4) {
            Log.d("result", "Result from show status, resultCode = " + resultCode);
            // handle result from StatusActivity.class
            if (resultCode == 8) {
                String message = data.getStringExtra("message");
                if (message != null) {
                    Log.d("result", message);
                } else {
                    Log.d("result", "No message returned");
                }
            }
        }
    }
}
