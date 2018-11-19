package com.randyperrone.randyperrone.positivityshare.Controller.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.randyperrone.randyperrone.positivityshare.Controller.BottomNavigationMainActivity;
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
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_NEWUSER;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_USERNAME;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_USERS;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditDetailsFragment extends Fragment {
    private final String TAG = "EditDetailsFragment";
    private static final String DBTABLENAME = "user";
    private View layoutView;
    private EditText usernameET, ageET, aboutET;
    private String uploadID;
    private DatabaseReference mDatabase;
    private String usernameStr, ageStr, aboutStr;
    private Boolean newUserFlag = true;
    private Boolean databaseEmpty = true;
    private DatabaseHelper dbHelper;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public EditDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment EditDetailsFragment.
     */
    public static EditDetailsFragment newInstance() {
        EditDetailsFragment fragment = new EditDetailsFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_edit_details, container, false);
        setHasOptionsMenu(true);

        usernameET = (EditText)layoutView.findViewById(R.id.profile_edit_username);
        ageET = (EditText)layoutView.findViewById(R.id.profile_edit_age);
        aboutET = (EditText)layoutView.findViewById(R.id.profile_edit_aboutme);
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


        try{
            Cursor cursor = dbHelper.getUser(uploadID);
            if(cursor.moveToFirst()){
                databaseEmpty = false;
                usernameStr = cursor.getString(1);
                ageStr = cursor.getString(2);
                aboutStr = cursor.getString(3);
                if(usernameStr != null){
                    usernameET.setText(usernameStr);
                }
                if(ageStr != null){
                    ageET.setText(ageStr);
                }
                if(aboutStr != null){
                    aboutET.setText(aboutStr);
                }
            }
            else{
                mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USERS).child(uploadID);
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String,Object> data = (Map<String,Object>) dataSnapshot.getValue();
                        if(data != null){
                            if(data.get(DATABASE_PATH_UPLOADS_USERNAME) != null){
                                usernameStr = data.get(DATABASE_PATH_UPLOADS_USERNAME).toString();
                                usernameET.setText(usernameStr);
                            }
                            if(data.get(DATABASE_PATH_UPLOADS_AGE) != null){
                                ageStr = data.get(DATABASE_PATH_UPLOADS_AGE).toString();
                                ageET.setText(ageStr);
                            }
                            if(data.get(DATABASE_PATH_UPLOADS_ABOUT) != null){
                                aboutStr = data.get(DATABASE_PATH_UPLOADS_ABOUT).toString();
                                aboutET.setText(aboutStr);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, databaseError.toString());
                    }
                });
            }
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }

        return layoutView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done: {
                storeData();
                if(!newUserFlag){
                    Toast.makeText(getActivity(), "Profile Updated",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), BottomNavigationMainActivity.class);
                    startActivity(intent);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void storeData(){
        String usernameEdited = usernameET.getText().toString();
        String ageEdited = ageET.getText().toString();
        String aboutEdited = aboutET.getText().toString();

        if(ageEdited != null && !ageEdited.isEmpty()){
            int age = 0;
            try{
                age = Integer.valueOf(ageEdited);
            }catch (Exception e){
                Log.e(TAG, e.toString());
            }
            if(age < 1){
                Toast.makeText(getActivity(), "Age cannot be less than 1",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }

        try{
            if(!usernameEdited.equals(usernameStr) || !ageEdited.equals(ageStr) || !aboutEdited.equals(aboutStr)){
                if(usernameEdited != null && !usernameEdited.isEmpty()){
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USERS).child(uploadID).child(DATABASE_PATH_UPLOADS_USERNAME).setValue(usernameEdited);
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USERS).child(uploadID).child(DATABASE_PATH_UPLOADS_NEWUSER).setValue(false);
                }
                else{
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USERS).child(uploadID).child(DATABASE_PATH_UPLOADS_NEWUSER).setValue(true);
                }
                if(ageEdited != null){
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USERS).child(uploadID).child(DATABASE_PATH_UPLOADS_AGE).setValue(ageEdited);
                }
                if(aboutEdited != null){
                    FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USERS).child(uploadID).child(DATABASE_PATH_UPLOADS_ABOUT).setValue(aboutEdited);
                }
                if(databaseEmpty){
                    logResult(dbHelper.insertUserData(uploadID, usernameEdited, ageEdited, aboutEdited));
                }
                else{
                    logResult(dbHelper.updateUserData(uploadID, usernameEdited, ageEdited, aboutEdited));
                }
            }
            if(usernameEdited != null && !usernameEdited.isEmpty()){
                newUserFlag = false;
            }
            else{
                newUserFlag = true;
                Toast.makeText(getActivity(), "Username required",
                        Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }

    private void logResult(Boolean result){
        if(result){
            Log.i(TAG,"Database inserted correctly");
        }
        else{
            Log.e(TAG,"Database error");
        }
    }
}
