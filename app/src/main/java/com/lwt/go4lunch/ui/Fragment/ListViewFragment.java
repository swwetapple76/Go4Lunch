package com.lwt.go4lunch.ui.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ListViewFragment extends Fragment{


    private FragmentListView binding;


    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        binding = FragmentListViewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }


}