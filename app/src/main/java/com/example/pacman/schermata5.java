package com.example.pacman;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class schermata5 extends AppCompatActivity
{
    // Dichiarazione variabili
    private Button schermata1;
    private Button schermata6;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Recupero l'email e il flag dalla pagina precedente
        Intent intent = getIntent();
        String email = intent.getStringExtra("EMAIL");
        boolean isGoogleSignIn = intent.getBooleanExtra("IS_GOOGLE_SIGNIN", false);

        // Nascondi la barra di stato e attiva la modalità schermo intero
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Imposta l'orientamento del display su orizzontale
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Rimuovi il bordo attorno alla visualizzazione
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Imposta il layout della tua attività
        setContentView(R.layout.activity_schermata5);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /*
        // Controlla se l'email è presente e se l'utente ha effettuato l'accesso con Google
        if (email != null && !email.isEmpty() && isGoogleSignIn) {
            // Se l'utente ha effettuato l'accesso con Google, passa automaticamente a schermata7
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(schermata5.this, schermata7.class);
                    intent.putExtra("EMAIL", email);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                }
            }, 0); // Delay di 0 secondi per passare immediatamente a schermata7
        }

         */

        // Codice per spostamento a schermata1----------------------------------------------------
        schermata1 = findViewById(R.id.BottoneX);
        schermata1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(schermata5.this, schermata1.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // Codice per spostamento a schermata6----------------------------------------------------
        Button schermata6Button = findViewById(R.id.BottoneOk5);
        schermata6Button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Controlla se l'email è presente e se l'utente ha effettuato l'accesso con Google
                if (email != null && !email.isEmpty() && isGoogleSignIn)
                {
                    // Se l'utente ha effettuato l'accesso con Google, passa automaticamente a schermata7
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(schermata5.this, schermata7.class);
                            intent.putExtra("EMAIL", email);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            finish();
                        }
                    }, 0); // Delay di 0 secondi per passare immediatamente a schermata7
                }
                else
                {
                    Intent intent = new Intent(schermata5.this, schermata6.class);
                    intent.putExtra("EMAIL", email); // Passaggio dell'email tramite intent extra
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });
    }
}
