package com.example.mobileappproject_tekdev_5sae3.Controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mobileappproject_tekdev_5sae3.Entity.User;
import com.example.mobileappproject_tekdev_5sae3.Dao.UserDao;
import com.example.mobileappproject_tekdev_5sae3.database.AppDatabase;
import com.example.mobileappproject_tekdev_5sae3.R;

public class SignupActivity extends AppCompatActivity {

    EditText username, email, pass;
    Button signupBtn;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.et_username);
        email = findViewById(R.id.et_email);
        pass = findViewById(R.id.et_password);

        signupBtn = findViewById(R.id.btn_signup);

        AppDatabase db = AppDatabase.getDatabase(this);
        userDao = db.userDao();

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String mail = email.getText().toString();
                String password = pass.getText().toString();

                if (name.isEmpty() || mail.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if user already exists and insert the user with password
                    new CheckUserExistsTask().execute(mail, name, password);
                }
            }
        });
    }

    // AsyncTask to check if the user exists and then insert a new one with password if not
    private class CheckUserExistsTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String email = params[0];
            String name = params[1];
            String password = params[2];

            User existingUser = userDao.getUserByEmail(email);
            if (existingUser == null) {
                User newUser = new User(name, email, password); // Create new User object
                userDao.insertUser(newUser);  // Insert User object
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                Toast.makeText(SignupActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SignupActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
