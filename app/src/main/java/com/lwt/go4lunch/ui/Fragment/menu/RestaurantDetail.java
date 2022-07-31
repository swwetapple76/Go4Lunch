package com.lwt.go4lunch.ui.Fragment.menu;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lwt.go4lunch.R;
import com.lwt.go4lunch.databinding.RestaurantDetailBinding;
import com.lwt.go4lunch.databinding.RestaurantDetailListBinding;

public class RestaurantDetail extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.restaurant_detail, container, false);
    }

}


