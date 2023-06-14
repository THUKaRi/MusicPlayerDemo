package edu.csu.demo.musicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.csu.demo.musicplayer.R;
import edu.csu.demo.musicplayer.db.MyDbFunctions;

public class addListActivity extends AppCompatActivity {

    private static MyDbFunctions myDbFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        Button button = findViewById(R.id.addListButton);

        myDbFunctions = MyDbFunctions.getInstance(this);

        Toolbar toolbar = findViewById(R.id.toolbar_activity_addLists);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {//toolbar回退键
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addListActivity.this, ListsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.addListName);
                if(myDbFunctions.createList(editText.getText().toString())){
                    Toast.makeText(addListActivity.this,"添加歌单成功",Toast.LENGTH_SHORT).show();
                    editText.setText("");
                }else {
                    Toast.makeText(addListActivity.this,"歌单已存在",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}