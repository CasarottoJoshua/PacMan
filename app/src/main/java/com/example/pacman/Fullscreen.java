package com.example.pacman;

import android.content.pm.ActivityInfo;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

public class Fullscreen {
    public static void enableFullscreen(AppCompatActivity activity)
    {

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // Nascondi la barra di stato e attiva la modalità schermo intero

        int UI_OPTIONS = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        activity.getWindow().getDecorView().setSystemUiVisibility(UI_OPTIONS);

        // Imposta l'orientamento del display su orizzontale
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Rimuovi il bordo attorno alla visualizzazione
        //EdgeToEdge.enable(activity);
        WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);

        //-------------------------------------------------------------------------------------------------------------------------------------------------------------------

    }
}
