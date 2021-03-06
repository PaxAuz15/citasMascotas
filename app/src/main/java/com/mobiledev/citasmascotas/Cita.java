package com.mobiledev.citasmascotas;

public class Cita {
    String tipo_mascota;
    String detalle;
    Float total;
    long fecha;

    public Cita(){
    }

    public Cita(String tipo_mascota, String detalle, Float total, long fecha){
        this.tipo_mascota = tipo_mascota;
        this.detalle = detalle;
        this.total = total;
        this.fecha = fecha;
    }

    public String getTipo_mascota() {
        return tipo_mascota;
    }

    public String getDetalle() {
        return detalle;
    }

    public Float getTotal() {
        return total;
    }

    public long getFecha() {
        return fecha;
    }

    public void setTipo_mascota(String tipo_mascota) {
        this.tipo_mascota = tipo_mascota;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }
}
