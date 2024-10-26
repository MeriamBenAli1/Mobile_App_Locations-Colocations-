package com.example.mobileappproject_tekdev_5sae3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mobileappproject_tekdev_5sae3.entities.Annonce;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdsAdapter extends RecyclerView.Adapter<AdsAdapter.AdsViewHolder> implements Filterable {
    private List<Annonce> adList;
    private List<Annonce> adListFull; // Full list for filtering
    private final Context context;
    private final OnAdClickListener adClickListener;

    // Functional interface
    public interface OnAdClickListener {
        void onAdClick(Annonce annonce);
        void onAdDelete(Annonce annonce); // Keep both methods
    }

    public AdsAdapter(Context context, List<Annonce> adList, OnAdClickListener listener) {
        this.context = context;
        this.adList = adList;
        this.adListFull = new ArrayList<>(adList);
        this.adClickListener = listener;
    }

    @NonNull
    @Override
    public AdsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ad_item, parent, false);
        return new AdsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdsViewHolder holder, int position) {
        Annonce annonce = adList.get(position);
        holder.titleTextView.setText(annonce.getTitle());
        holder.descriptionTextView.setText(annonce.getDescription());
        holder.priceTextView.setText(String.valueOf(annonce.getPrice()));
        holder.dateTextView.setText(annonce.getFormattedDate());

        // Use Glide to load the image
        Glide.with(context)
                .load(annonce.getImageUri())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> adClickListener.onAdClick(annonce));
        // Set click listener for edit button
        holder.editButton.setOnClickListener(v -> adClickListener.onAdClick(annonce));

        // Set click listener for delete button
        holder.deleteButton.setOnClickListener(v -> {
            // Notify the listener to delete the ad
            adClickListener.onAdDelete(annonce);
        });
    }

    @Override
    public int getItemCount() {
        return adList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Annonce> filteredAds = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredAds.addAll(adListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Annonce ad : adListFull) {
                        // Check if the ad matches the filter criteria
                        if (ad.getTitle().toLowerCase().contains(filterPattern) ||
                                ad.getDescription().toLowerCase().contains(filterPattern) ||
                                String.valueOf(ad.getPrice()).contains(filterPattern) ||
                                ad.getFormattedDate().toLowerCase().contains(filterPattern)) { // Filtering by date
                            filteredAds.add(ad);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredAds;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                adList.clear();
                adList.addAll((List<Annonce>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public static class AdsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        TextView priceTextView;
        TextView dateTextView;
        ImageView imageView;
        Button editButton;
        Button deleteButton;

        public AdsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.adTitle);
            descriptionTextView = itemView.findViewById(R.id.adDescription);
            priceTextView = itemView.findViewById(R.id.adPrice);
            dateTextView = itemView.findViewById(R.id.adDate);
            imageView = itemView.findViewById(R.id.adImage);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
