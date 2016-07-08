package com.yuyu.android.wct.tools;

import android.os.Environment;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.yuyu.android.wct.utils.Videos;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by bernie.shi on 2016/3/15.
 */
public class AppendVideos {
    public static String saveVideoPath = "Rollit/";
    public static String appendVideo(List<Videos> videos) throws IOException {
        String appendVideoPath = "";
        Movie[] inMovies = new Movie[videos.size()];
        int index = 0;
        for(int i=0;i<videos.size();i++)
        {
            inMovies[index] = MovieCreator.build(videos.get(i).path);
            index++;
        }
        List<Track> videoTracks = new LinkedList<Track>();
        List<Track> audioTracks = new LinkedList<Track>();
        for (Movie m : inMovies) {
            for (Track t : m.getTracks()) {
                if (t.getHandler().equals("soun")) {
                    audioTracks.add(t);
                }
                if (t.getHandler().equals("vide")) {
                    videoTracks.add(t);
                }
            }
        }
        Movie result = new Movie();
        if (audioTracks.size() > 0) {
            result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
        }
        if (videoTracks.size() > 0) {
            result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
        }
        Container out = new DefaultMp4Builder().build(result);
        File sampleDir = new File(Environment.getExternalStorageDirectory()
                + File.separator + saveVideoPath);
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        appendVideoPath = sampleDir + "/rollit_video" + String.valueOf(System.currentTimeMillis()) +".mp4";
        FileChannel fc = new RandomAccessFile(appendVideoPath, "rw").getChannel();
        out.writeContainer(fc);
        fc.close();
        return appendVideoPath;
    }
}
