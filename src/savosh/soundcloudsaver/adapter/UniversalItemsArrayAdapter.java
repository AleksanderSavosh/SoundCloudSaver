package savosh.soundcloudsaver.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import savosh.soundcloudsaver.R;
import savosh.soundcloudsaver.listener.OnSaveTrackClickListener;
import savosh.soundcloudsaver.model.Track;
import savosh.soundcloudsaver.task.SaveTask;

import java.util.HashMap;
import java.util.Map;

public class UniversalItemsArrayAdapter extends ArrayAdapter<Track> {

    public static final int MODE_FOR_SEARCH_RESULT = 0;
    public static final int MODE_FOR_SAVE_RESULT = 1;

    private final int mode;
    private final Map<Track, SaveTask> savingsTrack;

    public OnSaveTrackClickListener onSaveTrackClickListener = new OnSaveTrackClickListener();

    public UniversalItemsArrayAdapter(Context context, int mode, Map<Track, SaveTask> savingsTrack) {
        super(context, 0);
        this.mode = mode;
        this.savingsTrack = ((savingsTrack == null) ? new HashMap<Track, SaveTask>() : savingsTrack);
    }

    private class ViewHolder {
        ImageView image;
        ImageView imageSave;
        TextView title;
        ImageView imageHeart;
        TextView likesNumber;
        ImageView imageClock;
        TextView time;
        ProgressBar saveProgressBar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.universal_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) row.findViewById(R.id.universal_item_image);
            viewHolder.imageSave = (ImageView) row.findViewById(R.id.universal_item_save);
            viewHolder.title = (TextView) row.findViewById(R.id.universal_item_title);
            viewHolder.imageHeart = (ImageView) row.findViewById(R.id.universal_item_image_heart);
            viewHolder.likesNumber = (TextView) row.findViewById(R.id.universal_item_likes_number);
            viewHolder.imageClock = (ImageView) row.findViewById(R.id.universal_item_image_clock);
            viewHolder.time = (TextView) row.findViewById(R.id.universal_item_time);
            viewHolder.saveProgressBar = (ProgressBar) row.findViewById(R.id.universal_item_progress);

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

        if (track.getLikesCount() != null) {
            viewHolder.likesNumber.setText(track.getLikesCount().toString());
        } else {
            viewHolder.likesNumber.setText("No likes number");
        }

        if(track.getDuration() != null){
            viewHolder.time.setText(durationToTime(track.getDuration()));
        } else {
            viewHolder.time.setText("No track duration");
        }

        if(mode == MODE_FOR_SEARCH_RESULT) {
            viewHolder.imageSave.setTag(track);
            viewHolder.imageSave.setOnClickListener(onSaveTrackClickListener);
            viewHolder.saveProgressBar.setVisibility(View.GONE);
            viewHolder.imageHeart.setVisibility(View.VISIBLE);
            viewHolder.likesNumber.setVisibility(View.VISIBLE);
            viewHolder.imageClock.setVisibility(View.VISIBLE);
            viewHolder.time.setVisibility(View.VISIBLE);
        }

        if (mode == MODE_FOR_SAVE_RESULT) {
            viewHolder.imageSave.setVisibility(View.GONE);
            if (savingsTrack.containsKey(track)){
                savingsTrack.get(track).setProgressBar(viewHolder.saveProgressBar);
                viewHolder.saveProgressBar.setVisibility(View.VISIBLE);
                viewHolder.imageHeart.setVisibility(View.GONE);
                viewHolder.likesNumber.setVisibility(View.GONE);
                viewHolder.imageClock.setVisibility(View.GONE);
                viewHolder.time.setVisibility(View.GONE);
            } else {
                viewHolder.saveProgressBar.setVisibility(View.GONE);
                viewHolder.imageHeart.setVisibility(View.GONE);
                viewHolder.likesNumber.setVisibility(View.GONE);
                viewHolder.imageClock.setVisibility(View.VISIBLE);
                viewHolder.time.setVisibility(View.VISIBLE);
            }
        }


        return row;
    }

    private String durationToTime(long d){
        String minutes = "" + (int)d/60000;
        String seconds = "" + ((int)d/1000 - ((int)d/60000)*60);

        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        seconds = seconds.length() == 1 ? "0" + seconds : seconds;

        return new StringBuilder()
                .append(minutes).append(":").append(seconds).toString();
    }

}
