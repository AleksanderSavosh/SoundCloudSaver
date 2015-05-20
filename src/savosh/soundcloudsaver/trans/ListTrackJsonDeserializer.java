package savosh.soundcloudsaver.trans;

import com.google.gson.*;
import savosh.soundcloudsaver.model.Track;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListTrackJsonDeserializer implements JsonDeserializer<List<Track>> {
    @Override
    public List<Track> deserialize(JsonElement jsonElement, Type type,
                             JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ArrayList<Track> tracks = new ArrayList<Track>();
        if(jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            for(JsonElement element : jsonArray){
                if(element.isJsonObject()){
                    JsonObject jsonObject = element.getAsJsonObject();
                    Track track = new Track();
                    track.setTitle(jsonObject.get("title").getAsString());
                    if(jsonObject.has("artwork_url") && !jsonObject.get("artwork_url").isJsonNull()) {
                        track.setArtworkUrl(jsonObject.get("artwork_url").getAsString());
                    }
                    track.setDuration(jsonObject.get("duration").getAsLong());
                    if(jsonObject.has("likes_count") && !jsonObject.get("likes_count").isJsonNull()) {
                        track.setLikesCount(jsonObject.get("likes_count").getAsInt());
                    }
                    track.setStreamUrl(jsonObject.get("stream_url").getAsString());
                    tracks.add(track);
                }
            }
        }
        return tracks;
    }
}
