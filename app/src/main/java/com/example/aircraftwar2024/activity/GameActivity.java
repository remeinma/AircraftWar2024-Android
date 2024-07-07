package com.example.aircraftwar2024.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.game.EasyGame;
import com.example.aircraftwar2024.game.HardGame;
import com.example.aircraftwar2024.game.MediumGame;


public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";


    private int gameType=0;
    private boolean musicPlay;

    public static String degree;
    public static int screenWidth,screenHeight;

    public static Handler mHandler;
    BaseGame baseGameView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityManager.getActivityManager().addActivity(this);
        super.onCreate(savedInstanceState);

        getScreenHW();

        if(getIntent() != null){
            gameType = getIntent().getIntExtra("gameType",1);
        }
        musicPlay = getIntent().getBooleanExtra("musicPlay",false);

        /*TODO:根据用户选择的难度加载相应的游戏界面*/

        switch (gameType){
            case 0:
                baseGameView = new EasyGame(this);
                degree = "Easy";
                break;
            case 1:
                baseGameView = new MediumGame(this);
                degree = "Medium";
                break;
            case 2:
                baseGameView = new HardGame(this);
                degree = "Hard";
                break;
        }
        if(musicPlay){
            baseGameView.needMusic = true;
        }
        setContentView(baseGameView);
        mHandler = new Mhandler();
    }

    public void getScreenHW(){
        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getDisplay().getRealMetrics(dm);

        //窗口的宽度
        screenWidth= dm.widthPixels;
        //窗口高度
        screenHeight = dm.heightPixels;

        Log.i(TAG, "screenWidth : " + screenWidth + " screenHeight : " + screenHeight);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    class Mhandler extends Handler {

        // 通过复写handlerMessage() 从而确定更新UI的操作
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1://gameOver
                    ActivityManager.getActivityManager().finishActivity(GameActivity.this);
                    Intent intent = new Intent(GameActivity.this,RecordActivity.class);
                    intent.putExtra("gameType",gameType);
                    intent.putExtra("Score",baseGameView.getScore());
                    startActivity(intent);
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
    }
}