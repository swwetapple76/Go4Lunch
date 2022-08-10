package com.lwt.go4lunch.services.map;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lwt.go4lunch.databinding.NearbyResturtantListBinding;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<PlacesPOJO.CustomA> stLstStores;
    private List<RestaurantModel> models;
    NearbyResturtantListBinding binding;


    public RecyclerViewAdapter(List<PlacesPOJO.CustomA> stores, List<RestaurantModel> storeModels) {

        stLstStores = stores;
        models = storeModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = NearbyResturtantListBinding.inflate(LayoutInflater.from(parent.getContext()));
        MyViewHolder holder = new MyViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.setData(stLstStores.get(holder.getBindingAdapterPosition()), holder, models.get(holder.getBindingAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return Math.min(5, stLstStores.size());
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        NearbyResturtantListBinding binding;
        public MyViewHolder(@NonNull NearbyResturtantListBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(PlacesPOJO.CustomA info, MyViewHolder holder, RestaurantModel restaurantModel) {

            holder.binding.txtStoreDist.setText(restaurantModel.distance + "\n" + restaurantModel.duration);
            holder.binding.txtStoreName.setText(info.name);
            holder.binding.txtStoreAddr.setText(info.vicinity);


        }

    }
}
