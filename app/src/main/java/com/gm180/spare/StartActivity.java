package com.gm180.spare;

import androidx.annotation.NonNull;

import android.content.Intent;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.Serializable;


public class StartActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "StartActivity";

    private static final int RC_SIGN_UP = 9002;

    private FirebaseAuth mAuth;




    /** 구글 로그인 구현할 때 쓰는 것
    private static final int RC_SIGN_IN = 9001;
    // [START declare_auth]

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    // [END declare_auth]
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // Button listeners

        findViewById(R.id.signUpButton).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();



        /** 구글 로그인 구현 할 때 쓰는
        findViewById(R.id.signInButton).setOnClickListener(this);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) //
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [START initialize_auth]
        // Initialize Firebase Auth

        // [END initialize_auth]
        Log.d(TAG, "on create success!!!!!!!!!!!!!!!!");것
         **/
    }


    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        Log.d(TAG, "on start success!!!!!!!!!!!!!!!!");

        if (currentUser != null) {
            goHomeIntent();
        } else {
            // No user is signed in
        }


    }
    // [END on_start_check_user]

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signUpButton) {
            Intent intent = new Intent(this, SignUpActivity.class);
            Log.d(TAG, "sign out clicked !!!!!!!!!!!!!!!!");
            startActivityForResult(intent,RC_SIGN_UP);
        }/* else if (i == R.id.signInButton) {
            Log.d(TAG, "sign in clicked !!!!!!!!!!!!!!!!");
            //signIn();
        }*/
    }


    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        /** 구글 로그인 구현할 때 쓴 것
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                Log.d(TAG, "Google sign in success !!!!!!!!!!!!!!!!");
                goHomeIntent();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed  !!!!!!!!!!!!!!!!", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }**/
    }
    // [END onactivityresult]

    /** 구글로그인 구현 할 때 쓴 것
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
    // [END auth_with_google]**/


    /**구글 로그인 구현 할 때 쓴 것
    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();  // 구글 로그인 연동 인텐트(즉, 구글 로그인 페이지)
        startActivityForResult(signInIntent, RC_SIGN_IN);             // 실행
    }
    // [END signin]
     **/

    /** 구글 로그아웃 구현할 때 쓴 것
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
     **/


    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            //findViewById(R.id.signInButton).setVisibility(View.GONE);
            //findViewById(R.id.signOutAndDisconnect).setVisibility(View.VISIBLE);
        } else {
            //findViewById(R.id.signInButton).setVisibility(View.VISIBLE);
            //findViewById(R.id.signOutAndDisconnect).setVisibility(View.GONE);
        }
    }

    private void goHomeIntent() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }



}
