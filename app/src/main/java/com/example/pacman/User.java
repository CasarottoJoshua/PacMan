package com.example.pacman;

public class User
{
    // Dichiarazione variabili
    public int id;
    public String username;
    public int highscore;
    public String tempo;
    public String image;

    // Costruttore
    public User(int id, String username, int highscore, String tempo, String image)
    {
        this.id = id;
        this.username = username;
        this.highscore = highscore;
        this.tempo = tempo;
        this.image = image;
    }

    // Metodo get per ottenere l'id
    public int getId()
    {
        return id;
    }

    // Metodo get per ottenere l'username
    public String getUsername()
    {
        return username;
    }

    // Metodo get per ottenere l'highscore
    public int getHighscore()
    {
        return highscore;
    }

    // Metodo get per ottenere il tempo
    public String getTime()
    {
        return tempo;
    }

    // Metodo get per ottenere l'immagine
    public String getImage()
    {
        return image;
    }

    // Metodo set per settare l'id
    public void setId(int id)
    {
        this.id = id;
    }

    // Metodo set per settare l'username
    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    public void setTime(String time) {
        this.tempo = time;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
