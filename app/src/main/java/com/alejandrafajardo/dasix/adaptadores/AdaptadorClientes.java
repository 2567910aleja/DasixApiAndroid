package com.alejandrafajardo.dasix.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandrafajardo.dasix.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class AdaptadorClientes extends RecyclerView.Adapter<AdaptadorClientes.ViewHolder> {
    JSONArray jsonArray;
    Context context;
    public AdaptadorClientes(Context context, JSONArray jsonArray){
        this.jsonArray =jsonArray; this.context=context;
    }

    @NonNull
    @Override
    public AdaptadorClientes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_clientes, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorClientes.ViewHolder holder, int position) {
        // Colocar los datos del json a cada elemento o vista
        try {
            String nombres=jsonArray.getJSONObject(position).getString("Nombres")+" "+jsonArray.getJSONObject(position).getString("Apellidos");
            holder.tvNombre.setText(nombres);
            holder.tvCedula.setText(jsonArray.getJSONObject(position).getString("Cedula"));
            holder.tvDire.setText(jsonArray.getJSONObject(position).getString("Direccion"));
            holder.tvCumple.setText(jsonArray.getJSONObject(position).getString("Cumple"));
            holder.tvSexo.setText(jsonArray.getJSONObject(position).getString("Sexo"));

            // creo las opciones de modificar y eliminar cliente
            //holder.btnModificar.

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
        Button btnModificar, btnEliminar;
        TextView tvNombre,tvDire,tvCedula,tvCumple,tvSexo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Relacion de los atributos con la vista
            tvNombre=itemView.findViewById(R.id.tvNombre);
            tvDire=itemView.findViewById(R.id.tvDire);
            tvCedula=itemView.findViewById(R.id.tvCedula);
            tvCumple=itemView.findViewById(R.id.tvCumple);
            tvSexo=itemView.findViewById(R.id.tvSexo);
            btnModificar=itemView.findViewById(R.id.btnModificar);
            btnEliminar=itemView.findViewById(R.id.btnEliminar);

        }
    }
}
