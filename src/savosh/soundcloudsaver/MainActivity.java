package savosh.soundcloudsaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import savosh.soundcloudsaver.listener.OnPlayerButtonClickListener;

import static savosh.soundcloudsaver.ObjectsLocator.*;

public class MainActivity extends FragmentActivity {

    public static final String BROADCAST_UPDATE_PROGRESS = "savosh.soundcloudsaver.MainActivity.UpdateProgress";
    public static final String BROADCAST_SET_CURRENT_TRACK_TITLE = "savosh.soundcloudsaver.MainActivity.SetCurrentTrackTitle";
    public static final String BROADCAST_SET_NEXT_TRACK_TITLE = "savosh.soundcloudsaver.MainActivity.SetNextTrackTitle";

    public static final String BROADCAST_KEY_PLAYER_PROGRESS = "PlayerProgress";
    public static final String BROADCAST_KEY_MAX_PLAYER_PROGRESS = "MaxPlayerProgress";
    public static final String BROADCAST_KEY_PLAYER_TIME_PROGRESS = "PlayerTimeProgress";
    public static final String BROADCAST_KEY_TITLE = "Title";
    public static final String BROADCAST_KEY_VISIBLE = "Visible";

    public BroadcastReceiver broadcastReceiverUpdateProgress;
    public BroadcastReceiver broadcastReceiverSetCurrentTrackTitle;
    public BroadcastReceiver broadcastReceiverSetNextTrackTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        OnPlayerButtonClickListener onPlayerButtonClickListener = new OnPlayerButtonClickListener();
        findViewById(R.id.main_activity_play).setOnClickListener(onPlayerButtonClickListener);
        findViewById(R.id.main_activity_pause).setOnClickListener(onPlayerButtonClickListener);
        findViewById(R.id.main_activity_stop).setOnClickListener(onPlayerButtonClickListener);

        final ProgressBar playerProgressBar = (ProgressBar) findViewById(R.id.main_activity_progress_play);
        final TextView timeProgress = (TextView) findViewById(R.id.main_activity_time);
        final TextView currentTrackTitle = (TextView) findViewById(R.id.main_activity_title);
        final TextView nextTrackTitle = (TextView) findViewById(R.id.main_activity_next);

        broadcastReceiverUpdateProgress = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                playerProgressBar.setMax(intent.getIntExtra(BROADCAST_KEY_MAX_PLAYER_PROGRESS, 100));
                playerProgressBar.setProgress(intent.getIntExtra(BROADCAST_KEY_PLAYER_PROGRESS, 0));
                timeProgress.setText(intent.getStringExtra(BROADCAST_KEY_PLAYER_TIME_PROGRESS));
            }
        };

        broadcastReceiverSetCurrentTrackTitle = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(getClass().getName(), "intent.getStringExtra(BROADCAST_KEY_TITLE): " + intent.getStringExtra(BROADCAST_KEY_TITLE));
                currentTrackTitle.setText(intent.getStringExtra(BROADCAST_KEY_TITLE));
            }
        };

        broadcastReceiverSetNextTrackTitle = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                nextTrackTitle.setText(intent.getStringExtra(BROADCAST_KEY_TITLE));
                if(intent.getBooleanExtra(BROADCAST_KEY_VISIBLE, true)){
                    nextTrackTitle.setVisibility(View.VISIBLE);
                } else {
                    nextTrackTitle.setVisibility(View.GONE);
                }
            }
        };

        FragmentTabHost mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec(SearchFragment.TAG).setIndicator("Search"),
                SearchFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec(SaverFragment.TAG).setIndicator("Save"),
                SaverFragment.class, null);

        setTabsBackground(mTabHost);
    }

    private void setTabsBackground(TabHost tabhost) {
        TabWidget widget = tabhost.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);
            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView)v.findViewById(android.R.id.title);
            if(tv == null) {
                continue;
            }
            tv.setTextColor(getResources().getColor(R.color.main_toolbar));
            v.setBackgroundResource(R.drawable.main_activity_tabs);
        }
    }

    @Override
    protected void onResume() {
        Log.d(getClass().getName(), "onResume");
        super.onResume();
        registerReceiver(broadcastReceiverUpdateProgress, new IntentFilter(BROADCAST_UPDATE_PROGRESS));
        registerReceiver(broadcastReceiverSetCurrentTrackTitle, new IntentFilter(BROADCAST_SET_CURRENT_TRACK_TITLE));
        registerReceiver(broadcastReceiverSetNextTrackTitle, new IntentFilter(BROADCAST_SET_NEXT_TRACK_TITLE));
    }

    @Override
    protected void onPause() {
        Log.d(getClass().getName(), "onPause");
        super.onPause();
        unregisterReceiver(broadcastReceiverUpdateProgress);
        unregisterReceiver(broadcastReceiverSetCurrentTrackTitle);
        unregisterReceiver(broadcastReceiverSetNextTrackTitle);
    }

    @Override
    protected void onDestroy() {
        Log.d(getClass().getName(), "onDestroy");
        super.onDestroy();
        destroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(getClass().getName(), "onSaveInstanceState");
        isRotateScreenEvent = true;
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onRestoreInstanceState");
        isRotateScreenEvent = false;
        super.onRestoreInstanceState(savedInstanceState);
    }
}
