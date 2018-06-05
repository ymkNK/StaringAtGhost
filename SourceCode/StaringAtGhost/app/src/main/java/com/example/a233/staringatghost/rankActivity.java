package com.example.a233.staringatghost;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class rankActivity extends AppCompatActivity {
    TableLayout rank_area;
    ServerController serverController=new ServerController();
    Button btn_getRank;
    JsonTool jsonTool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        Button btn_back = findViewById(R.id.btn_back);
        btn_getRank=findViewById(R.id.getRank);
        rank_area = findViewById(R.id.rankArea);
        jsonTool=new JsonTool();
        serverController.showRank();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_getRank.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       serverController.showRank();
                        ShowData();
                    }
                }
        );

    }

    private void addRow(Msg item,int rank)
    {
//        TableRow tableRow = new TableRow(this);
//
        TableRow tableRow = new TableRow(this);
        TextView rankView = new TextView(this);
        TextView scoreView = new TextView(this);

        rankView.setText("No."+rank);
        scoreView.setText(item.getScore());


        tableRow.addView(rankView);
        tableRow.addView(scoreView);
        rank_area.addView(tableRow);

        TableLayout.LayoutParams text_row_Params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.MATCH_PARENT);
        tableRow.setLayoutParams(text_row_Params);
        TableRow.LayoutParams text_view_Params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.MATCH_PARENT,3);
        rankView.setLayoutParams(text_view_Params);
        scoreView.setLayoutParams(text_view_Params);
        rankView.setGravity(Gravity.CENTER);
        scoreView.setGravity(Gravity.CENTER);//设置字体居中
        rankView.setTextSize(0x1e);
        rankView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        scoreView.setTextSize(0x1e);

        //得到AssetManager
        AssetManager mgr=getAssets();
        //根据路径得到Typeface
        Typeface tf=Typeface.createFromAsset(mgr, "fonts/SHOWG.TTF");
        rankView.setTypeface(tf);
        scoreView.setTypeface(tf);

    }



     private void ShowData() {
         serverController.showRank();//发送请求所有数据的信息

         if (serverController.result.length() != 0)
         {

         List<Msg> datas = new ArrayList<Msg>();
         String json = serverController.result;//获取到结果
         datas = jsonTool.turnmore(json);//结果集转换

         int length = datas.size();
         rank_area.removeAllViews();
         for (int i = 0; i < length; i++) {
             Msg item = datas.get(i);//获取单行数据
             addRow(item,i+1);
         }
     }
     else
         {
             Log.i("Log","排名没有获取成功");
         }
    }


}
