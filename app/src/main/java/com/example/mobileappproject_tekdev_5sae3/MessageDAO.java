package com.example.mobileappproject_tekdev_5sae3;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    private SQLiteDatabase database;
    private MessagesDatabase dbHelper;

    public MessageDAO(Context context) {
        dbHelper = new MessagesDatabase(context);
    }

    // Ouvrir la base de données en mode écriture
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    // Fermer la base de données
    public void close() {
        dbHelper.close();
    }

    // Ajouter un message
    public long addMessage(Message message) {
        ContentValues values = new ContentValues();
        values.put(MessagesDatabase.COLUMN_MESSAGE_TEXT, message.getText());
        values.put(MessagesDatabase.COLUMN_MESSAGE_SENDER, message.getSender());

        return database.insert(MessagesDatabase.TABLE_MESSAGES, null, values);
    }

    // Obtenir tous les messages
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();

        // Effectuer une requête pour récupérer tous les messages
        Cursor cursor = database.query(MessagesDatabase.TABLE_MESSAGES,
                new String[]{"_id", MessagesDatabase.COLUMN_MESSAGE_TEXT, MessagesDatabase.COLUMN_MESSAGE_SENDER},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
                String text = cursor.getString(cursor.getColumnIndexOrThrow(MessagesDatabase.COLUMN_MESSAGE_TEXT));
                String sender = cursor.getString(cursor.getColumnIndexOrThrow(MessagesDatabase.COLUMN_MESSAGE_SENDER));

                // Créer un objet Message et l'ajouter à la liste
                messages.add(new Message(id, text, sender));
            }
            cursor.close();
        }

        return messages;
    }

    // Supprimer un message par son ID
    public void deleteMessage(long messageId) {
        database.delete(MessagesDatabase.TABLE_MESSAGES, "_id = ?", new String[]{String.valueOf(messageId)});
    }

    // Mettre à jour un message
    public void updateMessage(Message message) {
        ContentValues values = new ContentValues();
        values.put(MessagesDatabase.COLUMN_MESSAGE_TEXT, message.getText());
        values.put(MessagesDatabase.COLUMN_MESSAGE_SENDER, message.getSender());

        database.update(MessagesDatabase.TABLE_MESSAGES, values, "_id = ?", new String[]{String.valueOf(message.getId())});
    }
}

