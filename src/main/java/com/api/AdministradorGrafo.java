package com.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AdministradorGrafo {

    private List<Arista> aristas;
    private Map<String, Integer> ciudadIndice;
    private double[][] matriz;
    private double[][] distancias;
    private String climaActual;

    public AdministradorGrafo(String archivo) throws IOException {
        this.aristas = Matriz_Funcionamiento.leerArchivoLogistica(archivo);
        this.ciudadIndice = Matriz_Funcionamiento.obtenerCiudades(aristas);
        this.climaActual = "normal";
        this.recalcular();
    }

    //Llamado a las funciones de acualizar por clima y algoritmo Floyd
    public void recalcular() {
        this.matriz = Matriz_Funcionamiento.actualizarMatrizPorClima(aristas, ciudadIndice, ciudadIndice.size(), climaActual);
        this.distancias = Matriz_Funcionamiento.algoritmoFloyd(matriz);
    }

    public void cambiarClima(String nuevoClima) {
        this.climaActual = nuevoClima;
        this.recalcular();
    }

    // Llamado agregar arista y obtener Ciudades 
    public void agregarConexion(String origen, String destino, double tNormal, double tLluvia, double tNieve, double tTormenta) {
        Matriz_Funcionamiento.agregarArista(aristas, origen, destino, tNormal, tLluvia, tNieve, tTormenta);
        this.ciudadIndice = Matriz_Funcionamiento.obtenerCiudades(aristas); 
        this.recalcular();
    }

    // Llamado eliminar arista 
    public void eliminarConexion(String origen, String destino) {
        Matriz_Funcionamiento.eliminarArista(aristas, origen, destino);
        this.recalcular();
    }

    // Obtener el resultado del nodo central
    public String getCiudadCentral() {
        return Matriz_Funcionamiento.encontrarCiudadCentral(distancias, ciudadIndice);
    }

    // Obtener las conexiones entre nodos 
    public void mostrarRuta(String origen, String destino) {
        Matriz_Funcionamiento.mostrarRutaMasCorta(origen, destino, ciudadIndice, distancias);
    }

    //Obtener la matriz completa 
    public void mostrarMatriz() {
        Matriz_Funcionamiento.mostrarMatriz(matriz, ciudadIndice);
    }
}
