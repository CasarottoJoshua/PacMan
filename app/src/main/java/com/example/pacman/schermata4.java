package com.example.pacman;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class schermata4 extends AppCompatActivity
{
    // Dichiarazione delle variabili
    private FirebaseAuth auth; // Oggetto per l'autenticazione dell'utente tramite Firebase
    private EditText codeEditText; // Campo di testo per l'inserimento del codice di verifica
    private Button verifyButton; // Bottone per inviare il codice di verifica
    private TextView emailUtente; // Campo di testo per l'email dell'utente
    private String username; // Username dell'utente

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
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        setContentView(R.layout.activity_schermata4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance(); // Inizializzazione dell'oggetto per l'autenticazione dell'utente tramite Firebase
        codeEditText = findViewById(R.id.TextInputVerifica); // Inizializzazione del campo di testo per l'inserimento del codice di verifica
        verifyButton = findViewById(R.id.BottoneInvia); // Inizializzazione del bottone per inviare il codice di verifica
        emailUtente = findViewById(R.id.emailUtente); // Inizializzazione del campo di testo per l'email dell'utente

        // Recupero dell'username dell'utente dalla schermata precedente
        Intent intent = getIntent();
        if (intent != null)
        {
            username = intent.getStringExtra("username"); // Recupero dell'username dell'utente
        }

        // Recupero dell'email dell'utente tramite l'username (non funzionante)
        verifyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String code = codeEditText.getText().toString().trim();
                if (!code.isEmpty() && emailUtente.getText() != null)
                {
                    //verifyEmailWithCode(emailUtente.getText().toString().trim(), code);
                } else
                {
                    Toast.makeText(schermata4.this, "Inserisci il codice di verifica", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
    private void verifyEmailWithCode(String email, String code) {
        auth.signInWithEmailLink(email, code)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(schermata4.this, "Verifica avvenuta con successo", Toast.LENGTH_SHORT).show();
                            // Proceed to reset password or main activity
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(schermata4.this, "Codice di verifica non valido", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(schermata4.this, "Errore nella verifica", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void retrieveEmail(String username) {
        String url = "https://5cijoshuacasarotto.barsanti.edu.it/DBPacMan/RecuperaEmail.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String email = response.getString("email");
                            emailUtente.setText(email);
                        } catch (Exception e) {
                            Log.e("Error", "Unexpected response format", e);
                            Toast.makeText(schermata4.this, "Errore nel recupero dell'email", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        Toast.makeText(schermata4.this, "Errore di connessione", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

     */
}
