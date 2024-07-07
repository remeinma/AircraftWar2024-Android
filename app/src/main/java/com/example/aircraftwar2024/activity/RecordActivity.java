package com.example.aircraftwar2024.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.game.EasyGame;
import com.example.aircraftwar2024.game.HardGame;
import com.example.aircraftwar2024.game.MediumGame;
import com.example.aircraftwar2024.score.ScoreRanking;
import com.example.aircraftwar2024.score.ScoreRankingDaoImpl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordActivity extends AppCompatActivity {

    private int gameType=0;
    private int score = 0;
    SimpleAdapter listItemAdapter = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityManager.getActivityManager().addActivity(this);
        super.onCreate(savedInstanceState);

        gameType = getIntent().getIntExtra("gameType",1);
        score = getIntent().getIntExtra("Score",0);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record);

        TextView textView = findViewById(R.id.title);
        switch (gameType){
            case 0:
                textView.setText("简单模式");
                break;
            case 1:
                textView.setText("普通模式");
                break;
            case 2:
                textView.setText("困难模式");
                break;
        }

        ListView list = (ListView) findViewById(R.id.ListView01);
        try {
            Print(score,"test");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listItemAdapter = new SimpleAdapter(
                this,
                getData(),
                R.layout.listitem,
                new String[]{"rank","user","score","time"},
                new int[]{R.id.rank,R.id.user,R.id.score,R.id.time});

        //添加并且显示
        list.setAdapter(listItemAdapter);

        //添加单击监听
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Map<String, Object> clkmap = (Map<String, Object>) arg0.getItemAtPosition(arg2);
                AlertDialog.Builder dialog = new AlertDialog.Builder(RecordActivity.this);
                dialog.setTitle("确认删除？");
                dialog.setMessage("是否确认删除第"+clkmap.get("rank")+"条记录？");
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scoreRankingDao.readFile();
                        scoreRankingDao.doDelete((String) clkmap.get("time"));
                        scoreRankingDao.clearFile();
                        List<ScoreRanking> scoreRankingList = scoreRankingDao.getAllScores();
                        scoreRankingDao.writeFile(scoreRankingList);
                        refresh();
                        listItemAdapter = null;
                        listItemAdapter = new SimpleAdapter(
                                RecordActivity.this,
                                getData(),
                                R.layout.listitem,
                                new String[]{"rank","user","score","time"},
                                new int[]{R.id.rank,R.id.user,R.id.score,R.id.time});

                        //添加并且显示
                        list.setAdapter(listItemAdapter);
                    }
                });
                dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog1 = dialog.create();
                dialog.show();
            }
        });

        Button returnButton = (Button)findViewById(R.id.returnButton);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getActivityManager().finishAllActivity();
                Intent intent = new Intent(RecordActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private ScoreRankingDaoImpl scoreRankingDao = new ScoreRankingDaoImpl();
    private List<ScoreRanking> scoreRankingList;
    private List<Map<String, Object>> getData(){
        ArrayList<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
        scoreRankingDao.readFile();
        scoreRankingList = scoreRankingDao.getAllScores();
        int num = scoreRankingList.size();

        for(int i = 0; i < num; i ++) {
            ScoreRanking scoreRanking = scoreRankingList.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rank", (i+1)+"");
            map.put("user", scoreRanking.getName());
            map.put("score", scoreRanking.getScore()+"");
            map.put("time", scoreRanking.getTime());
            listitem.add(map);
        }
        return listitem;
    }

    public List<Map<String, Object>> refresh() {
        ArrayList<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>();
        scoreRankingDao.readFile();
        scoreRankingList = scoreRankingDao.getAllScores();
        int num = scoreRankingList.size();

        for(int i = 0; i < num; i ++) {
            ScoreRanking scoreRanking = scoreRankingList.get(i);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rank", i+1);
            map.put("user", scoreRanking.getName());
            map.put("score", scoreRanking.getScore());
            map.put("time", scoreRanking.getTime());
            listitem.add(map);
        }
        return listitem;
    }
    public void Print(int score,String name) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        String time = format.format(new Date());
        scoreRankingDao.readFile();
        scoreRankingDao.clearFile();
        scoreRankingDao.doAdd(new ScoreRanking(name, score, time));
        scoreRankingList = scoreRankingDao.getAllScores();
        scoreRankingDao.writeFile(scoreRankingList);
    }
}