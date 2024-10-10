package com.example.mobileappproject_tekdev_5sae3;

public class Annonce {
    private String titre;
    private String description;
    private String prix;
    private String urlImage;

    public Annonce(String titre, String description, String prix, String urlImage) {
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.urlImage = urlImage;
    }

    public String getTitre() {
        return titre;
    }

    public String getDescription() {
        return description;
    }

    public String getPrix() {
        return prix;
    }

    public String getUrlImage() {
        return urlImage;
    }
}
