package com.randyperrone.randyperrone.positivityshare.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.randyperrone.randyperrone.positivityshare.R;


public class GetMessagesViewHolder extends RecyclerView.ViewHolder {
    private TextView dateTV;
    private TextView messageTV;

    public GetMessagesViewHolder(View itemView) {
        super(itemView);
        dateTV = (TextView)itemView.findViewById(R.id.message_date_tv);
        messageTV = (TextView)itemView.findViewById(R.id.message_actual_tv);
    }

    public void setDate(String date){
        if(date != null){
            dateTV.setText(date);
        }
    }

    public void setMessage(String message){
        if(message != null){
            messageTV.setText(message);
        }
    }
}
