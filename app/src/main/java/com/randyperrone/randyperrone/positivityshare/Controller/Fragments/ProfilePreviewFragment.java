package com.randyperrone.randyperrone.positivityshare.Controller.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseUser;
import com.randyperrone.randyperrone.positivityshare.Controller.EditProfileActivity;
import com.randyperrone.randyperrone.positivityshare.Controller.LoginActivity;
import com.randyperrone.randyperrone.positivityshare.Model.DatabaseHelper;
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
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_PROFILEPICURL;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_USERNAME;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_USERS;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfilePreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilePreviewFragment extends Fragment {
    private final String TAG = "ProfilePreviewFragment";
    private static final String DBTABLENAME = "user";
    private DatabaseHelper dbHelper;
    private DatabaseReference mDatabase;
    private View layoutView;
    private String uploadID;
    private String usernameStr, ageStr, aboutStr, picUrlStr;
    private TextView usernameTV, ageTV, aboutTV;
    private ImageView userPhotoIV;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ProfilePreviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ProfilePreviewFragment.
     */
    public static ProfilePreviewFragment newInstance() {
        ProfilePreviewFragment fragment = new ProfilePreviewFragment();
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
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        layoutView = inflater.inflate(R.layout.fragment_profile_preview, container, false);

        usernameTV = (TextView)layoutView.findViewById(R.id.browse_username_tv);
        ageTV = (TextView)layoutView.findViewById(R.id.browse_age_tv);
        aboutTV = (TextView)layoutView.findViewById(R.id.browse_about_tv);
        userPhotoIV = (ImageView) layoutView.findViewById(R.id.browse_user_image);
        dbHelper = new DatabaseHelper(getActivity().getApplicationContext(), DBTABLENAME);
        try{
            uploadID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
        if(uploadID == null){
            Intent intent = new Intent(layoutView.getContext(), LoginActivity.class);
            layoutView.getContext().startActivity(intent);
        }

        if(!fetchDataFromDatabase()){
            fetchDataFromServer();
        }
        return layoutView;
    }

    private boolean fetchDataFromDatabase(){
        boolean dataInDatabase = false;
        try{
            Cursor cursor = dbHelper.getUser(uploadID);
            if(cursor.moveToFirst()){
                dataInDatabase = true;
                usernameStr = cursor.getString(1);
                ageStr = cursor.getString(2);
                aboutStr = cursor.getString(3);
                picUrlStr = cursor.getString(4);

                addData(usernameStr, ageStr, aboutStr, picUrlStr);
            }
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }

        return dataInDatabase;
    }

    private void fetchDataFromServer(){
        try{
            mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USERS).child(uploadID);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                    if (data != null) {
                        if(data.get(DATABASE_PATH_UPLOADS_USERNAME) != null){
                            usernameStr = data.get(DATABASE_PATH_UPLOADS_USERNAME).toString();
                        }
                        if(data.get(DATABASE_PATH_UPLOADS_AGE) != null){
                            ageStr = data.get(DATABASE_PATH_UPLOADS_AGE).toString();
                        }
                        if(data.get(DATABASE_PATH_UPLOADS_ABOUT) != null){
                            aboutStr = data.get(DATABASE_PATH_UPLOADS_ABOUT).toString();
                        }
                        if (data.get(DATABASE_PATH_UPLOADS_PROFILEPICURL) != null) {
                            picUrlStr = data.get(DATABASE_PATH_UPLOADS_PROFILEPICURL).toString();
                        }
                        addData(usernameStr, ageStr, aboutStr, picUrlStr);
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

    private void addData(String username, String age, String about, String picURL){
        if(username != null){
            usernameTV.setText(username);
        }
        if(age != null){
            ageTV.setText(age);
        }
        if(about != null){
            aboutTV.setText(about);
        }
        if(picURL != null){
            try{
                Glide.with(layoutView.getContext())
                        .load(picURL)
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.positivity_share_icon)
                                .fitCenter()
                                .centerCrop())
                        .into(userPhotoIV);
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_goto_edit_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

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
