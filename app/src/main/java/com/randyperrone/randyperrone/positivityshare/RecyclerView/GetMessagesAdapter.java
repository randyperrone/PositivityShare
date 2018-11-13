package com.randyperrone.randyperrone.positivityshare.RecyclerView;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randyperrone.randyperrone.positivityshare.Controller.ViewMessageActivity;
import com.randyperrone.randyperrone.positivityshare.Model.Message;
import com.randyperrone.randyperrone.positivityshare.R;

import java.util.ArrayList;
import java.util.List;

public class GetMessagesAdapter extends RecyclerView.Adapter<GetMessagesViewHolder> {
    private List<Message> messageList;

    public GetMessagesAdapter() {
        this.messageList = new ArrayList<>();
    }

    @NonNull
    @Override
    public GetMessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_messages_list, parent, false);
        return new GetMessagesViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull GetMessagesViewHolder holder, int position) {
        final Message message = messageList.get(position);
        String date = "";
        String messageStr = "";
        boolean read = false;
        if(message != null){
            date = message.getDate();
            messageStr = message.getMessage();
        }
        if(date != null){
            holder.setDate(date);
        }
        if(messageStr != null){
            holder.setMessage(messageStr);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ViewMessageActivity.class);
                intent.putExtra("message", message);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void addAll(List<Message> newUsers) {
        int initialSize = messageList.size();
        messageList.addAll(newUsers);
        notifyItemRangeInserted(initialSize, newUsers.size());
    }

    public String getLastItemId() {
        return messageList.get(messageList.size() - 1).getDbKey();
    }
}
