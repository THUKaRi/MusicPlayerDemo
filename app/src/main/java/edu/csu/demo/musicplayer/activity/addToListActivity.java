package edu.csu.demo.musicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.csu.demo.musicplayer.R;
import edu.csu.demo.musicplayer.db.MyDbFunctions;
import edu.csu.demo.musicplayer.model.BaseActivity;
import edu.csu.demo.musicplayer.model.SongList;
import edu.csu.demo.musicplayer.tool.ListAdapter;

public class addToListActivity extends BaseActivity {

    private static List<SongList> myLists = new ArrayList<>();//有序可重复
    private static MyDbFunctions myDbFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_list);

        Intent intent = getIntent();
        final String songTitle = intent.getStringExtra("CURRENT_SONG_NAME");

        Toolbar toolbar = findViewById(R.id.toolbar_activity_addToList);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {//toolbar回退键
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addToListActivity.this, DisplayActivity.class);
                startActivity(intent);
                finish();
            }
        });

        myDbFunctions = MyDbFunctions.getInstance(this);

        myLists = myDbFunctions.loadLists();//从数据库加载,注意加载出来的这些Song对象没有设置专辑图片
        final ListAdapter adapter = new ListAdapter(this,R.layout.song_list_item,myLists);
        final ListView listView = findViewById(R.id.listView_activity_addToList);
        listView.setAdapter(adapter);

        FloatingActionButton floatingActionButton = findViewById(R.id.button_add_list);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addToListActivity.this, addListActivity.class);
                startActivity(intent);
            }
        });

        if(myLists.size()==0){
            Toast.makeText(addToListActivity.this, "还没有创建过歌单,请先创建歌单", Toast.LENGTH_SHORT).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(myDbFunctions.addSongToList(myLists.get(position).getListName(),songTitle)){
                    Toast.makeText(addToListActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(addToListActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
