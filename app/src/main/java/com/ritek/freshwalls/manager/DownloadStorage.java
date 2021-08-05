package com.ritek.freshwalls.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ritek.freshwalls.entity.Wallpaper;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DownloadStorage {
    private final String STORAGE_IMAGE = "MY_Download_RINGTONE";

    private SharedPreferences preferences;
    private Context context;
    public DownloadStorage(Context context) {
        this.context = context;
    }
    public void storeRingtone(ArrayList<Wallpaper> arrayList) {
        preferences = context.getSharedPreferences(STORAGE_IMAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("DownloadsListRintone", json);
        editor.apply();
    }
    public ArrayList<Wallpaper> loadRingtonesFavorites() {
        preferences = context.getSharedPreferences(STORAGE_IMAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("DownloadsListRintone", null);
        Type type = new TypeToken<ArrayList<Wallpaper>>() {
        }.getType();

        return gson.fromJson(json, type);
    }
}
