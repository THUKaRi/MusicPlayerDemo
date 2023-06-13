package edu.csu.demo.musicplayer.model;

import android.graphics.Bitmap;

public class SongList {
    private int listID;
    private Bitmap album_icon;
    private String listName;
    private int songCount;

    public SongList(){};

    public SongList( int listID, Bitmap album_icon, String listName, int songCount) {
        this.listID = listID;
        this.album_icon = album_icon;
        this.listName = listName;
        this.songCount = songCount;
    }

    public int getListID() {
        return listID;
    }

    public void setListID(int listID) {
        this.listID = listID;
    }

    public Bitmap getAlbum_icon() {
        return album_icon;
    }

    public void setAlbum_icon(Bitmap album_icon) {
        this.album_icon = album_icon;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }
}
