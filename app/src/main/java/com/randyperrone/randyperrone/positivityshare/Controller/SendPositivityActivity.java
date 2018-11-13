package com.randyperrone.randyperrone.positivityshare.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.randyperrone.randyperrone.positivityshare.Controller.Fragments.ChoosePositivityToSendFragment;
import com.randyperrone.randyperrone.positivityshare.Model.User;
import com.randyperrone.randyperrone.positivityshare.R;

public class SendPositivityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_positivity);

        User user = (User)getIntent().getParcelableExtra("user");
        ChoosePositivityToSendFragment fragment = new ChoosePositivityToSendFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}
