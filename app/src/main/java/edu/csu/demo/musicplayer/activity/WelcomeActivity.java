package edu.csu.demo.musicplayer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import edu.csu.demo.musicplayer.R;
import edu.csu.demo.musicplayer.myView.Lead;
import edu.csu.demo.musicplayer.myView.LeadTextView;
import edu.csu.demo.musicplayer.tool.TypefacesUtil;


public class WelcomeActivity extends AppCompatActivity {

    LeadTextView leadTv;
    private static final int mDuration = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        initView();//延时秒数
        initLeadText();//添加启动页动画
    }


    private void initLeadText(){
        leadTv=findViewById(R.id.my_text_view);
        leadTv.setTypeface(TypefacesUtil.get(this,"Satisfy-Regular.ttf"));
        final Lead lead = new Lead(mDuration);
        lead.start(leadTv);
        new Lead(mDuration).start(leadTv);
    }


    private void initView(){
       Thread td=new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   Thread.sleep(3*900);
                   toMain();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       });
       td.start();
    }


    /**
     * 跳转到主页MainActivity
     */
    private void toMain(){
        Intent intent=new Intent(this,DisplayActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);//过渡动画
    }
}
