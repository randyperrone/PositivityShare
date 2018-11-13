package com.randyperrone.randyperrone.positivityshare.Controller.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.randyperrone.randyperrone.positivityshare.Controller.LoginActivity;
import com.randyperrone.randyperrone.positivityshare.Model.DatabaseHelper;
import com.randyperrone.randyperrone.positivityshare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_PROFILEPICURL;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_USERS;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.STORAGE_PATH_PICTURELOCATION;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditPhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPhotoFragment extends Fragment {
    private final String TAG = "EditPhotoFragment";
    private static final String DBTABLENAME = "user";
    private static final int SELECT_PICTURE = 101;
    private static final int REPLACEPICTURE = 201;
    private View layoutView;
    private DatabaseReference mDatabase;
    private DatabaseHelper dbHelper;
    private String uploadID, profilePicURL;
    private ImageButton profilePicImageButton;
    private Uri picture;
    private FrameLayout progressBarHolder;


    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public EditPhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment EditPhotoFragment.
     */
    public static EditPhotoFragment newInstance() {
        EditPhotoFragment fragment = new EditPhotoFragment();
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
        layoutView = inflater.inflate(R.layout.fragment_edit_photo, container, false);

        progressBarHolder = (FrameLayout)layoutView.findViewById(R.id.frame_layout_pb);
        profilePicImageButton = (ImageButton)layoutView.findViewById(R.id.profile_pic_image);
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

        profilePicImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if(profilePicURL != null && !profilePicURL.isEmpty()){
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), REPLACEPICTURE);
                }
                else{
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), SELECT_PICTURE);
                }
            }
        });
        return layoutView;
    }

    private boolean fetchDataFromDatabase(){
        boolean dataInDatabase = false;
        try{
            Cursor cursor = dbHelper.getUser(uploadID);
            if(cursor.moveToFirst()){
                profilePicURL = cursor.getString(4);
                if(profilePicURL != null){
                    dataInDatabase = true;
                    Glide.with(getContext())
                            .load(profilePicURL)
                            .apply(new RequestOptions()
                                    .fitCenter()
                                    .centerCrop())
                            .into(profilePicImageButton);
                }
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
                        if (data.get(DATABASE_PATH_UPLOADS_PROFILEPICURL) != null) {
                            profilePicURL = data.get(DATABASE_PATH_UPLOADS_PROFILEPICURL).toString();
                            Glide.with(getContext())
                                    .load(profilePicURL)
                                    .apply(new RequestOptions()
                                            .fitCenter()
                                            .centerCrop())
                                    .into(profilePicImageButton);
                        }
                    }
                    progressBarHolder.setVisibility(View.GONE);
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

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REPLACEPICTURE) {
                StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(profilePicURL);
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "File deleted successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
            }
            if (requestCode == SELECT_PICTURE || requestCode == REPLACEPICTURE) {
                progressBarHolder.setVisibility(View.VISIBLE);

                picture = data.getData();
                StorageReference path = FirebaseStorage.getInstance().getReference(STORAGE_PATH_PICTURELOCATION).child("img" + System.currentTimeMillis());
                path.putFile(picture).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        @SuppressWarnings("VisibleForTests") Uri aURI = taskSnapshot.getDownloadUrl();
                        String temp = aURI.toString();
                        FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USERS).child(uploadID).child(DATABASE_PATH_UPLOADS_PROFILEPICURL).setValue(temp);
                        dbHelper.updatePicUrl(uploadID, temp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
            }
        }
    }
}
