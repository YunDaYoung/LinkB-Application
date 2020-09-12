package com.example.linkb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class mainSearchFrag extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_logined_main_frag2, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.main_nav_toolbar2);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);



        return view;
    }
}