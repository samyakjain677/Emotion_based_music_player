package com.samyakmyappcompany.emotionbasedmusicplayer;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

class SongUtils {
    private static final SimpleDateFormat HMS_FORMATTER_UTC = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

    static {
        HMS_FORMATTER_UTC.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));
    }

    public static List<Song> getLocalSongs(Context context) {
        List<Song> list = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                null, MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Song song = new Song();
                song.setId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                song.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                song.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));

                if (song.getTitle().contains("-")) {
                    String[] str = song.getTitle().split("-");
                    if (TextUtils.isEmpty(song.getArtist()))
                        song.setArtist(str[0]);
                    if (TextUtils.isEmpty(song.getTitle()))
                        song.setTitle(str[1]);
                }
                list.add(song);
            }
            cursor.close();
        }

        return list;
    }

   /* public static String secondsToHms(long s) {
        return millisecondsToHms(1000 * s);
    }*/

    public static String millisecondsToHms(long ms) {
        String result = HMS_FORMATTER_UTC.format(ms);
        if (result.startsWith("00:")) {
            result = result.substring(3);
        }
        return result;
    }
}
