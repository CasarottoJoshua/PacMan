package com.example.pacman;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

public class Pacman {
    // Dichiarazione delle variabili
    Context context; // Contesto dell'applicazione
    int[][] map; // Mappa del gioco
    int cellSizeRow, cellSizeCol; // Dimensioni della cella
    int posX, posY; // Posizione di Pacman sulla mappa (in pixel)
    int speed;  // Velocità di Pacman
    MediaPlayer mp; // Suono di Pacman
    Handler uiHandler; // Handler per l'aggiornamento dell'interfaccia utente
    boolean isSoundPlaying; // Flag per verificare se il suono è in riproduzione

    public Pacman(Context context, int[][] map, int cellSizeRow, int cellSizeCol, int speed) {
        this.context = context;
        this.map = map;
        this.cellSizeRow = cellSizeRow;
        this.cellSizeCol = cellSizeCol;
        this.speed = speed;
        posX = cellSizeCol * 10;
        posY = cellSizeRow * 15;
        this.uiHandler = new Handler(Looper.getMainLooper());
        mp = MediaPlayer.create(context, R.raw.pacman_wacca_v6);
        isSoundPlaying = false;

        // Listener per sapere quando il suono finisce
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // Aggiunge una pausa di 200ms prima di permettere di mangiare un altro pellet
                uiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isSoundPlaying = false;
                    }
                }, 50);
            }
        });
    }

    // Metodo per disegnare Pacman sulla mappa
    public void draw(Canvas canvas) {
        Paint paint = new Paint(); // Pennello per disegnare Pacman
        paint.setColor(Color.TRANSPARENT); // Imposta il colore del pennello su trasparente
        canvas.drawRect(posX, posY, posX + cellSizeCol, posY + cellSizeRow, paint); // Disegna un rettangolo trasparente

        paint.setColor(Color.YELLOW); // Imposta il colore del pennello su giallo
        canvas.drawCircle(posX + cellSizeCol / 2, posY + cellSizeRow / 2, cellSizeCol / 2, paint); // Disegna un cerchio giallo
    }

    // Metodo per muovere Pacman sulla mappa
    public void move(int dx, int dy) {
        int newX = posX + dx;
        int newY = posY + dy;

        // Controlla se la nuova posizione è valida
        if (isValidMove(newX, newY) && isValidMove(newX + cellSizeCol - 1, newY + cellSizeRow - 1) && isValidMove(newX, newY + cellSizeRow - 1) && isValidMove(newX + cellSizeCol - 1, newY)) {
            posX = newX;
            posY = newY;
            consumePellet(newX + cellSizeCol / 2, newY + cellSizeRow / 2);
        }
    }

    // Metodo per controllare se la mossa è valida
    private boolean isValidMove(int x, int y) {
        int col = x / cellSizeCol;
        int row = y / cellSizeRow;
        return map[row][col] != 1;
    }

    // Metodo per consumare un pellet
    private void consumePellet(int centerX, int centerY) {
        int pacmanRow = centerY / cellSizeRow;
        int pacmanCol = centerX / cellSizeCol;

        // Controlla se Pacman ha mangiato un pellet
        if (map[pacmanRow][pacmanCol] == 2) {
            if (!isSoundPlaying) {
                map[pacmanRow][pacmanCol] = 0; // Rimuovi il pellet dalla mappa
                isSoundPlaying = true;

                // Esegui il suono e aggiorna il punteggio in un thread separato
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Riproduce il suono del mangiare
                        mp.start();

                        // Aggiorna il punteggio
                        ((activity_play) context).incrementScore();
                    }
                });
            }
        }
    }
}
