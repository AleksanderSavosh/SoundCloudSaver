package savosh.soundcloudsaver;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mTabHost = (FragmentTabHost) findViewById(R.id.main_activity_tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.main_activity_frame_layout);

        mTabHost.addTab(mTabHost.newTabSpec("Search").setIndicator("Search", null), SearchFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Save").setIndicator("Save", null), SaverFragment.class, null);

//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.main_activity_frame_layout, new SearchFragment(), SearchFragment.TAG_NAME);
//        fragmentTransaction.add(R.id.main_activity_frame_layout, new SaverFragment(), SaverFragment.TAG_NAME);
//        fragmentTransaction.commit();

    }
}
