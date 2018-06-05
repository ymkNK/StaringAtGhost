package com.example.a233.staringatghost;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.a233.staringatghost.gameView;

public class game extends Activity {
    private gameView gameView;
    private ServerController serverController;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameView = (gameView)findViewById(R.id.gameView);
        serverController=new ServerController();
        //0:combatAircraft
        gameView.start();
        Button Btn1 = (Button) findViewById(R.id.clock);//获取按钮资源
        Btn1.setOnClickListener(new Button.OnClickListener() {//创建监听
            public void onClick(View v) {
                gameView.clockwise();
            }
        });
        Button Btn2 = (Button) findViewById(R.id.anticlock);//获取按钮资源
        Btn2.setOnClickListener(new Button.OnClickListener() {//创建监听
            public void onClick(View v) {
                gameView.anticlock();
            }
        });

    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void backHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void upload(long score){
        serverController.createData(score);
    }//上传分数

}
