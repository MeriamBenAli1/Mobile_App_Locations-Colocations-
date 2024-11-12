package com.example.mobileappproject_tekdev_5sae3.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobileappproject_tekdev_5sae3.EmailUtils;
import com.example.mobileappproject_tekdev_5sae3.Entity.User;
import com.example.mobileappproject_tekdev_5sae3.R;
import com.example.mobileappproject_tekdev_5sae3.database.AppDatabase;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnResetPassword;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);

        // Edge-to-edge display setup
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up button click listener for password reset
        btnResetPassword.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError("Please enter your email");
                return;
            }

            // Call the method to send OTP on a background thread
            sendOtpInBackground(email);
        });
    }

    // Method to generate a random 6-digit OTP
    public static String generateOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    // Method to send OTP in a background thread
    private void sendOtpInBackground(String email) {
        executor.execute(() -> {
            String otp = generateOtp();
            String subject = "Your OTP for Password Reset";
            String body = "Your OTP is: " + otp + ". It will expire in 15 minutes.";

            AppDatabase db = AppDatabase.getDatabase(this);
            User user = db.userDao().getUserByEmail(email);

            if (user != null) {
                user.setOtp(otp);
                user.setOtpTimestamp(System.currentTimeMillis());
                db.userDao().updateUser(user);

                // Send OTP via email
                EmailUtils.sendEmail(email, subject, body);

                // Notify the UI on the main thread after sending the OTP
                runOnUiThread(() -> {
                    Toast.makeText(ForgotPasswordActivity.this, "OTP sent to " + email, Toast.LENGTH_SHORT).show();

                    // Redirect to OTP verification screen
                    Intent intent = new Intent(ForgotPasswordActivity.this, OtpVerificationActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                });
            } else {
                // Notify the UI on the main thread if the user is not found
                runOnUiThread(() -> Toast.makeText(ForgotPasswordActivity.this, "Email not registered", Toast.LENGTH_SHORT).show());
            }
        });
    }
}