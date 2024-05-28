package com.example.pacman;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class schermata1 extends AppCompatActivity
{
    // Dichiarazione variabili
    private Button schermata2;
    private Button schermata8;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Nascondi la barra di stato e attiva la modalità schermo intero
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Imposta l'orientamento del display su orizzontale
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Rimuovi il bordo attorno alla visualizzazione
        //EdgeToEdge.enable(this);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Imposta il layout della tua attività
        setContentView(R.layout.activity_schermata1)  ;

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Ottieni l'intent che ha avviato questa attività
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("REGISTRATION_COMPLETE", false))
        {
            // La registrazione è stata completata se l'intent non è nullo e contiene il valore "REGISTRATION_COMPLETE"
            Toast.makeText(this, "Registrazione completata!", Toast.LENGTH_LONG).show();
        }

        setContentView(R.layout.activity_schermata1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //codice per spostamento a schermata2----------------------------------------------------
        schermata2 = findViewById(R.id.BottoneRegistrazione);
        schermata2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(schermata1.this, schermata2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        //codice per spostamento a schermata8----------------------------------------------------
        schermata8 = findViewById(R.id.BottoneAccesso);
        schermata8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(schermata1.this, schermata8.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}