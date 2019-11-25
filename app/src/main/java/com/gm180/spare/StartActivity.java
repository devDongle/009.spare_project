package com.gm180.spare;

import androidx.annotation.NonNull;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;



public class StartActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "StartActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]



    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Button listeners
        findViewById(R.id.signInButton).setOnClickListener(this);
        findViewById(R.id.signOutButton).setOnClickListener(this);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) //
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        //checkCurrentUser();

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        Log.d(TAG, "on create success!!!!!!!!!!!!!!!!");
    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        Log.d(TAG, "on start success!!!!!!!!!!!!!!!!");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }

    }
    // [END on_start_check_user]

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signInButton) {
            Log.d(TAG, "sign in clicked !!!!!!!!!!!!!!!!");
            signIn();
        } else if (i == R.id.signOutButton) {
            signOut();
            Log.d(TAG, "sign out clicked !!!!!!!!!!!!!!!!");
        }
    }


    // [START onactivityresult]
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
                Log.d(TAG, "Google sign in success !!!!!!!!!!!!!!!!");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed  !!!!!!!!!!!!!!!!", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]


    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

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
                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]



    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();  // 구글 로그인 연동 인텐트(즉, 구글 로그인 페이지)
        startActivityForResult(signInIntent, RC_SIGN_IN);             // 실행
    }
    // [END signin]


    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
        Log.d(TAG, "Google sign out success !!!!!!!!!!!!!!!!");
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            //mStatusTextView.setText(getString(R.string.google_status_fmt, user.getEmail()));
            //mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.signInButton).setVisibility(View.GONE);
            findViewById(R.id.signOutAndDisconnect).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);
            //mDetailTextView.setText(null);

            findViewById(R.id.signInButton).setVisibility(View.VISIBLE);
            findViewById(R.id.signOutAndDisconnect).setVisibility(View.GONE);
        }
    }





}
