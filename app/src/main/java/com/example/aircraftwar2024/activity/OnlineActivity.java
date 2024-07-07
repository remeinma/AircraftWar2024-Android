package com.example.aircraftwar2024.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.game.BaseGame;
import com.example.aircraftwar2024.game.MediumGame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class OnlineActivity extends AppCompatActivity {
    private int gameType = 2;
    private boolean musicPlay;
    private Socket socket;
    private PrintWriter writer;
    Handler handler;
    private static  final String TAG = "OnlineActivity";
    private String HOST = "10.0.2.2";//服务器程序和客户端程序运行在一台主机
    //    private String HOST="10.250.220.249";//主机IP
    private int PORT = 9999;
    BaseGame game = null;
    private static int opponentScore = 0;
    private static boolean gameOverFlag = false;
    private static String opName = "Rival";
    private static String myName = "Player";
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        musicPlay = getIntent().getBooleanExtra("musicPlay",false);
        ActivityManager.getActivityManager().addActivity(this);
//        game.needMusic = musicPlay;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_online);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AlertDialog.Builder dialog = new AlertDialog.Builder(OnlineActivity.this);
        dialog.setMessage("匹配中，请等待……");
        alertDialog = dialog.create();
        alertDialog.show();

        handler = new Handler(getMainLooper()){
            @Override
            public void handleMessage(Message msg){
                //启动游戏
                if(msg.what == 0x123 && msg.obj.equals("start")) {
                    try {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss(); // 消除对话框
                        }
                        game = new MediumGame(OnlineActivity.this);
                        game.needMusic = musicPlay;
                        setGameOverFlag(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setContentView(game);
                    // 如果开启游戏，那么就新开一个线程给服务端发送当前分数
                    // 如果当前玩家已经死亡，那么就给服务器传"end"信息
                    new Thread(() -> {

                        //发送当前分数
                        while (!game.isGameOverFlag()) {

                            writer.println(game.getScore());
                            Log.i(TAG,"send to server: score "+game.getScore());
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        //死亡后发送结束标志
                        writer.println("end");
                        Log.i(TAG,"send to server: end");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                setContentView(R.layout.activity_end);
                            }
                        });

                    }).start();
                }else if (msg.what == 0x123 && msg.obj.equals("gameover")) {
                    //设置标志：双方游戏全部结束
                    setGameOverFlag(true);
                    Log.i(TAG,"游戏结束");

                    Intent intent = new Intent(OnlineActivity.this, OverActivity.class);
                    intent.putExtra("myScore",game.getScore());
                    intent.putExtra("otherScore",opponentScore);

                    startActivity(intent);
                    Log.i(TAG,"跳转");

                } else if (msg.what == 0x456) {

                } else if (msg.what == 0x789){

                } else {

                    try {
                        if ((String)msg.obj != null){
                            opponentScore = Integer.parseInt((String)msg.obj);
                        }
                    } catch (NumberFormatException e) {

                    }

                }
            }
        };

        new Thread(new NetConn(handler)).start();

    }

    protected class NetConn extends Thread{
        private BufferedReader in;
        private Handler toClientHandler;

        public NetConn(Handler myHandler){
            this.toClientHandler = myHandler;
        }
        @Override
        public void run(){
            try{
                //创建socket对象
                socket = new Socket();
                //connect,要保证服务器已启动
                socket.connect(new InetSocketAddress
                        (HOST,PORT),5000);

                //获取socket输入输出流
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
                writer = new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream(),"utf-8")),true);
                Log.i(TAG,"connect to server");

                //接收服务器返回的数据
                Thread receiveServerMsg =  new Thread(){
                    @Override
                    public void run(){
                        String msg = null;
                        int cnt = 0;
                        try{
                            while((msg = in.readLine())!=null)
                            {
                                if(cnt == 1) {
                                    Message msg2 = new Message();
                                    msg2.what = 0x789;
                                    msg2.obj = msg;
                                    handler.sendMessage(msg2);
                                    cnt++;
                                }
                                if(cnt == 0) {
                                    Message msg1 = new Message();
                                    msg1.what = 0x456;
                                    msg1.obj = msg;
                                    handler.sendMessage(msg1);
                                    cnt++;
                                }
                                //发送消息给UI线程
                                Message msgFromServer = new Message();
                                msgFromServer.what = 0x123;
                                msgFromServer.obj = msg;
                                toClientHandler.sendMessage(msgFromServer);
                            }
                        }catch (IOException ex){
                            ex.printStackTrace();
                        }
                    }
                };
                receiveServerMsg.start();
            }catch(UnknownHostException ex){
                ex.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void setGameOverFlag(boolean gameOverFlag) {
        OnlineActivity.gameOverFlag = gameOverFlag;
    }
    public static String getOpponentName() {
        return opName;
    }
    public static int getOpponentScore() {
        return opponentScore;
    }
    public static boolean isGameOverFlag() {
        return gameOverFlag;
    }

}