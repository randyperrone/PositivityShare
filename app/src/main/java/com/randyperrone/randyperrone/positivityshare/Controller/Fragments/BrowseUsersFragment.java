package com.randyperrone.randyperrone.positivityshare.Controller.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.randyperrone.randyperrone.positivityshare.Controller.LoginActivity;
import com.randyperrone.randyperrone.positivityshare.Controller.OnBoardingActivity;
import com.randyperrone.randyperrone.positivityshare.Model.User;
import com.randyperrone.randyperrone.positivityshare.R;
import com.randyperrone.randyperrone.positivityshare.RecyclerView.BrowseUsersAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_ABOUT;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_AGE;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_PROFILEPICURL;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_USERNAME;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_USERS;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowseUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseUsersFragment extends Fragment {
    private final String TAG = "BrowseUsersFragment";
    private static final int TOTAL_ITEM_EACH_LOAD = 10;
    private View layoutView;
    private RecyclerView recyclerView;
    private BrowseUsersAdapter mAdapter;
    private GridLayoutManager gridLayoutManager;
    private String uploadID, lastKey, lastNode;
    private int totalItemCount, lastVisibleItem;
    private boolean isLoading, isLastData;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public BrowseUsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment BrowseUsersFragment.
     */

    public static BrowseUsersFragment newInstance() {
        BrowseUsersFragment fragment = new BrowseUsersFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutView = inflater.inflate(R.layout.fragment_browse_users, container, false);
        setHasOptionsMenu(true);

        lastNode = "";  //userkey of the last node added to my list
        lastKey = "Finds the last user key in firebase";
        isLoading = isLastData = false;
        try{
            uploadID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
        if(uploadID == null){
            Intent intent = new Intent(layoutView.getContext(), LoginActivity.class);
            layoutView.getContext().startActivity(intent);
        }
        recyclerView = (RecyclerView)layoutView.findViewById(R.id.recyclerview);
        mAdapter = new BrowseUsersAdapter();
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
                            .child(DATABASE_PATH_UPLOADS_USERS)
                            .orderByKey()
                            .limitToFirst(TOTAL_ITEM_EACH_LOAD);
                }
                else{
                    aQuery = FirebaseDatabase.getInstance().getReference()
                            .child(DATABASE_PATH_UPLOADS_USERS)
                            .orderByKey()
                            .startAt(lastNode)
                            .limitToFirst(TOTAL_ITEM_EACH_LOAD);
                }
                aQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){
                            List<User> userList = new ArrayList<>();
                            if(dataSnapshot != null){
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    String userID = "";
                                    String userName = "";
                                    String about = "";
                                    String age = "";
                                    String picURL = "";
                                    if(postSnapshot.getKey() != null){
                                        userID = postSnapshot.getKey().toString();
                                        if(userID.equals(uploadID)){
                                            continue;
                                        }
                                    }
                                    if(postSnapshot.child(DATABASE_PATH_UPLOADS_PROFILEPICURL).getValue() != null){
                                        picURL = postSnapshot.child(DATABASE_PATH_UPLOADS_PROFILEPICURL).getValue().toString();
                                    }
                                    if(postSnapshot.child(DATABASE_PATH_UPLOADS_USERNAME).getValue() != null){
                                        userName = postSnapshot.child(DATABASE_PATH_UPLOADS_USERNAME).getValue().toString();
                                    }
                                    if(postSnapshot.child(DATABASE_PATH_UPLOADS_AGE).getValue() != null){
                                        age = postSnapshot.child(DATABASE_PATH_UPLOADS_AGE).getValue().toString();
                                    }
                                    if(postSnapshot.child(DATABASE_PATH_UPLOADS_ABOUT).getValue() != null){
                                        about = postSnapshot.child(DATABASE_PATH_UPLOADS_ABOUT).getValue().toString();
                                    }
                                    User user = new User(userID, picURL, userName, age, about);
                                    userList.add(user);
                                }
                                if(!userList.isEmpty()){
                                    lastNode = userList.get(userList.size() - 1).getUserKey();
                                }
                                if(!lastNode.equals(lastKey) && !userList.isEmpty()){
                                    userList.remove(userList.size() - 1);
                                }
                                else{
                                    lastNode = "end";
                                }
                                mAdapter.addAll(userList);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_browse_other_profiles, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_tour){
            Intent intent = new Intent(layoutView.getContext(), OnBoardingActivity.class);
            startActivity(intent);
        }
        if(id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getLastKeyFromFirebase(){
        try{
            Query lastKeyQuery = FirebaseDatabase.getInstance().getReference()
                    .child(DATABASE_PATH_UPLOADS_USERS)
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
