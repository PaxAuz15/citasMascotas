package com.mobiledev.citasmascotas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemViewHolder> {
    Context context;
    ArrayList<Cita> CitaArrayList;
    Locale id = new Locale("in","ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-YYYY",id);

    public AdapterItem(Context context, ArrayList<Cita> CitaArrayList) {
        this.context = context;
        this.CitaArrayList = CitaArrayList;
    }

    @NonNull
    @Override
    public AdapterItem.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItem.ItemViewHolder holder, int position) {
        holder.viewBind(CitaArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return CitaArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tipoMascota,
                tv_adaptadorDetalle,
                tv_total,
                tv_fecha;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tipoMascota = itemView.findViewById(R.id.tv_totalShow);
            tv_adaptadorDetalle = itemView.findViewById(R.id.tv_adaptadorDetalle);
            tv_total = itemView.findViewById(R.id.tv_total);
            tv_fecha = itemView.findViewById(R.id.tv_fecha);
        }

        public void viewBind(Cita Cita) {
            tv_tipoMascota.setText(Cita.getTipo_mascota());
            tv_adaptadorDetalle.setText(Cita.getDetalle());
            tv_total.setText(Cita.getTotal().toString());
            tv_fecha.setText(simpleDateFormat.format(Cita.getFecha()));
        }
    }
}
