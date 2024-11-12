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
       // updateButton.setOnClickListener(v -> updateLastMessage());

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
                addMessageToView(messageText, lastMessageId, true, false);
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
            boolean isEdited = messageText.contains("(modifié)");
            addMessageToView(messageText, id, false, isEdited);

            // Store the ID of the last displayed message
            lastMessageId = id;
        }
        cursor.close();
    }

    private void addMessageToView(String messageText, long messageId, boolean isOutgoing, boolean isEdited) {
        // Create a layout to hold the message and delete button
        LinearLayout messageLayout = new LinearLayout(getActivity());
        messageLayout.setOrientation(LinearLayout.HORIZONTAL);

        TextView messageView = new TextView(getActivity());
        messageView.setText(messageText + (isEdited ? " (modifié)" : ""));
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

        messageView.setOnClickListener(v -> editMessageDialog(messageId, messageView));

        messagesContainer.addView(messageView);
    }

    private void editMessageDialog(long messageId, TextView messageView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Modifier le message");

        final EditText input = new EditText(getActivity());
        input.setText(messageView.getText().toString().replace(" (modifié)", ""));
        builder.setView(input);

        builder.setPositiveButton("Modifier", (dialog, which) -> {
            String updatedText = input.getText().toString().trim();
            if (!updatedText.isEmpty()) {
                updateMessageInDb(messageId, updatedText + " (modifié)");
                loadMessages();
            } else {
                Toast.makeText(getActivity(), "Le texte ne peut pas être vide", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updateMessageInDb(long messageId, String updatedText) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MessagesDatabase.COLUMN_MESSAGE_TEXT, updatedText);

        int rowsAffected = db.update(MessagesDatabase.TABLE_MESSAGES, values, "_id = ?", new String[]{String.valueOf(messageId)});
        if (rowsAffected > 0) {
            Toast.makeText(getActivity(), "Message modifié", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Erreur lors de la modification", Toast.LENGTH_SHORT).show();
        }
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
            boolean isEdited = messageText.contains("(modifié)");
            addMessageToView(messageText, id, false, isEdited);
        }
        cursor.close();
    }

    private void deleteLastMessage() {
        if (lastMessageId != -1) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Supprimer le dernier message")
                    .setMessage("Êtes-vous sûr de vouloir supprimer ce message ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        int rowsDeleted = db.delete(MessagesDatabase.TABLE_MESSAGES, "_id = ?", new String[]{String.valueOf(lastMessageId)});
                        if (rowsDeleted > 0) {
                            Toast.makeText(getActivity(), "Message supprimé", Toast.LENGTH_SHORT).show();
                            loadMessages();
                        } else {
                            Toast.makeText(getActivity(), "Erreur lors de la suppression", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Annuler", null)
                    .show();
        } else {
            Toast.makeText(getActivity(), "Aucun message à supprimer", Toast.LENGTH_SHORT).show();
        }
    }
}
