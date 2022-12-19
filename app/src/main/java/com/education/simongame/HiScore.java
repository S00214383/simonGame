package com.education.simongame;

//DONE
public class HiScore implements Comparable  {
    /*
     * CREATE TABLE hiscores (
     *      score_id INTEGER PRIMARY KEY,
     *      game_date TEXT NOT NULL,
     *      player_name TEXT NOT NULL,
     *      score INTEGER NOT NULL);
     */

    String ID;    // primary score
    String Name;  // JOE
    String Score;  // score - should be even
    String Level; //level

    /*
     * Constructors
     */

    public int getScoreInt(){
        return Integer.parseInt(getScore());
    }
    public String getLevel() {
        return Level;
    }
    public String getScore() {
        return Score;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    /*
     * Getter and setter methods
     */
    public HiScore(String id,String name, String level, String score){
        ID = id;
        Name = name;
        Score = score;
        Level = level;
    }

    public HiScore(String name,String score,String level)
    {
        this(null,name,level,score);
    }
    @Override
    public int compareTo(Object o) {
        int otherScore = Integer.parseInt(((HiScore)o).getScore());
        int score =Integer.parseInt(getScore());
        return otherScore -score;
    }
}

