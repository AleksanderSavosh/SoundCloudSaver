package savosh.soundcloudsaver.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import savosh.soundcloudsaver.convert.ListTrackJsonDeserializer;
import savosh.soundcloudsaver.http.Http;
import savosh.soundcloudsaver.model.Track;

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

}
