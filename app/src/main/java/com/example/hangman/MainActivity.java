package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("lifecycle", "onCreate " + this);
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

        // hide continue game if there is no saved game
        SharedPreferences prefs = getSharedPreferences("Hangman", MODE_PRIVATE);
        if (prefs == null) {
            findViewById(R.id.continueGame).setVisibility(View.INVISIBLE);
        } else {
            String wordToGuess = prefs.getString("wordToGuess", null);
            if (wordToGuess == null) {
                findViewById(R.id.continueGame).setVisibility(View.INVISIBLE);
            }
        }
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

    public void startGame(View view) {
        Intent intent = new Intent(this, PlayGameActivity.class);
        intent.putExtra("game", "new");
        startActivity(intent);
    }

    public void continueGame(View view) {
        Intent intent = new Intent(this, PlayGameActivity.class);
        intent.putExtra("game", "saved");
        startActivity(intent);
    }
}
