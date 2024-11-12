package com.example.mobileappproject_tekdev_5sae3.Controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileappproject_tekdev_5sae3.Dao.UserDao;
import com.example.mobileappproject_tekdev_5sae3.MainActivity;
import com.example.mobileappproject_tekdev_5sae3.R;
import com.example.mobileappproject_tekdev_5sae3.database.AppDatabase;
import com.example.mobileappproject_tekdev_5sae3.database.ColocoDataBase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    ColocoDataBase dbHelper;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvCreateAccount, tvForgotPassword;
    UserDao userDao;
    private ExecutorService executorService;
    //SharedPreferences
    public static final String LOGIN="etEmail";
    public static final String PWD="etPassword";
    private static final String SHARED_PREF_NAME="mypref";
    private SharedPreferences sharedPref;
    //SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new ColocoDataBase(this);
        //SharedPreferences
         sharedPref = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPref.edit();


        //SharedPreferences


        // Initialize views
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvCreateAccount = findViewById(R.id.tv_create_account);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

        Intent intent = new Intent(this,MainActivity.class);
        // Initialize the ExecutorService for background tasks
        executorService = Executors.newSingleThreadExecutor();

//        //SharedPreferences
//        btnLogin.setOnClickListener(e->{
//                    editor.putString(LOGIN,etEmail.getText().toString());
//                    editor.putString(PWD,etPassword.getText().toString());
//                    editor.apply();
//                    Toast.makeText(this,"data saved",Toast
//                            .LENGTH_SHORT).show();
//
//                    startActivity(intent);
//                }
//        );
//
//        //SharedPreferences
        // Get the AppDatabase instance
        AppDatabase db = AppDatabase.getDatabase(this);
        userDao = db.userDao();  // Get the UserDao

        // Set button click listener for creating a new account
        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        // Set the login button click listener
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Please enter your email");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    etPassword.setError("Please enter your password");
                    return;
                }

                // Perform login validation in the background thread
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Check if the user exists in the database
                        int userCount = userDao.isValidUser(email, password);

                        if (userCount > 0) {
                            userDao.logInUser(email);
                            String name = userDao.getUserNameByEmail(email);
                            // Once login is successful, switch back to the main thread to handle UI
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Save user info in SharedPreferences
                                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("name", name);
                                    editor.putString("email", email);
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.apply();

                                    // Redirect to MainActivity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // Close LoginActivity
                                }
                            });
                        } else {
                            // If the user is not found, show a toast message on the main thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });

        // Handle "Forgot your password?" click
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ForgotPasswordActivity
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
