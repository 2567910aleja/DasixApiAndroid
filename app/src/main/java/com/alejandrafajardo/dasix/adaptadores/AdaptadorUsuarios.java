package com.alejandrafajardo.dasix.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandrafajardo.dasix.Api.Api;
import com.alejandrafajardo.dasix.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class AdaptadorUsuarios extends RecyclerView.Adapter<AdaptadorUsuarios.ViewHolder> {

    JSONArray jsonArray;
    Context context;
    public AdaptadorUsuarios(Context context, JSONArray jsonArray){
        this.jsonArray =jsonArray; this.context=context;
    }

    @NonNull
    @Override
    public AdaptadorUsuarios.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_usuarios, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorUsuarios.ViewHolder holder, int position) {
        // Colocar los datos del json a cada elemento o vista
        try {
            holder.tvNombre.setText(jsonArray.getJSONObject(position).getString("username"));
            // Imagen
            //holder.ivImagen.setImageResource(R.mipmap.ic_launcher);
            String url=jsonArray.getJSONObject(position).get("image").toString();

            Picasso.with(context).load(url).into(holder.ivImagen);
        } catch (JSONException e) {
            // Error porque no se pudo obtener el Json
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Atributos
        TextView tvNombre;
        ImageView ivImagen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Relacion de los atributos con la vista
            tvNombre=itemView.findViewById(R.id.tvNombre);
            ivImagen=itemView.findViewById(R.id.ivImagen);
        }
    }
}

