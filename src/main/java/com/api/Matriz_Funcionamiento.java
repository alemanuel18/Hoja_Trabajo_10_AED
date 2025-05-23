package com.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Matriz_Funcionamiento {

    // Estructura para almacenar el siguiente nodo en el camino más corto
    private static int[][] siguienteNodo;

    // Variables estáticas para mantener el estado del grafo
    private static List<Arista> aristas;
    private static Map<String, Integer> ciudadIndice;
    private static double[][] matriz;
    private static double[][] distancias;
    private static String climaActual = "normal";

    // Método para inicializar el grafo desde un archivo
    public static void inicializarGrafo(String nombreArchivo) throws IOException {
        aristas = leerArchivoLogistica(nombreArchivo);
        ciudadIndice = obtenerCiudades(aristas);
        climaActual = "normal";
        recalcular();
    }

    // Método para recalcular la matriz y distancias
    public static void recalcular() {
        matriz = actualizarMatrizPorClima(aristas, ciudadIndice, ciudadIndice.size(), climaActual);
        distancias = algoritmoFloyd(matriz);
    }

    // Método para cambiar el clima global
    public static void cambiarClima(String nuevoClima) {
        climaActual = nuevoClima;
        recalcular();
    }

    // Método para agregar interrupción de tráfico (eliminar conexión)
    public static void agregarInterrupcionTrafico(String origen, String destino) {
        eliminarArista(aristas, origen, destino);
        recalcular();
        System.out.println("Interrupción de tráfico establecida entre " + origen + " y " + destino);
    }

    // Método para establecer nueva conexión con todos los tiempos
    public static void establecerNuevaConexion(String origen, String destino, 
            double tiempoNormal, double tiempoLluvia, 
            double tiempoNieve, double tiempoTormenta) {
        agregarArista(aristas, origen, destino, tiempoNormal, tiempoLluvia, tiempoNieve, tiempoTormenta);
        ciudadIndice = obtenerCiudades(aristas);
        recalcular();
        System.out.println("Nueva conexión establecida entre " + origen + " y " + destino);
    }

    // Método para establecer clima específico entre dos ciudades
    public static void establecerClimaEspecifico(String origen, String destino, String clima) {
        // Buscar la arista específica y modificar su tiempo según el clima
        boolean encontrada = false;
        for (Arista arista : aristas) {
            if (arista.getOrigen().equals(origen) && arista.getDestino().equals(destino)) {
                // Crear una copia temporal para modificar solo esta conexión
                double tiempoOriginal = switch (clima.toLowerCase()) {
                    case "lluvia" -> arista.getTiempoLluvia();
                    case "nieve" -> arista.getTiempoNieve();
                    case "tormenta" -> arista.getTiempoTormenta();
                    default -> arista.getTiempoNormal();
                };
                
                // Aquí podríamos implementar una lógica más compleja para manejar climas específicos
                // Por simplicidad, aplicamos el tiempo correspondiente al clima especificado
                System.out.println("Clima " + clima + " establecido entre " + origen + " y " + destino + 
                                 " (Tiempo: " + tiempoOriginal + ")");
                encontrada = true;
                break;
            }
        }
        
        if (!encontrada) {
            System.out.println("No se encontró conexión entre " + origen + " y " + destino);
        } else {
            recalcular();
        }
    }

    // Métodos getter para acceder a los datos desde Main
    public static String getClimaActual() {
        return climaActual;
    }

    public static String getCiudadCentral() {
        return encontrarCiudadCentral(distancias, ciudadIndice);
    }

    public static void mostrarRutaCorta(String origen, String destino) {
        mostrarRutaMasCorta(origen, destino, ciudadIndice, distancias);
    }

    public static void mostrarMatrizActual() {
        mostrarMatriz(matriz, ciudadIndice);
    }

    // Método para leer el archivo logistica.txt y obtener las aristas del grafo
    public static List<Arista> leerArchivoLogistica(String nombreArchivo) throws IOException {
        List<Arista> aristas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split("\\s+");

                if (partes.length >= 6) {
                    String ciudad1 = partes[0];
                    String ciudad2 = partes[1];
                    double tiempoNormal = Double.parseDouble(partes[2]);
                    double tiempoLluvia = Double.parseDouble(partes[3]);
                    double tiempoNieve = Double.parseDouble(partes[4]);
                    double tiempoTormenta = Double.parseDouble(partes[5]);

                    // Agregar la arista de ciudad1 a ciudad2
                    aristas.add(new Arista(ciudad1, ciudad2, tiempoNormal, tiempoLluvia, tiempoNieve, tiempoTormenta));
                }
            }
        }

        return aristas;
    }

    // Método para agregar una nueva arista al grafo
    public static List<Arista> agregarArista(List<Arista> aristas, String origen, String destino,
            double tiempoNormal, double tiempoLluvia,
            double tiempoNieve, double tiempoTormenta) {
        // Crear una nueva arista con los datos proporcionados
        Arista nuevaArista = new Arista(origen, destino, tiempoNormal, tiempoLluvia, tiempoNieve, tiempoTormenta);

        // Añadir la arista al listado de aristas
        aristas.add(nuevaArista);

        return aristas;
    }

    // Metodo para eliminar la arista 
    public static List<Arista> eliminarArista(List<Arista> aristas, String origen, String destino) {
        aristas.removeIf(a -> a.getOrigen().equals(origen) && a.getDestino().equals(destino));
        return aristas;
    }

    // Metodo para dar nuevo costo de tiempo a las aristas acorde al clima 
    public static double[][] actualizarMatrizPorClima(List<Arista> aristas, Map<String, Integer> ciudadIndice, int numCiudades, String clima) {
        double[][] matriz = new double[numCiudades][numCiudades];

        for (int i = 0; i < numCiudades; i++) {
            for (int j = 0; j < numCiudades; j++) {
                matriz[i][j] = (i == j) ? 0 : Double.POSITIVE_INFINITY;
            }
        }

        for (Arista arista : aristas) {
            int i = ciudadIndice.get(arista.getOrigen());
            int j = ciudadIndice.get(arista.getDestino());

            double peso = switch (clima.toLowerCase()) {
                case "lluvia" -> arista.getTiempoLluvia();
                case "nieve" -> arista.getTiempoNieve();
                case "tormenta" -> arista.getTiempoTormenta();
                default -> arista.getTiempoNormal(); 
            };

            matriz[i][j] = peso;
        }

        return matriz;
    }

    // Método para obtener todas las ciudades del grafo
    public static Map<String, Integer> obtenerCiudades(List<Arista> aristas) {
        Map<String, Integer> ciudades = new HashMap<>();
        int indice = 0;

        for (Arista arista : aristas) {
            if (!ciudades.containsKey(arista.getOrigen())) {
                ciudades.put(arista.getOrigen(), indice++);
            }
            if (!ciudades.containsKey(arista.getDestino())) {
                ciudades.put(arista.getDestino(), indice++);
            }
        }

        return ciudades;
    }

    // Método para crear la matriz de adyacencia
    public static double[][] crearMatrizAdyacencia(List<Arista> aristas, Map<String, Integer> ciudadIndice,
            int numCiudades) {
        double[][] matriz = new double[numCiudades][numCiudades];

        // Inicializar la matriz con valores infinitos
        for (int i = 0; i < numCiudades; i++) {
            for (int j = 0; j < numCiudades; j++) {
                if (i == j) {
                    matriz[i][j] = 0; // La distancia de una ciudad a sí misma es 0
                } else {
                    matriz[i][j] = Double.POSITIVE_INFINITY; // Infinito para ciudades no conectadas directamente
                }
            }
        }

        // Llenar la matriz con los tiempos de viaje
        for (Arista arista : aristas) {
            int origen = ciudadIndice.get(arista.getOrigen());
            int destino = ciudadIndice.get(arista.getDestino());
            matriz[origen][destino] = arista.getTiempoNormal(); // Usamos el tiempo en clima normal
        }

        return matriz;
    }

    // Implementación del algoritmo de Floyd para encontrar las distancias más
    // cortas
    public static double[][] algoritmoFloyd(double[][] matriz) {
        int n = matriz.length;
        double[][] distancias = new double[n][n];
        siguienteNodo = new int[n][n];

        // Copiar la matriz original
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distancias[i][j] = matriz[i][j];
                // Inicializar la matriz de siguiente nodo
                if (i != j && distancias[i][j] < Double.POSITIVE_INFINITY) {
                    siguienteNodo[i][j] = j;
                } else {
                    siguienteNodo[i][j] = -1;
                }
            }
        }

        // Algoritmo de Floyd
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                        siguienteNodo[i][j] = siguienteNodo[i][k];
                    }
                }
            }
        }

        return distancias;
    }

    // Método para mostrar una matriz
    public static void mostrarMatriz(double[][] matriz, Map<String, Integer> ciudadIndice) {
        // Convertir el mapa de índices a un array para mostrar los nombres de las
        // ciudades
        String[] ciudades = new String[ciudadIndice.size()];
        for (Map.Entry<String, Integer> entry : ciudadIndice.entrySet()) {
            ciudades[entry.getValue()] = entry.getKey();
        }

        // Mostrar los nombres de las ciudades como encabezados de columna
        System.out.print("\t");
        for (String ciudad : ciudades) {
            System.out.print(ciudad + "\t");
        }
        System.out.println();

        // Mostrar la matriz con los nombres de las ciudades como encabezados de fila
        for (int i = 0; i < matriz.length; i++) {
            System.out.print(ciudades[i] + "\t");
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] == Double.POSITIVE_INFINITY) {
                    System.out.print("∞\t");
                } else {
                    System.out.print(matriz[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    // Método para encontrar la ciudad que está en el centro del grafo
    public static String encontrarCiudadCentral(double[][] distancias, Map<String, Integer> ciudadIndice) {
        int n = distancias.length;
        double[] excentricidades = new double[n];

        // Calcular la excentricidad de cada vértice
        for (int i = 0; i < n; i++) {
            double maxDistancia = 0;
            for (int j = 0; j < n; j++) {
                if (distancias[i][j] != Double.POSITIVE_INFINITY && distancias[i][j] > maxDistancia) {
                    maxDistancia = distancias[i][j];
                }
            }
            excentricidades[i] = maxDistancia;
        }

        // Encontrar el vértice con la menor excentricidad (centro del grafo)
        int indiceCentro = 0;
        double minExcentricidad = excentricidades[0];

        for (int i = 1; i < n; i++) {
            if (excentricidades[i] < minExcentricidad) {
                minExcentricidad = excentricidades[i];
                indiceCentro = i;
            }
        }

        // Convertir el índice del centro a nombre de ciudad
        String ciudadCentral = "";
        for (Map.Entry<String, Integer> entry : ciudadIndice.entrySet()) {
            if (entry.getValue() == indiceCentro) {
                ciudadCentral = entry.getKey();
                break;
            }
        }

        return ciudadCentral;
    }

    // Método para encontrar la ruta más corta entre dos ciudades y mostrar las
    // ciudades intermedias
    public static void mostrarRutaMasCorta(String origen, String destino, Map<String, Integer> ciudadIndice,
            double[][] distancias) {
        // Obtener los índices de las ciudades origen y destino
        Integer indiceOrigen = ciudadIndice.get(origen);
        Integer indiceDestino = ciudadIndice.get(destino);

        // Verificar que ambas ciudades existan en el grafo
        if (indiceOrigen == null || indiceDestino == null) {
            System.out.println("Error: Una o ambas ciudades no existen en el grafo.");
            return;
        }

        // Verificar si existe una ruta entre las ciudades
        if (distancias[indiceOrigen][indiceDestino] == Double.POSITIVE_INFINITY) {
            System.out.println("No existe una ruta desde " + origen + " hasta " + destino);
            return;
        }

        // Construir la ruta usando la matriz de siguiente nodo
        List<String> ruta = reconstruirRuta(indiceOrigen, indiceDestino, ciudadIndice);

        // Mostrar la distancia más corta y la ruta
        System.out.println(
                "La ruta más corta de " + origen + " a " + destino + " es: " + distancias[indiceOrigen][indiceDestino]);
        System.out.println("Ruta completa: " + String.join(" -> ", ruta));
    }

    // Método para reconstruir la ruta usando la matriz de siguiente nodo
    public static List<String> reconstruirRuta(int origen, int destino, Map<String, Integer> ciudadIndice) {
        List<String> ruta = new ArrayList<>();

        // Obtener los nombres de las ciudades a partir de los índices
        String[] ciudades = new String[ciudadIndice.size()];
        for (Map.Entry<String, Integer> entry : ciudadIndice.entrySet()) {
            ciudades[entry.getValue()] = entry.getKey();
        }

        // Añadir la ciudad de origen a la ruta
        ruta.add(ciudades[origen]);

        // Si la ruta es directa, solo se añade el destino y se devuelve
        if (siguienteNodo[origen][destino] == -1) {
            ruta.add(ciudades[destino]);
            return ruta;
        }

        // Reconstruir la ruta usando la matriz de siguiente nodo
        int nodoActual = origen;
        while (nodoActual != destino) {
            nodoActual = siguienteNodo[nodoActual][destino];
            if (nodoActual == -1) {
                break; // Para evitar un bucle infinito si no hay camino
            }
            ruta.add(ciudades[nodoActual]);
        }

        return ruta;
    }
}