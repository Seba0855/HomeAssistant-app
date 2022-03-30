package com.apeman.homeassistant;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apeman.homeassistant.blynk.BlynkClient;
import com.apeman.homeassistant.blynk.BlynkData;
import com.apeman.homeassistant.fragments.ChartFragment;
import com.apeman.homeassistant.fragments.MainFragment;
import com.apeman.homeassistant.fragments.RecyclerGridAdapter;
import com.apeman.homeassistant.fragments.SettingsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new MainFragment())
                .commit();

    }

    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
        // Cannot use switch statement as resource identifiers are not final

        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.homenav) {
            selectedFragment = new MainFragment();
        } else if (item.getItemId() == R.id.wykresy) {
            selectedFragment = new ChartFragment();
        } else if (item.getItemId() == R.id.ustawienia) {
            selectedFragment = new SettingsFragment();
        }


        assert selectedFragment != null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();
        return true;
    };
}