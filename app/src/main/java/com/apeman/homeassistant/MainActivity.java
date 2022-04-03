package com.apeman.homeassistant;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;

import com.apeman.homeassistant.fragments.ChartFragment;
import com.apeman.homeassistant.fragments.MainFragment;
import com.apeman.homeassistant.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    final Fragment mainFragment = new MainFragment();
    final Fragment chartFragment = new ChartFragment();
    final Fragment settingsFragment = new SettingsFragment();
    Fragment active = mainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the bottom navigation view
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        // Disable navigation tooltips
        disableNavigationTooltips();

        // Set default fragment as MainFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, active)
                .commit();
    }


    /**
     * Fragment selector - it allows user to switch
     * between fragments available on bottom navigation view.
     */
    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
        // Fragment selector
        if (item.getItemId() == R.id.homenav) {
            active = mainFragment;
        } else if (item.getItemId() == R.id.wykresy) {
            active = chartFragment;
        } else if (item.getItemId() == R.id.ustawienia) {
            active = settingsFragment;
        }

        // Switch fragment to selected above
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in, // enter
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_in
                )
                .replace(R.id.fragment_container, active)
                .commit();
        return true;
    };

    /**
     * Disables menu tooltips, which were displayed
     * on long menuItem click.
     */
    public void disableNavigationTooltips() {
        Menu menu = bottomNavigationView.getMenu();
        int menuSize = menu.size();

        for (int i = 0; i < menuSize; i++) {
            MenuItem menuItem = menu.getItem(i);
            TooltipCompat.setTooltipText(findViewById(menuItem.getItemId()), null);
        }
    }
}