package com.randyperrone.randyperrone.positivityshare.Controller.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randyperrone.randyperrone.positivityshare.Controller.LoginActivity;
import com.randyperrone.randyperrone.positivityshare.Model.Message;
import com.randyperrone.randyperrone.positivityshare.R;
import com.randyperrone.randyperrone.positivityshare.RecyclerView.GetMessagesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_MESSAGES;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessagesFragment extends Fragment {
    private final String TAG = "MessagesFragment";
    private static final int TOTAL_ITEM_EACH_LOAD = 10;
    private View layoutView;
    private RecyclerView recyclerView;
    private GetMessagesAdapter mAdapter;
    private GridLayoutManager gridLayoutManager;
    private String uploadId, lastNode, lastKey;
    private int totalItemCount, lastVisibleItem;
    private boolean isLoading, isLastData;


    public MessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MessagesFragment.
     */
    public static MessagesFragment newInstance() {
        MessagesFragment fragment = new MessagesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_messages, container, false);

        lastNode = "";
        lastKey = "";
        isLoading = isLastData = false;
        try{
            uploadId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
        if(uploadId == null){
            Intent intent = new Intent(layoutView.getContext(), LoginActivity.class);
            layoutView.getContext().startActivity(intent);
        }
        recyclerView = (RecyclerView)layoutView.findViewById(R.id.messages_recyclerview);
        mAdapter = new GetMessagesAdapter();
        recyclerView.setAdapter(mAdapter);
        int gridColumnCount = getResources().getInteger(R.integer.grid_column_count);
        gridLayoutManager = new GridLayoutManager(getActivity(), gridColumnCount);
        recyclerView.setLayoutManager(gridLayoutManager);

        getLastKeyFromFirebase();
        loadTheData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = gridLayoutManager.getItemCount();
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + TOTAL_ITEM_EACH_LOAD) && !lastNode.equals("end")) {
                    loadTheData();
                    isLoading = true;
                }
            }
        });

        return layoutView;
    }

    private void loadTheData(){
        if(!isLastData){
            try{
                Query aQuery;
                if(TextUtils.isEmpty(lastNode)){
                    aQuery = FirebaseDatabase.getInstance().getReference()
                            .child(DATABASE_PATH_UPLOADS_MESSAGES)
                            .child(uploadId)
                            .orderByKey()
                            .limitToFirst(TOTAL_ITEM_EACH_LOAD);
                }
                else{
                    aQuery = FirebaseDatabase.getInstance().getReference()
                            .child(DATABASE_PATH_UPLOADS_MESSAGES)
                            .child(uploadId)
                            .orderByKey()
                            .startAt(lastNode)
                            .limitToFirst(TOTAL_ITEM_EACH_LOAD);
                }
                aQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){
                            List<Message> messageList = new ArrayList<>();
                            if(dataSnapshot != null){
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    Message message = postSnapshot.getValue(Message.class);
                                    messageList.add(message);
                                }
                                lastNode = messageList.get(messageList.size() - 1).getDbKey();

                                if(!lastNode.equals(lastKey)){
                                    messageList.remove(messageList.size() - 1);
                                }
                                else{
                                    lastNode = "end";
                                }
                                mAdapter.addAll(messageList);
                                isLoading = false;
                            }
                        }
                        else{
                            isLoading = false;
                            isLastData = true;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        isLoading = false;
                        Log.e(TAG, databaseError.toString());
                    }
                });
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }

        }
    }

    private void getLastKeyFromFirebase(){
        try{
            Query lastKeyQuery = FirebaseDatabase.getInstance().getReference()
                    .child(DATABASE_PATH_UPLOADS_MESSAGES)
                    .child(uploadId)
                    .orderByKey()
                    .limitToLast(1);
            lastKeyQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot lastKeyinDb: dataSnapshot.getChildren()){
                        lastKey = lastKeyinDb.getKey();
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
