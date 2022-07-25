package com.lwt.go4lunch.ui.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lwt.go4lunch.databinding.FragmentWorkmatesBinding;
import com.lwt.go4lunch.databinding.FragmentWorkmatesListBinding;


public class WorkmatesFragment extends Fragment {

    private FragmentWorkmatesBinding binding;


    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup photoContainer,
                              Bundle savedInstanceState) {
        binding = FragmentWorkmatesBinding.inflate(inflater, photoContainer, false);
        View view = binding.getRoot();
        return view;
    }
}