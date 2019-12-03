package com.gm180.spare;

import androidx.annotation.NonNull;

import android.content.Intent;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


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
    private static final int RESULT_OK = 1;

    private EditText et_Email;
    private EditText et_Pwd;

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

        // View
        et_Email = findViewById(R.id.signIn_email);
        et_Pwd = findViewById(R.id.signIn_pwd);

        // Button listeners
        findViewById(R.id.signUpButton).setOnClickListener(this);
        findViewById(R.id. bt_signIn).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();


        Log.d(TAG, "complete onCreate!!!");
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
        Log.d(TAG, "Complete onStart!!!");


    }
    // [END on_start_check_user]


    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
            Log.d(TAG, "on start success!!!!!!!!!!!!!!!!");
        }


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



    // [START signin]
    private void signIn(String email, String password) {
        Log.d(TAG, "Sign In start!!!");
        if (!validateForm()) {
            return;
        }
        showProgressDialog();
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "로그인에 실패 했습니다! 다시 확인해주세요.", task.getException());
                            Toast.makeText(StartActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]


    }
    // [END signin]


    private boolean validateForm() {
        boolean valid = true;

        String email = et_Email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            et_Email.setError("Required.");
            valid = false;
        } else {
            et_Email.setError(null);
        }

        String password = et_Pwd.getText().toString();
        if (TextUtils.isEmpty(password)) {
            et_Pwd.setError("Required.");
            valid = false;
        } else {
            et_Pwd.setError(null);
        }

        return valid;
    }


    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            goHomeIntent();
        } else {

        }
    }

    private void goHomeIntent() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signUpButton) {
            Intent intent = new Intent(this, SignUpActivity.class);
            Log.d(TAG, "Sign Up clicked!!!");
            startActivityForResult(intent,RC_SIGN_UP);
        } else if (i == R.id.bt_signIn) {
            Log.d(TAG, "Sign In clicked!!!");
            signIn(et_Email.getText().toString(),et_Pwd.getText().toString());
        }
    }


}
