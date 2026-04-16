package com.example.istream_app;

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

/*
 * I am creating a screen that allows a new user to sign up.
 * This activity collects user details and stores them in the Room database.
 */
public class CreateAccountActivity extends AppCompatActivity {

    // I am declaring UI components for user input
    private EditText editFullName;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private Button btnCreateAccount;

    // I am creating a reference to my Room database
    private LocalVaultDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        // I am handling system bar padding for proper UI layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.createAccountRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // I am initializing the database instance
        database = LocalVaultDatabase.getInstance(this);

        // I am linking XML views with Java variables
        editFullName = findViewById(R.id.editFullName);
        editUsername = findViewById(R.id.editCreateUsername);
        editPassword = findViewById(R.id.editCreatePassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        // I am setting a click listener to start account creation
        btnCreateAccount.setOnClickListener(v -> createAccount());
    }

    /*
     * I am validating all input fields before creating a new account.
     * If everything is valid, I store the user in the database.
     */
    private void createAccount() {

        // I am reading user input from UI fields
        String fullName = editFullName.getText().toString().trim();
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        // I am checking if full name is empty
        if (TextUtils.isEmpty(fullName)) {
            editFullName.setError("Enter full name");
            editFullName.requestFocus();
            return;
        }

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

        // I am checking if confirm password is empty
        if (TextUtils.isEmpty(confirmPassword)) {
            editConfirmPassword.setError("Confirm password");
            editConfirmPassword.requestFocus();
            return;
        }

        // I am ensuring both passwords match
        if (!password.equals(confirmPassword)) {
            editConfirmPassword.setError("Passwords do not match");
            editConfirmPassword.requestFocus();
            return;
        }

        // I am checking if username already exists in database
        if (database.appUserDao().getUserByUsername(username) != null) {
            editUsername.setError("Username already exists");
            editUsername.requestFocus();
            return;
        }

        // I am creating a new user object with validated data
        AppUser newUser = new AppUser(fullName, username, password);

        // I am inserting the new user into the Room database
        database.appUserDao().insertUser(newUser);

        // I am showing success message after account creation
        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();

        // I am closing this screen and returning to previous screen (login)
        finish();
    }
}