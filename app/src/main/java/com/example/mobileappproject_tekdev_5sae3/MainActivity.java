package com.example.mobileappproject_tekdev_5sae3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.mobileappproject_tekdev_5sae3.Controller.Blank1Fragment;
import com.example.mobileappproject_tekdev_5sae3.Controller.Blank2Fragment;
import com.example.mobileappproject_tekdev_5sae3.Controller.BlogFragment;
import com.example.mobileappproject_tekdev_5sae3.Controller.HomeFragment;
import com.example.mobileappproject_tekdev_5sae3.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.blog) {
                replaceFragment(new BlogFragment());
            } else if (item.getItemId() == R.id.sub) {
                replaceFragment(new Blank1Fragment());
            } else if (item.getItemId() == R.id.lib) {
                replaceFragment(new Blank2Fragment());
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
