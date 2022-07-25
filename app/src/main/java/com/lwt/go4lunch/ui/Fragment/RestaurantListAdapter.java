package com.lwt.go4lunch.ui.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lwt.go4lunch.databinding.FragmentListViewBinding;
import com.lwt.go4lunch.ui.Fragment.placeholder.PlaceholderContent.PlaceholderItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    private final List<PlaceholderItem> mValues;

    public RestaurantListAdapter(List<PlaceholderItem> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentListViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mIdView;
        public final TextView mContentView;
        public PlaceholderItem mItem;

        public ViewHolder(FragmentListViewBinding binding) {
            super(binding.getRoot());
            mIdView = binding.restaurantName;
            mContentView = binding.numberOfWorkmatesThere;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}