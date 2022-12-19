package com.education.simongame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ScoreListViewAdapter extends ArrayAdapter<HiScore> {
        public ScoreListViewAdapter(@NonNull Context context, List<HiScore> itemsList) {
            super(context, 0,itemsList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return InitView(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return InitView(position, convertView, parent);
        }


        private View InitView(int pos,View convertView,ViewGroup parent){

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.score_item,parent,false
                );
            }

            TextView TV_Score = convertView.findViewById(R.id.tvScore);
            TextView TV_Level = convertView.findViewById(R.id.TV_LevelItem);
            TextView TV_Name = convertView.findViewById(R.id.TV_nameItem);

            HiScore item = getItem(pos);


            TV_Score.setText("Score: "+item.getScore());
            TV_Level.setText("Level: "+item.getLevel());
            TV_Name.setText(item.ID+"."+item.getName());
            return convertView;
        }
    }
