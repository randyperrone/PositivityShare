package com.randyperrone.randyperrone.positivityshare.Controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.randyperrone.randyperrone.positivityshare.Model.Message;
import com.randyperrone.randyperrone.positivityshare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_ABOUT;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_AGE;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_MESSAGES;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_PROFILEPICURL;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_USERNAME;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_USERS;

public class ViewMessageActivity extends AppCompatActivity {
    private final String TAG = "ViewMessageActivity";
    private TextView usernameTV, ageTV, aboutTV, dateTV, messageTV;
    private ImageView userImage;
    private String dateStr, messageStr, userKeyStr, dbKeyStr, uploadId;
    private String usernameStr, ageStr, aboutStr, picUrlStr;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);

        Message message = (Message)getIntent().getParcelableExtra("message");
        try{
            uploadId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
        if(uploadId == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        userImage = (ImageView)findViewById(R.id.browse_user_image);
        usernameTV = (TextView)findViewById(R.id.browse_username_tv);
        ageTV = (TextView)findViewById(R.id.browse_age_tv);
        aboutTV = (TextView)findViewById(R.id.browse_about_tv);
        dateTV = (TextView)findViewById(R.id.display_date_tv);
        messageTV = (TextView)findViewById(R.id.display_message_tv);

        if(message != null){
            dateStr = message.getDate();
            messageStr = message.getMessage();
            userKeyStr = message.getUserKey();
            dbKeyStr = message.getDbKey();
        }
        if(dateStr != null){
            dateTV.setText(dateStr);
        }
        if(messageStr != null){
            messageTV.setText(messageStr);
        }
        if(userKeyStr != null){
            getUserData();
        }
        if(uploadId != null && dbKeyStr != null){
            try{
                FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_MESSAGES).child(uploadId).child(dbKeyStr).setValue(null);
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }
        }
    }

    private void getUserData(){
        try{
            mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USERS).child(userKeyStr);
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                    if(data != null){
                        if(data.get(DATABASE_PATH_UPLOADS_USERNAME) != null){
                            usernameStr = data.get(DATABASE_PATH_UPLOADS_USERNAME).toString();
                            usernameTV.setText(usernameStr);
                        }
                        if(data.get(DATABASE_PATH_UPLOADS_AGE) != null){
                            ageStr = data.get(DATABASE_PATH_UPLOADS_AGE).toString();
                            ageTV.setText(ageStr);
                        }
                        if(data.get(DATABASE_PATH_UPLOADS_ABOUT) != null){
                            aboutStr = data.get(DATABASE_PATH_UPLOADS_ABOUT).toString();
                            aboutTV.setText(aboutStr);
                        }
                        if (data.get(DATABASE_PATH_UPLOADS_PROFILEPICURL) != null) {
                            picUrlStr = data.get(DATABASE_PATH_UPLOADS_PROFILEPICURL).toString();
                            Glide.with(getApplicationContext())
                                    .load(picUrlStr)
                                    .apply(new RequestOptions()
                                            .placeholder(R.mipmap.positivity_share_icon)
                                            .fitCenter()
                                            .centerCrop())
                                    .into(userImage);
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.toString());
                }
            });
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }

    }
}
