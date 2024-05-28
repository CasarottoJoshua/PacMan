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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class schermata9 extends AppCompatActivity
{
    // Dichiarazione delle variabili
    private Button schermata1;
    private Button schermata4;
    private Button schermata8;
    private Button schermata_10;
    private Button googleLogInButton;
    private Button condizioniButton;
    private FirebaseAuth auth; // Instanza di Firebase Auth
    private FirebaseDatabase database; // Instanza di Firebase Database
    private GoogleSignInClient mGoogleSignInClient; // Instanza di Google Sign-In
    private int RC_SIGN_IN = 20; // Codice di richiesta per il login con Google
    TextInputEditText textInputEditTextPassword, textInputEditTextUsername;

    @SuppressLint("MissingInflatedId")
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
        setContentView(R.layout.activity_schermata1);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_schermata9);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inizializza Firebase Auth e Database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Configura Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso); // Inizializza il client di Google Sign-In

        // Imposta il listener per il bottone di login con Google
        googleLogInButton = findViewById(R.id.google_log_in_button);
        googleLogInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut().addOnCompleteListener(schermata9.this, new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        googleSignIn();
                    }
                });
            }
        });

        // Passaggio alla schermata4
        schermata1 = findViewById(R.id.BottoneX);
        schermata1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(schermata9.this, schermata1.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // Passaggio alla schermata4
        schermata8 = findViewById(R.id.BottoneAnnulla9);
        schermata8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(schermata9.this, schermata8.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        // Passaggio alla schermata4
        textInputEditTextPassword = findViewById(R.id.passwordLogin);
        textInputEditTextUsername = findViewById(R.id.usernameLogin);
        schermata_10 = findViewById(R.id.BottoneAccedi9);
        schermata_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String password, username; // Dichiarazione delle variabili per la password e l'username
                password = String.valueOf(textInputEditTextPassword.getText()); // Recupero del testo inserito (password)
                username = String.valueOf(textInputEditTextUsername.getText()); // Recupero del testo inserito (username)

                if (!password.equals("") && !username.equals("")) // Se la password e l'username non sono vuoti
                {
                    // Start ProgressBar first (Set visibility VISIBLE)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Creazione di un array per i campi da inviare
                            String[] field = new String[2];
                            field[0] = "password";
                            field[1] = "username";

                            // Creazione di un array per i dati da inviare
                            String[] data = new String[2];
                            data[0] = password;
                            data[1] = username;

                            PutData putData = new PutData("https://5cijoshuacasarotto.barsanti.edu.it/DBPacMan/login.php", "POST", field, data);

                            if (putData.startPut()) // Inizio della connessione
                            {
                                if (putData.onComplete()) // Se la connessione è completata
                                {
                                    String result = putData.getResult();
                                    if (result.equals("Login Success"))  // Se il login è riuscito
                                    {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(schermata9.this, schermata_10.class);
                                        intent.putExtra("USERNAME", username); // Passaggio dell'username tramite intent extra
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
                    // Se la password e l'username sono vuoti == errore
                    Toast.makeText(getApplicationContext(), "Tutti i componenti richiesti", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Codice per il bottone "condizioni"
        condizioniButton = findViewById(R.id.condizioni);
        condizioniButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Apre il browser con il link delle condizioni
                String url = "https://docs.google.com/document/d/1lwhg9LnOj2kxPRqtNK_-y9wuAWGsMVseQii2V-7qXog/edit?usp=sharing";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    /*
    private void sendVerificationCode(String username) {
        String url = "https://5cijoshuacasarotto.barsanti.edu.it/DBPacMan/RecuperaEmail.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                Toast.makeText(schermata9.this, "Link di reset inviato all'email associata", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(schermata9.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("Error", "Unexpected response format", e);
                            Toast.makeText(schermata9.this, "Errore nel recupero dell'email", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        Toast.makeText(schermata9.this, "Errore di connessione", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
    */

    // Metodo per l'autenticazione con Google
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Metodo per gestire il risultato dell'autenticazione con Google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) // Se la richiesta è per l'autenticazione con Google
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
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
        // Crea un'istanza di AuthCredential con l'idToken
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())  // Se l'autenticazione è avvenuta con successo
                        {
                            FirebaseUser user = auth.getCurrentUser(); // Recupera l'utente corrente
                            if (user != null)
                            {
                                checkEmailInDatabase(user.getEmail(), user); // Controlla se l'email è presente nel database e passa l'utente
                            }
                        } else
                        {
                            Toast.makeText(getApplicationContext(), "Sign in with Google failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Metodo per controllare se l'email è presente nel database
    private void checkEmailInDatabase(String email, FirebaseUser user)
    {
        String url = "https://5cijoshuacasarotto.barsanti.edu.it/DBPacMan/checkEmail.php"; // URL del file PHP

        // Crea un oggetto HashMap per i parametri da inviare
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);

        // Crea un oggetto JSON con i parametri
        JSONObject jsonObject = new JSONObject(params);
        Log.d("checkEmailInDatabase", "Sending data: " + jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try
                        {
                            String status = response.getString("status");
                            if (status.equals("exists"))  // Se l'email è presente nel database
                            {
                                String username = response.getString("username"); // Recupera l'username dalla risposta

                                // Crea un oggetto HashMap per i dati da inviare al database
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("name", user.getDisplayName());
                                map.put("email", user.getEmail());
                                map.put("profileImage", String.valueOf(user.getPhotoUrl()));

                                database.getReference().child("Users").child(user.getUid()).updateChildren(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    Intent intent = new Intent(schermata9.this, schermata_10.class);
                                                    intent.putExtra("USERNAME", username); // Passa l'username
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else
                                                {
                                                    Toast.makeText(schermata9.this, "Error in saving user data", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Email not found in database", Toast.LENGTH_SHORT).show();
                                auth.signOut();
                            }
                        }
                        catch (Exception e)
                        {
                            Log.e("Database Error", "Unexpected response format", e);
                            auth.signOut();
                        }
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) // Se c'è un errore nella connessione
                    {
                        Log.e("Database Error", error.toString());
                        auth.signOut();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders()  // Aggiunge l'header Content-Type
            {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private class ErrorListener {
    }
}
