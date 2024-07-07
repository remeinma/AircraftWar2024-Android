package com.example.aircraftwar2024.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aircraftwar2024.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {



    private boolean musicPlay = false;
    public static boolean isOnline =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityManager.getActivityManager().addActivity(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button playButton = (Button) findViewById(R.id.playButton);
        Button onlineButton = (Button) findViewById(R.id.onlineButton);
        Button music_on = (Button) findViewById(R.id.music_on);
        Button music_off = (Button) findViewById(R.id.music_off);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOnline = false;
                ActivityManager.getActivityManager().finishActivity(MainActivity.this);
                Intent intent = new Intent(MainActivity.this,OfflineActivity.class);
                intent.putExtra("musicPlay",musicPlay);
                startActivity(intent);
            }
        });

        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOnline = true;
                ActivityManager.getActivityManager().finishActivity(MainActivity.this);
                Intent intent = new Intent(MainActivity.this,OnlineActivity.class);
                intent.putExtra("musicPlay",musicPlay);
                startActivity(intent);
            }
        });

        music_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlay = true;
            }
        });

        music_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlay = false;
            }
        });
    }



}