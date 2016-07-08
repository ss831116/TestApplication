package com.yuyu.dreamachieve.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import com.yuyu.dreamachieve.utils.Videos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public class GetVideoListFromSD {
    Context context;
    public GetVideoListFromSD(Context context){
        this.context = context;
    }
    public List<Videos> getList() {
        List<Videos> videosList = new ArrayList<>();
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    int duration = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    String resolution = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
                    long size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    Bitmap bitmap = createVideoThumbnail(path);
                    Videos video = new Videos(title, path, duration,
                            resolution, size, bitmap);
                    videosList.add(video);
                }
                cursor.close();
            }
        }
        return videosList;
    }
    @SuppressLint("NewApi")
    private Bitmap createVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
        } catch (RuntimeException ex) {
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }
        return bitmap;
    }
}
