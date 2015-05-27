package savosh.soundcloudsaver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import savosh.soundcloudsaver.model.Track;


public class SearchedItemsArrayAdapter extends ArrayAdapter<Track> {

    private View.OnClickListener saveOnClickListener;

    public SearchedItemsArrayAdapter(Context context, View.OnClickListener saveOnClickListener) {
        super(context, R.layout.main_search_fragment_list_item);
        this.saveOnClickListener = saveOnClickListener;
    }

    private class ViewHolder {
        ImageView image;
        TextView title;
        TextView likesNumber;
        TextView time;
        ImageView save;
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
            viewHolder.save = (ImageView) row.findViewById(R.id.main_search_fragment_list_item_save);

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

        viewHolder.save.setTag(track);
        viewHolder.save.setOnClickListener(saveOnClickListener);

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
