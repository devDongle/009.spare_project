package com.gm180.spare;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseActivity  {

    private static final String TAG = "HomeActivity";

    FirebaseUser user;

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    private FirebaseAuth firebaseAuth;
    private GoogleApiClient mGoogleApiClient;

    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_chatting, R.id.navigation_board, R.id.navigation_average, R.id.navigation_setting)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        db = FirebaseFirestore.getInstance();

        //login confirm start
        user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                String providerId = profile.getProviderId();
                String phonnNum = profile.getPhoneNumber();
                // UID specific to the provider
                String uid = profile.getUid();

                // Name, email address, and profile photo Url
                String name = profile.getDisplayName();
                String email = profile.getEmail();
                Uri photoUrl = profile.getPhotoUrl();
                Log.d(TAG, "providerID!!!!!!!!!!!!!!!!"+providerId);
                Log.d(TAG, "UID!!!!!!!!!!!!!!!!"+uid);
                Log.d(TAG, "Name!!!!!!!!!!!!!!!!"+name);
                Log.d(TAG, "email!!!!!!!!!!!!!!!!"+email);
                Log.d(TAG,"phone"+phonnNum);
                addUser(name, email);
            }
        }
        //login confirm end

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        firebaseAuth = FirebaseAuth.getInstance();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "Login fail");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }


    public void signOut() {
        showProgressDialog();
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

            @Override
            public void onConnected(@Nullable Bundle bundle) {
                // Firebase signout
                firebaseAuth.signOut();
                Log.d(TAG, "Firebase sign out success !!!!!!!!!!!!!!!!");

                // Google Client signout
                if (mGoogleApiClient.isConnected()) {
                    // Google sign out
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {

                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.d(TAG, "User Logged out");
                                Intent intent = new Intent(HomeActivity.this, StartActivity.class);
                                startActivity(intent);
                                finish();
                                //setResult(ResultCode.SIGN_OUT_SUCCESS);
                            } else {
                                //setResult(ResultCode.SIGN_OUT_FAIL);
                            }

                            hideProgressDialog();

                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d(TAG, "Google API Client Connection Suspended");
                //setResult(ResultCode.SIGN_OUT_FAIL);
                //hideProgressDialog();
                //finish();
            }
        });

    }

    public void onClickSignOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Out").setMessage("로그아웃 하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                signOut();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void addUser(String name, String email) {
        // [START add_ada_lovelace]
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        // [END add_ada_lovelace]
    }

}
