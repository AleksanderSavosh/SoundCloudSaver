package savosh.soundcloudsaver.listener;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import savosh.soundcloudsaver.ObjectsLocator;
import savosh.soundcloudsaver.Operation;
import savosh.soundcloudsaver.PlayerService;
import savosh.soundcloudsaver.R;


public class OnPlayerButtonClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_activity_play:
                Log.i(getClass().getName(), "Play!");
                ObjectsLocator.operation = Operation.PLAY;
                break;
            case R.id.main_activity_pause:
                Log.i(getClass().getName(), "Pause!");
                ObjectsLocator.operation = Operation.PAUSE;
                break;
            case R.id.main_activity_stop:
                Log.i(getClass().getName(), "Stop!");
                ObjectsLocator.operation = Operation.STOP;
                break;
        }
        ObjectsLocator.mainActivity.startService(new Intent(ObjectsLocator.mainActivity, PlayerService.class));
    }
}