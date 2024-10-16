package com.example.mobileappproject_tekdev_5sae3.activity_blog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileappproject_tekdev_5sae3.MainActivity;
import com.example.mobileappproject_tekdev_5sae3.R;

public class Activity_Blog  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        // Back button functionality
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(Activity_Blog.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
