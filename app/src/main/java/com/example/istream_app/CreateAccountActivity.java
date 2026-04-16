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
 * This screen allows a new user to create an account.
 */
public class CreateAccountActivity extends AppCompatActivity {

    private EditText editFullName;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private Button btnCreateAccount;

    private LocalVaultDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.createAccountRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = LocalVaultDatabase.getInstance(this);

        editFullName = findViewById(R.id.editFullName);
        editUsername = findViewById(R.id.editCreateUsername);
        editPassword = findViewById(R.id.editCreatePassword);
        editConfirmPassword = findViewById(R.id.editConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        btnCreateAccount.setOnClickListener(v -> createAccount());
    }

    /*
     * Validates all fields before inserting a new user into Room database.
     */
    private void createAccount() {
        String fullName = editFullName.getText().toString().trim();
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            editFullName.setError("Enter full name");
            editFullName.requestFocus();
            return;
        }

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

        if (TextUtils.isEmpty(confirmPassword)) {
            editConfirmPassword.setError("Confirm password");
            editConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            editConfirmPassword.setError("Passwords do not match");
            editConfirmPassword.requestFocus();
            return;
        }

        if (database.appUserDao().getUserByUsername(username) != null) {
            editUsername.setError("Username already exists");
            editUsername.requestFocus();
            return;
        }

        AppUser newUser = new AppUser(fullName, username, password);
        database.appUserDao().insertUser(newUser);

        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
}