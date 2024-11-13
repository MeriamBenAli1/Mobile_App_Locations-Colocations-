package com.example.mobileappproject_tekdev_5sae3.Entity;

import java.io.Serializable;
import java.util.Date;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "publications")
public class Publication implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String description;
    private String type;
    private int userId;
    private String  datePublication;
    private String imageUri;
    private int likeCount;

    public Publication(String description, String type,
                       int userId,String  datePublication,
                       String imageUri) {
        this.description = description;
        this.type = type;
        this.userId = userId;
        this.datePublication= datePublication;
        this.imageUri = imageUri != null ? imageUri : "";  // Chaîne vide pour éviter les erreurs
    }

    public Publication() {

    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }



    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String  getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(String  datePublication) {
        this.datePublication = datePublication;
    }
    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}

