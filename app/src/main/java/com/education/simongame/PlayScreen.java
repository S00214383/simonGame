package com.education.simongame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class PlayScreen extends AppCompatActivity implements SensorEventListener {

    // this will allow me to track the user input
    enum UserInput{
        north(0),
        south(1),
        west(2),
        east(3),
        none(4),
        ;

        private  int Value;
        UserInput(int value){
            Value = value;
        }

        public int getValue() {
            return Value;
        }
    }

    // used for user input
    Sensor Accelerometer;
    SensorManager sensorManager;
    // will hold current user input
    UserInput userInput;
    // button used to indicate user input
    Button btnE, btnW, btnN, btnS;
    // used to check if the player has changed his/her input
    boolean readUserInput;
    // used to animate buttons
    CountUpTimer GameLoop;
    // holds the correct sequence used to check against user input
    int[] correctAnswer;
    // current sequence index to check
    int checkIndex = 0;
    // true when the player input is incorrect
    boolean gameOver = false;
    // used to indicate to the player when they win or lose
    TextView tv_UserDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);

        // get view elements
        btnN = findViewById(R.id.btnBlue);
        btnS = findViewById(R.id.btnRed);
        btnE = findViewById(R.id.btnGreen);
        btnW = findViewById(R.id.btnYellow);

        tv_UserDisplay = findViewById(R.id.TV_UserDisplay);

        // setup the sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // setup the user input
        userInput = UserInput.none;
        readUserInput = true;
        // get the correct sequence from the main activity
        correctAnswer = getIntent().getIntArrayExtra("sequence");
        // register Listener to the sensor with short delay this will help get more accurate input
        sensorManager.registerListener(this,Accelerometer,SensorManager.SENSOR_DELAY_GAME);


        // used as a loop to animate button when the used tilt the phone
        GameLoop = new CountUpTimer(10000000) {
            public void onTick(double second) {
                // set all buttons to disabled
                btnN.setEnabled(false);
                btnS.setEnabled(false);
                btnE.setEnabled(false);
                btnW.setEnabled(false);
                // only enable the button matching user input
                switch (userInput){
                    case north:
                        btnN.setEnabled(true);
                        break;
                    case south:
                        btnS.setEnabled(true);
                        break;
                    case west:
                        btnW.setEnabled(true);
                        break;
                    case east:
                        btnE.setEnabled(true);
                        break;
                }

                // if the game is over show a message for a short time and switch to the game over screen
                if(gameOver){
                    // show game over screen
                    tv_UserDisplay.setText("Game Over");
                    StartGameOverScreen();
                }
                // if the game is not over and we have checked all the sequence to be true
                // then the player have won and will add 4 to the score
                // and bring him/her back to the sequence screen
                if(checkIndex >= correctAnswer.length){
                    MainActivity.CurrentScore +=4;
                    tv_UserDisplay.setText("You win");
                    MainActivity.gameState = MainActivity.GameState.Setup;
                    CleanActivity();
                }
            }
        };
        // start the game loop
        GameLoop.start();
    }

    void StartGameOverScreen(){
        CleanActivity();
        startActivity(new Intent(this,GameOverScreen.class));
    }
    void CleanActivity(){
        sensorManager.unregisterListener(this,Accelerometer);
        GameLoop.cancel();
        finish();
    }



    final int lOW_LIMIT = 2;
    final double UP_LIMIT = 3.5;

    @Override
    public void onSensorChanged(SensorEvent event) {
        // all i need to check for input is x and y values
        float x = event.values[0];
        float y = event.values[1];

        // if i am expecting a read from the player
        // IF Y < LOW limit AMD X > UP Limit this means it's up or down
        // IF X < LOW limit AMD Y > UP limit this means it's left or right
        // -x is up
        // - y is down

        if(readUserInput) {
            // get user input
            if (x < -UP_LIMIT && (y > -lOW_LIMIT && y < lOW_LIMIT)) {
                userInput = UserInput.north;
                readUserInput = false;
            }else if(x > UP_LIMIT && (y > -lOW_LIMIT && y < lOW_LIMIT)){
                userInput = UserInput.south;
                readUserInput = false;
            }else if (y < -UP_LIMIT && (x > -lOW_LIMIT && x < lOW_LIMIT)) {
                userInput = UserInput.west;
                readUserInput = false;
            }else if(y > UP_LIMIT && (x > -lOW_LIMIT && x < lOW_LIMIT)){
                userInput = UserInput.east;
                readUserInput = false;
            }
        }
        // if all x,y > low limit this means phone is flat and the used has finished his input
        else if( (x > -lOW_LIMIT && x < lOW_LIMIT) && (y > -lOW_LIMIT && y < lOW_LIMIT) )
        {
            // check if the Input is incorrect make gameOver true
            if(userInput.getValue() != correctAnswer[checkIndex] && !gameOver) {
                gameOver = true;
            }
            // if input match the current sequence move to check the next index
            else if(userInput.getValue() == correctAnswer[checkIndex]){
                checkIndex++;
                userInput = UserInput.none;
                readUserInput = true;
            }
            // reset if true
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}