package com.example.kampsuyg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.kampsuyg.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean isMenuOpen = false;
    private String userRole = "student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        userRole = sharedPref.getString("userRole", "student");

        // ViewPager2 Ayarları
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);

        // TabLayout ile ViewPager2'yi birbirine bağla
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.tab_events);
                    break;
                case 1:
                    tab.setText(R.string.tab_past_events);
                    break;
                case 2:
                    tab.setText(R.string.tab_social);
                    break;
            }
        }).attach();

        binding.fab.setOnClickListener(view -> toggleFabMenu());

        binding.fabCreatePost.setOnClickListener(view -> {
            toggleFabMenu();
            startActivity(new Intent(MainActivity.this, CreatePostActivity.class));
        });

        binding.fabCreateEvent.setOnClickListener(view -> {
            toggleFabMenu();
            startActivity(new Intent(MainActivity.this, createAventActivity.class));
        });
    }

    private void toggleFabMenu() {
        if (!isMenuOpen) {
            binding.fabCreatePost.show();
            if (userRole.equals("club")) {
                binding.fabCreateEvent.show();
            }
            binding.fab.animate().rotation(45f).setDuration(200).start();
            isMenuOpen = true;
        } else {
            binding.fabCreatePost.hide();
            binding.fabCreateEvent.hide();
            binding.fab.animate().rotation(0f).setDuration(200).start();
            isMenuOpen = false;
        }
    }

    // ViewPager2 için Adapter Sınıfı
    private static class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull AppCompatActivity activity) {
            super(activity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0: return new EventsFragment();
                case 1: return new PastEventsFragment();
                case 2: return new SocialFragment();
                default: return new EventsFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
            if (sharedPref.getBoolean("isLoggedIn", false)) {
                String roleText = userRole.equals("club") ? "Kulüp Sorumlusu" : "Öğrenci";
                Snackbar.make(binding.getRoot(), "Kullanıcı: " + sharedPref.getString("username", "") + " (" + roleText + ")", Snackbar.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

