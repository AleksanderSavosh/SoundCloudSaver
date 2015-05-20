package savosh.soundcloudsaver.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.turbomanage.httpclient.BasicHttpClient;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import savosh.soundcloudsaver.model.Track;
import savosh.soundcloudsaver.trans.ListTrackJsonDeserializer;

import java.util.List;

public class TrackService {

    private String doGet(String text){
        BasicHttpClient basicHttpClient = new BasicHttpClient("http://api.soundcloud.com");
        ParameterMap parameterMap = basicHttpClient.newParams()
                .add("q", text)
                .add("client_id", "b45b1aa10f1ac2941910a7f0d10f8e28");
        basicHttpClient.setConnectionTimeout(2000);
        HttpResponse httpResponse = basicHttpClient.get("/tracks.json", parameterMap);
        return httpResponse.getBodyAsString();
    }

    public List<Track> find(String text){
        String response = doGet(text);
        Gson gson = new GsonBuilder().registerTypeAdapter(List.class, new ListTrackJsonDeserializer()).create();
        return gson.fromJson(response, List.class);
    }

    public static void main(String[] args) {
        System.out.println(new TrackService().find("test"));
    }


}
