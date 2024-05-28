package com.example.pacman;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class schermata6 extends AppCompatActivity
{
    // Dichiarazione variabili
    private Button schermata1;
    private Button schermata7;
    private Button bottoneSicurezza;
    TextInputEditText textInputEditTextUsername;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //Recupero l'email della pagina precedente
        Intent intent = getIntent();
        String email = intent.getStringExtra("EMAIL");

        // Nascondi la barra di stato e attiva la modalità schermo intero
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Imposta l'orientamento del display su orizzontale
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Rimuovi il bordo attorno alla visualizzazione
        //EdgeToEdge.enable(this);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Imposta il layout della tua attività
        setContentView(R.layout.activity_schermata1);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schermata6);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //codice per spostamento a schermata1----------------------------------------------------
        schermata1 = findViewById(R.id.BottoneX);
        schermata1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(schermata6.this, schermata1.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        //codice per spostamento a schermata7----------------------------------------------------
        textInputEditTextUsername = findViewById(R.id.username);
        schermata7 = findViewById(R.id.BottoneContinua6);
        schermata7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String username = textInputEditTextUsername.getText().toString().trim(); //Recupero il testo inserito

                if (!username.isEmpty()) //Se il campo non è vuoto
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {

                            // Creazione array per i campi da inviare
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "email";

                            // Creazione array per i dati da inviare
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = email;

                            // PutData per inviare i dati al server tramite file PHP
                            PutData putData = new PutData("https://5cijoshuacasarotto.barsanti.edu.it/DBPacMan/update.php", "POST", field, data);

                            if (putData.startPut()) //Inizio invio dati
                            {
                                if (putData.onComplete())  //Se il collegamento è completato
                                {
                                    String result = putData.getResult();
                                    if (result.equals("Username aggiornato con successo!")) //Se l'username è stato aggiornato con successo
                                    {
                                        Intent intent = new Intent(schermata6.this, schermata7.class);
                                        intent.putExtra("EMAIL", email);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    });
                }
                else
                {
                    //Se manca anche solo un componente == errore
                    Toast.makeText(getApplicationContext(),"Tutti i componenti richiesti", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //codice per aprire il link di sicurezza----------------------------------------------------
        bottoneSicurezza = findViewById(R.id.sicurezza);
        bottoneSicurezza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Link del documento di sicurezza di Google
                String url = "https://docs.google.com/document/d/1LHaFbSawAPLDmzn8b8-VjXbh76LJX4S3ULlQMA2zZss/edit?usp=sharing";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }
}