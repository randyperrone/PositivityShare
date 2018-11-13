package com.randyperrone.randyperrone.positivityshare.Controller.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.randyperrone.randyperrone.positivityshare.Controller.LoginActivity;
import com.randyperrone.randyperrone.positivityshare.Model.ComplimentDownloadService;
import com.randyperrone.randyperrone.positivityshare.Model.Message;
import com.randyperrone.randyperrone.positivityshare.Model.User;
import com.randyperrone.randyperrone.positivityshare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_MESSAGES;
import static com.randyperrone.randyperrone.positivityshare.Model.Consts.DATABASE_PATH_UPLOADS_USERS;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class ChooseComplimentFragment extends Fragment {
    private final String TAG = "ChooseCompFragment";
    private final String URL = "https://complimentr.com/api";
    private View layoutView;
    private User user;
    private TextView complimentTV;
    private Button getNextButton, sendComplimentButton;
    private String userKey, complimentStr;
    private ComplimentDownloadService downloadService;
    private DatabaseReference databaseReference;
    private String uploadId;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ChooseComplimentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ChooseComplimentFragment.
     */
    public static ChooseComplimentFragment newInstance(String param1, String param2) {
        ChooseComplimentFragment fragment = new ChooseComplimentFragment();
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
            Bundle bundle = getArguments();
            if(bundle != null){
                user = bundle.getParcelable("user");
            }
            if(user != null){
                userKey = user.getUserKey();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutView = inflater.inflate(R.layout.fragment_choose_compliment, container, false);
        complimentTV = (TextView)layoutView.findViewById(R.id.actual_compliment_tv);
        getNextButton = (Button)layoutView.findViewById(R.id.get_next_compliment_button);
        sendComplimentButton = (Button)layoutView.findViewById(R.id.send_compliment_button);
        downloadService = new ComplimentDownloadService(getActivity().getApplicationContext());
        try{
            uploadId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
        if(uploadId == null){
            Intent intent = new Intent(layoutView.getContext(), LoginActivity.class);
            layoutView.getContext().startActivity(intent);
        }

        getData();

        getNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
        sendComplimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userKey != null && complimentStr != null){
                    try{
                        databaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_USERS).child(userKey).child(DATABASE_PATH_UPLOADS_MESSAGES);
                        String key = databaseReference.push().getKey();
                        DateFormat df = new SimpleDateFormat("MMM d, yyyy, HH:mm a");
                        String date = df.format(Calendar.getInstance().getTime());
                        Message message = new Message(uploadId, date, complimentStr, key);
                        FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_MESSAGES).child(userKey).child(key).setValue(message);
                        Toast.makeText(getActivity(), "Message Sent!",
                                Toast.LENGTH_LONG).show();
                        Log.i(TAG, "Message sent to user");
                    }catch (Exception e){
                        Log.e(TAG, e.toString());
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Error: Message Not Sent",
                            Toast.LENGTH_LONG).show();
                    Log.w(TAG, "user is null or compliment is null");
                }
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return layoutView;
    }

    private void getData(){
        downloadService.downloadCompliment(URL, new ComplimentDownloadService.VolleyCallBack() {
            @Override
            public void onSuccess(String compliment) {
                Log.i(TAG, "downloadData onSuccess");
                complimentStr = compliment;
                if(complimentStr != null){
                    complimentTV.setText(complimentStr);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable("user", user);
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
