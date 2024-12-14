package com.darkdream.tsweat;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.darkdream.tsweat.Activitys.DashboardActivity;
import com.darkdream.tsweat.Activitys.LoginActivity;
import com.darkdream.tsweat.Services.UtilClass;

public class MainActivity extends AppCompatActivity {
public MainActivity ACTIVITY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACTIVITY = this;
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String username= UtilClass.getPreference(ACTIVITY,"username");
                if (username != null){
                    Intent intent = new Intent(ACTIVITY, DashboardActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(ACTIVITY, LoginActivity.class);
                    startActivity(intent);
                }

            }
        },4000);
    }
}
