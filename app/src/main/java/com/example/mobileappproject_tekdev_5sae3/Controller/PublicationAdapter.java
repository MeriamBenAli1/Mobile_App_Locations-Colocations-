package com.example.mobileappproject_tekdev_5sae3.Controller;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappproject_tekdev_5sae3.Dao.PublicationDao;
import com.example.mobileappproject_tekdev_5sae3.Entity.Publication;
import com.example.mobileappproject_tekdev_5sae3.R;

import java.util.List;
public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.PublicationViewHolder> {
    private List<Publication> publications;
    private PublicationDao publicationDao;
    private OnPublicationDeleteListener deleteListener;

    public PublicationAdapter(List<Publication> publications, PublicationDao publicationDao, OnPublicationDeleteListener deleteListener) {
        this.publications = publications;
        this.publicationDao = publicationDao;
        this.deleteListener = deleteListener;

    }
    public void updateList(List<Publication> newPublications) {
        this.publications = newPublications;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PublicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publication, parent, false);
        return new PublicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicationViewHolder holder, int position) {
        Publication publication = publications.get(position);
        holder.titreTextView.setText(publication.getTitre());
        holder.descriptionTextView.setText(publication.getDescription());
        holder.typeTextView.setText(publication.getType());
        holder.dateTextView.setText(publication.getDatePublication());
        // Gestion de l'image
        String imageUriString = publication.getImageUri();
        if (imageUriString != null && !imageUriString.isEmpty()) {
            Uri imageUri = Uri.parse(imageUriString);
            holder.imageView.setImageURI(imageUri);
        } else {
            holder.imageView.setImageResource(R.drawable.arn); // Assurez-vous que `default_image` est dans le dossier drawable
        }
        holder.deleteButton.setOnClickListener(v -> {
            Log.d("PublicationAdapter", "Delete button clicked for publication: " + publication.getTitre());

            deleteListener.onPublicationDelete(publication);
        });

    }

    @Override
    public int getItemCount() {
        return publications.size();
    }

    public void filterList(List<Publication> filteredList) {
        publications = filteredList;
        notifyDataSetChanged();
    }

    public interface OnPublicationDeleteListener {
        void onPublicationDelete(Publication publication);
    }


    public static class PublicationViewHolder extends RecyclerView.ViewHolder {
        TextView titreTextView;
        TextView descriptionTextView;
        TextView typeTextView;
        ImageButton buttonLike;
        ImageButton buttonComment;
        ImageButton deleteButton;
        TextView dateTextView;
        ImageView imageView;

        public PublicationViewHolder(@NonNull View itemView) {
            super(itemView);
            titreTextView = itemView.findViewById(R.id.titreTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            buttonLike = itemView.findViewById(R.id.buttonLike);
            buttonComment = itemView.findViewById(R.id.buttonComment);
            imageView = itemView.findViewById(R.id.imageView);
        }

    }
}
