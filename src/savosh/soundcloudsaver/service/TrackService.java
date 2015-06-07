package savosh.soundcloudsaver.service;

import android.util.Log;
import savosh.soundcloudsaver.cache.Cache;
import savosh.soundcloudsaver.dao.TrackDao;
import savosh.soundcloudsaver.model.Track;
import savosh.soundcloudsaver.util.FileUtil;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TrackService {

    public static List<Track> find(String text) {
        Log.d(TrackService.class.getName(), "Find " + text);
        if (Cache.contains(text)) {
            return (List<Track>) Cache.get(text);
        }

        List<Track> tracks = TrackDao.find(text);
        if (tracks != null) {
            Cache.put(text, (Serializable) tracks);
        }
        return tracks;
    }

    public static void save(List<Track> tracks) {
        TrackDao.put(tracks);
    }

    public static List<Track> read() {
        List<Track> tracks = TrackDao.getSaved();
        if (tracks == null) {
            return new ArrayList<>();
        }

        List<Track> result = new ArrayList<>();
        List<File> downloadFiles = FileUtil.getDownloadMp3Files();
        List<String> fileNames = new ArrayList<>();
        for (File file : downloadFiles) {
            fileNames.add(file.getName());
        }

        for (Track track : tracks) {
            if (fileNames.contains(track.getTitle() + ".mp3")) {
                result.add(track);
                Log.d(TrackService.class.getName(), "Read saved file: " + track);
            }
        }

        return result;
    }


    public static void main(String[] args) {
        System.out.println(new TrackService().find("test"));
    }

}
