package com.randyperrone.randyperrone.positivityshare.RecyclerView;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randyperrone.randyperrone.positivityshare.Controller.SendPositivityActivity;
import com.randyperrone.randyperrone.positivityshare.Model.User;
import com.randyperrone.randyperrone.positivityshare.R;

import java.util.ArrayList;
import java.util.List;

public class BrowseUsersAdapter extends RecyclerView.Adapter<BrowseUsersViewHolder> {
    private List<User> userList;

    public BrowseUsersAdapter() {
        this.userList = new ArrayList<>();
    }

    @NonNull
    @Override
    public BrowseUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_browse_users, parent, false);
        return new BrowseUsersViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseUsersViewHolder holder, int position) {
        final User user = userList.get(position);
        String username = "";
        String picURL = "";
        String age = "";
        String about = "";
        if(user != null){
            username = user.getUsername();
            picURL = user.getPicUrl();
            age = user.getAge();
            about = user.getAbout();
        }
        if(username != null){
            holder.setUsername(username);
        }
        if(picURL != null && !picURL.isEmpty()){
            holder.setUserPhoto(picURL);
        }
        if(age != null && !age.isEmpty()){
            holder.setAge(age);
        }
        if(about != null && !about.isEmpty()){
            holder.setAbout(about);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SendPositivityActivity.class);
                intent.putExtra("user", user);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void addAll(List<User> newUsers) {
        int initialSize = userList.size();
        userList.addAll(newUsers);
        notifyItemRangeInserted(initialSize, newUsers.size());
    }

    public String getLastItemId() {
        return userList.get(userList.size() - 1).getUserKey();
    }
}
