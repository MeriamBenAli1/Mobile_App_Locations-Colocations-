package com.example.mobileappproject_tekdev_5sae3;

import android.content.Context;
import com.example.mobileappproject_tekdev_5sae3.Entity.User;
import com.example.mobileappproject_tekdev_5sae3.database.AppDatabase;

public class OtpVerificationService {

    private static final long OTP_EXPIRATION_TIME = 15 * 60 * 1000; // 15 minutes in milliseconds

    public static boolean verifyOtp(Context context, String email, String otpInput) {
        // Get an instance of UserDao
        AppDatabase db = AppDatabase.getDatabase(context);
        User user = db.userDao().getUserByEmail(email);

        if (user != null && user.getOtp() != null) {
            long currentTime = System.currentTimeMillis();

            // Check if OTP matches and is within expiration time
            if (user.getOtp().equals(otpInput) && (currentTime - user.getOtpTimestamp() <= OTP_EXPIRATION_TIME)) {
                // Clear OTP after successful verification
                user.setOtp(null);
                user.setOtpTimestamp(0);
                db.userDao().updateUser(user);
                return true;
            }
        }
        return false;
    }
}