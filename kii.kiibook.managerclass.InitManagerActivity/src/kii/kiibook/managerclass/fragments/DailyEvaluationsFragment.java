
package kii.kiibook.managerclass.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kii.kiibook.teacher.R;

public class DailyEvaluationsFragment extends Fragment {
    
    private View mRoot;
    
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
    
        // Inflate the layout for this fragment
        mRoot = inflater.inflate(R.layout.avaliations_diaries, container, false);
        
        return mRoot;
    }
}
