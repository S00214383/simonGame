package com.education.simongame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    final int LEVEL_START_DIFFICULTY = 4;
    final int LEVEL_START_DIFFICULTY_INCREASE = 2;
     enum GameState{
         Setup,
         Start,
         ShowingGuess,
         ShowingMessage,
         NextActivity,
         End,
         Restart,
         Wait,
    }
    // using buttons as visual feedback to the player
    Button btnCandle,btnPoison,btnBook,btnWitch,btn_play;
    TextView tv_UserDisplay, tv_score, tv_level;
    int[] currentSequence;
    // using counters to animate the buttons and create game loop
    CountUpTimer highlightTimer;
    // this will allow me to track the game state using the enum "GameState"
    CountUpTimer GameLoop;
    int LevelDifficulty = LEVEL_START_DIFFICULTY;
    // used in animating the button along with the timer
    boolean timeUp = false;
    boolean highlightStarted = false;
    float animationTime = 11f;

    // this allows me to check if the current game is new game
    boolean DataLoaded = false;

    // will hold data to save when the game is over
    public static int CurrentLevel;
    public static  int CurrentScore;
    // used to get the computer guess
    Random rand = new Random();
    // will hold the current game state
    public static GameState gameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get view elements
        btnBook = findViewById(R.id.btnBlue);
        btnWitch = findViewById(R.id.btnRed);
        btnCandle = findViewById(R.id.btnGreen);
        btnPoison = findViewById(R.id.btnYellow);
        btn_play = findViewById(R.id.btnGo);

        tv_UserDisplay = findViewById(R.id.TV_UserDisplay);
        tv_score = findViewById(R.id.tvScore);
        tv_level = findViewById(R.id.tvCurLvl);

        // setup game variables
        // using the counter to animate the buttons
        highlightTimer = new CountUpTimer(1000000000) {
            public void onTick(double second) {
                //Log.i("Timer",String.valueOf(this.CurrentSeconds));
                timeUp = this.CurrentSeconds >=animationTime;
            }
        };

        // using an enum to track game state
        MainActivity.gameState = GameState.Setup;
        // using the counter as a game loop
        // this will hold all game logic
        GameLoop = new CountUpTimer(10000000) {
            public void onTick(double second) {
                tv_score.setText("Score: "+(CurrentScore));
                tv_level.setText("Level: "+(CurrentLevel+1));
                //Log.i("Main activity:","Level Difficulty "+ LevelDifficulty);
                switch (MainActivity.gameState){
                    // will get a new random sequence and move the state to start state
                    case Setup:
                        Setup();
                        break;
                    case Start:
                        // this state will wait for the player to click on play button
                        SetupGameToStart();
                        break;
                    case ShowingGuess:
                        // this will start animating the buttons depending on the sequence
                        //tv_level.setText("Index: "+(currentButtonToLightIndex));
                        LightButtons();
                        break;
                    case ShowingMessage:
                        // when the sequence  is finished wait 10ms and switch to the play screen
                        if (highlightTimer.CurrentSeconds >=10)
                            MainActivity.gameState = GameState.NextActivity;
                        break;
                    case NextActivity:
                        //start play screen and setup this screen for the next round if the player wins
                        tv_UserDisplay.setText("");
                        LevelDifficulty +=LEVEL_START_DIFFICULTY_INCREASE;
                        CurrentLevel++;
                        DataLoaded = false;
                        //recreate();
                        btn_play.setEnabled(true);
                        StartPlayScreen();
                        gameState = GameState.Wait;
                        break;
                    case End:
                            EndThisGame();
                            break;
                    case Restart:
                        //Log.i("Main activity:","Restarted -----------------------------------------------------");
                        RestartGame();
                        break;
                    case Wait:
                        // do nothing
                        break;
                }
            }
        };
        // start the game
        GameLoop.start();
    }

    private void RestartGame() {
        LevelDifficulty = LEVEL_START_DIFFICULTY;
        currentButtonToLightIndex = 0;
        DataLoaded = false;
        MainActivity.gameState = GameState.Setup;
    }

    private void EndThisGame() {
        GameLoop.cancel();
        finish();
        startActivity(new Intent(this,ScoreBoard.class));
    }


    private void StartPlayScreen(){
        Intent playScreenIntent = new Intent(this,PlayScreen.class);
        //playScreenIntent.getIntExtra("Size",LevelDifficulty-2);
        playScreenIntent.putExtra("sequence", currentSequence);
        startActivity(playScreenIntent);
        highlightTimer.cancel();
        DataLoaded = false;
    }


    public void StartLevel(View view) {
        tv_UserDisplay.setText("");
        MainActivity.gameState = GameState.Start;
        highlightTimer.start();
        btn_play.setEnabled(false);
    }

    private void Setup() {
        if(DataLoaded) return;
        tv_score.setText("Score: "+(CurrentScore));
        tv_level.setText("Level: "+(CurrentLevel+1));
        tv_UserDisplay.setText( ("Get Ready!! Press Play") );
        timeUp = false;
        currentButtonToLightIndex = 0;
        // reset guess array to get a new one
        currentSequence = null;
        FillGuessArray();
        DataLoaded = true;
    }

    private void SetupGameToStart() {

        if(highlightTimer.CurrentSeconds > 0) {
            tv_UserDisplay.setText("");
            highlightTimer.cancel();
            highlightTimer.CurrentSeconds =0;
            MainActivity.gameState = GameState.ShowingGuess;
            highlightStarted = false;
            //LightButtons();
        }
    }

    /*
     * will fill the array with random selection
     * this will make sure the number dose not show up twice in a row
     */
    private void FillGuessArray(){
        // set the array size to match the level difficulty starts at '1'
        currentSequence = new int[LevelDifficulty];

        int lastGuess = -1;
        for (int i = 0; i< currentSequence.length; i++){
            // a save exit from the while loop to avoid infinite loop
            int saveExit = 0;
            while(saveExit < 30 && true) {
                int guess = rand.nextInt(4);
                if (guess != lastGuess) {
                    lastGuess = guess;
                    break;
                }
                saveExit++;
            }
            currentSequence[i] = lastGuess;
        }
    }

    private int currentButtonToLightIndex = 0;

    // will animate the buttons depending on the guess array
    // the button animation happens in "StartHighlight(Button btn)" method
    private void LightButtons(){
        if(currentSequence != null){
            //Toast.makeText(this,"Index: "+currentButtonToLightIndex,Toast.LENGTH_SHORT).show();
            switch (currentSequence[currentButtonToLightIndex]){
                case 0: // cat
                    StartHighlight(btnBook);
                    break;
                case  1: // dog
                    StartHighlight(btnWitch);
                    break;
                case 2: // mouse
                    StartHighlight(btnPoison);
                    break;
                case  3: // monkey
                    StartHighlight(btnCandle);
                    break;
            }
            // when every guess has been shown, this will change the game state to the next state
            if(currentButtonToLightIndex >= currentSequence.length){
                //tv_level.setText("Index: "+(currentButtonToLightIndex));
                String Text = rand.nextBoolean() ? "Match The Pattern" : "Did You Get That ";
                tv_UserDisplay.setText(Text);
                highlightTimer.start();
                MainActivity.gameState = GameState.ShowingMessage;
            }
        }
    }

    private void StartHighlight(Button btn) {

        // if the timer is up will set the button to be disabled and move on to the next button
        if(timeUp )
        {
            //Toast.makeText(this,"Time is up: "+currentButtonToLightIndex,Toast.LENGTH_SHORT).show();
            highlightTimer.cancel();
            btn.setEnabled(false);
            currentButtonToLightIndex++;
            timeUp= false;
            highlightStarted = false;
            // if the button is not highlighted and the timer is not up
            // this means this is the first frame and i will set the button to be enabled
        }else if(!highlightStarted) {
            highlightTimer.start();
            highlightTimer.CurrentSeconds = 0;
            animationTime =11f;// in ms
            highlightStarted = true;
            btn.setEnabled(true);
        }
    }

}