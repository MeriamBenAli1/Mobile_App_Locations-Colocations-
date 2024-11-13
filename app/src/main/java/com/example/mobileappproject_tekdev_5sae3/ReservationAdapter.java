package com.example.mobileappproject_tekdev_5sae3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappproject_tekdev_5sae3.entity.Reservation;

import java.util.List;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private final List<Reservation> reservations;
    private final OnReservationDeleteListener onReservationDeleteListener;

    // Constructor with listener for delete
    public ReservationAdapter(List<Reservation> reservations, OnReservationDeleteListener onReservationDeleteListener) {
        this.reservations = reservations;
        this.onReservationDeleteListener = onReservationDeleteListener;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        holder.tvReservationDate.setText(reservation.getDate());
        holder.tvReservationTitle.setText(reservation.getFirstName() + " " + reservation.getLastName());
        holder.tvReservationDetails.setText(reservation.getGuests() + " guests for " + reservation.getDuration() + " " + reservation.getDurationType());
        holder.tvReservationDetails.append("\nTotal price: $" + reservation.getAmount());
        holder.tvReservationDetails.append("\nEmail: " + reservation.getEmail());
        holder.tvReservationDetails.append("\nPhone: " + reservation.getPhone());

        // Set click listener for delete button (AppCompatImageButton)
        holder.btnDelete.setOnClickListener(v -> {
            // Notify the fragment to delete the reservation
            onReservationDeleteListener.onReservationDelete(reservation);
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tvReservationTitle, tvReservationDate, tvReservationDetails;
        AppCompatImageButton btnDelete;  // Use AppCompatImageButton here

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReservationTitle = itemView.findViewById(R.id.tvReservationTitle);
            tvReservationDate = itemView.findViewById(R.id.tvReservationDate);
            tvReservationDetails = itemView.findViewById(R.id.tvReservationDetails);
            btnDelete = itemView.findViewById(R.id.btnDeleteReservation);  // Reference AppCompatImageButton
        }
    }

    // Interface to handle delete action in the fragment
    public interface OnReservationDeleteListener {
        void onReservationDelete(Reservation reservation);
    }
}
