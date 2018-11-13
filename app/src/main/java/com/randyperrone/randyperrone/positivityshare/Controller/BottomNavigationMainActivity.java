package com.randyperrone.randyperrone.positivityshare.Controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.randyperrone.randyperrone.positivityshare.Controller.Fragments.BrowseUsersFragment;
import com.randyperrone.randyperrone.positivityshare.Controller.Fragments.MessagesFragment;
import com.randyperrone.randyperrone.positivityshare.Controller.Fragments.ProfilePreviewFragment;
import com.randyperrone.randyperrone.positivityshare.R;

public class BottomNavigationMainActivity extends AppCompatActivity {
    private Fragment fragment = null;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = BrowseUsersFragment.newInstance();
                    break;
                case R.id.navigation_dashboard:
                    fragment = ProfilePreviewFragment.newInstance();
                    break;
                case R.id.navigation_notifications:
                    fragment = MessagesFragment.newInstance();
                    break;
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout_bottom_nav, fragment);
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragment = BrowseUsersFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout_bottom_nav, fragment);
        transaction.commit();
    }



}
