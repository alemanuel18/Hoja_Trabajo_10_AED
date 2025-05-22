package com.api;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            // Carga del archivo .txt
            AdministradorGrafo grafo = new AdministradorGrafo("logistica.txt");

            while (true) {
                System.out.println("*****Analicis de Rutas ver 1.4*****");
                System.out.println("""
                    1. Ruta más corta entre dos ciudades
                    2. Mostrar ciudad central
                    3. Modificar grafo
                    4. Mostrar matriz de adyacencia
                    5. Salir
                    Elija una opción:
                    """);

                int opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1 -> {
                        System.out.print("Ciudad origen: ");
                        String origen = sc.nextLine();
                        System.out.print("Ciudad destino: ");
                        String destino = sc.nextLine();
                        grafo.mostrarRuta(origen, destino);
                    }
                    case 2 -> System.out.println("Ciudad central: " + grafo.getCiudadCentral());

                    case 3 -> {
                        System.out.println("""
                            a) Eliminar ruta
                            b) Agregar nueva ruta
                            c) Cambiar clima 
                            Elija una opción:""");
                        String mod = sc.nextLine();
                        switch (mod.toLowerCase()) {
                            case "a" -> {
                                System.out.print("Ciudad origen: ");
                                String ori = sc.nextLine();
                                System.out.print("Ciudad destino: ");
                                String des = sc.nextLine();
                                grafo.eliminarConexion(ori, des);
                            }
                            case "b" -> {
                                System.out.print("Ciudad origen: ");
                                String ori = sc.nextLine();
                                System.out.print("Ciudad destino: ");
                                String des = sc.nextLine();
                                System.out.print("Tiempo normal: ");
                                double tN = Double.parseDouble(sc.nextLine());
                                System.out.print("Tiempo lluvia: ");
                                double tL = Double.parseDouble(sc.nextLine());
                                System.out.print("Tiempo nieve: ");
                                double tNv = Double.parseDouble(sc.nextLine());
                                System.out.print("Tiempo tormenta: ");
                                double tT = Double.parseDouble(sc.nextLine());
                                grafo.agregarConexion(ori, des, tN, tL, tNv, tT);
                            }
                            case "c" -> {
                                System.out.print("Ingrese clima (normal, lluvia, nieve, tormenta): ");
                                String clima = sc.nextLine();
                                grafo.cambiarClima(clima);
                            }
                            default -> System.out.println("Opción inválida.");
                        }
                    }

                    case 4 -> grafo.mostrarMatriz();

                    case 5 -> {
                        System.out.println("Finalizando programa...");
                        return;
                    }

                    default -> System.out.println("Opción inválida.");
                }
            }
        // Deteccion de error al momento de leer el archivo .txt
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }
}
