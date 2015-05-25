package savosh.soundcloudsaver;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import savosh.soundcloudsaver.model.Track;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class TrackPlayer {

    private ImageView play;
    private ImageView pause;
    private ImageView stop;
    private TextView title;
    private ProgressBar progress;
    private TextView time;

    private Context context;
    private MediaPlayer mediaPlayer = null;
    private PlayerProgress playerProgress = null;
    private Track current = null;
    private Track next = null;

    private TrackPlayer(final Context context, View root) {
        this.context = context;
        this.play = (ImageView) root.findViewById(R.id.main_search_fragment_play);
        this.pause = (ImageView) root.findViewById(R.id.main_search_fragment_pause);
        this.stop = (ImageView) root.findViewById(R.id.main_search_fragment_stop);
        this.title = (TextView) root.findViewById(R.id.main_search_fragment_title);
        this.progress = (ProgressBar) root.findViewById(R.id.main_search_fragment_progress_play);
        this.time = (TextView) root.findViewById(R.id.main_search_fragment_time);

        this.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show();
                if(mediaPlayer == null){
                    playNext();
                } else if(next != null && current != null && !next.equals(current)){
                    release();
                    playNext();
                }
            }
        });

        this.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Pause", Toast.LENGTH_SHORT).show();
                pause();
            }
        });

        this.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Stop", Toast.LENGTH_SHORT).show();
                stop();
            }
        });
    }

    private static TrackPlayer player;

    public static TrackPlayer init(Context context, View root) {
        if (player == null) {
            player = new TrackPlayer(context, root);
        }
        return player;
    }

    public TrackPlayer put(Track track){
        title.setText(track.getTitle());
        this.next = track;
        return this;
    }


    private void playNext(){
        if(next != null) {
            current = next;
            next = null;
            this.title.setText(current.getTitle());
            try {
                Log.i(getClass().getName(), "Stream url: " + current.getStreamUrl());
                InputStream inputStream = new URL(current.getStreamUrl()).openConnection().getInputStream();


                mediaPlayer = MediaPlayer.create(context, Uri.parse(current.getStreamUrl()));
                mediaPlayer.start();
                playerProgress = new PlayerProgress();
            } catch (Exception e) {
                Log.e(getClass().getName(), "Error in block start play track: " + e.getMessage(), e);
            }
        }
    }

    private void pause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
        playerProgress.setStop(true);
    }

    private void stop() {
        if(next == null){
            next = current;
            current = null;
        }
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }
        playerProgress.setStop(true);
    }

    public void destroy() {
        release();
    }

    private void release() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(playerProgress != null){
            playerProgress.setStop(true);
        }
    }

    private class PlayerProgress extends AsyncTask<Void, Void, Void>{

        public PlayerProgress(){
            execute();
        }

        private boolean stop = false;

        public void setStop(boolean stop) {
            this.stop = stop;
        }

        @Override
        protected void onPreExecute() {
            progress.setMax(100);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            int currPosition = mediaPlayer.getCurrentPosition();
            Log.i(getClass().getName(), "currPosition: " + currPosition);
            int currProgress = (int)(currPosition/(float)mediaPlayer.getDuration() * 100);
            Log.i(getClass().getName(), "currProgress: " + currProgress);
            progress.setProgress(currProgress);

            time.setText("" + (currPosition/(60000)) + ":" + (currPosition/1000 - currPosition/(60000)*60));

        }

        @Override
        protected Void doInBackground(Void... params) {
            while(true){
                if(mediaPlayer == null){
                    return null;
                }
                if(stop){
                    return null;
                }
                if(!mediaPlayer.isPlaying()){
                    return null;
                }
                publishProgress();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.e(getClass().getName(), "Error in update media player progress block code: "
                            + e.getMessage(), e);
                    return null;
                }
            }
        }
    }


}
