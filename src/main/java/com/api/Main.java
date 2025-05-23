package com.api;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            
            // Inicializar el grafo desde el archivo
            Matriz_Funcionamiento.inicializarGrafo("logistica.txt");
            System.out.println("Grafo inicializado correctamente.");

            while (true) {
                System.out.println("\n*****Análisis de Rutas ver 2.0*****");
                System.out.println("Clima actual: " + Matriz_Funcionamiento.getClimaActual());
                System.out.println("""
                    1. Ruta más corta entre dos ciudades
                    2. Mostrar ciudad central
                    3. Modificar grafo
                    4. Mostrar matriz de adyacencia
                    5. Finalizar programa
                    Elija una opción:
                    """);

                int opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1 -> {
                        System.out.print("Ciudad origen: ");
                        String origen = sc.nextLine();
                        System.out.print("Ciudad destino: ");
                        String destino = sc.nextLine();
                        Matriz_Funcionamiento.mostrarRutaCorta(origen, destino);
                    }
                    
                    case 2 -> {
                        String ciudadCentral = Matriz_Funcionamiento.getCiudadCentral();
                        System.out.println("Ciudad central: " + ciudadCentral);
                    }

                    case 3 -> {
                        System.out.println("""
                            Modificaciones disponibles:
                            a) Interrupción de tráfico entre ciudades
                            b) Establecer nueva conexión entre ciudades
                            c) Cambiar clima global
                            d) Establecer clima específico entre dos ciudades
                            Elija una opción:""");
                        
                        String mod = sc.nextLine().toLowerCase();
                        
                        switch (mod) {
                            case "a" -> {
                                System.out.print("Ciudad origen (interrupción): ");
                                String ori = sc.nextLine();
                                System.out.print("Ciudad destino (interrupción): ");
                                String des = sc.nextLine();
                                Matriz_Funcionamiento.agregarInterrupcionTrafico(ori, des);
                                
                                // Mostrar nueva ciudad central después de la modificación
                                System.out.println("Nueva ciudad central: " + Matriz_Funcionamiento.getCiudadCentral());
                            }
                            
                            case "b" -> {
                                System.out.print("Ciudad origen (nueva conexión): ");
                                String ori = sc.nextLine();
                                System.out.print("Ciudad destino (nueva conexión): ");
                                String des = sc.nextLine();
                                System.out.print("Tiempo normal: ");
                                double tN = Double.parseDouble(sc.nextLine());
                                System.out.print("Tiempo lluvia: ");
                                double tL = Double.parseDouble(sc.nextLine());
                                System.out.print("Tiempo nieve: ");
                                double tNv = Double.parseDouble(sc.nextLine());
                                System.out.print("Tiempo tormenta: ");
                                double tT = Double.parseDouble(sc.nextLine());
                                
                                Matriz_Funcionamiento.establecerNuevaConexion(ori, des, tN, tL, tNv, tT);
                                
                                // Mostrar nueva ciudad central después de la modificación
                                System.out.println("Nueva ciudad central: " + Matriz_Funcionamiento.getCiudadCentral());
                            }
                            
                            case "c" -> {
                                System.out.print("Ingrese clima global (normal, lluvia, nieve, tormenta): ");
                                String clima = sc.nextLine();
                                
                                if (clima.matches("(?i)(normal|lluvia|nieve|tormenta)")) {
                                    Matriz_Funcionamiento.cambiarClima(clima.toLowerCase());
                                    System.out.println("Clima global cambiado a: " + clima);
                                    
                                    // Mostrar nueva ciudad central después del cambio de clima
                                    System.out.println("Nueva ciudad central: " + Matriz_Funcionamiento.getCiudadCentral());
                                } else {
                                    System.out.println("Clima inválido. Use: normal, lluvia, nieve o tormenta");
                                }
                            }
                            
                            case "d" -> {
                                System.out.print("Ciudad origen: ");
                                String ori = sc.nextLine();
                                System.out.print("Ciudad destino: ");
                                String des = sc.nextLine();
                                System.out.print("Clima específico (normal, lluvia, nieve, tormenta): ");
                                String climaEsp = sc.nextLine();
                                
                                if (climaEsp.matches("(?i)(normal|lluvia|nieve|tormenta)")) {
                                    Matriz_Funcionamiento.establecerClimaEspecifico(ori, des, climaEsp.toLowerCase());
                                    
                                    // Mostrar nueva ciudad central después de la modificación
                                    System.out.println("Nueva ciudad central: " + Matriz_Funcionamiento.getCiudadCentral());
                                } else {
                                    System.out.println("Clima inválido. Use: normal, lluvia, nieve o tormenta");
                                }
                            }
                            
                            default -> System.out.println("Opción inválida.");
                        }
                    }

                    case 4 -> {
                        System.out.println("\nMatriz de adyacencia actual:");
                        Matriz_Funcionamiento.mostrarMatrizActual();
                    }

                    case 5 -> {
                        System.out.println("Finalizando programa...");
                        sc.close();
                        return;
                    }

                    default -> System.out.println("Opción inválida. Por favor seleccione una opción del 1 al 5.");
                }
                
                // Pausa para que el usuario pueda leer los resultados
                System.out.println("\nPresione Enter para continuar...");
                sc.nextLine();
            }
            
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            System.out.println("Asegúrese de que el archivo 'logistica.txt' existe en el directorio del proyecto.");
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un número válido.");
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }
}