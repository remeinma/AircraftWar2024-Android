package com.example.aircraftwar2024.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aircraftwar2024.R;

public class OverActivity extends AppCompatActivity {
    TextView text_1,text_2,text_3;
    TextView winnerName;
    Button returnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_over);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        text_1=findViewById(R.id.text1);
        text_2=findViewById(R.id.text2);
        text_3=findViewById(R.id.text3);
        winnerName=findViewById(R.id.winner_name);
        returnButton = (Button)findViewById(R.id.returnButton);


        int myScore = getIntent().getIntExtra("myScore",0);
        int otherScore = getIntent().getIntExtra("otherScore",0);
        text_1.setText("你的分数是： \r"+myScore);
        text_2.setText("对手分数是： \r"+otherScore);

        if(myScore>otherScore){
            winnerName.setText("You");
            text_3.setText("恭喜你获胜");
        }else if((myScore<otherScore)){
            winnerName.setText("Rival");
            text_3.setText("很遗憾输了");
        }else{
            winnerName.setText("Nobody");
            text_3.setText("平局");
        }

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getActivityManager().finishAllActivity();
                Intent intent = new Intent(OverActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}