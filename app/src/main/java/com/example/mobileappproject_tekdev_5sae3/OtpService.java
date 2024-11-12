package com.example.mobileappproject_tekdev_5sae3;

import android.content.Context;

import com.example.mobileappproject_tekdev_5sae3.Dao.UserDao;
import com.example.mobileappproject_tekdev_5sae3.Entity.User;
import com.example.mobileappproject_tekdev_5sae3.database.AppDatabase;
import com.example.mobileappproject_tekdev_5sae3.database.ColocoDataBase;

import java.util.Random;

public class OtpService {

    // Generate a 6-digit OTP
    public static String generateOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    // Generate and send OTP to user email
    public static void sendOtp(Context context, String email) {
        String otp = generateOtp();
        String subject = "Your OTP for Password Reset";
        String body = "Your OTP is: " + otp + ". It will expire in 15 minutes.";

        AppDatabase db = AppDatabase.getDatabase(context);
        User user = db.userDao().getUserByEmail(email);

        if (user != null) {
            user.setOtp(otp);
            user.setOtpTimestamp(System.currentTimeMillis());
            db.userDao().updateUser(user);

            // Send OTP via email
            EmailUtils.sendEmail(email, subject, body);
        }
    }
}