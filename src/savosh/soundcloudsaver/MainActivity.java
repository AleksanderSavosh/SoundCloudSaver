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


public class MainActivity extends FragmentActivity {

    public static final String BROADCAST_NAME = "savosh.soundcloudsaver.MainActivity";
    private BroadcastReceiver broadcastReceiver;

    public static final String BROADCAST_KEY_PLAYER_PROGRESS = "PlayerProgress";
    public static final String BROADCAST_KEY_MAX_PLAYER_PROGRESS = "MaxPlayerProgress";
    public static final String BROADCAST_KEY_PLAYER_TIME_PROGRESS = "PlayerTimeProgress";
    public static final String BROADCAST_KEY_TITLE = "Title";
    public static final String BROADCAST_KEY_TITLE_NEXT = "TitleNext";

    private FragmentTabHost fragmentTabHost;
    public FragmentTabHost getFragmentTabHost() {
        return fragmentTabHost;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((ApplicationContext) getApplicationContext()).setMainActivity(this);
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

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if(intent.hasExtra(BROADCAST_KEY_MAX_PLAYER_PROGRESS)){
                    playerProgressBar.setMax(intent.getIntExtra(BROADCAST_KEY_MAX_PLAYER_PROGRESS, 100));
                }
                if(intent.hasExtra(BROADCAST_KEY_PLAYER_PROGRESS)){
                    playerProgressBar.setProgress(intent.getIntExtra(BROADCAST_KEY_PLAYER_PROGRESS, 0));
                }
                if(intent.hasExtra(BROADCAST_KEY_PLAYER_TIME_PROGRESS)){
                    timeProgress.setText(intent.getStringExtra(BROADCAST_KEY_PLAYER_TIME_PROGRESS));
                }
                if(intent.hasExtra(BROADCAST_KEY_TITLE)) {
                    currentTrackTitle.setText(intent.getStringExtra(BROADCAST_KEY_TITLE));
                    nextTrackTitle.setVisibility(View.GONE);
                }

                if(intent.hasExtra(BROADCAST_KEY_TITLE_NEXT)) {
                    nextTrackTitle.setText(intent.getStringExtra(BROADCAST_KEY_TITLE_NEXT));
                    nextTrackTitle.setVisibility(View.VISIBLE);
                }
            }
        };

        fragmentTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(SearchFragment.TAG).setIndicator("Search"),
                SearchFragment.class, null);
        fragmentTabHost.addTab(fragmentTabHost.newTabSpec(SaverFragment.TAG).setIndicator("Save"),
                SaverFragment.class, null);

        setTabsBackground(fragmentTabHost);
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
        registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_NAME));
    }

    @Override
    protected void onPause() {
        Log.d(getClass().getName(), "onPause");
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        ((ApplicationContext) getApplicationContext()).removeLinkToMainActivity();
        Log.d(getClass().getName(), "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(getClass().getName(), "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(getClass().getName(), "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }
}
