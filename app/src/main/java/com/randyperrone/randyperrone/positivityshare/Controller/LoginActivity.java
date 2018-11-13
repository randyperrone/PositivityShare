package com.randyperrone.randyperrone.positivityshare.Controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.randyperrone.randyperrone.positivityshare.Model.Consts;
import com.randyperrone.randyperrone.positivityshare.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private final String TAG = "LoginActivity";
    private final int RC_SIGN_IN = 101;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    private DatabaseReference mDatabase;
    private String uploadId;
    private FrameLayout progressBarHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        progressBarHolder = (FrameLayout)findViewById(R.id.frame_layout_pb);
        signInButton = (SignInButton)findViewById(R.id.google_signin_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        progressBarHolder.setVisibility(View.VISIBLE);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            uploadId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            mDatabase = FirebaseDatabase.getInstance().getReference(Consts.DATABASE_PATH_UPLOADS_USERS).child(uploadId);
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();
                    boolean check = true;
                    if(data != null){
                        if(data.get(Consts.DATABASE_PATH_UPLOADS_NEWUSER) != null){
                            String user = data.get(Consts.DATABASE_PATH_UPLOADS_NEWUSER).toString();
                            check = Boolean.valueOf(user);
                            if(!check){
                                progressBarHolder.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(), BottomNavigationMainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                    else if(data == null || data.get(Consts.DATABASE_PATH_UPLOADS_NEWUSER) == null || check){
                        progressBarHolder.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(), NewUserScreenActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.toString());
                }
            });
        }
        else{
            progressBarHolder.setVisibility(View.GONE);
        }

    }
}
