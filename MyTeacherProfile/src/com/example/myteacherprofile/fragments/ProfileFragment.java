
package com.example.myteacherprofile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myteacherprofile.R;

public class ProfileFragment extends Fragment {
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }
}
