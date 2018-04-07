package com.app.aydemir.mustwatch;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private PopularFragment popularFragment;
    private SearchFragment searchFragment;
    private SettingsFragment settingsFragment;
    private ListToWatchFragment listToWatchFragment;
    private ListWatchedFragment listWatchedFragment;
    boolean doubleBackToExitPressedOnce = false;
    Toolbar main_toolbar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/a.ttf");
        TextView toolbarTextView = findViewById(R.id.toolbarTextView);
        toolbarTextView.setTextColor(Color.WHITE);
        toolbarTextView.setText("MustWatch");
        toolbarTextView.setTypeface(face);
        toolbarTextView.setVisibility(View.VISIBLE);

        main_toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(main_toolbar);
        getSupportActionBar().setTitle("");
        main_toolbar.setTitleTextColor(Color.parseColor("#fafafa"));
        searchFragment = new SearchFragment();
        popularFragment = new PopularFragment();
        settingsFragment = new SettingsFragment();
        listToWatchFragment = new ListToWatchFragment();
        listWatchedFragment = new ListWatchedFragment();

        setFragment(listToWatchFragment);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_to_watch:
                    setFragment(listToWatchFragment);
                    return true;
                case R.id.navigation_watched:
                    setFragment(listWatchedFragment);
                    return true;
                case R.id.navigation_search:
                    setFragment(searchFragment);
                    return true;
                case R.id.navigation_popular:
                    setFragment(new PopularFragment());
                    return true;
                case R.id.navigation_settings:
                    setFragment(settingsFragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }
}
