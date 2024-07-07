package com.example.aircraftwar2024.music;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

import com.example.aircraftwar2024.R;

import java.util.HashMap;

public class MySoundPool {

    private SoundPool mysp;
    private AudioAttributes audioAttributes = null;
    private HashMap<Integer,Integer> soundPoolMap;

    public MySoundPool(Context context) {
        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        mysp = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();

        soundPoolMap = new HashMap<>();
        soundPoolMap.put(1,mysp.load(context, R.raw.bomb_explosion,1));
        soundPoolMap.put(2,mysp.load(context, R.raw.bullet_hit,1));
        soundPoolMap.put(3,mysp.load(context, R.raw.get_supply,1));
        soundPoolMap.put(4,mysp.load(context, R.raw.game_over,1));

    }

    public void explosionSP(){
        mysp.play(soundPoolMap.get(1),1,1,1,0,1.2f);
    }
    public void bulletHitSP(){
        mysp.play(soundPoolMap.get(2),1,1,1,0,1.2f);
    }
    public void supplySP(){
        mysp.play(soundPoolMap.get(3),1,1,1,0,1.2f);
    }
    public void gameOverSP(){
        mysp.play(soundPoolMap.get(4),1,1,1,0,1.2f);
    }
}
