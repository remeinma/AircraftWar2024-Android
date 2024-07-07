package com.example.aircraftwar2024.score;


import android.content.Context;

import com.example.aircraftwar2024.activity.GameActivity;

import java.io.*;
import java.util.*;

public class ScoreRankingDaoImpl implements ScoreRankingDao{
    private List<ScoreRanking> scoreRankings;

    String path = "/data/data/com.example.aircraftwar2024/files/ScoreAndRankings"+ GameActivity.degree.toString()+".txt";
    File file = new File(path);
    public ScoreRankingDaoImpl(){
        scoreRankings = new ArrayList<ScoreRanking>();
    }


    @Override
    public List<ScoreRanking> getAllScores() {
        Collections.sort(scoreRankings, new Comparator<ScoreRanking>() {
            @Override
            public int compare(ScoreRanking o1, ScoreRanking o2) {
                return o2.getScore()-o1.getScore();
            }
        });
        return this.scoreRankings;
    }

    @Override
    public void doAdd(ScoreRanking scoreRanking) {
        scoreRankings.add(scoreRanking);
    }

    @Override
    public void doDelete(String time) {
        for (ScoreRanking player : scoreRankings) {
            if(Objects.equals(player.getTime(), time)) {
                scoreRankings.remove(player);
                return;
            }
        }
    }

    public void readFile() {
        scoreRankings=new ArrayList<ScoreRanking>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String strin;
            while ((strin = br.readLine())!= null) {
                String[] strinSave = strin.split("\\|");
                ScoreRanking scoreRanking = new ScoreRanking(strinSave[0], Integer.parseInt(strinSave[1]),strinSave[2]);
                scoreRankings.add(scoreRanking);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearFile() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(List<ScoreRanking> scoreRankings) {
        try {
            FileWriter fop = new FileWriter(file,true);
            for (ScoreRanking scoreRanking : scoreRankings) {
                String strout = scoreRanking.getName()+"|"+Integer.toString(scoreRanking.getScore())+"|"+
                        scoreRanking.getTime();
                fop.write(strout+"\r\n");
            }
            fop.flush();
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
