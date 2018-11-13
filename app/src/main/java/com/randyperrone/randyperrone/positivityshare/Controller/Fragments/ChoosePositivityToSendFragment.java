package com.randyperrone.randyperrone.positivityshare.Controller.Fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.randyperrone.randyperrone.positivityshare.Controller.SendPositivityActivity;
import com.randyperrone.randyperrone.positivityshare.Model.User;
import com.randyperrone.randyperrone.positivityshare.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChoosePositivityToSendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChoosePositivityToSendFragment extends Fragment {
    private final String TAG = "ChoosePositivityFrag";
    private View layoutView;
    private String usernameStr, aboutStr, ageStr, picURLStr;
    private User user;
    private TextView usernameTV, aboutTV, ageTV;
    private ImageView userImageIV;
    private Button complimentButton, inspirationButton;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ChoosePositivityToSendFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static ChoosePositivityToSendFragment newInstance(String param1, String param2) {
        ChoosePositivityToSendFragment fragment = new ChoosePositivityToSendFragment();
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
                usernameStr = user.getUsername();
                aboutStr = user.getAbout();
                ageStr = user.getAge();
                picURLStr = user.getPicUrl();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutView = inflater.inflate(R.layout.fragment_choose_positivity_to_send, container, false);

        userImageIV = (ImageView)layoutView.findViewById(R.id.browse_user_image);
        usernameTV = (TextView)layoutView.findViewById(R.id.browse_username_tv);
        aboutTV = (TextView)layoutView.findViewById(R.id.browse_about_tv);
        ageTV = (TextView)layoutView.findViewById(R.id.browse_age_tv);
        complimentButton = (Button)layoutView.findViewById(R.id.compliment_button);
        inspirationButton = (Button)layoutView.findViewById(R.id.inspiration_button);

        if(usernameStr != null){
            usernameTV.setText(usernameStr);
        }
        if(aboutStr != null){
            aboutTV.setText(aboutStr);
        }
        if(ageStr != null){
            ageTV.setText(ageStr);
        }
        if(picURLStr != null){
            try{
                Glide.with(getActivity())
                        .load(picURLStr)
                        .apply(new RequestOptions()
                                .centerCrop()
                                .fitCenter()
                                .placeholder(R.mipmap.positivity_share_icon))
                        .into(userImageIV);
            }
            catch (Exception e){
                Log.e(TAG, e.toString());
            }
        }

        complimentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseComplimentFragment fragment = new ChooseComplimentFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);
                fragment.setArguments(bundle);

                ((SendPositivityActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        inspirationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Goto inspiration fragment
            }
        });

        return layoutView;
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
