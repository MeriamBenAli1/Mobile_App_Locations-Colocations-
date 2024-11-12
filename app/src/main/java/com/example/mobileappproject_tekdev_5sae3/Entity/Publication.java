package com.example.mobileappproject_tekdev_5sae3.Entity;

import java.util.Date;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "publications")
public class Publication {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String titre;
    private String description;
    private String type;
    private int userId;
    private String  datePublication;
    private String imageUri;
    public Publication(String titre, String description, String type, int userId,String  datePublication, String imageUri) {
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.userId = userId;
        this.datePublication= datePublication;
        this.imageUri = imageUri != null ? imageUri : "";  // Chaîne vide pour éviter les erreurs
    }

    public Publication() {

    }
    @Ignore
    public Publication(String titre) {
        this.titre = titre;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

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

}

