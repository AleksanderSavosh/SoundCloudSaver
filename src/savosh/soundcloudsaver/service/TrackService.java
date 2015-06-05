package savosh.soundcloudsaver.service;

import android.util.Log;
import savosh.soundcloudsaver.cache.Cache;
import savosh.soundcloudsaver.dao.TrackDao;
import savosh.soundcloudsaver.model.Track;

import java.io.Serializable;
import java.util.List;

public class TrackService {

    public static List<Track> find(String text){
        Log.d(TrackService.class.getName(), "Find " + text);
        if(Cache.contains(text)){
            return (List<Track>) Cache.get(text);
        }

        List<Track> tracks = TrackDao.find(text);
        if(tracks != null){
            Cache.put(text, (Serializable) tracks);
        }
        return tracks;
    }

    public static void main(String[] args) {
        System.out.println(new TrackService().find("test"));
    }

}
