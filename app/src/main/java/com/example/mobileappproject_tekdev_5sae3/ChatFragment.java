package com.example.mobileappproject_tekdev_5sae3;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

public class ChatFragment extends Fragment {

    private EditText messageInput;
    private EditText searchInput;
    private ImageButton sendButton;
    private ImageButton deleteButton;
    private ImageButton updateButton;
    private LinearLayout messagesContainer;
    private MessagesDatabase dbHelper;
    private long lastMessageId = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        messageInput = rootView.findViewById(R.id.editTextMessage);
        searchInput = rootView.findViewById(R.id.editTextSearch);
        sendButton = rootView.findViewById(R.id.buttonSend);
        messagesContainer = rootView.findViewById(R.id.messages_container);

        // Initialize buttons
        deleteButton = rootView.findViewById(R.id.buttonDelete);
        updateButton = rootView.findViewById(R.id.buttonUpdate);

        dbHelper = new MessagesDatabase(getActivity());

        sendButton.setOnClickListener(v -> sendMessage());
        deleteButton.setOnClickListener(v -> deleteLastMessage());
        updateButton.setOnClickListener(v -> updateLastMessage());

        // Add a text watcher to search input to filter messages dynamically
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchMessages(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadMessages();
        return rootView;
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();

        if (!messageText.isEmpty()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(MessagesDatabase.COLUMN_MESSAGE_TEXT, messageText);
            values.put(MessagesDatabase.COLUMN_MESSAGE_SENDER, "Ben Ali Meriam");

            lastMessageId = db.insert(MessagesDatabase.TABLE_MESSAGES, null, values);
            if (lastMessageId != -1) {
                addMessageToView(messageText, lastMessageId, true);
                messageInput.setText("");
            } else {
                Toast.makeText(getActivity(), "Erreur lors de l'envoi du message", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Veuillez entrer un message", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMessages() {
        messagesContainer.removeAllViews();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "_id",
                MessagesDatabase.COLUMN_MESSAGE_TEXT,
                MessagesDatabase.COLUMN_MESSAGE_SENDER
        };

        Cursor cursor = db.query(
                MessagesDatabase.TABLE_MESSAGES,
                projection,
                null,
                null,
                null,
                null,
                MessagesDatabase.COLUMN_MESSAGE_SENDER + " ASC"
        );

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
            String messageText = cursor.getString(cursor.getColumnIndexOrThrow(MessagesDatabase.COLUMN_MESSAGE_TEXT));
            addMessageToView(messageText, id, false);

            // Store the ID of the last displayed message
            lastMessageId = id;
        }
        cursor.close();
    }

    private void addMessageToView(String messageText, long messageId, boolean isOutgoing) {
        // Create a layout to hold the message and delete button
        LinearLayout messageLayout = new LinearLayout(getActivity());
        messageLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView messageView = new TextView(getActivity());
        messageView.setText(messageText);
        messageView.setPadding(12, 12, 12, 12);
        messageView.setTextSize(16);

        if (isOutgoing) {
            messageView.setBackgroundResource(R.drawable.message_outgoing);
            messageView.setTextColor(getResources().getColor(android.R.color.white));
            messageView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
        } else {
            messageView.setBackgroundResource(R.drawable.message_incoming);
            messageView.setTextColor(getResources().getColor(android.R.color.black));
            messageView.setGravity(View.TEXT_ALIGNMENT_TEXT_START);
        }

        // Add delete button, shown only for the last message
        ImageButton deleteButton = new ImageButton(getActivity());
        deleteButton.setImageResource(android.R.drawable.ic_delete);
        if (messageId == lastMessageId) {
            deleteButton.setVisibility(View.VISIBLE);
        } else {
            deleteButton.setVisibility(View.INVISIBLE);
        }
        deleteButton.setOnClickListener(v -> confirmDeleteMessage(messageId));

        // Add message view and delete button to the layout
        messageLayout.addView(messageView);
        messageLayout.addView(deleteButton);

        messagesContainer.addView(messageLayout);
    }

    private void updateLastMessage() {
        if (lastMessageId == -1) {
            Toast.makeText(getActivity(), "Aucun message à mettre à jour", Toast.LENGTH_SHORT).show();
            return;
        }

        String updatedText = messageInput.getText().toString().trim();
        if (updatedText.isEmpty()) {
            Toast.makeText(getActivity(), "Veuillez entrer un message pour la mise à jour", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MessagesDatabase.COLUMN_MESSAGE_TEXT, updatedText);

        int rowsAffected = db.update(MessagesDatabase.TABLE_MESSAGES, values, "_id = ?", new String[]{String.valueOf(lastMessageId)});
        if (rowsAffected > 0) {
            messagesContainer.removeAllViews();
            loadMessages();
            messageInput.setText("");
            Toast.makeText(getActivity(), "Message mis à jour", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Erreur lors de la mise à jour du message", Toast.LENGTH_SHORT).show();
        }
    }

    // Confirmation avant suppression du message
    private void confirmDeleteMessage(long messageId) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Supprimer ce message ?")
                .setMessage("Voulez-vous vraiment supprimer ce message ?")
                .setPositiveButton("Oui", (dialog, which) -> deleteMessage(messageId))
                .setNegativeButton("Non", null)
                .show();
    }

    private void deleteMessage(long messageId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(MessagesDatabase.TABLE_MESSAGES, "_id = ?", new String[]{String.valueOf(messageId)});
        if (rowsDeleted > 0) {
            messagesContainer.removeAllViews();
            loadMessages();
            Toast.makeText(getActivity(), "Message supprimé", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Erreur lors de la suppression du message", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteLastMessage() {
        deleteMessage(lastMessageId);
    }

    private void searchMessages(String query) {
        messagesContainer.removeAllViews();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                "_id",
                MessagesDatabase.COLUMN_MESSAGE_TEXT,
                MessagesDatabase.COLUMN_MESSAGE_SENDER
        };

        String selection = MessagesDatabase.COLUMN_MESSAGE_TEXT + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};

        Cursor cursor = db.query(
                MessagesDatabase.TABLE_MESSAGES,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                MessagesDatabase.COLUMN_MESSAGE_SENDER + " ASC"
        );

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
            String messageText = cursor.getString(cursor.getColumnIndexOrThrow(MessagesDatabase.COLUMN_MESSAGE_TEXT));
            addMessageToView(messageText, id, false);
        }
        cursor.close();
    }
}
