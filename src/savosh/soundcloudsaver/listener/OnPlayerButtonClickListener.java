package savosh.soundcloudsaver.listener;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import savosh.soundcloudsaver.*;


public class OnPlayerButtonClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ApplicationContext.instance, PlayerService.class);
        switch (v.getId()) {
            case R.id.main_activity_play:
                Log.i(getClass().getName(), "Play!");
                intent.putExtra(PlayerService.INTENT_KEY_OPERATION, Operation.PLAY);
                break;
            case R.id.main_activity_pause:
                Log.i(getClass().getName(), "Pause!");
                intent.putExtra(PlayerService.INTENT_KEY_OPERATION, Operation.PAUSE);
                break;
            case R.id.main_activity_stop:
                Log.i(getClass().getName(), "Stop!");
                intent.putExtra(PlayerService.INTENT_KEY_OPERATION, Operation.STOP);
                break;
        }
        ApplicationContext.instance.startService(intent);
    }
}