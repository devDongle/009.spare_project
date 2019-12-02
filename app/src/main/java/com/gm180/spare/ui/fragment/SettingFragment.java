package com.gm180.spare.ui.fragment;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.gm180.spare.BaseActivity;
import com.gm180.spare.HomeActivity;
import com.gm180.spare.R;


public class SettingFragment extends Fragment {

    private SettingViewModel mViewModel;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    private static final String TAG = "SettingFragment";
    HomeActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (HomeActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_fragment, container, false);



        view.findViewById(R.id.bt_signOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "sign out clicked !!!!!!!!!!!!!!!!");
                activity.onClickSignOut();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingViewModel.class);
        // TODO: Use the ViewModel

    }


}
