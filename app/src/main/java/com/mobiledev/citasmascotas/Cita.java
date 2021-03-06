package com.mobiledev.citasmascotas;

import java.util.ArrayList;

public class Cita {
    String tipo_mascota;
    ArrayList<String> detalle;
    double total;
    long fecha;
public Cita(){

}
    public Cita(String tipo_mascota, ArrayList<String> detalle, double total, long fecha){
        this.tipo_mascota = tipo_mascota;
        this.detalle = detalle;
        this.total = total;
        this.fecha = fecha;
    }

    public String getTipo_mascota() {
        return tipo_mascota;
    }

    public ArrayList<String> getDetalle() {
        return detalle;
    }

    public double getTotal() {
        return total;
    }

    public long getFecha() {
        return fecha;
    }

    public void setTipo_mascota(String tipo_mascota) {
        this.tipo_mascota = tipo_mascota;
    }

    public void setDetalle(ArrayList<String> detalle) {
        this.detalle = detalle;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }
}
