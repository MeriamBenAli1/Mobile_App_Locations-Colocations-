package com.example.mobileappproject_tekdev_5sae3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConversationsAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] conversations;
    private final int[] avatars = {R.drawable.ic_user_avatar1, R.drawable.ic_user_avatar2, R.drawable.ic_user_avatar3}; // Exemple d'avatars

    public ConversationsAdapter(Context context, String[] conversations) {
        super(context, R.layout.conversation_item, conversations);
        this.context = context;
        this.conversations = conversations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.conversation_item, parent, false);

        TextView nameTextView = rowView.findViewById(R.id.conversation_name);
        TextView messageTextView = rowView.findViewById(R.id.last_message);
        TextView timeTextView = rowView.findViewById(R.id.conversation_time);
        ImageView avatarImageView = rowView.findViewById(R.id.avatar_image_view);

        // Remplir les champs avec des données
        nameTextView.setText(conversations[position]);
        messageTextView.setText("Dernier message ici...");
        timeTextView.setText("12:34");

        // Assigner un avatar aléatoire
        avatarImageView.setImageResource(avatars[position % avatars.length]);

        return rowView;
    }
}
