package edu.csu.demo.musicplayer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {
    private static final String CREATE_SONGS =
            "create table SONGS ("
            + "id integer primary key autoincrement, "
            + "title text, "//歌名
            + "artist text, "//歌手
            + "duration integer,"//时长
            + "dataPath text, "//文件路径
            + "isLove text,"//是否是喜爱的歌曲
            + "isDefaultAlbumIcon text)";//是否使用的默认专辑图片

    private static final String CREATE_SONG_LIST =
            "create table SONG_LIST ("
                    + "id integer primary key autoincrement, "
                    + "listName text)";

    private static final String CREATE_LIST_SONGS =
            "create table LIST_SONGS ("
                    + "id integer primary key autoincrement, "
                    + "songID integer, "
                    + "listID Integer)";

    private Context mContext;
    public MyDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SONGS);
        db.execSQL(CREATE_SONG_LIST);
        db.execSQL(CREATE_LIST_SONGS);
        //建表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}
