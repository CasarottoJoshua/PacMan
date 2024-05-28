package com.example.pacman;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class score extends AppCompatActivity
{
    // Dichiarazione variabili
    private Button activity_lobby; // Dichiarazione del bottone per tornare alla lobby
    private RecyclerView recyclerView; // Dichiarazione della RecyclerView
    private AA_recyclerViewAdapter adapter; // Dichiarazione dell'adapter per la RecyclerView
    private ArrayList<PositionModel> righe; // Dichiarazione dell'array di righe per la RecyclerView

    // Array di immagini per le posizioni
    int[] arrayImmaginiPosizioni = {
            R.drawable.numero1, R.drawable.numero2, R.drawable.numero3,
            R.drawable.numero4, R.drawable.numero5, R.drawable.numero6,
            R.drawable.numero7, R.drawable.numero8, R.drawable.numero9, R.drawable.numero10
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");

        // ------------------------------------------------------------------------

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_score);

        // Nascondi la barra di stato e attiva la modalità schermo intero
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Imposta l'orientamento del display su orizzontale
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Rimuovi il bordo attorno alla visualizzazione
        //EdgeToEdge.enable(this);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // ------------------------------------------------------------------------

        recyclerView = findViewById(R.id.mRecyclerView); // Inizializzazione della RecyclerView
        righe = new ArrayList<>(); // Inizializzazione dell'array di righe per la RecyclerView
        adapter = new AA_recyclerViewAdapter(this, righe); // Inizializzazione dell'adapter per la RecyclerView
        recyclerView.setAdapter(adapter); // Impostazione dell'adapter per la RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Impostazione del layout manager per la RecyclerView

        //codice per spostamento a activity_lobby----------------------------------------------------
        activity_lobby = findViewById(R.id.BottoneIndietro);
        activity_lobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(score.this, com.example.pacman.activity_lobby.class);
                intent.putExtra("USERNAME", username); // Passaggio dell'username tramite intent extra
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        //tentativo di ottenere dati dal database per classifica
        getUsersFromServer();
    }

    // Metodo per ottenere i dati dal server
    private void getUsersFromServer()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                // Creazione array per i parametri
                String[] field = new String[0]; // Non servono parametri per questa richiesta
                String[] data = new String[0]; // Non servono parametri per questa richiesta

                // Richiesta al server per ottenere i dati dei punteggi
                PutData putData = new PutData("https://5cijoshuacasarotto.barsanti.edu.it/DBPacMan/ottieniDatiPunteggio.php", "POST", field, data);

                if (putData.startPut())  // Se la richiesta è partita
                {
                    if (putData.onComplete())  // Se la richiesta è completata
                    {
                        String result = putData.getResult(); // Risultato della richiesta

                        Log.d("JSON_RECEIVED", result);  // Log per vedere il risultato JSON

                        if (result != null && result.startsWith("["))  // Se il risultato è un JSON valido
                        {
                            // Conversione del JSON in una lista di oggetti User
                            Gson gson = new Gson();

                            // Creazione di una lista di oggetti User
                            Type userListType = new TypeToken<List<User>>() {}.getType();

                            // Conversione del JSON in una lista di oggetti User
                            List<User> userList = gson.fromJson(result, userListType);

                            Log.d("JSON_RECEIVED", result);  //

                            // Ordinamento della lista in base al punteggio
                            Collections.sort(userList, new Comparator<User>() {
                                @Override
                                public int compare(User u1, User u2) {
                                    return Integer.compare(u2.getHighscore(), u1.getHighscore());
                                }
                            });

                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    righe.clear(); // Pulizia dell'array di righe per la RecyclerView

                                    int numUsers = Math.min(userList.size(), arrayImmaginiPosizioni.length); // Numero di utenti da visualizzare

                                    // Aggiunta delle righe all'array di righe per la RecyclerView
                                    for (int i = 0; i < numUsers; i++)
                                    {
                                        User user = userList.get(i); // Utente corrente
                                        righe.add(new PositionModel( // Aggiunta della riga all'array di righe per la RecyclerView
                                                arrayImmaginiPosizioni[i],
                                                user.getUsername(),
                                                String.valueOf(user.getHighscore()),
                                                user.getTime(),
                                                user.getImage()
                                        ));
                                    }
                                    adapter.notifyDataSetChanged(); // Aggiornamento della RecyclerView
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(getApplicationContext(), "Error: No valid data received", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
            }
        }).start();
    }
}
