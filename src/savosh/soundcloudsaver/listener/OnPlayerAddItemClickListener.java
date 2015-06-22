package savosh.soundcloudsaver.listener;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import savosh.soundcloudsaver.ApplicationContext;
import savosh.soundcloudsaver.PlayerService;
import savosh.soundcloudsaver.model.Track;


public class OnPlayerAddItemClickListener implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(getClass().getName(), "onItemClick");
        Track track = (Track) parent.getAdapter().getItem(position);

        Intent intent = new Intent(ApplicationContext.instance, PlayerService.class);
        intent.putExtra(PlayerService.INTENT_KEY_ADD_TRACK, track);
        ApplicationContext.instance.startService(intent);
    }
}