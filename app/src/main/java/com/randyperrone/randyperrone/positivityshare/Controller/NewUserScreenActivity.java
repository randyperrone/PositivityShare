package com.randyperrone.randyperrone.positivityshare.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.randyperrone.randyperrone.positivityshare.R;

public class NewUserScreenActivity extends AppCompatActivity {
    private Button setupProfileButton, takeTourButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_screen);

        setupProfileButton = (Button)findViewById(R.id.edit_profile_button);
        takeTourButton = (Button)findViewById(R.id.take_tour_button);

        setupProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        takeTourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OnBoardingActivity.class);
                startActivity(intent);
            }
        });
    }
}
