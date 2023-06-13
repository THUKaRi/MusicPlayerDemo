package edu.csu.demo.musicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.csu.demo.musicplayer.R;
import edu.csu.demo.musicplayer.db.MyDbFunctions;
import edu.csu.demo.musicplayer.model.SongList;

public class ListsActivity extends AppCompatActivity {

    private static List<SongList> myLists = new ArrayList<>();//有序可重复
    private static MyDbFunctions myDbFunctions;
    private int current_number,current_status,actual_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        Toolbar toolbar = findViewById(R.id.toolbar_activity_myLists);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {//toolbar回退键
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListsActivity.this, DisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myDbFunctions = MyDbFunctions.getInstance(this);

        myLists = myDbFunctions.loadLists();//从数据库加载,注意加载出来的这些Song对象没有设置专辑图片

    }
}