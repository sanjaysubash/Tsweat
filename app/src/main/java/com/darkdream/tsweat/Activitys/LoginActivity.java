package com.darkdream.tsweat.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.darkdream.tsweat.R;
import com.darkdream.tsweat.Services.UtilClass;

public class LoginActivity extends AppCompatActivity {
    public LoginActivity ACTIVITY;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ACTIVITY=this;

        // Find views by their ID
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        // Set an onClick listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the username and password input
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Basic validation
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(ACTIVITY, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Here you can add your login logic
                    if (username.equals("admin") && password.equals("admin123")) {
                        Toast.makeText(ACTIVITY, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        UtilClass.savePreference(ACTIVITY,"username",username);
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}