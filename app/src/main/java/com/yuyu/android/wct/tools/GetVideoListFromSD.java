package com.yuyu.android.wct.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import com.yuyu.android.wct.videoappend.utils.VideoInfo;

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
    public List<VideoInfo> getList() {
        List<VideoInfo> videosList = new ArrayList<>();
        if (context != null) {
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    VideoInfo video = new VideoInfo();
                    video.title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    video.path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    video.duration = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    video.resolution = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION));
                    video.size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    video.bitmap = createVideoThumbnail(video.path);
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
