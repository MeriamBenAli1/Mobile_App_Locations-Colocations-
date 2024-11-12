package com.example.mobileappproject_tekdev_5sae3.Controller;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobileappproject_tekdev_5sae3.Entity.User;
import com.example.mobileappproject_tekdev_5sae3.R;
import com.example.mobileappproject_tekdev_5sae3.database.AppDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class OtpVerificationActivity extends AppCompatActivity {

    private EditText etOtp, etNewPassword, etConfirmPassword;
    private Button btnVerifyAndReset;
    private String email;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable Edge-to-Edge display
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otp_verification);

        // Set padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        etOtp = findViewById(R.id.etOtp);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnVerifyAndReset = findViewById(R.id.btnVerifyAndReset);

        // Retrieve the email passed from the previous activity
        email = getIntent().getStringExtra("email");

        // Set up button click listener for OTP verification and password reset
        btnVerifyAndReset.setOnClickListener(v -> {
            String otpInput = etOtp.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Check if fields are not empty and passwords match
            if (TextUtils.isEmpty(otpInput)) {
                etOtp.setError("Please enter OTP");
                return;
            }
            if (TextUtils.isEmpty(newPassword)) {
                etNewPassword.setError("Please enter a new password");
                return;
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                etConfirmPassword.setError("Please confirm your new password");
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verify OTP and reset password in the background
            verifyOtpAndResetPassword(otpInput, newPassword);
        });
    }

    private void verifyOtpAndResetPassword(String otpInput, String newPassword) {
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(this);
            User user = db.userDao().getUserByEmail(email);

            if (user != null && user.getOtp() != null) {
                long currentTime = System.currentTimeMillis();
                long fifteenMinutesInMillis = 15 * 60 * 1000;

                if (user.getOtp().equals(otpInput) && (currentTime - user.getOtpTimestamp() <= fifteenMinutesInMillis)) {
                    // OTP is valid, clear it and update the password
                    user.setOtp(null);
                    user.setOtpTimestamp(0);
                    user.setPassword(newPassword); // You may want to hash this password in a real application
                    db.userDao().updateUser(user);

                    // Notify success on the main thread
                    runOnUiThread(() -> {
                        Toast.makeText(OtpVerificationActivity.this, "Password reset successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close this activity and return to login
                    });
                } else {
                    // OTP is invalid or expired
                    runOnUiThread(() -> Toast.makeText(OtpVerificationActivity.this, "Invalid or expired OTP", Toast.LENGTH_SHORT).show());
                }
            } else {
                // User not found or OTP not set
                runOnUiThread(() -> Toast.makeText(OtpVerificationActivity.this, "Email not registered", Toast.LENGTH_SHORT).show());
            }
        });
    }
}