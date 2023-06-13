package edu.csu.demo.musicplayer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import edu.csu.demo.musicplayer.model.Song;
import edu.csu.demo.musicplayer.model.SongList;
import edu.csu.demo.musicplayer.tool.PictureDealHelper;

public class MyDbFunctions {
    //数据库名
    public static final String DB_NAME = "Mung_Music";
    //数据库版本
    public static final int VERSION = 1;
    private volatile static MyDbFunctions myDbFunctions;
    private SQLiteDatabase db;
    private WeakReference<Context> weakReference;//弱引用方式引入context
    //私有化构造方法,单例模式
    private MyDbFunctions(Context context){
        weakReference = new WeakReference<>(context);
        db = new MyDbHelper(weakReference.get(),DB_NAME,null,VERSION).getWritableDatabase();
    }
    /*双重锁模式*/
    public static MyDbFunctions getInstance(Context context){
        if(myDbFunctions == null){//为了避免不必要的同步
            synchronized (MyDbFunctions.class){
                if(myDbFunctions ==null){//为了在实例为空时才创建实例
                    myDbFunctions = new MyDbFunctions(context);
                }
            }
        }
        return myDbFunctions;
    }
    /**
     * 将Song实例存储到数据库的SONGS表中*/
    public void saveSong(Song song){
        if(song != null && db != null){
            ContentValues values = new ContentValues();
            values.put("title",song.getTitle());
            values.put("artist",song.getArtist());
            values.put("duration",song.getDuration());
            values.put("dataPath",song.getDataPath());
            if(song.isLove())
                values.put("isLove","true");
            else
                values.put("isLove","false");
            if(song.isDefaultAlbumIcon())
                values.put("isDefaultAlbumIcon","true");
            else
                values.put("isDefaultAlbumIcon","false");
            db.insert("SONGS",null,values);
        }
    }
    /**
     * 将Song实例从数据库的MyLoveSongs表中删除*/
    public void removeSong(String dataPath){
        if(dataPath != null && db != null){
            //db.execSQL("delete from lxrData where name=?", new String[] { name });
            db.delete("SONGS","dataPath=?",new String[]{dataPath});
        }
    }
    /**
     * 给SONGS表中的某个歌曲修改isLove标志*/
    public void setLove(String dataPath,String flag){
        ContentValues values = new ContentValues();
        values.put("isLove",flag);
        db.update("SONGS",values,"dataPath=?",new String[]{dataPath});
    }
    /**
     * 从数据库读取SONGS表中所有的我喜爱的歌曲*/
    public ArrayList<Song> loadMyLoveSongs(){
        ArrayList<Song> list = new ArrayList<>();
        if(db != null){
            Cursor cursor = db.query("SONGS",null,"isLove =?",new String[]{"true"},null,null,null);
            if(cursor.moveToFirst()){
                do{
                    Song song = new Song();
                    song.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    song.setArtist(cursor.getString(cursor.getColumnIndex("artist")));
                    song.setDuration(cursor.getLong(cursor.getColumnIndex("duration")));
                    song.setDataPath(cursor.getString(cursor.getColumnIndex("dataPath")));
                    song.setLove(true);
                    String flag2 = cursor.getString(cursor.getColumnIndex("isDefaultAlbumIcon"));
                    if(flag2.equals("true"))
                        song.setFlagDefaultAlbumIcon(true);
                    else
                        song.setFlagDefaultAlbumIcon(false);
                    song.setAlbum_icon(PictureDealHelper.getAlbumPicture(weakReference.get(),song.getDataPath(),96,96));
                    list.add(song);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }
    /**
     * 读取数据库中的所有歌曲*/
    public ArrayList<Song> loadAllSongs(){
        ArrayList<Song> list = new ArrayList<>();
        if(db != null){
            Cursor cursor = db.query("SONGS",null,null,null,null,null,null);
            if(cursor.moveToFirst()){
                do{
                    Song song = new Song();
                    song.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    song.setArtist(cursor.getString(cursor.getColumnIndex("artist")));
                    song.setDuration(cursor.getLong(cursor.getColumnIndex("duration")));
                    song.setDataPath(cursor.getString(cursor.getColumnIndex("dataPath")));
                    String flag1 = cursor.getString(cursor.getColumnIndex("isLove"));
                    if(flag1.equals("true"))
                        song.setLove(true);
                    else
                        song.setLove(false);
                    String flag2 = cursor.getString(cursor.getColumnIndex("isDefaultAlbumIcon"));
                    if(flag2.equals("true")){
                        song.setFlagDefaultAlbumIcon(true);
//                        Log.w("MyDbFunctions","isDefaultAlbumIcon:   "+flag2);
                    }
                    else{
//                        Log.w("MyDbFunctions","isDefaultAlbumIcon:   "+flag2);
                        song.setFlagDefaultAlbumIcon(false);
                    }
                    song.setAlbum_icon(PictureDealHelper.getAlbumPicture(weakReference.get(),song.getDataPath(),96,96));
                    list.add(song);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        return list;
    }

    public void createList(String listName){
        if(listName != null && !listName.equals("") && db != null){
            ContentValues values = new ContentValues();
            values.put("listName",listName);
            db.insert("SONG_LIST",null,values);
        }
    }

    public ArrayList<SongList> loadLists(){
        ArrayList<SongList> songLists = new ArrayList<>();
        if(db!=null){
            Cursor cursor = db.query("SONG_LIST",null,null,null,null,null,null);
            if (cursor.moveToFirst()){
                do {
                    SongList songList = new SongList();
                    Song song = getListFirstSong(cursor.getInt(cursor.getColumnIndex("id")));
                    songList.setAlbum_icon(song.getAlbum_icon());
                    songList.setListID(cursor.getInt(cursor.getColumnIndex("id")));
                    songList.setListName(cursor.getString(cursor.getColumnIndex("listName")));
                    songList.setSongCount(getListSongs(songList.getListName()).size());
                    songLists.add(songList);
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        return songLists;
    }

    public int addSongToList(String listName,Song song){
        int listID = 0;
        int songID = 0;
        if(db!=null&&listName!=null){
            listID = getListID(listName);
            Cursor cursor1 = db.query("SONGS",null,"title=?",new String[]{song.getTitle()},null,null,null);
            songID=cursor1.getInt(cursor1.getColumnIndex("id"));
            cursor1.close();
        }
        //如果已经有就不再添加
        if(listID!=0&&songID!=0){
            Cursor cursor = db.query("LIST_SONGS",null,"listID=?,songID=?",new String[]{String.valueOf(listID),String.valueOf(songID)},null,null,null);
            if(cursor.moveToFirst()){
                ContentValues values = new ContentValues();
                values.put("songID",songID);
                values.put("listID",listID);
                db.insert("LIST_SONGS",null,values);
                cursor.close();
                return 1;
            }
        }
        return 0;
    }

    public int getListID(String listName){
        int listID = 0;
        if(db!=null&&listName!=null){
            Cursor cursor = db.query("SONG_LIST",null,"listName=?",new String[]{listName},null,null,null);
            listID=cursor.getInt(cursor.getColumnIndex("id"));
            cursor.close();
        }
        return listID;
    }

    public ArrayList<Song> getListSongs(String listName){
        ArrayList<Song> songs = new ArrayList<>();
        if(db!=null){
            int listID = getListID(listName);
            Cursor cursor = db.query("LIST_SONGS",null,"listID=?",new String[]{String.valueOf(listID)},null,null,null);
            if(cursor.moveToFirst()){
                do {
                    Song song = getSongByID(cursor.getInt(cursor.getColumnIndex("songID")));
                    if(song!=null){
                        songs.add(song);
                    }
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        return songs;
    }

    public Song getListFirstSong(int listID){
        int songID = 0;
        if (db!=null){
            Cursor cursor = db.query("LIST_SONGS",null,"listID=?",new String[]{String.valueOf(listID)},null,null,null);
            if(cursor.moveToFirst()){
                songID = cursor.getInt(cursor.getColumnIndex("songID"));
            }
            cursor.close();
        }
        return getSongByID(songID);
    }

    public Song getSongByID(int songID){
        if(db!=null){
            Cursor cursor = db.query("SONGS",null,"id=?",new String[]{String.valueOf(songID)},null,null,null);
            if (cursor.moveToFirst()){
                Song song = new Song();
                song.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                song.setArtist(cursor.getString(cursor.getColumnIndex("artist")));
                song.setDuration(cursor.getLong(cursor.getColumnIndex("duration")));
                song.setDataPath(cursor.getString(cursor.getColumnIndex("dataPath")));
                String flag1 = cursor.getString(cursor.getColumnIndex("isLove"));
                if (flag1.equals("true"))
                    song.setLove(true);
                else
                    song.setLove(false);
                String flag2 = cursor.getString(cursor.getColumnIndex("isDefaultAlbumIcon"));
                if (flag2.equals("true")) {
                    song.setFlagDefaultAlbumIcon(true);
                } else {
                    song.setFlagDefaultAlbumIcon(false);
                }
                song.setAlbum_icon(PictureDealHelper.getAlbumPicture(weakReference.get(), song.getDataPath(), 96, 96));
                return song;
            }
            cursor.close();
        }
        return null;
    }

    /**
     * 判断当前SONGS表中是否有数据*/
    public boolean isSONGS_Null(){
        if(db != null){
            Cursor cursor = db.query("SONGS",null,null,null,null,null,null);
            if(cursor.moveToFirst()){
                return false;//不为空
            }
            cursor.close();
        }
        return true;//空
    }
}
