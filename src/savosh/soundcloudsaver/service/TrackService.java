package savosh.soundcloudsaver.service;

import android.util.Log;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import savosh.soundcloudsaver.SimpleDiskCache;
import savosh.soundcloudsaver.model.Track;
import savosh.soundcloudsaver.trans.ListTrackJsonDeserializer;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TrackService {

    Cache<String, List<Track>> memCache = CacheBuilder.newBuilder().maximumSize(100).build();

    private TrackService(){}

    private static final TrackService TRACK_SERVICE = new TrackService();
    public static TrackService service(){
        return TRACK_SERVICE;
    }

    private String doGet(String text){
        try {
            BasicHttpClient basicHttpClient = new BasicHttpClient("http://api.soundcloud.com");
            ParameterMap parameterMap = basicHttpClient.newParams()
                    .add("q", text)
    //                .add("limit", "5")
                    .add("client_id", "b45b1aa10f1ac2941910a7f0d10f8e28");
            basicHttpClient.setConnectionTimeout(2000);
            HttpResponse httpResponse = basicHttpClient.get("/tracks.json", parameterMap);
            return httpResponse.getBodyAsString();
        } catch(Exception e){
            Log.e(getClass().getName(), "Error in do get request block code: " + e.getMessage(), e);
            return null;
        }
    }

    public List<Track> find(String text){
        List<Track> tracks = memCache.getIfPresent(text);
        if(tracks == null) {
            tracks = (List<Track>) SimpleDiskCache.get(text);
            if (tracks == null) {
                Log.d(getClass().getName(), "Load from internet: " + text);
                String response = doGet(text);
                if (response == null) {
                    return tracks;
                }
                Gson gson = new GsonBuilder().registerTypeAdapter(List.class, new ListTrackJsonDeserializer()).create();
                tracks = gson.fromJson(response, List.class);
                memCache.put(text, tracks);
                SimpleDiskCache.put(text, tracks);
            }
        }
        return tracks;
    }

    public static void main(String[] args) {
        System.out.println(new TrackService().find("test"));
    }


}
