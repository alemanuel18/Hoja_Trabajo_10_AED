package com.api;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

class Matriz_FuncionamientoTest {
    private List<Arista> aristas;
    private Map<String, Integer> ciudadIndice;
    private double[][] matrizAdyacencia;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba básicos
        aristas = Arrays.asList(
            new Arista("CiudadA", "CiudadB", 1.0, 2.0, 3.0, 4.0),
            new Arista("CiudadB", "CiudadC", 2.0, 3.0, 4.0, 5.0),
            new Arista("CiudadC", "CiudadA", 3.0, 4.0, 5.0, 6.0)
        );
        ciudadIndice = Matriz_Funcionamiento.obtenerCiudades(aristas);
        matrizAdyacencia = Matriz_Funcionamiento.crearMatrizAdyacencia(aristas, ciudadIndice, ciudadIndice.size());
    }

    @Test
    void testLeerArchivoLogistica() {
        try {
            // Crear un archivo de prueba temporal y leerlo
            List<Arista> aristasLeidas = Matriz_Funcionamiento.leerArchivoLogistica("src/test/resources/logistica_test.txt");
            assertNotNull(aristasLeidas);
            assertFalse(aristasLeidas.isEmpty());
        } catch (IOException e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }

    @Test
    void testAgregarArista() {
        List<Arista> nuevasAristas = Matriz_Funcionamiento.agregarArista(
            aristas,
            "CiudadD",
            "CiudadE",
            1.0,
            2.0,
            3.0,
            4.0
        );

        assertNotNull(nuevasAristas);
        assertEquals(4, nuevasAristas.size());
        Arista ultimaArista = nuevasAristas.get(nuevasAristas.size() - 1);
        assertEquals("CiudadD", ultimaArista.getOrigen());
        assertEquals("CiudadE", ultimaArista.getDestino());
    }

    @Test
    void testObtenerCiudades() {
        Map<String, Integer> ciudades = Matriz_Funcionamiento.obtenerCiudades(aristas);
        assertNotNull(ciudades);
        assertEquals(3, ciudades.size());
        assertTrue(ciudades.containsKey("CiudadA"));
        assertTrue(ciudades.containsKey("CiudadB"));
        assertTrue(ciudades.containsKey("CiudadC"));
    }

    @Test
    void testCrearMatrizAdyacencia() {
        double[][] matriz = Matriz_Funcionamiento.crearMatrizAdyacencia(aristas, ciudadIndice, ciudadIndice.size());
        assertNotNull(matriz);
        assertEquals(ciudadIndice.size(), matriz.length);
        assertEquals(ciudadIndice.size(), matriz[0].length);
        
        // Verificar que la diagonal principal sea 0
        for (int i = 0; i < matriz.length; i++) {
            assertEquals(0.0, matriz[i][i]);
        }
    }

    @Test
    void testAlgoritmoFloyd() {
        double[][] distancias = Matriz_Funcionamiento.algoritmoFloyd(matrizAdyacencia);
        assertNotNull(distancias);
        assertEquals(matrizAdyacencia.length, distancias.length);
        
        // Verificar que no haya distancias negativas
        for (double[] fila : distancias) {
            for (double distancia : fila) {
                assertTrue(distancia >= 0 || distancia == Double.POSITIVE_INFINITY);
            }
        }
    }

    @Test
    void testEncontrarCiudadCentral() {
        double[][] distancias = Matriz_Funcionamiento.algoritmoFloyd(matrizAdyacencia);
        String ciudadCentral = Matriz_Funcionamiento.encontrarCiudadCentral(distancias, ciudadIndice);
        
        assertNotNull(ciudadCentral);
        assertTrue(ciudadIndice.containsKey(ciudadCentral));
    }

    @Test
    void testMostrarRutaMasCorta() {
        // Preparar datos
        double[][] distancias = Matriz_Funcionamiento.algoritmoFloyd(matrizAdyacencia);
        
        // Verificar ruta existente
        Matriz_Funcionamiento.mostrarRutaMasCorta("CiudadA", "CiudadB", ciudadIndice, distancias);
        // Como mostrarRutaMasCorta imprime en consola, solo verificamos que no lance excepciones
        
        // Verificar ruta no existente
        Matriz_Funcionamiento.mostrarRutaMasCorta("CiudadA", "CiudadNoExistente", ciudadIndice, distancias);
    }

    @Test
    void testReconstruirRuta() {
        // Primero ejecutar Floyd para inicializar la matriz de siguiente nodo
        Matriz_Funcionamiento.algoritmoFloyd(matrizAdyacencia);
        
        // Obtener índices de las ciudades
        int origen = ciudadIndice.get("CiudadA");
        int destino = ciudadIndice.get("CiudadB");
        
        // Obtener la ruta
        List<String> ruta = Matriz_Funcionamiento.reconstruirRuta(origen, destino, ciudadIndice);
        
        assertNotNull(ruta);
        assertFalse(ruta.isEmpty());
        assertEquals("CiudadA", ruta.get(0));
    }
}
