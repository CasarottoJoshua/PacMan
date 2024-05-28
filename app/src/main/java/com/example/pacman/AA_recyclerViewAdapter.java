package com.example.pacman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

// Adapter per la RecyclerView
public class AA_recyclerViewAdapter extends RecyclerView.Adapter<AA_recyclerViewAdapter.MyViewHolder>
{
    // Dichiarazione delle variabili
    Context context;
    ArrayList<PositionModel> positionModels;

    // Costruttore
    public AA_recyclerViewAdapter(Context context, ArrayList<PositionModel> positionModels)
    {
        this.context = context;
        this.positionModels = positionModels;
    }

    // Metodo per creare la vista
    @NonNull
    @Override
    public AA_recyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // Creazione della vista
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);
        return new AA_recyclerViewAdapter.MyViewHolder(view);
    }

    // Metodo per riempire la vista con i dati
    @Override
    public void onBindViewHolder(@NonNull AA_recyclerViewAdapter.MyViewHolder holder, int position)
    {
        // Riempimento della vista
        PositionModel model = positionModels.get(position);
        holder.imageViewPosition.setImageResource(model.getPosition());
        holder.username.setText(model.getUsername());
        holder.punteggio.setText(model.getPunteggio());

        // Gestione del campo tempo che può essere null
        if (model.getTempo() != null)
        {
            // Se il campo tempo non è null, viene impostato
            holder.tempo.setText(model.getTempo());
        }
        else
        {
            // Se il campo tempo è null, viene impostato "N/A"
            holder.tempo.setText("N/A");
        }

        // Verifica che model.getImage() non sia null o vuoto
        String profileImageName = model.getImage();
        if (profileImageName != null && !profileImageName.isEmpty())
        {
            // Recupera l'id dell'immagine dal nome
            int profileImageId = context.getResources().getIdentifier(profileImageName, "drawable", context.getPackageName());

            // Verifica che la risorsa esista
            if (profileImageId != 0)
            {
                // Se esiste, viene impostata l'immagine
                holder.imageViewProfile.setImageResource(profileImageId);
            }
            else
            {
                // Se non esiste, viene impostata un'immagine di default
                holder.imageViewProfile.setImageResource(R.drawable.frutto_grigio1);
            }
        }
        else
        {
            // Se il campo è null o vuoto, viene impostata un'immagine di default
            holder.imageViewProfile.setImageResource(R.drawable.frutto_grigio1);
        }
    }

    // Metodo per ottenere il numero di elementi
    @Override
    public int getItemCount()
    {
        return positionModels.size();
    }

    // Classe per la gestione della vista
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        // Dichiarazione delle variabili
        ImageView imageViewPosition;
        TextView username, punteggio, tempo;
        ImageView imageViewProfile;

        // Costruttore
        public MyViewHolder(@NonNull View itemView)
        {
            // Inizializzazione delle variabili
            super(itemView);
            imageViewPosition = itemView.findViewById(R.id.imageView12);
            username = itemView.findViewById(R.id.textView_username);
            punteggio = itemView.findViewById(R.id.textView_punteggio);
            tempo = itemView.findViewById(R.id.textView_tempo);
            imageViewProfile = itemView.findViewById(R.id.imageView_user);
        }
    }
}
