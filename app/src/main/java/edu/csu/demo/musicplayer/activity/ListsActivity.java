package edu.csu.demo.musicplayer.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
import edu.csu.demo.musicplayer.tool.SongAdapter;

public class ListsActivity extends BaseActivity {

    private static List<SongList> myLists = new ArrayList<>();//有序可重复
    private static MyDbFunctions myDbFunctions;

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
        final ListAdapter adapter = new ListAdapter(this,R.layout.song_list_item,myLists);
        final ListView listView = findViewById(R.id.listView_activity_myLists);
        listView.setAdapter(adapter);

        if(myLists.size()==0){
            Toast.makeText(ListsActivity.this, "还没有创建过歌单", Toast.LENGTH_SHORT).show();
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.button_to_add_list);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListsActivity.this, addListActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SongList songList = myLists.get(position);
                String s = songList.getListName();
                Intent intent =new Intent(ListsActivity.this,ListSongsActivity.class);
                intent.putExtra("listName",s);
                startActivity(intent);
            }
        });

        adapter.setOnItemMoreOptionsClickListener(new SongAdapter.onItemMoreOptionsListener() {
            @Override
            public void onMoreOptionsClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListsActivity.this)
                        .setTitle("请确认!")
                        .setIcon(R.drawable.danger)
                        .setMessage("确认要删除此歌单吗")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*which
                                 *int BUTTON_POSITIVE = -1; int BUTTON_NEGATIVE = -2;   int BUTTON_NEUTRAL = -3;*/
//                                Toast.makeText(DisplayActivity.this,"你点击了确定"+position,Toast.LENGTH_SHORT).show();
                                //判断是否该歌曲正在播放
                                //从数据库中删除该歌曲的喜爱标志
                                if(myDbFunctions != null){
                                    myDbFunctions.deleteList(myLists.get(position).getListID());
                                }
                                //从内存喜爱的歌曲列表中删除该歌曲
                                myLists.remove(position);
                                //通知列表数据变化了
                                if(adapter != null){
                                    adapter.notifyDataSetChanged();
                                }
                                if(listView != null){
                                    listView.invalidate();
                                }
                                Toast.makeText(ListsActivity.this,"已删除",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(ListsActivity.this,"下次不要点错了哦",Toast.LENGTH_SHORT).show();
                            }
                        })
                        ;
                builder.create().show();
            }
        });

    }
}