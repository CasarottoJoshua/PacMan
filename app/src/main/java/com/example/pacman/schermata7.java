package com.example.pacman;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class schermata7 extends AppCompatActivity {

    private Button schermata1, schermata7;
    private Button[] fruttiButtons = new Button[5];
    private Button[] sfondiButtons = new Button[5];
    private int selectedFrutto = -1;
    private int selectedSfondo = -1;

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
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Imposta il layout della tua attività
        setContentView(R.layout.activity_schermata7);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Codice per spostamento a schermata1
        schermata1 = findViewById(R.id.BottoneX);
        schermata1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(schermata7.this, schermata1.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        schermata7 = findViewById(R.id.BottoneContinua7);
        schermata7.setOnClickListener(v -> saveCombinationAndProceed());

        // Ciclo per inizializzare i bottoni dei frutti
        for (int i = 0; i < 5; i++)
        {
            String fruttoButtonId = "buttonFrutto" + (i + 1); //inizializzo i bottoni dei frutti (buttonFrutto1, buttonFrutto2, ... buttonFrutto5)
            int resID = getResources().getIdentifier(fruttoButtonId, "id", getPackageName()); //prendo l'id del bottone
            fruttiButtons[i] = findViewById(resID); //inizializzo il bottone
            final int index = i; //inizializzo l'indice

            // Seleziona il frutto e aggiorna l'immagine combinata
            fruttiButtons[i].setOnClickListener(v -> {
                selectFrutto(index);
                updateCombinedImage();
            });
        }

        // Ciclo per inizializzare i bottoni degli sfondi
        for (int i = 0; i < 5; i++)
        {
            String sfondoButtonId = "buttonSfondo" + (i + 1); //inizializzo i bottoni degli sfondi (buttonSfondo1, buttonSfondo2, ... buttonSfondo5)
            int resID = getResources().getIdentifier(sfondoButtonId, "id", getPackageName()); //prendo l'id del bottone
            sfondiButtons[i] = findViewById(resID); //inizializzo il bottone
            final int index = i; //inizializzo l'indice

            // Seleziona lo sfondo e aggiorna l'immagine combinata
            sfondiButtons[i].setOnClickListener(v -> {
                selectSfondo(index);
                updateCombinedImage();
            });
        }
    }

    // Metodo per selezionare un frutto
    private void selectFrutto(int index)
    {
        if (selectedFrutto != -1)  // Se è già stato selezionato un frutto
        {
            fruttiButtons[selectedFrutto].setSelected(false); // Deseleziona il frutto
        }
        fruttiButtons[index].setSelected(true); // Seleziona il frutto
        selectedFrutto = index; // Imposta l'indice del frutto selezionato
    }

    // Metodo per selezionare uno sfondo
    private void selectSfondo(int index)
    {
        if (selectedSfondo != -1) // Se è già stato selezionato uno sfondo
        {
            sfondiButtons[selectedSfondo].setSelected(false); // Deseleziona lo sfondo
        }
        sfondiButtons[index].setSelected(true); // Seleziona lo sfondo
        selectedSfondo = index; // Imposta l'indice dello sfondo selezionato
    }

    // Metodo per aggiornare l'immagine combinata
    private void updateCombinedImage()
    {
        if (selectedFrutto != -1 && selectedSfondo != -1) // Se è stato selezionato sia un frutto che uno sfondo
        {
            // Crea un array con i nomi delle immagini dei frutti
            String[] fruttoNames = {"frutto_grigio", "frutto_blu", "frutto_viola", "frutto_verde", "frutto_arancione"};
            String combinedImage = fruttoNames[selectedSfondo] + (selectedFrutto + 1); // Componi il nome dell'immagine combinata

            ImageView imageViewCombinata = findViewById(R.id.imageViewCombinata); // Prendi l'ImageView dell'immagine combinata
            int resID = getResources().getIdentifier(combinedImage, "drawable", getPackageName()); // Prendi l'id dell'immagine combinata
            imageViewCombinata.setImageResource(resID); // Imposta l'immagine combinata
        }
    }

    // Metodo per salvare la combinazione e procedere
    private void saveCombinationAndProceed() {
        if (selectedFrutto != -1 && selectedSfondo != -1)
        {
            // Crea un array con i nomi delle immagini dei frutti
            String[] fruttoNames = {"frutto_grigio", "frutto_blu", "frutto_viola", "frutto_verde", "frutto_arancione"};
            String combinedImage = fruttoNames[selectedSfondo] + (selectedFrutto + 1); // Componi il nome dell'immagine combinata
            saveImageToDatabase(combinedImage); // Salva l'immagine combinata nel database
        }
        else
        {
            Toast.makeText(this, "Seleziona sia un frutto che uno sfondo", Toast.LENGTH_SHORT).show();
        }
    }

    // Metodo per salvare l'immagine nel database
    private void saveImageToDatabase(String imageName)
    {
        // Recupero l'email dell'utente dalla schermata precedente
        Intent intent = getIntent();
        String email = intent.getStringExtra("EMAIL");

        // Codice per salvare l'immagine nel database associata all'utente loggato tramite una chiamata HTTP a uno script PHP
        new Thread(new Runnable() {
            @Override
            public void run() {
                String[] field = new String[2];
                field[0] = "email";
                field[1] = "image";

                String[] data = new String[2];
                data[0] = email;
                data[1] = imageName;

                PutData putData = new PutData("https://5cijoshuacasarotto.barsanti.edu.it/DBPacMan/saveImage.php", "POST", field, data);

                if (putData.startPut())
                {
                    if (putData.onComplete())
                    {
                        String result = putData.getResult();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(schermata7.this, schermata1.class);
                                intent.putExtra("REGISTRATION_COMPLETE", true);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    else
                    {
                        Log.e("PutDataError", "PutData onComplete failed");
                    }
                }
                else
                {
                    Log.e("PutDataError", "PutData startPut failed");
                }
            }
        }).start();
    }
}
