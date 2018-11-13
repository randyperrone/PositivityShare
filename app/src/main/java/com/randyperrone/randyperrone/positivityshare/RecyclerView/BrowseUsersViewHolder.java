package com.randyperrone.randyperrone.positivityshare.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.randyperrone.randyperrone.positivityshare.R;

public class BrowseUsersViewHolder extends RecyclerView.ViewHolder {
    private final String TAG = "BrowseUsersViewHolder";
    private TextView username, age, about;
    private ImageView userPhoto;

    public BrowseUsersViewHolder(View itemView) {
        super(itemView);
        username = (TextView)itemView.findViewById(R.id.browse_username_tv);
        age = (TextView)itemView.findViewById(R.id.browse_age_tv);
        about = (TextView)itemView.findViewById(R.id.browse_about_tv);
        userPhoto = (ImageView) itemView.findViewById(R.id.browse_user_image);
    }

    public void setUsername(String name){
        if(name != null){
            username.setText(name);
        }
    }

    public void setAge(String age){
        if(age != null){
            this.age.setText(age);
        }
    }

    public void setAbout(String about){
        if(about != null){
            this.about.setText(about);
        }
    }

    public void setUserPhoto(String URL){
        if(URL != null){
            try{
                Glide.with(userPhoto.getContext())
                        .load(URL)
                        .apply(new RequestOptions()
                        .centerCrop()
                        .fitCenter()
                        .placeholder(R.mipmap.positivity_share_icon))
                        .into(userPhoto);
            }
            catch (Exception e){
                Log.e(TAG, e.toString());
            }
        }
    }
}
