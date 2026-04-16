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
 * MainActivity = Login Screen
 * This is the entry screen of the app as requested.
 */
public class MainActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editPassword;
    private Button btnLogin;
    private Button btnSignUp;

    private LocalVaultDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enables drawing edge-to-edge for a modern full-screen look
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handles safe padding for status bar / navigation bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = LocalVaultDatabase.getInstance(this);

        // If the user is already logged in, directly open dashboard
        if (AuthKeeper.isLoggedIn(this)) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
            return;
        }

        editUsername = findViewById(R.id.editLoginUsername);
        editPassword = findViewById(R.id.editLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnGoToCreateAccount);

        btnLogin.setOnClickListener(v -> loginUser());

        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });
    }

    /*
     * Validates login fields and checks credentials in the Room database.
     */
    private void loginUser() {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            editUsername.setError("Enter username");
            editUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editPassword.setError("Enter password");
            editPassword.requestFocus();
            return;
        }

        AppUser user = database.appUserDao().loginUser(username, password);

        if (user != null) {
            AuthKeeper.saveLoggedInUser(this, user.userId);
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}