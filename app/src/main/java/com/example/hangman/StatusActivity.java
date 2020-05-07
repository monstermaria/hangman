package com.example.hangman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class StatusActivity extends AppCompatActivity {

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

    public void showStatus() {

        TextView hangmanView = findViewById(R.id.hangmanView);
        TextView usedLettersView = findViewById(R.id.usedLettersView);
        TextView showWordView = findViewById(R.id.showWordView);
        TextView timeLeftView = findViewById(R.id.timeLeftView);

        Intent intent = getIntent();

        int hangRound = intent.getIntExtra("hangRound", 0);
        hangmanView.setText(hangman[hangRound]);

        String usedLetters = intent.getStringExtra("usedLetters");
        usedLettersView.setText(getString(R.string.used_letters, usedLetters));

        String showWord = intent.getStringExtra("showWord");
        showWordView.setText(showWord);

        int seconds = intent.getIntExtra("secondsLeft", 0);
        timeLeftView.setText(getString(R.string.timer_text, seconds));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        Log.d("lifecycle", "onCreate " + this);

        showStatus();
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

    public void okClicked(View view) {
        Intent intent = new Intent();
        intent.putExtra("message", "Result delivered from StatusActivity");
        setResult(8, intent);
        finish();
    }
}
