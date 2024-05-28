package com.example.pacman;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Ghost
{
    // Dichiarazione delle variabili
    int[][] map; // Mappa del gioco
    int cellSizeRow, cellSizeCol; // Dimensioni delle celle
    int posX, posY; // Posizione del fantasma sull'asse x e y
    int speed;  // Velocità del fantasma
    int color; // Colore del fantasma
    boolean isMoving = false; // Variabile booleana per controllare se il fantasma si sta muovendo
    boolean isStart = false; // Variabile booleana per controllare se il fantasma è in movimento
    int goOutCounter = 0; // Contatore per far uscire il fantasma dalla prigione
    boolean should = false; // Variabile booleana per controllare se il fantasma deve cambiare direzione
    boolean canMoveUp, canMoveDown, canMoveLeft, canMoveRight; // Variabili booleane per controllare se il fantasma può muoversi in una determinata direzione
    Random ran = new Random();  // Variabile Random per generare numeri casuali
    int dir = 0; // Variabile per controllare la direzione del movimento del fantasma

    // Costruttore della classe Ghost
    public Ghost(int color, int[][] map, int cellSizeRow, int cellSizeCol, int speed, int position, int place)
    {
        this.color = color;
        this.map = map;
        this.cellSizeRow = cellSizeRow;
        this.cellSizeCol = cellSizeCol;
        this.speed = speed;
        posX = cellSizeCol * (position + 9);
        posY = cellSizeRow * place;
    }

    // Metodo per disegnare il fantasma
    public void draw(Canvas canvas)
    {
        Paint paint = new Paint(); // Creazione di un oggetto Paint
        paint.setColor(Color.TRANSPARENT); // Impostazione del colore di sfondo
        canvas.drawRect(posX, posY, posX + cellSizeCol, posY + cellSizeRow, paint); // Disegno del rettangolo

        paint.setColor(color); // Impostazione del colore del fantasma
        canvas.drawCircle(posX + cellSizeCol / 2, posY + cellSizeRow / 2, cellSizeCol / 2, paint); // Disegno del cerchio
        canvas.drawRect(posX, posY + (cellSizeRow / 2), posX + cellSizeCol, posY + cellSizeRow, paint); // Disegno del rettangolo
    }

    // Metodo per muovere il fantasma
    public void move(int dx, int dy)
    {
        int newX = posX + dx;
        int newY = posY + dy;

        // Se la mossa è valida, il fantasma si muove
        if (isValidMove(newX, newY) && isValidMove(newX + cellSizeCol - 1, newY + cellSizeRow - 1) && isValidMove(newX, newY + cellSizeRow - 1) && isValidMove(newX + cellSizeCol - 1, newY))
        {
            posX = newX;
            posY = newY;
            isMoving = true;
        }
        else
        {
            isMoving = false;
        }
    }

    // Metodo per controllare se la mossa è valida
    boolean isValidMove(int newX, int newY)
    {
        // Se la nuova posizione è all'interno della mappa e non è un muro, la mossa è valida allora ritorna true
        if (newX >= 0 && newX < map[0].length * cellSizeCol && newY >= 0 && newY < map.length * cellSizeRow)
        {
            int col = newX / cellSizeCol;
            int row = newY / cellSizeRow;

            if (map[row][col] != 1)
            {
                return true;
            }
        }
        return false;
    }

    // Metodo per far uscire il fantasma dalla prigione
    void goOutside()
    {
        // Controllo della presenza di pikini rimanenti
        switch (goOutCounter)
        {
            // Se non ci sono pikini rimanenti, il fantasma esce dalla prigione
            case 0:
                if (!isStart){
                    move(-speed, 0);
                }
                if (!isMoving){
                    isStart = true;
                    goOutCounter++;
                }
                break;
            // Se il fantasma è uscito dalla prigione, il fantasma si muove in una direzione casuale
            case 1:
                if (isStart){
                    move(0, -speed);
                }
                if (!isMoving){
                    isStart = false;
                    goOutCounter++;
                }
                break;
            // Se il fantasma ha raggiunto la fine della prigione, il fantasma si muove in una direzione casuale
            case 2:
                if (!isStart)
                {
                    if (isValidMove(posX, posY - speed) && isValidMove(posX + cellSizeCol - 1, posY + cellSizeRow - 1 - speed) && isValidMove(posX, posY + cellSizeRow - 1 - speed) && isValidMove(posX + cellSizeCol - 1, posY - speed))
                    {
                        move(0, -speed);
                        should = true;
                    }
                    else
                    {
                        if (should)
                        {
                            isStart = true;
                            goOutCounter++;
                        }
                        move(speed, 0);
                    }
                }
                break;
            // Se il fantasma ha raggiunto la fine della prigione, il fantasma si muove in una direzione casuale
            case 3:
                if (isStart)
                {
                    move(-speed, 0);
                }
                if (!isMoving)
                {
                    isStart = false;
                    goOutCounter++;
                }
                break;
        }
    }

    // Metodo per far muovere il fantasma in modo casuale
    void moveRandomly()
    {
        // Controllo della direzione del movimento del fantasma e della validità della mossa
        canMoveUp = isValidMove(posX, posY - speed) && isValidMove(posX + cellSizeCol - 1, posY + cellSizeRow - 1 - speed) && isValidMove(posX, posY + cellSizeRow - 1 - speed) && isValidMove(posX + cellSizeCol - 1, posY - speed);

        // Controllo della direzione del movimento del fantasma e della validità della mossa
        canMoveDown = isValidMove(posX, posY + speed) && isValidMove(posX + cellSizeCol - 1, posY + cellSizeRow - 1 + speed) && isValidMove(posX, posY + cellSizeRow - 1 + speed) && isValidMove(posX + cellSizeCol - 1, posY + speed);

        // Controllo della direzione del movimento del fantasma e della validità della mossa
        canMoveLeft = isValidMove(posX - speed, posY) && isValidMove(posX + cellSizeCol - 1 - speed, posY + cellSizeRow - 1) && isValidMove(posX - speed, posY + cellSizeRow - 1) && isValidMove(posX + cellSizeCol - 1 - speed, posY);

        // Controllo della direzione del movimento del fantasma e della validità della mossa
        canMoveRight = isValidMove(posX + speed, posY) && isValidMove(posX + cellSizeCol - 1 + speed, posY + cellSizeRow - 1) && isValidMove(posX + speed, posY + cellSizeRow - 1) && isValidMove(posX + cellSizeCol - 1 + speed, posY);

        // Controllo della direzione del movimento del fantasma
        switch (dir)
        {
            // Se la direzione è 0, il fantasma si muove verso l'alto
            case 0:
                if (canMoveUp)
                {
                    move(0, -speed);
                }
                else
                {
                    dir = ran.nextInt(4);
                }
                break;

            // Se la direzione è 1, il fantasma si muove verso il basso
            case 1:
                if (canMoveDown)
                {
                    move(0, speed);
                }
                else
                {
                    dir = ran.nextInt(4);
                }
                break;

            // Se la direzione è 2, il fantasma si muove a sinistra
            case 2:
                if (canMoveLeft)
                {
                    move(-speed, 0);
                }
                else
                {
                    dir = ran.nextInt(4);
                }
                break;

            // Se la direzione è 3, il fantasma si muove a destra
            case 3:
                if (canMoveRight)
                {
                    move(speed, 0);
                }
                else
                {
                    dir = ran.nextInt(4);
                }
                break;
        }
    }
}