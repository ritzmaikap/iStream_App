package com.example.istream_app;

import android.content.Intent;
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

import com.example.istream_app.data.AppUser;
import com.example.istream_app.data.LocalVaultDatabase;
import com.example.istream_app.utils.AuthKeeper;

/*
 * I am creating the MainActivity which acts as the Login Screen.
 * This is the first screen that appears when the app starts.
 */
public class MainActivity extends AppCompatActivity {

    // I am declaring UI components for login
    private EditText editUsername;
    private EditText editPassword;
    private Button btnLogin;
    private Button btnSignUp;

    // I am creating a reference to my Room database
    private LocalVaultDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // I am enabling edge-to-edge layout for modern UI
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // I am handling padding for system bars (status bar and navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // I am initializing the database
        database = LocalVaultDatabase.getInstance(this);

        // I am checking if the user is already logged in
        // If yes, I directly open the dashboard screen
        if (AuthKeeper.isLoggedIn(this)) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
            return;
        }

        // I am linking UI elements
        editUsername = findViewById(R.id.editLoginUsername);
        editPassword = findViewById(R.id.editLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnGoToCreateAccount);

        // I am setting login button action
        btnLogin.setOnClickListener(v -> loginUser());

        // I am navigating to Create Account screen when Sign Up is clicked
        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }

    /*
     * I am validating login inputs and checking credentials from database.
     */
    private void loginUser() {

        // I am reading input values
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // I am checking if username is empty
        if (TextUtils.isEmpty(username)) {
            editUsername.setError("Enter username");
            editUsername.requestFocus();
            return;
        }

        // I am checking if password is empty
        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Enter password");
            editPassword.requestFocus();
            return;
        }

        // I am verifying user credentials from Room database
        AppUser user = database.appUserDao().loginUser(username, password);

        if (user != null) {
            // I am saving logged-in user session using SharedPreferences
            AuthKeeper.saveLoggedInUser(this, user.userId);

            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

            // I am navigating to Dashboard screen after successful login
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            // I am showing error if credentials are incorrect
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}