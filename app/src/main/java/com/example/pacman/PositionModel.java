package com.example.pacman;

//Model per la gestione della posizione
public class PositionModel
{
    // Dichiarazione delle variabili
    private int position; // Posizione
    private String image; // Immagine
    private String punteggio; // Punteggio
    private String tempo; // Tempo
    private String username; // Username

    // Costruttore
    public PositionModel(int position, String username, String punteggio, String tempo, String image)
    {
        this.position = position;
        this.image = image;
        this.punteggio = punteggio;
        this.tempo = tempo;
        this.username = username;
    }

    // Metodo get per ottenere la posizione
    public int getPosition()
    {
        return position;
    }

    // Metodo get per ottenere l'immagine
    public String getImage()
    {
        return image;
    }

    // Metodo get per ottenere il punteggio
    public String getPunteggio()
    {
        return punteggio;
    }

    // Metodo get per ottenere il tempo
    public String getTempo()
    {
        return tempo;
    }

    // Metodo get per ottenere l'username
    public String getUsername()
    {
        return username;
    }
}
