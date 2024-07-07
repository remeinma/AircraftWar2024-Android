package com.example.aircraftwar2024.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aircraftwar2024.R;

public class OfflineActivity extends AppCompatActivity {

    private int gameType;
    private boolean musicPlay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        musicPlay = getIntent().getBooleanExtra("musicPlay",false);
        ActivityManager.getActivityManager().addActivity(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_offline);

        Button easyButton = (Button) findViewById(R.id.easy);
        Button normalButton = (Button) findViewById(R.id.normal);
        Button hardButton = (Button) findViewById(R.id.hard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameType = 0;
                ActivityManager.getActivityManager().finishActivity(OfflineActivity.this);
                Intent intent = new Intent(OfflineActivity.this,GameActivity.class);
                intent.putExtra("gameType",gameType);
                intent.putExtra("musicPlay",musicPlay);
                startActivity(intent);
            }
        });

        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameType = 1;
                ActivityManager.getActivityManager().finishActivity(OfflineActivity.this);
                Intent intent = new Intent(OfflineActivity.this,GameActivity.class);
                intent.putExtra("gameType",gameType);
                intent.putExtra("musicPlay",musicPlay);
                startActivity(intent);
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameType = 2;
                ActivityManager.getActivityManager().finishActivity(OfflineActivity.this);
                Intent intent = new Intent(OfflineActivity.this,GameActivity.class);
                intent.putExtra("gameType",gameType);
                intent.putExtra("musicPlay",musicPlay);
                startActivity(intent);
            }
        });


    }
}