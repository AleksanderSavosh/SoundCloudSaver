package savosh.soundcloudsaver;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import savosh.soundcloudsaver.model.Track;

import java.io.IOException;

public class SearchedItemsArrayAdapter extends ArrayAdapter<Track> {

    private static class Player {
        private Context context;
        private Player(Context context){
            this.context = context;
        }
        private static Player player;
        public static Player with(Context context){
            if(player == null){
                player = new Player(context);
            }
            player.context = context;
            return player;
        }

        MediaPlayer mediaPlayer = null;
        String currUrlStream = "";
        public Player start(String urlStream){
            if(currUrlStream.equalsIgnoreCase(urlStream)){
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                return this;
            }

            currUrlStream = urlStream;

            releaseMP();
            if(mediaPlayer == null){
                try {
                    Log.i(getClass().getName(),"Stream url: " + currUrlStream);
                    Uri uri = Uri.parse(currUrlStream);
                    Log.i(getClass().getName(),"Stream uri: " + uri);
//                    mediaPlayer = new MediaPlayer();
//                    mediaPlayer.setDataSource(currUrlStream);
                    mediaPlayer = MediaPlayer.create(context, uri);
//                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    mediaPlayer.prepareAsync();
                    mediaPlayer.start();
                } catch(Exception e){
                    Log.e(getClass().getName(), "Error in block start play track: " + e.getMessage(), e);
                }
            }
            return this;
        }

        public Player pause(){
            if(mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
            return this;
        }

        public Player stop(){
            if(mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
            return this;
        }

        public void destroy(){
            releaseMP();
        }

        private void releaseMP() {
            if (mediaPlayer != null) {
                try {
                    mediaPlayer.release();
                    mediaPlayer = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public SearchedItemsArrayAdapter(Context context) {
        super(context, R.layout.main_search_fragment_list_item);
    }

    private class ViewHolder {
        ImageView image;
        TextView title;
        TextView likesNumber;
        TextView time;
        ImageView play;
        ImageView pause;
        ImageView stop;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.main_search_fragment_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) row.findViewById(R.id.main_search_fragment_list_item_image);
            viewHolder.title = (TextView) row.findViewById(R.id.main_search_fragment_list_item_title);
            viewHolder.likesNumber = (TextView) row.findViewById(R.id.main_search_fragment_list_item_likes_number);
            viewHolder.time = (TextView) row.findViewById(R.id.main_search_fragment_list_item_time);

            viewHolder.play = (ImageView) row.findViewById(R.id.main_search_fragment_list_item_play);
            viewHolder.pause = (ImageView) row.findViewById(R.id.main_search_fragment_list_item_pause);
            viewHolder.stop = (ImageView) row.findViewById(R.id.main_search_fragment_list_item_stop);

            row.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder) row.getTag();
        final Track track = getItem(position);

        if(track.getArtworkUrl() != null && track.getArtworkUrl().trim().length() > 0) {
            Picasso.with(getContext()).load(track.getArtworkUrl().replace("large", getContext()
                    .getResources().getString(R.string.main_search_fragment_image_size))).into(viewHolder.image);
        }

        if(track.getTitle() != null && track.getTitle().trim().length() > 0){
            viewHolder.title.setText(track.getTitle().toUpperCase());
        } else {
            viewHolder.title.setText("No title".toUpperCase());
        }

        if(track.getLikesCount() != null){
            viewHolder.likesNumber.setText(track.getLikesCount().toString());
        } else {
            viewHolder.likesNumber.setText("No likes number");
        }

        if(track.getDuration() != null){
            viewHolder.time.setText(durationToTime(track.getDuration()));
        } else {
            viewHolder.time.setText("No track duration");
        }


        viewHolder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.with(getContext()).start(track.getStreamUrl() + "?client_id=b45b1aa10f1ac2941910a7f0d10f8e28");
            }
        });

        viewHolder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.player.pause();
            }
        });

        viewHolder.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Player.player.stop();
            }
        });

        return row;
    }

    private String durationToTime(long d){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append((int)d/60000);
        stringBuilder.append(":");
        stringBuilder.append((int)d/1000 - ((int)d/60000)*60);

        return stringBuilder.toString();
    }

}
