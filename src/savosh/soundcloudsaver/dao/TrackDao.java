package savosh.soundcloudsaver.dao;

import android.os.Environment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import savosh.soundcloudsaver.ObjectsLocator;
import savosh.soundcloudsaver.convert.ListTrackJsonDeserializer;
import savosh.soundcloudsaver.util.FileUtil;
import savosh.soundcloudsaver.util.Http;
import savosh.soundcloudsaver.model.Track;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrackDao {

    public static final String DOMAIN = "api.soundcloud.com";
    public static final String URN = "/tracks.json";
    public static final Map.Entry<String, String> AUTHORIZE_ENTRY =
            new AbstractMap.SimpleEntry<String, String>("client_id", "b45b1aa10f1ac2941910a7f0d10f8e28");

    public static List<Track> find(String text){
        List<Track> tracks = new ArrayList<>();
        String response = Http.get(DOMAIN, URN, AUTHORIZE_ENTRY,
                new AbstractMap.SimpleEntry<String, String>("q", text));

        Gson gson = new GsonBuilder().registerTypeAdapter(List.class, new ListTrackJsonDeserializer()).create();
        tracks = gson.fromJson(response, List.class);

        return tracks;
    }

    public static final String SAVE_KEY = "SAVED_TRACKS";
    public static List<Track> getSaved(){
        String savedTracks = ObjectsLocator.mainActivity.getFilesDir().getAbsolutePath() + "/" + SAVE_KEY;
        return (List<Track>) FileUtil.get(savedTracks);
    }

    public static void put(List<Track> tracks){
        String savedTracks = ObjectsLocator.mainActivity.getFilesDir().getAbsolutePath() + "/" + SAVE_KEY;
        FileUtil.put(savedTracks, (Serializable) tracks);
    }

}
