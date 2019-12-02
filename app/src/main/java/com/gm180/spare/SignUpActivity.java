package com.gm180.spare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SignUpActivity";

    private EditText et_Email;
    private EditText et_Password;
    private EditText et_Pwdchk;
    private EditText et_Birthday;
    private EditText et_PhoneNum;
    private EditText et_Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //View
        et_Email = findViewById(R.id.et_Email);
        et_Password = findViewById(R.id.et_Password);
        et_Birthday = findViewById(R.id.et_Birthday);
        et_Name = findViewById(R.id.et_Name);
        et_PhoneNum = findViewById(R.id.et_PhoneNum);
        et_Pwdchk = findViewById(R.id.et_confirmPassword);


        //Button
        findViewById(R.id.bt_signUpComplete).setOnClickListener(this);
        findViewById(R.id.bt_certify).setOnClickListener(this);
    }

    private void createAccount(String email, String password, String birthday, String name, String phonenumber, String pwdchk) {
        Log.d(TAG, "createAccount start!!!" );
        if (!validateForm()) {
            return;
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = et_Email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            et_Email.setError("Required.");
            valid = false;
        } else {
            et_Email.setError(null);
        }

        String pwd = et_Password.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            et_Password.setError("Required.");
            valid = false;
        } else {
            et_Password.setError(null);
        }

        String pwdchk = et_Pwdchk.getText().toString();
        if (TextUtils.isEmpty(pwdchk)) {
            et_Pwdchk.setError("Required.");
            valid = false;
        } else {
            et_Pwdchk.setError(null);
        }

        String name = et_Name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            et_Name.setError("Required.");
            valid = false;
        } else {
            et_Name.setError(null);
        }

        String bd = et_Birthday.getText().toString();
        if (TextUtils.isEmpty(bd)) {
            et_Birthday.setError("Required.");
            valid = false;
        } else {
            et_Birthday.setError(null);
        }

        String phonenum = et_PhoneNum.getText().toString();
        if (TextUtils.isEmpty(phonenum)) {
            et_PhoneNum.setError("Required.");
            valid = false;
        } else {
            et_PhoneNum.setError(null);
        }

        if (!pwd.equals(pwdchk)) {
            et_Pwdchk.setError("Not Same Password!");
            valid = false;
        } else {
            et_Pwdchk.setError(null);
        }

        return valid;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_signUpComplete) {
            String mEmail = et_Email.getText().toString();
            String mPassword = et_Password.getText().toString();
            String mBirthday = et_Birthday.getText().toString();
            String mName = et_Name.getText().toString();
            String mPhoneNum = et_PhoneNum.getText().toString();
            String mPwdchk = et_Pwdchk.getText().toString();
            createAccount(mEmail, mPassword, mBirthday,mName, mPhoneNum, mPwdchk);




        } else if(view.getId() == R.id.bt_certify) {

        }
    }
}
