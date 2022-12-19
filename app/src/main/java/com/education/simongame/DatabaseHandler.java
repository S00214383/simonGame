package com.education.simongame;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SimonFinalCA";
    private static final String TABLE_Scores = "scores";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_Score = "score";
    private static final String KEY_Level = "level";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_Scores + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_Level + " TEXT,"
                + KEY_Score + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Scores);
        onCreate(db);
    }

    public void emptyScores() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Scores);
        onCreate(db);
    }


    public ArrayList<HiScore> GetTopFive(){
        HiScore[] scores = new HiScore[5];
        List<HiScore> scoreList = getAllScores();
        if(scoreList.size() < 5){
            for (int i = 0; i < scoreList.size(); i++){
                scores[i] = scoreList.get(i);
            }
            for (int i = scoreList.size(); i< scores.length;i++){
                scores[i] = new HiScore("0","Player","0","0");
            }
        }

        else {
            for (int i = 0; i < scores.length; i++){
                scores[i] = scoreList.get(i);
            }
        }
        ArrayList<HiScore> scoreListSorted = new ArrayList<>();
        for (HiScore s : scores)
            scoreListSorted.add(s);
        return scoreListSorted;
    }



    void addScore(HiScore score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, score.getName());
        values.put(KEY_Level, score.getLevel());
        values.put(KEY_Score, score.getScore());
        db.insert(TABLE_Scores, null, values);
        db.close();
    }


    public List<HiScore> getAllScores() {
        List<HiScore> contactList = new ArrayList<HiScore>();
        String selectQuery = "SELECT  * FROM " + TABLE_Scores + " Group by "+ KEY_Score + " ORDER BY " +KEY_Score;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
               //Log.i("Database index 0",cursor.getString(0)+""); // for debugging
                HiScore score = new HiScore(
                        cursor.getInt(0)+"",// id
                        cursor.getString(1),// name
                        cursor.getString(2),// level
                        cursor.getString(3)// score
                );
                contactList.add(score);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return contactList;
    }
}
