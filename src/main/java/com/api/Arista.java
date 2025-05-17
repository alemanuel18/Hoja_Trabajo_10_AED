package com.api;

public class Arista {
    private String origen;
    private String destino;
    private double tiempoNormal;
    private double tiempoLluvia;
    private double tiempoNieve;
    private double tiempoTormenta;

    public Arista(String origen, String destino, double tiempoNormal, double tiempoLluvia, double tiempoNieve, double tiempoTormenta) {
        this.origen = origen;
        this.destino = destino;
        this.tiempoNormal = tiempoNormal;
        this.tiempoLluvia = tiempoLluvia;
        this.tiempoNieve = tiempoNieve;
        this.tiempoTormenta = tiempoTormenta;
    }

    public String getDestino() {
        return destino;
    }

    public String getOrigen() {
        return origen;
    }

    public double getTiempoLluvia() {
        return tiempoLluvia;
    }

    public double getTiempoNieve() {
        return tiempoNieve;
    }

    public double getTiempoNormal() {
        return tiempoNormal;
    }

    public double getTiempoTormenta() {
        return tiempoTormenta;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public void setTiempoLluvia(double tiempoLluvia) {
        this.tiempoLluvia = tiempoLluvia;
    }

    public void setTiempoNieve(double tiempoNieve) {
        this.tiempoNieve = tiempoNieve;
    }

    public void setTiempoNormal(double tiempoNormal) {
        this.tiempoNormal = tiempoNormal;
    }

    public void setTiempoTormenta(double tiempoTormenta) {
        this.tiempoTormenta = tiempoTormenta;
    }
}