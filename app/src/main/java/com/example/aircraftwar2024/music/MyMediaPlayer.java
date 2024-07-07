package com.example.aircraftwar2024.music;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.aircraftwar2024.R;

public class MyMediaPlayer {

    private MediaPlayer bgMP;
    private MediaPlayer bossMP;



    public MyMediaPlayer(Context context) {
        if(bgMP == null) {
            //根据音乐资源文件创建对象，循环播放
            bgMP = MediaPlayer.create(context,R.raw.bgm);
            bgMP.setLooping(true);
        }

        if(bossMP == null) {
            //根据音乐资源文件创建对象，循环播放
            bossMP = MediaPlayer.create(context,R.raw.bgm_boss);
            bossMP.setLooping(true);
        }
    }
    public void playBgm() {
        bgMP.start();
    }


    public void playBossBgm() {
        bossMP.start();
    }

    public void stopBgm(){
        if(bgMP != null&& bgMP.isPlaying()){
            bgMP.stop();
            bgMP.release();
        }
    }

    public void stopBossBgm(){
        if(bossMP != null && bossMP.isPlaying()) {
            bossMP.stop();
            bossMP.release();
        }
    }

    public void pauseBossBgm() {
        bossMP.pause();
    }

    public void pauseBgm() {
        bgMP.pause();
    }

}
