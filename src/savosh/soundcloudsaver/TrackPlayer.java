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

public class TrackPlayer {

    private ImageView play;
    private ImageView pause;
    private ImageView stop;
    private TextView title;
    private ProgressBar progress;
    private TextView time;
    private TextView nextTextView;

    private Context context;
    private MediaPlayer mediaPlayer = null;
    private PlayerProgress playerProgress = null;
    private Track current = null;
    private Track next = null;

    private TrackPlayer(final Context context) {
        this.context = context;
    }

    private void initViews(View root) {
        this.play = (ImageView) root.findViewById(R.id.main_search_fragment_play);
        this.pause = (ImageView) root.findViewById(R.id.main_search_fragment_pause);
        this.stop = (ImageView) root.findViewById(R.id.main_search_fragment_stop);
        this.title = (TextView) root.findViewById(R.id.main_search_fragment_title);
        this.progress = (ProgressBar) root.findViewById(R.id.main_search_fragment_progress_play);
        this.time = (TextView) root.findViewById(R.id.main_search_fragment_time);
        this.nextTextView = (TextView) root.findViewById(R.id.main_search_fragment_next);

        if (current != null) {
            this.title.setText(current.getTitle());
        }
        if (next != null) {
            this.nextTextView.setText(next.getTitle());
        }

        this.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current != null) {
                    if (mediaPlayer == null) {
                        try {
                            Log.i(getClass().getName(), "Stream url: " + current.getStreamUrl());
                            mediaPlayer = MediaPlayer.create(context, Uri.parse(current.getStreamUrl()));
                            mediaPlayer.start();
                            playerProgress = new PlayerProgress();
                        } catch (Exception e) {
                            Log.e(getClass().getName(), "Error in block start play track: " + e.getMessage(), e);
                        }
                    } else {
                        mediaPlayer.start();
                        playerProgress = new PlayerProgress();
                    }
                }
            }
        });

        this.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Pause", Toast.LENGTH_SHORT).show();
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                }
                if (playerProgress != null) {
                    playerProgress.setStop(true);
                }
            }
        });

        this.stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Stop", Toast.LENGTH_SHORT).show();
                current = next;
                player.title.setText(next.getTitle());
                next = null;
                nextTextView.setVisibility(View.GONE);
                release();
            }
        });
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
        if (playerProgress != null) {
            playerProgress.setStop(true);
        }
    }

    private static TrackPlayer player;

    public static void init(Context context, View root) {
        if (player == null) {
            player = new TrackPlayer(context);
        }
        player.initViews(root);
    }

    public static void put(Track track) {
        if (player.current == null) {
            player.title.setText(track.getTitle());
            player.current = track;
        } else {
            player.nextTextView.setVisibility(View.VISIBLE);
            player.nextTextView.setText(track.getTitle());
            player.next = track;
        }
    }

    public static void destroy() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private class PlayerProgress extends AsyncTask<Void, Void, Void> {

        public PlayerProgress() {
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
            int currProgress = (int) (currPosition / (float) mediaPlayer.getDuration() * 100);
            Log.i(getClass().getName(), "currProgress: " + currProgress);
            progress.setProgress(currProgress);

            time.setText("" + (currPosition / (60000)) + ":" + (currPosition / 1000 - currPosition / (60000) * 60));

        }

        @Override
        protected Void doInBackground(Void... params) {
            while (true) {
                if (mediaPlayer == null) {
                    return null;
                }
                if (stop) {
                    return null;
                }
                if (!mediaPlayer.isPlaying()) {
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
