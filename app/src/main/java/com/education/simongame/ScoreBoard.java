package com.education.simongame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class ScoreBoard extends AppCompatActivity {

    ListView lv_scores;
    // using the database to get all the stored scores
    DatabaseHandler db = new DatabaseHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);

        lv_scores = findViewById(R.id.LV_TopScores);
        // will fill the list view with top 5 distinct scores
        FillTopFiveScores();
    }

    // will reset the game and bring the player back to the sequence screen
    public void StartNewGame(View view){
        MainActivity.CurrentScore = 0;
        MainActivity.CurrentLevel = 0;
        startActivity(new Intent(this,MainActivity.class));
        this.finish();
    }


    // this will fill the list view with top 5 high scores from the database
    // the list returned from the database dose not have any duplicates
    private void FillTopFiveScores(){
        lv_scores.setAdapter(new ScoreListViewAdapter(this,db.GetTopFive()));
    }

    // will delete all scores from the database and update the list view
    public void ClearScores(View view) {
        db.emptyScores();
        lv_scores.setAdapter(new ScoreListViewAdapter(this,db.GetTopFive()));
    }
}