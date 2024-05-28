package com.example.pacman;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class schermata3 extends AppCompatActivity
{
    // Dichiarazione delle variabili
    private Button schermata1, schermata2, schermata4, schermata5;
    private Button googleAuth, condizioniButton; // Bottone per l'autenticazione con Google
    private FirebaseAuth auth; // Autenticazione Firebase
    private FirebaseDatabase database; // Database Firebase
    private GoogleSignInClient mGoogleSignInClient; // Client per l'autenticazione con Google
    private int RC_SIGN_IN = 20; // Codice di richiesta per l'autenticazione con Google

    TextInputEditText textInputEditTextPassword, textInputEditTextEmail;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Imposta l'orientamento del display su orizzontale
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Nascondi la barra di stato e attiva la modalità schermo intero
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Rimuovi il bordo attorno alla visualizzazione
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Imposta il layout della tua attività
        setContentView(R.layout.activity_schermata3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Codice per collegamento gmail ----------------------------------------------------
        googleAuth = findViewById(R.id.google_sign_in_button);
        auth = FirebaseAuth.getInstance(); // Inizializza l'istanza di autenticazione Firebase
        database = FirebaseDatabase.getInstance(); // Inizializza l'istanza del database Firebase

        // Configura l'opzione di accesso con Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .build();

        // Inizializza il client di accesso con Google
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Aggiunge un listener al bottone di accesso con Google
        googleAuth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Avvia il processo di accesso con Google
                mGoogleSignInClient.signOut().addOnCompleteListener(schermata3.this, new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        googleSignIn();
                    }
                });
            }
        });

        // Codice per spostamento a schermata1----------------------------------------------------
        schermata1 = findViewById(R.id.BottoneX);
        schermata1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(schermata3.this, schermata1.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // Codice per spostamento a schermata2----------------------------------------------------
        schermata2 = findViewById(R.id.BottoneAnnulla3);
        schermata2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(schermata3.this, schermata2.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        //codice per spostamento a schermata5----------------------------------------------------
        textInputEditTextPassword = findViewById(R.id.password);
        textInputEditTextEmail = findViewById(R.id.email);
        schermata5 = findViewById(R.id.BottoneRegistrazione);
        schermata5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Prende i valori inseriti dall'utente
                String password, email;
                password = String.valueOf(textInputEditTextPassword.getText());
                email = String.valueOf(textInputEditTextEmail.getText());

                // Controlla se i campi sono stati compilati
                if(!password.equals("") && !email.equals(""))
                {
                    if (isValidEmail(email)) // Controlla se l'email è valida
                    {
                        // Inizia la connessione al server e registra l'utente nel database grazie al file PHP
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                // Creazione di un array per i campi da inviare
                                String[] field = new String[2];
                                field[0] = "password";
                                field[1] = "email";

                                // Creazione di un array per i dati da inviare
                                String[] data = new String[2];
                                data[0] = password;
                                data[1] = email;

                                // Inizio della scrittura e lettura dei dati con URL
                                PutData putData = new PutData("https://5cijoshuacasarotto.barsanti.edu.it/DBPacMan/signup.php", "POST", field, data);

                                if (putData.startPut()) // Inizia la connessione
                                {
                                    if (putData.onComplete()) // Controlla se la connessione è stata completata
                                    {
                                        String result = putData.getResult();
                                        if(result.equals("Sign Up Success!")) // Se la registrazione è avvenuta con successo
                                        {
                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(schermata3.this, schermata5.class);
                                            intent.putExtra("EMAIL", email); // Passaggio dell'email tramite intent extra
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else // Altrimenti mostra un messaggio di errore
                                        {
                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                        });
                    }
                    else // Altrimenti mostra un messaggio di errore
                    {
                        Toast.makeText(getApplicationContext(), "Email non valida", Toast.LENGTH_SHORT).show();
                    }
                }
                else // Altrimenti mostra un messaggio di errore
                {
                    //Se manca anche solo un componente == errore
                    Toast.makeText(getApplicationContext(),"Tutti i componenti richiesti", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Codice per il bottone "condizioni"
        condizioniButton = findViewById(R.id.condizioni);
        condizioniButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://docs.google.com/document/d/1lwhg9LnOj2kxPRqtNK_-y9wuAWGsMVseQii2V-7qXog/edit?usp=sharing";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    // Metodo per controllare se l'email è valida
    private boolean isValidEmail(String email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(); // Controlla se l'email è valida
    }

    // Metodo per l'autenticazione con Google
    public void googleSignIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Metodo per gestire il risultato dell'autenticazione con Google
    @Override
    protected void onActivityResult(int requestcode, int resultcode, @Nullable Intent data)
    {
        super.onActivityResult(requestcode, resultcode, data);

        if (requestcode == RC_SIGN_IN) // Se la richiesta è per l'autenticazione con Google
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data); // Ottieni l'account Google
            try
            {
                // Se l'autenticazione è avvenuta con successo
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            }
            catch (ApiException e)  // Altrimenti mostra un messaggio di errore
            {
                Log.e("Google Sign-In Error", "signInResult:failed code=" + e.getStatusCode());
                Toast.makeText(this, "Sign-in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Metodo per l'autenticazione con Firebase
    private void firebaseAuth(String idToken)
    {
        // Crea un'istanza di credenziali di autenticazione con Google
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())  // Se l'autenticazione è avvenuta con successo
                        {
                            // Ottieni l'utente autenticato
                            FirebaseUser user = auth.getCurrentUser();

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", user.getUid());
                            map.put("name", user.getDisplayName());
                            map.put("profile", user.getPhotoUrl().toString());

                            database.getReference().child("users").child(user.getUid()).setValue(map);

                            saveUserToPleskDatabase(user.getDisplayName(), user.getEmail(), true);
                        }
                        else // Altrimenti mostra un messaggio di errore
                        {
                            Toast.makeText(getApplicationContext(), "Sign in with Google failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Metodo per salvare l'utente nel database Plesk
    private void saveUserToPleskDatabase(String username, String email, boolean isGoogleUser)
    {
        // Crea una richiesta POST per l'inserimento dell'utente nel database
        String url = "https://5cijoshuacasarotto.barsanti.edu.it/DBPacMan/inserimento.php";

        // Crea un HashMap per i parametri
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("email", email);
        params.put("isGoogleUser", String.valueOf(isGoogleUser)); // Include whether the user is a Google user

        // Crea un oggetto JSON per i parametri
        JSONObject jsonBody = new JSONObject(params);
        final String requestBody = jsonBody.toString();

        // Crea una richiesta POST per l'inserimento dell'utente nel database
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("saveUserToPleskDatabase", "Server response: " + response);
                try
                {
                    // Verifica lo stato della risposta
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");

                    if (status.equalsIgnoreCase("success"))  // Se la risposta è positiva
                    {
                        Intent intent = new Intent(schermata3.this, schermata5.class);
                        intent.putExtra("EMAIL", email); // Pass email to the next activity
                        intent.putExtra("IS_GOOGLE_SIGNIN", true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish(); // Finish the current activity to prevent user from coming back
                    }
                    else  // Altrimenti mostra un messaggio di errore
                    {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(getApplicationContext(), "Server response: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)  // Altrimenti mostra un messaggio di errore
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "JSON parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) // Errori di connessione o server
            {
                Log.e("Plesk Error", "Error response: " + error.toString());
                if (error.networkResponse != null)
                {
                    Log.e("Plesk Error", "Error data: " + new String(error.networkResponse.data));
                }
                else
                {
                    Log.e("Plesk Error", "Error response is null");
                }
                Toast.makeText(getApplicationContext(), "Server error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public String getBodyContentType()
            {
                return "application/json; charset=utf-8"; // Imposta il tipo di contenuto del corpo della richiesta come JSON UTF-8
            }

            @Override
            public byte[] getBody() throws AuthFailureError  // Restituisce il corpo della richiesta (ovvero i parametri)
            {
                try
                {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException uee)
                {
                    return null;
                }
            }
        };

        Volley.newRequestQueue(this).add(stringRequest); // Aggiungi la richiesta alla coda delle richieste
    }
}
