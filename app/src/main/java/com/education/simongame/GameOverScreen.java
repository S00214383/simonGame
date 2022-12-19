package com.education.simongame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GameOverScreen extends AppCompatActivity {

    //VARIABLES

    EditText etName;
    HighScoreScreen score;
    TextView tvGameOver,tvLevel, tvScore;


    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);

        tvScore = findViewById(R.id.tvScore);
        tvLevel = findViewById(R.id.tvLvl);
        tvGameOver = findViewById(R.id.tvGameOver);
        etName = findViewById(R.id.etName);

        // create score object from the current score and level
        score = new HighScoreScreen("Moh",(MainActivity.CurrentScore)+"",""+(MainActivity.CurrentLevel-1));
        // show score on screen
        tvScore.setText("Your Score is: "+score.getScore());
        tvLevel.setText("Your got to level: "+score.getLevel());


        // check if the current score is a new high score
        ArrayList<HighScoreScreen> scores = db.GetTopFive();
        for(HighScoreScreen highScore : scores){
            if(score.getScoreInt() > highScore.getScoreInt()){
                tvGameOver.setText("New High Score Enter Your Name");
                ((Button)findViewById(R.id.btnSave)).setEnabled(true);
                etName.setEnabled(true);
                break;
            }
        }
    }


    public void StartNewGame(View view){
        StartActivityFrom(MainActivity.GameState.Restart);
    }
    public void ShowScoreBoard(View view){
        StartActivityFrom(MainActivity.GameState.End);
    }

    // will reset the game and start an intent (sequence screen or high score screen)
    private void StartActivityFrom(MainActivity.GameState gameState) {
        MainActivity.CurrentScore = 0;
        MainActivity.CurrentLevel = 0;
        MainActivity.gameState = gameState;
        //startActivity(intent);
        this.finish();
    }

    public void SaveHighScore(View view) {
        score.setName(etName.getText().toString());
        db.addScore(score);
        Toast.makeText(this,"Saved",Toast.LENGTH_LONG).show();
        ((Button) view).setEnabled(false);
        etName.setEnabled(false);
        etName.setBackgroundColor(Color.GRAY);
    }
}