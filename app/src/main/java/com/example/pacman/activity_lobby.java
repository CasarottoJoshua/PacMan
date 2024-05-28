package com.example.pacman;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.WindowCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class activity_lobby extends AppCompatActivity {

    // Dichiarazione della variabile MediaPlayer
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        // Recupera l'username passato dall'activity precedente
        Intent intent = getIntent();
        String username = intent.getStringExtra("USERNAME");

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Nascondi la barra di stato e attiva la modalità schermo intero
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Imposta l'orientamento del display su orizzontale
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Rimuovi il bordo attorno alla visualizzazione
        //EdgeToEdge.enable(this);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Disabilita la modalità notturna
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Inizializzazione del MediaPlayer
        mp = MediaPlayer.create(this, R.raw.pacman_intro);

        // Impostazione del volume del MediaPlayer
        ImageView iv1 = findViewById(R.id.iv1);

        // Caricamento dell'immagine gif
        Glide.with(this)
                .asGif()
                .load(R.drawable.pacman)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(iv1);

        // Dichiarazione dei pulsanti
        Button play = findViewById(R.id.btnPlay);
        Button informazioni = findViewById(R.id.btnInformazioni);
        Button punteggio = findViewById(R.id.btnPunteggio);
        Button esci = findViewById(R.id.btnEsci);

        // Passaggio dell'username tramite intent extra da activity_lobby a activity_play
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_lobby.this, activity_play.class);
                intent.putExtra("USERNAME", username); // Passaggio dell'username tramite intent extra
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // Passaggio da activity_lobby a activity_information
        informazioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_lobby.this, activity_information.class);
                intent.putExtra("USERNAME", username); // Passaggio dell'username tramite intent extra
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // Passaggio da activity_lobby a score
        punteggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("activity_lobby", "btnPunteggio clicked");
                Intent intent = new Intent(activity_lobby.this, score.class);
                intent.putExtra("USERNAME", username); // Passaggio dell'username tramite intent extra
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // Passaggio da activity_lobby a schermata1
        esci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(activity_lobby.this, schermata1.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    // Metodo per la riproduzione del suono
    @Override
    protected void onResume() {
        super.onResume();
        Fullscreen.enableFullscreen(this);
        mp.start();
    }

    // Metodo per la pausa del suono
    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
    }
}