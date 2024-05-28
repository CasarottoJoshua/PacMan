package com.example.pacman;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Dichiarazione della classe activity_play
public class activity_play extends AppCompatActivity implements Runnable
{
    // Dichiarazione delle variabili
    int score = 0; // Punteggio
    int highscoreValue = 0; // highscore attuale
    int vecchioHighscore = 0; // highscore precedente
    TextView punteggio, highscore; // Punteggio e highscore
    private Handler uiHandler; // Handler per l'aggiornamento dell'interfaccia utente
    int fps = 60; // Frame per secondo
    Thread thread; // Thread per il ciclo di gioco
    SurfaceHolder surfaceHolder; // Gestione della superficie
    SurfaceView sv; // Visualizzazione della superficie
    boolean running; // Flag per il controllo del ciclo di gioco
    int drawCount; // Contatore dei disegni
    int cellSizeRow, cellSizeCol; // Dimensioni delle celle
    int[][] map = { // Mappa del gioco
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,2,1,1,1,1,1,2,1,2,1,1,1,2,1},
            {1,2,2,2,2,2,1,2,2,2,1,2,2,2,1,2,2,2,2,2,1},
            {1,1,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,1,1},
            {0,0,0,0,1,2,1,2,2,2,2,2,2,2,1,2,1,0,0,0,0},
            {1,1,1,1,1,2,1,2,1,1,0,1,1,2,1,2,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,1,0,0,0,1,2,2,2,2,2,2,2,1},
            {1,1,1,1,1,2,1,2,1,0,1,0,1,2,1,2,1,1,1,1,1},
            {0,0,0,0,1,2,1,2,1,0,0,0,1,2,1,2,1,0,0,0,0},
            {1,1,1,1,1,2,1,2,1,1,1,1,1,2,1,2,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,1,2,1},
            {1,2,2,2,1,2,2,2,2,2,0,2,2,2,2,2,1,2,2,2,1},
            {1,1,1,2,1,2,1,2,1,1,1,1,1,2,1,2,1,2,1,1,1},
            {1,2,2,2,2,2,1,2,2,2,1,2,2,2,1,2,2,2,2,2,1},
            {1,2,1,1,1,1,1,1,1,2,1,2,1,1,1,1,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };
    int rows = 21, cols = 21; // Numero di righe e colonne della mappa
    int width; // Larghezza dello schermo
    Pacman pacman; // Pacman
    static final int mup = 1, mdown = 2, mleft = 3, mright = 4; // Direzioni del movimento
    int direction = 0, pendingDirection = 0; // Direzione attuale e direzione in attesa
    boolean isGameFinished = false; // Flag per il controllo del gioco
    int seconds = 0, minutes = 0, hours = 0;   // Timer
    int randomDirection = 0; // Direzione casuale
    Ghost g1, g2, g3, g4; // Fantasmi
    boolean gameOver = false; // Flag per il controllo del game over
    ImageButton up, down, left, right; // Pulsanti per il movimento
    TextView timerTV; // Timer

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // Inizializzazione dell'handler per l'aggiornamento dell'interfaccia utente
        uiHandler = new Handler(Looper.getMainLooper());

        // Inizializzazione del punteggio e dell'highscore
        punteggio = findViewById(R.id.punteggio);
        highscore = findViewById(R.id.highscore);

        // Impostazione dell'highscore a 0
        highscore.setText(String.valueOf(highscoreValue));

        // Ottieni l'highscore dal database
        ottieniHighscore();

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Nascondi la barra di stato e attiva la modalità schermo intero
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Imposta l'orientamento del display su orizzontale
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Rimuovi il bordo attorno alla visualizzazione
        //EdgeToEdge.enable(this);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        DisplayMetrics displayMetrics = new DisplayMetrics(); // Inizializzazione di displayMetrics
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics); // Impostazione delle dimensioni dello schermo
        width = displayMetrics.heightPixels; // Impostazione della larghezza dello schermo
        cellSizeRow = width / cols; // Impostazione della dimensione delle celle per le righe
        cellSizeCol = width / rows; // Impostazione della dimensione delle celle per le colonne

        // Controllo della dimensione delle celle
        if (cellSizeRow % 2 != 0)
        {
            // Decremento della dimensione delle celle per le righe
            cellSizeRow--;
        }

        // Controllo della dimensione delle celle
        if (cellSizeCol % 2 != 0)
        {
            // Decremento della dimensione delle celle per le colonne
            cellSizeCol--;
        }

        // Creazione dell'interfaccia utente
        sv = findViewById(R.id.sv);
        sv.getHolder().setFixedSize(cellSizeCol * cols, cellSizeRow * rows); // Impostazione della dimensione dello schermo
        surfaceHolder = sv.getHolder(); // Impostazione della superficie
        sv.setFocusable(true); // Impostazione del focus

        // Inizializzazione dei pulsanti per il movimento e del timer
        up = findViewById(R.id.up);
        down = findViewById(R.id.down);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        timerTV = findViewById(R.id.timer);

        // Impostazione di up, down, left e right come pulsanti per il movimento
        up.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Controllo della direzione
                if (isValidDirection(mup))
                {
                    // Impostazione della direzione
                    direction = mup; // Direzione verso l'alto
                }
                else
                {
                    // Impostazione della direzione in attesa
                    pendingDirection = mup; // Direzione in attesa verso l'alto
                }
            }
        });

        down.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Controllo della direzione
                if (isValidDirection(mdown))
                {
                    // Impostazione della direzione
                    direction = mdown; // Direzione verso il basso
                }
                else
                {
                    // Impostazione della direzione in attesa
                    pendingDirection = mdown; // Direzione in attesa verso il basso
                }
            }
        });

        left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Controllo della direzione
                if (isValidDirection(mleft))
                {
                    // Impostazione della direzione
                    direction = mleft; // Direzione verso sinistra
                }
                else
                {
                    // Impostazione della direzione in attesa
                    pendingDirection = mleft; // Direzione in attesa verso sinistra
                }
            }
        });

        right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Controllo della direzione
                if (isValidDirection(mright))
                {
                    // Impostazione della direzione
                    direction = mright; // Direzione verso destra
                }
                else
                {
                    // Impostazione della direzione in attesa
                    pendingDirection = mright; // Direzione in attesa verso destra
                }
            }
        });

        // Inizializzazione di pacman e dei fantasmi
        pacman = new Pacman(this, map, cellSizeRow, cellSizeCol, 2);
        g1 = new Ghost(Color.RED, map, cellSizeRow, cellSizeCol, 1, 1, 9);
        g2 = new Ghost(Color.rgb(228, 142, 183), map, cellSizeRow, cellSizeCol, 2, 1, 11);
        g3 = new Ghost(Color.CYAN, map, cellSizeRow, cellSizeCol, 1, 0, 11);
        g4 = new Ghost(Color.rgb(235, 125, 0), map, cellSizeRow, cellSizeCol, 1, 2, 11);
    }

    // Metodo per l'attivazione della modalità schermo intero
    @Override
    protected void onResume()
    {
        super.onResume(); // Richiama il metodo della superclasse
        Fullscreen.enableFullscreen(this); // Attiva la modalità schermo intero
        running = true; // Imposta il flag running a true
        thread = new Thread(this); // Inizializza il thread
        thread.start(); // Avvia il thread
    }

    // Metodo per la disattivazione della modalità schermo intero
    @Override
    protected void onPause()
    {
        super.onPause(); // Richiama il metodo della superclasse
        running = false; // Imposta il flag running a false

        // Gestione dell'eccezione
        try
        {
            // Attende la fine del thread
            thread.join();
        }
        catch (InterruptedException e)
        {
            // Stampa l'errore
            e.printStackTrace();
        }
    }

    // Metodo per l'aggiornamento del punteggio
    public void incrementScore()
    {
        // Incremento del punteggio di 10
        score += 10;
        updateScore();
    }

    // Metodo per l'aggiornamento del punteggio
    void updateScore()
    {
        uiHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                // Controllo e aggiornamento dell'interfaccia utente
                punteggio.setText(String.valueOf(score));

                // Controlla e aggiorna l'highscore
                if (score > highscoreValue)
                {
                    highscoreValue = score;
                    highscore.setText(String.valueOf(highscoreValue));
                }
            }
        });
    }

    // Metodo per l'aggiornamento del gioco
    void update()
    {
        // Controlla se il gioco è finito
        if (isGameFinished)
        {
            return;
        }

        // Controllo della direzione
        if (pendingDirection != 0 && isValidDirection(pendingDirection))
        {
            direction = pendingDirection;
            pendingDirection = 0;
        }

        // Controllo della direzione
        pacmanTouchGhost(g1);
        pacmanTouchGhost(g2);
        pacmanTouchGhost(g3);
        pacmanTouchGhost(g4);

        // Controllo della direzione
        switch (direction)
        {
            // Direzione verso l'alto
            case 1:
                pacman.move(0, -pacman.speed);
                break;
            // Direzione verso il basso
            case 2:
                pacman.move(0, pacman.speed);
                break;
            // Direzione verso sinistra
            case 3:
                pacman.move(-pacman.speed, 0);
                break;
            // Direzione verso destra
            case 4:
                pacman.move(pacman.speed, 0);
                break;
            // Nessuna direzione
            default:
                break;
        }

        // Tramite un contatore, il fantasma 1 esce dalla prigione
        if (seconds > 2 && seconds < 18){
            if (g1.goOutCounter != 4){
                g1.goOutside();
            }
        }

        // Tramite un contatore, il fantasma 2 esce dalla prigione
        if (seconds > 7 && seconds < 23){
            if (g2.goOutCounter != 4){
                g2.goOutside();
            }
        }

        // Tramite un contatore, il fantasma 3 esce dalla prigione
        if (seconds > 12 && seconds < 28){
            if (g3.goOutCounter != 4){
                g3.goOutside();
            }
        }

        // Tramite un contatore, il fantasma 4 esce dalla prigione
        if (seconds > 17 && seconds < 33){
            if (g4.goOutCounter != 4){
                g4.goOutside();
            }
        }

        // Movimento casuale dei fantasmi
        if (g1.goOutCounter == 4)
        {
            g1.moveRandomly();
        }
        if (g2.goOutCounter == 4)
        {
            g2.moveRandomly();
        }
        if (g3.goOutCounter == 4)
        {
            g3.moveRandomly();
        }
        if (g4.goOutCounter == 4)
        {
            g4.moveRandomly();
        }
    }

    // Metodo per l'invio del punteggio al database
    public void ottieniHighscore()
    {
        // Ottieni l'username
        Intent intent = getIntent();
        final String username = intent.getStringExtra("USERNAME");

        if (!isNetworkAvailable()) // Controllo della connessione a Internet
        {
            Toast.makeText(this, "Nessuna connessione a Internet disponibile", Toast.LENGTH_SHORT).show();
            return;
        }

        new RetrieveHighscoreTask().execute(username); // Esecuzione del task per l'ottenimento dell'highscore
    }

    private boolean isNetworkAvailable()  // Metodo per il controllo della connessione a Internet
    {
        // Controllo della connessione a Internet per la gestione del gmail
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Metodo per l'invio del punteggio al database
    private class RetrieveHighscoreTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String result = null;
            try {
                URL url = new URL("https://5cijoshuacasarotto.barsanti.edu.it/DBPacMan/ottieniHighscore.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write("username=" + URLEncoder.encode(username, "UTF-8"));
                writer.flush();
                writer.close();
                os.close();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder resultBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    resultBuilder.append(line);
                }
                result = resultBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                highscoreValue = Integer.parseInt(result);
                highscore.setText(String.valueOf(highscoreValue));
                vecchioHighscore = Integer.parseInt(result);
            } else {
                Log.d("HighscoreTask", "Errore di connessione al server");
            }
        }
    }

/*
    void updateScore() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Integer.parseInt(punteggio.getText().toString()) != score) {
                    punteggio.setText(String.valueOf(score));

                    // Controlla e aggiorna l'highscore
                    if (score > highscoreValue) {
                        highscoreValue = score;
                        highscore.setText(String.valueOf(highscoreValue));
                    }
                }
            }
        });
    }

 */

    // Metodo per il controllo del contatto tra pacman e i fantasmi
    void pacmanTouchGhost(Ghost ghost)
    {
        // Controllo del contatto tra pacman e i fantasmi in base alla loro posizione
        if (pacman.posX >= ghost.posX && pacman.posX <= ghost.posX + cellSizeCol
                && pacman.posY >= ghost.posY && pacman.posY <= ghost.posY + cellSizeRow)
        {
            gameOver = true; // se pacman collide con un fantasma, game over
        }

        // Controllo del contatto tra pacman e i fantasmi in base alla loro posizione
        if (pacman.posX + pacman.cellSizeCol >= ghost.posX && pacman.posX + pacman.cellSizeCol <= ghost.posX + cellSizeCol
                && pacman.posY + pacman.cellSizeRow >= ghost.posY && pacman.posY + pacman.cellSizeRow <= ghost.posY + cellSizeRow)
        {
            gameOver = true; // se pacman collide con un fantasma, game over
        }
    }

    // Metodo per il controllo della direzione valida
    boolean isValidDirection(int direction)
    {
        // Controllo della direzione
        switch (direction)
        {
            // Direzione verso l'alto
            case 1:
                return isValidMoveTL(0, -pacman.speed) &&
                        isValidMoveTR(0, -pacman.speed) &&
                        isValidMoveBL(0, -pacman.speed) &&
                        isValidMoveBR(0, -pacman.speed);
            // Direzione verso il basso
            case 2:
                return isValidMoveTL(0, pacman.speed) &&
                        isValidMoveTR(0, pacman.speed) &&
                        isValidMoveBL(0, pacman.speed) &&
                        isValidMoveBR(0, pacman.speed);
            // Direzione verso sinistra
            case 3:
                return isValidMoveTL(-pacman.speed, 0) &&
                        isValidMoveTR(-pacman.speed, 0) &&
                        isValidMoveBL(-pacman.speed, 0) &&
                        isValidMoveBR(-pacman.speed, 0);
            // Direzione verso destra
            case 4:
                return isValidMoveTL(pacman.speed, 0) &&
                        isValidMoveTR(pacman.speed, 0) &&
                        isValidMoveBL(pacman.speed, 0) &&
                        isValidMoveBR(pacman.speed, 0);
            // Nessuna direzione
            default:
                break;
        }
        return false;
    }

    // Metodo per il controllo del movimento valido in alto a sinistra
    boolean isValidMoveTL(int x, int y)
    {
        int newX = pacman.posX + x;
        int newY = pacman.posY + y;

        return isValidMove(newX, newY);
    }

    // Metodo per il controllo del movimento valido in alto a destra
    boolean isValidMoveTR(int x, int y)
    {
        int newX = pacman.posX + x + cellSizeCol;
        int newY = pacman.posY + y;

        return isValidMove(newX - 1, newY);
    }

    // Metodo per il controllo del movimento valido in basso a sinistra
    boolean isValidMoveBL(int x, int y) {
        int newX = pacman.posX + x;
        int newY = pacman.posY + y + cellSizeRow;

        return isValidMove(newX, newY - 1);
    }

    // Metodo per il controllo del movimento valido in basso a destra
    boolean isValidMoveBR(int x, int y) {
        int newX = pacman.posX + x + cellSizeCol;
        int newY = pacman.posY + y + cellSizeRow;

        return isValidMove(newX - 1, newY - 1);
    }

    // Metodo per il controllo del movimento valido in base alla posizione
    boolean isValidMove(int newX, int newY)
    {
        // se la nuova posizione è valida e non è un muro
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

    // Metodo per il disegno della mappa
    void drawMap(Canvas canvas)
    {
        // Disegno della mappa
        Paint paint = new Paint();

        // Ciclo per il disegno della mappa
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                // Controllo della cella
                int cellType = map[row][col];

                // Disegno della cella
                if (cellType == 1)
                {
                    // Disegno del muro
                    paint.setColor(Color.BLUE);
                    int left = col * cellSizeCol;
                    int top = row * cellSizeRow;
                    int right = left + cellSizeCol;
                    int bottom = top + cellSizeRow;
                    canvas.drawRect(left, top, right, bottom, paint);
                }
                else if (cellType == 2)
                {
                    // Disegno del pallino
                    paint.setColor(Color.WHITE);
                    int centerX = col * cellSizeCol + cellSizeCol / 2;
                    int centerY = row * cellSizeRow + cellSizeRow / 2;
                    int radius = cellSizeCol / 6;
                    canvas.drawCircle(centerX, centerY, radius, paint);
                }
            }
        }
    }

    // Metodo per il controllo della presenza di pallini rimanenti
    boolean arePelletsRemaining()
    {
        // Controllo della presenza di pallini rimanenti
        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                if (map[row][col] == 2)
                {
                    return true;
                }
            }
        }
        return false;
    }

    // Metodo per il settaggio del timer
    String setTimer() {
        String sec, min, hr;

        // Se i secondi sono 59
        if (seconds == 59) {
            // Resetta i secondi e incrementa i minuti
            seconds = 0;
            minutes++;
        } else { // Altrimenti incrementa i secondi
            seconds++;
        }

        // Se i minuti sono 60
        if (minutes == 60) {
            // Resetta i minuti e incrementa le ore
            minutes = 0;
            hours++;
        }

        // Formatta i secondi
        if (seconds < 10) {
            sec = "0" + seconds;
        } else {
            sec = String.valueOf(seconds);
        }

        // Formatta i minuti
        if (minutes < 10) {
            min = "0" + minutes;
        } else {
            min = String.valueOf(minutes);
        }

        // Formatta le ore
        if (hours < 10) {
            hr = "0" + hours;
        } else {
            hr = String.valueOf(hours);
        }

        // Ritorna il tempo trascorso
        return hr + ":" + min + ":" + sec;
    }


    // Metodo per il disegno del gioco
    @Override
    public void run()
    {
        // Ottieni l'username dall'intent
        Intent intent = getIntent();
        final String username = intent.getStringExtra("USERNAME");

        // Inizializzazione delle variabili
        double interval = 1000000000.0 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long cTime;
        long timer = 0;
        drawCount = 0;

        // Ciclo di gioco
        while (running)
        {
            // Controllo della validità della superficie
            if (!surfaceHolder.getSurface().isValid())
            {
                continue;
            }

            // Controllo del tempo trascorso
            cTime = System.nanoTime();
            delta += (cTime - lastTime) / interval;
            timer += (cTime - lastTime);
            lastTime = cTime;

            // Controllo del delta
            if (delta > 1)
            {
                update();
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas != null)  // Controllo della validità della superficie
                {
                    canvas.drawColor(Color.BLACK); // Disegno dello sfondo

                    if (!gameOver) // Controllo del gioco
                    {
                        if (!arePelletsRemaining()) // Se non ci sono più pallini
                        {
                            if (!isGameFinished) // Se il gioco non è finito
                            {
                                Handler h = new Handler(Looper.getMainLooper()); // Inizializzazione dell'handler
                                h.post(new Runnable()  // Esecuzione del task
                                {
                                    // Metodo run
                                    @Override
                                    public void run()
                                    {
                                        seconds--; // Decremento dei secondi

                                        // Personalizzazione del dialog
                                        AlertDialog.Builder adb = new AlertDialog.Builder(activity_play.this);
                                        adb.setTitle("Hai vinto!");
                                        adb.setMessage("Gioco completato!\n\nTempo trascorso: " + setTimer());
                                        adb.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                                sendScoreToDatabase(score, setTimer());
                                                Intent intent = new Intent(activity_play.this, activity_lobby.class);
                                                intent.putExtra("USERNAME", username); // Passaggio dell'username tramite intent extra
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                startActivity(intent);
                                            }
                                        });

                                        // Personalizzazione del colore del dialog
                                        AlertDialog ad = adb.create();
                                        ad.setOnShowListener(new DialogInterface.OnShowListener() {
                                            @Override
                                            public void onShow(DialogInterface dialog) {
                                                ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GREEN);
                                            }
                                        });
                                        ad.show();
                                    }
                                });
                                isGameFinished = true; // Impostazione del flag isGameFinished a true
                            }
                        }
                        else // Altrimenti disegna la mappa e i personaggi
                        {
                            drawMap(canvas);
                            g1.draw(canvas);
                            g2.draw(canvas);
                            g3.draw(canvas);
                            g4.draw(canvas);
                            pacman.draw(canvas);
                        }
                    }
                    else // Altrimenti mostra il dialog del game over
                    {
                        if (!isGameFinished) // Se il gioco non è finito
                        {
                            Handler h = new Handler(Looper.getMainLooper()); // Inizializzazione dell'handler
                            h.post(new Runnable()  // Esecuzione del task
                            {
                                // Metodo run
                                @Override
                                public void run()
                                {
                                    seconds--; // Decremento dei secondi

                                    // Personalizzazione del dialog
                                    AlertDialog.Builder adb = new AlertDialog.Builder(activity_play.this);
                                    adb.setTitle("Hai perso!");
                                    adb.setMessage("Hai colpito un fantasma!\n\nTempo trascorso: " + setTimer());
                                    adb.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                            sendScoreToDatabase(score, setTimer());
                                            Intent intent = new Intent(activity_play.this, activity_lobby.class);
                                            intent.putExtra("USERNAME", username); // Passaggio dell'username tramite intent extra
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
                                        }
                                    });

                                    // Personalizzazione del colore del dialog
                                    AlertDialog ad = adb.create();
                                    ad.setOnShowListener(new DialogInterface.OnShowListener() {
                                        @Override
                                        public void onShow(DialogInterface dialog) {
                                            ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED); // Modifica del colore del testo del pulsante
                                        }
                                    });
                                    ad.show();
                                }
                            });
                            isGameFinished = true; // Impostazione del flag isGameFinished a true
                        }
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas); // Sblocco della superficie
                }
                delta--; // Decremento del delta
                drawCount++; // Incremento del contatore dei disegni
            }

            // Controllo del timer
            if (timer >= 1000000000)
            {
                Handler h = new Handler(Looper.getMainLooper()); // Inizializzazione dell'handler
                h.post(new Runnable() // Esecuzione del task
                {
                    @Override
                    public void run()
                    {
                        if (!isGameFinished) // Se il gioco non è finito
                        {
                            timerTV.setText(setTimer()); // Impostazione del timer nella textview
                        }
                    }
                });
                randomDirection = (int) (Math.random() * 4); // Generazione di una direzione casuale
                drawCount = 0; // Reset del contatore dei disegni
                timer = 0; // Reset del timer
            }
        }
    }

    // Metodo per l'invio del punteggio al database
    void sendScoreToDatabase(int score, String tempo)
    {
        // Ottieni l'username dall'intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");

        // Stampa del punteggio, dell'username e del tempo nel logcat
        Log.d("sendScoreToDatabase", "Username: " + username);
        Log.d("sendScoreToDatabase", "Score: " + score);
        Log.d("sendScoreToDatabase", "Tempo: " + tempo);

        // Aggiornamento del punteggio nel database
        if (score > vecchioHighscore)
        {
            updateScore(username, String.valueOf(score), tempo);
        }
    }

    // Metodo per l'aggiornamento del punteggio nel database
    private void updateScore(final String username, final String score, final String tempo)
    {
        // Aggiornamento del punteggio nel database
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    // Connessione al database
                    URL url = new URL("https://5cijoshuacasarotto.barsanti.edu.it/DBPacMan/aggiornaPunteggio.php");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    // Impostazione dei parametri della richiesta
                    String postData = "username=" + URLEncoder.encode(username, "UTF-8") +
                            "&score=" + URLEncoder.encode(score, "UTF-8") +
                            "&tempo=" + URLEncoder.encode(tempo, "UTF-8");

                    // Scrittura dei parametri della richiesta
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(postData);
                    writer.flush();
                    writer.close();
                    os.close();

                    // Lettura della risposta
                    InputStream in = new BufferedInputStream(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;

                    // Ciclo di lettura
                    while ((line = reader.readLine()) != null)
                    {
                        result.append(line); // Aggiunta della linea al risultato
                    }
                    final String response = result.toString(); // Impostazione della risposta

                    // Aggiornamento dell'interfaccia utente
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            // Stampa della risposta nel logcat
                            Log.d("UpdateScoreTask", "Response: " + response);
                        }
                    });

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}